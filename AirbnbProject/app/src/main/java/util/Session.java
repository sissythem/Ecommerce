package util;
/** Object containing the SharedPreferences: username, token, userLoggedinState **/
public class Session {

    private String token;
    private String username;
    private Boolean userLoggedInState;


    public Session() {}

    public Session(String token, String username, Boolean userLoggedInState) {
        this.token = token;
        this.username = username;
        this.userLoggedInState = userLoggedInState;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) { this.username = username; }

    public Boolean getUserLoggedInState() {
        return userLoggedInState;
    }
    public void setUserLoggedInState(Boolean userLoggedInState) { this.userLoggedInState = userLoggedInState; }
}
