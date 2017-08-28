package util;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/** HTTP GET request for tha map, in order to find the exact location of the residence **/
public class RestCalls {
    public static LatLng findCoordinates(String address, String city, String country) {
        double lat, lng;
        lat = 0.0;
        lng = 0.0;
        ArrayList<String> paramNames = new ArrayList<>();
        ArrayList<String> paramValues = new ArrayList<>();
        /** Create the URL **/
        paramNames.add("address");
        paramNames.add("key");
        paramValues.add(address + city + country);
        paramValues.add("AIzaSyBmmBY1k_AYX_kJC6skswTF-tscZK8zLe8");
        String coordinatesURL = "https://maps.googleapis.com/maps/api/geocode/json" + Utils.encodeParameterizedURL(paramNames, paramValues);
        /** Make the call using RestCallParameters and RestCallManager **/
        ArrayList<JSONObject> jsonLatLng = callWebService(coordinatesURL, "GET", "JSON", "");
        for(int i=0;i<jsonLatLng.size();i++){
            JSONObject coordinationsObj = jsonLatLng.get(i);
        /** Get the result **/
            if(coordinationsObj.has("results")) {
                try {
                    JSONArray jsonResultsArr = (JSONArray) coordinationsObj.get("results");
                    JSONObject jsonResults;
                    for(int j=0; j<jsonResultsArr.length();j++) {
                        jsonResults= (JSONObject)jsonResultsArr.get(i);
                        if(jsonResults.has("geometry")){
                            JSONObject jsonGeometryObj = (JSONObject) jsonResults.get("geometry");
                            JSONObject jsonLatLngObj = (JSONObject) jsonGeometryObj.get("location");
                            lat = (double) jsonLatLngObj.get("lat");
                            lng = (double) jsonLatLngObj.get("lng");
                        }
                    }
                } catch (JSONException e) {
                    Log.i("",e.getMessage());
                }
            }
        }
        LatLng getResidenceCoordinates = new LatLng(lat, lng);
        return getResidenceCoordinates;
    }

    /** create an object of type RestCallManager to get the result of the query **/
    public static ArrayList<JSONObject> callWebService(String url, String type, String returnType, String params) {
        RestCallManager dataManager = new RestCallManager();
        RestCallParameters dataParams = new RestCallParameters(url, type, returnType, params);
        dataManager.execute(dataParams);
        return dataManager.getSingleJSONArray();
    }
}
