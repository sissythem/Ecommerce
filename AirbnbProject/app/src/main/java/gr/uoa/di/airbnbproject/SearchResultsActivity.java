package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Residences;
import util.RecyclerAdapterResidences;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static android.text.TextUtils.isEmpty;
import static util.Utils.FORMAT_DATE_DM;
import static util.Utils.FORMAT_DATE_YMD;
import static util.Utils.convertDateToMillisSec;
import static util.Utils.getSessionData;

public class SearchResultsActivity extends AppCompatActivity {
    Boolean user;
    TextView searchlist;
    String token;

    ArrayList<Residences> Recommendations;

    Integer guests;
    String startDate, endDate;
    Context c;

    RecyclerView residencesRecyclerView;
    RecyclerView.Adapter residencesAdapter;
    RecyclerView.LayoutManager residencesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = getSessionData(SearchResultsActivity.this);
        token = sessionData.getToken();
        c=this;
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

        setContentView(R.layout.activity_search_results);

        Bundle buser    = getIntent().getExtras();
        String city     = buser.getString("city");
        guests          = buser.getInt("guests");
        startDate       = buser.getString("startDate");
        endDate         = buser.getString("endDate");
        user            = buser.getBoolean("type");
        user=true;

        if (guests <= 0) guests = 1;

        if (isEmpty(startDate) || startDate==null || !Utils.isThisDateValid(startDate, FORMAT_DATE_YMD)) startDate = Utils.getCurrentDate(FORMAT_DATE_YMD);
        if (isEmpty(endDate) || endDate==null || !Utils.isThisDateValid(endDate, FORMAT_DATE_YMD)) endDate = Utils.getDefaultEndDate(FORMAT_DATE_YMD);

        String str_city = (!isEmpty(city)) ? Character.toUpperCase(city.charAt(0)) + city.substring(1) : "Anywhere";
        String str_startdate = Utils.formatDate(startDate, FORMAT_DATE_DM);
        String str_enddate = Utils.getDefaultEndDate(FORMAT_DATE_DM);
        String str_guests = guests + " " + ((guests > 1) ? "guests" : "guest");

        searchlist = (TextView) findViewById(R.id.searchlist);
        searchlist.setText(str_city + ", " + str_startdate + "-" + str_enddate + ", " + str_guests);

        RetrofitCalls retrofitCalls = new RetrofitCalls();

        long start_timestamp = convertDateToMillisSec(startDate, FORMAT_DATE_YMD);
        long end_timestamp = convertDateToMillisSec(endDate, FORMAT_DATE_YMD);

        Recommendations = retrofitCalls.getRecommendations(token, sessionData.getUsername(), city, start_timestamp, end_timestamp, guests);
        residencesRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        residencesLayoutManager = new GridLayoutManager(this, 1);
        residencesRecyclerView.setLayoutManager(residencesLayoutManager);
        residencesRecyclerView.setHasFixedSize(true);

        try {
            if (Recommendations.size() > 0) {
                residencesAdapter = new RecyclerAdapterResidences(this, user, Recommendations);
                residencesRecyclerView.setAdapter(residencesAdapter);
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(SearchResultsActivity.this, user);
    }
}
