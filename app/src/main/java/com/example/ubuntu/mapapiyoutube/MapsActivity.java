package com.example.ubuntu.mapapiyoutube;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this,"Map is ready",Toast.LENGTH_LONG).show();
        mMap = googleMap;

        if(mLocationPermissionGranted){
            getDeviceLocation();
        }

        Log.d(TAG,"map is ready");
    }
    String TAG = "MapsActivity";
    private static final String  FineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CourseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LocationPermissionRequestCode = 123;
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mfusedLocationProviderclient; //to get device current location
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
return;
        }mMap.setMyLocationEnabled(true);
    }

    public void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation : getting the device current location");
        mfusedLocationProviderclient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                Task location = mfusedLocationProviderclient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"found location");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()
                            ),DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG,"not found location");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch(SecurityException e){
        Log.e(TAG,"getDeviceLocation: securityException:" + e.getMessage());
        }
    }
private void moveCamera(LatLng latLng, float zoom){
    Log.d(TAG, "moveCamera: moving the camera to : lat: "+ latLng.latitude + ", lng: "+ latLng.longitude);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
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
                initMap();
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
