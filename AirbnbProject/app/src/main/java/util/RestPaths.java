package util;

/**
 * Created by sissy on 15/5/2017.
 */

public class RestPaths
{
    public static final String restPath = "http://192.168.1.6:8080/ecommerce_restful/webresources/";

    public static String AllUsers                   =   restPath + "users";
    public static String UserByUsername             =   restPath + "users/username";
    public static String UserByEmail                =   restPath + "users/email";
    public static String UserByUsernameAndPassword  =   restPath + "users/login";
    public static String ResidenceByCity            =   restPath + "residences/city";
    public static String CitiesByUser               =   restPath + "searches/city";
    public static String AllReviews                 =   restPath + "reviews";
    public static String ReviewsByResidence         =   restPath + "reviews/residence";
    public static String EditUser                   =   restPath + "users/put";
    public static String DeleteUser                 =   restPath + "users/delete";
    public static String ResidencesByHostId         =   restPath + "residences/hostId";
    public static String AllResidences              =   restPath + "residences";
    public static String FrontResidences            =   restPath + "residences/front";
    public static String getReservationsByTenant    =   restPath + "reservations/tenant";
    public static String DeleteResidence            =   restPath + "residences/delete";
    public static String AllReservations            =   restPath + "reservations";
    public static String DeleteReviews              =   restPath + "reviews/delete";
    public static String DeleteReservations         =   restPath + "reservations/delete";

    public RestPaths() {}

    public static String getUserByUsername(String username) {
        return UserByUsername + "?username=" + username;
    }
    public static String getUserByEmail(String email) {
        return UserByEmail + "?username=" + email;
    }
    public static String getLoginUser(String username, String password) {
        return UserByUsernameAndPassword + "?username=" + username + "&email=" + password;
    }
    public static String getResidenceByCity(String city) {
        return ResidenceByCity + "?city=" + city;
    }
    public static String getReviewsByResidence(String residence_id) {
        return ReviewsByResidence + "?residenceId=" + residence_id;
    }
    public static String getCitiesByUser(String user_id) {
        return CitiesByUser + "?userId=" + user_id;
    }
    public static String editUserById (String id){
        return EditUser;
    }
    public static String deleteUserById (String id){ return DeleteUser + "/" +id;}
    public static String getResidencesByHostId (String id) { return ResidencesByHostId + "?hostId=" + id;}
    public static String getResidencesById (int residenceId){ return AllResidences + "/" +residenceId;}
    public static String getFrontResidences(String user_id, String city, String start_date, String end_date, Integer guests) {
        return FrontResidences + "?userId=" + user_id + "&city=" + city + "&startDate=" + start_date + "&endDate=" + end_date + "&guests=" + guests;
    }
    public static String getUserById (int userId){ return  AllUsers +"/" + userId;}
    public static String getReservationsByTenantId (int tenantId){ return  getReservationsByTenant + "?tenantId=" + tenantId;}
    public static String deleteResidenceById (String id) { return DeleteResidence +"/" + id;}
    public static String deleteReviewsByResidence (String residenceId) { return DeleteReviews + "?residenceId=" + residenceId;}
    public static String deleteReservationsByResidence(String residenceId){ return DeleteReservations + "?residenceId=" + residenceId; }
}

