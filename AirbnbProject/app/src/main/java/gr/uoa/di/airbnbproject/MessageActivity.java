package gr.uoa.di.airbnbproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import fromRESTful.Conversations;
import fromRESTful.Messages;
import fromRESTful.Residences;
import fromRESTful.Users;
import retrofit2.Call;
import retrofit2.Response;
import util.ListAdapterMessages;
import util.RestAPI;
import util.RestClient;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.ConvertStringToDate;
import static util.Utils.DATABASE_DATE_FORMAT;
import static util.Utils.getCurrentDate;
import static util.Utils.logout;

public class MessageActivity extends AppCompatActivity {
    Context c;
    Integer currentUserId, toUserId;

    Button send;
    String msgSubject, msgBody;
    TextView subject;
    EditText body;
    int conversationId, messagesSize, residenceId;

    ListAdapterMessages msgadapter;
    ListView messageslist;
    Boolean user, isNewMessage;

    ArrayList<Messages> Messages;
    Conversations conversation;

    String userType, token, backBundle;
    private static final String USER_SENDER = "sender";
    private static final String USER_RECEIVER = "receiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;

        Session sessionData = Utils.getSessionData(MessageActivity.this);
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
        setContentView(R.layout.activity_message);

        Bundle bextras  = getIntent().getExtras();
        user            = bextras.getBoolean("type");
        currentUserId   = bextras.getInt("currentUserId");
        toUserId        = bextras.getInt("toUserId");
        msgSubject      = bextras.getString("msgSubject");
        backBundle      = bextras.getString("back");

        subject = (TextView) findViewById(R.id.subject);
        subject.setText(msgSubject);
        body = (EditText) findViewById(R.id.body);

        RetrofitCalls retrofitCalls = new RetrofitCalls();

        if(Utils.isTokenExpired(token))
        {
            Utils.logout(this);
            finish();
        }

        if (bextras.containsKey("conversationId")) {
            isNewMessage = false;
            conversationId = bextras.getInt("conversationId");

            Messages = retrofitCalls.getMessagesByConversation(token, Integer.toString(conversationId));
            messagesSize = Messages.size();
            conversation = retrofitCalls.getConversationById(token, Integer.toString(conversationId));
        } else if (bextras.containsKey("residenceId")) {
            residenceId = bextras.getInt("residenceId");

            ArrayList<Conversations> conversationData = retrofitCalls.getConversationsByResidenceId(token, Integer.toString(residenceId), currentUserId.toString());
            if (conversationData.size() > 0) {
                conversation = conversationData.get(0);
                isNewMessage = false;
                conversationId = conversation.getId();
                Messages = retrofitCalls.getMessagesByConversation(token, Integer.toString(conversationId));
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
        if(backBundle.equals("inbox"))
        {
            Utils.manageBackButton(this, InboxActivity.class, user);
        }
        else if(backBundle.equals("residence"))
        {
            Utils.manageBackButton(this, ViewHostProfileActivity.class, user);
        }
    }

    public void sendMessage()
    {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;

                msgBody = body.getText().toString();
                if (msgBody == null || msgBody == "" || msgBody.length() == 0) {
                    Toast.makeText(c, "Please write a message!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    RetrofitCalls retrofitCalls = new RetrofitCalls();
                    Users senderUser = retrofitCalls.getUserbyId(token, currentUserId.toString());
                    Users receiverUser = retrofitCalls.getUserbyId(token, toUserId.toString());

                    if (!isNewMessage) {
                        success = PostMessageResult(senderUser, conversation, msgBody);
                    } else {
                        if (PostConversationResult(senderUser, receiverUser, retrofitCalls.getResidenceById(token, Integer.toString(residenceId)), msgSubject)) {
                            /** Last Conversation entry in dbtable **/
                            conversation = retrofitCalls.getLastConversation(token, currentUserId.toString(), toUserId.toString()).get(0);
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
                        retrofitCalls.updateConversation(token, "0", userType, Integer.toString(conversation.getId())).get(0);

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

    public boolean PostConversationResult(Users senderUser, Users receiverUser, Residences residence, String subject)
    {
        short val_zero = 0;
        short val_one = 1;
        Conversations ConversationParams = new Conversations(senderUser, receiverUser, residence, subject, val_one, val_zero, val_zero, val_zero);
        RestAPI restAPI = RestClient.getClient(token).create(RestAPI.class);
        System.out.println(ConversationParams);
        Call<String> call = restAPI.postConversation(ConversationParams);
        try {
            Response<String> resp = call.execute();
            token = resp.body();
        } catch (IOException e) {
            Log.i("",e.getMessage());
        }

        boolean success = false;
        if (!token.equals("not")) {
            success = true;
        } else {
            logout(MessageActivity.this);
        }
        return success;
    }

    public boolean PostMessageResult(Users usermodel, Conversations conversationmodel, String body) {
        short val_zero = 0;
        String currDate = getCurrentDate(DATABASE_DATE_FORMAT);
        Messages MessagesParams = new Messages(usermodel, conversationmodel, body, ConvertStringToDate(currDate, DATABASE_DATE_FORMAT), val_zero, val_zero);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<String> call = restAPI.postMessage(MessagesParams);
        try {
            Response<String> resp = call.execute();
            token = resp.body();
        } catch (IOException e) {
            Log.i("",e.getMessage());
        }

        boolean success = false;
        if (!token.equals("not")) {
            success = true;
        } else {
            logout(MessageActivity.this);
        }
        return success;
    }
}
