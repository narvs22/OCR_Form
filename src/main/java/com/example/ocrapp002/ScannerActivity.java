package com.example.ocrapp002;

import static android.Manifest.permission.CAMERA;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.ocrapp002.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;



import org.json.JSONArray;
import org.json.JSONObject;



import java.io.FileNotFoundException;

import java.io.IOException;

import okhttp3.Address;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.FormBody;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.OutputStream;
import java.util.Random;


public class ScannerActivity extends AppCompatActivity {
private final OkHttpClient okHttpClient=new OkHttpClient();
private EditText convertedText;
private Uri filepath;
private final int PICK_IMAGE_REQUEST=1;
private ImageView captureIV;
private Button snapBtn;
private Button selectBtn;
private Button uploadBtn;
private Bitmap imageBitmap;

StorageReference storageReference;
FirebaseStorage firebaseStorage;

static final int REQUEST_IMAGE_CAPTURE=1;
    ActivityMainBinding binding;
    ActivityResultLauncher<String> cropImage;
    public ScannerActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result-> {
            Intent intent = new Intent(ScannerActivity.this.getApplicationContext(), UcropperActivity.class);
            intent.putExtra("SendImageData", result.toString());
            startActivityForResult(intent, 1);
        });
        setContentView(R.layout.activity_scanner);
        snapBtn=findViewById(R.id.idBtnSnap);
        captureIV= findViewById(R.id.idIVCaptureImage);
        convertedText=findViewById(R.id.convertedText);
        selectBtn=findViewById(R.id.idBtnSelect);
        uploadBtn=findViewById(R.id.idBtnUpload);

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();


        selectBtn.setOnClickListener(new View.OnClickListener() {// response of select btn
    @Override
    public void onClick(View v) {
        selectImage();
    }
});
uploadBtn.setOnClickListener(new View.OnClickListener() { //response of upload btn
    @Override
    public void onClick(View v) {
        uploadImage();

    }
});
        snapBtn.setOnClickListener(new View.OnClickListener() {//response of snap btn
            @Override
            public void onClick(View v){
if(checkPermissions()){
    captureImage();


}else{
    requestPermission();
}
            }
        });
    }

    private boolean checkPermissions(){//function to check if the user has access to the camera
        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        return cameraPermission== PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission(){//function to ask the user to access the camera
        int PERMISSION_CODE=200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);
    }
    private void selectImage(){// function to choose an image from gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image from here"),PICK_IMAGE_REQUEST);

    }
private void captureImage(){// function to take a picture
Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action to take picture is initiated
if(takePicture.resolveActivity(getPackageManager())!=null){
    startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);//function that allows the user
    // take the picture based on the input parameters

}

}
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           @NonNull int[] grantResults) {//After user asks for
        // permission, if the user grants permission the capture image begins. if the user denied
        // permission, a message stating that permission is denied is shown and the capture function
        // does not occur
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraPermission=grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this,"Permission Granted..",Toast.LENGTH_SHORT).show();
                captureImage();
                //cropImage.launch("image/*");
            }
            else{
                Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            String result = data.getStringExtra("");
            filepath=data.getData(); //uri data is collected from the gallery image
            if (result!=null){
                filepath = Uri.parse(result);//string result which gets data is parsed(converted
                // to differ data type) if condition is met
            }

            try{
                imageBitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);//uri
                // data is converted to bitmap
                captureIV.setImageBitmap(imageBitmap);//bitmap produced is set to the image view for
                // viewing purposes by the user
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            detectText();
        }
        else if (requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
            Bundle extras=data.getExtras();//data is obtained from image
            imageBitmap=(Bitmap) extras.get("data");//imageBitmap is set to the data obtained in the
            // form of a bitmap

            captureIV.setImageBitmap(imageBitmap);//bitmap produced is set to the image view for
            // viewing purposes by the user
           detectText();//detects the text from the bitmap
        }
    }

    private void detectText(){
        InputImage image=InputImage.fromBitmap(imageBitmap,0);

        //TextRecognition client for performing optical character recognition(OCR) on an input image
        // to detect latin-based characters. created via
        // TextRecognition.getClient(TextRecognizerOptionsInterface)
        TextRecognizer recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        //Task is just a unit of work
        Task<Text> result=recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text){
                StringBuilder result= new StringBuilder(); //modifiable sequence of characters

                for(Text.TextBlock block: text.getTextBlocks()){

                    String blockText=block.getText(); //text with box is set to the string blockText

                    Point[] blockCornerPoint=block.getCornerPoints();//getting coordinates of the block

                    Rect blockFrame=block.getBoundingBox(); //A Rectangle specifies an area in a
                    // coordinate space that is enclosed by the Rectangle object's upper-left point
                    // (x,y) in the coordinate space, its width, and its height
                    for(Text.Line line: block.getLines()){
                        String lineTExt= line.getText();
                        Point[] lineCornerPoint=line.getCornerPoints();
                        Rect linRect=line.getBoundingBox();
                        for(Text.Element element:line.getElements()){
                            String elementText= element.getText();
                            result.append(elementText);
                        }
                        convertedText.setText(blockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScannerActivity.this, "Fail to detect text from image..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage(){

        if(filepath!=null){
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref =storageReference.child("images/image");
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(ScannerActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ScannerActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100*snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                    progressDialog.setMessage("uploaded"+ (int) progress + "%");
                }
            });
            StorageReference storageReference=FirebaseStorage.getInstance().getReference();
            StorageReference dataRef=storageReference.child("images/image");
            dataRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toast.makeText(ScannerActivity.this,uri.toString(),Toast.LENGTH_SHORT).show();
                    try {
                        sendPost(uri.toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private  void sendPost(String imageUrl)throws Exception{
        RequestBody formBody= new FormBody.Builder()
                .add("language", "eng")
                .add("isOverlayRequired", "false")
                .add("url", imageUrl)
                .add("iscreatesearchablepdf", "false")
                .add("issearchablepdfhidetextlayer", "false")
                .build();
        okhttp3.Request request=new Request.Builder()
                .url("https://api.ocr.space.com/parse/image")
                .addHeader("User-Agent", "OhHttp Bot")
                .addHeader("apikey","K83612094488957")
                .post(formBody)
                .build();

        try(Response response=okHttpClient.newCall(request).execute()){
            if(!response.isSuccessful()) {throw new IOException("Unexpected code"+ response);}

            String res=response.body().string();
            JSONObject obj = new JSONObject(res);
            JSONArray jsonArray=(JSONArray) obj.get("Parsed Results");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                convertedText.setText(jsonObject.get("ParsedText").toString());
            }
        }
    }
}