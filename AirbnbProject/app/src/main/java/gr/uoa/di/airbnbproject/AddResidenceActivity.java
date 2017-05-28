package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import fromRESTful.Users;
import util.AddResidenceParameters;
import util.RestCallManager;
import util.RestCallParameters;
import util.RestCalls;
import util.RestPaths;
import util.Utils;

import static android.R.attr.country;
import static android.R.attr.type;
import static gr.uoa.di.airbnbproject.R.id.city;

public class AddResidenceActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE =1;

    String username;

    Boolean user;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    ImageButton bcontinue;
    ImageButton btnStartDate;
    ImageButton btnEndDate;
    ImageButton bback;

    ImageView imageToUpload;
    EditText etUpload, etType, etAbout, etAddress, etCity, etCountry, etAmenities, etFloor, etRooms, etBaths, etView;
    EditText etSpaceArea, etGuests, etMinPrice, etAdditionalCost, etStartDate, etEndDate, etCancellationPolicy, etRules;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    CheckBox cbKitchen;
    CheckBox cbLivingRoom;

    boolean bkitchen;
    boolean blivingRoom;

    Context c;
    Users host;

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

        setContentView(R.layout.activity_add_residence);

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        user=false;

        c=this;
        bback = (ImageButton)findViewById(R.id.ibBack);

        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hostIntent = new Intent(AddResidenceActivity.this, HostActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type",user);
                hostIntent.putExtras(buser);
                try {
                    startActivity(hostIntent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        userInputLayout();

        host = RestCalls.getUser(username);

        saveResidence();
    }

    public void userInputLayout ()
    {
        etUpload            = (EditText)findViewById(R.id.etUpload);
        etType              = (EditText)findViewById(R.id.etType);
        etAbout             = (EditText)findViewById(R.id.etAbout);
        etAddress           = (EditText)findViewById(R.id.etAddress);
        etCity              = (EditText)findViewById(R.id.etCity);
        etCountry           = (EditText)findViewById(R.id.etCountry);
        etAmenities         = (EditText)findViewById(R.id.etAmenities);
        etFloor             = (EditText)findViewById(R.id.etFloor);
        etRooms             = (EditText)findViewById(R.id.etRooms);
        etBaths             = (EditText)findViewById(R.id.etBaths);
        etView              = (EditText)findViewById(R.id.etView);
        etSpaceArea         = (EditText)findViewById(R.id.etSpaceArea);
        etGuests            = (EditText)findViewById(R.id.etGuests);
        etMinPrice          = (EditText)findViewById(R.id.etMinPrice);
        etAdditionalCost    = (EditText)findViewById(R.id.etAdditionalCost);
        etStartDate         = (EditText)findViewById(R.id.etStartDate);
        etEndDate           = (EditText)findViewById(R.id.etEndDate);
        etCancellationPolicy = (EditText)findViewById(R.id.etCancellationPolicy);
        etRules             = (EditText)findViewById(R.id.etRules);

        btnStartDate        = (ImageButton)findViewById(R.id.btnStartDate);
        btnEndDate          = (ImageButton)findViewById(R.id.btnEndDate);

        cbKitchen           = (CheckBox)findViewById(R.id.cbKitchen);
        cbLivingRoom        = (CheckBox)findViewById(R.id.cbLivingRoom);

        bkitchen = (cbKitchen).isChecked();
        blivingRoom = (cbLivingRoom).isChecked();

        bcontinue = (ImageButton)findViewById(R.id.ibContinue);

//        imageToUpload = (ImageView)findViewById(R.id.imageToUpload);
//
//        imageToUpload.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
//            }
//        });

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnStartDate) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mStartYear = c.get(Calendar.YEAR);
                    mStartMonth = c.get(Calendar.MONTH);
                    mStartDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddResidenceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            etStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mStartYear, mStartMonth, mStartDay);
                    datePickerDialog.show();
                }
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnEndDate) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mEndYear = c.get(Calendar.YEAR);
                    mEndMonth = c.get(Calendar.MONTH);
                    mEndDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddResidenceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            etEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.show();
                }
            }
        });
    }


    public void saveResidence ()
    {
        bcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.w("","SETTING HARDCODED RESIDENCE VALUES FOR DEBUGGING!");
                final String photo = etUpload.getText().toString();
                final String type = etType.getText().toString();
                final String about = etAbout.getText().toString();
                final String address = etAddress.getText().toString();
                final String city = etCity.getText().toString();
                final String country = etCountry.getText().toString();
                final String amenities = etAmenities.getText().toString();
                final String floor = etFloor.getText().toString();
                final String rooms = etRooms.getText().toString();
                final String baths = etBaths.getText().toString();
                final String view = etView.getText().toString();
                final String spaceArea = etSpaceArea.getText().toString();
                final String guests = etGuests.getText().toString();
                final String minPrice = etMinPrice.getText().toString();
                final String additionalCostPerPerson = etAdditionalCost.getText().toString();
                final String availableStartDate = etStartDate.getText().toString();
                final String availableEndDate = etEndDate.getText().toString();
                final String cancellationPolicy = etCancellationPolicy.getText().toString();
                final String rules = etRules.getText().toString();
                final String kitchen = Boolean.toString(bkitchen);
                final String livingRoom = Boolean.toString(blivingRoom);

                Date startDate = Utils.ConvertStringToDate(availableStartDate, Utils.APP_DATE_FORMAT);
                String convertedStartDate = Utils.ConvertDateToString(startDate, Utils.DATABASE_DATE_FORMAT);

                Date endDate = Utils.ConvertStringToDate(availableEndDate, Utils.APP_DATE_FORMAT);
                String convertedEndDate = Utils.ConvertDateToString(endDate, Utils.DATABASE_DATE_FORMAT);

                if (type.length() == 0 || about.length() == 0 || address.length() == 0 || city.length() == 0 || country.length() == 0 || amenities.length() == 0 || floor.length() == 0
                        || rooms.length() == 0 || baths.length() == 0 || view.length() == 0 || spaceArea.length() == 0 || guests.length() == 0 || minPrice.length() == 0
                        || additionalCostPerPerson.length() == 0 || cancellationPolicy.length() == 0 || rules.length() == 0 || kitchen.length() == 0 || livingRoom.length() == 0
                        || convertedStartDate.length() == 0 || convertedEndDate.length() == 0 || photo.length() == 0)
                {
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    {

                    boolean success = PostResult(host, type, about, address, city, country, amenities, floor, rooms, baths, view, spaceArea, guests, minPrice, additionalCostPerPerson,
                            cancellationPolicy, rules, kitchen, livingRoom, convertedStartDate, convertedEndDate, photo);

                    if (success)
                    {
                        int hostId = RestCalls.getUserId(username);
                        int residenceId = RestCalls.getMaxResidenceId(hostId);
                        Intent roomIntent = new Intent(AddResidenceActivity.this, AddRoomsActivity.class);
                        Bundle bhost = new Bundle();
                        int numberOfRooms = Integer.parseInt(rooms);
                        bhost.putInt("numberOfRooms", numberOfRooms);
                        bhost.putInt("residenceId", residenceId);
                        roomIntent.putExtras(bhost);
                        try {
                            startActivity(roomIntent);
                            finish();
                        } catch (Exception e) {
                            Log.e("",e.getMessage());
                        }
                    } else {
                        Toast.makeText(c, "Residence upload failed, please try again!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

    }

    public boolean PostResult(Users host, String type, String about, String address, String city, String country, String amenities, String floor, String rooms, String baths, String view,
                              String spaceArea, String guests, String minPrice, String additionalCostPerPerson, String cancellationPolicy, String rules,
                              String kitchen, String livingRoom, String startDate, String endDate, String photo)
    {
        boolean success = true;

        String residencePostURL = RestPaths.AllResidences;
        AddResidenceParameters ResidenceParameters = new AddResidenceParameters(host, type, about, address, city, country, amenities, floor, rooms, baths,
                view, spaceArea, guests, minPrice, additionalCostPerPerson, startDate, endDate, cancellationPolicy, rules, photo, kitchen, livingRoom);

        RestCallManager residencePostManager = new RestCallManager();
        RestCallParameters residencePostParameters = new RestCallParameters(residencePostURL, "POST", "", ResidenceParameters.getAddResidenceParameters());

//        ArrayList<String> PostResponse ;
        String response;

        residencePostManager.execute(residencePostParameters);
//            PostResponse = userpost.get(1000, TimeUnit.SECONDS);
        response = (String)residencePostManager.getRawResponse().get(0);
//            String result = PostResponse.get(0);
        if (response.equals("OK")) ;
        else success = false;

        return success;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }
    }
}
