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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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

import fromRESTful.Images;
import fromRESTful.Reservations;
import fromRESTful.Residences;
import fromRESTful.Users;
import util.MyTextSlider;
import util.RestCalls;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static gr.uoa.di.airbnbproject.R.id.calendar;
import static gr.uoa.di.airbnbproject.R.id.reservations;
import static gr.uoa.di.airbnbproject.R.id.reviews;
import static util.RestClient.BASE_URL;
import static util.Utils.FORMAT_DATETIME_DMY_HMS;
import static util.Utils.FORMAT_DATE_DMY;
import static util.Utils.FORMAT_DATE_YMD;
import static util.Utils.convertTimestampToDate;
import static util.Utils.goToActivity;

public class ResidenceActivity extends FragmentActivity implements OnMapReadyCallback, AppCompatCallback, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener
{
    Bundle buser;
    Boolean user;
    String selected_date_start_str, selected_date_end_str, guests, token;

    int residenceId, maxGuests, guestsInt;
    Context c;

    TextView tvTitle, tvDetails, tvHostName, tvAbout, tvHostAbout, tvPrice;
    Button bBook;
    RatingBar rating;
    ScrollView mScrollView;
    FrameLayout mWrapperFL;
    EditText etGuests;
    GoogleMap mMap;
    ImageView profilePic;

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
    private SliderLayout mPhotosSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        c = this;
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = Utils.getSessionData(ResidenceActivity.this);
        token = sessionData.getToken();
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
        /**
         * This should be created only in onCreate Method!
         * This activity extends FragmentActivity, but in order to set up the toolbar we should extend AppCompactActivity
         * Delegate is used in order to overcome this problem, since only one class can be extended
         **/
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_residence);

        buser               = getIntent().getExtras();
        user                = buser.getBoolean("type");
        residenceId         = buser.getInt("residenceId");

        if (buser.containsKey("startDate")) selected_date_start_str = buser.getString("startDate");
        if (buser.containsKey("endDate")) selected_date_end_str = buser.getString("endDate");
        if (buser.containsKey("guests")) guests = buser.getString("guests");

        retrofitCalls = new RetrofitCalls();
        //Get the user and the residence
        loggedinUser = retrofitCalls.getUserbyUsername(token, sessionData.getUsername()).get(0);
        selectedResidence   = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));

        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("View Residence");
        toolbar.setSubtitle(selectedResidence.getTitle());
        delegate.setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (delegate.getSupportActionBar() != null)
        {
            delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        /** Handle the back button of the upper toolbar **/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackAction();
            }
        });

        mPhotosSlider = (SliderLayout)findViewById(R.id.residencesslider);

        setUpSlider();
        setUpResidenceView();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapResidence);
        mapFragment.getMapAsync(this);
    }

    public void setUpSlider() {
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Images> residencePhotos = retrofitCalls.getResidencePhotos(token, residenceId);
        if(residencePhotos.size() == 0)
        {
            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout);
            relativeLayout.setVisibility(View.GONE);
            try {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)frameLayout.getLayoutParams();
                params.setMargins(0, 50, 0, 0);
                frameLayout.setLayoutParams(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(Images residenceImage : residencePhotos)
        {
            MyTextSlider textSliderView = new MyTextSlider(this);
            /** Initialize a SliderLayout **/
            textSliderView.image(BASE_URL + "images/img/" + residenceImage.getName())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this)
                    .empty(R.drawable.ic_upload_image);

            /** Add your extra information **/
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",residenceImage.getName());
            mPhotosSlider.addSlider(textSliderView);
        }
        mPhotosSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mPhotosSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mPhotosSlider.setCustomAnimation(new DescriptionAnimation());
        mPhotosSlider.setDuration(4000);
        mPhotosSlider.addOnPageChangeListener(this);
    }

    public void setUpResidenceView ()
    {
        /** Present all the info for this residence **/
        host = selectedResidence.getHostId();

        /** Show the images of this residence **/
        profilePic = (ImageView)findViewById(R.id.ivHostPic);
        Utils.loadProfileImage(this, profilePic, host.getPhoto());

        tvTitle                 = (TextView)findViewById(R.id.tvTitle);
        tvDetails               = (TextView)findViewById(R.id.tvDetails);
        tvHostName              = (TextView)findViewById(R.id.tvHostName);
        tvHostAbout             = (TextView)findViewById(R.id.tvHostAbout);
        tvAbout                 = (TextView)findViewById(R.id.tvAboutText);
        tvPrice                 = (TextView)findViewById(R.id.price);
        rating                  = (RatingBar)findViewById(R.id.rating);
        bBook                   = (Button)findViewById(R.id.btnReservation);
        mScrollView             = (ScrollView)findViewById(R.id.scrollView);
        mWrapperFL              = (FrameLayout)findViewById(R.id.frameLayout);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());
        etGuests                = (EditText)findViewById(R.id.etGuests);
        etGuests.setSelected(false);

        tvTitle.setText(selectedResidence.getTitle());
        tvDetails.setText(selectedResidence.getType() + " \n"+ selectedResidence.getAddress()+ " \n"+ selectedResidence.getCity() +", " + selectedResidence.getCountry());
        tvHostName.setText(host.getFirstName() +" " + host.getLastName());
        tvHostAbout.setText(host.getAbout());
        tvAbout.setText(selectedResidence.getAbout() + "\n\n" + "What we will provide you:".toUpperCase() + "\n\n" + selectedResidence.getAmenities() + "\n\n" +
                "Our Cancelation Policy:".toUpperCase() + "\n\n" + selectedResidence.getCancellationPolicy() + "\n\n" + "Guest requirements:".toUpperCase()
                + "\n\n" + selectedResidence.getRules());
        tvPrice.setText(Double.toString(selectedResidence.getMinPrice()));
        rating.setRating((float)selectedResidence.getAverageRating());

        if(!user)
        {
            bBook.setVisibility(View.GONE);
            etGuests.setVisibility(View.GONE);
        }
        setCalendar();
        setBookResidence();
    }

    public void setBookResidence()
    {
        /** User can make a reservation **/
        bBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Gets the number of guests **/
                guests = etGuests.getText().toString();
                guestsInt = Integer.parseInt(guests);

                /** Gets selected dates from user input **/
                if(selectedDates[0] != null && selectedDates[1] != null) {
                    //find the start and the end date
                    if (selectedDates[0].before(selectedDates[1])) {
                        selectedStartDate = selectedDates[0];
                        selectedEndDate = selectedDates[1];
                    } else {
                        selectedStartDate = selectedDates[1];
                        selectedEndDate = selectedDates[0];
                    }
                    /** Check the availability of the days between the selected by the user start and end dates **/
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(selectedStartDate);
                    Date curr = selectedStartDate;
                    boolean inBetweenIsValid = true;
                    while (!curr.after(selectedEndDate)) {
                        inBetweenIsValid = !(reservedDates.contains(curr) || datesDisabled_byGuestCount.contains(curr));
                        if (!inBetweenIsValid) break;
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        curr = cal.getTime();
                    }
                    /** If user has chosen a valid period the dates now appear selected**/
                    if (!inBetweenIsValid) {
                        /** If not user has to choose again **/
                        Toast.makeText(c, "You have chosen a period with fully booked dates, please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                /** In case user has not specified the period for booking or the number of guests, reservation cannot be performed **/
                if(selectedStartDate == null || selectedEndDate == null || guests == null) {
                    Toast.makeText(c, "Please fill in the dates and the number of guests", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    long diff = selectedEndDate.getTime() - selectedStartDate.getTime();
                    int days = (int) (diff / (1000*60*60*24));
                    double totalAmount = selectedResidence.getMinPrice() + (selectedResidence.getAdditionalCostPerPerson()*guestsInt*days);
                    new AlertDialog.Builder(ResidenceActivity.this)
                        .setTitle("Booking Confirmation")
                        .setMessage("Please confirm the details below:"
                            + "\n\n" + selectedResidence.getTitle()
                            + "\n\n" + guestsInt + (guestsInt == 1 ? " guest" : " guests")
                            + "\n" + "Arrival Date: " + Utils.ConvertDateToString(selectedStartDate, FORMAT_DATE_DMY)
                                + "\n" + "Departure Date: " + Utils.ConvertDateToString(selectedEndDate, FORMAT_DATE_DMY)
                                + "\n" + "Total Amount: " + totalAmount
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

                                if (!token.isEmpty())
                                {
                                    Intent reservationsIntent = new Intent(ResidenceActivity.this, HomeActivity.class);
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

    public void setCalendar ()
    {
        /** Get available dates from host **/
        long available_date_start_millis = selectedResidence.getAvailableDateStart();
        long available_date_end_millis = selectedResidence.getAvailableDateEnd();
        reservedDates = new ArrayList<>();

        Date startDateHost = convertTimestampToDate(available_date_start_millis, FORMAT_DATE_YMD);
        Date endDateHost = convertTimestampToDate(available_date_end_millis, FORMAT_DATE_YMD);

        /** Something went wrong and did not get the available period for this residence **/
        if(startDateHost == null || endDateHost == null)
        {
            Toast.makeText(c, "There are no available dates", Toast.LENGTH_SHORT).show();
            TextView tvNoAvailability = new TextView(c);
            tvNoAvailability.setText("There are no available dates");
            return;
        }
        /** Use of caldroid in order to show the availability and disable all dates fully booked **/
        caldroidFragment = new CaldroidFragment();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(calendar, caldroidFragment);
        t.commit();

        /** Calendar will show only the period set by host as available **/
        caldroidFragment.setMinDate(startDateHost);
        caldroidFragment.setMaxDate(endDateHost);

        Calendar calendar = Calendar.getInstance();
        /** Calendar starts from the start date of the available period **/
        calendar.setTime(startDateHost);
        /** Count of guests for each date of the available period. Used to find all fully booked dates **/
        NumGuestsPerDay = new HashMap<>();
        Date current = calendar.getTime();

        /** Initialize guest sum **/
        while (!current.after(endDateHost)) {
            NumGuestsPerDay.put(current, 0);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            current = calendar.getTime();
        }

        /** Get all reservations for the selected residence **/
        ArrayList<Reservations> allReservationsByResidence = retrofitCalls.getReservationsByResidenceId(token, residenceId);

        /** Get the max guests for this residence **/
        maxGuests = selectedResidence.getGuests();
        Date dateStart, dateEnd;
        int guestsFromDatabase;
        for(int i=0;i<allReservationsByResidence.size();i++) {
            /** Get for each reservation the start and the end date, and the number of guests **/
            dateStart = Utils.convertTimestampToDate(allReservationsByResidence.get(i).getStartDate(), FORMAT_DATE_YMD);
            dateEnd = Utils.convertTimestampToDate(allReservationsByResidence.get(i).getEndDate(), FORMAT_DATE_YMD);
            guestsFromDatabase = allReservationsByResidence.get(i).getGuests();

            Date currentDate = dateStart;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateStart);
            /** Count the number of guests per day **/
            while (!currentDate.after(dateEnd)) {
                int sum = 0;
                try {
                    sum = NumGuestsPerDay.get(currentDate)+guestsFromDatabase;
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            /** Disable all dates in the past **/
            if(date.before(Utils.ConvertStringToDate(Utils.getCurrentDate(FORMAT_DATE_DMY), FORMAT_DATE_DMY))){
                reservedDates.add(date);
            }
        }
        caldroidFragment.setDisableDates(reservedDates);
        datesDisabled_byGuestCount = new ArrayList<>();

        /** This field is completed if user has already provided number of guests **/
        if(guests != null && !guests.equals("0")) {
            etGuests.setText(guests);
            filterDates();
        }
        etGuests.addTextChangedListener(
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count){}
                /** Based on the number of guests the available dates are examined again **/
                /** We exclude the remaining dates that cannot accommodate the selected number of guests **/
                public void afterTextChanged(Editable s) {
                    filterDates();
                }
            }
        );

        /** Use of an array so as to store the selected dates on the calendar **/
        selectedDates = new Date[2];
        selectedDates[0] = null;
        selectedDates[1] = null;

        /** If user has already selected dates from the search field in the home or search activity, he can see his selection on the calendar **/
        if(selected_date_start_str != null && selected_date_end_str!=null ) {
            Date selected_date_start = Utils.ConvertStringToDate(selected_date_start_str, FORMAT_DATETIME_DMY_HMS);
            Date selected_date_end = Utils.ConvertStringToDate(selected_date_end_str, FORMAT_DATETIME_DMY_HMS);
            /** Check if the selected dates are between the available period of the host and if the two dates are fully booked **/
            boolean startEndIsReserved = reservedDates.contains(selected_date_start) || datesDisabled_byGuestCount.contains(selected_date_start) ||
                    reservedDates.contains(selected_date_end) || datesDisabled_byGuestCount.contains(selected_date_end);
            boolean startEndIsOutsideHostRange = (selected_date_start.before(startDateHost) || selected_date_start.after(endDateHost)) ||
                    (selected_date_end.before(startDateHost) || selected_date_end.after(endDateHost));

            /** check in-between dates **/
            if (selected_date_end.before(selected_date_start)) {
                Date temp = selected_date_end;
                selected_date_end = selected_date_start;
                selected_date_start = temp;
            }
            /** Boolean variable isValid is used in order to check the hole period between the two selected dates **/
            Calendar cal = Calendar.getInstance();
            cal.setTime(selected_date_start);
            Date curr = selected_date_start;
            boolean inBetweenIsValid = true;
            while (!curr.after(selected_date_end)) {
                inBetweenIsValid = !(reservedDates.contains(curr) || datesDisabled_byGuestCount.contains(curr));
                if (!inBetweenIsValid) break;
                cal.add(Calendar.DAY_OF_MONTH, 1);
                curr = cal.getTime();
            }
            if (inBetweenIsValid && !startEndIsReserved && !startEndIsOutsideHostRange) {
                colorCalendarDates();
            }
        }

        final CaldroidListener listener = new CaldroidListener() {
            View view;
            //user can deselect a date
            void reset(int idx) {
                selectedDates[idx] = null;
                view.setBackgroundColor(Color.WHITE);
            }
            @Override
            public void onSelectDate(Date date, View view) {
                /** In order to select dates, user must first select a number of guests **/
                if(guests == null || guests.equals("0")) {
                    Toast.makeText(c, "Please select number of guests first", Toast.LENGTH_SHORT).show();
                    return;
                }
                this.view = view;
                int freeIdx= -1;
                for(int i=0;i<2;++i) {
                    if (selectedDates[i] != null) {
                        /** If user selects again the same date, means that he wants to deselect it **/
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

    public void colorCalendarDates()
    {
        /** Set the color of selected dates **/
        ColorDrawable blue = new ColorDrawable(Color.BLUE);

        selectedDates[0] = Utils.ConvertStringToDate(selected_date_start_str, FORMAT_DATE_YMD);
        caldroidFragment.setBackgroundDrawableForDate(blue, selectedDates[0]);

        selectedDates[1] = Utils.ConvertStringToDate(selected_date_end_str, FORMAT_DATE_YMD);
        caldroidFragment.setBackgroundDrawableForDate(blue, selectedDates[1]);
    }




    /** In case app is minimized, caldroid saves the state left **/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

    public void filterDates()
    {
        /** First clear all disabled dates and then set as disabled those from setCalendar method **/
        caldroidFragment.clearDisableDates();
        caldroidFragment.setDisableDates(reservedDates);
        /** Get the number of guests **/
        guests = etGuests.getText().toString();
        int numberOfGuestsGiven;
        try {
            numberOfGuestsGiven = Integer.parseInt(guests);
        }
        catch (Exception e) {
            Log.e("",e.getMessage());
            caldroidFragment.clearDisableDates();
            caldroidFragment.setDisableDates(reservedDates);
            return;
        }
        /** Check if user can perform a reservation to this period based on the number of guests given and the number of guests already booked this residence
         * during the same period **/
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
        /** Set the map in order to show the location of the residence **/
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
        /** Different popup menu based on the role user has chosen to navigate **/
        if (user) {
            inflater.inflate(R.menu.menu_residence_host, menu);
        } else {
            inflater.inflate(R.menu.menu_residence_user, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle buser = new Bundle();
        buser.putBoolean("type", user);
        switch (item.getItemId()) {
            /** User can view the reviews for this residence **/
            case reviews:
                if (item.getItemId() == reviews) {
                    Intent historyReviewsIntent = new Intent(ResidenceActivity.this, ReviewsActivity.class);
                    buser.putInt("residenceId", residenceId);
                    historyReviewsIntent.putExtras(buser);
                    startActivity(historyReviewsIntent);
                    finish();
                    break;
                }
                /** Host can see the reservations for this residence **/
            case reservations:
                buser.putInt("residenceId", residenceId);
                buser.putString("source", "residence");
                Utils.goToActivity(ResidenceActivity.this, HistoryReservationsActivity.class, buser);
                break;
            /** If user navigates as tenant can view the host's profile and contact him, otherwise he can see his own profile **/
            case R.id.contact:
                if (user) {
                    buser.putInt("host", host.getId());
                    buser.putInt("residenceId", residenceId);
                    goToActivity(ResidenceActivity.this, ViewHostProfileActivity.class, buser);
                } else {
                    buser.putString("source", "residence");
                    buser.putInt("residenceId", residenceId);
                    goToActivity(ResidenceActivity.this, ProfileActivity.class, buser);
                }
                break;
        }
        return true;
    }
    /** Handle the back action from phone **/
    @Override
    public void onBackPressed() {
        handleBackAction();
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

    /** Check previous activity in order to go back correctly **/
    public void handleBackAction()
    {
        if(!(buser.getString("source") == null) && buser.getString("source").equals("reviews"))
        {
            Utils.manageBackButton(ResidenceActivity.this, HistoryReviewsActivity.class, user);
        }
        else if(!(buser.getString("source") == null) && buser.getString("source").equals("reservations")){
            Utils.manageBackButton(ResidenceActivity.this, HistoryReservationsActivity.class, user);
        }
        else if(!(buser.getString("source") == null) && buser.getString("source").equals("hostprofile"))
        {
            Utils.manageBackButton(ResidenceActivity.this, ViewHostProfileActivity.class, user);
        }
        else
        {
            Utils.manageBackButton(ResidenceActivity.this, (user)?HomeActivity.class:HostActivity.class, user);
        }
    }

    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        private int mImageViewHeight;

        public ScrollPositionObserver() {
            mImageViewHeight = getResources().getDimensionPixelSize(R.dimen.res_images);
        }

        @Override
        public void onScrollChanged() {
            int scrollY = Math.min(Math.max(mScrollView.getScrollY(), 0), mImageViewHeight);

            /** Changing position of ImageView **/
//            mPhotosSlider.setTranslationY(scrollY / 2);

            /** Alpha you could set to ActionBar background **/
            float alpha = scrollY / (float) mImageViewHeight;
        }
    }

    /** Slider actions **/
    @Override
    protected void onStop() {
        /** To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed **/
        mPhotosSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}