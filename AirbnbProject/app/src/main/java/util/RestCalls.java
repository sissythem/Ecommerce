package util;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;

/**
 * Created by sissy on 14/5/2017.
 */

public class RestCalls {
    public static int getUserId (String username){
        int userId;
        //get UserId based on username
        String getUserIdurl = RestPaths.getUserByUsername(username);
        ArrayList<JSONObject> jsonArrayUsers = callWebService(getUserIdurl, "GET", "JSON", "");

        JSONObject userRow = jsonArrayUsers.get(0);
        Users loggedinUser = Users.fromJSON(userRow);
        userId = loggedinUser.getId();

        return userId;
    }
    public static Set<String> getSearchedCities (int userId){
        //get all searched cities by user
        String getSearchedCitiesUrl = RestPaths.getCitiesByUser(Integer.toString(userId));

        ArrayList<JSONObject> jsonArraySearchedCities = callWebService(getSearchedCitiesUrl, "GET", "JSON", "");

        //keep city info where user has made reservations. Info from searches table
        Set<String> relevantCity = new HashSet<>();
        for (int i = 0; i < jsonArraySearchedCities.size(); i++) {
            JSONObject cityrow = jsonArraySearchedCities.get(i);
            Searches citySearches = Searches.fromJSON(cityrow);
            relevantCity.add(citySearches.getCity());
        }
        return relevantCity;
    }
    public static ArrayList<Reviews> getReviews(){
        ArrayList<Reviews> reviews = new ArrayList<>();
        // specify call url, parameters and create call manager object
        String findPopularResidencesUrl = RestPaths.AllReviews;
        // get call results
        ArrayList<JSONObject> jsonArrayPopularResidences = callWebService(findPopularResidencesUrl, "GET", "JSON", "");

        for (int i=0;i<jsonArrayPopularResidences.size();i++) {
            reviews.add(Reviews.fromJSON(jsonArrayPopularResidences.get(i)));
        }
        return reviews;
    }
    public static ArrayList<Residences> getResidenceByCity(String city){
        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        String getCityResidences = RestPaths.getResidenceByCity(city);
        ArrayList<JSONObject> jsonArrayResidences = callWebService(getCityResidences, "GET", "JSON", "");

        for (int i=0;i<jsonArrayResidences.size();i++) {
            JSONObject residenceObject = jsonArrayResidences.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            reviewedResidences.add(residence);
        }
        return reviewedResidences;
    }
    public static ArrayList<Reviews>getReviewsByResidenceId(int residenceId){
        ArrayList<Reviews>reviewsByResidenceId = new ArrayList<>();
        String reviewsByResidenceURL = RestPaths.getReviewsByResidence(Integer.toString(residenceId));

        ArrayList<JSONObject> jsonArrayReviews = callWebService(reviewsByResidenceURL, "GET", "JSON", "");
        for (int i=0;i<jsonArrayReviews.size();i++){
            JSONObject reviewObject = jsonArrayReviews.get(i);
            Reviews review = Reviews.fromJSON(reviewObject);
            reviewsByResidenceId.add(review);
        }
        return reviewsByResidenceId;
    }
    public static Bitmap getPhoto (String url){
        ArrayList<Bitmap> Response  = new ArrayList<>();
        RestCallManager photoManager = new RestCallManager();
        RestCallParameters photoParams = new RestCallParameters(url, "GET", "TEXT", "", "STREAM");
        photoManager.execute(photoParams);

        return photoManager.getSingleBitmap();

    }
    public static Users getUser (String username){
        Users user = new Users();
        String findUserUrl = RestPaths.getUserByUsername(username);

        ArrayList<JSONObject>jsonArrayUser = callWebService(findUserUrl, "GET", "JSON", "");
        for(int i=0;i<jsonArrayUser.size();i++){
            user = Users.fromJSON(jsonArrayUser.get(i));
        }
        return user;
    }
    public static ArrayList<Residences> getResidencesbyHostId (int hostId){
        ArrayList<Residences> storedResidences = new ArrayList<>();
        String residencesByHostURL = RestPaths.getResidencesByHostId(Integer.toString(hostId));

        ArrayList<JSONObject> jsonArrayResidences = callWebService(residencesByHostURL, "GET", "JSON", "");
        for (int i=0;i<jsonArrayResidences.size();i++) {
            JSONObject residenceObject = jsonArrayResidences.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            storedResidences.add(residence);
        }
        return storedResidences;
    }
    public static Residences getResidenceById (int residenceId)
    {
        String residenceByIdURL = RestPaths.getResidencesById(residenceId);
        ArrayList<JSONObject> jsonResidence = callWebService(residenceByIdURL, "GET", "JSON", "");
        JSONObject uploadedres = jsonResidence.get(0);
        Residences uploadedResidence = Residences.fromJSON(uploadedres);
        return uploadedResidence;
    }
    public static ArrayList<Residences> getAllResidences(){
        ArrayList<Residences> allStoredResidences = new ArrayList<>();
        String residencesURL = RestPaths.AllResidences;
        ArrayList<JSONObject> jsonResidence = callWebService(residencesURL, "GET", "JSON", "");
        for(int i=0; i<jsonResidence.size();i++){
            JSONObject residenceObject = jsonResidence.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            allStoredResidences.add(residence);
        }
        return allStoredResidences;
    }
    public static ArrayList<Residences> getRecommendations(String username, String city, String startDate, String endDate, Integer guests) {
        ArrayList<JSONObject> jsonArrayRecommendations = callWebService(RestPaths.getFrontResidences(Integer.toString(getUserId(username)), city, startDate, endDate, guests), "GET", "JSON", "");

        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        for (int i=0;i<jsonArrayRecommendations.size();i++) {
            JSONObject residenceObject = jsonArrayRecommendations.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            reviewedResidences.add(residence);
        }
        return reviewedResidences;
    }
    public static Users getUserById(int userId){
        String userByIdURL = RestPaths.getUserById(userId);
        ArrayList<JSONObject> jsonUser = callWebService(userByIdURL, "GET", "JSON", "");
        JSONObject userToGet = jsonUser.get(0);
        Users user = Users.fromJSON(userToGet);
        return user;
    }

    public static ArrayList<Reservations> getReservationsByTenantId (int tenantId){
        ArrayList<Reservations> allReservationsByUser = new ArrayList<>();
        String reservationsURL = RestPaths.getReservationsByTenantId(tenantId);
        ArrayList<JSONObject> jsonReservations = callWebService(reservationsURL, "GET", "JSON", "");
        for(int i=0; i<jsonReservations.size();i++){
            JSONObject reservationsObj = jsonReservations.get(i);
            Reservations reservation = Reservations.fromJSON(reservationsObj);
            allReservationsByUser.add(reservation);
        }
        return allReservationsByUser;
    }


    /** create an object of type RestCallManager to get the result of the query **/
    public static ArrayList<JSONObject> callWebService(String url, String type, String returnType, String params) {
        RestCallManager dataManager = new RestCallManager();
        RestCallParameters dataParams = new RestCallParameters(url, type, returnType, params);
        dataManager.execute(dataParams);
        return dataManager.getSingleJSONArray();
    }
}
