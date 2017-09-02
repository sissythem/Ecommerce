package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import fromRESTful.Reservations;
import fromRESTful.Users;
import util.RecyclerAdapterReservations;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.CANCEL_RESERVATION_ACTION;
import static util.Utils.CONTACT_HOST_ACTION;
import static util.Utils.CONTACT_USER_ACTION;
import static util.Utils.FORMAT_DATE_DMY;
import static util.Utils.VIEW_RESIDENCE_ACTION;
import static util.Utils.convertTimestampToDateStr;
import static util.Utils.goToActivity;

public class HistoryReservationsActivity extends AppCompatActivity {
    Boolean user;
    String token;
    Users loggedinUser;
    Toolbar toolbar;
    Context c;
    ArrayList<Reservations> userReservations;
    Bundle buser;

    RecyclerView reservationsRecyclerView;
    RecyclerAdapterReservations adapterReservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_reservations);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = Utils.getSessionData(HistoryReservationsActivity.this);
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

        buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("My Reservations");
        setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        /** Handle back action. This activity is called either from ResidenceActivity or from ProfileActivity */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackAction();
            }
        });

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        //Get the user
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        loggedinUser = getUserByUsername.get(0);
        //If activity is called from ResidenceActivity, show all reservations for this residence
        if (buser.containsKey("residenceId")) {
            toolbar.setTitle("Reservations made by users");
            userReservations = retrofitCalls.getReservationsByResidenceId(token, buser.getInt("residenceId"));
        }
        //if the activity is called from ProfileActivity, show all reservations by the specific user
        else
        {
            userReservations = retrofitCalls.getReservationsByTenantId(token, loggedinUser.getId().toString());
        }

        /** Use of RecyclerView in order to show the reservations */
        reservationsRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        reservationsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        reservationsRecyclerView.setHasFixedSize(true);
        registerForContextMenu(reservationsRecyclerView);

        try {
            if (userReservations.size() > 0) {
                adapterReservations = new RecyclerAdapterReservations(this, user, userReservations, buser.containsKey("residenceId"));
                reservationsRecyclerView.setAdapter(adapterReservations);
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HistoryReservationsActivity.this, user);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        super.onContextItemSelected(item);
        /** Menu options by item*/
        Integer resId = userReservations.get(item.getItemId()).getResidenceId().getId();
        String resTitle = userReservations.get(item.getItemId()).getResidenceId().getTitle();
        /** User can cancel his reservation only if reservation date is not in the past */
        if (item.getTitle().equals(CANCEL_RESERVATION_ACTION)) {
            String currentdate = Utils.getCurrentDate(FORMAT_DATE_DMY);
            Date current = Utils.ConvertStringToDate(currentdate, FORMAT_DATE_DMY);

            String startDate = convertTimestampToDateStr(userReservations.get(item.getItemId()).getStartDate(), FORMAT_DATE_DMY);
            Date start = Utils.ConvertStringToDate(startDate, FORMAT_DATE_DMY);

            if(current.after(start))
            {
                new AlertDialog.Builder(HistoryReservationsActivity.this).setTitle("Cancel Reservation").setMessage("You cannot delete this reservation since it is in the past").show();
            } else {
                new AlertDialog.Builder(HistoryReservationsActivity.this)
                        .setTitle("Cancel Reservation").setMessage("Are you sure you want to cancel your reservation?").setIcon(R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                RetrofitCalls retrofitCalls = new RetrofitCalls();
                                token = retrofitCalls.deleteReservation(token, userReservations.get(item.getItemId()).getId());
                                if (!token.isEmpty() && token != null && token != "not") {
                                    Toast.makeText(c, "Reservation was cancelled!", Toast.LENGTH_SHORT).show();
                                    userReservations.remove(item.getItemId());
                                    adapterReservations.setReservations(userReservations);
                                    adapterReservations.notifyDataSetChanged();
                                } else if (token.equals("not")) {
                                    Toast.makeText(c, "Failed to cancel reservation! Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                                    Utils.logout(HistoryReservationsActivity.this);
                                    finish();
                                } else {
                                    Toast.makeText(c, "Something went wrong, reservation was not cancelled. Please try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();

            }
            /** User can see the residence he has performed the specific reservation */
        } else if (item.getTitle().equals(VIEW_RESIDENCE_ACTION)) {
            Bundle btype = new Bundle();

            btype.putBoolean("type", user);
            btype.putString("source", "reservations");
            btype.putInt("residenceId", resId);
            goToActivity(HistoryReservationsActivity.this, ResidenceActivity.class, btype);
            /** User can contact the host of the residence */
        } else if (item.getTitle().equals(CONTACT_HOST_ACTION)) {
            Bundle bmessage = new Bundle();
            bmessage.putBoolean("type", user);
            bmessage.putInt("currentUserId", loggedinUser.getId());
            bmessage.putInt("toUserId", userReservations.get(item.getItemId()).getResidenceId().getHostId().getId());
            bmessage.putString("msgSubject", resTitle);
            bmessage.putInt("residenceId", resId);
            goToActivity(HistoryReservationsActivity.this, MessageActivity.class, bmessage);
            /** The host of this residence can contact the user who has performed the selected reservation */
        } else if (item.getTitle().equals(CONTACT_USER_ACTION)) {
            Bundle bmessage = new Bundle();
            bmessage.putBoolean("type", user);
            bmessage.putInt("currentUserId", loggedinUser.getId());
            bmessage.putInt("toUserId", userReservations.get(item.getItemId()).getTenantId().getId());
            bmessage.putString("msgSubject", resTitle);
            bmessage.putInt("residenceId", resId);
            goToActivity(HistoryReservationsActivity.this, MessageActivity.class, bmessage);
        } else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        handleBackAction();
    }

    public void handleBackAction(){
        if (buser.containsKey("source")) {
            if(buser.getString("source").equals("residence")){
                Bundle btores = new Bundle();
                btores.putBoolean("type", user);
                btores.putInt("residenceId", buser.getInt("residenceId"));
                Utils.goToActivity(HistoryReservationsActivity.this, ResidenceActivity.class, btores);
            }
            else if(buser.getString("source").equals("host")){
                Bundle btores = new Bundle();
                btores.putBoolean("type", user);
                Utils.goToActivity(HistoryReservationsActivity.this, HostActivity.class, btores);
            }
        } else{
            Utils.manageBackButton(HistoryReservationsActivity.this, ProfileActivity.class, user);
        }
    }
}