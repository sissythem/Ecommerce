package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import util.RetrofitCalls;
import util.Session;

import static util.Utils.USER_LOGIN_PREFERENCES;
import static util.Utils.updateSessionData;

public class LoginActivity extends AppCompatActivity {
    Context c;
    String token;

    SharedPreferences sharedPrefs;
    Session sessionData;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

<<<<<<< HEAD
        /** Set View to activity_login.xml **/
=======
        // Set View to activity_login.xml
>>>>>>> 5fedcaaadcb2aa4e50a1fabee84f8e4ccd279bd7
        setContentView(R.layout.activity_login);

        c = this;
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

<<<<<<< HEAD
        /** Get user input **/
=======
        //Edit fields to be filled in by user in order to login to the app
>>>>>>> 5fedcaaadcb2aa4e50a1fabee84f8e4ccd279bd7
        final EditText etUsername   = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword   = (EditText) findViewById(R.id.etPassword);
        final Button blogin         = (Button) findViewById(R.id.login);
        final TextView registerlink = (TextView) findViewById(R.id.registerlink);

        etUsername.setSelected(false);
        etPassword.setSelected(false);

        /** if user does not have an account, presses on the Register Here and the Register Screen appears **/
        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerintent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerintent);
            }
        });

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                if(Username.length() == 0 || password.length() == 0) {
                    /** If something is not filled in, user must fill again the form **/
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                /** Check if the user is correct **/
                RetrofitCalls retrofitCalls = new RetrofitCalls();
                token = retrofitCalls.getLoginUser(Username, password);
                if(token.equals("not")) {
                    Toast.makeText(c, "User does not exist, please click on Register to create a new account", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        sessionData = new Session(token, Username, true);
                        updateSessionData(LoginActivity.this, sessionData);
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /** If user is not logged in cannot go back to the home activity **/
    //example: if user presses the logout back, he is no longer logged in and he should not be able to return to the home activity
    @Override
    public void onBackPressed() {
        if (sessionData.getUserLoggedInState()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            super.onBackPressed();
            return;
        }
        else {
            Intent greetingIntent = new Intent(this, GreetingActivity.class);
            startActivity(greetingIntent);
            super.onBackPressed();
        }
    }
}