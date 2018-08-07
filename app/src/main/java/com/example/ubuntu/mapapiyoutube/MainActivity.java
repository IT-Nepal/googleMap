package com.example.ubuntu.mapapiyoutube;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001; //final;it's value can not change

    // inorder to use google map api they need to have a certain version, while app running we need to check
//    they have that correct version if they won't then app won't work.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(isServicesOK()){
            init(); //if ok then button get initialized
        }
    }
    public void init(){
        Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }


    public boolean isServicesOK() {
        Log.d(TAG, "is service ok: checking google service version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map request.
            Log.d(TAG, "is service ok: google service is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can solved it.
            Log.d(TAG, "is service ok: but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "can't create map request", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

}
