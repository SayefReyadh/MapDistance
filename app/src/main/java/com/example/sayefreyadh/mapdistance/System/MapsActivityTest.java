package com.example.sayefreyadh.mapdistance.System;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sayefreyadh.mapdistance.IDistanceCalculatedListener;
import com.example.sayefreyadh.mapdistance.Models.MapDirection;
import com.example.sayefreyadh.mapdistance.Models.Route;
import com.example.sayefreyadh.mapdistance.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MapsActivityTest extends FragmentActivity implements OnMapReadyCallback,
        // for listening when the map is ready to update the route between source and destination
        IDistanceCalculatedListener,
        // for listening the destination location selected by user in google autocomplete place api
        PlaceSelectionListener {

    private GoogleMap mMap;
//    private AutoCompleteTextView startingLocationAutoCompleteTextView;
//    private AutoCompleteTextView endingLocationAutoCompleteTextView;

    //    latlng for user and destination
    private LatLng sourceLocation, destinationLocation;

    private Button calculateButton;
    private TextView distanceTextView;
    private TextView timeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        initializing the autocomplete place fragment
        initPlaceAutoComplete();

//        startingLocationAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.startingLocationAutoCompleteTextView);
//        endingLocationAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.endingLocationAutoCompleteTextView);

        calculateButton = (Button) findViewById(R.id.calculateButton);

        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDistanceBetweenPlaces();
            }
        });

//        at first disabling the button so user can't request for destination before selecting a destination
        calculateButton.setEnabled(false);
    }

    private void initPlaceAutoComplete() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint("Search Destination");

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);

    }


    /*
        In this method we are going to calculate the distance and time between two location
        The 1st location will be user's own locaiton
        The 2nd will be the destination
        Both of them are lat and lng

     */
    private void calculateDistanceBetweenPlaces() {
        try {
            new MapDirection(this, sourceLocation, destinationLocation)
                    .executeLatLng();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

//    private void calculateDistanceBetweenPlaces()
//    {
//        String startingLocationString = startingLocationAutoCompleteTextView.getText().toString();
//        String endingLocationString = endingLocationAutoCompleteTextView.getText().toString();
//
//        if (startingLocationString.isEmpty()) {
//            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (endingLocationString.isEmpty()) {
//            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        try {
//
//            MapDirection mapDirection = new MapDirection(startingLocationString, endingLocationString, this);
//            mapDirection.execute();
////            showPath(mapDirection.getRoutesBetweenPlace());
//            //onDirectionFinderStart();
//            ///Direction direction = new Direction(this, startingLocationString, endingLocationString);
//            ///direction.execute();
//            //onDirectionFinderSuccess(direction.getRoutesBetweenPlace());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
//    }


    public void showPath(ArrayList<Route> routes)
    {
//        if map is not ready or map is not initialized then do nothing, otherwise mMap will throw null exception
        if (mMap == null)
            return;

//        removing all markers
        mMap.clear();

        for(Route route : routes)
        {
            distanceTextView.setText(route.distance.text);
            timeTextView.setText(route.duration.text);

            ArrayList<LatLng> latlng = route.points;
            PolylineOptions op = new PolylineOptions()
                    .color(Color.BLUE)
                    .width(10);

            ///the problem seems to be here while updating the map the app crashes
            /// sayef last checked 19 - 8 - 17 2 : 40 PM

            /****************

             Shaafi-20-08-2017
             the map was crashing because the locations was being fetched in a async task,
             which takes time and is done in background, but the main UI is working instantly
             and looking for data which is not present at the time. So it gets a null.

             Rigth now the show map method is being called through a interface, as when the data
             is received through the async task, it then calls the interface function and the function
             then calls show map with the data that has been received from google json data

             ***************/

            for(LatLng l : latlng)
            {
                op.add(l);
            }

            mMap.addPolyline(op);

            mMap.addMarker(new MarkerOptions().position(latlng.get(0)).title("source position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng.get(0), 12f));


            mMap.addMarker(new MarkerOptions().position(latlng.get(latlng.size() - 1)).title("destination position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng.get(latlng.size() - 1), 13f));
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        sourceLocation = new LatLng(23.693800, 90.455241);
        mMap.addMarker(new MarkerOptions().position(sourceLocation).title("user source"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourceLocation, 12f));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    @Override
    public void setLocationInMap(ArrayList<Route> routesBetweenPlace) {
        showPath(routesBetweenPlace);
    }

    @Override
    public void onPlaceSelected(Place place) {
        if (place != null) {
            destinationLocation = place.getLatLng();

            calculateButton.setEnabled(true);
        }
    }

    @Override
    public void onError(Status status) {

    }
}
