package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fromRESTful.Users;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.DATABASE_DATE_FORMAT;
import static util.Utils.updateSessionData;

public class EditProfileActivity extends AppCompatActivity
{
    Context c;

    Boolean user;
    RetrofitCalls retrofitCalls;
    Users loggedinUser;

    ImageButton btnBirthDate;
    Button bsave;

    EditText etName, etLastName, etPhoneNumber, etEmail, etPassword, etCountry, etCity, etAbout;
    TextView etUsername, tvBirthDate;
    String username, token;
    int userId;
    private int mYear, mMonth, mDay;

    Session sessionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = Utils.getSessionData(EditProfileActivity.this);
        if (!sessionData.getUserLoggedInState()) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        token = sessionData.getToken();
        username = sessionData.getUsername();
        setContentView(R.layout.activity_edit_profile);
        c = this;

        Toolbar footerToolbar = (Toolbar) findViewById(R.id.footerToolbar);
        setSupportActionBar(footerToolbar);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        retrofitCalls = new RetrofitCalls();
        if(Utils.isTokenExpired(token))
        {
            Utils.logout(this);
            finish();
        }
        ArrayList<Users> getUsersByUsername = retrofitCalls.getUserbyUsername(token, username);
        loggedinUser  = getUsersByUsername.get(0);

        userId              = loggedinUser.getId();
        etName              = (EditText) findViewById(R.id.etName);
        etLastName          = (EditText) findViewById(R.id.etLastName);
        etPhoneNumber       = (EditText) findViewById(R.id.etPhoneNumber);
        etEmail             = (EditText) findViewById(R.id.etEmail);
        etUsername          = (TextView) findViewById(R.id.etUsername);
        etPassword          = (EditText) findViewById(R.id.etPassword);
        etCountry           = (EditText)findViewById(R.id.etCountry);
        etCity              = (EditText)findViewById(R.id.etCity);
        tvBirthDate         = (TextView) findViewById(R.id.tvBirthDate);
        etAbout             = (EditText)findViewById(R.id.etAbout);
        btnBirthDate        = (ImageButton)findViewById(R.id.btnBirthDate);

        etName.setText(loggedinUser.getFirstName());
        etLastName.setText(loggedinUser.getLastName());
        etPhoneNumber.setText(loggedinUser.getPhoneNumber());
        etEmail.setText(loggedinUser.getEmail());
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

        bsave = (Button)findViewById(R.id.post);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveUserProfile(); }
        });

        /** BACK BUTTON **/
        Utils.manageBackButton(EditProfileActivity.this, ProfileActivity.class, user);
    }

    public boolean checkEmail (String Email)
    {
        boolean emailIsNew=false;
        //same process for email
        ArrayList<Users> users = retrofitCalls.getUserbyEmail(token, Email);
        if(users.size() == 0) emailIsNew = true;
        return  emailIsNew;
    }

    public void saveUserProfile ()
    {
        final String name = etName.getText().toString();
        final String lastName = etLastName.getText().toString();
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String Email = etEmail.getText().toString();
        final String Username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        final String country = etCountry.getText().toString();
        final String city = etCity.getText().toString();
        final String birthdate = tvBirthDate.getText().toString();
        final String photo = "photo path";
        final String about = etAbout.getText().toString();

        Date bdate = Utils.ConvertStringToDate(birthdate, DATABASE_DATE_FORMAT);

        boolean emailIsNew;

        if(Username.length() == 0 || name.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0 || Email.length() == 0 || password.length() == 0) {
            //if something is not filled in, user must fill again the form
            Toast.makeText(c, "Please fill in obligatory fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!username.equals(Username)){
            Toast.makeText(c, "You cannot change your username", Toast.LENGTH_SHORT).show();
            return;
        }
        emailIsNew = checkEmail(Email);
        if(loggedinUser.getEmail().equals(Email)){
            emailIsNew=true;
        }

        if(emailIsNew) {
            token = PutResult(loggedinUser.getId(), name, lastName, Username, password, Email, phoneNumber, country, city, photo, about, bdate);
            if (!token.isEmpty() || token!=null || token!="not") {
                sessionData.setToken(token);
                sessionData.setUsername(username);
                sessionData.setUserLoggedInState(true);
                System.out.println(sessionData);
                //if data are stored successfully in the data base, the user is now logged in and the home activity starts
                sessionData = updateSessionData(EditProfileActivity.this, sessionData);

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
                Toast.makeText(c, "Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                Utils.logout(this);
                finish();
            }
        } else {
            Toast.makeText(c, "Email already exists, please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public String PutResult(int userId, String firstName, String lastName, String username, String password, String email, String phoneNumber, String country, String city,
                            String photo, String about, Date birthDate) {
        Users UserParameters = new Users(userId, firstName, lastName, username, password, email, phoneNumber, country, city, photo, about, birthDate);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token=retrofitCalls.editUser(token, userId, UserParameters);
        return token;
    }

}