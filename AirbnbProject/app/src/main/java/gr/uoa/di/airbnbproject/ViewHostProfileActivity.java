package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fromRESTful.Residences;
import fromRESTful.Users;
import util.ListAdapterProfile;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;

public class ViewHostProfileActivity extends AppCompatActivity {

    Users host, loggedinUser;
    int hostId, residenceId;
    String username;
    ListView list;
    Context c;
    String token;

    ImageButton bback, ibContact;

    ListAdapterProfile adapter;
    String[] userdetails;
    Boolean user;
    Residences selectedResidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = getSessionData(ViewHostProfileActivity.this);
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

        c = this;
        if(Utils.isTokenExpired(token))
        {
            Utils.logout(this);
            finish();
        }
        setContentView(R.layout.activity_view_host_profile);

        Toolbar backToolbar = (Toolbar) findViewById(R.id.backToolbar);
        setSupportActionBar(backToolbar);
        getSupportActionBar().setTitle(null);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        hostId = buser.getInt("host");
        residenceId = buser.getInt("residenceId");

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> userByUsername = retrofitCalls.getUserbyUsername(token, username);
        loggedinUser = userByUsername.get(0);
        host = retrofitCalls.getUserbyId(token, Integer.toString(hostId));

        selectedResidence = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));

        bback = (ImageButton)findViewById(R.id.back);

        userdetails = new String[9];

        manageBackToolbar();

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(ViewHostProfileActivity.this, true);

        userdetails[0] = host.getFirstName();
        userdetails[1] = host.getLastName();
        userdetails[2] = host.getUsername();
        userdetails[3] = host.getEmail();
        userdetails[4] = host.getPhoneNumber();
        userdetails[5] = host.getCountry();
        userdetails[6] = host.getCity();
        userdetails[7] = host.getAbout();
        Date bdate = host.getBirthDate();
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

        ibContact = (ImageButton) findViewById(R.id.ibContact);
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

        adapter = new ListAdapterProfile(this, userdetails);
        list = (ListView)findViewById(R.id.profilelist);
        list.setAdapter(adapter);
    }

    public void manageBackToolbar(){
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent(ViewHostProfileActivity.this, ResidenceActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type",user);
                backintent.putExtras(buser);
                try {
                    startActivity(backintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }

            }
        });
    }
}