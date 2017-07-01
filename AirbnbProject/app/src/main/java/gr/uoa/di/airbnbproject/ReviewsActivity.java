package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Users;
import retrofit2.Call;
import retrofit2.Response;
import util.ListAdapterReviews;
import util.RestAPI;
import util.RestClient;
import util.RetrofitCalls;
import util.Session;
import util.Utils;
import static util.Utils.getSessionData;

public class ReviewsActivity extends AppCompatActivity {
    Boolean user;
    String token;

    Context c;
    ListAdapterReviews adapter;
    ListView reviewsList;

    Users loggedinUser, host;
    Residences selectedResidence;
    int residenceId;
    double rating;

    EditText etcomment;
    Button btnreview;
    RatingBar ratingBar;
    TextView txtrating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = getSessionData(ReviewsActivity.this);
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

        setContentView(R.layout.activity_reviews);

        c = this;
        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        residenceId         = buser.getInt("residenceId");
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        loggedinUser        = getUserByUsername.get(0);
        selectedResidence   = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));
        host                = selectedResidence.getHostId();

        ArrayList<Reviews> reviewsForSelectedResidence = retrofitCalls.getReviewsByResidenceId(token, Integer.toString(residenceId));
        String[] representativePhoto    = new String [reviewsForSelectedResidence.size()];
        String[] username               = new String[reviewsForSelectedResidence.size()];
        String[] comment                = new String[reviewsForSelectedResidence.size()];

        for(int i=0; i<reviewsForSelectedResidence.size();i++) {
            representativePhoto[i] = reviewsForSelectedResidence.get(i).getHostId().getPhoto();
            username[i] = reviewsForSelectedResidence.get(i).getTenantId().getUsername();
            comment[i] = reviewsForSelectedResidence.get(i).getComment();
        }

        adapter = new ListAdapterReviews(this, representativePhoto, username, comment);
        reviewsList = (ListView)findViewById(R.id.reviewslist);
        reviewsList.setAdapter(adapter);

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(ReviewsActivity.this, user);
        /** BACK BUTTON **/
        Utils.manageBackButton(this, ResidenceActivity.class, user);

        etcomment = (EditText)findViewById(R.id.writeComment);
        btnreview = (Button)findViewById(R.id.btnreview);
        ratingBar = (RatingBar)findViewById(R.id.rating);
        txtrating = (TextView)findViewById(R.id.txtRate);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar bar = (RatingBar) v;
                rating = bar.getRating();
            }
        });

        ArrayList<Reservations> reservationsByTenantIdandResidenceId= retrofitCalls.getReservationsByTenantIdandResidenceId(token,
                loggedinUser.getId().toString(), selectedResidence.getId().toString());

        boolean isDatePassed = false;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentDate = calendar.getTime();

        for(int i=0;i<reservationsByTenantIdandResidenceId.size();i++)
        {
            if(reservationsByTenantIdandResidenceId.get(i).getEndDate().before(currentDate))
            {
                //at least one reservation in the past
                isDatePassed=true;
            }
            else
            {
                isDatePassed=false;
            }
        }

        if (reservationsByTenantIdandResidenceId.isEmpty() || isDatePassed==false)
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
                        token = postResult(selectedResidence, host, loggedinUser, comment, ratingfromuser);
                        if (!token.isEmpty()) {
                            Toast.makeText(c, "Your comment has been successfully submitted. Thank you!", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed()
    {
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        super.onBackPressed();
//        return;
        moveTaskToBack(true);
    }

    public String postResult(Residences residence, Users host, Users tenant, String comment, double rating)
    {
        Reviews reviews = new Reviews(residence, host, tenant, comment, rating);
        RestAPI restAPI = RestClient.getClient(token).create(RestAPI.class);
        Call<String> call = restAPI.postReview(reviews);
        try {
            Response<String> resp = call.execute();
            token = resp.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }
}
