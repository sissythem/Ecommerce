package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Residences;
import fromRESTful.Users;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;

public class ViewHostProfileActivity extends AppCompatActivity {

    Users host, loggedinUser;
    int hostId, residenceId;
    String username;
    TextView tvName, tvAbout, tvUsername, tvEmail, tvPhoneNumber, tvCity, tvCountry, tvBirthDate;
    Context c;
    String token;
    Toolbar toolbar;
    ImageButton ibContact;

    Boolean user;
    Residences selectedResidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = getSessionData(ViewHostProfileActivity.this);
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

        setContentView(R.layout.activity_view_host_profile);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        hostId = buser.getInt("host");
        residenceId = buser.getInt("residenceId");

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> userByUsername = null;
        try {
            userByUsername = retrofitCalls.getUserbyUsername(token, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loggedinUser = userByUsername.get(0);
        host = retrofitCalls.getUserbyId(token, Integer.toString(hostId));

        selectedResidence = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));

        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Profile of Host");
        toolbar.setSubtitle(host.getFirstName() + " " + host.getLastName());
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
                Utils.manageBackButton(ViewHostProfileActivity.this, ResidenceActivity.class, user);
            }
        });

        ibContact = (ImageButton) findViewById(R.id.ibContact);
        ibContact.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getTheme()));
        ibContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(ViewHostProfileActivity.this, MessageActivity.class);

                Bundle bmessage = new Bundle();
                bmessage.putBoolean("type", user);
                bmessage.putInt("currentUserId", loggedinUser.getId());
                bmessage.putInt("toUserId", host.getId());
                bmessage.putString("msgSubject", selectedResidence.getTitle());
                bmessage.putInt("residenceId", residenceId);
                bmessage.putString("back", "residence");
                messageIntent.putExtras(bmessage);
                try {
                    startActivity(messageIntent);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Log.e("",e.getMessage());
                }
            }
        });
        setUpProfile();
    }

    public void setUpProfile()
    {
        tvName = (TextView)findViewById(R.id.name);
        tvAbout = (TextView)findViewById(R.id.about);
        tvUsername = (TextView)findViewById(R.id.about);
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
}