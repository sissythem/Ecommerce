package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import fromRESTful.Reviews;
import fromRESTful.Users;
import util.ListAdapterReviews;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

public class HistoryReviewsActivity extends AppCompatActivity {
    Boolean user;
    String token;
    Users loggedinUser;

    Context c;
    ListAdapterReviews adapter;
    ListView reviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = Utils.getSessionData(HistoryReviewsActivity.this);
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
        setContentView(R.layout.activity_history_reviews);
        c = this;

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Utils.checkToken(token, HistoryReviewsActivity.this);
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        loggedinUser = getUserByUsername.get(0);

        Utils.checkToken(token, HistoryReviewsActivity.this);
        ArrayList<Reviews> userReviews = retrofitCalls.getReviewsByTenantId(token, loggedinUser.getId().toString());

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
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
