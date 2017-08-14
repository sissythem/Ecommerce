package util;

import java.util.List;

import fromRESTful.Conversations;
import fromRESTful.Messages;
import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestAPI {
    /***** Check Token *****/

    @GET("users/checktoken")
    Call<Boolean> checkTokenExpired();

    /***** Images Facade Methods *****/
    @Multipart
    @POST("images/profilepic/{id}")
    Call<String> uploadProfileImg(@Path("id")Integer id, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Multipart
    @POST("images/residence/{id}")
    Call<String> uploadResidenceImg(@Path("id")Integer id, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    /***** Users Facade Methods *****/
    @GET("users/{userId}")
    Call<Users> getUserById(@Path("userId") String userId);

    @GET("users/username")
    Call<List<Users>> getUserByUsername(@Query("username") String username);

    @GET("users/email")
    Call<List<Users>> getUserByEmail(@Query("email") String email);

    @GET("users/login")
    Call<String> getLoginUser(@Query("username") String username, @Query("password") String password);

    @POST("users/register")
    Call<String> postUser(@Body Users user);

    @PUT("users/put/{id}")
    Call<String> editUserById(@Path("id") int id, @Body Users user);

    @DELETE("users/delete/{id}")
    Call<String> deleteUserById(@Path("id") String id);

    @GET("images/img/{name}")
    Call<ResponseBody> getUserImage(@Path("name")String name);

    /** Searches Facade Methods **/
    @GET("searches/city")
    Call<List<Searches>> getCitiesByUserId(@Query("userId") String userId);

    /** Residences Facade Methods **/
    @GET("residences/city")
    Call<List<Residences>> getResidenceByCity(@Query("city") String city);

    @GET("residences/search")
    Call<List<Residences>> getSearchResidences(@Query("userId") String userId, @Query("city") String city, @Query("startDate") String startDate,
                                               @Query("endDate") String endDate, @Query("guests") String guests);

    @GET("residences/host")
    Call<List<Residences>> getResidencesByHostId(@Query("hostId") String hostId);

    @GET("residences/{residenceId}")
    Call<Residences> getResidencesById (@Path("residenceId") String residenceId);

    @DELETE("residences/delete/{id}")
    Call<String> deleteResidenceById(@Path("id") String id);

    @PUT("residences/put/{id}")
    Call<String> editResidenceById(@Path("id") Integer id, @Body Residences residence);

    @POST("residences/add")
    Call<String> postResidence(@Body Residences residence);

    @GET("residences")
    Call<List<Residences>> getAllResidences();

    /***** Messages Facade Methods *****/

    @GET("messages/conversation")
    Call<List<Messages>> getMessagesByConversation(@Query("conversationId") String conversationId);

    @POST("messages/addmessage")
    Call<String> postMessage(@Body Messages message);


    /** Conversations Facade Methods **/

    @GET("conversations/user")
    Call<List<Conversations>> getConversations(@Query("userId") String userId);

    @GET("conversations/{conversationId}")
    Call<Conversations> getConversationById(@Path("conversationId") String conversationId);

    @GET("conversations/last")
    Call<List<Conversations>> lastConversationEntry(@Query("senderId") String senderId, @Query("receiverId") String receiverId);

    @GET("conversations/residence/{id}/{user}")
    Call<List<Conversations>> getConversationByResidenceId(@Path("id") String residenceId, @Path("user") String userId);

    @GET("conversations/update_conversation")
    Call<List<Conversations>> updateConversation(@Query("read") String isRead, @Query("type") String userType, @Query("id") String id);

    @POST("conversations/add")
    Call<String> postConversation(@Body Conversations conversation);

    /** Reservations Facade Methods **/

    @GET("reservations/tenant")
    Call<List<Reservations>> getReservationsByTenantId(@Query("tenantId") String tenantId);

    @GET("reservations/residence")
    Call<List<Reservations>> getReservationsByResidenceId(@Query("residenceId") String residenceId);

    @DELETE("reservations/delete/{residenceId}")
    Call<Void> deleteReservationsByResidence(@Path("residenceId") String residenceId);

    @GET("reservations/comment")
    Call<List<Reservations>> getReservationsByTenantIdAndResidenceId(@Query("tenantId") String tenantId, @Query("residenceId") String residenceId);

    @POST("reservations/makereservation")
    Call<String> postReservation(@Body Reservations reservation);


    /** Reviews Facade Methods **/

    @GET("reviews")
    Call<List<Reviews>> getReviews();

    @GET("reviews/residence")
    Call<List<Reviews>> getReviewsByResidence(@Query("residenceId") String residenceId);

    @GET("reviews/tenant")
    Call<List<Reviews>> getReviewsByTenant(@Query("tenantId") String tenantId);

    @DELETE("reviews/delete/{residenceId}")
    Call<Void> deleteReviewsByResidence(@Path("residenceId") String residenceId);

    @POST("reviews/postreview")
    Call<String> postReview(@Body Reviews review);
}