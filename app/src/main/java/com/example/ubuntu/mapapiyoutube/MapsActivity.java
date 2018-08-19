package com.example.ubuntu.mapapiyoutube;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this,"Map is ready",Toast.LENGTH_LONG).show();
        mMap = googleMap;

        if(mLocationPermissionGranted){
            getDeviceLocation();
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }mMap.setMyLocationEnabled(true); // show blue color in current location
            mMap.getUiSettings().setMyLocationButtonEnabled(false); //it hides show current location button


            init();
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

    //widgets
    private EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchText = findViewById(R.id.et_search);

        getLocationPermission();


    }
    //reason for init(), when we click enter button then it will work as enter, instead of moving to the
//        nextline;


    //            IME_ACTION_SEARCH
//            Bits of IME_MASK_ACTION: the action key performs a "search" operation,
// taking the user to the results of searching for the text they have typed (in whatever context is appropriate).

//            	IME_ACTION_DONE
//Bits of IME_MASK_ACTION: the action key performs a "done" operation,
// typically meaning there is nothing more to input and the IME will be closed.

//            KeyEvent.ACTION_DOWN
//   Each key press is described by a sequence of key events. A key press starts with a key event with ACTION_DOWN.
// If the key is held sufficiently long that it repeats,
// then the initial down is followed additional key events with ACTION_DOWN and a non-zero value for getRepeatCount().


    //            KEYCODE_ENTER
//Key code constant: Enter key.
    private void init(){
        Log.d(TAG,"init:initialization");
// searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//     @Override
//     public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if(actionId == EditorInfo.IME_ACTION_DONE
//                || actionId == EditorInfo.IME_ACTION_SEARCH
//                || event.getAction()== KeyEvent.ACTION_DOWN
//                || event.getAction()== KeyEvent.KEYCODE_ENTER){
//
            geolocate();
//        }
//
//         return false;
//     }
// });


    }

//    GeoCoder
//    A class for handling geocoding and reverse geocoding.
// Geocoding is the process of transforming a street address or other description of a location into a (latitude, longitude) coordinate.
// Reverse geocoding is the process of transforming a (latitude, longitude) coordinate into a (partial) address.
    private void geolocate(){
        Log.d(TAG, "geoLocate:geolocating");
        String searchString = searchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();

        try{
        list = geocoder.getFromLocationName(searchString,1); //1 for maximum result; it gives list of addresses
                                            //it takes searchString as location name and find its longtitude and latitude.
        }catch(IOException e){
        Log.e(TAG,"geoLocate:IOException" + e.getMessage());
        }
        if(list.size() > 0){
            Address address = list.get(0);
//
  Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG,"geoLocation: found a location" + address.toString());
        }
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
