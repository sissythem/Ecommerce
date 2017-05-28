package util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sissy on 20/5/2017.
 */


public class EditProfileParameters {
    private Map<String, String> EditProfileParams;
    public EditProfileParameters (String userId, String firstname, String lastname, String phonenumber, String email, String username, String password, String photo, String country,
                                  String city, String birthdate, String about){

        EditProfileParams = new HashMap<>();
        EditProfileParams.put("id", userId);
        EditProfileParams.put("firstName", firstname);
        EditProfileParams.put("lastName", lastname);
        EditProfileParams.put("phoneNumber", phonenumber);
        EditProfileParams.put("email", email);
        EditProfileParams.put("username", username);
        EditProfileParams.put("password", password);
        EditProfileParams.put("country", country);
        EditProfileParams.put("city", city);
        EditProfileParams.put("photo", photo);
        EditProfileParams.put("about", about);
        EditProfileParams.put("birthDate", birthdate);
        EditProfileParams.put("host", "0");
    }


    public String getEditProfileParameters() {
        JSONObject jsonparams = new JSONObject(EditProfileParams);
        return jsonparams.toString();
    }
}
