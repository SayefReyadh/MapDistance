package com.example.sayefreyadh.mapdistance.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by SayefReyadh on 8/17/2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public ArrayList<LatLng> points;

    public void printDetails()
    {
        System.out.println("TAGROUTEDETAILS ::: distance = " + distance.text + " " + distance.value +
                ", duration = " + duration.text + " " + duration.value +
                ", endAddress = '" + endAddress +
                ", endLocation = " + endLocation +
                ", startAddress = '" + startAddress +
                ", startLocation = " + startLocation);
    }


}
