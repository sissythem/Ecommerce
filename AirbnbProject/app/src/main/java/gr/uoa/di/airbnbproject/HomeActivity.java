package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Rooms;
import util.CustomListAdapter;
import util.RestCalls;

/**
 * Created by sissy on 30/4/2017.
 */

public class HomeActivity extends AppCompatActivity
{
    String username;
    ListView list;
    Button binbox;
    Button bprofile;
    Button baddResidence;
    Button blogout;

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //logout();
        //String Username = getIntent().getStringExtra("username");

        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        username = sharedPrefs.getString("currentLoggedInUser", "");

        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            return;
        }
        setContentView(R.layout.activity_home);

        binbox = (Button) findViewById(R.id.inbox);
        bprofile = (Button) findViewById(R.id.profile);
        baddResidence = (Button) findViewById(R.id.host);
        blogout = (Button) findViewById(R.id.logout);

        binbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxintent = new Intent(HomeActivity.this, InboxActivity.class);
                HomeActivity.this.startActivity(inboxintent);
            }
        });

        bprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileintent = new Intent(HomeActivity.this, ProfileActivity.class);
                HomeActivity.this.startActivity(profileintent);
            }
        });

        baddResidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                return;
            }
        });

        ArrayList<Residences> Recommendations = popularRecommendations(username);

        String[] city = new String[Recommendations.size()];
        Double[] price = new Double[Recommendations.size()];
        float[] rating = new float[Recommendations.size()];

        for(int i=0; i<Recommendations.size();i++){
            city[i] = Recommendations.get(i).getCity();
            price[i] = Recommendations.get(i).getMinPrice();
            rating[i] = (float)Recommendations.get(i).getAverageRating();
        }
        CustomListAdapter adapter=new CustomListAdapter(this, city, price, rating);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        return;
    }


    public void logout() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        Intent greetingintent = new Intent(HomeActivity.this, GreetingActivity.class);
        HomeActivity.this.startActivity(greetingintent);
    }

    public ArrayList<Residences> popularRecommendations(String username)
    {
        int userId = RestCalls.getUserId(username);
        Set<String> relevantCity = RestCalls.getSearchedCities(userId);

        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        ArrayList<Rooms> roomsByResidence;
        ArrayList<Reviews> reviewsByResidence;
        int residenceId;
        //if user has not searched anything yet, most popular residences will appear
        if (relevantCity.size() == 0) {
            ArrayList<Reviews> reviews = RestCalls.getReviews();
            for (int i=0;i<reviews.size();i++) {
                reviewedResidences.add(reviews.get(i).getResidenceId());
            }
        }
        //if user has already searched, we will show the most popular residences in the relevant cities
        else {
            for (String city : relevantCity) {
                reviewedResidences = RestCalls.getResidenceByCity(city);
            }
        }
        //check for duplicates
        Set<Residences> hs = new HashSet<>();
        hs.addAll(reviewedResidences);
        reviewedResidences.clear();
        reviewedResidences.addAll(hs);
        //get all relevant rooms and reviews
        for(int i=0; i<reviewedResidences.size();i++){
            residenceId = reviewedResidences.get(i).getId();
            roomsByResidence = RestCalls.getRoomsByResidenceId(residenceId);
            reviewsByResidence = RestCalls.getReviewsByResidenceId(residenceId);
            reviewedResidences.get(i).setRoomsCollection(roomsByResidence);
            reviewedResidences.get(i).setReviewsCollection(reviewsByResidence);
        }
        //sort the results
        Collections.sort(reviewedResidences);
        return reviewedResidences;
    }
}