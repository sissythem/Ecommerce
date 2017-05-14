package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import util.RegisterParameters;
import util.RestCallManager;
import util.RestCallParameters;

/**
 * Created by sissy on 30/4/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE =1;

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;
    Context c;

    ImageView imageToUpload;
    EditText uploadImageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=this;
        // Set View to activity_register.xml
        setContentView(R.layout.activity_register);

        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        //Create variables for storing user input
        final EditText firstname = (EditText) findViewById(R.id.firstname);
        final EditText lastname = (EditText) findViewById(R.id.lastname);
        final EditText phonenumber = (EditText) findViewById(R.id.phonenumber);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        final Button bregister = (Button) findViewById(R.id.register);
        final TextView loginlink = (TextView) findViewById(R.id.loginlink);

        //link the text view loginlink to the LoginActivity in case user has already an account
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginintent);
            }
        });

        imageToUpload = (ImageView)findViewById(R.id.imageToUpload);

        imageToUpload.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        //when register button is clicked, data are sent to be stored to the database
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gets user input in string format
                final String firstName = firstname.getText().toString();
                final String lastName = lastname.getText().toString();
                final String phoneNumber = phonenumber.getText().toString();
                final String Email = email.getText().toString();
                final String Username = username.getText().toString();
                final String Password = password.getText().toString();
                final String ConfirmPassword = confirmpassword.getText().toString();
                boolean userIsNew=false;
                boolean emailIsNew=false;

                //check if user has filled all fields of the registration form
                if(Username.length() == 0 || firstName.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0 || Email.length() == 0 || Password.length() == 0
                        || ConfirmPassword.length() == 0)
                {
                    //if something is not filled in, user must fill again the form
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if password and confirmation of password are equal
                if(!Password.equals(ConfirmPassword))
                {
                    //if there is a mismatch a message appears and user must fill in again the fields
                    Toast.makeText(c, "Passwords do not match, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                userIsNew = checkUsername(Username);
                emailIsNew = checkEmail(Email);
                //if username and email are new the application sends the data with sendPOST method to be stored in the database
                if(userIsNew && emailIsNew) {
                        boolean success = PostResult(firstName, lastName, phoneNumber, Email, Username, Password);
                    if (success) {
                        //if data are stored successfully in the data base, the user is now logged in and the home activity starts
                        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
                        editor = sharedPrefs.edit();
                        editor.putBoolean("userLoggedInState", true);
                        editor.putString("currentLoggedInUser", Username);
                        editor.commit();

                        //user can continue to the Home Screen
                        Intent homeintent = new Intent(RegisterActivity.this, HomeActivity.class);
                        try {
                            RegisterActivity.this.startActivity(homeintent);
                            finish();
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            ex.printStackTrace();
                        }

                    } else {
                        Toast.makeText(c, "Registration failed, please try again!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if (!userIsNew)
                {
                    Toast.makeText(c, "Username already exists, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!emailIsNew)
                {
                    Toast.makeText(c, "Email already exists, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }

    public boolean PostResult(String firstName, String lastName, String phoneNumber, String Email, String Username, String Password) {
        boolean success = true;
        String usersposturl = "http://192.168.1.6:8080/ecommerce_restful/webresources/users";
        RegisterParameters UserParameters = new RegisterParameters(firstName, lastName, phoneNumber, Email, Username, Password);

        RestCallManager userpost = new RestCallManager();
        RestCallParameters postparameters = new RestCallParameters(usersposturl, "POST", "", UserParameters.getRegisterParameters());

//        ArrayList<String> PostResponse ;
        String response;

        userpost.execute(postparameters);
//            PostResponse = userpost.get(1000, TimeUnit.SECONDS);
        response = (String)userpost.getRawResponse().get(0);
//            String result = PostResponse.get(0);
        if (response.equals("OK")) ;
        else success = false;

        return success;
    }

    public boolean checkUsername (String Username)
    {
        boolean userIsNew = false;
        //check if username already exist in the database
        //calls the function from RESTful with path users/username and as parameter user's input
        String usernameurl = "http://192.168.1.6:8080/ecommerce_restful/webresources/users/username?username=" + Username;
        //create an object of type RestCallManager to get the result of the query
        RestCallManager UsernameManager = new RestCallManager();
        RestCallParameters usernameparameters = new RestCallParameters(usernameurl, "GET", "JSON", "");

        UsernameManager.execute(usernameparameters);

        ArrayList<JSONObject> jsonArrayUsername = UsernameManager.getSingleJSONArray();
        if(jsonArrayUsername.size() == 0) userIsNew = true;
        return userIsNew;

    }

    public boolean checkEmail (String Email){
        boolean emailIsNew=false;
        //same process for email
        String emailurl = "http://192.168.1.6:8080/ecommerce_restful/webresources/users/email?email=" + Email;
        RestCallManager EmailManager = new RestCallManager();
        RestCallParameters emailparameters = new RestCallParameters(emailurl, "GET", "JSON", "");
        EmailManager.execute(emailparameters);

        ArrayList<JSONObject> jsonArrayEmail = EmailManager.getSingleJSONArray();
        if(jsonArrayEmail.size() == 0) emailIsNew = true;
        return  emailIsNew;
    }

    @Override
    public void onBackPressed() {
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        if (isUserLoggedIn) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            super.onBackPressed();
            return;
        }
    }
}


