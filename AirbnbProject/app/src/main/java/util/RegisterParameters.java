package util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterParameters {
    private Map<String, String> RegisterParameters;

    public RegisterParameters(String firstname, String lastname, String phonenumber, String email, String username, String password, String birthdate){

        RegisterParameters = new HashMap<>();
        RegisterParameters.put("firstName", firstname);
        RegisterParameters.put("lastName", lastname);
        RegisterParameters.put("phoneNumber", phonenumber);
        RegisterParameters.put("email", email);
        RegisterParameters.put("username", username);
        RegisterParameters.put("password", password);
        RegisterParameters.put("country", "greece");
        RegisterParameters.put("city", "athens");
        RegisterParameters.put("photo", "photo");
        RegisterParameters.put("about", "about");
        RegisterParameters.put("birthDate", birthdate);
        RegisterParameters.put("host", "0");
    }

    public String getRegisterParameters() {
        JSONObject jsonparams = new JSONObject(RegisterParameters);
        return jsonparams.toString();
    }
}