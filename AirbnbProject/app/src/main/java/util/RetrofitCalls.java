package util;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fromRESTful.Conversations;
import fromRESTful.Messages;
import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sissy on 25/6/2017.
 */

public class RetrofitCalls
{
    ArrayList<Residences> residencesList;
    ArrayList<Reviews> reviewsList;
    ArrayList<Reservations> reservationsList;
    ArrayList<Searches> searchesList;
    ArrayList<Users> usersList;
    ArrayList<Conversations> conversationsList;
    ArrayList<Messages> messagesList;

    Residences residence;
    Conversations conversation;
    Users user;


    /** Calls for User **/
    private class getUserByUsernameHttpRequestTask extends AsyncTask<String, String, ArrayList<Users>> {

        @Override
        protected ArrayList<Users> doInBackground(String... params)
        {
            usersList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Users>> call = restAPI.getUserByUsername(params[0]);
            try {
                Response<List<Users>> resp = call.execute();
                usersList.addAll(resp.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return usersList;
        }
        @Override
        protected void onPostExecute(ArrayList<Users> users) {
        }
    }


    public ArrayList<Users> getUserbyUsername(String username)
    {
        getUserByUsernameHttpRequestTask getUserByUsername = new getUserByUsernameHttpRequestTask();
        getUserByUsername.execute(username);
        try {
            getUserByUsername.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    private class getUserByEmailHttpRequestTask extends AsyncTask<String, String, ArrayList<Users>> {

        @Override
        protected ArrayList<Users> doInBackground(String... params)
        {
            usersList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Users>> call = restAPI.getUserByEmail(params[0]);
            try {
                Response<List<Users>> resp = call.execute();
                usersList.addAll(resp.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return usersList;
        }
        @Override
        protected void onPostExecute(ArrayList<Users> users) {
        }
    }


    public ArrayList<Users> getUserbyEmail(String email)
    {
        getUserByEmailHttpRequestTask getUserByEmail = new getUserByEmailHttpRequestTask();
        getUserByEmail.execute(email);
        try {
            getUserByEmail.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    private class getLoginUserHttpRequestTask extends AsyncTask<String, String, ArrayList<Users>>
    {
        @Override
        protected ArrayList<Users> doInBackground(String... params)
        {
            usersList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Users>> call = restAPI.getLoginUser(params[0], params[1]);
            try{
                Response<List<Users>> resp = call.execute();
                usersList.addAll(resp.body());
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return usersList;
        }
    }

    public ArrayList<Users> getLoginUser(String username, String password)
    {
        getLoginUserHttpRequestTask loginUser = new getLoginUserHttpRequestTask();
        loginUser.execute(username, password);
        try{
            loginUser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    private class getUserByIdHttpRequestTask extends AsyncTask<Integer, Integer, Users>
    {

        @Override
        protected Users doInBackground(Integer... params)
        {
            user = new Users();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<Users> call = restAPI.getUserById(params[0]);
            try {
                Response<Users> resp = call.execute();
                user = resp.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user;
        }
        @Override
        protected void onPostExecute(Users users) {
        }
    }

    public Users getUserbyId(int id)
    {
        getUserByIdHttpRequestTask userById = new getUserByIdHttpRequestTask();
        userById.execute(id);
        try {
            userById.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

    private class getSearchedCitiesHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Searches>>
    {

        @Override
        protected ArrayList<Searches> doInBackground(Integer... params)
        {
            searchesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Searches>> call = restAPI.getCitiesByUserId(params[0]);
            try
            {
                Response<List<Searches>> resp = call.execute();
                searchesList.addAll(resp.body());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return searchesList;
        }
        @Override
        protected void onPostExecute(ArrayList<Searches> searches) {
        }
    }


    /** Calls for Searches**/
    public ArrayList<Searches> getSearchedCities(int userId)
    {
        getSearchedCitiesHttpRequestTask getCities = new getSearchedCitiesHttpRequestTask();
        getCities.execute(userId);
        try {
            getCities.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return searchesList;
    }


    /** Calls for Reviews**/
    private class getReviewsHttpRequestTask extends AsyncTask<Void, Void, ArrayList<Reviews>> {

        @Override
        protected ArrayList<Reviews> doInBackground(Void... params) {

            reviewsList = new ArrayList<>();

            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Reviews>> call = restAPI.getReviews();
            try {
                Response<List<Reviews>> resp = call.execute();
                reviewsList.addAll(resp.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviewsList;

        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> reviews) {
        }
    }
    public ArrayList<Reviews> getAllReviews(){
        getReviewsHttpRequestTask allReviews = new getReviewsHttpRequestTask();
        allReviews.execute();
        try {
            allReviews.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    private class getReviewsByResidenceIdHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Reviews>> {

        @Override
        protected ArrayList<Reviews> doInBackground(Integer... params) {

            reviewsList = new ArrayList<>();

            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Reviews>> call = restAPI.getReviewsByResidence(params[0]);
            try {
                Response<List<Reviews>> resp = call.execute();
                reviewsList.addAll(resp.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviewsList;

        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> reviews) {
        }

    }

    public ArrayList<Reviews> getReviewsByResidenceId(int residenceId){
        getReviewsByResidenceIdHttpRequestTask reviewsByResidenceId = new getReviewsByResidenceIdHttpRequestTask();
        reviewsByResidenceId.execute(residenceId);
        try {
            reviewsByResidenceId.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    private class getResidenceByCityHttpRequestTask extends AsyncTask<String, String, ArrayList<Residences>>
    {
        @Override
        protected ArrayList<Residences> doInBackground(String... params){
            residencesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Residences>> call = restAPI.getResidenceByCity(params[0]);
            try
            {
                Response<List<Residences>> resp = call.execute();
                residencesList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return residencesList;
        }
    }

    public ArrayList<Reviews> getReviewsByTenantId(int tenantId)
    {
        getReviewsByTenantIdHttpRequestTask reviewsByTenant = new getReviewsByTenantIdHttpRequestTask();
        reviewsByTenant.execute(tenantId);
        try {
            reviewsByTenant.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }


    /** Calls for Residences**/
    private class getResidencesByHostIdHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Residences>>
    {
        @Override
        protected ArrayList<Residences> doInBackground(Integer... params){
            residencesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Residences>> call = restAPI.getResidencesByHostId(params[0]);
            try
            {
                Response <List<Residences>> resp = call.execute();
                residencesList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return residencesList;
        }
    }

    public ArrayList<Residences> getResidencesByCity(String city){
        getResidenceByCityHttpRequestTask residencesByCity = new getResidenceByCityHttpRequestTask();
        residencesByCity.execute(city);
        try {
            residencesByCity.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return residencesList;
    }

    private class getReviewsByTenantIdHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Reviews>>
    {
        @Override
        protected ArrayList<Reviews> doInBackground (Integer... params)
        {
            reviewsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Reviews>> call = restAPI.getReviewsByTenant(params[0]);
            try
            {
                Response<List<Reviews>> resp = call.execute();
                reviewsList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return reviewsList;
        }
    }

    public ArrayList<Residences> getResidencesByHost(int hostId){
        getResidencesByHostIdHttpRequestTask residencesByHost = new getResidencesByHostIdHttpRequestTask();
        residencesByHost.execute(hostId);
        try {
            residencesByHost.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return residencesList;
    }

     private class getResidenceByIdHttpRequestTask extends AsyncTask<Integer, Integer, Residences>
     {
         @Override
         protected Residences doInBackground(Integer... params)
         {
             residence = new Residences(host, title, type, about, address, city, country, amenities, floor, rooms, baths, view, spaceArea, guests, minPrice, additionalCostPerPerson, cancellationPolicy, rules, kitchen, kitchen, livingRoom, startDate, endDate, photo);
             RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
             Call<Residences> call = restAPI.getResidencesById(params[0]);
             try
             {
                 Response<Residences> resp = call.execute();
                 residence = resp.body();
             }
             catch (IOException e) {
                 e.printStackTrace();
             }
             return residence;
         }
     }

     public Residences getResidenceById (int id){
         getResidenceByIdHttpRequestTask residenceById = new getResidenceByIdHttpRequestTask();
         residenceById.execute(id);
         try {
             residenceById.get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         return residence;
     }

     private class getAllResidencesHttpRequestTask extends AsyncTask<Void, Void, ArrayList<Residences>>
     {
         @Override
         protected ArrayList<Residences> doInBackground(Void... params)
         {
             residencesList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
             Call<List<Residences>> call = restAPI.getAllResidences();
             try
             {
                 Response<List<Residences>> resp = call.execute();
                 residencesList.addAll(resp.body());
             }
             catch (IOException e) {
                 e.printStackTrace();
             }
             return residencesList;
         }
     }

     public ArrayList<Residences> getAllResidences()
     {
         getAllResidencesHttpRequestTask residences = new getAllResidencesHttpRequestTask();
         residences.execute();
         try {
             residences.get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         return residencesList;
     }

     private class getRecommendationsHttPRequestTask extends AsyncTask<String, String, ArrayList<Residences>>
     {
         @Override
         protected ArrayList<Residences> doInBackground(String... params)
         {
             residencesList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
             Call<List<Residences>> call = restAPI.getSearchResidences(params[0], params[1], params[2], params[3], params[4]);
             try
             {
                 Response<List<Residences>> resp = call.execute();
                 residencesList.addAll(resp.body());
             }
             catch (IOException e) {
                 e.printStackTrace();
             }
             return residencesList;
         }
     }
     public ArrayList<Residences> getRecommendations(String userId, String city, String startDate, String endDate, String guests)
     {
         getRecommendationsHttPRequestTask recommendations = new getRecommendationsHttPRequestTask();
         recommendations.execute(userId, city, startDate, endDate, guests);
         try {
             recommendations.get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         return residencesList;
     }


    /** Calls for Conversations**/

    private class getConversationsHttPRequestTask extends AsyncTask<Integer, Integer, ArrayList<Conversations>>
     {
         @Override
         protected ArrayList<Conversations> doInBackground(Integer... params)
         {
             conversationsList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
             Call<List<Conversations>> call = restAPI.getConversations(params[0]);
             try
             {
                 Response<List<Conversations>> resp = call.execute();
                 conversationsList.addAll(resp.body());
             }
             catch (IOException e){
                 e.printStackTrace();
             }
             return conversationsList;
         }
     }

     public ArrayList<Conversations> getConversations(int userId)
     {
         getConversationsHttPRequestTask getConversationsByUserId = new getConversationsHttPRequestTask();
         getConversationsByUserId.execute(userId);
         try {
             getConversationsByUserId.get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         return conversationsList;
     }

     private class getConversationsByIdHttPRequestTask extends AsyncTask<Integer, Integer, Conversations>
     {
         @Override
         protected Conversations doInBackground(Integer... params)
         {
             conversation = new Conversations();
             RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
             Call<Conversations> call = restAPI.getConversationById(params[0]);
             try
             {
                 Response<Conversations> resp = call.execute();
                 conversation = resp.body();
             }
             catch (IOException e){
                 e.printStackTrace();
             }
             return conversation;
         }
     }

     public Conversations getConversationsById(int id)
     {
         getConversationsByIdHttPRequestTask conversationsById = new getConversationsByIdHttPRequestTask();
         conversationsById.execute(id);
         try {
             conversationsById.get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         return conversation;
     }

     private class getConversationByResidenceIdHttPRequestTask extends AsyncTask<Integer, Integer, ArrayList<Conversations>>
     {
         @Override
         protected ArrayList<Conversations> doInBackground(Integer... params)
         {
             conversationsList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
             Call<List<Conversations>> call = restAPI.getConversationByResidenceId(params[0]);
             try
             {
                 Response<List<Conversations>> resp = call.execute();
                 conversationsList.addAll(resp.body());
             }
             catch (IOException e){
                 e.printStackTrace();
             }
             return conversationsList;
         }
     }

    public ArrayList<Conversations> getConversationsByResidenceId(int residenceId)
    {
        getConversationByResidenceIdHttPRequestTask conversationsByResidenceId = new getConversationByResidenceIdHttPRequestTask();
        conversationsByResidenceId.execute(residenceId);
        try {
            conversationsByResidenceId.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return conversationsList;
    }

    private class getLastConversationHttPRequestTask extends AsyncTask<Integer, Integer, ArrayList<Conversations>>
    {
        @Override
        protected ArrayList<Conversations> doInBackground(Integer... params)
        {
            conversationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Conversations>> call = restAPI.lastConversationEntry(params[0], params[1]);
            try
            {
                Response<List<Conversations>> resp = call.execute();
                conversationsList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return conversationsList;
        }
    }

    public ArrayList<Conversations> getLastConversation(int senderId, int receiverId)
    {
        getLastConversationHttPRequestTask lastConversation = new getLastConversationHttPRequestTask();
        lastConversation.execute(senderId, receiverId);
        try {
            lastConversation.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return conversationsList;
    }

    private class  updateConversationHttPRequestTask extends AsyncTask<String, String, ArrayList<Conversations>>
    {
        @Override
        protected ArrayList<Conversations> doInBackground(String... params)
        {
            conversationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Conversations>> call = restAPI.updateConversation(params[0], params[1], params[2]);
            try
            {
                Response<List<Conversations>> resp = call.execute();
                conversationsList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return conversationsList;
        }
    }

    public ArrayList<Conversations> updateConversation (String isRead, String userType, String conversationId)
    {
        updateConversationHttPRequestTask conversation = new updateConversationHttPRequestTask();
        conversation.execute(isRead, userType, conversationId);
        try {
            conversation.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return conversationsList;
    }



    /** Calls for Messages**/

    private class getMessagesByConversationHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Messages>>
    {
        @Override
        protected ArrayList<Messages> doInBackground(Integer... params)
        {
            messagesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Messages>> call = restAPI.getMessagesByConversation(params[0]);
            try
            {
                Response<List<Messages>> resp = call.execute();
                messagesList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return messagesList;
        }
    }

    public ArrayList<Messages> getMessagesByConversation (int conversationId)
    {
        getMessagesByConversationHttpRequestTask messagesByConversation = new getMessagesByConversationHttpRequestTask();
        messagesByConversation.execute(conversationId);
        try {
            messagesByConversation.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return messagesList;
    }


    /** Calls for Reservations**/

    private class getReservationsByTenantIdHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Reservations>>
    {
        @Override
        protected ArrayList<Reservations> doInBackground(Integer... params)
        {
            reservationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Reservations>> call = restAPI.getReservationsByTenantId(params[0]);
            try
            {
                Response<List<Reservations>> resp = call.execute();
                reservationsList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return reservationsList;
        }
    }
    public ArrayList<Reservations> getReservationsByTenantId (int tenantId)
    {
        getReservationsByTenantIdHttpRequestTask reservationsByTenantId = new getReservationsByTenantIdHttpRequestTask();
        reservationsByTenantId.execute(tenantId);
        try {
            reservationsByTenantId.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

    private class getReservationsByResidenceIdHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Reservations>>
    {
        @Override
        protected ArrayList<Reservations> doInBackground(Integer... params)
        {
            reservationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Reservations>> call = restAPI.getReservationsByResidenceId(params[0]);
            try
            {
                Response<List<Reservations>> resp = call.execute();
                reservationsList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return reservationsList;
        }
    }

    public ArrayList<Reservations> getReservationsByResidenceId (int residenceId)
    {
        getReservationsByResidenceIdHttpRequestTask reservationsByResidenceId = new getReservationsByResidenceIdHttpRequestTask();
        reservationsByResidenceId.execute(residenceId);
        try {
            reservationsByResidenceId.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

    private class getReservationsByTenantIdAndResidenceIdHttpRequestTask extends AsyncTask<Integer, Integer, ArrayList<Reservations>>
    {
        @Override
        protected ArrayList<Reservations> doInBackground(Integer... params)
        {
            reservationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
            Call<List<Reservations>> call = restAPI.getReservationsByTenantIdAndResidenceId(params[0], params[1]);
            try
            {
                Response<List<Reservations>> resp = call.execute();
                reservationsList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return reservationsList;
        }
    }

    public ArrayList<Reservations> getReservationsByTenantIdandResidenceId (int tenantId, int residenceId)
    {
        getReservationsByTenantIdAndResidenceIdHttpRequestTask reservations = new getReservationsByTenantIdAndResidenceIdHttpRequestTask();
        reservations.execute(tenantId, residenceId);
        try {
            reservations.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

}