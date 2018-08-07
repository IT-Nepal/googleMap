package com.example.ubuntu.mapapiyoutube;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this,"Map is ready",Toast.LENGTH_LONG).show();
        mMap = googleMap;

        Log.d(TAG,"map is ready");
    }
    String TAG = "MapsActivity";
    private static final String  FineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CourseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LocationPermissionRequestCode = 123;
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
    }
//   SupportMapFragment:- A Map component in an app.
// This fragment is the simplest way to place a map in an application.
// It's a wrapper around a view of a map to automatically handle the necessary life cycle needs.
// Being a fragment, this component can be added to an activity's layout file simply with the XML below.
    public void initMap() {
        Log.d(TAG,"initMap");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(MapsActivity.this);
//            getMapAsync(OnMapReadyCallback). This class automatically initializes the maps system
//         and the view.


        //explicitly check permission after marshmallow

    }
    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission");
    String permission[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//    coarse location is for network provider's location
//    and fine location is for both gps provider and network location provider.
        if(ContextCompat.checkSelfPermission(getApplicationContext(),FineLocation)
                == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(),CourseLocation)
            == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
                Log.d(TAG,"mLocationPermissionGranted is true");
            } else{
                //ask for permission
                Log.d(TAG,"ask for permission");
                ActivityCompat.requestPermissions(this, permission,LocationPermissionRequestCode);
            }

        }else{
            Log.d(TAG,"ask for permission1");
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
                        Log.d(TAG,"ask for permission and didn't get permission");
                        return ;
                    }
                }
                mLocationPermissionGranted = true;
                //initalize our map
                initMap();
                Log.d(TAG,"initmap");
            }
        }
    }
}
