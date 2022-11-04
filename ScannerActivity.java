package com.example.ocrapp002;

import static android.Manifest.permission.CAMERA;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;




public class ScannerActivity extends AppCompatActivity {

private ImageView captureIV;
private Button snapBtn;
private Bitmap imageBitmap;


static final int REQUEST_IMAGE_CAPTURE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        snapBtn=findViewById(R.id.idBtnSnap);
        captureIV=findViewById(R.id.idIVCaptureImage); //function to keep image on screen when captured


        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(checkPermissions()){
    captureImage();

}else{
    requestPermission();
}
            }
        });
    }
    private boolean checkPermissions(){
        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        return cameraPermission== PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission(){
        int PERMISSION_CODE=200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);
    }
private void captureImage(){ //function to capture image
Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
if(takePicture.resolveActivity(getPackageManager())!=null){
    startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);

}
}
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraPermission=grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this,"Permission Granted..",Toast.LENGTH_SHORT).show();
                captureImage();
            }
            else{
                Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE &&resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            imageBitmap=(Bitmap) extras.get("data");


            captureIV.setImageBitmap(imageBitmap);


        }
    }





}