package util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fromRESTful.Conversations;
import fromRESTful.Images;
import fromRESTful.Messages;
import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;
import retrofit2.Call;
import retrofit2.Response;
/** All calls performed using Retrofit 2.0 and AsyncTask **/
public class RetrofitCalls {
    ArrayList<Residences> residencesList;
    ArrayList<Reviews> reviewsList;
    ArrayList<Reservations> reservationsList;
    ArrayList<Searches> searchesList;
    ArrayList<Users> usersList;
    ArrayList<Conversations> conversationsList;
    ArrayList<Messages> messagesList;
    ArrayList<Images> photosList;

    Residences residence;
    Conversations conversation;
    Users user;
    String token;
    Boolean flag;

    String unreadMessages = "0";

    /** Call to check if token is still valid **/
    private class checkTokenHttpRequestTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<Boolean> call = restAPI.checkTokenExpired();
            try {
                Response<Boolean> resp = call.execute();
                flag = resp.body();
            } catch(IOException e){
                Log.i("",e.getMessage());
            }
            return flag;
        }
    }

    public Boolean isTokenExpired(String token) {
        checkTokenHttpRequestTask checktoken = new checkTokenHttpRequestTask();
        checktoken.execute(token);
        try{
            checktoken.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return flag;
    }

    /** Calls for User **/
    private class deleteUserImageHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            String token="";
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteUserImg((Integer)params[1]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String deleteUserImage(String token, Integer id) {
        deleteUserImageHttpRequestTask deleteUserImage = new deleteUserImageHttpRequestTask();
        deleteUserImage.execute(token, id);
        try {
            token = deleteUserImage.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return usersList;
    }

    private class postUserHttpRequestTask extends AsyncTask<Users, Users, String>
    {
        @Override
        protected String doInBackground(Users... params){
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            Call<String> call = restAPI.postUser(params[0]);
            try{
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String postUser(Users user){
        postUserHttpRequestTask postUserTask = new postUserHttpRequestTask();
        postUserTask.execute(user);
        try{
            postUserTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

    private class editUserHttpRequestTask extends AsyncTask<Object, Object, String>
    {
        @Override
        protected String doInBackground(Object... params){
            try{
                RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
                Call<String> call = restAPI.editUserById((Integer)params[1],(Users)params[2]);

                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            catch (Exception e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String editUser(String token, int userId, Users user){
        editUserHttpRequestTask editUserTask = new editUserHttpRequestTask();
        editUserTask.execute(token, userId, user);
        try{
            editUserTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
            }
            return reviewsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> reviews) {}

    }

    public ArrayList<Reviews> getReviewsByResidenceId(String token, String residenceId){
        getReviewsByResidenceIdHttpRequestTask reviewsByResidenceId = new getReviewsByResidenceIdHttpRequestTask();
        reviewsByResidenceId.execute(token, residenceId);
        try {
            reviewsByResidenceId.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return reviewsList;
    }

    private class getReviewsByTenantIdHttpRequestTask extends AsyncTask<String, String, ArrayList<Reviews>> {
        @Override
        protected ArrayList<Reviews> doInBackground (String... params) {
            reviewsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<List<Reviews>> call = restAPI.getReviewsByTenant(params[1]);
            try {
                Response<List<Reviews>> resp = call.execute();
                reviewsList.addAll(resp.body());
            }
            catch (IOException e){
                Log.i("",e.getMessage());
            }
            return reviewsList;
        }
    }

    public ArrayList<Reviews> getReviewsByTenantId(String token, String tenantId) {
        getReviewsByTenantIdHttpRequestTask reviewsByTenant = new getReviewsByTenantIdHttpRequestTask();
        reviewsByTenant.execute(token, tenantId);
        try {
            reviewsByTenant.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return reviewsList;
    }

    private class postReviewHttpRequestTask extends AsyncTask<Object, Object, String> {
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
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String postReview(String token, Reviews review) {
        postReviewHttpRequestTask postReviewTask = new postReviewHttpRequestTask();
        postReviewTask.execute(token, review);
        try {
            postReviewTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return this.token;
    }

    private class deleteReviewHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteReview((Integer) params[1]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }
    public String deleteReview(String token, Integer id) {
        deleteReviewHttpRequestTask deleteReview = new deleteReviewHttpRequestTask();
        deleteReview.execute(token, id);
        try {
            token = deleteReview.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

    /***** Calls for Residences *****/
    private class setMainResidencePhotoHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            String token="";
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.setMainResidencePhoto((Integer)params[1], (String)params[2]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String setMainResidencePhoto(String token, Integer id, String name) {
        setMainResidencePhotoHttpRequestTask setMainResidencePhoto = new setMainResidencePhotoHttpRequestTask();
        setMainResidencePhoto.execute(token, id, name);
        try {
            token = setMainResidencePhoto.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

    private class deleteResidenceImageHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            String token="";
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteResidenceImg((Integer)params[1], (String)params[2]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String deleteResidenceImage(String token, Integer id, String name) {
        deleteResidenceImageHttpRequestTask deleteResidenceImage = new deleteResidenceImageHttpRequestTask();
        deleteResidenceImage.execute(token, id, name);
        try {
            token = deleteResidenceImage.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

    private class getResidencePhotosIdHttpRequestTask extends AsyncTask<Object, Object, ArrayList<Images>> {
        @Override
        protected ArrayList<Images> doInBackground(Object... params){
            photosList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<List<Images>> call = restAPI.getResidencePhotos((Integer)params[1]);
            try {
                Response <List<Images>> resp = call.execute();
                photosList.addAll(resp.body());
            }
            catch (IOException e){
                Log.i("",e.getMessage());
            }
            return photosList;
        }
    }

    public ArrayList<Images> getResidencePhotos(String token, Integer residenceId){
        getResidencePhotosIdHttpRequestTask getResidencePhotos = new getResidencePhotosIdHttpRequestTask();
        getResidencePhotos.execute(token, residenceId);
        try {
            getResidencePhotos.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return photosList;
    }


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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
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
                Log.i("",e.getMessage());
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
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return residence;
    }

    private class postResidenceHttpRequestTask extends AsyncTask<Object, Object, String>
    {
        @Override
        protected String doInBackground(Object... params) {
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.postResidence((Residences)params[1]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String postResidence(String token, Residences residence) {
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

    private class editResidenceHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params){
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.editResidenceById((Integer)params[1],(Residences) params[2]);
            try{
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            System.out.println(token);
            return token;
        }
    }

    public String editResidence(String token, Integer residenceId, Residences residence){
        editResidenceHttpRequestTask editResidenceTask = new editResidenceHttpRequestTask();
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

    private class getRecommendationsHttPRequestTask extends AsyncTask<Object, Object, ArrayList<Residences>> {
        @Override
        protected ArrayList<Residences> doInBackground(Object... params) {
            residencesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<List<Residences>> call = restAPI.getSearchResidences((String)params[1], (String)params[2], (long)params[3], (long)params[4], (Integer)params[5]);
            try {
                Response<List<Residences>> resp = call.execute();
                residencesList.addAll(resp.body());
            }
            catch (IOException e) {
                Log.i("",e.getMessage());
            }
            return residencesList;
        }
    }

    public ArrayList<Residences> getRecommendations(String token, String username, String city, long startDate, long endDate, Integer guests) {
        getRecommendationsHttPRequestTask recommendations = new getRecommendationsHttPRequestTask();
        recommendations.execute(token, username, city, startDate, endDate, guests);
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


    /** Calls for Conversations **/

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

    private class getConversationByIdHttPRequestTask extends AsyncTask<Object, Object, Conversations> {
        @Override
        protected Conversations doInBackground(Object... params) {
            conversation = new Conversations();
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<Conversations> call = restAPI.getConversationById((Integer)params[1]);
            try {
                Response<Conversations> resp = call.execute();
                conversation = resp.body();
            } catch (IOException e) {
                Log.i("",e.getMessage());
            }
            return conversation;
        }
    }

    public Conversations getConversationById(String token, Integer id) {
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

    private class getConversationByResidenceIdHttPRequestTask extends AsyncTask<Object, Object, ArrayList<Conversations>> {
        @Override
        protected ArrayList<Conversations> doInBackground(Object... params) {
            conversationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<List<Conversations>> call = restAPI.getConversationByResidenceId((Integer)params[1], (Integer)params[2]);
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

    public ArrayList<Conversations> getConversationsByResidenceId(String token, Integer residenceId, Integer userId) {
        getConversationByResidenceIdHttPRequestTask conversationsByResidenceId = new getConversationByResidenceIdHttPRequestTask();
        conversationsByResidenceId.execute(token, residenceId, userId);
        try {
            conversationsByResidenceId.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return conversationsList;
    }

    private class getLastConversationHttPRequestTask extends AsyncTask<Object, Object, ArrayList<Conversations>> {
        @Override
        protected ArrayList<Conversations> doInBackground(Object... params) {
            conversationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<List<Conversations>> call = restAPI.lastConversationEntry((Integer)params[1], (Integer)params[2]);
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

    public ArrayList<Conversations> getLastConversation(String token, Integer senderId, Integer receiverId) {
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

    private class updateConversationHttpRequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            RestAPI restAPI = RestClient.getClient(params[0]).create(RestAPI.class);
            Call<String> call = restAPI.updateConversation(params[1], params[2], params[3]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e) {
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String updateConversation(String token, String isRead, String userType, String conversationId) {
        updateConversationHttpRequestTask updatedConversation = new updateConversationHttpRequestTask();
        updatedConversation.execute(token, isRead, userType, conversationId);
        try {
            updatedConversation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return this.token;
    }

    private class startConversationTaskHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params){
            try{
                RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
                Call<String> call = restAPI.postConversation((Conversations) params[1]);
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            catch (Exception e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String startConversation(String token, Conversations conversation){
        startConversationTaskHttpRequestTask startConversationTask = new startConversationTaskHttpRequestTask();
        startConversationTask.execute(token, conversation);
        try{
            startConversationTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return this.token;
    }

    private class deleteConversationHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteConversation((Integer) params[1], (Integer) params[2], (String) params[3]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }
    public String deleteConversation(String token, Integer id, Integer userId, String userType) {
        deleteConversationHttpRequestTask deleteConversation = new deleteConversationHttpRequestTask();
        deleteConversation.execute(token, id, userId, userType);
        try {
            token = deleteConversation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

    private class restoreConversationHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.restoreConversation((Integer) params[1], (Integer) params[2], (String) params[3]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }
    public String restoreConversation(String token, Integer id, Integer userId, String userType) {
        restoreConversationHttpRequestTask restoreConversation = new restoreConversationHttpRequestTask();
        restoreConversation.execute(token, id, userId, userType);
        try {
            token = restoreConversation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

    /** Calls for Messages**/

    private class countMessagesTaskHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params){
            try{
                RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
                Call<String> call = restAPI.countNewMessages((Integer)params[1]);
                Response<String> resp = call.execute();
                unreadMessages = resp.body();
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            catch (Exception e){
                Log.i("",e.getMessage());
            }
            return unreadMessages;
        }
    }

    public Integer countNewMessages(String token, Integer userId){
        countMessagesTaskHttpRequestTask countMessagesTask = new countMessagesTaskHttpRequestTask();
        countMessagesTask.execute(token, userId);
        try{
            countMessagesTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return Integer.parseInt(unreadMessages);
    }

    private class getMessagesByConversationHttpRequestTask extends AsyncTask<Object, Object, ArrayList<Messages>> {
        @Override
        protected ArrayList<Messages> doInBackground(Object... params) {
            messagesList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<List<Messages>> call = restAPI.getMessagesByConversation((Integer)params[1]);
            try {
                Response<List<Messages>> resp = call.execute();
                messagesList.addAll(resp.body());
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            return messagesList;
        }
    }

    public ArrayList<Messages> getMessagesByConversation(String token, Integer conversationId) {
        getMessagesByConversationHttpRequestTask messagesByConversation = new getMessagesByConversationHttpRequestTask();
        messagesByConversation.execute(token, conversationId);
        try {
            messagesByConversation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return messagesList;
    }

    private class sendMessageTaskHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params){
            try{
                RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
                Call<String> call = restAPI.postMessage((Messages)params[1]);
                Response<String> resp = call.execute();
                token = resp.body();
            } catch (IOException e){
                Log.i("",e.getMessage());
            }
            catch (Exception e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }

    public String sendMessage(String token, Messages message){
        sendMessageTaskHttpRequestTask sendMessageTask = new sendMessageTaskHttpRequestTask();
        sendMessageTask.execute(token, message);
        try{
            sendMessageTask.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return this.token;
    }

    private class deleteMessageHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteMessage((Integer) params[1], (Integer) params[2], (String) params[3]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }
    public String deleteMessage(String token, Integer id, Integer userId, String userType) {
        deleteMessageHttpRequestTask deleteMessage = new deleteMessageHttpRequestTask();
        deleteMessage.execute(token, id, userId, userType);
        try {
            token = deleteMessage.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }

    /** Calls for Reservations **/
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

    private class getReservationsByResidenceIdHttpRequestTask extends AsyncTask<Object, Object, ArrayList<Reservations>> {
        @Override
        protected ArrayList<Reservations> doInBackground(Object... params)
        {
            reservationsList = new ArrayList<>();
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<List<Reservations>> call = restAPI.getReservationsByResidenceId((Integer)params[1]);
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

    public ArrayList<Reservations> getReservationsByResidenceId (String token, Integer residenceId) {
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

    public String postReservation(String token, Reservations reservation) {
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

    private class deleteReservationHttpRequestTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            RestAPI restAPI = RestClient.getClient((String)params[0]).create(RestAPI.class);
            Call<String> call = restAPI.deleteReservation((Integer) params[1]);
            try {
                Response<String> resp = call.execute();
                token = resp.body();
            }
            catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }
    }
    public String deleteReservation(String token, Integer id) {
        deleteReservationHttpRequestTask deleteReservation = new deleteReservationHttpRequestTask();
        deleteReservation.execute(token, id);
        try {
            token = deleteReservation.get();
        } catch (InterruptedException e) {
            Log.i("",e.getMessage());
        } catch (ExecutionException e) {
            Log.i("",e.getMessage());
        }
        return token;
    }
}