package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Conversations;
import fromRESTful.Messages;
import fromRESTful.Residences;
import fromRESTful.Users;
import util.AddConversationParameters;
import util.AddMessageParameters;
import util.ListAdapterMessages;
import util.RestCallManager;
import util.RestCallParameters;
import util.RestCalls;
import util.RestPaths;
import util.Utils;

public class MessageActivity extends AppCompatActivity {
    private static final String USER_LOGIN_PREFERENCES = "login_preferences";
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private boolean isUserLoggedIn;
    Context c;
    Integer currentUserId, toUserId;

    Button send;
    String username, msgSubject, msgBody;
    TextView subject;
    EditText body;
    int conversationId, messagesSize, residenceId;

    ListAdapterMessages msgadapter;
    ListView messageslist;
    Boolean user, isNewMessage;

    ArrayList<Messages> Messages;
    Conversations conversation;

    String userType;
    private static final String USER_SENDER = "sender";
    private static final String USER_RECEIVER = "receiver";

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

        setContentView(R.layout.activity_message);

        c = this;

        Bundle bextras  = getIntent().getExtras();
        user            = bextras.getBoolean("type");
        currentUserId   = bextras.getInt("currentUserId");
        toUserId        = bextras.getInt("toUserId");
        msgSubject      = bextras.getString("msgSubject");

        subject = (TextView) findViewById(R.id.subject);
        subject.setText(msgSubject);

        body = (EditText) findViewById(R.id.body);

        if (bextras.containsKey("conversationId")) {
            isNewMessage = false;

            conversationId = bextras.getInt("conversationId");
            Messages = RestCalls.getMessages(conversationId);
            messagesSize = Messages.size();
            conversation = RestCalls.getConversation(conversationId);
        } else if (bextras.containsKey("residenceId")) {
            residenceId = bextras.getInt("residenceId");
            conversation = RestCalls.getConversationByResidence(residenceId);
            if (conversation != null) {
                isNewMessage = false;
                conversationId = conversation.getId();
                Messages = RestCalls.getMessages(conversationId);
                messagesSize = Messages.size();
            } else {
                isNewMessage = true;
                messagesSize = 0;
            }
        }

        int[] msguserid         = new int[messagesSize];
        String[] msgname        = new String[messagesSize];
        String[] msgbody        = new String[messagesSize];
        String[] msgtimestamp   = new String[messagesSize];

        if (messagesSize > 0) {
            for(int i=0; i < messagesSize; i++) {
                msguserid[i]    = Messages.get(i).getUserId().getId();
                msgname[i]      = Messages.get(i).getUserId().getUsername();
                msgbody[i]      = Messages.get(i).getBody();
                msgtimestamp[i] = Messages.get(i).getTimestamp().toString();
            }
        }

        messageslist = (ListView) findViewById(R.id.messageslist);
        msgadapter = new ListAdapterMessages(this, currentUserId, msguserid, msgname, msgbody, msgtimestamp);
        messageslist.setAdapter(msgadapter);

        send = (Button)findViewById(R.id.message_send_btn);
        sendMessage();

        /** BACK BUTTON **/
        Utils.manageBackButton(this, InboxActivity.class);
    }

    public void sendMessage() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;

                msgBody = body.getText().toString();
                if (msgBody == null || msgBody == "" || msgBody.length() == 0) {
                    Toast.makeText(c, "Please write a message!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Users senderUser = RestCalls.getUserById(currentUserId);

                    if (!isNewMessage) {
                        success = PostMessageResult(senderUser, conversation, msgBody);
                    } else {
                        if (PostConversationResult(senderUser, RestCalls.getUserById(toUserId), RestCalls.getResidenceById(residenceId), msgSubject)) {
                            /** Last Conversation entry in dbtable **/
                            conversation = RestCalls.getLastConversation(currentUserId, toUserId);
                            conversationId = conversation.getId();
                            success = PostMessageResult(senderUser, conversation, msgBody);
                        }
                    }

                    if (success) {
                        if (currentUserId == conversation.getSenderId().getId()) {
                            userType = USER_RECEIVER;
                        } else if (currentUserId == conversation.getReceiverId().getId()) {
                            userType = USER_SENDER;
                        }
                        RestCalls.updateConversation(0, userType, conversation.getId());

                        //finish();
                        Bundle bupdated = new Bundle();
                        bupdated.putBoolean("type", user);
                        bupdated.putInt("currentUserId", currentUserId);
                        bupdated.putInt("toUserId", toUserId);
                        bupdated.putString("msgSubject", msgSubject);
                        bupdated.putInt("conversationId", conversationId);

                        Intent currentIntent = getIntent();
                        currentIntent.putExtras(bupdated);
                        startActivity(currentIntent);
                        finish();
                    } else {
                        Toast.makeText(c, "Message failed to send!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    public boolean PostConversationResult(Users senderUser, Users receiverUser, Residences residence, String body) {
        boolean success = true;
        AddConversationParameters ConversationParameters = new AddConversationParameters(senderUser, receiverUser, residence, body);

        RestCallManager conversationPostManager = new RestCallManager();
        RestCallParameters conversationPostParameters = new RestCallParameters(RestPaths.AllConversations, "POST", "", ConversationParameters.getAddConversationParameters());

        conversationPostManager.execute(conversationPostParameters);
        String response = (String) conversationPostManager.getRawResponse().get(0);
        if (!response.equals("OK")) success = false;

        return success;
    }

    public boolean PostMessageResult(Users usermodel, Conversations conversationmodel, String body) {
        boolean success = true;
        AddMessageParameters MessageParameters = new AddMessageParameters(usermodel, conversationmodel, body);

        RestCallManager messagePostManager = new RestCallManager();
        RestCallParameters messagePostParameters = new RestCallParameters(RestPaths.addNewMessage(), "POST", "", MessageParameters.getAddMessageParameters());

        messagePostManager.execute(messagePostParameters);
        String response = (String) messagePostManager.getRawResponse().get(0);
        if (!response.equals("OK")) success = false;

        return success;
    }

}
