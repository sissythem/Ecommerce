package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import fromRESTful.Residences;
import fromRESTful.Users;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.FORMAT_DATE_DMY;
import static util.Utils.convertDateToMillisSec;

public class AddResidenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private static final int RESULT_LOAD_IMAGE =1;

    String token;
    Boolean user;
    Toolbar toolbar;

    ImageButton bcontinue, btnStartDate, btnEndDate;
    Button upload;
    Spinner etType;
    String resType;
    EditText etAbout, etAddress, etCity, etCountry, etAmenities, etFloor, etRooms, etBaths, etView, etTitle, etSpaceArea, etGuests, etMinPrice, etAdditionalCost, etCancellationPolicy, etRules;
    TextView tvStartDate, tvEndDate;
    CheckBox cbKitchen, cbLivingRoom;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    Context c;
    Users host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = Utils.getSessionData(AddResidenceActivity.this);
        token = sessionData.getToken();
        c = this;
        user = false;
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
        setContentView(R.layout.layout_residence_editfields);
        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Add new Residence");
        setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        //handle the back action
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.manageBackButton(AddResidenceActivity.this, HostActivity.class, user);
            }
        });

        userInputLayout();
        RetrofitCalls retrofitCalls = new RetrofitCalls();

        //get all user data as Users object based on the username used to login
        List<Users> userData = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        host = userData.get(0);
        saveResidence();
    }

    public void userInputLayout ()
    {
        /** Necessary fields to be completed in order to upload a new residence*/
        etTitle              = (EditText)findViewById(R.id.etTitle);
        etAbout              = (EditText)findViewById(R.id.etAbout);
        etAddress            = (EditText)findViewById(R.id.etAddress);
        etCity               = (EditText)findViewById(R.id.etCity);
        etCountry            = (EditText)findViewById(R.id.etCountry);
        etAmenities          = (EditText)findViewById(R.id.etAmenities);
        etFloor              = (EditText)findViewById(R.id.etFloor);
        etRooms              = (EditText)findViewById(R.id.etRooms);
        etBaths              = (EditText)findViewById(R.id.etBaths);
        etView               = (EditText)findViewById(R.id.etView);
        etGuests             = (EditText)findViewById(R.id.etGuests);
        etMinPrice           = (EditText)findViewById(R.id.etMinPrice);
        etAdditionalCost     = (EditText)findViewById(R.id.etAdditionalCost);
        tvStartDate          = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate            = (TextView)findViewById(R.id.tvEndDate);
        etCancellationPolicy = (EditText)findViewById(R.id.etCancellationPolicy);
        etRules              = (EditText)findViewById(R.id.etRules);

        btnStartDate        = (ImageButton)findViewById(R.id.btnStartDate);
        btnEndDate          = (ImageButton)findViewById(R.id.btnEndDate);

        cbKitchen           = (CheckBox)findViewById(R.id.cbKitchen);
        cbLivingRoom        = (CheckBox)findViewById(R.id.cbLivingRoom);
        upload              = (Button)findViewById(R.id.btnAddPhots);
        bcontinue           = (ImageButton)findViewById(R.id.ibContinue);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.customultiple);
        linearLayout.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);

        /** When user clicks on the button, a calendar appears in order to pick up a date*/
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
                            tvStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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
                            tvEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.show();
                }
            }
        });

        //select residence type from a dropdown list
        etType = (Spinner) findViewById(R.id.etType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this, R.array.residence_types_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        etType.setAdapter(spinneradapter);
        etType.setOnItemSelectedListener(this);
    }

    /**  An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos) **/
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { resType = parent.getItemAtPosition(pos).toString(); }
    /** Another interface callback **/
    public void onNothingSelected(AdapterView<?> parent) { resType = "Residence Title"; }

    public void saveResidence ()
    {
        bcontinue.setOnClickListener(new View.OnClickListener()
        {
            /** Get user input*/
            @Override
            public void onClick(View v)
            {
                final String title                      = etTitle.getText().toString();
                final String type                       = resType;
                final String about                      = etAbout.getText().toString();
                final String address                    = etAddress.getText().toString();
                final String city                       = etCity.getText().toString();
                final String country                    = etCountry.getText().toString();
                final String amenities                  = etAmenities.getText().toString();
                final String floor                      = etFloor.getText().toString();
                final String rooms                      = etRooms.getText().toString();
                final String baths                      = etBaths.getText().toString();
                final String view                       = etView.getText().toString();
                final String spaceArea                  = etSpaceArea.getText().toString();
                final String guests                     = etGuests.getText().toString();
                final String minPrice                   = etMinPrice.getText().toString();
                final String additionalCostPerPerson    = etAdditionalCost.getText().toString();
                final String availableStartDate         = tvStartDate.getText().toString();
                final String availableEndDate           = tvEndDate.getText().toString();
                final String cancellationPolicy         = etCancellationPolicy.getText().toString();
                final String rules                      = etRules.getText().toString();
                final short kitchen                    = cbKitchen.isChecked() ? (short)1 : (short)0;
                final short livingRoom                 = cbLivingRoom.isChecked() ? (short)1 : (short)0;

                long startDate = convertDateToMillisSec(availableStartDate, FORMAT_DATE_DMY);
                long endDate = convertDateToMillisSec(availableEndDate, FORMAT_DATE_DMY);

                /** Check if all fields are completed by the user */
                if (title.length() == 0 || type.length() == 0 || about.length() == 0 || address.length() == 0 || city.length() == 0 || country.length() == 0 || amenities.length() == 0 || floor.length() == 0
                        || rooms.length() == 0 || baths.length() == 0 || view.length() == 0 || spaceArea.length() == 0 || guests.length() == 0 || minPrice.length() == 0
                        || additionalCostPerPerson.length() == 0 || cancellationPolicy.length() == 0 || rules.length() == 0
                        || startDate <= 0 || endDate <= 0 || endDate <= startDate) {
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    /** CALL AND GET REST PUT EDIT RESULT **/
                    Residences ResidenceParameters = new Residences(
                            host,
                            title,
                            type,
                            about,
                            address,
                            city,
                            country,
                            amenities,
                            Integer.parseInt(floor),
                            Integer.parseInt(rooms),
                            Integer.parseInt(baths),
                            view,
                            Integer.parseInt(guests),
                            Double.parseDouble(minPrice),
                            Double.parseDouble(additionalCostPerPerson),
                            cancellationPolicy,
                            rules,
                            kitchen,
                            livingRoom,
                            startDate,
                            endDate
                    );

                    RetrofitCalls retrofitCalls = new RetrofitCalls();
                    /** Send user input to be booked as a new residence in the database */
                    token = retrofitCalls.postResidence(token, ResidenceParameters);
                    /** If posting was successful, user is redirected to the HostActivity */
                    if (!token.isEmpty() && token!=null && token != "not")
                    {
                        Intent hostIntent = new Intent(AddResidenceActivity.this, HostActivity.class);
                        Bundle bhost = new Bundle();
                        bhost.putBoolean("type", user);
                        hostIntent.putExtras(bhost);
                        try {
                            startActivity(hostIntent);
                            finish();
                        } catch (Exception e) {
                            Log.e("",e.getMessage());
                        }
                    } else {
                        Toast.makeText(c, "Your session is finished, please login again!", Toast.LENGTH_SHORT).show();
                        Utils.logout(AddResidenceActivity.this);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }
}