package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import util.RestCalls;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.FORMAT_DATE_YMD;
import static util.Utils.convertTimestampToDate;

public class ResidenceActivity extends FragmentActivity implements OnMapReadyCallback, AppCompatCallback
{
    Boolean user;
    String username, date_start, date_end, guests, token;

    int residenceId, maxGuests, guestsInt;
    Context c;

    TextView tvTitle, tvType, tvAddress, tvCity, tvCountry, tvHostName, tvAbout, tvAmenities, tvCancellationPolicy, tvHostAbout, tvRules, tvPrice;
    Button bBook;
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
    Toolbar toolbar;
    private AppCompatDelegate delegate;

    RetrofitCalls retrofitCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        c = this;
        Session sessionData = Utils.getSessionData(ResidenceActivity.this);
        token = sessionData.getToken();
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
        //this should be created only in onCreate Method!
        //this activity extends FragmentActivity, but in order to set up the toolbar we should extend AppCompactActivity
        //Delegate is used in order to overcome this problem, since only one class can be extended
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_residence);

        Bundle buser        = getIntent().getExtras();
        user                = buser.getBoolean("type");
        residenceId         = buser.getInt("residenceId");
        date_start = "";
        date_end = "";
        guests = "";
        if (buser.containsKey("startDate")) date_start = buser.getString("startDate");
        if (buser.containsKey("endDate")) date_end = buser.getString("endDate");
        if (buser.containsKey("guests")) guests = buser.getString("guests");

        retrofitCalls = new RetrofitCalls();
        loggedinUser = retrofitCalls.getUserbyUsername(token, sessionData.getUsername()).get(0);
        selectedResidence   = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));

        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("View Residence");
        toolbar.setSubtitle(selectedResidence.getTitle());
        delegate.setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (delegate.getSupportActionBar() != null){
            delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: check when user enters the activity from history reviews
                Utils.manageBackButton(ResidenceActivity.this, (user)?HomeActivity.class:HostActivity.class, user);
            }
        });

        setUpResidenceView();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapResidence);
        mapFragment.getMapAsync(this);
    }

    public void setUpResidenceView () {
        host = selectedResidence.getHostId();

        ImageView resPhoto = (ImageView) findViewById(R.id.ivResidencePhotos);
        Utils.loadResidenceImage(this, resPhoto, selectedResidence.getPhotos());

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
        bBook                   = (Button)findViewById(R.id.btnReservation);

        etGuests                = (EditText)findViewById(R.id.etGuests);
        etGuests.setSelected(false);

        tvTitle.setText(selectedResidence.getTitle());
        tvType.setText(selectedResidence.getType());
        tvAddress.setText(selectedResidence.getAddress());
        tvCity.setText(selectedResidence.getCity());
        tvCountry.setText(selectedResidence.getCountry());
        tvHostName.setText(host.getFirstName() +" " + host.getLastName());
        tvHostName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent profileIntent;
                if (user){
                    profileIntent = new Intent(ResidenceActivity.this, ViewHostProfileActivity.class);
                }
                else{
                    profileIntent = new Intent(ResidenceActivity.this, ProfileActivity.class);
                }
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                buser.putInt("host", host.getId());
                buser.putInt("residenceId", residenceId);
                profileIntent.putExtras(buser);
                try{
                    startActivity(profileIntent);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                    Log.e("",e.getMessage());
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

        if (user) {
            setCalendar();
        } else {
            bBook.setVisibility(View.GONE);
        }
        setBookResidence();
    }

    public void setBookResidence()
    {
        bBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guests = etGuests.getText().toString();
                guestsInt = Integer.parseInt(guests);

                /** Gets selected dates from user input **/
                if(selectedDates[0] != null && selectedDates[1] != null) {
                    if (selectedDates[0].before(selectedDates[1])) {
                        selectedStartDate = selectedDates[0];
                        selectedEndDate = selectedDates[1];
                    } else {
                        selectedStartDate = selectedDates[1];
                        selectedEndDate = selectedDates[0];
                    }
                }

                /** In case user has not specified the period for booking or the number of guests, reservation cannot be performed **/
                if(selectedStartDate == null || selectedEndDate == null || guests == null) {
                    Toast.makeText(c, "Please fill in the dates and the number of guests", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new AlertDialog.Builder(ResidenceActivity.this)
                        .setTitle("Booking Confirmation")
                        .setMessage("Please confirm the details below:"
                            + "\n\n" + selectedResidence.getTitle()
                            + "\n\n" + guestsInt + (guestsInt == 1 ? " guest" : " guests")
                            + "\n" + "Arrival Date: " + selectedStartDate
                            + "\n" + "Departure Date: " + selectedEndDate
                            + "\n\n" + "Click OK to continue, or CANCEL to go back to the residence"
                        )
                        .setIcon(android.R.drawable.ic_secure)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                /** User makes a reservation and goes back to home activity **/
                                long date_start = Utils.convertDateToMillisSec(selectedStartDate, FORMAT_DATE_YMD);
                                long date_end = Utils.convertDateToMillisSec(selectedEndDate, FORMAT_DATE_YMD);

                                Reservations reservationParameters = new Reservations(loggedinUser, selectedResidence, date_start, date_end, guestsInt);
                                RetrofitCalls retrofitCalls = new RetrofitCalls();
                                token = retrofitCalls.postReservation(token, reservationParameters);

                                if (!token.isEmpty()) {
                                    Intent reservationsIntent = new Intent(ResidenceActivity.this, HistoryReservationsActivity.class);
                                    user = true;
                                    Bundle buser = new Bundle();
                                    buser.putBoolean("type", user);
                                    reservationsIntent.putExtras(buser);
                                    startActivity(reservationsIntent);
                                    finish();
                                } else {
                                    Toast.makeText(c, "Booking failed, your session is terminated, please log in again!", Toast.LENGTH_SHORT).show();
                                    Utils.logout(ResidenceActivity.this);
                                    finish();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
    }

    public void setCalendar () {
        caldroidFragment = new CaldroidFragment();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        /** Get available dates from host **/
        long available_date_start = selectedResidence.getAvailableDateStart();
        long available_date_end = selectedResidence.getAvailableDateEnd();

        Date startDate = convertTimestampToDate(available_date_start, FORMAT_DATE_YMD);
        Date endDate = convertTimestampToDate(available_date_end, FORMAT_DATE_YMD);

        ColorDrawable blue = new ColorDrawable(Color.BLUE);
        caldroidFragment.setMinDate(startDate);
        caldroidFragment.setMaxDate(endDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        NumGuestsPerDay = new HashMap<>();
        Date current = new Date();

        /** Initialize guest sum **/
        while (!current.after(endDate)) {
            NumGuestsPerDay.put(current, 0);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            current = calendar.getTime();
        }

        /** Get all reservations for the selected residence **/
        ArrayList<Reservations> allReservationsByResidence = retrofitCalls.getReservationsByResidenceId(token, residenceId);

        //get the max guests for this residence
        maxGuests = selectedResidence.getGuests();

        reservedDates = new ArrayList<>();
        Date dateStart, dateEnd;
        int guestsFromDatabase;
        for(int i=0;i<allReservationsByResidence.size();i++) {
            /** Gt for each reservation the start and the end date, and the number of guests **/
            dateStart = Utils.convertTimestampToDate(allReservationsByResidence.get(i).getStartDate(), FORMAT_DATE_YMD);
            dateEnd = Utils.convertTimestampToDate(allReservationsByResidence.get(i).getEndDate(), FORMAT_DATE_YMD);

            guestsFromDatabase = allReservationsByResidence.get(i).getGuests();

            Date currentDate = dateStart;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);

            while (!currentDate.after(dateEnd)) {
                int sum = NumGuestsPerDay.get(currentDate)+guestsFromDatabase;
                NumGuestsPerDay.put(currentDate, sum);
                cal.add(Calendar.DAY_OF_MONTH,1);
                currentDate = cal.getTime();
            }
        }

        /** Disable all dates that are already fully booked **/
        for(Date date : NumGuestsPerDay.keySet()) {
            if(NumGuestsPerDay.get(date)>= maxGuests) {
                reservedDates.add(date);
            }
        }
        caldroidFragment.setDisableDates(reservedDates);
        datesDisabled_byGuestCount = new ArrayList<>();

        /** This field is visible only if user has not already provided number of guests **/
        if(guests != null) {
            etGuests.setText(guests);
            filterDates();
        }
        etGuests.addTextChangedListener(
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count){}
                public void afterTextChanged(Editable s) {
                    filterDates();
                }
            }
        );

        selectedDates = new Date[2];
        selectedDates[0] = null;
        selectedDates[1] = null;

        if(date_start != null) {
            selectedDates[0] = Utils.ConvertStringToDate(date_start, FORMAT_DATE_YMD);
            caldroidFragment.setBackgroundDrawableForDate(blue, selectedDates[0]);
        }
        if(date_end !=null) {
            selectedDates[1] = Utils.ConvertStringToDate(date_end, FORMAT_DATE_YMD);
            caldroidFragment.setBackgroundDrawableForDate(blue, selectedDates[1]);
        }
        final CaldroidListener listener = new CaldroidListener() {
            View view;
            void reset(int idx) {
                selectedDates[idx] = null;
                view.setBackgroundColor(Color.WHITE);
            }
            @Override
            public void onSelectDate(Date date, View view) {
                if(guests == null) {
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

                if(freeIdx < 0) {
                    /** No space left **/
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
    protected void onSaveInstanceState(Bundle outState) {
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
        try {
            numberOfGuestsGiven = Integer.parseInt(guests);
        }
        catch (Exception e) {
            Log.e("",e.getMessage());
            caldroidFragment.clearDisableDates();
            caldroidFragment.setDisableDates(reservedDates);
            return;
        }

        for(Date date : NumGuestsPerDay.keySet()) {
            int sum = NumGuestsPerDay.get(date)+ numberOfGuestsGiven;
            if(sum > maxGuests) {
                datesDisabled_byGuestCount.add(date);
            }
        }
        caldroidFragment.setDisableDates(datesDisabled_byGuestCount);
        caldroidFragment.refreshView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng address = RestCalls.findCoordinates(selectedResidence.getAddress(), selectedResidence.getCity(), selectedResidence.getCountry());
        mMap.addMarker(new MarkerOptions().position(address).title("Residence Address"));
        CameraUpdate center = CameraUpdateFactory.newLatLng(address);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_residence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle buser = new Bundle();
        buser.putBoolean("type", user);
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.reviews:
                if (item.getItemId() == R.id.reviews) {
                    Intent historyReviewsIntent = new Intent(ResidenceActivity.this, ReviewsActivity.class);
                    buser.putInt("residenceId", residenceId);
                    historyReviewsIntent.putExtras(buser);
                    startActivity(historyReviewsIntent);
                    finish();
                    break;
                }
            // action with ID action_settings was selected
                //TODO: invisible when user is navigating as host and this is his residence
            case R.id.contact:
                Intent contactIntent = new Intent(ResidenceActivity.this, MessageActivity.class);
                buser.putInt("currentUserId", loggedinUser.getId());
                buser.putInt("toUserId", host.getId());
                buser.putString("msgSubject", tvTitle.getText().toString());
                buser.putInt("residenceId", residenceId);
                contactIntent.putExtras(buser);
                startActivity(contactIntent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}