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

import java.util.ArrayList;

import fromRESTful.Residences;
import util.HostViewResidencesListAdapter;
import util.RestCalls;

public class HostActivity extends AppCompatActivity {

    String username;

    ImageButton bhome;
    ImageButton binbox;
    ImageButton bprofile;
    ImageButton bswitch;
    ImageButton blogout;

    ImageButton baddResidence;

    HostViewResidencesListAdapter adapter;
    ListView residencesList;
    int[] residenceId;

    Boolean user;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

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

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_host);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        manageFooter();
        baddResidence = (ImageButton)findViewById(R.id.addResidence);

        baddResidence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent addResidenceIntent = new Intent(HostActivity.this, AddResidenceActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type",user);
                addResidenceIntent.putExtras(buser);
                try {
                    startActivity(addResidenceIntent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        int hostId = RestCalls.getUserId(username);
        ArrayList<Residences> storedResidences = RestCalls.getResidencesbyHostId(hostId);

        String[] representativePhoto = new String [storedResidences.size()];
        String[] city = new String[storedResidences.size()];
        Double[] price = new Double[storedResidences.size()];
        float[] rating = new float[storedResidences.size()];
        residenceId = new int[storedResidences.size()];

        for(int i=0; i<storedResidences.size();i++){
            representativePhoto[i] = storedResidences.get(i).getPhotos();
            city[i] = storedResidences.get(i).getCity();
            price[i] = storedResidences.get(i).getMinPrice();
            rating[i] = (float)storedResidences.get(i).getAverageRating();
            residenceId[i] = storedResidences.get(i).getId();
        }

        adapter=new HostViewResidencesListAdapter(this, representativePhoto, city, price, rating, residenceId);
        residencesList = (ListView)findViewById(R.id.residenceList);
        residencesList.setAdapter(adapter);

//        residencesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                Intent showResidenceIntent = new Intent(HostActivity.this, ResidenceActivity.class);
//                Bundle btype = new Bundle();
//                btype.putBoolean("type",user);
//                btype.putInt("residenceId", residenceId[position]);
//                showResidenceIntent.putExtras(btype);
//                try {
//                    startActivity(showResidenceIntent);
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                    ex.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed()
    {
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        super.onBackPressed();
//        return;
        moveTaskToBack(true);
    }

    public void manageFooter(){
        Toolbar footerToolbar = (Toolbar) findViewById(R.id.footerToolbar);
        setSupportActionBar(footerToolbar);
        getSupportActionBar().setTitle(null);

        bhome = (ImageButton)findViewById(R.id.home);
        binbox = (ImageButton) findViewById(R.id.inbox);
        bprofile = (ImageButton) findViewById(R.id.profile);
        bswitch = (ImageButton) findViewById(R.id.host);
        blogout = (ImageButton) findViewById(R.id.logout);

        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        binbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxintent = new Intent(HostActivity.this, InboxActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type",user);
                inboxintent.putExtras(btype);
                try {
                    startActivity(inboxintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileintent = new Intent(HostActivity.this, ProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type",user);
                profileintent.putExtras(buser);
                try {
                    startActivity(profileintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hostintent = new Intent(HostActivity.this, HomeActivity.class);
                Bundle buser = new Bundle();
                user=true;
                buser.putBoolean("type",user);
                hostintent.putExtras(buser);
                startActivity(hostintent);
            }
        });

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                return;
            }
        });
    }

    public void logout() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        Intent greetingintent = new Intent(HostActivity.this, GreetingActivity.class);
        HostActivity.this.startActivity(greetingintent);
    }
}
