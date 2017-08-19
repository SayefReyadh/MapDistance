package com.example.sayefreyadh.mapdistance;

import com.example.sayefreyadh.mapdistance.Models.Route;

import java.util.ArrayList;

/**
 * Created by Asif Imtiaz Shaafi on 08, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public interface IDistanceCalculatedListener {

    //    this method will be invoked when the async task of downloading the json data and parsing is done
    void setLocationInMap(ArrayList<Route> routesBetweenPlace);

}
