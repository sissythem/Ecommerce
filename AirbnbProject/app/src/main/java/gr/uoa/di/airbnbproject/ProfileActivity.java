package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fromRESTful.Users;
import util.ListAdapterProfile;
import util.RestCallManager;
import util.RestCallParameters;
import util.RestCalls;
import util.RestPaths;
import util.RetrofitCalls;
import util.Utils;

public class ProfileActivity extends AppCompatActivity
{

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    Users loggedinUser;
    String username;
    ListView list;
    Context c;

    ImageButton btnMenu;

    ListAdapterProfile adapter;
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
        c = this;

        setContentView(R.layout.activity_profile);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        btnMenu = (ImageButton)findViewById(R.id.btnMenu);

        userdetails = new String[9];

        manageToolbarButtons();
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> userLoggedIn = retrofitCalls.getUserbyUsername(username);
        loggedinUser = userLoggedIn.get(0);
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
        adapter = new ListAdapterProfile(this, userdetails);
        list = (ListView)findViewById(R.id.profilelist);
        list.setAdapter(adapter);

        /** BACK BUTTON **/
        Utils.manageBackButton(this, (user)?HomeActivity.class:HostActivity.class);
    }

    public void manageToolbarButtons() {

        //TODO fix if statements, menu does not work
        btnMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(ProfileActivity.this, btnMenu);
                popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Bundle buser = new Bundle();
                        buser.putBoolean("type", user);
                        if(item.getTitle() == "My Reviews")
                        {
                            Intent historyReviewsIntent = new Intent(ProfileActivity.this, HistoryReviewsActivity.class);
                            historyReviewsIntent.putExtras(buser);
                            startActivity(historyReviewsIntent);
                        }
                        else if(item.getTitle() == "My Reservations")
                        {
                            Intent historyReservationsIntent = new Intent(ProfileActivity.this, HistoryReservationsActivity.class);
                            historyReservationsIntent.putExtras(buser);
                            startActivity(historyReservationsIntent);
                        }
                        else if(item.getTitle() == "Edit profile")
                        {
                            Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            editIntent.putExtras(buser);
                            startActivity(editIntent);
                        }
                        else if(item.getTitle() == "Delete profile")
                        {
                            loggedinUser = RestCalls.getUser(username);
                            boolean deletionStatus = deleteUserAccount(loggedinUser.getId().toString());
                            if(deletionStatus)
                            {
                                Toast.makeText(c, "Account deleted!", Toast.LENGTH_SHORT).show();
                                Utils.logout(ProfileActivity.this);
                                Intent greetingIntent = new Intent (ProfileActivity.this, GreetingActivity.class);
                                startActivity(greetingIntent);
                                finish();
                            }
                            else {
                                Toast.makeText(c, "Something went wrong, account is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                });

                popup.show();
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