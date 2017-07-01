package util;

import android.os.AsyncTask;
import android.util.Log;

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

public class RetrofitCalls {
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
    String token;

    private class checkTokenHttpRequestTask extends AsyncTask<String, String, String>
    {
        @Override
        protected  String doInBackground(String... params)
        {
            token="";
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<String> call = restAPI.checkToken();
            try
            {
                Response<String> resp = call.execute();
            } catch(IOException e){
                e.printStackTrace();
            }
            return token;
        }
    }

    public String checkToken(String token)
    {
        checkTokenHttpRequestTask checktoken = new checkTokenHttpRequestTask();
        checktoken.execute(token);
        try{
            checktoken.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return token;
    }

    /** Calls for User **/
    private class getUserByUsernameHttpRequestTask extends AsyncTask<String, String, ArrayList<Users>> {

        @Override
        protected ArrayList<Users> doInBackground(String... params) {
            usersList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Users>> call = restAPI.getUserByUsername(params[1]);
            try {
                Response<List<Users>> resp = call.execute();
                usersList.addAll(resp.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return usersList;
        }
        @Override
        protected void onPostExecute(ArrayList<Users> users) {}
    }

    public ArrayList<Users> getUserbyUsername(String token, String username) {
        getUserByUsernameHttpRequestTask getUserByUsername = new getUserByUsernameHttpRequestTask();
        getUserByUsername.execute(token, username);
        try {
            getUserByUsername.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    private class postUserHttpRequestTesk extends AsyncTask<Users, Users, String>
    {
        @Override
        protected String doInBackground(Users... params){
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            Call<String> call = restAPI.postUser(params[0]);
            try{
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                e.printStackTrace();
            }
            return token;
        }
    }

    public String postUser(Users user){
        postUserHttpRequestTesk postUserTask = new postUserHttpRequestTesk();
        postUserTask.execute(user);
        try{
            postUserTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return token;
    }

    private class editUserHttpRequestTesk extends AsyncTask<Object, Object, String>
    {
        @Override
        protected String doInBackground(Object... params){
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.editUserById((String)params[1],(Users)params[2]);
            try{
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                e.printStackTrace();
            }
            return token;
        }
    }

    public String editUser(String token, String userId, Users user){
        editUserHttpRequestTesk editUserTask = new editUserHttpRequestTesk();
        editUserTask.execute(token, userId, user);
        try{
            editUserTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return this.token;
    }

    private class getUserByEmailHttpRequestTask extends AsyncTask<String, String, ArrayList<Users>> {
        @Override
        protected ArrayList<Users> doInBackground(String... params) {
            usersList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Users>> call = restAPI.getUserByEmail(params[1]);
            try {
                Response<List<Users>> resp = call.execute();
                usersList.addAll(resp.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return usersList;
        }
        @Override
        protected void onPostExecute(ArrayList<Users> users) {}
    }

    public ArrayList<Users> getUserbyEmail(String token, String email){
        getUserByEmailHttpRequestTask getUserByEmail = new getUserByEmailHttpRequestTask();
        getUserByEmail.execute(token, email);
        try {
            getUserByEmail.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    private class getLoginUserHttpRequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String token="";
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);

            Call<String> call = restAPI.getLoginUser(params[0], params[1]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return token;
        }
    }

    public String getLoginUser(String username, String password) {
        String token = "";
        getLoginUserHttpRequestTask loginUser = new getLoginUserHttpRequestTask();
        loginUser.execute(username, password);
        try {
            token = loginUser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return token;
    }

    private class deleteUserByIdHttpRequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String token="";
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteUserById(params[1]);
            try {
                Response<String> resp = call.execute();
                System.out.println(resp);
                token = resp.body();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return token;
        }
    }

    public String deleteUserById(String token, String id) {
        deleteUserByIdHttpRequestTask deleteUser = new deleteUserByIdHttpRequestTask();
        deleteUser.execute(token, id);
        try {
            token = deleteUser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return token;
    }

    private class getUserByIdHttpRequestTask extends AsyncTask<String, String, Users> {
        @Override
        protected Users doInBackground(String... params) {
            user = new Users();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<Users> call = restAPI.getUserById(params[1]);
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

    public Users getUserbyId(String token, String id) {
        getUserByIdHttpRequestTask userById = new getUserByIdHttpRequestTask();
        userById.execute(token, id);
        try {
            userById.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return user;
    }

    /** Calls for Searches**/
    private class getSearchedCitiesHttpRequestTask extends AsyncTask<String, String, ArrayList<Searches>> {
        @Override
        protected ArrayList<Searches> doInBackground(String... params) {
            searchesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Searches>> call = restAPI.getCitiesByUserId(params[1]);
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

    public ArrayList<Searches> getSearchedCities(String token, String userId) {
        getSearchedCitiesHttpRequestTask getCities = new getSearchedCitiesHttpRequestTask();
        getCities.execute(token, userId);
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
    private class getReviewsHttpRequestTask extends AsyncTask<String, String, ArrayList<Reviews>> {

        @Override
        protected ArrayList<Reviews> doInBackground(String... params) {
            reviewsList = new ArrayList<>();

            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
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
        protected void onPostExecute(ArrayList<Reviews> reviews) {}
    }

    public ArrayList<Reviews> getAllReviews(String token){
        getReviewsHttpRequestTask allReviews = new getReviewsHttpRequestTask();
        allReviews.execute(token);
        try {
            allReviews.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    private class getReviewsByResidenceIdHttpRequestTask extends AsyncTask<String, String, ArrayList<Reviews>> {

        @Override
        protected ArrayList<Reviews> doInBackground(String... params) {

            reviewsList = new ArrayList<>();

            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Reviews>> call = restAPI.getReviewsByResidence(params[1]);
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

    public ArrayList<Reviews> getReviewsByResidenceId(String token, String residenceId){
        getReviewsByResidenceIdHttpRequestTask reviewsByResidenceId = new getReviewsByResidenceIdHttpRequestTask();
        reviewsByResidenceId.execute(token, residenceId);
        try {
            reviewsByResidenceId.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    private class getReviewsByTenantIdHttpRequestTask extends AsyncTask<String, String, ArrayList<Reviews>>
    {
        @Override
        protected ArrayList<Reviews> doInBackground (String... params)
        {
            reviewsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Reviews>> call = restAPI.getReviewsByTenant(params[1]);
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

    public ArrayList<Reviews> getReviewsByTenantId(String token, String tenantId)
    {
        getReviewsByTenantIdHttpRequestTask reviewsByTenant = new getReviewsByTenantIdHttpRequestTask();
        reviewsByTenant.execute(token, tenantId);
        try {
            reviewsByTenant.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    private class postReviewHttpRequestTask extends AsyncTask<Object, Object, String>
    {
        @Override
        protected String doInBackground (Object... params)
        {
            RestAPI restAPI = RestClient.getClient((String) params[0]).create(RestAPI.class);
            Call<String> call = restAPI.postReview((Reviews)params[1]);
            try
            {
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                e.printStackTrace();
            }
            return token;
        }
    }

    public String postReview(String token, Reviews review)
    {
        postReviewHttpRequestTask postReviewTask = new postReviewHttpRequestTask();
        postReviewTask.execute(token, review);
        try {
            postReviewTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return this.token;
    }


    /***** Calls for Residences *****/
    private class getResidencesByHostIdHttpRequestTask extends AsyncTask<String, String, ArrayList<Residences>> {
        @Override
        protected ArrayList<Residences> doInBackground(String... params){
            residencesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Residences>> call = restAPI.getResidencesByHostId(params[1]);
            try {
                Response <List<Residences>> resp = call.execute();
                residencesList.addAll(resp.body());
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return residencesList;
        }
    }

    public ArrayList<Residences> getResidencesByHost(String token, String hostId){
        getResidencesByHostIdHttpRequestTask residencesByHost = new getResidencesByHostIdHttpRequestTask();
        residencesByHost.execute(token, hostId);
        try {
            residencesByHost.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return residencesList;
    }

    private class getResidenceByCityHttpRequestTask extends AsyncTask<String, String, ArrayList<Residences>> {
        @Override
        protected ArrayList<Residences> doInBackground(String... params){
            residencesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Residences>> call = restAPI.getResidenceByCity(params[1]);
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

    public ArrayList<Residences> getResidencesByCity(String token, String city){
        getResidenceByCityHttpRequestTask residencesByCity = new getResidenceByCityHttpRequestTask();
        residencesByCity.execute(token, city);
        try {
            residencesByCity.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return residencesList;
    }

     private class getResidenceByIdHttpRequestTask extends AsyncTask<String, String, Residences> {
         @Override
         protected Residences doInBackground(String... params)
         {
             residence = new Residences();
             RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
             Call<Residences> call = restAPI.getResidencesById(params[1]);
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

     public Residences getResidenceById (String token, String id){
         getResidenceByIdHttpRequestTask residenceById = new getResidenceByIdHttpRequestTask();
         residenceById.execute(token, id);
         try {
             residenceById.get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         return residence;
     }

     private class postResidenceHttpRequestTask extends AsyncTask<Object, Object, String>
     {
         @Override
         protected String doInBackground(Object... params)
         {
             RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
             Call<String> call = restAPI.postResidence((Residences)params[1]);
             try
             {
                 Response<String> resp = call.execute();
                 token = resp.body();
             }
             catch(IOException e){
                 Log.i("",e.getMessage());
                 e.printStackTrace();
             }
             return token;
         }
     }

     public String postResidence(String token, Residences residence)
     {
         postResidenceHttpRequestTask postResidenceTask = new postResidenceHttpRequestTask();
         postResidenceTask.execute(token, residence);
         try{
             postResidenceTask.get();
         } catch (InterruptedException e) {
             Log.i("",e.getMessage());
         } catch (ExecutionException e) {
             Log.i("",e.getMessage());
         }
         return this.token;
     }

    private class editResidenceHttpRequestTesk extends AsyncTask<Object, Object, String>
    {
        @Override
        protected String doInBackground(Object... params){
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.editResidenceById((String)params[1],(Residences) params[2]);
            try{
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String editResidence(String token, String residenceId, Residences residence){
        editResidenceHttpRequestTesk editResidenceTask = new editResidenceHttpRequestTesk();
        editResidenceTask.execute(token, residenceId, residence);
        try{
            editResidenceTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return this.token;
    }

     private class getAllResidencesHttpRequestTask extends AsyncTask<String, String, ArrayList<Residences>> {
         @Override
         protected ArrayList<Residences> doInBackground(String... params) {
             residencesList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
             Call<List<Residences>> call = restAPI.getAllResidences();
             try
             {
                 Response<List<Residences>> resp = call.execute();
                 residencesList.addAll(resp.body());
             }
             catch (IOException e) {
                 Log.i("",e.getMessage());
             }
             return residencesList;
         }
     }

     public ArrayList<Residences> getAllResidences(String token) {
         getAllResidencesHttpRequestTask residences = new getAllResidencesHttpRequestTask();
         residences.execute(token);
         try {
             residences.get();
         } catch (InterruptedException e) {
             Log.i("",e.getMessage());
         } catch (ExecutionException e) {
             Log.i("",e.getMessage());
         }
         return residencesList;
     }

     private class getRecommendationsHttPRequestTask extends AsyncTask<String, String, ArrayList<Residences>> {
         @Override
         protected ArrayList<Residences> doInBackground(String... params)
         {
             residencesList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
             Call<List<Residences>> call = restAPI.getSearchResidences(params[1], params[2], params[3], params[4], params[5]);
             try
             {
                 Response<List<Residences>> resp = call.execute();
                 residencesList.addAll(resp.body());
             }
             catch (IOException e) {
                 Log.i("",e.getMessage());
             }
             return residencesList;
         }
     }

     public ArrayList<Residences> getRecommendations(String token, String userId, String city, String startDate, String endDate, String guests) {
         getRecommendationsHttPRequestTask recommendations = new getRecommendationsHttPRequestTask();
         recommendations.execute(token, userId, city, startDate, endDate, guests);
         try {
             recommendations.get();
         } catch (InterruptedException e) {
             Log.i("",e.getMessage());
         } catch (ExecutionException e) {
             Log.i("",e.getMessage());
         }
         return residencesList;
     }

    private class deleteResidenceByIdHttpRequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String token="";
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteResidenceById(params[1]);
            try {
                Response<String> resp = call.execute();
                System.out.println(resp);
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }
    public String deleteResidenceById(String token, String id) {
        deleteResidenceByIdHttpRequestTask deleteResidence = new deleteResidenceByIdHttpRequestTask();
        deleteResidence.execute(token, id);
        try {
            token = deleteResidence.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }


    /** Calls for Conversations**/

    private class getConversationsHttPRequestTask extends AsyncTask<String, String, ArrayList<Conversations>> {
         @Override
         protected ArrayList<Conversations> doInBackground(String... params) {
             conversationsList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
             Call<List<Conversations>> call = restAPI.getConversations(params[1]);
             try {
                 Response<List<Conversations>> resp = call.execute();
                 conversationsList.addAll(resp.body());
             } catch (IOException e){
                 Log.i("",e.getMessage());
             }
             return conversationsList;
         }
     }

     public ArrayList<Conversations> getConversations(String token, String userId) {
         getConversationsHttPRequestTask getConversationsByUserId = new getConversationsHttPRequestTask();
         getConversationsByUserId.execute(token, userId);
         try {
             getConversationsByUserId.get();
         } catch (InterruptedException e) {
             Log.i("",e.getMessage());
         } catch (ExecutionException e) {
             Log.i("",e.getMessage());
         }
         return conversationsList;
     }

     private class getConversationByIdHttPRequestTask extends AsyncTask<String, String, Conversations> {
         @Override
         protected Conversations doInBackground(String... params) {
             conversation = new Conversations();
             RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
             Call<Conversations> call = restAPI.getConversationById(params[1]);
             try {
                 Response<Conversations> resp = call.execute();
                 conversation = resp.body();
             } catch (IOException e) {
                 Log.i("",e.getMessage());
             }
             return conversation;
         }
     }

     public Conversations getConversationById(String token, String id) {
         getConversationByIdHttPRequestTask conversationsById = new getConversationByIdHttPRequestTask();
         conversationsById.execute(token, id);
         try {
             conversationsById.get();
         } catch (InterruptedException e) {
             Log.i("",e.getMessage());
         } catch (ExecutionException e) {
             Log.i("",e.getMessage());
         }
         return conversation;
     }

     private class getConversationByResidenceIdHttPRequestTask extends AsyncTask<String, String, ArrayList<Conversations>> {
         @Override
         protected ArrayList<Conversations> doInBackground(String... params) {
             conversationsList = new ArrayList<>();
             RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
             Call<List<Conversations>> call = restAPI.getConversationByResidenceId(params[1], params[2]);
             try {
                 Response<List<Conversations>> resp = call.execute();
                 conversationsList.addAll(resp.body());
             }
             catch (IOException e){
                 Log.i("",e.getMessage());
             }
             return conversationsList;
         }
     }

    public ArrayList<Conversations> getConversationsByResidenceId(String token, String residenceId, String userId) {
        getConversationByResidenceIdHttPRequestTask conversationsByResidenceId = new getConversationByResidenceIdHttPRequestTask();
        conversationsByResidenceId.execute(token, residenceId, userId);
        try {
            conversationsByResidenceId.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        System.out.println(conversationsList);
        return conversationsList;
    }

    private class getLastConversationHttPRequestTask extends AsyncTask<String, String, ArrayList<Conversations>> {
        @Override
        protected ArrayList<Conversations> doInBackground(String... params) {
            conversationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Conversations>> call = restAPI.lastConversationEntry(params[1], params[2]);
            try
            {
                Response<List<Conversations>> resp = call.execute();
                conversationsList.addAll(resp.body());
            }
            catch (IOException e){
                Log.i("",e.getMessage());
            }
            return conversationsList;
        }
    }

    public ArrayList<Conversations> getLastConversation(String token, String senderId, String receiverId) {
        getLastConversationHttPRequestTask lastConversation = new getLastConversationHttPRequestTask();
        lastConversation.execute(token, senderId, receiverId);
        try {
            lastConversation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return conversationsList;
    }

    private class updateConversationHttpRequestTask extends AsyncTask<String, String, ArrayList<Conversations>> {
        @Override
        protected ArrayList<Conversations> doInBackground(String... params) {
            conversationsList = new ArrayList<Conversations>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Conversations>> call = restAPI.updateConversation(params[1], params[2], params[3]);
            try {
                Response<List<Conversations>> resp = call.execute();
                conversationsList.addAll(resp.body());
            } catch (IOException e) {
                Log.i("",e.getMessage());
            }
            return conversationsList;
        }
    }

    public ArrayList<Conversations> updateConversation(String token, String isRead, String userType, String conversationId) {
        updateConversationHttpRequestTask updatedConversation = new updateConversationHttpRequestTask();
        updatedConversation.execute(token, isRead, userType, conversationId);
        try {
            updatedConversation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return conversationsList;
    }

    /** Calls for Messages**/
    private class getMessagesByConversationHttpRequestTask extends AsyncTask<String, String, ArrayList<Messages>> {
        @Override
        protected ArrayList<Messages> doInBackground(String... params) {
            messagesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Messages>> call = restAPI.getMessagesByConversation(params[1]);
            try {
                Response<List<Messages>> resp = call.execute();
                messagesList.addAll(resp.body());
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            return messagesList;
        }
    }

    public ArrayList<Messages> getMessagesByConversation(String token, String conversationId) {
        getMessagesByConversationHttpRequestTask messagesByConversation = new getMessagesByConversationHttpRequestTask();
        messagesByConversation.execute(token, conversationId);
        try {
            messagesByConversation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        System.out.println(messagesList);
        return messagesList;
    }

    /** Calls for Reservations**/
    private class getReservationsByTenantIdHttpRequestTask extends AsyncTask<String, String, ArrayList<Reservations>> {
        @Override
        protected ArrayList<Reservations> doInBackground(String... params) {
            reservationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Reservations>> call = restAPI.getReservationsByTenantId(params[1]);
            try {
                Response<List<Reservations>> resp = call.execute();
                reservationsList.addAll(resp.body());
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            return reservationsList;
        }
    }

    public ArrayList<Reservations> getReservationsByTenantId (String token, String tenantId) {
        getReservationsByTenantIdHttpRequestTask reservationsByTenantId = new getReservationsByTenantIdHttpRequestTask();
        reservationsByTenantId.execute(token, tenantId);
        try {
            reservationsByTenantId.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return reservationsList;
    }

    private class getReservationsByResidenceIdHttpRequestTask extends AsyncTask<String, String, ArrayList<Reservations>> {
        @Override
        protected ArrayList<Reservations> doInBackground(String... params)
        {
            reservationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Reservations>> call = restAPI.getReservationsByResidenceId(params[1]);
            try
            {
                Response<List<Reservations>> resp = call.execute();
                reservationsList.addAll(resp.body());
            }
            catch (IOException e){
                Log.i("",e.getMessage());
            }
            return reservationsList;
        }
    }

    public ArrayList<Reservations> getReservationsByResidenceId (String token, String residenceId) {
        getReservationsByResidenceIdHttpRequestTask reservationsByResidenceId = new getReservationsByResidenceIdHttpRequestTask();
        reservationsByResidenceId.execute(token, residenceId);
        try {
            reservationsByResidenceId.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return reservationsList;
    }

    private class getReservationsByTenantIdAndResidenceIdHttpRequestTask extends AsyncTask<String, String, ArrayList<Reservations>>
    {
        @Override
        protected ArrayList<Reservations> doInBackground(String... params)
        {
            reservationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Reservations>> call = restAPI.getReservationsByTenantIdAndResidenceId(params[1], params[2]);
            try
            {
                Response<List<Reservations>> resp = call.execute();
                reservationsList.addAll(resp.body());
            }
            catch (IOException e){
                Log.i("",e.getMessage());
            }
            return reservationsList;
        }
    }

    public ArrayList<Reservations> getReservationsByTenantIdandResidenceId (String token, String tenantId, String residenceId) {
        getReservationsByTenantIdAndResidenceIdHttpRequestTask reservations = new getReservationsByTenantIdAndResidenceIdHttpRequestTask();
        reservations.execute(token, tenantId, residenceId);
        try {
            reservations.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return reservationsList;
    }

    private class postReservationHttpRequestTask extends AsyncTask<Object, Object, String>
    {
        @Override
        protected String doInBackground(Object... params)
        {
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.postReservation((Reservations)params[1]);
            try
            {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch (IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String postReservation(String token, Reservations reservation)
    {
        postReservationHttpRequestTask postReservationTask = new postReservationHttpRequestTask();
        postReservationTask.execute(token, reservation);
        try {
            postReservationTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return this.token;
    }
}
