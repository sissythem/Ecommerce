package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Users;
import util.ListAdapterReviews;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;

public class ReviewsActivity extends AppCompatActivity
{
    Boolean user;
    String token;
    Toolbar toolbar;
    Context c;
    ListAdapterReviews adapter;
    ListView reviewsList;

    Users loggedinUser, host;
    Residences selectedResidence;
    int residenceId;
    double ratingNew;

    EditText etcomment;
    ImageButton btnreview;
    RatingBar ratingBar;
    TextView txtrating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = getSessionData(ReviewsActivity.this);
        token = sessionData.getToken();
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

        setContentView(R.layout.activity_reviews);

        Bundle buser = getIntent().getExtras();
        user        = buser.getBoolean("type");
        residenceId = buser.getInt("residenceId");
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        //get user, host and residence as objects
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        loggedinUser        = getUserByUsername.get(0);
        selectedResidence   = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));
        host                = selectedResidence.getHostId();

        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Reviews");
        toolbar.setSubtitle("Residence " + selectedResidence.getTitle());
        setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        //handle the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle btores = new Bundle();
                btores.putBoolean("type", user);
                btores.putInt("residenceId", residenceId);
                Utils.goToActivity(ReviewsActivity.this, ResidenceActivity.class, btores);
            }
        });
        /** Show the reviews for this residence **/
        ArrayList<Reviews> reviewsForSelectedResidence = retrofitCalls.getReviewsByResidenceId(token, Integer.toString(residenceId));
        String[] representativePhoto    = new String [reviewsForSelectedResidence.size()];
        String[] username               = new String[reviewsForSelectedResidence.size()];
        String[] comment                = new String[reviewsForSelectedResidence.size()];
        double[] rating                 = new double[reviewsForSelectedResidence.size()];

        for(int i=0; i<reviewsForSelectedResidence.size();i++) {
            representativePhoto[i] = reviewsForSelectedResidence.get(i).getTenantId().getPhoto();
            username[i] = reviewsForSelectedResidence.get(i).getTenantId().getUsername();
            comment[i] = reviewsForSelectedResidence.get(i).getComment();
            rating [i] = reviewsForSelectedResidence.get(i).getRating();
        }

        adapter = new ListAdapterReviews(this, representativePhoto, username, comment, rating);
        reviewsList = (ListView)findViewById(R.id.reviewslist);
        reviewsList.setAdapter(adapter);

        etcomment = (EditText)findViewById(R.id.writeComment);
        btnreview = (ImageButton)findViewById(R.id.btnreview);
        ratingBar = (RatingBar)findViewById(R.id.rating);
        txtrating = (TextView)findViewById(R.id.txtRate);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar bar = (RatingBar) v;
                ratingNew = bar.getRating();
            }
        });

        ArrayList<Reservations> reservationsByTenantIdandResidenceId= retrofitCalls.getReservationsByTenantIdandResidenceId(token, loggedinUser.getId().toString(), selectedResidence.getId().toString());

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        long currentDate = calendar.getTimeInMillis();

        boolean isDatePassed = false;
        for(int i=0;i<reservationsByTenantIdandResidenceId.size();i++) {
            isDatePassed = reservationsByTenantIdandResidenceId.get(i).getEndDate() < currentDate;
        }

        //only users who have already made a reservation and the relevant dates are in the past can rate and comment
        if (reservationsByTenantIdandResidenceId.isEmpty() || !isDatePassed)
        {
            etcomment.setVisibility(View.GONE);
            btnreview.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            txtrating.setVisibility(View.GONE);
        }
        else
        {
            btnreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    final String comment = etcomment.getText().toString();
                    final double ratingfromuser = ratingBar.getRating();
                    if(comment.length() == 0 || ratingfromuser == 0){
                        Toast.makeText(c, "Please write your comment and rate the residence", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Reviews reviews = new Reviews(selectedResidence, host, loggedinUser, comment, ratingfromuser);
                        RetrofitCalls retrofitCalls = new RetrofitCalls();
                        token = retrofitCalls.postReview(token, reviews);

                        if (!token.isEmpty() && !(token.equals("not")) && token != null) {
                            Toast.makeText(c, "Your comment has been successfully submitted. Thank you!", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("type", user);
                            bundle.putInt("residenceId", residenceId);
                            Utils.reloadActivity(ReviewsActivity.this, bundle);
                            return;
                        } else {
                            Toast.makeText(c, "Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                            Utils.logout(ReviewsActivity.this);
                            finish();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        Bundle btores = new Bundle();
        btores.putBoolean("type", user);
        btores.putInt("residenceId", residenceId);
        Utils.goToActivity(ReviewsActivity.this, ResidenceActivity.class, btores);
    }
}