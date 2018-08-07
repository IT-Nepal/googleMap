package com.example.ubuntu.mapapiyoutube;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapsActivity extends AppCompatActivity {

    private static final String  FineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CourseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LocationPermissionRequestCode = 123;
    private Boolean mLocationPermissionGranted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    }

    //explicitly check permission after marshmallow
    private void getLocationPermission(){
    String permission[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//    coarse location is for network provider's location
//    and fine location is for both gps provider and network location provider.
        if(ContextCompat.checkSelfPermission(getApplicationContext(),FineLocation)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(),CourseLocation)
            == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
            } else{
                //ask for permission
                ActivityCompat.requestPermissions(this, permission,LocationPermissionRequestCode);
            }

        }else{
            ActivityCompat.requestPermissions(this, permission,LocationPermissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch(requestCode){
            case LocationPermissionRequestCode:
            if(grantResults.length > 0){
                for(int i = 0 ; i<grantResults.length; i++){
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        mLocationPermissionGranted = false;
                        return ;
                    }
                }
                mLocationPermissionGranted = true;
                //initalize our map
            }
        }
    }
}
