package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import fromRESTful.Users;
import util.RetrofitCalls;
import util.Session;

import static util.Utils.updateSessionData;

public class RegisterActivity extends AppCompatActivity {
    String token;
    Context c;

    EditText firstname, lastname, phonenumber, email, username, password, confirmpassword;
    TextView birthdate, loginlink;
    ImageButton btnBirthDate;
    Button bregister;
    Calendar cal;
    private int mYear, mMonth, mDay;
    Session sessionData;

    String dateOfBirth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=this;
        // Set View to activity_register.xml
        setContentView(R.layout.activity_register);

        //Create variables for storing user input
        firstname        = (EditText) findViewById(R.id.firstname);
        lastname         = (EditText) findViewById(R.id.lastname);
        birthdate        = (TextView) findViewById(R.id.tvBirthDate);
        btnBirthDate     = (ImageButton)findViewById(R.id.btnBirthDate);
        phonenumber      = (EditText) findViewById(R.id.phonenumber);
        email            = (EditText) findViewById(R.id.email);
        username         = (EditText) findViewById(R.id.username);
        password         = (EditText) findViewById(R.id.password);
        confirmpassword  = (EditText) findViewById(R.id.confirmpassword);
        bregister        = (Button) findViewById(R.id.register);
        loginlink        = (TextView) findViewById(R.id.loginlink);

        firstname.setSelected(false);
        lastname.setSelected(false);
        phonenumber.setSelected(false);
        email.setSelected(false);
        username.setSelected(false);
        password.setSelected(false);
        confirmpassword.setSelected(false);

        //link the text view loginlink to the LoginActivity in case user has already an account
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginintent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginintent);
            }
        });

        btnBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnBirthDate) {
                    // Get Current Date
                    cal = Calendar.getInstance();
                    mYear = cal.get(Calendar.YEAR);
                    mMonth = cal.get(Calendar.MONTH);
                    mDay = cal.get(Calendar.DAY_OF_MONTH);

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
//                final String firstName          = "test_firstname";
//                final String lastName           = "test_lastname";
//                final String BirthDate          = "3-7-2017";
//                final String phoneNumber        = "123123123";
//                final String Email              = "email@domain.com";
//                final String Username           = "usernametest";
//                final String Password           = "password";
//                final String ConfirmPassword    = "password";

                final String firstName          = firstname.getText().toString();
                final String lastName           = lastname.getText().toString();
                final String BirthDate          = birthdate.getText().toString();
                final String phoneNumber        = phonenumber.getText().toString();
                final String Email              = email.getText().toString();
                final String Username           = username.getText().toString();
                final String Password           = password.getText().toString();
                final String ConfirmPassword    = confirmpassword.getText().toString();


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

//                Date bdate = Utils.ConvertStringToDate(BirthDate,Utils.APP_DATE_FORMAT);
//                System.out.println(bdate);
//
//                //java.util.Date dt = new java.util.Date();
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                dateOfBirth = sdf.format(bdate);
//                System.out.println(dateOfBirth);

//                String cdate="Mar 10, 2016 6:30:00 PM";
//                SimpleDateFormat formatter=new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
//                try {
//                    Date newDate = formatter.parse(cdate);
//                    formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                    cdate = formatter.format(newDate);
//                    System.out.println(cdate);
//                } catch (Exception e) {}
//
//                System.out.println(cdate);


//                Calendar calendar = Calendar.getInstance();
//                /* to display time*/
//                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
//                /* to display date in the given format */
//                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
//                /*get the date using */
//                Date dDate = dateFormatter.parse(yourObject.getTaskDate());
//                /*set the date */
//                calendar.setTime(dDate);

                token = PostResult(firstName, lastName, Username, Password, Email, phoneNumber, BirthDate);
                if(token.equals("username exists")){
                    Toast.makeText(c, "Username already exists, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(token.equals("email exists")){
                    Toast.makeText(c, "Email already exists, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (token!=null && !token.isEmpty() &&  (!token.equals("error"))) {
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
                } else {
                    Toast.makeText(c, "Registration failed, please try again!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public String PostResult(String firstName, String lastName, String username, String password, String email, String phoneNumber, String bdate) {
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