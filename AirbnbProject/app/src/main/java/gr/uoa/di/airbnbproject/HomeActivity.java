package gr.uoa.di.airbnbproject;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import util.ListAdapterResidences;
import util.RestCalls;
import util.Utils;

public class HomeActivity extends AppCompatActivity
{
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    ListAdapterResidences adapter;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";

    EditText field_city, field_guests;
    TextView startDate, endDate;
    ListView list;
    Button btnStartDatePicker, btnEndDatePicker, field_search;

    int[]residenceId;
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    String username, date_start, date_end;

    Boolean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);

//        currentUser = sharedPrefs.getString("loggedUser", "");
        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            startActivity(intent);
            return;
        } else {
            username = sharedPrefs.getString("currentLoggedInUser", "");
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_home);

        /**SEARCH VIEW EXPANDABLE START **/
        setupSearchView();

        /** RECOMMENDATIONS **/
        ArrayList<Residences> Recommendations = popularRecommendations();
        String[] title                  = new String[Recommendations.size()];
        String[] representativePhoto    = new String[Recommendations.size()];
        String[] city                   = new String[Recommendations.size()];
        Double[] price                  = new Double[Recommendations.size()];
        float[] rating                  = new float[Recommendations.size()];
        residenceId                     = new int[Recommendations.size()];

        for(int i=0; i<Recommendations.size();i++){
            title[i]                = Recommendations.get(i).getTitle();
            representativePhoto[i]  = Recommendations.get(i).getPhotos();
            city[i]                 = Recommendations.get(i).getCity();
            price[i]                = Recommendations.get(i).getMinPrice();
            rating[i]               = (float)Recommendations.get(i).getAverageRating();
            residenceId[i]          = Recommendations.get(i).getId();
        }
        adapter = new ListAdapterResidences(this, title, representativePhoto, city, price, rating);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showResidenceIntent = new Intent(HomeActivity.this, ResidenceActivity.class);
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
        Utils.manageFooter(HomeActivity.this, true);
    }

    public void setupSearchView() {
        final TextView searchlist = (TextView) findViewById(R.id.searchlist);
        searchlist.setVisibility(View.GONE);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    searchlist.setVisibility(View.GONE);
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                    searchlist.setVisibility(View.VISIBLE);
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                    searchlist.setVisibility(View.GONE);
                }
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
                            date_start = year + "-" + dayOfMonth + "-" + (monthOfYear + 1);
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
                            date_end = year + "-" + dayOfMonth + "-" + (monthOfYear + 1);
                        }
                    }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.show();
                }
            }
        });

        field_search = (Button) findViewById(R.id.field_search);
        field_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchintent = new Intent(HomeActivity.this, SearchResultsActivity.class);
                Bundle bsearch = new Bundle();
//                Integer userID = RestCalls.getUserId(username);

                bsearch.putString("username", username);
                bsearch.putString("city", field_city.getText().toString());
                bsearch.putString("guests", field_guests.getText().toString());
                bsearch.putString("startDate", date_start);
                bsearch.putString("endDate", date_end);
                bsearch.putBoolean("type", user);
                searchintent.putExtras(bsearch);

                startActivity(searchintent);
            }
        });
    }

    public ArrayList<Residences> popularRecommendations() {
        Integer userID = RestCalls.getUserId(username);

        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        ArrayList<Reviews> reviewsByResidence;
        int residenceId;

        Set<String> relevantCities = RestCalls.getSearchedCities(userID);

		/* if user has not searched anything yet, most popular residences will appear */
        if (relevantCities.size() == 0) {
            ArrayList<Reviews> reviews = RestCalls.getReviews();
            for (int i=0;i<reviews.size();i++) {
                reviewedResidences.add(reviews.get(i).getResidenceId());
            }
        }
        /* if user has already searched, we will show the most popular residences in the relevant cities */
        else {
            for (String city : relevantCities) {
                reviewedResidences = RestCalls.getResidenceByCity(city);
            }
        }

        /** check for duplicates **/
        Set<Residences> hs = new HashSet<>();
        hs.addAll(reviewedResidences);
        reviewedResidences.clear();
        reviewedResidences.addAll(hs);
        if(reviewedResidences.size() ==0) reviewedResidences = RestCalls.getAllResidences();

        /** get all relevant rooms and reviews **/
        for(int i=0; i < reviewedResidences.size(); i++){
            residenceId = reviewedResidences.get(i).getId();
            reviewsByResidence = RestCalls.getReviewsByResidenceId(residenceId);
            reviewedResidences.get(i).setReviewsCollection(reviewsByResidence);
        }
        //sort the results
        Collections.sort(reviewedResidences);
        return reviewedResidences;
    }

    @Override
    protected void onResume() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        user = true;
        super.onResume();
    }

    @Override
    protected void onRestart() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        if (!isUserLoggedIn) {
            Intent intent = new Intent(this, GreetingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        user = true;
        super.onRestart();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        super.onBackPressed();
//        return;
        moveTaskToBack(true);
    }

    //addPermission method, used below for Runtime Permissions. Checks if permission is already approved by the user
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    //method used for runtime permissions as well, message shown to user in order to approve permissions
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                {
                    // All Permissions Granted
                    popularRecommendations();
                } else {
                    // Permission Denied
                    Toast.makeText(HomeActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getPermissions(){
        //Runtime permissions
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            List<String> permissionsNeeded = new ArrayList<>();

            final List<String> permissionsList = new ArrayList<>();

            if (!addPermission(permissionsList, Manifest.permission.INTERNET))
                permissionsNeeded.add("Access Internet");
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
                permissionsNeeded.add("Access Network State");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Access External Storage");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write to External Storage");
            if(!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("Access Fine Location");

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
}