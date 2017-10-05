package com.example.sayefreyadh.mapdistance.DistancePackage;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by SayefReyadh on 10/3/2017.
 */

public class DistanceCalculator {
    private final double Radius = 6371;

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }


    public double totalDistance(ArrayList<LatLng> points)
    {
        double totalDist = 0;

        for(int i = 0 ; i < points.size() - 1 ; i++)
        {
            totalDist += calculationByDistance(points.get(i) , points.get(i+1));
        }
        return totalDist;
    }



}
