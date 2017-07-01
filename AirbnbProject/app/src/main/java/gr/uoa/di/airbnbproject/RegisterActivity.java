package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import fromRESTful.Users;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.updateSessionData;

public class RegisterActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE =1;
    String token;
    /*public String getToken() {
        return token;
    }*/

    Context c;
    boolean success;

    ImageView imageToUpload;
    private int mYear, mMonth, mDay;
    Session sessionData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=this;
        // Set View to activity_register.xml
        setContentView(R.layout.activity_register);

        //Create variables for storing user input
        final EditText firstname        = (EditText) findViewById(R.id.firstname);
        final EditText lastname         = (EditText) findViewById(R.id.lastname);
        final TextView birthdate        = (TextView)findViewById(R.id.tvBirthDate);
        final ImageButton btnBirthDate  = (ImageButton)findViewById(R.id.btnBirthDate);
        final EditText phonenumber      = (EditText) findViewById(R.id.phonenumber);
        final EditText email            = (EditText) findViewById(R.id.email);
        final EditText username         = (EditText) findViewById(R.id.username);
        final EditText password         = (EditText) findViewById(R.id.password);
        final EditText confirmpassword  = (EditText) findViewById(R.id.confirmpassword);
        final Button bregister          = (Button) findViewById(R.id.register);
        final TextView loginlink        = (TextView) findViewById(R.id.loginlink);

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

        btnBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnBirthDate) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            birthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        //when register button is clicked, data are sent to be stored to the database
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gets user input in string format
                Log.w("","Hardcoding a registration user");
                final String firstName          = "test_firstname";
                final String lastName           = "test_lastname";
                final String BirthDate          = "3-7-2017";
                final String phoneNumber        = "123123123";
                final String Email              = "email@domain.com";
                final String Username           = "usernametest";
                final String Password           = "password";
                final String ConfirmPassword    = "password";

//                final String firstName          = firstname.getText().toString();
//                final String lastName           = lastname.getText().toString();
//                final String BirthDate          = birthdate.getText().toString();
//                final String phoneNumber        = phonenumber.getText().toString();
//                final String Email              = email.getText().toString();
//                final String Username           = username.getText().toString();
//                final String Password           = password.getText().toString();
//                final String ConfirmPassword    = confirmpassword.getText().toString();


                //check if user has filled all fields of the registration form
                if(Username.length() == 0 || firstName.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0 || Email.length() == 0 || Password.length() == 0
                        || ConfirmPassword.length() == 0 || BirthDate.length() == 0) {
                    //if something is not filled in, user must fill again the form
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if password and confirmation of password are equal
                if(!Password.equals(ConfirmPassword)) {
                    //if there is a mismatch a message appears and user must fill in again the fields
                    Toast.makeText(c, "Passwords do not match, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date bdate = Utils.ConvertStringToDate(BirthDate,Utils.APP_DATE_FORMAT);



                token = PostResult(firstName, lastName, Username, Password, Email, phoneNumber, bdate);
                if(token.equals("username exists")){
                    Toast.makeText(c, "Username already exists, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(token.equals("email exists")){
                    Toast.makeText(c, "Email already exists, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( token!=null && !token.isEmpty() &&  (!token.equals("error"))) {
                    //if data are stored successfully in the data base, the user is now logged in and the home activity starts
                    sessionData = new Session(token, Username, true);
                    updateSessionData(RegisterActivity.this, sessionData);
                    //user can continue to the Home Screen
                    Intent homeintent = new Intent(RegisterActivity.this, HomeActivity.class);
                    try {
                        Bundle btoken = new Bundle();
                        homeintent.putExtras(btoken);
                        RegisterActivity.this.startActivity(homeintent);
                        finish();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                }
                } else
                {
                    Toast.makeText(c, "Registration failed, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }

    public String PostResult(String firstName, String lastName, String username, String password, String email, String phoneNumber, Date bdate) {
        Users UserParameters = new Users(firstName, lastName, username, password, email, phoneNumber, bdate);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.postUser(UserParameters);
        return token;
    }

    @Override
    public void onBackPressed() {
        if (sessionData.getUserLoggedInState()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            super.onBackPressed();
            return;
        } else {
            Intent greetingIntent = new Intent(this, GreetingActivity.class);
            startActivity(greetingIntent);
            super.onBackPressed();
        }
    }
}