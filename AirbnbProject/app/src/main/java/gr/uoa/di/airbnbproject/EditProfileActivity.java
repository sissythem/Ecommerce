package gr.uoa.di.airbnbproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
    TextView etUsername, tvBirthDate;
    String username, token;
    Integer userId;
    private int mYear, mMonth, mDay;

    Session sessionData;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionData = Utils.getSessionData(EditProfileActivity.this);
        token = sessionData.getToken();
        username = sessionData.getUsername();
        c = this;
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

        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Edit Your Profile");
        toolbar.setSubtitle("Welcome " + username);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        retrofitCalls = new RetrofitCalls();
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
        Utils.loadProfileImage(EditProfileActivity.this, mImageView, loggedinUser.getPhoto());

        String birthDate ="";
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
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        bsave = (Button)findViewById(R.id.post);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveUserProfile(); }
        });

//        /** BACK BUTTON **/
//        Utils.manageBackButton(EditProfileActivity.this, ProfileActivity.class, user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            mImageView = (ImageView) findViewById(R.id.userImage);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(selectedImage.toString()));

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                mImageView.setImageBitmap(bitmap);
                new EditProfileActivity.SendImageTask().execute(Utils.getRealPathFromURI(EditProfileActivity.this, selectedImage));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            System.out.println(params[0]);
            File file = new File(params[0]);
            System.out.println(file);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            /** MultipartBody.Part is used to send also the actual file name **/
            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            /** add another part within the multipart request **/
            RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, "User Image");

            RestAPI restAPI = RestClient.getClient(token).create(RestAPI.class);
            Call<String> call = restAPI.uploadProfileImg(userId, description, body);
            try {
                Response<String> resp = call.execute();
                System.out.println(resp.body());
                token = resp.body();
            } catch(IOException e){
                Log.i("",e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String nothing) {}
    }

    public boolean checkEmail (String Email) {
        boolean emailIsNew=false;
        //same process for email
        ArrayList<Users> users = retrofitCalls.getUserbyEmail(token, Email);
        if(users.size() == 0) emailIsNew = true;
        System.out.println(emailIsNew);
        return  emailIsNew;
    }

    public void saveUserProfile () {
        final String name           = etName.getText().toString();
        final String lastName       = etLastName.getText().toString();
        final String phoneNumber    = etPhoneNumber.getText().toString();
        final String Email          = etEmail.getText().toString();
        final String Username       = sessionData.getUsername();
        final String password       = etPassword.getText().toString();
        final String country        = etCountry.getText().toString();
        final String city           = etCity.getText().toString();
        final String birthdate      = tvBirthDate.getText().toString();
        final String photo          = imagePath;
        final String about          = etAbout.getText().toString();

        boolean emailIsNew;

        if(Username.length() == 0 || name.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0 || Email.length() == 0 || password.length() == 0) {
            //if something is not filled in, user must fill again the form
            Toast.makeText(c, "Please fill in obligatory fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!username.equals(Username)){
            Toast.makeText(c, "You cannot change your username", Toast.LENGTH_SHORT).show();
            return;
        }
        emailIsNew = checkEmail(Email);
        if(loggedinUser.getEmail().equals(Email)){
            emailIsNew=true;
        }

        if(emailIsNew) {
            token = PutResult(loggedinUser.getId(), name, lastName, Username, password, Email, phoneNumber, country, city, photo, about, birthdate);
            //if (!token.isEmpty() && token!=null && token!="not") {
            if (!token.isEmpty() && token!=null && token!="not") {
                sessionData.setToken(token);
                sessionData.setUsername(username);
                sessionData.setUserLoggedInState(true);
                System.out.println(sessionData);
                //if data are stored successfully in the data base, the user is now logged in and the home activity starts
                sessionData = updateSessionData(EditProfileActivity.this, sessionData);

                //user can continue to the Home Screen
                Intent profileintent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                profileintent.putExtras(buser);
                try {
                    startActivity(profileintent);
                    finish();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
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
                            String photo, String about, String birthDate) {
        Users UserParameters = new Users(userId, firstName, lastName, username, password, email, phoneNumber, country, city, photo, about, birthDate);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.editUser(token, userId, UserParameters);
        return token;
    }
}