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

import fromRESTful.Reservations;
import fromRESTful.Users;
import util.ListAdapterReservations;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.CANCEL_RESERVATION_ACTION;
import static util.Utils.CONTACT_HOST_ACTION;
import static util.Utils.CONTACT_USER_ACTION;
import static util.Utils.FORMAT_DATE_YMD;
import static util.Utils.VIEW_RESIDENCE_ACTION;
import static util.Utils.convertTimestampToDateStr;
import static util.Utils.reloadActivity;

public class HistoryReservationsActivity extends AppCompatActivity {
    Boolean user;
    String token;
    Users loggedinUser;
    int[] residenceId;
    String[] residenceTitle;
    Toolbar toolbar;

    Context c;
    ListAdapterReservations adapter;
    ListView reservationsList;
    ArrayList<Reservations> userReservations;
    Bundle buser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = Utils.getSessionData(HistoryReservationsActivity.this);
        token = sessionData.getToken();
        c = this;

        if (!sessionData.getUserLoggedInState()) {
            Utils.logout(this);
            finish();
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        if(Utils.isTokenExpired(sessionData.getToken())){
            Toast.makeText(c, "Session is expired", Toast.LENGTH_SHORT).show();
            Utils.logout(this);
            finish();
            return;
        }

        setContentView(R.layout.activity_history_reservations);
        buser = getIntent().getExtras();
        user = buser.getBoolean("type");

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.manageBackButton(HistoryReservationsActivity.this, ProfileActivity.class, user);
            }
        });

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        loggedinUser = getUserByUsername.get(0);

        if (buser.containsKey("residenceId")) {
            toolbar.setTitle("Reservations made by users");
            System.out.println(buser.getInt("residenceId"));
            userReservations = retrofitCalls.getReservationsByResidenceId(token, buser.getInt("residenceId"));
        } else {
            userReservations = retrofitCalls.getReservationsByTenantId(token, loggedinUser.getId().toString());
        }

        residenceId             = new int [userReservations.size()];
        residenceTitle          = new String[userReservations.size()];
        String[] startDate      = new String[userReservations.size()];
        String[] endDate        = new String[userReservations.size()];

        for(int i=0; i<userReservations.size();i++) {
            residenceId[i]      = userReservations.get(i).getResidenceId().getId();
            residenceTitle[i]   = userReservations.get(i).getResidenceId().getTitle();

            startDate[i]    = convertTimestampToDateStr(userReservations.get(i).getResidenceId().getAvailableDateStart(), FORMAT_DATE_YMD);
            endDate[i]      = convertTimestampToDateStr(userReservations.get(i).getResidenceId().getAvailableDateEnd(), FORMAT_DATE_YMD);
        }
        adapter = new ListAdapterReservations(this, user, residenceTitle, startDate, endDate);
        reservationsList = (ListView)findViewById(R.id.reservationsList);
        reservationsList.setAdapter(adapter);
        registerForContextMenu(reservationsList);

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HistoryReservationsActivity.this, user);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Reservation Options");

        menu.add(0, info.position, 0, VIEW_RESIDENCE_ACTION);
        if (buser.containsKey("residenceId")) {
            menu.add(0, info.position, 1, CONTACT_USER_ACTION);
        } else {
            menu.add(0, info.position, 1, CONTACT_HOST_ACTION);
            menu.add(0, info.position, 2, CANCEL_RESERVATION_ACTION);
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        super.onContextItemSelected(item);
        if (item.getTitle().equals(CANCEL_RESERVATION_ACTION)) {
            new AlertDialog.Builder(HistoryReservationsActivity.this)
                .setTitle("Cancel Reservation").setMessage("Are you sure you want to cancel your reservation?").setIcon(R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RetrofitCalls retrofitCalls = new RetrofitCalls();
                        token = retrofitCalls.deleteReservation(token, userReservations.get(item.getItemId()).getId());
                        if (!token.isEmpty() && token!=null && token!="not") {
                            Toast.makeText(c, "Reservation was cancelled!", Toast.LENGTH_SHORT).show();
                            reloadHistoryReservations();
                        } else if (token.equals("not")) {
                            Toast.makeText(c, "Failed to cancel reservation! Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                            Utils.logout(HistoryReservationsActivity.this);
                            finish();
                        } else {
                            Toast.makeText(c, "Something went wrong, reservation was not cancelled. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();

        } else if (item.getTitle().equals(VIEW_RESIDENCE_ACTION)) {
            Intent showResidenceIntent = new Intent(HistoryReservationsActivity.this, ResidenceActivity.class);
            Bundle btype = new Bundle();

            System.out.println(user);
            System.out.println(residenceId[item.getItemId()]);
            btype.putBoolean("type", user);
            btype.putInt("residenceId", residenceId[item.getItemId()]);
            showResidenceIntent.putExtras(btype);
            try {
                startActivity(showResidenceIntent);
                finish();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        } else if (item.getTitle().equals(CONTACT_HOST_ACTION)) {
            Intent messageIntent = new Intent(HistoryReservationsActivity.this, MessageActivity.class);

            Bundle bmessage = new Bundle();
            bmessage.putBoolean("type", user);
            bmessage.putInt("currentUserId", loggedinUser.getId());
            bmessage.putInt("toUserId", userReservations.get(item.getItemId()).getResidenceId().getHostId().getId());
            bmessage.putString("msgSubject", residenceTitle[item.getItemId()]);
            bmessage.putInt("residenceId", residenceId[item.getItemId()]);
            messageIntent.putExtras(bmessage);
            try {
                startActivity(messageIntent);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e("",e.getMessage());
            }
        } else if (item.getTitle().equals(CONTACT_USER_ACTION)) {
            Intent messageIntent = new Intent(HistoryReservationsActivity.this, MessageActivity.class);

            Bundle bmessage = new Bundle();
            bmessage.putBoolean("type", user);
            bmessage.putInt("currentUserId", loggedinUser.getId());
            bmessage.putInt("toUserId", userReservations.get(item.getItemId()).getTenantId().getId());
            bmessage.putString("msgSubject", residenceTitle[item.getItemId()]);
            bmessage.putInt("residenceId", residenceId[item.getItemId()]);
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

    public void reloadHistoryReservations() {
        Bundle bupdated = new Bundle();
        bupdated.putBoolean("type", user);
        reloadActivity(c, bupdated);
    }
}