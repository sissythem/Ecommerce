package util;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fromRESTful.Conversations;
import fromRESTful.Messages;
import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;

public class RestCalls {

    /** TO FIX: GET ID FROM LOGIN DIRECTLY - NO NEED TO MAKE EXTRA CALLS **/
    public static int getUserId (String username){
        int userId;
        ArrayList<JSONObject> jsonArrayUsers = callWebService(RestPaths.getUserByUsername(username), "GET", "JSON", "");
        Users loggedinUser = Users.fromJSON(jsonArrayUsers.get(0));
        return loggedinUser.getId();
    }

    public static Set<String> getSearchedCities (Integer userId){
        //get all searched cities by user
        ArrayList<JSONObject> jsonArraySearchedCities = callWebService(RestPaths.getCitiesByUserId(userId), "GET", "JSON", "");

        //keep place info where user has made reservations. Info from searches table
        Set<String> relevantCitites = new HashSet<>();
        for (int i = 0; i < jsonArraySearchedCities.size(); i++) {
            Searches citySearches = Searches.fromJSON(jsonArraySearchedCities.get(i));
            relevantCitites.add(citySearches.getCity());
        }
        return relevantCitites;
    }

    public static ArrayList<Reviews> getReviews(){
        ArrayList<JSONObject> jsonArrayPopularResidences = callWebService(RestPaths.AllReviews, "GET", "JSON", "");

        ArrayList<Reviews> reviews = new ArrayList<>();
        for (int i=0;i<jsonArrayPopularResidences.size();i++) {
            reviews.add(Reviews.fromJSON(jsonArrayPopularResidences.get(i)));
        }
        return reviews;
    }

    public static ArrayList<Residences> getResidenceByCity(String city){
        ArrayList<JSONObject> jsonArrayResidences = callWebService(RestPaths.getResidenceByCity(city), "GET", "JSON", "");

        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        for (int i=0;i<jsonArrayResidences.size();i++) {
            JSONObject residenceObject = jsonArrayResidences.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            reviewedResidences.add(residence);
        }
        return reviewedResidences;
    }

    public static ArrayList<Reviews>getReviewsByResidenceId(int residenceId){
        ArrayList<JSONObject> jsonArrayReviews = callWebService(RestPaths.getReviewsByResidence(residenceId), "GET", "JSON", "");

        ArrayList<Reviews> reviewsByResidenceId = new ArrayList<>();
        for (int i=0;i<jsonArrayReviews.size();i++){
            JSONObject reviewObject = jsonArrayReviews.get(i);
            Reviews review = Reviews.fromJSON(reviewObject);
            reviewsByResidenceId.add(review);
        }
        return reviewsByResidenceId;
    }

    public static ArrayList<Residences> getResidencesbyHostId (Integer hostId) {
        ArrayList<Residences> storedResidences = new ArrayList<>();
        ArrayList<JSONObject> jsonArrayResidences = callWebService(RestPaths.getResidencesByHostId(hostId), "GET", "JSON", "");
        for (int i=0;i<jsonArrayResidences.size();i++) {
            JSONObject residenceObject = jsonArrayResidences.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            storedResidences.add(residence);
        }
        return storedResidences;
    }

    public static Residences getResidenceById (int residenceId) {
        ArrayList<JSONObject> jsonResidence = callWebService(RestPaths.getResidencesById(residenceId), "GET", "JSON", "");
        return Residences.fromJSON(jsonResidence.get(0));
    }

    public static ArrayList<Residences> getAllResidences() {
        ArrayList<Residences> allStoredResidences = new ArrayList<>();
        ArrayList<JSONObject> jsonResidence = callWebService(RestPaths.AllResidences, "GET", "JSON", "");
        for(int i=0; i<jsonResidence.size();i++){
            JSONObject residenceObject = jsonResidence.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            allStoredResidences.add(residence);
        }
        return allStoredResidences;
    }

    public static ArrayList<Residences> getRecommendations(String username, String city, String startDate, String endDate, Integer guests) {
        ArrayList<JSONObject> jsonArrayRecommendations = callWebService(RestPaths.getSearchResidences(Integer.toString(getUserId(username)), city, startDate, endDate, guests), "GET", "JSON", "");

        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        for (int i=0;i<jsonArrayRecommendations.size();i++) {
            JSONObject residenceObject = jsonArrayRecommendations.get(i);
            Residences residence = Residences.fromJSON(residenceObject);
            reviewedResidences.add(residence);
        }
        return reviewedResidences;
    }

    public static ArrayList<Conversations> getConversations(Integer userId) {
        ArrayList<JSONObject> jsonArrayConversations = callWebService(RestPaths.getConversations(Integer.toString(userId)), "GET", "JSON", "");

        ArrayList<Conversations> allConversations = new ArrayList<>();
        for (int i=0; i < jsonArrayConversations.size(); i++) {
            JSONObject conversationObject = jsonArrayConversations.get(i);
            Conversations conversation = Conversations.fromJSON(conversationObject);
            allConversations.add(conversation);
        }
        return allConversations;
    }

    public static Conversations getConversation(Integer conversationId) {
        ArrayList<JSONObject> jsonConversation = callWebService(RestPaths.getConversationById(conversationId), "GET", "JSON", "");
        return Conversations.fromJSON(jsonConversation.get(0));
    }

    public static Conversations getConversationByResidence(Integer residenceId) {
        ArrayList<JSONObject> jsonConversation = callWebService(RestPaths.getConversationByResidenceId(residenceId.toString()), "GET", "JSON", "");
        if (jsonConversation.size() > 0) return Conversations.fromJSON(jsonConversation.get(0));
        return null;
    }

    public static Conversations getLastConversation (Integer senderId, Integer receiverId) {
        ArrayList<JSONObject> jsonConversation = callWebService(RestPaths.lastConversationEntry(senderId.toString(), receiverId.toString()), "GET", "JSON", "");
        return Conversations.fromJSON(jsonConversation.get(0));
    }

    public static ArrayList<Messages> getMessages(Integer conversationId) {
        ArrayList<JSONObject> jsonArrayMessages = callWebService(RestPaths.getMessagesByConversation(conversationId), "GET", "JSON", "");
        ArrayList<Messages> allMessages = new ArrayList<>();

        for (int i=0; i < jsonArrayMessages.size(); i++) {
            JSONObject messageObject = jsonArrayMessages.get(i);
            Messages message = Messages.fromJSON(messageObject);
            allMessages.add(message);
        }
        return allMessages;
    }

    public static Conversations updateConversation(Integer isRead, String userType, Integer conversationId) {
        ArrayList<JSONObject> jsonArrayConversation = callWebService(RestPaths.updateConversation(isRead.toString(), userType, conversationId.toString()), "GET", "JSON", "");
        return Conversations.fromJSON((jsonArrayConversation.get(0)));
    }

    public static Bitmap getPhoto (String url){
        ArrayList<Bitmap> Response  = new ArrayList<>();
        RestCallManager photoManager = new RestCallManager();
        RestCallParameters photoParams = new RestCallParameters(url, "GET", "TEXT", "", "STREAM");
        photoManager.execute(photoParams);

        return photoManager.getSingleBitmap();
    }

    public static Users getUser (String username){
        ArrayList<JSONObject>jsonArrayUser = callWebService(RestPaths.getUserByUsername(username), "GET", "JSON", "");
        return Users.fromJSON(jsonArrayUser.get(0));
    }

    public static Users getUserById(int userId){
        ArrayList<JSONObject>jsonArrayUser = callWebService(RestPaths.getUserById(userId), "GET", "JSON", "");
        return Users.fromJSON(jsonArrayUser.get(0));
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
