package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import fromRESTful.Reviews;
import fromRESTful.Users;
import util.ListAdapterReviews;
import util.RetrofitCalls;
import util.Utils;

public class HistoryReviewsActivity extends AppCompatActivity
{
    Boolean user;
    String loggedInUsername;
    Users loggedinUser;

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    Context c;
    ListAdapterReviews adapter;
    ListView reviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_history_reviews);

        c = this;
        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(loggedInUsername);
        loggedinUser = getUserByUsername.get(0);

        ArrayList<Reviews> userReviews = retrofitCalls.getReviewsByTenantId(loggedinUser.getId());

        String[] representativePhoto    = new String [userReviews.size()];
        String[] username               = new String[userReviews.size()];
        String[] comment                = new String[userReviews.size()];

        for(int i=0; i<userReviews.size();i++) {
            representativePhoto[i] = userReviews.get(i).getTenantId().getPhoto();
            username[i] = userReviews.get(i).getHostId().getUsername();
            comment[i] = userReviews.get(i).getComment();
        }

        adapter = new ListAdapterReviews(this, representativePhoto, username, comment);
        reviewsList = (ListView)findViewById(R.id.reviewslist);
        reviewsList.setAdapter(adapter);


        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HistoryReviewsActivity.this, user);
        /** BACK BUTTON **/
        Utils.manageBackButton(this, ProfileActivity.class, user);
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
