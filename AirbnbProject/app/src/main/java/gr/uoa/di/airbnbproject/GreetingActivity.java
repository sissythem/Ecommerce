package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import util.Session;

public class GreetingActivity extends AppCompatActivity
{
    Context c;
    Session sessionData;
    //Fist screen of application. Gives user the choice either to log in or register
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

        //Initialize button for register
        final Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //if register button is clicked RegisterActivity starts
                Intent registerintent = new Intent(GreetingActivity.this, RegisterActivity.class);
                GreetingActivity.this.startActivity(registerintent);
                finish();
            }
        });

        //Initialize button for log in
        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //if log in button is clicked log in activity is launched
                Intent loginintent = new Intent(GreetingActivity.this, LoginActivity.class);
                GreetingActivity.this.startActivity(loginintent);
                finish();
            }
        });
    }

    /** If user is not logged in cannot go back to the home activity **/
    //example: if user presses the logout back, he is no longer logged in and he should not be able to return to the home activity
    @Override
    public void onBackPressed() {
        //isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
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