package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import fromRESTful.Conversations;
import util.ListAdapterInbox;
import util.RestCalls;
import util.Utils;

public class InboxActivity extends AppCompatActivity {
    String username;

    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;

    ListView inboxlist;
    ListAdapterInbox inboxadapter;
    int[] conversationId, toUserId;
    String[] msgsubject, msgname;
    Boolean user;
    Integer currentUserId;
    short[] isRead;

    String userType;
    private static final String USER_SENDER = "sender";
    private static final String USER_RECEIVER = "receiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        sharedPrefs = getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isUserLoggedIn = sharedPrefs.getBoolean("userLoggedInState", false);
        username = sharedPrefs.getString("currentLoggedInUser", "");

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        currentUserId = RestCalls.getUserId(username);

        loadConversations();
        setAdapter();
        inboxlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /** TODO: ANDROID CANT UNDERSTAND UPDATES?? **/
                if (isRead[position] != 1) {
                    Conversations updatedConversation = RestCalls.updateConversation(1, userType, conversationId[position]);
//                    if (userType == USER_SENDER) {
//                        isRead[position] = updatedConversation.getReadFromSender();
//                    } else if (userType == USER_RECEIVER){
//                        isRead[position] = updatedConversation.getReadFromReceiver();
//                    }
                    isRead[position] = 1;
                    loadConversations();
                    setAdapter();
                }


                Intent showMessageIntent = new Intent(InboxActivity.this, MessageActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("user", user);
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

    protected void setAdapter() {
        inboxlist = (ListView) findViewById(R.id.inboxlist);
        inboxadapter = new ListAdapterInbox(this, msgname, msgsubject, isRead);
        inboxlist.setAdapter(inboxadapter);
    }

    protected void loadConversations() {
        ArrayList<Conversations> Conversations = RestCalls.getConversations(currentUserId);
        msgname             = new String[Conversations.size()];
        msgsubject          = new String[Conversations.size()];
        conversationId      = new int[Conversations.size()];
        toUserId            = new int[Conversations.size()];
        isRead              = new short[Conversations.size()];

        for(int i = 0; i < Conversations.size(); i++) {
            if (currentUserId == Conversations.get(i).getSenderId().getId()) {
                userType    = USER_SENDER;
                msgname[i]  = Conversations.get(i).getReceiverId().getFirstName() + " " + Conversations.get(i).getReceiverId().getLastName();
                toUserId[i] = Conversations.get(i).getReceiverId().getId();
                isRead[i]   = Conversations.get(i).getReadFromSender();
            } else {
                userType    = USER_RECEIVER;
                msgname[i]  = Conversations.get(i).getSenderId().getFirstName() + " " + Conversations.get(i).getSenderId().getLastName();
                toUserId[i] = Conversations.get(i).getSenderId().getId();
                isRead[i]   = Conversations.get(i).getReadFromReceiver();
            }
            msgsubject[i]       = Conversations.get(i).getSubject();
            conversationId[i]   = Conversations.get(i).getId();
        }
    }
}
