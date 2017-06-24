package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Date;

import fromRESTful.Residences;
import fromRESTful.Users;
import util.ReservationParameters;
import util.RestCallManager;
import util.RestCallParameters;
import util.RestCalls;
import util.RestPaths;
import util.Utils;

public class ResidenceActivity extends AppCompatActivity implements OnMapReadyCallback  {
    Boolean user;
    String username, date_start, date_end, guests;

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    int residenceId;
    Context c;

    TextView tvTitle, tvType, tvAddress, tvCity, tvCountry, tvHostName, tvAbout, tvAmenities, tvCancellationPolicy, tvHostAbout, tvRules, tvPrice;
    ImageButton ibContact;
    Button bReviews, bBook;
    RatingBar rating;
    GoogleMap mMap;
    CalendarView availabilityCalendar;

    Users loggedinUser, host;
    Residences selectedResidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        username = sharedPrefs.getString("currentLoggedInUser", "");

        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_residence);
        c = this;

        Bundle buser        = getIntent().getExtras();
        user                = buser.getBoolean("type");
        date_start          = buser.getString("startDate");
        date_end            = buser.getString("endDate");
        guests              = buser.getString("guests");

        residenceId         = buser.getInt("residenceId");
        loggedinUser        = RestCalls.getUser(username);
        selectedResidence   = RestCalls.getResidenceById(residenceId);

        setUpResidenceView();

        /** BACK BUTTON **/
        Utils.manageBackButton(this, (user)?HomeActivity.class:HostActivity.class);
    }

    public void setUpResidenceView () {
        host = selectedResidence.getHostId();

        tvTitle                 = (TextView)findViewById(R.id.tvTitle);
        tvType                  = (TextView)findViewById(R.id.tvType);
        tvAddress               = (TextView)findViewById(R.id.tvAddress);
        tvCity                  = (TextView)findViewById(R.id.tvCity);
        tvCountry               = (TextView)findViewById(R.id.tvCountry);
        tvHostName              = (TextView)findViewById(R.id.tvHostName);
        tvAbout                 = (TextView)findViewById(R.id.tvAboutText);
        tvAmenities             = (TextView)findViewById(R.id.tvAmenities);
        tvCancellationPolicy    = (TextView)findViewById(R.id.tvCancellationPolicy);
        tvHostAbout             = (TextView)findViewById(R.id.tvHostAbout);
        tvRules                 = (TextView)findViewById(R.id.tvRules);
        tvPrice                 = (TextView)findViewById(R.id.price);

        rating                  = (RatingBar)findViewById(R.id.rating);
        bReviews                = (Button)findViewById(R.id.btnReviews);
        bBook                   = (Button)findViewById(R.id.btnReservation);

        Date startDate = selectedResidence.getAvailableDateStart();
        Date endDate = selectedResidence.getAvailableDateEnd();
        if(user == false) bBook.setText("DELETE");
        if(date_start == null || date_end == null) bBook.setText("See Dates");

        ibContact               = (ImageButton) findViewById(R.id.ibContact);
        availabilityCalendar    = (CalendarView)findViewById(R.id.calendar);
        availabilityCalendar.setMinDate(startDate.getTime());
        availabilityCalendar.setMaxDate(endDate.getTime());

        tvTitle.setText(selectedResidence.getTitle());
        tvType.setText(selectedResidence.getType());
        tvAddress.setText(selectedResidence.getAddress());
        tvCity.setText(selectedResidence.getCity());
        tvCountry.setText(selectedResidence.getCountry());
        tvHostName.setText(host.getFirstName() +"" + host.getLastName());
        tvHostName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(ResidenceActivity.this, ViewHostProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                buser.putInt("host", host.getId());
                profileIntent.putExtras(buser);
                try{
                    startActivity(profileIntent);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        tvAbout.setText(selectedResidence.getAbout());
        tvAmenities.setText(selectedResidence.getAmenities());
        tvCancellationPolicy.setText(selectedResidence.getCancellationPolicy());
        tvHostAbout.setText(host.getAbout());
        tvRules.setText(selectedResidence.getRules());
        tvPrice.setText(Double.toString(selectedResidence.getMinPrice()));

        rating.setRating((float)selectedResidence.getAverageRating());

        ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(ResidenceActivity.this, MessageActivity.class);

                Bundle bmessage = new Bundle();
                bmessage.putBoolean("type", user);
                bmessage.putInt("currentUserId", RestCalls.getUserId(username));
                bmessage.putInt("toUserId", host.getId());
                bmessage.putString("msgSubject", tvTitle.getText().toString());
                bmessage.putInt("residenceId", residenceId);
                messageIntent.putExtras(bmessage);
                try{
                    startActivity(messageIntent);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        bReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewsIntent = new Intent(ResidenceActivity.this, ReviewsActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                reviewsIntent.putExtras(buser);
                try {
                    startActivity(reviewsIntent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user) {
                    boolean result = deleteResidence(Integer.toString(residenceId));
                    if(result){
                        Intent hostIntent = new Intent(ResidenceActivity.this, HostActivity.class);
                        Bundle buser = new Bundle();
                        user=false;
                        buser.putBoolean("type", user);
                        hostIntent.putExtras(buser);
                        try
                        {
                            startActivity(hostIntent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if(date_start == null || date_end == null || guests == null) {
                        Intent bookIntent = new Intent(ResidenceActivity.this, ShowCalendarActivity.class);
                        Bundle bundle = new Bundle();
                        user=true;
                        bundle.putBoolean("type", user);
                        bundle.putInt("residenceId", residenceId);
                        bookIntent.putExtras(bundle);
                        startActivity(bookIntent);
                    } else {
                        boolean success = PostResult();
                        if(success){
                            Intent homeIntent = new Intent(ResidenceActivity.this, HomeActivity.class);
                            user=true;
                            Bundle buser=new Bundle();
                            buser.putBoolean("type", user);
                            homeIntent.putExtras(buser);
                            startActivity(homeIntent);
                            finish();
                        }
                        else {
                            Toast.makeText(c, "Booking failed, please try again!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }
        });
    }

    public Boolean deleteResidence (String id) {
        boolean success = true;
        String deleteReviewsURL = RestPaths.deleteReviewsByResidence(Integer.toString(residenceId));
        RestCallManager deleteReviewsManager = new RestCallManager();
        RestCallParameters deleteReviewsParams = new RestCallParameters(deleteReviewsURL, "DELETE", "TEXT", "");

        String reviewsResponse;
        deleteReviewsManager.execute(deleteReviewsParams);
        reviewsResponse = (String)deleteReviewsManager.getRawResponse().get(0);

        String deleteReservationsURL = RestPaths.deleteReservationsByResidence(Integer.toString(residenceId));
        RestCallManager deleteReservationsManager = new RestCallManager();
        RestCallParameters deleteReservationsParams = new RestCallParameters(deleteReservationsURL, "DELETE", "TEXT", "");

        String reservationsResponse;
        deleteReservationsManager.execute(deleteReservationsParams);
        reservationsResponse = (String)deleteReservationsManager.getRawResponse().get(0);

        //TODO delete all data from all other tables
        RestCallManager deleteResidenceManager = new RestCallManager();
        RestCallParameters deleteParameters = new RestCallParameters(RestPaths.deleteResidenceById(id), "DELETE", "TEXT", "");

        String response;
        deleteResidenceManager.execute(deleteParameters);

        response = (String)deleteResidenceManager.getRawResponse().get(0);
        if (response.equals("OK") && reviewsResponse.equals("OK") && reservationsResponse.equals("OK")) ;
        else success = false;

        return  success;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng address = new LatLng(lat,longt);
//        mMap.addMarker(new MarkerOptions().position(address).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
    }

    public boolean PostResult() {
        boolean success = true;
        ReservationParameters reservationParameters = new ReservationParameters(loggedinUser, selectedResidence, date_start, date_end, guests);

        RestCallManager reservationPostManager = new RestCallManager();
        RestCallParameters postparameters = new RestCallParameters(RestPaths.AllReservations, "POST", "", reservationParameters.getReservationParameters());

//        ArrayList<String> PostResponse ;
        String response;

        reservationPostManager.execute(postparameters);
//            PostResponse = userpost.get(1000, TimeUnit.SECONDS);
        response = (String)reservationPostManager.getRawResponse().get(0);
//            String result = PostResponse.get(0);
        if (response.equals("OK")) ;
        else success = false;

        return success;
    }
}