package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class ResidenceActivity extends AppCompatActivity
{
    String username;
    Boolean user;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;
    int residenceId;

    ImageButton bback;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        username = sharedPrefs.getString("currentLoggedInUser", "");

        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_residence);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        residenceId = buser.getInt("residenceId");

        bback = (ImageButton)findViewById(R.id.back);

        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user){
                    Intent homeintent = new Intent(ResidenceActivity.this, HomeActivity.class);
                    Bundle buser = new Bundle();
                    buser.putBoolean("type",user);
                    homeintent.putExtras(buser);
                    try {
                        startActivity(homeintent);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                else{
                    Intent hostintent = new Intent(ResidenceActivity.this, HostActivity.class);
                    Bundle bhost = new Bundle();
                    user=false;
                    bhost.putBoolean("type", user);
                    hostintent.putExtras(bhost);
                    try{
                        startActivity(hostintent);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}
