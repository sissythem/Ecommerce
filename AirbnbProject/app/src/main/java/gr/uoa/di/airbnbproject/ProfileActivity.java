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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import fromRESTful.Users;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;

public class ProfileActivity extends AppCompatActivity {
    Users loggedinUser;
    String username, token;
    ListView list;
    Context c;
    Toolbar toolbar;
    ImageView userImage;
    TextView tvName, tvAbout, tvUsername, tvEmail, tvPhoneNumber, tvCity, tvCountry, tvBirthDate;
    Boolean user;
    RetrofitCalls retrofitCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = getSessionData(ProfileActivity.this);
        token = sessionData.getToken();
        username = sessionData.getUsername();
        c = this;
        if (!sessionData.getUserLoggedInState()) {
            Utils.logout(this);
            finish();
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        if(Utils.isTokenExpired(sessionData.getToken())){
            Toast.makeText(c, "Session is expired", Toast.LENGTH_SHORT).show();
            Utils.logout(this);
            finish();
            return;
        }
        setContentView(R.layout.activity_profile);

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.manageBackButton(ProfileActivity.this, (user)?HomeActivity.class:HostActivity.class, user);
            }
        });

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        retrofitCalls = new RetrofitCalls();
        loggedinUser    = retrofitCalls.getUserbyUsername(token, username).get(0);
        setUpProfile();
        userImage = (ImageView) findViewById(R.id.userImage);
        Utils.loadProfileImage(ProfileActivity.this, userImage, loggedinUser.getPhoto());

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(ProfileActivity.this, user);
    }

    public void setUpProfile()
    {
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
        tvUsername.setText(loggedinUser.getUsername());
        tvEmail.setText(loggedinUser.getEmail());
        tvPhoneNumber.setText(loggedinUser.getPhoneNumber());
        if(loggedinUser.getCity() != null)
            tvCity.setText(loggedinUser.getCity());
        if(loggedinUser.getCountry() !=null)
            tvCountry.setText(loggedinUser.getCountry());
        tvBirthDate.setText(loggedinUser.getBirthDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_popup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle buser = new Bundle();
        buser.putBoolean("type", user);
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.reviews:
                if (item.getItemId() == R.id.reviews) {
                    Intent historyReviewsIntent = new Intent(ProfileActivity.this, HistoryReviewsActivity.class);
                    historyReviewsIntent.putExtras(buser);
                    startActivity(historyReviewsIntent);
                    break;
                }
                // action with ID action_settings was selected
            case R.id.reservations:
                Intent historyReservationsIntent = new Intent(ProfileActivity.this, HistoryReservationsActivity.class);
                historyReservationsIntent.putExtras(buser);
                startActivity(historyReservationsIntent);
                break;
            case R.id.editprofile:
                Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                editIntent.putExtras(buser);
                startActivity(editIntent);
                break;
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
}