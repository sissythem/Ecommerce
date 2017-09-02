package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fromRESTful.Users;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;
import static util.Utils.goToActivity;

public class ProfileActivity extends AppCompatActivity {
    Users loggedinUser;
    String username, token;
    Bundle buser;
    Context c;
    Toolbar toolbar;
    ImageView userImage;
    TextView tvName, tvAbout, tvUsername, tvEmail, tvPhoneNumber, tvCity, tvCountry, tvBirthDate;
    Boolean user;
    RetrofitCalls retrofitCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = getSessionData(ProfileActivity.this);
        token = sessionData.getToken();
        username = sessionData.getUsername();
        c = this;
        //check if user is logged in
        if (!sessionData.getUserLoggedInState()) {
            Utils.logout(this);
            finish();
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        //check if token is expired
        if(Utils.isTokenExpired(sessionData.getToken())){
            Toast.makeText(c, "Session is expired", Toast.LENGTH_SHORT).show();
            Utils.logout(this);
            finish();
            return;
        }
        setContentView(R.layout.activity_profile);
        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Profile");
        toolbar.setSubtitle("Welcome " + username);
        setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        //handle the back action
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackAction();
            }
        });

        buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        retrofitCalls = new RetrofitCalls();

        /** Get the user **/
        loggedinUser    = retrofitCalls.getUserbyUsername(token, username).get(0);
        setUpProfile();

        userImage = (ImageView) findViewById(R.id.userImage);
        Utils.loadProfileImage(ProfileActivity.this, userImage, loggedinUser.getPhoto());

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(ProfileActivity.this, user);
    }

    public void setUpProfile()
    {
        /** Show info about the user **/
        tvName = (TextView)findViewById(R.id.name);
        tvAbout = (TextView)findViewById(R.id.about);
        tvUsername = (TextView)findViewById(R.id.username);
        tvEmail = (TextView)findViewById(R.id.email);
        tvPhoneNumber = (TextView)findViewById(R.id.phonenumber);
        tvCity = (TextView)findViewById(R.id.city);
        tvCountry = (TextView)findViewById(R.id.country);
        tvBirthDate = (TextView)findViewById(R.id.birthDate);

        tvName.setText(loggedinUser.getFirstName() + " " + loggedinUser.getLastName());
        if(loggedinUser.getAbout() != null)
        {
            tvAbout.setText(loggedinUser.getAbout());
        }
        else {
            tvAbout.setText("About this user");
        }
        tvUsername.setText("Username: " + loggedinUser.getUsername());
        tvEmail.setText("Email: " + loggedinUser.getEmail());
        tvPhoneNumber.setText("Phone Number: " + loggedinUser.getPhoneNumber());
        if(!loggedinUser.getCity().isEmpty())
            tvCity.setText("City: " + loggedinUser.getCity());
        else
            tvCity.setHint("Add your city");
        if(!loggedinUser.getCountry().isEmpty())
            tvCountry.setText("Country: " + loggedinUser.getCountry());
        else
            tvCountry.setHint("Add your country");
        if(!loggedinUser.getBirthDate().isEmpty())
            tvBirthDate.setText("Birth Date: " + loggedinUser.getBirthDate());
        else
            tvBirthDate.setHint("Add your birth date");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_popup, menu);
        return true;
    }

    /** PopUp Menu in the upper toolbar **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Bundle buser = new Bundle();
        buser.putBoolean("type", user);
        switch (item.getItemId()) {
            /** User can see a history of his reviews **/
            case R.id.reviews:
                if (item.getItemId() == R.id.reviews) {
                    goToActivity(ProfileActivity.this, HistoryReviewsActivity.class, buser);
                    break;
                }
                /** User can see a history of his reservations **/
            case R.id.reservations:
                goToActivity(ProfileActivity.this, HistoryReservationsActivity.class, buser);
                break;
            /** User can edit his profile **/
            case R.id.editprofile:
                goToActivity(ProfileActivity.this, EditProfileActivity.class, buser);
                break;
            /** User can delete his profile **/
            case R.id.deleteProfile:
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Delete Account").setMessage("Do you really want to delete your account?").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (retrofitCalls.deleteUserById(token, loggedinUser.getId().toString()) == null) {
                                    Toast.makeText(c, "Account deleted!", Toast.LENGTH_SHORT).show();
                                    Utils.logout(ProfileActivity.this);
                                } else {
                                    Toast.makeText(c, "Something went wrong, account is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
        }
        return true;
    }

    public void handleBackAction(){
        if(buser.getString("source").equals("residence"))
        {
            Intent residenceIntent = new Intent(ProfileActivity.this, ResidenceActivity.class);
            Bundle btype = new Bundle();
            btype.putBoolean("type", user);
            btype.putInt("residenceId", buser.getInt("residenceId"));
            residenceIntent.putExtras(btype);
            startActivity(residenceIntent);
        }
        else{
            Bundle btype = new Bundle();
            btype.putBoolean("type", user);
            Utils.goToActivity(ProfileActivity.this, (user)?HomeActivity.class:HostActivity.class, btype);
        }
    }

    @Override
    public void onBackPressed(){
        handleBackAction();
    }
}