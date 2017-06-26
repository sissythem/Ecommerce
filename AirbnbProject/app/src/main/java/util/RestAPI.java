package util;

import java.util.List;

import fromRESTful.Conversations;
import fromRESTful.Messages;
import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sissy on 25/6/2017.
 */

public interface RestAPI
{
    /** Users Facade Methods **/
    @GET("users/{userId}")
    Call<List<Users>> getUserById(@Path("userId") int userId);

    @GET("users/username")
    Call<List<Users>> getUserByUsername(@Query("username") String username);

    @GET("users/email")
    Call<List<Users>> getUserByEmail(@Query("email") String email);

    @GET("users/login")
    Call<List<Users>> getLoginUser(@Query("username") String username, @Query("password") String password);

    @POST("users")
    @FormUrlEncoded
    Call<Users> postUser(@Body Users user);

    @PUT("users/put/{id}")
    Call<Users> editUserById(@Path("userId") int userId);

    @DELETE("users/delete/{id}")
    Call<Users> deleteUserById(@Path("userId") int userId);


    /** Searches Facade Methods **/
    @GET("searches/city")
    Call<List<Searches>> getCitiesByUserId(@Query("userId") int userId);


    /** Residences Facade Methods **/
    @GET("residences/city")
    Call<List<Residences>> getResidenceByCity(@Query("city") String city);

    @GET("residences/search")
    Call<List<Residences>> getSearchResidences(@Query("userId") String userId, @Query("city") String city, @Query("startDate") String startDate,
                                         @Query("endDate") String endDate, @Query("guests") String guests);

    @GET("residences/host")
    Call<List<Residences>> getResidencesByHostId(@Query("hostId") int hostId);

    @GET("residences/{residenceId}")
    Call<List<Residences>> getResidencesById (@Path("residenceId") int residenceId);

    @DELETE("residences/delete/{id}")
    Call<Residences> deleteResidenceById(@Path("id") int id);

    @PUT("residences/edit/{id}")
    Call<Residences> editResidenceById(@Path("id") int id);

    @POST("residences")
    @FormUrlEncoded
    Call<Residences> postResidence(@Body Residences residence);

    @GET("residences")
    Call<List<Residences>> getAllResidences();


    /** Messages Facade Methods **/

    @GET("messages/conversation")
    Call<List<Messages>> getMessagesByConversation(@Query("conversationId") int conversationId);

    @POST("messages/addmessage")
    Call<Messages> postMessage();


    /** Conversations Facade Methods **/

    @GET("conversations/user")
    Call<List<Conversations>> getConversations(@Query("userId") int userId);

    @GET("conversations/{conversationId}")
    Call<List<Conversations>> getConversationById(@Path("conversationId") int conversationId);

    @GET("conversations/last")
    Call<List<Conversations>> lastConversationEntry(@Query("senderId") int senderId, @Query("receiverId") int receiverId);

    @GET("conversations/residence")
    Call<List<Conversations>> getConversationByResidenceId(@Query("residenceId") int residenceId);

    @GET("conversations/update_conversation")
    Call<List<Conversations>> updateConversation(@Query("read") String isRead, @Query("type") String userType, @Query("id") String id);


    /** Reservations Facade Methods **/

    @GET("reservations/tenant")
    Call<List<Reservations>> getReservationsByTenantId(@Query("tenantId") int tenantId);

    @GET("reservations/residence")
    Call<List<Reservations>> getReservationsByResidenceId(@Query("residenceId") int residenceId);

    @DELETE("reservations/delete/{residenceId}")
    Call<Reservations> deleteReservationsByResidence(@Path("residenceId") int residenceId);

    @GET("reservations/comment")
    Call<List<Reservations>> getReservationsByTenantIdAndResidenceId(@Query("tenantId") int tenantId, @Query("residenceId") int residenceId);


    /** Reviews Facade Methods **/

    @GET("reviews")
    Call<List<Reviews>> getReviews();

    @GET("reviews/residence")
    Call<List<Reviews>> getReviewsByResidence(@Query("residenceId") int residenceId);

    @GET("reviews/tenant")
    Call<List<Reviews>> getReviewsByTenant(@Query("tenantId") int tenantId);

    @DELETE("reviews/delete/{residenceId}")
    Call<Reviews> deleteReviewsByResidence(@Path("residenceId") int residenceId);

    @POST("reviews")
    @FormUrlEncoded
    Call<Reviews> postReview(@Body Reviews review);
}
