package com.example.ocrapp002;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//extends a class (indicates that a class is inherited from another class)
//AppCompatActivity is the base class for activities with the support library action bar features

public class MainActivity extends AppCompatActivity {

    private Button continueBtn;

    @Override //indicates that a method declaration is intended to override a method
    // declaration in a superclass.

    protected void onCreate(Bundle savedInstanceState) {//perform one-time initialization such as
        //creating the user interface.it takes one parameter that is saved by the onSaveInstanceState

        super.onCreate(savedInstanceState); //allows the code to  compile
        setContentView(R.layout.activity_main); // method of activity class. It shows layout on
        // screen.

        continueBtn=findViewById(R.id.idBtnContinue);//method that finds the View by the ID it is
        // given

        continueBtn.setOnClickListener(new View.OnClickListener() {//response of the button that
            // when clicked.page is changed to the Scanner activity class user interface
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,ScannerActivity.class);
                startActivity(i);
            }
        });
    }
}