package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import fromRESTful.Reservations;
import fromRESTful.Users;
import util.ListAdapterReservations;
import util.RetrofitCalls;
import util.Utils;

public class HistoryReservationsActivity extends AppCompatActivity
{
    Boolean user;
    String loggedInUsername;
    Users loggedinUser;
    int[]residenceId;

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    Context c;
    ListAdapterReservations adapter;
    ListView reservationsList;

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

        setContentView(R.layout.activity_history_reservations);

        c = this;
        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(loggedInUsername);
        loggedinUser = getUserByUsername.get(0);

        ArrayList<Reservations> userReservations = retrofitCalls.getReservationsByResidenceId(loggedinUser.getId());

        residenceId             = new int [userReservations.size()];
        String[] residenceTitle = new String[userReservations.size()];
        String[] startDate      = new String[userReservations.size()];
        String[] endDate        = new String[userReservations.size()];

        for(int i=0; i<userReservations.size();i++) {
            residenceId[i] = userReservations.get(i).getResidenceId().getId();
            residenceTitle[i] = userReservations.get(i).getResidenceId().getTitle();
            Date date = userReservations.get(i).getResidenceId().getAvailableDateStart();
            startDate[i] = Utils.ConvertDateToString(date, Utils.DATABASE_DATE_FORMAT);
        }
        adapter = new ListAdapterReservations(this, residenceTitle, startDate, endDate);
        reservationsList = (ListView)findViewById(R.id.reservationsList);
        reservationsList.setAdapter(adapter);

        reservationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent showResidenceIntent = new Intent(HistoryReservationsActivity.this, ResidenceActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type", user);
                btype.putInt("residenceId", residenceId[position]);
                showResidenceIntent.putExtras(btype);
                try {
                    startActivity(showResidenceIntent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HistoryReservationsActivity.this, user);
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
