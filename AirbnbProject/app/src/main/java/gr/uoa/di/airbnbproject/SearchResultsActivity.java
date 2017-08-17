package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fromRESTful.Residences;
import util.ListAdapterResidences;
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
    ListView list;
    TextView searchlist;
    String token;

    ListAdapterResidences adapter;
    List<Residences> Recommendations;

    Integer guests;
    String startDate, endDate;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = getSessionData(SearchResultsActivity.this);
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

        setContentView(R.layout.activity_search_results);

        Bundle buser    = getIntent().getExtras();
        String city     = buser.getString("city");
        guests          = buser.getInt("guests");
        startDate       = buser.getString("startDate");
        endDate         = buser.getString("endDate");
        user            = buser.getBoolean("type");
        user=true;
        c=this;

        if(Utils.isTokenExpired(token)) {
            Utils.logout(this);
            finish();
        }
        if (guests <= 0) guests = 1;

        if (isEmpty(startDate) || !Utils.isThisDateValid(startDate, FORMAT_DATE_YMD)) startDate = Utils.getCurrentDate(FORMAT_DATE_YMD);
        if (isEmpty(endDate) || !Utils.isThisDateValid(endDate, FORMAT_DATE_YMD)) endDate = Utils.getDefaultEndDate(FORMAT_DATE_YMD);

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

        String[] title                  = new String [Recommendations.size()];
        String[] representativePhoto    = new String [Recommendations.size()];
        String[] rescity                = new String[Recommendations.size()];
        Double[] price                  = new Double[Recommendations.size()];
        float[] rating                  = new float[Recommendations.size()];

        for(int i=0; i < Recommendations.size(); i++){
            title[i]                = Recommendations.get(i).getTitle();
            representativePhoto[i]  = Recommendations.get(i).getPhotos();
            rescity[i]              = Recommendations.get(i).getCity();
            price[i]                = Recommendations.get(i).getMinPrice();
            rating[i]               = (float)Recommendations.get(i).getAverageRating();
        }
        adapter = new ListAdapterResidences(SearchResultsActivity.this, title, representativePhoto, rescity, price, rating);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showResidenceIntent = new Intent(SearchResultsActivity.this, ResidenceActivity.class);
                Bundle bsearch = new Bundle();
                bsearch.putBoolean("type", user);
                bsearch.putInt("guests", guests);
                bsearch.putString("startDate", startDate);
                bsearch.putString("endDate", endDate);
                startActivity(showResidenceIntent);
            }
        });

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(SearchResultsActivity.this, user);
    }
}
