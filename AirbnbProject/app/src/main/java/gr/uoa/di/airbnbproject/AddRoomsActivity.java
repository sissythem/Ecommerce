package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import fromRESTful.Residences;
import fromRESTful.Users;
import util.AddRoomsParameters;
import util.RestCallManager;
import util.RestCallParameters;
import util.RestCalls;
import util.RestPaths;

public class AddRoomsActivity extends AppCompatActivity
{
    String username;

    Boolean user;
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    Context c;
    ImageButton bsave;

    EditText etBeds;
    EditText etTypeBed;
    EditText etBaths;
    EditText etView;
    EditText etSpaceArea;
    TextView tvRoomNumber;

    Users host;
    Residences uploadedResidence;

    int numberOfRooms;
    int count;

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

        setContentView(R.layout.activity_add_rooms);

        Bundle buser = getIntent().getExtras();
        int residId = buser.getInt("residenceId");
        numberOfRooms = buser.getInt("numberOfRooms");
        user=false;
        c=this;

        host = RestCalls.getUser(username);
        uploadedResidence = RestCalls.getResidenceById(residId);

        bsave = (ImageButton)findViewById(R.id.ibSave);

        etBeds      = (EditText)findViewById(R.id.etBeds);
        etTypeBed   = (EditText)findViewById(R.id.etTypeBed);
        etBaths     = (EditText)findViewById(R.id.etBaths);
        etView      = (EditText)findViewById(R.id.etView);
        etSpaceArea = (EditText)findViewById(R.id.etSpaceArea);
        count=0;
        tvRoomNumber = (TextView) findViewById(R.id.roomNumber);
        tvRoomNumber.setText("Room " + (count+1));
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String beds = etBeds.getText().toString();
                final String typeBed = etTypeBed.getText().toString();
                final String baths = etBaths.getText().toString();
                final String viewRoom = etView.getText().toString();
                final String spaceArea = etSpaceArea.getText().toString();

                if(beds == null || typeBed == null || baths == null || viewRoom == null || spaceArea == null)
                {
                    Toast.makeText(c, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    boolean success = PostResult(beds, typeBed, baths, viewRoom, spaceArea);
                    if(success)
                    {
                        count++;
                        if(count<numberOfRooms)
                        {
                            etBeds.setText("");
                            etTypeBed.setText("");
                            etBaths.setText("");
                            etView.setText("");
                            etSpaceArea.setText("");
                            tvRoomNumber.setText("Room " + (count+1));
                            return;
                        }
                        else
                        {
                            Intent hostIntent = new Intent(AddRoomsActivity.this, HostActivity.class);
                            Bundle bhost = new Bundle();
                            bhost.putBoolean("type", user);
                            hostIntent.putExtras(bhost);
                            try{
                                startActivity(hostIntent);
                                finish();
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(c, "Rooms upload failed, please try again!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });
    }

    public boolean PostResult(String beds, String typeBed, String baths, String view, String spaceArea)
    {
        boolean success=true;
        String roomPostURL = RestPaths.AllRooms;
        AddRoomsParameters RoomParameters = new AddRoomsParameters(host, uploadedResidence, beds, typeBed, baths, view, spaceArea);

        RestCallManager roomPostManager = new RestCallManager();
        RestCallParameters roomPostParameters = new RestCallParameters(roomPostURL, "POST", "", RoomParameters.getAddRoomParameters());

        String response;

        roomPostManager.execute(roomPostParameters);
        response = (String)roomPostManager.getRawResponse().get(0);
        if (response.equals("OK")) ;
        else success = false;

        return success;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(getIntent());

    }


}
