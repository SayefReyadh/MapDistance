package com.example.sayefreyadh.mapdistance.System;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sayefreyadh.mapdistance.Models.MapDirection;
import com.example.sayefreyadh.mapdistance.Models.Route;
import com.example.sayefreyadh.mapdistance.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MapsActivityTest extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView startingLocationAutoCompleteTextView;
    private AutoCompleteTextView endingLocationAutoCompleteTextView;

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

        startingLocationAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.startingLocationAutoCompleteTextView);
        endingLocationAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.endingLocationAutoCompleteTextView);

        calculateButton = (Button) findViewById(R.id.calculateButton);

        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDistanceBetweenPlaces();
            }
        });
    }

    private void calculateDistanceBetweenPlaces()
    {
        String startingLocationString = startingLocationAutoCompleteTextView.getText().toString();
        String endingLocationString = endingLocationAutoCompleteTextView.getText().toString();

        if (startingLocationString.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endingLocationString.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }


        try {

            MapDirection mapDirection = new MapDirection(startingLocationString , endingLocationString);
            mapDirection.execute();
            showPath(mapDirection.getRoutesBetweenPlace());
            //onDirectionFinderStart();
            ///Direction direction = new Direction(this, startingLocationString, endingLocationString);
            ///direction.execute();
            //onDirectionFinderSuccess(direction.getRoutesBetweenPlace());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    public void showPath(ArrayList<Route> routes)
    {
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
            for(LatLng l : latlng)
            {
                op.add(l);
            }

            mMap.addPolyline(op);
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
        LatLng sydney = new LatLng(23.693800, 90.455241);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Sayef"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
        mMap.setMyLocationEnabled(true);
        ArrayList<LatLng> latlng = new ArrayList<>();
        latlng.add(new LatLng(23.693800, 90.455241));
        latlng.add(new LatLng(23.693984, 90.455236));
        latlng.add(new LatLng(23.694171, 90.455182));
        PolylineOptions op = new PolylineOptions();
        for(LatLng l : latlng)
        {
            op.add(l);
        }
        mMap.addPolyline(op);
    }
}
