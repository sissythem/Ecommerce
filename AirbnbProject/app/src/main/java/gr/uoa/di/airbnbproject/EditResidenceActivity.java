package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import fromRESTful.Images;
import fromRESTful.Residences;
import fromRESTful.Users;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import util.RecyclerAdapterImages;
import util.RestAPI;
import util.RestClient;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.DELETE_ACTION;
import static util.Utils.FORMAT_DATE_DMY;
import static util.Utils.SET_AS_MAIN_IMAGE_ACTION;
import static util.Utils.convertDateToMillisSec;
import static util.Utils.convertTimestampToDateStr;
import static util.Utils.goToActivity;

public class EditResidenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    String resType, token;
    Integer residenceId;
    Residences selectedResidence;
    Toolbar toolbar;

    Boolean user;

    ImageButton bcontinue, btnStartDate, btnEndDate;
    EditText etTitle, etAbout, etAddress, etCity, etCountry, etAmenities, etFloor, etRooms, etBaths, etView, etGuests, etMinPrice, etAdditionalCost, etCancellationPolicy, etRules;
    TextView tvStartDate, tvEndDate;
    Spinner etType;
    String photo;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;

    CheckBox cbKitchen, cbLivingRoom;

    Context c;
    Users host;
    RetrofitCalls retrofitCalls;

    RecyclerView imagesRecyclerView;
    RecyclerAdapterImages adapterImages;

    private Button btnAddPhots;
    private ArrayList<String> imagesPathList;

    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    ArrayList<Images> residencePhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = Utils.getSessionData(EditResidenceActivity.this);
        token = sessionData.getToken();
        c = this;

        /** Check if user is logged in **/
        if (!sessionData.getUserLoggedInState()) {
            Utils.logout(this);
            finish();
            return;
        }

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        /** Check if token is expired **/
        if(Utils.isTokenExpired(sessionData.getToken())){
            Toast.makeText(c, "Session is expired", Toast.LENGTH_SHORT).show();
            Utils.logout(this);
            finish();
            return;
        }

        setContentView(R.layout.layout_residence_editfields);
        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        user = false;
        residenceId = buser.getInt("residenceId");

        /** Set up the upper toolbar **/
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Edit Residence");
        setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        /** Add back arrow to toolbar **/
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        /** Handle the back action **/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackAction();
            }
        });

        /** RecyclerView for displaying all uploaded residences by this host */
        imagesRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        imagesRecyclerView.setHasFixedSize(true);

        /** Initialize RetrofitCalls Instance **/
        retrofitCalls = new RetrofitCalls();

        /** Get images saved for this residence **/
        residencePhotos = retrofitCalls.getResidencePhotos(token, residenceId);
        try {
            if (residencePhotos.size() > 0) {
                adapterImages = new RecyclerAdapterImages(this, user, residencePhotos);
                imagesRecyclerView.setAdapter(adapterImages);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EditResidenceActivity.this, LinearLayoutManager.HORIZONTAL, false);
                imagesRecyclerView.setLayoutManager(horizontalLayoutManager);
                imagesRecyclerView.setAdapter(adapterImages);
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        /** Get the residence selected by user for update **/
        selectedResidence = retrofitCalls.getResidenceById(token, Integer.toString(residenceId));

        userInputLayout();
        ArrayList<Users> getUsersByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        host = getUsersByUsername.get(0);

        saveResidence();

        /** Initialize library for multiple selection of images **/
        Fresco.initialize(getApplicationContext());

        btnAddPhots = (Button)findViewById(R.id.btnAddPhots);
        btnAddPhots.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /** start multiple photos selector **/
                Intent intent = new Intent(EditResidenceActivity.this, ImagesSelectorActivity.class);

                /** max number of images to be selected **/
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);

                /** min size of image which will be shown; to filter tiny images (mainly icons) **/
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);

                /** show camera or not **/
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);

                /** pass current selected images as the initial value **/
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);

                /** start the selector **/
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        /** By selecting an item a menu appears */
        super.onContextItemSelected(item);
        final Bundle btype = new Bundle();
        btype.putBoolean("type", user);
        btype.putInt("residenceId", residenceId);

        final String resName    = residencePhotos.get(item.getItemId()).getName();
        final Integer imgId     = residencePhotos.get(item.getItemId()).getId();
        /** Host can delete an image from the residence **/
        if (item.getTitle().equals(DELETE_ACTION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Image").setMessage("Do you really want to delete this image?").setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            RetrofitCalls retrofitCalls = new RetrofitCalls();
                            token = retrofitCalls.deleteResidenceImage(token, imgId, resName);
                            if (!token.isEmpty() && token!=null && token!="not") {
                                Toast.makeText(EditResidenceActivity.this, "Image was successfully deleted!", Toast.LENGTH_SHORT).show();
                                residencePhotos.remove(item.getItemId());
                                adapterImages.setImages(residencePhotos);
                                adapterImages.notifyDataSetChanged();
                            } else {
                                Toast.makeText(EditResidenceActivity.this, "Something went wrong, image is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }})
                    .setNegativeButton(android.R.string.no, null).show();

        } else if (item.getTitle().equals(SET_AS_MAIN_IMAGE_ACTION)) {
            RetrofitCalls retrofitCalls = new RetrofitCalls();
            token = retrofitCalls.setMainResidencePhoto(token, residenceId, resName);
            if (!token.isEmpty() && token!=null && token!="not") {
                Toast.makeText(EditResidenceActivity.this, "Image was successfully set as main photo!", Toast.LENGTH_SHORT).show();
                goToActivity(EditResidenceActivity.this, EditResidenceActivity.class, btype);
            } else {
                Toast.makeText(EditResidenceActivity.this, "Something went wrong, image could not be set as main photo. Please try again!", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** Get selected images from selector **/
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            assert mResults != null;

            imagesPathList = new ArrayList<>();
            for(String result : mResults) {
                imagesPathList.add(result);
            }
        }
        String noofimage = (imagesPathList.size() > 1) ? " images are selected" : " image is selected";
        Toast.makeText(EditResidenceActivity.this, imagesPathList.size() + noofimage, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SendImageTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            ArrayList<String> selectedFiles = (ArrayList<String>) params[0];

            for(int i=0; i<selectedFiles.size(); i++){
                File file = new File(selectedFiles.get(i));

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
                /** MultipartBody.Part is used to send also the actual file name **/
                MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                /** add another part within the multipart request **/
                RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, "Residence Image");

                RestAPI restAPI = RestClient.getClient(token).create(RestAPI.class);
                Call<String> call = restAPI.uploadResidenceImg(residenceId, description, body);
                try {
                    Response<String> resp = call.execute();
                    photo = resp.body();
                } catch(IOException e){
                    Log.i("",e.getMessage());
                }
            }
            return token;
        }

        @Override
        protected void onPostExecute(String nothing) {}
    }

    public void userInputLayout ()
    {
        /** Present all the details of the selected residence */
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

        cbKitchen.setChecked((selectedResidence.getKitchen() == 1 ? true : false));
        cbLivingRoom.setChecked((selectedResidence.getLivingRoom() == 1 ? true : false));

        bcontinue = (ImageButton)findViewById(R.id.ibContinue);

        etType = (Spinner) findViewById(R.id.etType);
        /** Create an ArrayAdapter using the string array and a default spinner layout **/
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this, R.array.residence_types_array, android.R.layout.simple_spinner_item);

        /** Specify the layout to use when the list of choices appears **/
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /** Apply the adapter to the spinner **/
        etType.setAdapter(spinneradapter);
        etType.setOnItemSelectedListener(this);
        etType.setSelection(spinneradapter.getPosition(selectedResidence.getType()));

        etTitle.setText(selectedResidence.getTitle());
        etAbout.setText(selectedResidence.getAbout());
        etAddress.setText(selectedResidence.getAddress());
        etCity.setText(selectedResidence.getCity());
        etCountry.setText(selectedResidence.getCountry());
        etAmenities.setText(selectedResidence.getAmenities());
        etFloor.setText(Integer.toString(selectedResidence.getFloor()));
        etRooms.setText(Integer.toString(selectedResidence.getRooms()));
        etBaths.setText(Integer.toString(selectedResidence.getBaths()));
        etView.setText(selectedResidence.getView());
        etGuests.setText(Integer.toString(selectedResidence.getGuests()));
        etMinPrice.setText(Double.toString(selectedResidence.getMinPrice()));
        etAdditionalCost.setText(Double.toString(selectedResidence.getAdditionalCostPerPerson()));
        etCancellationPolicy.setText(selectedResidence.getCancellationPolicy());
        etRules.setText(selectedResidence.getRules());

        tvStartDate.setText(convertTimestampToDateStr(selectedResidence.getAvailableDateStart(), FORMAT_DATE_DMY));
        tvEndDate.setText(convertTimestampToDateStr(selectedResidence.getAvailableDateEnd(), FORMAT_DATE_DMY));

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnStartDate) {
                    /** Get Current Date **/
                    final Calendar c = Calendar.getInstance();
                    mStartYear = c.get(Calendar.YEAR);
                    mStartMonth = c.get(Calendar.MONTH);
                    mStartDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditResidenceActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    /** Get Current Date **/
                    final Calendar c = Calendar.getInstance();
                    mEndYear = c.get(Calendar.YEAR);
                    mEndMonth = c.get(Calendar.MONTH);
                    mEndDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditResidenceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tvEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.show();
                }
            }
        });

        photo = selectedResidence.getPhotos();
    }

    /**  An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos) **/
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { resType = parent.getItemAtPosition(pos).toString(); }
    /** Another interface callback **/
    public void onNothingSelected(AdapterView<?> parent) {}

    public void saveResidence ()
    {
        /** Get user input and send the updates back to the database with a PUT request */
        bcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagesPathList !=null && imagesPathList.size() > 0){
                    new EditResidenceActivity.SendImageTask().execute(imagesPathList);
                } else {
                    Toast.makeText(EditResidenceActivity.this," no images are selected", Toast.LENGTH_SHORT).show();
                }

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
                final String guests                     = etGuests.getText().toString();
                final String minPrice                   = etMinPrice.getText().toString();
                final String additionalCostPerPerson    = etAdditionalCost.getText().toString();
                final String availableStartDate         = tvStartDate.getText().toString();
                final String availableEndDate           = tvEndDate.getText().toString();
                final String cancellationPolicy         = etCancellationPolicy.getText().toString();
                final String rules                      = etRules.getText().toString();
                final short kitchen                     = cbKitchen.isChecked() ? (short)1 : (short)0;
                final short livingRoom                  = cbLivingRoom.isChecked() ? (short)1 : (short)0;

                long startDate = convertDateToMillisSec(availableStartDate, FORMAT_DATE_DMY);
                long endDate = convertDateToMillisSec(availableEndDate, FORMAT_DATE_DMY);

                if (type.length() == 0 || title.length() == 0 || about.length() == 0 || address.length() == 0 || city.length() == 0 || country.length() == 0 || amenities.length() == 0 || floor.length() == 0
                        || rooms.length() == 0 || baths.length() == 0 || view.length() == 0 || guests.length() == 0 || minPrice.length() == 0
                        || additionalCostPerPerson.length() == 0 || cancellationPolicy.length() == 0 || rules.length() == 0
                        || startDate <= 0 || endDate <= 0 || endDate <= startDate) {
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Residences ResidenceParameters = new Residences(
                            residenceId,
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
                            endDate,
                            photo
                    );

                    RetrofitCalls retrofitCalls = new RetrofitCalls();
                    token = retrofitCalls.editResidence(token, residenceId, ResidenceParameters);

                    /** If the PUT request is successful user can go back to HostActivity **/
                    if (!token.isEmpty() && token!=null && token!="not") {
                        Bundle bhost = new Bundle();
                        user = false;
                        bhost.putBoolean("type", user);
                        goToActivity(EditResidenceActivity.this, HostActivity.class, bhost);
                    } else {
                        Toast.makeText(c, "Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                        Utils.logout(EditResidenceActivity.this);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
       handleBackAction();
    }

    public void handleBackAction() {
        /** Show confirmation message to user in order to go back **/
        new AlertDialog.Builder(this)
                .setTitle("Back").setMessage("Are you sure you want to go back? Your changes will not be saved!").setIcon(R.drawable.ic_back)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Utils.manageBackButton(EditResidenceActivity.this, HostActivity.class, user);
                        return;
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}