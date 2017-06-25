package util;

public class RestPaths {
    public static final String restPath = "http://192.168.1.6:8080/ecommerce_rest/webresources/";

    public static String AllUsers           = restPath + "users/";
    public static String AllSearches        = restPath + "searches/";
    public static String AllResidences      = restPath + "residences/";
    public static String AllMessages        = restPath + "messages/";
    public static String AllConversations   = restPath + "conversations/";
    public static String AllReservations    = restPath + "reservations/";
    public static String AllReviews         = restPath + "reviews/";

    public RestPaths() {}

    /** Users Facade Methods **/
    public static String getUserByUsername(String username) { return AllUsers + "username?username=" + username; }
    public static String getUserByEmail(String email) { return AllUsers + "email?email=" + email; }
    public static String getLoginUser(String username, String password) { return AllUsers + "login?username=" + username + "&password=" + password; }
    public static String getUserById(int userId){ return  AllUsers + userId; }
    public static String editUserById (Integer id) { return AllUsers +"put?id=" + id; }
    public static String deleteUserById (String id){ return AllUsers + "delete/" +id; }

    /** Searches Facade Methods **/
    public static String getCitiesByUserId(Integer user_id) { return AllSearches + "city?userId=" + user_id; }

    /** Residences Facade Methods **/
    public static String getResidenceByCity(String city) { return AllResidences + "city?city=" + city; }
    public static String getSearchResidences(String user_id, String city, String start_date, String end_date, Integer guests) {
        return AllResidences + "search?userId=" + user_id + "&city=" + city + "&startDate=" + start_date + "&endDate=" + end_date + "&guests=" + guests;
    }
    public static String getResidencesByHostId (Integer id) { return AllResidences + "host?hostId=" + id; }
    public static String getResidencesById (int residenceId){ return AllResidences +residenceId;}
    public static String deleteResidenceById (String id) { return AllResidences +"delete/" + id; }
    public static String editResidenceById (String id) { return AllResidences +"edit/" + id;}


    /** Messages Facade Methods **/
    public static String getMessagesByConversation(Integer conversationId) { return AllMessages + "conversation?convId="+conversationId; }
    public static String addNewMessage() {
        return AllMessages + "addmessage";
    }

    /** Conversations Facade Methods **/
    public static String getConversations(String userId) { return AllConversations + "user?userId="+userId; }
    public static String getConversationById(Integer conversationId) { return AllConversations + conversationId; }
    public static String lastConversationEntry(String senderID, String receiverID) { return AllConversations + "last?senderId="+senderID+"&receiverId="+receiverID; }
    public static String getConversationByResidenceId(String residenceId) { return AllConversations + "residence/"+residenceId; }
    public static String updateConversation(String isRead, String userType, String id) { return AllConversations + "update_conversation?read="+isRead+"&type="+userType+"&id="+id; }

    /** Reservations Facade Methods **/
    public static String getReservationsByTenantId (int tenantId){ return AllReservations + "tenant?tenantId=" + tenantId; }
    public static String getReservationsByResidenceId (int residenceId){ return AllReservations + "residence?residenceId=" + residenceId;}
    public static String deleteReservationsByResidence(String residenceId){ return AllReservations + "delete?residenceId=" + residenceId; }

    /** Reviews Facade Methods **/
    public static String getReviewsByResidence(Integer residence_id) { return AllReviews + "residence?residenceId=" + residence_id; }
    public static String deleteReviewsByResidence(String residenceId) { return AllReservations + "delete?residenceId=" + residenceId; }
}