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
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Conversations;
import fromRESTful.Users;
import util.RecyclerAdapterInbox;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.DELETE_ACTION;
import static util.Utils.OPEN_MESSAGES_ACTION;
import static util.Utils.USER_RECEIVER;
import static util.Utils.USER_SENDER;
import static util.Utils.VIEW_RESIDENCE_ACTION;
import static util.Utils.getSessionData;
import static util.Utils.goToActivity;

public class InboxActivity extends AppCompatActivity
{
    String token;
    Toolbar toolbar;
    ArrayList<Conversations> Conversations;

    Boolean user;
    int currentUserId;

    String userType;
    Context c;

    RecyclerView inboxRecyclerView;
    RecyclerAdapterInbox adapterInbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = getSessionData(InboxActivity.this);
        token = sessionData.getToken();
        c=this;
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

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Inbox");
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
                Utils.manageBackButton(InboxActivity.this, (user)?HomeActivity.class:HostActivity.class, user);
            }
        });

        /** RecyclerView for presenting all conversations **/
        inboxRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        inboxRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        inboxRecyclerView.setHasFixedSize(true);

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        ArrayList<Users> getUsersByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        currentUserId = getUsersByUsername.get(0).getId();

        Conversations = retrofitCalls.getConversations(token, Integer.toString(currentUserId));
        loadConversations();

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(InboxActivity.this, user);
    }

    protected void loadConversations()
    {
        System.out.println(user);
        System.out.println(currentUserId);
        try {
            if (Conversations.size() > 0) {
                adapterInbox = new RecyclerAdapterInbox(this, user, Conversations, currentUserId);
                inboxRecyclerView.setAdapter(adapterInbox);
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    /** Menu options for each item **/
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        super.onContextItemSelected(item);
        /** User can delete a conversation */
        if (item.getTitle().equals(DELETE_ACTION)) {
            new AlertDialog.Builder(InboxActivity.this)
                .setTitle("Delete Conversation").setMessage("Do you really want to delete this conversation?").setIcon(R.drawable.ic_delete)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (currentUserId == Conversations.get(item.getItemId()).getSenderId().getId()) {
                            userType = USER_SENDER;
                        } else if (currentUserId == Conversations.get(item.getItemId()).getReceiverId().getId()) {
                            userType = USER_RECEIVER;
                        }

                        RetrofitCalls retrofitCalls = new RetrofitCalls();
                        token = retrofitCalls.deleteConversation(token, Conversations.get(item.getItemId()).getId(), currentUserId, userType);
                        if (!token.isEmpty() && token != null && token != "not") {
                            Toast.makeText(c, "Conversation was deleted!", Toast.LENGTH_SHORT).show();
                            Conversations.remove(item.getItemId());
                            adapterInbox.setConversations(Conversations);
                            adapterInbox.notifyDataSetChanged();
                        } else if (token.equals("not")) {
                            Toast.makeText(c, "Failed to delete conversation! Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                            Utils.logout(InboxActivity.this);
                            finish();
                        } else {
                            Toast.makeText(c, "Something went wrong, conversation is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();
            /** User can view the related residence **/
        } else if (item.getTitle().equals(VIEW_RESIDENCE_ACTION)) {
            final Bundle btype = new Bundle();
            btype.putBoolean("type", user);
            btype.putInt("residenceId", Conversations.get(item.getItemId()).getResidenceId().getId());
            goToActivity(this, ResidenceActivity.class, btype);
            /** User can open this conversation to read all messages **/
        } else if (item.getTitle().equals(OPEN_MESSAGES_ACTION)) {
            openMessages(item.getItemId());
        } else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void openMessages(int pos) {
        int toUser;
        short isRead;
        if (currentUserId == Conversations.get(pos).getSenderId().getId()) {
            userType    = USER_SENDER;
            toUser      = Conversations.get(pos).getReceiverId().getId();
            isRead      = Conversations.get(pos).getReadFromSender();
        } else {
            userType    = USER_RECEIVER;
            toUser      = Conversations.get(pos).getSenderId().getId();
            isRead      = Conversations.get(pos).getReadFromReceiver();
        }

        if (isRead != 1) {
            RetrofitCalls retrofitCalls = new RetrofitCalls();
            token = retrofitCalls.updateConversation(token, "1", userType, Integer.toString(Conversations.get(pos).getId()));

            loadConversations();
        }

        Bundle btype = new Bundle();
        btype.putBoolean("type", user);
        btype.putInt("conversationId", Conversations.get(pos).getId());
        btype.putInt("toUserId", toUser);
        btype.putInt("currentUserId", currentUserId);
        btype.putString("msgSubject", Conversations.get(pos).getSubject());

        goToActivity(this, MessageActivity.class, btype);
    }
}