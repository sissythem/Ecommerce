package gr.uoa.di.airbnbproject;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Conversations;
import fromRESTful.Users;
import util.ListAdapterInbox;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.DELETE_ACTION;
import static util.Utils.USER_RECEIVER;
import static util.Utils.USER_SENDER;
import static util.Utils.getSessionData;

public class InboxActivity extends AppCompatActivity {
    String token;

    ArrayList<Conversations> Conversations;

    ListView inboxlist;
    ListAdapterInbox inboxadapter;
    int[] conversationId, toUserId;
    String[] msgsubject, msgname, userCurrentType;
    Boolean user;
    Integer currentUserId;
    short[] isRead, readFromSender, readFromReceiver, deletedFromSender, deletedFromReceiver;

    String userType;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Session sessionData = getSessionData(InboxActivity.this);
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
        token = sessionData.getToken();
        c=this;

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");

        RetrofitCalls retrofitCalls = new RetrofitCalls();

        if(Utils.isTokenExpired(token)) {
            Utils.logout(this);
            finish();
        }
        ArrayList<Users> getUsersByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        currentUserId = getUsersByUsername.get(0).getId();

        loadConversations();
        setAdapter();
        inboxlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** TODO: ANDROID CANT UNDERSTAND UPDATES?? **/
                if (isRead[position] != 1) {
                    String userUnread = "";
                    if (currentUserId == Conversations.get(position).getSenderId().getId()) {
                        userUnread = USER_SENDER;
                    } else if (currentUserId == Conversations.get(position).getReceiverId().getId()) {
                        userUnread = USER_RECEIVER;
                    }

                    RetrofitCalls retrofitCalls = new RetrofitCalls();
                    token = retrofitCalls.updateConversation(token, "1", userUnread, Integer.toString(conversationId[position]));
                    isRead[position] = 1;
                    loadConversations();
                    setAdapter();
                }

                Intent showMessageIntent = new Intent(InboxActivity.this, MessageActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type", user);
                btype.putInt("conversationId", conversationId[position]);
                btype.putInt("toUserId", toUserId[position]);
                btype.putInt("currentUserId", currentUserId);
                btype.putString("msgSubject", msgsubject[position]);
                showMessageIntent.putExtras(btype);
                try {
                    startActivity(showMessageIntent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        /** BACK BUTTON **/
        Utils.manageBackButton(this, (user)?HomeActivity.class:HostActivity.class, user);

        /** FOOTER TOOLBAR **/
        Utils.manageFooter(InboxActivity.this, user);
    }

    protected void loadConversations() {
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Conversations = retrofitCalls.getConversations(token, currentUserId.toString());

        userCurrentType     = new String[Conversations.size()];
        msgname             = new String[Conversations.size()];
        msgsubject          = new String[Conversations.size()];
        conversationId      = new int[Conversations.size()];
        toUserId            = new int[Conversations.size()];
        isRead              = new short[Conversations.size()];
        readFromSender      = new short[Conversations.size()];
        readFromReceiver    = new short[Conversations.size()];
        deletedFromSender   = new short[Conversations.size()];
        deletedFromReceiver = new short[Conversations.size()];

        for(int i = 0; i < Conversations.size(); i++) {
            if (currentUserId == Conversations.get(i).getSenderId().getId()) {
                userCurrentType[i]  = USER_SENDER;
                msgname[i]          = Conversations.get(i).getReceiverId().getFirstName() + " " + Conversations.get(i).getReceiverId().getLastName();
                toUserId[i]         = Conversations.get(i).getReceiverId().getId();
//                isRead[i]           = Conversations.get(i).getReadFromSender();
            } else {
                userCurrentType[i]  = USER_RECEIVER;
                msgname[i]          = Conversations.get(i).getSenderId().getFirstName() + " " + Conversations.get(i).getSenderId().getLastName();
                toUserId[i]         = Conversations.get(i).getSenderId().getId();
//                isRead[i]           = Conversations.get(i).getReadFromReceiver();
            }
            msgsubject[i]           = Conversations.get(i).getSubject();
            conversationId[i]       = Conversations.get(i).getId();

            readFromSender[i]       = Conversations.get(i).getReadFromSender();
            readFromReceiver[i]     = Conversations.get(i).getReadFromReceiver();

            deletedFromSender[i]    = Conversations.get(i).getDeletedFromSender();
            deletedFromReceiver[i]  = Conversations.get(i).getDeletedFromReceiver();
        }
    }

    protected void setAdapter() {
        inboxlist = (ListView) findViewById(R.id.inboxlist);
        inboxadapter = new ListAdapterInbox(this, currentUserId, userCurrentType, msgname, msgsubject, readFromSender, readFromReceiver, deletedFromSender, deletedFromReceiver);
        inboxlist.setAdapter(inboxadapter);
        registerForContextMenu(inboxlist);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Conversation Options");
        menu.add(0, info.position, 0, DELETE_ACTION);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        super.onContextItemSelected(item);
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
                        if (!token.isEmpty() && token!=null && token!="not") {
                            Toast.makeText(c, "Conversation was deleted!", Toast.LENGTH_SHORT).show();
                            reloadInbox();
                        } else if (token.equals("not")) {
                            Toast.makeText(c, "Failed to delete conversation! Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                            Utils.logout(InboxActivity.this);
                            finish();
                        } else {
                            Toast.makeText(c, "Something went wrong, conversation is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();


        } else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void reloadInbox() {
        Bundle bupdated = new Bundle();
        bupdated.putBoolean("type", user);

        Intent currentIntent = getIntent();
        currentIntent.putExtras(bupdated);

        try {
            startActivity(currentIntent);
            finish();
        } catch (Exception e) {
            Log.e("",e.getMessage());
        }
    }
}