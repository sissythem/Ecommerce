package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import fromRESTful.Users;
import util.ProfileListAdapter;
import util.RestCallManager;
import util.RestCallParameters;
import util.RestCalls;
import util.RestPaths;
import util.Utils;

public class ProfileActivity extends AppCompatActivity {

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    Users loggedinUser;
    String username;
    ListView list;
    Context c;

    ImageButton bback;
    ImageButton bedit;
    ImageButton bdelete;

    ProfileListAdapter adapter;
    String[] userdetails;
    Boolean user;

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
        c=this;

        setContentView(R.layout.activity_profile);

        Toolbar backToolbar = (Toolbar) findViewById(R.id.backToolbar);
        setSupportActionBar(backToolbar);
        getSupportActionBar().setTitle(null);
        Toolbar footerToolbar = (Toolbar) findViewById(R.id.footerToolbar);
        setSupportActionBar(footerToolbar);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        bback = (ImageButton)findViewById(R.id.back);
        bedit = (ImageButton)findViewById(R.id.editprofile);
        bdelete = (ImageButton)findViewById(R.id.delete);

        userdetails = new String[9];

        manageBackToolbar();

        loggedinUser = RestCalls.getUser(username);
        userdetails[0] = loggedinUser.getFirstName();
        userdetails[1] = loggedinUser.getLastName();
        userdetails[2] = loggedinUser.getUsername();
        userdetails[3] = loggedinUser.getEmail();
        userdetails[4] = loggedinUser.getPhoneNumber();
        userdetails[5] = loggedinUser.getCountry();
        userdetails[6] = loggedinUser.getCity();
        userdetails[7] = loggedinUser.getAbout();
        Date bdate = loggedinUser.getBirthDate();
        String date="NO DATE";
        if(bdate != null){
            try{
                SimpleDateFormat newDateFormat = new SimpleDateFormat(Utils.APP_DATE_FORMAT);
                date = newDateFormat.format(bdate);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        userdetails[8] = date;
        adapter = new ProfileListAdapter(this, userdetails);
        list = (ListView)findViewById(R.id.profilelist);
        list.setAdapter(adapter);

    }

    public void logout() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        Intent greetingintent = new Intent(ProfileActivity.this, GreetingActivity.class);
        ProfileActivity.this.startActivity(greetingintent);
    }
    public void manageBackToolbar(){
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user){
                    Intent homeintent = new Intent(ProfileActivity.this, HomeActivity.class);
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
                    Intent hostintent = new Intent(ProfileActivity.this, HostActivity.class);
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

        bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                editIntent.putExtras(buser);
                try{
                    startActivity(editIntent);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedinUser = RestCalls.getUser(username);
                boolean deletionStatus = deleteUserAccount(loggedinUser.getId().toString());
                if(deletionStatus)
                {
                    Toast.makeText(c, "Account deleted!", Toast.LENGTH_SHORT).show();
                    logout();
                    Intent greetingIntent = new Intent (ProfileActivity.this, GreetingActivity.class);
                    startActivity(greetingIntent);
                    finish();
                }
                else {
                    Toast.makeText(c, "Something went wrong, account is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public Boolean deleteUserAccount (String id){
        boolean success = true;

        //TODO delete all data from all other tables
        String deleteUserURL = RestPaths.deleteUserById(id);

        RestCallManager deleteUserManager = new RestCallManager();
        RestCallParameters deleteParameters = new RestCallParameters(deleteUserURL, "DELETE", "TEXT", "");

        String response;
        deleteUserManager.execute(deleteParameters);

        response = (String)deleteUserManager.getRawResponse().get(0);
        if (response.equals("OK")) ;
        else success = false;

        return  success;
    }
}
