package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fromRESTful.Users;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import util.RestAPI;
import util.RestClient;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.goToActivity;
import static util.Utils.reloadActivity;
import static util.Utils.updateSessionData;

public class EditProfileActivity extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 3;

    Context c;
    Toolbar toolbar;
    Boolean user;
    RetrofitCalls retrofitCalls;
    Users loggedinUser;

    ImageView mImageView;

    ImageButton btnBirthDate;
    Button bsave;

    EditText etName, etLastName, etPhoneNumber, etEmail, etPassword, etCountry, etCity, etAbout;
    TextView tvBirthDate;
    String username, token;
    Integer userId;
    private int mYear, mMonth, mDay;

    Session sessionData;
    String realpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Get session data in order to check if user is logged in and if token is expired */
        sessionData = Utils.getSessionData(EditProfileActivity.this);
        token = sessionData.getToken();
        username = sessionData.getUsername();
        c = this;
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

        setContentView(R.layout.activity_edit_profile);
        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Edit Your Profile");
        toolbar.setSubtitle("Welcome " + username);
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
                handleBackAction();
            }
        });

        retrofitCalls = new RetrofitCalls();
        /** Get the user and present all his personal info*/
        ArrayList<Users> getUsersByUsername = retrofitCalls.getUserbyUsername(token, username);
        loggedinUser  = getUsersByUsername.get(0);

        userId              = loggedinUser.getId();
        etName              = (EditText) findViewById(R.id.etName);
        etLastName          = (EditText) findViewById(R.id.etLastName);
        etPhoneNumber       = (EditText) findViewById(R.id.etPhoneNumber);
        etEmail             = (EditText) findViewById(R.id.etEmail);
        etPassword          = (EditText) findViewById(R.id.etPassword);
        etCountry           = (EditText)findViewById(R.id.etCountry);
        etCity              = (EditText)findViewById(R.id.etCity);
        tvBirthDate         = (TextView) findViewById(R.id.tvBirthDate);
        etAbout             = (EditText)findViewById(R.id.etAbout);
        btnBirthDate        = (ImageButton)findViewById(R.id.btnBirthDate);
        mImageView          = (ImageView) findViewById(R.id.userImage);

        etName.setText(loggedinUser.getFirstName());
        etLastName.setText(loggedinUser.getLastName());
        etPhoneNumber.setText(loggedinUser.getPhoneNumber());
        etEmail.setText(loggedinUser.getEmail());
        etPassword.setText(loggedinUser.getPassword());
        etCountry.setText(loggedinUser.getCountry());
        etCity.setText(loggedinUser.getCity());

        /** Load Profile Image **/

        Button deleteImage = (Button) findViewById(R.id.deleteImage);
        if (loggedinUser.getPhoto() == null) {
            deleteImage.setVisibility(View.GONE);
        }

        Utils.loadProfileImage(EditProfileActivity.this, mImageView, loggedinUser.getPhoto());

        String birthDate;
        birthDate = loggedinUser.getBirthDate();
        tvBirthDate.setText(birthDate);
        etAbout.setText(loggedinUser.getAbout());

        btnBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnBirthDate) {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tvBirthDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        Button uploadImage = (Button) findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                token = retrofitCalls.deleteUserImage(token, userId);
                if (!token.isEmpty() && token!=null && token!="not") {
                    Toast.makeText(c, "Profile image was deleted", Toast.LENGTH_SHORT).show();

                    Bundle bextras = new Bundle();
                    bextras.putBoolean("type", user);
                    reloadActivity(EditProfileActivity.this, bextras);
                } else {
                    Toast.makeText(c, "Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                    Utils.logout(EditProfileActivity.this);
                    finish();
                }

            }
        });

        bsave = (Button)findViewById(R.id.post);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveUserProfile(); }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Detects request codes **/
        if(requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            mImageView = (ImageView) findViewById(R.id.userImage);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(selectedImage.toString()));

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                mImageView.setImageBitmap(bitmap);
                realpath = Utils.getRealPathFromURI(EditProfileActivity.this, selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class SendImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            File file = new File(params[0]);

//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            /** MultipartBody.Part is used to send also the actual file name **/
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            /** add another part within the multipart request **/
            RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, "User Image");

            RestAPI restAPI = RestClient.getClient(token).create(RestAPI.class);
            Call<String> call = restAPI.uploadProfileImg(userId, description, body);
            try {
                Response<String> resp = call.execute();
                resp.body();
            } catch(IOException e){
                Toast.makeText(c, "Uploading profile image failed.", Toast.LENGTH_SHORT).show();
                Log.i("",e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String nothing) {}
    }

    public boolean checkEmail (String Email) {
        boolean emailIsNew=false;
        /** Same process for email **/
        ArrayList<Users> users = retrofitCalls.getUserbyEmail(token, Email);
        if(users.size() == 0) emailIsNew = true;
        return  emailIsNew;
    }

    public void saveUserProfile ()
    {
        /** Get user input and send it back to the database */
        final String name           = etName.getText().toString();
        final String lastName       = etLastName.getText().toString();
        final String phoneNumber    = etPhoneNumber.getText().toString();
        final String Email          = etEmail.getText().toString();
        final String Username       = sessionData.getUsername();
        final String password       = etPassword.getText().toString();
        final String country        = etCountry.getText().toString();
        final String city           = etCity.getText().toString();
        final String birthdate      = tvBirthDate.getText().toString();
        final String about          = etAbout.getText().toString();
        final Date registrationDate = loggedinUser.getRegistrationDate();

        final String imageName      = loggedinUser.getPhoto();

        boolean emailIsNew;

        //check if all fields are completed
        if(Username.length() == 0 || name.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0 || Email.length() == 0 || password.length() == 0) {
            //if something is not filled in, user must fill again the form
            Toast.makeText(c, "Please fill in obligatory fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        //user is not allowed to change his username
        if(!username.equals(Username)){
            Toast.makeText(c, "You cannot change your username", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * Variable emailIsNew is used in order to check if user has changed his email and the new email is the same with another user's email
         * EmailIsNew is true if the email is unique or if email has not been changed
         */
        emailIsNew = checkEmail(Email);
        if(loggedinUser.getEmail().equals(Email)){
            emailIsNew = true;
        }

        if(emailIsNew)
        {
            //send user input to the database in order to update the specific user
            token = PutResult(loggedinUser.getId(), name, lastName, Username, password, Email, phoneNumber, country, city, about, birthdate, registrationDate, imageName);
            if (!token.isEmpty() && token!=null && token!="not") {
                if (realpath != null) {
                    new EditProfileActivity.SendImageTask().execute(realpath);
                }

                sessionData.setToken(token);
                sessionData.setUsername(username);
                sessionData.setUserLoggedInState(true);

                /** If data are stored successfully in the data base, the user is now logged in and the home activity starts **/
                sessionData = updateSessionData(EditProfileActivity.this, sessionData);

                /** In case of success user is redirected to the ProfileActivity **/
                Bundle bextras = new Bundle();
                bextras.putBoolean("type", user);
                goToActivity(EditProfileActivity.this, ProfileActivity.class, bextras);
            } else {
                Toast.makeText(c, "Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                Utils.logout(this);
                finish();
            }
        } else {
            Toast.makeText(c, "Email already exists, please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public String PutResult(int userId, String firstName, String lastName, String username, String password, String email, String phoneNumber, String country, String city,
                            String about, String birthDate, Date registrationDate, String photo) {
        Users UserParameters = new Users(userId, firstName, lastName, username, password, email, phoneNumber, country, city, about, birthDate, registrationDate, photo);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.editUser(token, userId, UserParameters);
        return token;
    }

    @Override
    public void onBackPressed(){
        handleBackAction();
    }

    public void handleBackAction()
    {
        /** Show confirmation message to user in order to go back **/
        new AlertDialog.Builder(this)
                .setTitle("Back").setMessage("Are you sure you want to go back? Your changes will not be saved!").setIcon(R.drawable.ic_back)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Utils.manageBackButton(EditProfileActivity.this, ProfileActivity.class, user);
                        return;
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}