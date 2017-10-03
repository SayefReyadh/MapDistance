package com.example.sayefreyadh.mapdistance.System;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.sayefreyadh.mapdistance.DistancePackage.DistanceCalculator;
import com.example.sayefreyadh.mapdistance.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RandomPathActivity extends AppCompatActivity {
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_path);
        editText = (EditText) findViewById(R.id.editText);
        LatLng latLng3 = new LatLng(-33.729752, 150.836090);
        LatLng latLng = new LatLng(-33, 151.2);
        LatLng latLng2 = new LatLng(-33.737885, 151.235260);
        DistanceCalculator ob = new DistanceCalculator();
        double d = ob.calculationByDistance(latLng3 , latLng);
        editText.setText(d + "\n");
        d = ob.calculationByDistance(latLng , latLng2);
        editText.append(d + "\n");
        ArrayList<LatLng> points = new ArrayList<>();

        points.add(latLng3);
        points.add(latLng);
        points.add(latLng2);

        d = ob.totalDistance(points);
        editText.append(d + "\n");
    }
}
