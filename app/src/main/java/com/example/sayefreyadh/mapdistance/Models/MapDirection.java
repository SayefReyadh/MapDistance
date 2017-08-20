package com.example.sayefreyadh.mapdistance.Models;

import android.os.AsyncTask;

import com.example.sayefreyadh.mapdistance.IDistanceCalculatedListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SayefReyadh on 8/19/2017.
 */

public class MapDirection {

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyBv_ysNgct58W5bvgL1QNIr3KVzotYPGMA";

    //    interface
    private IDistanceCalculatedListener mListerner;

    private String startingLocationString;
    private String endingLocationString;
    private ArrayList<Route> routesBetweenPlace;


    private LatLng startingLocation;
    private LatLng endingLocation;

    public MapDirection(String startingLocationString, String endingLocationString, IDistanceCalculatedListener mListerner) {
        this.startingLocationString = startingLocationString;
        this.endingLocationString = endingLocationString;
        this.mListerner = mListerner;
    }

    public MapDirection(IDistanceCalculatedListener listerner, LatLng startingLocation, LatLng endingLocation) {
        mListerner = listerner;
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlStartingLocationString = URLEncoder.encode(startingLocationString, "utf-8");
        String urlEndingLocationString = URLEncoder.encode(endingLocationString, "utf-8");

        return DIRECTION_URL_API + "origin=" + urlStartingLocationString + "&destination=" + urlEndingLocationString + "&key=" + GOOGLE_API_KEY;
    }

    private String createUrlLatLng() throws UnsupportedEncodingException {
//        making the url with lat and lng data
        String urlStartingLocationString = URLEncoder.encode(startingLocation.latitude + "," + startingLocation.longitude, "utf-8");
        String urlEndingLocationString = URLEncoder.encode(endingLocation.latitude + "," + endingLocation.longitude, "utf-8");

        return DIRECTION_URL_API + "origin=" + urlStartingLocationString + "&destination=" + urlEndingLocationString + "&key=" + GOOGLE_API_KEY;
    }

    public void execute() throws UnsupportedEncodingException {
        //listener.onDirectionFinderStart();
        DownloadJSONData ob = new DownloadJSONData();
        ob.execute(createUrl());
        System.out.println("URL : " + createUrl());
        System.out.println("TAGSJSON : " + ob.getJsonData());
        ///json data is not coming???

    }

    public void executeLatLng() throws UnsupportedEncodingException {
        //listener.onDirectionFinderStart();
        DownloadJSONData ob = new DownloadJSONData();
        ob.execute(createUrlLatLng());
        System.out.println("URL : " + createUrlLatLng());
        System.out.println("TAGSJSON : " + ob.getJsonData());
        ///json data is not coming???

    }

    public ArrayList<Route> getRoutesBetweenPlace() {
        return routesBetweenPlace;
    }

    public void setRoutesBetweenPlace(ArrayList<Route> routesBetweenPlace) {
        this.routesBetweenPlace = routesBetweenPlace;
    }

    private void parseJSon(String data) throws JSONException{
        if (data == null)
            return;

        ArrayList<Route> routes = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            route.points = decodePolyLine(overview_polylineJson.getString("points"));

            routes.add(route);
        }

        for(Route r : routes)
        {
            r.printDetails();
            System.out.println("LATLNG POINTS");
            for (LatLng l : r.points)
            {
                System.out.println(l.latitude + " " + l.longitude);
            }

        }

        setRoutesBetweenPlace(routes);
        //this.routesBetweenPlace = routes;


//       informing the listener about the route
        mListerner.setLocationInMap(getRoutesBetweenPlace());

    }

    private ArrayList<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        ArrayList<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

    private class DownloadJSONData extends AsyncTask<String, Void, String> {

        private String jsonData;

        public String getJsonData() {
            return jsonData;
        }

        public void setJsonData(String jsonData) {
            this.jsonData = jsonData;
        }

        @Override
        protected String doInBackground(String... params) {

            String link = params[0];
            System.out.println("JSON " + link);
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                System.out.println("JSON" + buffer.toString());
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                setJsonData(res);
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
