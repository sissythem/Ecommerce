package gr.uoa.di.airbnbproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fromRESTful.Residences;
import util.ListAdapterResidences;
import util.RestCalls;
import util.Utils;

import static android.text.TextUtils.isEmpty;
import static util.Utils.DATE_YEAR_FIRST;
import static util.Utils.DATE_TEXT_MONTH;

public class SearchResultsActivity extends AppCompatActivity {
    Boolean user;
    ListView list;
    TextView searchlist;

    ListAdapterResidences adapter;

    SharedPreferences sharedPrefs;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";

    List<Residences> Recommendations;

    String guests, startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Bundle buser    = getIntent().getExtras();
        String username = buser.getString("username");
        String city     = buser.getString("city");
        guests          = buser.getString("guests");
        startDate       = buser.getString("startDate");
        endDate         = buser.getString("endDate");
        user            = buser.getBoolean("type");
        user=true;

        if (isEmpty(username)) finish();
        if (isEmpty(guests)) guests = "1";

        if (isEmpty(startDate) || Utils.isThisDateValid(startDate, DATE_YEAR_FIRST)) startDate = Utils.getCurrentDate(DATE_YEAR_FIRST);
        if (isEmpty(endDate) || Utils.isThisDateValid(endDate, DATE_YEAR_FIRST)) endDate = Utils.getDefaultEndDate(DATE_YEAR_FIRST);

        String str_city = (!isEmpty(city)) ? Character.toUpperCase(city.charAt(0)) + city.substring(1) : "Anywhere";
        String str_startdate = Utils.formatDate(startDate, DATE_TEXT_MONTH);
        String str_enddate = Utils.getDefaultEndDate(DATE_TEXT_MONTH);
        String str_guests = guests + " " + ((Integer.parseInt(guests) > 1) ? "guests" : "guest");

        searchlist = (TextView) findViewById(R.id.searchlist);
        searchlist.setText(str_city + ", " + str_startdate + "-" + str_enddate + ", " + str_guests);

        Recommendations = RestCalls.getRecommendations(username, city, startDate, endDate, Integer.parseInt(guests));

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
                bsearch.putString("guests", guests);
                bsearch.putString("startDate", startDate);
                bsearch.putString("endDate", endDate);
                startActivity(showResidenceIntent);
            }
        });

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(SearchResultsActivity.this, true);
    }
}
