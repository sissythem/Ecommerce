package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Reviews;
import fromRESTful.Users;
import util.ListAdapterReviews;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.CONTACT_HOST_ACTION;
import static util.Utils.DELETE_ACTION;
import static util.Utils.VIEW_RESIDENCE_ACTION;
import static util.Utils.reloadActivity;

public class HistoryReviewsActivity extends AppCompatActivity {
    Boolean user;
    String token;
    Users loggedinUser;
    Toolbar toolbar;
    Context c;
    ListAdapterReviews adapter;
    ListView reviewsList;
    ArrayList<Reviews> userReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = Utils.getSessionData(HistoryReviewsActivity.this);
        c = this;
        token = sessionData.getToken();
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

        setContentView(R.layout.activity_history_reviews);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("My Reviews");
        setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        //handle back action
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.manageBackButton(HistoryReviewsActivity.this, ProfileActivity.class, user);
            }
        });

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        //Get the user
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        loggedinUser = getUserByUsername.get(0);
        /** Get all reviews uploaded by the user */
        userReviews = retrofitCalls.getReviewsByTenantId(token, loggedinUser.getId().toString());

        String[] representativePhoto    = new String [userReviews.size()];
        String[] name                   = new String[userReviews.size()];
        String[] comment                = new String[userReviews.size()];
        double[] rating                 = new double[userReviews.size()];

        for(int i=0; i<userReviews.size();i++) {
            representativePhoto[i]  = userReviews.get(i).getResidenceId().getPhotos();
            name[i]                 = userReviews.get(i).getResidenceId().getTitle();
            comment[i]              = userReviews.get(i).getComment();
            rating[i]               = userReviews.get(i).getRating();
        }
        /** List adapter for displaying the reviews. For each review we show user's photo, his name, the comment and the rating */
        adapter = new ListAdapterReviews(this, representativePhoto, name, comment, rating);
        reviewsList = (ListView)findViewById(R.id.reviewslist);
        reviewsList.setAdapter(adapter);
        registerForContextMenu(reviewsList);

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HistoryReviewsActivity.this, user);
}
    /** Menu which appears for each selected list item */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Reviews Options");

        menu.add(0, info.position, 0, VIEW_RESIDENCE_ACTION);
        menu.add(0, info.position, 1, CONTACT_HOST_ACTION);
        menu.add(0, info.position, 2, DELETE_ACTION);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        super.onContextItemSelected(item);
        /** User can delete the selected review */
        if (item.getTitle().equals(DELETE_ACTION)) {

            new AlertDialog.Builder(HistoryReviewsActivity.this)
                .setTitle("Delete Review").setMessage("Do you really want to delete your review?").setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RetrofitCalls retrofitCalls = new RetrofitCalls();
                        token = retrofitCalls.deleteReview(token, userReviews.get(item.getItemId()).getId());
                        if (!token.isEmpty() && token!=null && token!="not") {
                            Toast.makeText(c, "Your review was deleted!", Toast.LENGTH_SHORT).show();
                            reloadHistoryReviews();
                        } else if (token.equals("not")) {
                            Toast.makeText(c, "Failed to delete review! Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                            Utils.logout(HistoryReviewsActivity.this);
                            finish();
                        } else {
                            Toast.makeText(c, "Something went wrong, review was not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

            /** User can view respective residence */
        } else if (item.getTitle().equals(VIEW_RESIDENCE_ACTION)) {
            Intent showResidenceIntent = new Intent(HistoryReviewsActivity.this, ResidenceActivity.class);
            Bundle btype = new Bundle();
            btype.putBoolean("type", user);
            btype.putString("source", "reviews");
            btype.putInt("residenceId", userReviews.get(item.getItemId()).getResidenceId().getId());
            showResidenceIntent.putExtras(btype);
            try {
                startActivity(showResidenceIntent);
                finish();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
            /** User can contact the host of this residence */
        } else if (item.getTitle().equals(CONTACT_HOST_ACTION)) {
            Intent messageIntent = new Intent(HistoryReviewsActivity.this, MessageActivity.class);

            Bundle bmessage = new Bundle();
            bmessage.putBoolean("type", user);
            bmessage.putInt("currentUserId", loggedinUser.getId());
            bmessage.putInt("toUserId", userReviews.get(item.getItemId()).getResidenceId().getHostId().getId());
            bmessage.putString("msgSubject", userReviews.get(item.getItemId()).getResidenceId().getTitle());
            bmessage.putInt("residenceId", userReviews.get(item.getItemId()).getResidenceId().getId());
            messageIntent.putExtras(bmessage);
            try {
                startActivity(messageIntent);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e("",e.getMessage());
            }
        } else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void reloadHistoryReviews() {
        Bundle bupdated = new Bundle();
        bupdated.putBoolean("type", user);
        reloadActivity(c, bupdated);
    }
}
