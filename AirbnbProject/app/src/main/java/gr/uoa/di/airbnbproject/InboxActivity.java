package gr.uoa.di.airbnbproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import fromRESTful.Conversations;
import fromRESTful.Users;
import util.ListAdapterInbox;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.getSessionData;

public class InboxActivity extends AppCompatActivity {
    String token;

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

        Bundle buser = getIntent().getExtras();
        user = buser.getBoolean("type");
        //token = buser.getString("token", token);

        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Utils.checkToken(token, InboxActivity.this);
        ArrayList<Users> getUsersByUsername = retrofitCalls.getUserbyUsername(token, sessionData.getUsername());
        currentUserId = getUsersByUsername.get(0).getId();

        loadConversations();
        setAdapter();
        inboxlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /** TODO: ANDROID CANT UNDERSTAND UPDATES?? **/
                if (isRead[position] != 1) {
                    RetrofitCalls retrofitCalls = new RetrofitCalls();
                    Utils.checkToken(token, InboxActivity.this);
                    Conversations updatedConversation = retrofitCalls.updateConversation(token, "1", userType, Integer.toString(conversationId[position])).get(0);
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

    protected void setAdapter() {
        inboxlist = (ListView) findViewById(R.id.inboxlist);
        inboxadapter = new ListAdapterInbox(this, msgname, msgsubject, isRead);
        inboxlist.setAdapter(inboxadapter);
    }

    protected void loadConversations() {
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Utils.checkToken(token, InboxActivity.this);
        ArrayList<Conversations> Conversations = retrofitCalls.getConversations(token, currentUserId.toString());

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
