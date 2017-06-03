package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fromRESTful.Residences;
import util.CustomListAdapter;
import util.RestCalls;
import util.Utils;

import static android.text.TextUtils.isEmpty;
import static util.Utils.DATE_TEXT_MONTH;
import static util.Utils.DATE_YEAR_FIRST;

public class SearchResultsActivity extends AppCompatActivity {

    ImageButton bhome;
    ImageButton binbox;
    ImageButton bprofile;
    ImageButton bswitch;
    ImageButton blogout;

    Boolean user;
    ListView list;
    TextView searchlist;

    String guests;
    String startDate;
    String endDate;

    CustomListAdapter adapter;

    SharedPreferences sharedPrefs;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";

    List<Residences> Recommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Bundle buser = getIntent().getExtras();
        String username = buser.getString("username");
        String city = buser.getString("city");
        guests = buser.getString("guests");
        startDate = buser.getString("startDate");
        endDate = buser.getString("endDate");
        user=buser.getBoolean("type");
        user=true;
        manageFooter();

        if (isEmpty(username)) finish();
        if (isEmpty(guests)) guests = "1";

        if (isEmpty(startDate) || Utils.isThisDateValid(startDate, DATE_YEAR_FIRST)) {
            startDate = Utils.getCurrentDate(DATE_YEAR_FIRST);
        }

        if (isEmpty(endDate) || Utils.isThisDateValid(endDate, DATE_YEAR_FIRST)) {
            endDate = Utils.getDefaultEndDate(DATE_YEAR_FIRST);
        }

        String str_city = (!isEmpty(city)) ? Character.toUpperCase(city.charAt(0)) + city.substring(1) : "Anywhere";
        String str_startdate = Utils.formatDate(startDate, DATE_TEXT_MONTH);
        String str_enddate = Utils.getDefaultEndDate(DATE_TEXT_MONTH);
        String str_guests = guests + " " + ((Integer.parseInt(guests) > 1) ? "guests" : "guest");

        searchlist = (TextView) findViewById(R.id.searchlist);
        searchlist.setText(str_city + ", " + str_startdate + "-" + str_enddate + ", " + str_guests);

        Recommendations = RestCalls.getRecommendations(username, city, startDate, endDate, Integer.parseInt(guests));

        String[] representativePhoto = new String [Recommendations.size()];
        String[] rescity = new String[Recommendations.size()];
        Double[] price = new Double[Recommendations.size()];
        float[] rating = new float[Recommendations.size()];

        for(int i=0; i < Recommendations.size(); i++){
            representativePhoto[i] = Recommendations.get(i).getPhotos();
            rescity[i] = Recommendations.get(i).getCity();
            price[i] = Recommendations.get(i).getMinPrice();
            rating[i] = (float)Recommendations.get(i).getAverageRating();
        }
        adapter = new CustomListAdapter(this, representativePhoto, rescity, price, rating);
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
    }

    /** SET UP FOOTER BUTTONS **/
    public void manageFooter() {
        Toolbar footerToolbar = (Toolbar) findViewById(R.id.footerToolbar);
        setSupportActionBar(footerToolbar);
        getSupportActionBar().setTitle(null);

        bhome = (ImageButton)findViewById(R.id.home);
        binbox = (ImageButton) findViewById(R.id.inbox);
        bprofile = (ImageButton) findViewById(R.id.profile);
        bswitch = (ImageButton) findViewById(R.id.host);
        blogout = (ImageButton) findViewById(R.id.logout);

        user=true;

        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeintent = new Intent(SearchResultsActivity.this, HomeActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type",user);
                homeintent.putExtras(btype);
                try {
                    startActivity(homeintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }

            }
        });

        binbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxintent = new Intent(SearchResultsActivity.this, InboxActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type",user);
                inboxintent.putExtras(btype);
                try {
                    startActivity(inboxintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileintent = new Intent(SearchResultsActivity.this, ProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type",user);
                profileintent.putExtras(buser);
                try {
                    startActivity(profileintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hostintent = new Intent(SearchResultsActivity.this, HostActivity.class);
                Bundle buser = new Bundle();
                user=false;
                buser.putBoolean("type",user);
                hostintent.putExtras(buser);
                startActivity(hostintent);
            }
        });

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                return;
            }
        });
    }

    /** MANAGE LOGOUT ACTION **/
    public void logout() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        Intent greetingintent = new Intent(SearchResultsActivity.this, GreetingActivity.class);
        startActivity(greetingintent);
    }
}
