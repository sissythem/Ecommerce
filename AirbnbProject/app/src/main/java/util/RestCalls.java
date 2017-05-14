package util;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Rooms;
import fromRESTful.Searches;
import fromRESTful.Users;

/**
 * Created by sissy on 14/5/2017.
 */

public class RestCalls {
    public static int getUserId (String username){
        int userId;
        //get UserId based on username
        String getUserIdurl = "http://192.168.1.6:8080/ecommerce_restful/webresources/users/username?username=" + username;

        //create an object of type RestCallManager to get the result of the query
        RestCallManager UsernameManager = new RestCallManager();
        RestCallParameters usernameparameters = new RestCallParameters(getUserIdurl, "GET", "JSON", "");
        UsernameManager.execute(usernameparameters);

        ArrayList<JSONObject> jsonArrayUsers = UsernameManager.getSingleJSONArray();

        JSONObject userRow = jsonArrayUsers.get(0);
        Users loggedinUser = Users.fromJSON(userRow);
        userId = loggedinUser.getId();

        return userId;
    }
    public static Set<String> getSearchedCities (int userId){
        //get all searched cities by user
        String getSearchedCitiesUrl = "http://192.168.1.6:8080/ecommerce_restful/webresources/searches/city?userId=" + Integer.toString(userId);

        RestCallManager searchedCitiesManager = new RestCallManager();
        RestCallParameters searchedCitiesParameters = new RestCallParameters(getSearchedCitiesUrl, "GET", "JSON", "");
        searchedCitiesManager.execute(searchedCitiesParameters);

        ArrayList<JSONObject> jsonArraySearchedCities = searchedCitiesManager.getSingleJSONArray();

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
        String findPopularResidencesUrl = "http://192.168.1.6:8080/ecommerce_restful/webresources/reviews";
        RestCallManager getPopularResidencesManager = new RestCallManager();
        RestCallParameters getResidencesParameters = new RestCallParameters(findPopularResidencesUrl, "GET", "JSON", "");
        // set the call to be executed
        getPopularResidencesManager.execute(getResidencesParameters);
        // get call results
        ArrayList<JSONObject> jsonArrayPopularResidences = getPopularResidencesManager.getSingleJSONArray();

        for (int i=0;i<jsonArrayPopularResidences.size();i++) {
            reviews.add(Reviews.fromJSON(jsonArrayPopularResidences.get(i)));
        }
        return reviews;
    }
    public static ArrayList<Residences> getResidenceByCity(String city){
        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        String getCityResidences = "http://192.168.1.6:8080/ecommerce_restful/webresources/residences/city?city=" + city;
        RestCallManager residenceManager = new RestCallManager();
        RestCallParameters residenceParameters = new RestCallParameters(getCityResidences, "GET", "JSON", "");
        residenceManager.execute(residenceParameters);
        ArrayList<JSONObject> jsonArrayResidences = residenceManager.getSingleJSONArray();

        for (int i=0;i<jsonArrayResidences.size();i++) {
            JSONObject residenceObject = jsonArrayResidences.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            reviewedResidences.add(residence);
        }
        return reviewedResidences;
    }
    public static ArrayList<Rooms>getRoomsByResidenceId(int residenceId){
        ArrayList<Rooms> roomsByResidence = new ArrayList<>();
        String roomsByResidenceURL = "http://192.168.1.6:8080/ecommerce_restful/webresources/rooms/residenceId?residenceId="+ Integer.toString(residenceId);
        RestCallManager roomsManager = new RestCallManager();
        RestCallParameters roomsParameters = new RestCallParameters(roomsByResidenceURL, "GET", "JSON", "");
        roomsManager.execute(roomsParameters);
        ArrayList<JSONObject> jsonArrayRooms = roomsManager.getSingleJSONArray();

        for (int i=0;i<jsonArrayRooms.size();i++){
            JSONObject roomObject = jsonArrayRooms.get(i);
            Rooms room = Rooms.fromJSON(roomObject);
            roomsByResidence.add(room);
        }
        return roomsByResidence;
    }
    public static ArrayList<Reviews>getReviewsByResidenceId(int residenceId){
        ArrayList<Reviews>reviewsByResidenceId = new ArrayList<>();
        String reviewsByResidenceURL = "http://192.168.1.6:8080/ecommerce_restful/webresources/reviews/residence?residenceId="+ Integer.toString(residenceId);
        RestCallManager reviewsManager = new RestCallManager();
        RestCallParameters reviewsParameters = new RestCallParameters(reviewsByResidenceURL, "GET", "JSON", "");
        reviewsManager.execute(reviewsParameters);
        ArrayList<JSONObject> jsonArrayReviews = reviewsManager.getSingleJSONArray();
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
}
