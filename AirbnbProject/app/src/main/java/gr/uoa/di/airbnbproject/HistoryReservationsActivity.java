package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
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
import util.Session;
import util.Utils;

public class HistoryReservationsActivity extends AppCompatActivity {
    Boolean user;
    String token;
    Users loggedinUser;
    int[]residenceId;

    Context c;
    ListAdapterReservations adapter;
    ListView reservationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = Utils.getSessionData(HistoryReservationsActivity.this);
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
        setContentView(R.layout.activity_history_reservations);
        c = this;

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Utils.checkToken(token, HistoryReservationsActivity.this);
        ArrayList<Users> getUserByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        loggedinUser = getUserByUsername.get(0);

        Utils.checkToken(token, HistoryReservationsActivity.this);
        ArrayList<Reservations> userReservations = retrofitCalls.getReservationsByTenantId(token, loggedinUser.getId().toString());

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

}
