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
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import fromRESTful.Rooms;
import util.CustomListAdapter;
import util.RestCalls;

/**
 * Created by sissy on 30/4/2017.
 */

public class HomeActivity extends AppCompatActivity
{
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String username;
    ListView list;

    ImageButton bhome;
    ImageButton binbox;
    ImageButton bprofile;
    ImageButton bswitch;
    ImageButton blogout;

    CustomListAdapter adapter;

    EditText field_city, field_guests, startDate, endDate;
    Button btnStartDatePicker, btnEndDatePicker, field_search;
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    Boolean user;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

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

        setContentView(R.layout.activity_home);

        ManageFooter();
        setupSearchView();

        ArrayList<Residences> Recommendations = popularRecommendations(username);

        String[] representativePhoto = new String [Recommendations.size()];
        String[] city = new String[Recommendations.size()];
        Double[] price = new Double[Recommendations.size()];
        float[] rating = new float[Recommendations.size()];

        for(int i=0; i<Recommendations.size();i++){
            representativePhoto[i] = Recommendations.get(i).getPhotos();
            city[i] = Recommendations.get(i).getCity();
            price[i] = Recommendations.get(i).getMinPrice();
            rating[i] = (float)Recommendations.get(i).getAverageRating();
        }
        adapter=new CustomListAdapter(this, representativePhoto, city, price, rating);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showResidenceIntent = new Intent(HomeActivity.this, ResidenceActivity.class);
                HomeActivity.this.startActivity(showResidenceIntent);
            }
        });

    }

    public void ManageFooter(){
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
                finish();
                startActivity(getIntent());
            }
        });

        binbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxintent = new Intent(HomeActivity.this, InboxActivity.class);
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
                Intent profileintent = new Intent(HomeActivity.this, ProfileActivity.class);
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
                Intent hostintent = new Intent(HomeActivity.this, HostActivity.class);
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
        user=true;
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
        user=true;
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

    public void logout() {
        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        Intent greetingintent = new Intent(HomeActivity.this, GreetingActivity.class);
        HomeActivity.this.startActivity(greetingintent);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    } */

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

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

                {
                    // All Permissions Granted
                    popularRecommendations(username);
                } else {
                    // Permission Denied
                    Toast.makeText(HomeActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public ArrayList<Residences> popularRecommendations(String username)
    {
        ArrayList<Residences> reviewedResidences= new ArrayList<>();

        int userId = RestCalls.getUserId(username);
        Set<String> relevantCity = RestCalls.getSearchedCities(userId);
        ArrayList<Rooms> roomsByResidence;
        ArrayList<Reviews> reviewsByResidence;
        int residenceId;
        //if user has not searched anything yet, most popular residences will appear
        if (relevantCity.size() == 0) {
            ArrayList<Reviews> reviews = RestCalls.getReviews();
            for (int i=0;i<reviews.size();i++) {
                reviewedResidences.add(reviews.get(i).getResidenceId());
            }
        }
        //if user has already searched, we will show the most popular residences in the relevant cities
        else {
            for (String city : relevantCity) {
                reviewedResidences = RestCalls.getResidenceByCity(city);
            }
        }
        //check for duplicates
        Set<Residences> hs = new HashSet<>();
        hs.addAll(reviewedResidences);
        reviewedResidences.clear();
        reviewedResidences.addAll(hs);
        if(reviewedResidences.size() ==0)
        {
            reviewedResidences = RestCalls.getAllResidences();
        }
        //get all relevant rooms and reviews
        for(int i=0; i<reviewedResidences.size();i++){
            residenceId = reviewedResidences.get(i).getId();
            roomsByResidence = RestCalls.getRoomsByResidenceId(residenceId);
            reviewsByResidence = RestCalls.getReviewsByResidenceId(residenceId);
            reviewedResidences.get(i).setRoomsCollection(roomsByResidence);
            reviewedResidences.get(i).setReviewsCollection(reviewsByResidence);
        }
        //sort the results
        Collections.sort(reviewedResidences);
        return reviewedResidences;
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
        field_city.setText(field_city.getText().toString());
        //field_city.addTextChangedListener(new GenericTextWatcher(field_city));

        field_guests = (EditText) findViewById(R.id.field_guests);
        field_guests.setText(field_guests.getText().toString());
        //field_guests.addTextChangedListener(new GenericTextWatcher(field_guests));

        field_search = (Button) findViewById(R.id.field_search);

        field_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchintent = new Intent(HomeActivity.this, SearchResultsActivity.class);
                HomeActivity.this.startActivity(searchintent);

                /** Vasso's version **/
//                reviewedResidences = RestCalls.getRecommendations(userID, field_city.toString(), startDate.toString(), endDate.toString(), Integer.parseInt(field_guests.toString()));
//                System.out.println(reviewedResidences);
            }
        });

        /**** Dates Selector ****/
        btnStartDatePicker = (Button)findViewById(R.id.btn_start_date);
        startDate = (EditText)findViewById(R.id.start_date);

        btnEndDatePicker = (Button)findViewById(R.id.btn_end_date);
        endDate = (EditText)findViewById(R.id.end_date);

        btnStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnStartDatePicker) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mStartYear = c.get(Calendar.YEAR);
                    mStartMonth = c.get(Calendar.MONTH);
                    mStartDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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

                    DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            endDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.show();
                }
            }
        });

        field_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchintent = new Intent(HomeActivity.this, SearchResultsActivity.class);
                HomeActivity.this.startActivity(searchintent);

                /** Vasso's version **/
//                reviewedResidences = RestCalls.getRecommendations(userID, field_city.toString(), startDate.toString(), endDate.toString(), Integer.parseInt(field_guests.toString()));
//                System.out.println(reviewedResidences);
            }
        });
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