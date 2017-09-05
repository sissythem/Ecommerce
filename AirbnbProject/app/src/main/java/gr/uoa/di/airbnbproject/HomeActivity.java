package gr.uoa.di.airbnbproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;
import util.RecyclerAdapterResidences;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static android.text.TextUtils.isEmpty;
import static util.Utils.FORMAT_DATE_DM;
import static util.Utils.FORMAT_DATE_YMD;
import static util.Utils.getSessionData;
import static util.Utils.runWorker;
import static util.Utils.workerIsRunning;

public class HomeActivity extends AppCompatActivity
{
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    ArrayList<Residences> Recommendations;
    EditText field_city, field_guests;
    TextView startDate, endDate, searchbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    Button btnStartDatePicker, btnEndDatePicker, field_search, clear;
    ProgressBar progressBar;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    String username, date_start, date_end, token, city;
    int numGuests;
    Boolean user;
    Users loggedInUser;
    Context c;

    RecyclerView residencesRecyclerView;
    RecyclerView.LayoutManager residencesLayoutManager;
    RecyclerAdapterResidences residencesAdapter;

    boolean isShow = false;
    int scrollRange = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressBar = (ProgressBar)findViewById(R.id.loadingPanel);
        progressBar.setVisibility(View.GONE);

        /** Set up RecyclerView**/
        residencesRecyclerView = (RecyclerView) findViewById(R.id.recyclermain);
        residencesLayoutManager = new GridLayoutManager(this, 1);
        residencesRecyclerView.setLayoutManager(residencesLayoutManager);
        residencesRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = getSessionData(HomeActivity.this);

        username=sessionData.getUsername();
        user = true;
        token = sessionData.getToken();
        c=this;
        //check if user is logged in
        if (!sessionData.getUserLoggedInState()) {
            Utils.logout(this);
            return;
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        /** Check if token is expired **/
        if(Utils.isTokenExpired(token)){
            Toast.makeText(c, "Session is expired", Toast.LENGTH_SHORT).show();
            Utils.logout(this);
            finish();
            return;
        }

        getPermissions();
        resetActivity();
        /** Start Worker for Notifications **/

        if (!workerIsRunning(HomeActivity.this)) {
            runWorker(HomeActivity.this, true);
            new Worker().execute();
        }

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HomeActivity.this, true);
        /** RECOMMENDATIONS **/
        Recommendations = popularRecommendations();

        /**SEARCH VIEW EXPANDABLE START **/
        setupSearchView();

