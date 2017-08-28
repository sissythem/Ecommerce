package util;

import java.util.List;

import fromRESTful.Conversations;
import fromRESTful.Images;
import fromRESTful.Messages;
import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
/** Interface with all the calls used with Retrofit 2.0 **/
public interface RestAPI {
    /***** Check Token *****/

    @GET("users/checktoken")
    Call<Boolean> checkTokenExpired();

    /***** Images Facade Methods *****/
    @Multipart
    @PUT("images/profilepic/{id}")
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

    @PUT("images/deleteimg/profile/{id}")
    Call<String> deleteUserImg(@Path("id")Integer id);

    @GET("images/residence/{id}")
    Call<List<Images>> getResidencePhotos(@Path("id")Integer id);

    @DELETE("images/deleteimg/residence/{id}/{name}")
    Call<String> deleteResidenceImg(@Path("id")Integer id, @Path("name")String name);

    /** Searches Facade Methods **/
    @GET("searches/city")
    Call<List<Searches>> getCitiesByUserId(@Query("userId") String userId);

    /** Residences Facade Methods **/
    @PUT("residences/main/{id}/{name}")
    Call<String> setMainResidencePhoto(@Path("id")Integer id, @Path("name")String name);

    @GET("residences/city")
    Call<List<Residences>> getResidenceByCity(@Query("city") String city);

    @GET("residences/search")
    Call<List<Residences>> getSearchResidences(@Query("username") String username, @Query("city") String city, @Query("startDate") long startDate,
                                               @Query("endDate") long endDate, @Query("guests") Integer guests);

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

    @GET("messages/count/{user}")
    Call<String> countNewMessages(@Path("user") Integer user);

    @GET("messages/conversation/{id}")
    Call<List<Messages>> getMessagesByConversation(@Path("id") Integer id);

    @POST("messages/addmessage")
    Call<String> postMessage(@Body Messages message);

    @POST("messages/deletemsg/{id}/{user}/{type}")
    Call<String> deleteMessage(@Path("id") Integer id, @Path("user") Integer user, @Path("type") String type);

    /** Conversations Facade Methods **/

    @GET("conversations/user")
    Call<List<Conversations>> getConversations(@Query("userId") String userId);

    @GET("conversations/{id}")
    Call<Conversations> getConversationById(@Path("id") Integer id);

    @GET("conversations/last")
    Call<List<Conversations>> lastConversationEntry(@Query("senderId") Integer senderId, @Query("receiverId") Integer receiverId);

    @GET("conversations/residence/{id}/{user}")
    Call<List<Conversations>> getConversationByResidenceId(@Path("id") Integer residenceId, @Path("user") Integer userId);

    @POST("conversations/update_conversation")
    Call<String> updateConversation(@Query("read") String isRead, @Query("type") String userType, @Query("id") String id);

    @POST("conversations/deletecnv/{id}/{user}/{type}")
    Call<String> deleteConversation(@Path("id") Integer id, @Path("user") Integer user, @Path("type") String type);

    @POST("conversations/restore/{id}/{user}/{type}")
    Call<String> restoreConversation(@Path("id") Integer id, @Path("user") Integer user, @Path("type") String type);

    @POST("conversations/add")
    Call<String> postConversation(@Body Conversations conversation);

    /** Reservations Facade Methods **/

    @GET("reservations/tenant")
    Call<List<Reservations>> getReservationsByTenantId(@Query("tenantId") String tenantId);

    @GET("reservations/residence")
    Call<List<Reservations>> getReservationsByResidenceId(@Query("residenceId") Integer residenceId);

    @DELETE("reservations/delete/{residenceId}")
    Call<Void> deleteReservationsByResidence(@Path("residenceId") String residenceId);

    @GET("reservations/comment")
    Call<List<Reservations>> getReservationsByTenantIdAndResidenceId(@Query("tenantId") String tenantId, @Query("residenceId") String residenceId);

    @POST("reservations/makereservation")
    Call<String> postReservation(@Body Reservations reservation);

    @DELETE("reservations/delete/{id}")
    Call<String> deleteReservation(@Path("id") Integer id);

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

    @DELETE("reviews/delete/{id}")
    Call<String> deleteReview(@Path("id") Integer id);
}