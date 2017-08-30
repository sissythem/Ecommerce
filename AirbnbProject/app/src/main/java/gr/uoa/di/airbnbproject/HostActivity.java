package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Residences;
import fromRESTful.Reviews;
import fromRESTful.Users;
import util.RecyclerAdapterResidences;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.DELETE_ACTION;
import static util.Utils.EDIT_ACTION;
import static util.Utils.RESERVATIONS_ACTION;
import static util.Utils.VIEW_RESIDENCE_ACTION;
import static util.Utils.goToActivity;
import static util.Utils.reloadActivity;

public class HostActivity extends AppCompatActivity {
    String token;
    Users host;
    Toolbar toolbar;

    ImageButton baddResidence;

    Boolean user;
    Context c;

    ArrayList<Residences> storedResidences;
    ArrayList<Reviews> reviewsByResidence;
    RecyclerView residencesRecyclerView;
    RecyclerAdapterResidences adapterResidences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = Utils.getSessionData(HostActivity.this);
        token = sessionData.getToken();
        user=false;
        //check if user is logged in
        if (!sessionData.getUserLoggedInState()) {
            Utils.logout(this);
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

        setContentView(R.layout.activity_host);
        /** Set up the upper toolbar **/

        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Your Residences");
        setSupportActionBar(toolbar);

        /** User can upload a new residence */
        baddResidence = (ImageButton) findViewById(R.id.addResidence);
        baddResidence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle buser = new Bundle();
                buser.putBoolean("type", user);
                goToActivity(HostActivity.this, AddResidenceActivity.class, buser);
            }
        });

        /** Get the Host **/
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> hostUsers = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        host = hostUsers.get(0);

        /** RecyclerView for displaying all uploaded residences by this host */
        residencesRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        residencesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        residencesRecyclerView.setHasFixedSize(true);

        storedResidences = retrofitCalls.getResidencesByHost(token, host.getId().toString());
        for(int i=0;i<storedResidences.size();i++){
            reviewsByResidence = retrofitCalls.getReviewsByResidenceId(token, Integer.toString(storedResidences.get(i).getId()));
            storedResidences.get(i).setReviewsCollection(reviewsByResidence);
        }
        try {
            if (storedResidences.size() > 0) {
                adapterResidences = new RecyclerAdapterResidences(this, user, storedResidences, 1, "", "");
                residencesRecyclerView.setAdapter(adapterResidences);
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(HostActivity.this, user);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        /** By selecting an item a menu appears */
        super.onContextItemSelected(item);
        final Bundle btype = new Bundle();
        btype.putBoolean("type", user);

        final int resId = storedResidences.get(item.getItemId()).getId();

        /** Host can view his residence */
        if (item.getTitle().equals(VIEW_RESIDENCE_ACTION)) {
            btype.putInt("residenceId", resId);
            goToActivity(this, ResidenceActivity.class, btype);
        }
        /** Host can edit his residence **/
        else if (item.getTitle().equals(EDIT_ACTION)) {
            btype.putInt("residenceId", resId);
            goToActivity(this, EditResidenceActivity.class, btype);
        }
        /** Host can view all reservations made for the selected residence **/
        else if (item.getTitle().equals(RESERVATIONS_ACTION)) {
            btype.putInt("residenceId", resId);
            btype.putString("source", "host");
            goToActivity(this, HistoryReservationsActivity.class, btype);
        }
        /** Host can delete his residence **/
        else if (item.getTitle().equals(DELETE_ACTION)) {
            new AlertDialog.Builder(this)
                .setTitle("Delete Residence").setMessage("Do you really want to delete this residence?").setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RetrofitCalls retrofitCalls = new RetrofitCalls();
                        if (retrofitCalls.deleteResidenceById(token, Integer.toString(resId)) == null) {
                            Toast.makeText(HostActivity.this, "Residence was successfully deleted!", Toast.LENGTH_SHORT).show();
                            reloadActivity(HostActivity.this, btype);
                        } else {
                            Toast.makeText(HostActivity.this, "Something went wrong, residence is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();
            storedResidences.remove(resId);
            adapterResidences.setSearchList(storedResidences);
            adapterResidences.notifyDataSetChanged();

        } else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    /** When app is minimized and then reused, user can continue from where he left the app **/
    @Override
    public void onBackPressed() { moveTaskToBack(true); }
}