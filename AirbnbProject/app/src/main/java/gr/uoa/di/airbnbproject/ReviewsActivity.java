package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Users;
import util.ListAdapterReviews;
import util.RestCalls;
import util.Utils;

public class ReviewsActivity extends AppCompatActivity
{
    Boolean user;
    String loggedInUsername;

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    Context c;
    ListAdapterReviews adapter;
    ListView reviewsList;

    Users loggedinUser, host;
    Residences selectedResidence;
    int residenceId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        loggedInUsername = sharedPrefs.getString("currentLoggedInUser", "");

        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_reviews);

        c = this;
        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        residenceId         = buser.getInt("residenceId");
        loggedinUser        = RestCalls.getUser(loggedInUsername);
        selectedResidence   = RestCalls.getResidenceById(residenceId);

        ArrayList<Reviews> reviewsForSelectedResidence = RestCalls.getReviewsByResidenceId(residenceId);
        String[] representativePhoto    = new String [reviewsForSelectedResidence.size()];
        String[] username               = new String[reviewsForSelectedResidence.size()];
        String[] comment                = new String[reviewsForSelectedResidence.size()];

        for(int i=0; i<reviewsForSelectedResidence.size();i++) {
            representativePhoto[i] = reviewsForSelectedResidence.get(i).getHostId().getPhoto();
            username[i] = reviewsForSelectedResidence.get(i).getHostId().getUsername();
            comment[i] = reviewsForSelectedResidence.get(i).getComment();
        }

        adapter = new ListAdapterReviews(this, representativePhoto, username, comment);
        reviewsList = (ListView)findViewById(R.id.reviewslist);
        reviewsList.setAdapter(adapter);

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(ReviewsActivity.this, true);
        /** BACK BUTTON **/
        Utils.manageBackButton(this, ResidenceActivity.class);

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
}
