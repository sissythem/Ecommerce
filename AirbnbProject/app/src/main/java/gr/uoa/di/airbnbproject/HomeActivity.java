package gr.uoa.di.airbnbproject;

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
import android.util.Log;
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

import fromRESTful.Conversations;
import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Searches;
import fromRESTful.Users;
import util.ListAdapterResidences;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;

public class HomeActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    ListAdapterResidences adapter;

    EditText field_city, field_guests;
    TextView startDate, endDate;
    ListView list;
    Button btnStartDatePicker, btnEndDatePicker, field_search;

    int[] residenceId;
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    String username, date_start, date_end, token;

    Boolean user;
    Users loggedInUser;
    Context c;

    String[] title, representativePhoto, city;
    Double[]price;
    float[] rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session sessionData = getSessionData(HomeActivity.this);
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

        /** Start Worker for Notifications **/
        new Worker().execute();

        //getPermissions();
        token = sessionData.getToken();
        username = sessionData.getUsername();
        setContentView(R.layout.activity_home);
        user = true;
        c = this;
        if (Utils.isTokenExpired(token)) {
            Toast.makeText(c, "Session is expired", Toast.LENGTH_SHORT).show();
            Utils.logout(this);
        }

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HomeActivity.this, true);

        /**SEARCH VIEW EXPANDABLE START **/
        setupSearchView();

        /** RECOMMENDATIONS **/
        ArrayList<Residences> Recommendations = popularRecommendations();
        title                  = new String[Recommendations.size()];
        representativePhoto    = new String[Recommendations.size()];
        city                   = new String[Recommendations.size()];
        price                  = new Double[Recommendations.size()];
        rating                 = new float[Recommendations.size()];
        residenceId            = new int[Recommendations.size()];

        for(int i=0; i<Recommendations.size();i++) {
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
        field_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchintent = new Intent(HomeActivity.this, SearchResultsActivity.class);
                Bundle bsearch = new Bundle();

                bsearch.putString("city", field_city.getText().toString());
                bsearch.putInt("guests", Integer.parseInt(field_guests.getText().toString()));
                bsearch.putString("startDate", date_start);
                bsearch.putString("endDate", date_end);
                bsearch.putBoolean("type", user);
                searchintent.putExtras(bsearch);
                startActivity(searchintent);
            }
        });
    }

    public ArrayList<Residences> popularRecommendations()
    {
        ArrayList<Users> Users;
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Users = retrofitCalls.getUserbyUsername(token, username);

        loggedInUser = Users.get(0);

        ArrayList<Residences> reviewedResidences = new ArrayList<>();
        ArrayList<Reviews> reviewsByResidence;
        int residenceId;

        ArrayList<Searches> searchedCities;
        searchedCities = retrofitCalls.getSearchedCities(token, loggedInUser.getId().toString());
        Set<String> relevantCities = new HashSet<>();
        for(int i = 0;i<searchedCities.size();i++){
            relevantCities.add(searchedCities.get(i).getCity());
        }

		/* if user has not searched anything yet, most popular residences will appear */
        if (relevantCities.size() == 0) {
            ArrayList<Reviews> reviews;
            reviews = retrofitCalls.getAllReviews(token);
            for (int i=0;i<reviews.size();i++) {
                reviewedResidences.add(reviews.get(i).getResidenceId());
            }
        }
        /* if user has already searched, we will show the most popular residences in the relevant cities */
        else {
            for (String city : relevantCities) {
                reviewedResidences = retrofitCalls.getResidencesByCity(token, city);
            }
        }

        /** check for duplicates **/
        Set<Residences> hs = new HashSet<>();
        hs.addAll(reviewedResidences);
        reviewedResidences.clear();
        reviewedResidences.addAll(hs);
        if(reviewedResidences.size() ==0) {
            reviewedResidences = retrofitCalls.getAllResidences(token);
        }

        /** get all relevant rooms and reviews **/
        for(int i=0; i < reviewedResidences.size(); i++){
            residenceId = reviewedResidences.get(i).getId();
            reviewsByResidence = retrofitCalls.getReviewsByResidenceId(token, Integer.toString(residenceId));
            reviewedResidences.get(i).setReviewsCollection(reviewsByResidence);
        }
        //sort the results
        Collections.sort(reviewedResidences);
        return reviewedResidences;
    }

    @Override
    protected void onResume()
    {
        //TODO check if data are updated with adapter, I have read that it is better to load comments in onResume rather than in onCreate
        resetActivity();
        user = true;
        adapter.notifyDataSetChanged();
        super.onResume();
    }


    @Override
    protected void onRestart() {
        resetActivity();
        user = true;
        adapter.notifyDataSetChanged();
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
        moveTaskToBack(true);
    }

    private void resetActivity() {
        Session sessionData = getSessionData(HomeActivity.this);
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

    /** Worker options for notification messages **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("SomeTag", System.currentTimeMillis() / 1000L + "  onDestory()");
    }

    private class Worker extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            Log.i("SomeTag", "start do in background at " + System.currentTimeMillis());
            String data = null;

            try {
                //getNewMessages();
//                Utils.callAsynchronousTask(HomeActivity.this, HomeActivity.class);
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet("https://stackoverflow.com/questions/tagged/android");
//
//                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                data = EntityUtils.toString(httpEntity);
                Log.i("SomeTag", "doInBackGround done at " + System.currentTimeMillis());
            } catch (Exception e) {}
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Utils.callAsynchronousTask(HomeActivity.this, token, loggedInUser.getId());
            Log.i("SomeTag", System.currentTimeMillis() / 1000L + " post execute \n" + result);
        }
    }
}