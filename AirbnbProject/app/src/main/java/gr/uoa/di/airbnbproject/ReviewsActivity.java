package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ListAdapterReviews;
import util.RestAPI;
import util.RestCalls;
import util.RestClient;
import util.RetrofitCalls;
import util.Utils;

public class ReviewsActivity extends AppCompatActivity
{
    Boolean user, success;
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
    double rating;

    EditText etcomment;
    Button btnreview;
    RatingBar ratingBar;
    TextView txtrating;

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
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(loggedInUsername);
        loggedinUser        = getUserByUsername.get(0);
        ArrayList<Residences> getResidenceById = retrofitCalls.getResidenceById(residenceId);
        selectedResidence   = getResidenceById.get(0);
        host                = selectedResidence.getHostId();

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

        ArrayList<Reservations> reservationsByTenantIdandResidenceId= retrofitCalls.getReservationsByTenantIdandResidenceId(loggedinUser.getId(), selectedResidence.getId());
        if (reservationsByTenantIdandResidenceId.isEmpty())
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
                    }
                    else
                    {
                        boolean success = postResult(selectedResidence, host, loggedinUser, comment, ratingfromuser);
                        if (success)
                        {
                            Toast.makeText(c, "Your comment has been successfully submitted. Thank you!", Toast.LENGTH_SHORT).show();
                            return;
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

    public Boolean postResult(Residences residence, Users host, Users tenant, String comment, double rating)
    {
        success=false;

        Reviews reviews = new Reviews(residence, host, tenant, comment, rating);
        RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
        Call<Reviews> call = restAPI.postReview(reviews);
        call.enqueue(new Callback<Reviews>()
        {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if(response.isSuccessful())
                {
                    success=true;
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t)
            {
                success=false;
            }
        });

        return success;
    }
}
