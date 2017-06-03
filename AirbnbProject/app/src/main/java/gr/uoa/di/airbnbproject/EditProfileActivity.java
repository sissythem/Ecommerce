package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fromRESTful.Users;
import util.EditProfileParameters;
import util.RestCallManager;
import util.RestCallParameters;
import util.RestCalls;
import util.RestPaths;
import util.Utils;

public class EditProfileActivity extends AppCompatActivity {

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    String username;
    String userId;
    Boolean user;
    String email;

    Context c;

    ImageButton bback;
    Button bsave;
    ImageButton btnBirthDate;

    EditText etName;
    EditText etLastName;
    EditText etPhoneNumber;
    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    EditText etCountry;
    EditText etCity;
    TextView tvBirthDate;
    EditText etAbout;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        username = sharedPrefs.getString("currentLoggedInUser", "");

        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            return;
        }
        setContentView(R.layout.activity_edit_profile);

        c=this;
        Toolbar backToolbar = (Toolbar) findViewById(R.id.backToolbar);
        setSupportActionBar(backToolbar);
        getSupportActionBar().setTitle(null);

        Toolbar footerToolbar = (Toolbar) findViewById(R.id.footerToolbar);
        setSupportActionBar(footerToolbar);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        bsave = (Button)findViewById(R.id.post);
        bback = (ImageButton) findViewById(R.id.ibBack);

        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type",user);
                profileIntent.putExtras(buser);
                try {
                    startActivity(profileIntent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        Users loggedinUser = RestCalls.getUser(username);
        userId = loggedinUser.getId().toString();
        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCountry = (EditText)findViewById(R.id.etCountry);
        etCity = (EditText)findViewById(R.id.etCity);
        tvBirthDate = (TextView) findViewById(R.id.tvBirthDate);
        etAbout = (EditText)findViewById(R.id.etAbout);
        btnBirthDate = (ImageButton)findViewById(R.id.btnBirthDate);

        etName.setText(loggedinUser.getFirstName());
        etLastName.setText(loggedinUser.getLastName());
        etPhoneNumber.setText(loggedinUser.getPhoneNumber());
        etEmail.setText(loggedinUser.getEmail());
        email= etEmail.getText().toString();
        etUsername.setText(loggedinUser.getUsername());
        etPassword.setText(loggedinUser.getPassword());
        etCountry.setText(loggedinUser.getCountry());
        etCity.setText(loggedinUser.getCity());

        String date ="";
        Date birthDate = loggedinUser.getBirthDate();

        if(birthDate !=null){
            SimpleDateFormat newDateFormat = new SimpleDateFormat(Utils.APP_DATE_FORMAT);
            date = newDateFormat.format(birthDate);
        }
        tvBirthDate.setText(date);
        etAbout.setText(loggedinUser.getAbout());

        btnBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnBirthDate) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tvBirthDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    public boolean checkEmail (String Email){
        boolean emailIsNew=false;
        //same process for email
        String emailurl = RestPaths.getUserByEmail(Email);
        RestCallManager EmailManager = new RestCallManager();
        RestCallParameters emailparameters = new RestCallParameters(emailurl, "GET", "JSON", "");
        EmailManager.execute(emailparameters);

        ArrayList<JSONObject> jsonArrayEmail = EmailManager.getSingleJSONArray();
        if(jsonArrayEmail.size() == 0) emailIsNew = true;
        return  emailIsNew;
    }

    public boolean PutResult(String userId, String firstName, String lastName, String phoneNumber, String Email, String Username, String Password, String photo, String Country,
                              String City, String BirthDate, String About) {
        boolean success = true;
        String usersposturl = RestPaths.editUserById(userId);
        EditProfileParameters UserParameters = new EditProfileParameters(userId, firstName, lastName, phoneNumber, Email, Username, Password, photo, Country, City, BirthDate, About);

        RestCallManager userpost = new RestCallManager();
        RestCallParameters postparameters = new RestCallParameters(usersposturl, "PUT", "", UserParameters.getEditProfileParameters());

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

    public void saveUserProfile (){
        final String name = etName.getText().toString();
        final String lastName = etLastName.getText().toString();
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String Email = etEmail.getText().toString();
        final String Username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        final String country = etCountry.getText().toString();
        final String city = etCity.getText().toString();
        final String birthdate = tvBirthDate.getText().toString();
        SimpleDateFormat oldDateFormat = new SimpleDateFormat(Utils.APP_DATE_FORMAT);
        SimpleDateFormat newDateFormat = new SimpleDateFormat(Utils.DATABASE_DATE_FORMAT);
        Date bdate = new Date();
        try {
            bdate = oldDateFormat.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String birthDate = newDateFormat.format(bdate);
        final String about = etAbout.getText().toString();
        boolean emailIsNew;

        if(Username.length() == 0 || name.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0 || Email.length() == 0 || password.length() == 0)
        {
            //if something is not filled in, user must fill again the form
            Toast.makeText(c, "Please fill in obligatory fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!username.equals(Username)){
            Toast.makeText(c, "You cannot change your username", Toast.LENGTH_SHORT).show();
            return;
        }
        emailIsNew = checkEmail(Email);
        if(email.equals(Email)){
            emailIsNew=true;
        }

        if(emailIsNew)
        {
            boolean success = PutResult(userId, name, lastName, phoneNumber, Email, username, password, "", country, city, birthDate, about);
            if (success) {
                //if data are stored successfully in the data base, the user is now logged in and the home activity starts
                isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
                editor = sharedPrefs.edit();
                editor.putBoolean("userLoggedInState", true);
                editor.putString("currentLoggedInUser", username);
                editor.commit();

                //user can continue to the Home Screen
                Intent profileintent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                profileintent.putExtras(buser);
                try {
                    startActivity(profileintent);
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
        else
        {
            Toast.makeText(c, "Email already exists, please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
