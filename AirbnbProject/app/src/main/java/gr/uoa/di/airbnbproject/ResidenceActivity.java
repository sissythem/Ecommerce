package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.RestAPI;
import util.RestCalls;
import util.RestClient;
import util.RetrofitCalls;
import util.Utils;

public class ResidenceActivity extends FragmentActivity implements OnMapReadyCallback
{
    Boolean user, success;
    String username, date_start, date_end, guests;

    static final String TAG = "RESIDENCE_ACTIVITY";

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    int residenceId, maxGuests, guestsInt;
    Context c;

    TextView tvTitle, tvType, tvAddress, tvCity, tvCountry, tvHostName, tvAbout, tvAmenities, tvCancellationPolicy, tvHostAbout, tvRules, tvPrice;
    ImageButton ibContact;
    Button bReviews, bBook;
    RatingBar rating;
    EditText etGuests;
    GoogleMap mMap;

    Users loggedinUser, host;
    Residences selectedResidence;

    private CaldroidFragment caldroidFragment;
    Date[] selectedDates;
    Date selectedStartDate, selectedEndDate;
    Map<Date, Integer> NumGuestsPerDay;
    ArrayList <Date> reservedDates, datesDisabled_byGuestCount;

    RetrofitCalls retrofitCalls;

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

        retrofitCalls = new RetrofitCalls();
        ArrayList<Users> userLoggedIn = retrofitCalls.getUserbyUsername(username);
        loggedinUser = userLoggedIn.get(0);
        selectedResidence   = retrofitCalls.getResidenceById(residenceId);