        /** RecyclerView for displaying the recommendations */
        try {
            if (Recommendations.size() > 0) {
                residencesAdapter = new RecyclerAdapterResidences(this, user, Recommendations, numGuests, date_start, date_end);
                residencesRecyclerView.setAdapter(residencesAdapter);
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    public void setupSearchView() {
        searchbar = (TextView) findViewById(R.id.searchbar);
        /** Hide search toolbar **/
        searchbar.setVisibility(View.GONE);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        /** Show toolbar onClick **/
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(true);
            }
        });
        /** Show toolbar on scrolling **/
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                openSearchBar(appBarLayout, verticalOffset);
            }
        });

        field_city = (EditText) findViewById(R.id.field_city);
        field_guests = (EditText) findViewById(R.id.field_guests);

        /**** Dates Selector ****/
        btnStartDatePicker = (Button)findViewById(R.id.btn_start_date);
        startDate = (TextView)findViewById(R.id.start_date);

        btnEndDatePicker = (Button)findViewById(R.id.btn_end_date);
        endDate = (TextView)findViewById(R.id.end_date);

        btnStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnStartDatePicker) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mStartYear = c.get(Calendar.YEAR);
                    mStartMonth = c.get(Calendar.MONTH);
                    mStartDay = c.get(Calendar.DAY_OF_MONTH);

                    date_start = "";
                    DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            date_start = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }, mStartYear, mStartMonth, mStartDay);
                    datePickerDialog.show();
                }
            }
        });

        btnEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnEndDatePicker) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mEndYear = c.get(Calendar.YEAR);
                    mEndMonth = c.get(Calendar.MONTH);
                    mEndDay = c.get(Calendar.DAY_OF_MONTH);

                    date_end = "";
                    DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            endDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            date_end = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.show();
                }
            }
        });

        field_search = (Button) findViewById(R.id.field_search);
        field_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /** Change recommendations when user presses the search button
                 * Hide the keyboard
                 * Hide the search toolbar **/
                progressBar.setVisibility(View.VISIBLE);
                appBarLayout.setExpanded(false);

                hideSoftKeyboard(HomeActivity.this, v);
                final String guests = field_guests.getText().toString();
                /** Check user input **/
                if(guests!=null && !guests.isEmpty())
                    numGuests = Integer.parseInt(guests);
                else
                    numGuests=0;
                city = field_city.getText().toString();

                if (numGuests <= 0) numGuests = 1;
                if (isEmpty(date_start) || date_start==null || !Utils.isThisDateValid(date_start, FORMAT_DATE_YMD)) date_start = Utils.getCurrentDate(FORMAT_DATE_YMD);
                if (isEmpty(date_end) || date_end==null || !Utils.isThisDateValid(date_end, FORMAT_DATE_YMD)) date_end = Utils.getDefaultEndDate(FORMAT_DATE_YMD);

                /** Change toolbar header based on user input **/
                String str_city = (!isEmpty(city)) ? Character.toUpperCase(city.charAt(0)) + city.substring(1) : "Anywhere";
                String str_startdate = Utils.formatDate(date_start, FORMAT_DATE_DM);
                String str_enddate = Utils.getDefaultEndDate(FORMAT_DATE_DM);
                String str_guests = guests + " " + ((numGuests > 1) ? "guests" : "guest");

                RetrofitCalls retrofitCalls = new RetrofitCalls();

                long start_timestamp = Utils.convertDateToMillisSec(date_start, FORMAT_DATE_YMD);
                long end_timestamp = Utils.convertDateToMillisSec(date_end, FORMAT_DATE_YMD);

                /** Update recommendations based on user input **/
                Recommendations = retrofitCalls.getRecommendations(token, username, city, start_timestamp, end_timestamp, numGuests);

                /** In case that no residence matches user's input, popular recommendations will appear again **/
                if(Recommendations.size() == 0) {
                    /** Show confirmation message to user in order to logout **/
                    new AlertDialog.Builder(HomeActivity.this)
                            .setMessage("No available residences for city " + city + " were found.").setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    reloadActivity(HomeActivity.this, null);
                                    return;
                                }}).show();

                } else {
                    for(int i=0;i<Recommendations.size(); i++){
                        ArrayList<Reviews>reviewsByResidence = retrofitCalls.getReviewsByResidenceId(token, Integer.toString(Recommendations.get(i).getId()));
                        /** Set up the Reviews Collection so as to show it in the rating bar */
                        if (reviewsByResidence != null && reviewsByResidence.size() != 0) {
                            Recommendations.get(i).setReviewsCollection(reviewsByResidence);
                        }
                    }
                    searchbar.setText(str_city + ", " + str_startdate + "-" + str_enddate + ", " + str_guests);

                    /** Set up the ArrayList<Residences with the new results and notify the adapter **/
                    residencesAdapter.setSearchList(Recommendations);
                    residencesAdapter.setGuests(numGuests);
                    residencesAdapter.setStartDate(date_start);
                    residencesAdapter.setEndDate(date_end);
                    residencesAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        /** User can clear all search fields and view again the recommendations based on all previous searches he has performed **/
        clear = (Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearInput();
                appBarLayout.setExpanded(false);
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void openSearchBar(AppBarLayout appBarLayout, int verticalOffset) {
        searchbar.setVisibility(View.GONE);

        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
            searchbar.setVisibility(View.GONE);
        }
        if (scrollRange + verticalOffset == 0) {
            collapsingToolbarLayout.setTitle("Title");
            isShow = true;
            searchbar.setVisibility(View.VISIBLE);
        } else if(isShow) {
            collapsingToolbarLayout.setTitle(" "); /** Attention: there should a space between double quote otherwise it wont work **/
            isShow = false;
            searchbar.setVisibility(View.GONE);
        }
    }

    public void clearInput(){
        field_guests.setText("");
        field_city.setText("");
        startDate.setText("");
        endDate.setText("");
        date_start="";
        date_end="";
}

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public ArrayList<Residences> popularRecommendations()
    {
        ArrayList<Users> Users;
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        /**Get the user as Users object **/
        Users = retrofitCalls.getUserbyUsername(token, username);
        //If user is not found or if something went wrong
        if(Users.isEmpty())
        {
            Toast.makeText(this,"Failed to get users from database.", Toast.LENGTH_LONG).show();
            return new ArrayList<>();
        }
        loggedInUser = Users.get(0);

        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        ArrayList<Residences> residences = new ArrayList<>();
        ArrayList<Reviews> reviewsByResidence, reviews;
        int residenceId;

        ArrayList<Searches> searchedCities;
        /** Get all searches for cities that user has performed */
        searchedCities = retrofitCalls.getSearchedCities(token, loggedInUser.getId().toString());
        Set<String> relevantCities = new HashSet<>();
        for(int i = 0;i<searchedCities.size();i++){
            /**add the cities to a HashSet in order to include each city once **/
            relevantCities.add(searchedCities.get(i).getCity());
        }
        /** If user has already searched, we will show the most popular residences in the relevant cities **/
        if (relevantCities.size() != 0) {
            for (String city : relevantCities) {
                residences.addAll(retrofitCalls.getResidencesByCity(token, city));
            }
            if (residences.size() != 0) {
                /** Set up the reviews collection in order to sort the residences */
                /** In class Residences: getAverageRating computes the rating based on the reviews collection */
                for (int i = 0; i < residences.size(); i++) {
                    residenceId = residences.get(i).getId();
                    reviewsByResidence = retrofitCalls.getReviewsByResidenceId(token, Integer.toString(residenceId));
                    /** Include only residences with reviews */
                    if (reviewsByResidence != null && reviewsByResidence.size() != 0) {
                        residences.get(i).setReviewsCollection(reviewsByResidence);
                        reviewedResidences.add(residences.get(i));
                    }
                }
            }
        }
        /** 1)If there are not relevant residences for user's searches, he will see the most popular recommendations **/
        /** 2)If there are residences in the cities searched, but not reviewed **/
        /** 3)If user has not searched anything yet, most popular residences will appear **/
        if(reviewedResidences.size() == 0 || relevantCities.size() == 0 ||reviewedResidences == null || relevantCities ==null)
        {
            reviews = retrofitCalls.getAllReviews(token);
            for (int i=0;i<reviews.size();i++) {
                reviewedResidences.add(reviews.get(i).getResidenceId());
            }
            for (int i = 0; i < reviewedResidences.size(); i++) {
                residenceId = reviewedResidences.get(i).getId();
                reviewsByResidence = retrofitCalls.getReviewsByResidenceId(token, Integer.toString(residenceId));
                reviewedResidences.get(i).setReviewsCollection(reviewsByResidence);
            }
        }

        /** check for duplicates **/
        Set<Residences> hs = new HashSet<>();
        hs.addAll(reviewedResidences);
        reviewedResidences.clear();
        reviewedResidences.addAll(hs);

        /** exclude all residences that are uploaded by the user who is logged in **/
        for (int i=0; i < reviewedResidences.size(); i++) {
            if (reviewedResidences.get(i).getHostId().getId().equals(loggedInUser.getId())) {
                reviewedResidences.remove(reviewedResidences.get(i));
            }
        }

        /** In case that no residence has reviews we just present a list of the residences */
        if(reviewedResidences.size() ==0) {
            reviewedResidences = retrofitCalls.getAllResidences(token);
        }

        /** Sort the results **/
        Collections.sort(reviewedResidences);
        return reviewedResidences;
    }

    @Override
    protected void onRestart() {
        resetActivity();
        user = true;
        //adapter.notifyDataSetChanged();
        super.onRestart();
    }

    /** When the app is minimized and then reused, user can continue from where he left the app */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void resetActivity() {
        Session sessionData = Utils.getSessionData(HomeActivity.this);
        if (!sessionData.getUserLoggedInState()) {
            Intent intent = new Intent(this, GreetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    //addPermission method, used below for Runtime Permissions. Checks if permission is already approved by the user
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission)
    {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    //method used for runtime permissions as well, message shown to user in order to approve permissions
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //Below method is used when multiple permission are asked, in this case was not necessary to use this method
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(android.Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for permissions given
                if (perms.get(android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                {
                    // All Permissions Granted

                } else
                {
                    // Permission Denied
                    Toast.makeText(HomeActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                    Utils.logout(HomeActivity.this);
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getPermissions()
    {
        //Runtime permissions
        if (Build.VERSION.SDK_INT >= 23)
        {
            // Marshmallow+
            List<String> permissionsNeeded = new ArrayList<>();

            final List<String> permissionsList = new ArrayList<>();

            if (!addPermission(permissionsList, android.Manifest.permission.INTERNET))
                permissionsNeeded.add("Access Internet");
            if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_NETWORK_STATE))
                permissionsNeeded.add("Access Network State");
            if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Access External Storage");
            if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write to External Storage");
            if(!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("Access Fine Location");
            if(!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("Access Coarse Location");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        /** Reset worker as it wasnt running. Clear SharedPreferences state value **/
        runWorker(HomeActivity.this, false);
        Log.i("LogNotifications", System.currentTimeMillis() / 1000L + "  onDestroy()");

        super.onDestroy();
    }

    /** Worker options for notification messages **/
    private class Worker extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            Log.i("LogNotifications", "start do in background at " + System.currentTimeMillis());
            String data = null;

            try {
                Log.i("LogNotifications", "doInBackGround done at " + System.currentTimeMillis());
            } catch (Exception e) {}
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /** Set up Shared Preference in order to know when to initialize worker from the beginning **/
            if (workerIsRunning(HomeActivity.this)) {
                Utils.callAsynchronousTask(HomeActivity.this, token, loggedInUser.getId());
                Log.i("LogNotifications", System.currentTimeMillis() / 1000L + " post execute \n" + result);
            }
        }
    }
}