        setUpResidenceView();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapResidence);
        mapFragment.getMapAsync(this);

        /** BACK BUTTON **/
        Utils.manageBackButton(this, (user)?HomeActivity.class:HostActivity.class, user);
        if(!user)
        {
            //if user is logged in as host, this button does not appear
            bBook.setVisibility(View.INVISIBLE);
            RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
            ratingBar.setVisibility(View.INVISIBLE);
            TextView tvPrice = (TextView)findViewById(R.id.price);
            tvPrice.setVisibility(View.INVISIBLE);
            /** FOOTER TOOLBAR **/
            Utils.manageFooter(ResidenceActivity.this, user);
        }
    }

    public void setUpResidenceView ()
    {
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
        etGuests                = (EditText)findViewById(R.id.etGuests);
        etGuests.setSelected(false);

        ibContact = (ImageButton) findViewById(R.id.ibContact);
        tvTitle.setText(selectedResidence.getTitle());
        tvType.setText(selectedResidence.getType());
        tvAddress.setText(selectedResidence.getAddress());
        tvCity.setText(selectedResidence.getCity());
        tvCountry.setText(selectedResidence.getCountry());
        tvHostName.setText(host.getFirstName() +" " + host.getLastName());
        tvHostName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

        setCalendar();

        tvPrice.setText(Double.toString(selectedResidence.getMinPrice()));
        rating.setRating((float)selectedResidence.getAverageRating());

        ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(ResidenceActivity.this, MessageActivity.class);

                Bundle bmessage = new Bundle();
                bmessage.putBoolean("type", user);
                bmessage.putInt("currentUserId", loggedinUser.getId());
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
            public void onClick(View v)
            {
                Intent reviewsIntent = new Intent(ResidenceActivity.this, ReviewsActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                buser.putInt("residenceId", residenceId);
                reviewsIntent.putExtras(buser);
                try {
                    startActivity(reviewsIntent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                guests = etGuests.getText().toString();
                guestsInt = Integer.parseInt(guests);
                //gets selected dates from user input
                if(selectedDates[0] != null && selectedDates[1] != null)
                {
                    if (selectedDates[0].before(selectedDates[1])) {
                        selectedStartDate = selectedDates[0];
                        selectedEndDate = selectedDates[1];
                    }
                    else
                    {
                        selectedStartDate = selectedDates[1];
                        selectedEndDate = selectedDates[0];
                    }
                }
                //in case user has not specified the period for booking or the number of guests, reservation cannot be performed
                if(selectedStartDate == null || selectedEndDate == null || guests == null)
                {
                    Toast.makeText(c, "Please fill in the dates and the number of guests", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    //user makes a reservation and goes back to home activity
                    boolean success = PostResult();
                    if (success)
                    {
                        Intent homeIntent = new Intent(ResidenceActivity.this, HomeActivity.class);
                        user = true;
                        Bundle buser = new Bundle();
                        buser.putBoolean("type", user);
                        homeIntent.putExtras(buser);
                        startActivity(homeIntent);
                        finish();
                    } else {
                        Toast.makeText(c, "Booking failed, please try again!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    public boolean PostResult()
    {
        Reservations reservationParameters = new Reservations(loggedinUser, selectedResidence, selectedStartDate, selectedEndDate, guestsInt);

        RestAPI restAPI = RestClient.getClient().create(RestAPI.class);
        Call<Reservations> call = restAPI.postReservation(reservationParameters);

        call.enqueue(new Callback<Reservations>() {
            @Override
            public void onResponse(Call<Reservations> call, Response<Reservations> response) {
                if(response.isSuccessful())
                {
                    success=true;
                }
            }

            @Override
            public void onFailure(Call<Reservations> call, Throwable t)
            {
                success=false;
            }
        });


        return success;
    }

    public void setCalendar ()
    {
        caldroidFragment = new CaldroidFragment();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        //get available dates from host
        Date startDate = selectedResidence.getAvailableDateStart();
        Date endDate = selectedResidence.getAvailableDateEnd();

        ColorDrawable blue = new ColorDrawable(Color.BLUE);
        caldroidFragment.setMinDate(startDate);
        caldroidFragment.setMaxDate(endDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        NumGuestsPerDay = new HashMap<>();
        Date current = new Date();
        // initialize guest sum
        while (!current.after(endDate))
        {
            NumGuestsPerDay.put(current, 0);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            current = calendar.getTime();
        }
        //get all reservations for the selected residence
        ArrayList<Reservations> allReservationsByResidence = retrofitCalls.getReservationsByResidenceId(residenceId);

        //get the max guests for this residence
        maxGuests = selectedResidence.getGuests();

        reservedDates = new ArrayList<>();
        Date dateStart;
        Date dateEnd;
        int guestsFromDatabase;
        for(int i=0;i<allReservationsByResidence.size();i++)
        {
            //get for each reservation the start and the end date, and the number of guests
            dateStart = allReservationsByResidence.get(i).getStartDate();
            dateEnd = allReservationsByResidence.get(i).getEndDate();
            guestsFromDatabase = allReservationsByResidence.get(i).getGuests();

            Date currentDate = dateStart;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);

            while (!currentDate.after(dateEnd))
            {
                int sum = NumGuestsPerDay.get(currentDate)+guestsFromDatabase;
                NumGuestsPerDay.put(currentDate, sum);
                cal.add(Calendar.DAY_OF_MONTH,1);
                currentDate = cal.getTime();
            }
        }
        //disable all dates that are already fully booked
        for(Date date : NumGuestsPerDay.keySet())
        {
            if(NumGuestsPerDay.get(date)>= maxGuests)
            {
                reservedDates.add(date);
            }
        }
        caldroidFragment.setDisableDates(reservedDates);
        datesDisabled_byGuestCount = new ArrayList<>();

        //this field is visible only if user has not already provided number of guests
        if(guests != null)
        {
            etGuests.setText(guests);
            filterDates();
        }
        etGuests.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }

                    public void afterTextChanged(Editable s)
                    {
                        filterDates();
                    }
                }
        );

        selectedDates = new Date[2];
        selectedDates[0] = null;
        selectedDates[1] = null;

        if(date_start != null)
        {
            selectedDates[0] = Utils.ConvertStringToDate(date_start, Utils.DATABASE_DATE_FORMAT);
            caldroidFragment.setBackgroundDrawableForDate(blue, selectedDates[0]);
        }
        if(date_end !=null)
        {
            selectedDates[1] = Utils.ConvertStringToDate(date_end, Utils.DATABASE_DATE_FORMAT);
            caldroidFragment.setBackgroundDrawableForDate(blue, selectedDates[1]);
        }
        final CaldroidListener listener = new CaldroidListener()
        {
            View view;
            void reset(int idx)
            {
                selectedDates[idx] = null;
                view.setBackgroundColor(Color.WHITE);
            }
            @Override
            public void onSelectDate(Date date, View view)
            {
                if(guests == null)
                {
                    Toast.makeText(c, "Please select number of guests first", Toast.LENGTH_SHORT).show();
                    return;
                }
                this.view = view;
                int freeIdx= -1;
                for(int i=0;i<2;++i) {
                    if (selectedDates[i] != null) {
                        if (selectedDates[i].equals(date)) {
                            reset(i);
                            return;
                        }
                    }
                    else freeIdx = i;
                }

                if(freeIdx < 0)
                {
                    // no space left
                    Toast.makeText(c, "You have already selected two days", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedDates[freeIdx] = date;
                view.setBackgroundColor(Color.CYAN);

            }
        };
        caldroidFragment.setCaldroidListener(listener);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

    public void filterDates()
    {
        caldroidFragment.clearDisableDates();
        caldroidFragment.setDisableDates(reservedDates);
        guests = etGuests.getText().toString();
        int numberOfGuestsGiven = 0;
        try
        {
            numberOfGuestsGiven = Integer.parseInt(guests);
        }
        catch (Exception e){
            e.printStackTrace();
            caldroidFragment.clearDisableDates();
            caldroidFragment.setDisableDates(reservedDates);
            return;
        }

        for(Date date : NumGuestsPerDay.keySet())
        {
            int sum = NumGuestsPerDay.get(date)+ numberOfGuestsGiven;
            if(sum > maxGuests)
            {
                datesDisabled_byGuestCount.add(date);
            }
        }
        caldroidFragment.setDisableDates(datesDisabled_byGuestCount);
        caldroidFragment.refreshView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        LatLng address = RestCalls.findCoodrinates(selectedResidence.getAddress(), selectedResidence.getCity(), selectedResidence.getCountry());
        mMap.addMarker(new MarkerOptions().position(address).title("Residence Address"));
        CameraUpdate center = CameraUpdateFactory.newLatLng(address);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
    }
}