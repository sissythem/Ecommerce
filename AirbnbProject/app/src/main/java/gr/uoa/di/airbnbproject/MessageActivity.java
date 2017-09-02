package gr.uoa.di.airbnbproject;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fromRESTful.Conversations;
import fromRESTful.Messages;
import fromRESTful.Residences;
import fromRESTful.Users;
import util.RecyclerAdapterMessages;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.ConvertStringToDate;
import static util.Utils.FORMAT_DATE_YMD;
import static util.Utils.USER_RECEIVER;
import static util.Utils.USER_SENDER;
import static util.Utils.getCurrentDate;

public class MessageActivity extends AppCompatActivity {
    Context c;
    Bundle bextras;
    Integer currentUserId, toUserId;
    Toolbar toolbar;
    ImageButton send;
    String msgSubject, msgBody;
    TextView subject;
    EditText body;
    int conversationId, messagesSize, residenceId;

    RecyclerAdapterMessages mAdapter;
    RecyclerView mRecyclerView;
    Boolean user, isNewMessage;

    ArrayList<Messages> Messages;
    Conversations conversation;

    String userType, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        /** Get session data in order to check if user is logged in and if token is expired */
        Session sessionData = Utils.getSessionData(MessageActivity.this);
        token = sessionData.getToken();
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

        setContentView(R.layout.activity_message);

        bextras  = getIntent().getExtras();
        user            = bextras.getBoolean("type");
        currentUserId   = bextras.getInt("currentUserId");
        toUserId        = bextras.getInt("toUserId");
        msgSubject      = bextras.getString("msgSubject");
        //set up the upper toolbar
        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Send a message");
        setSupportActionBar(toolbar);

        /** BACK BUTTON **/
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, getTheme()));
        //handle the back button of the toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackAction();
           }
        });

        /** Set up the layout **/
        subject = (TextView) findViewById(R.id.subject);
        subject.setText(msgSubject);
        body = (EditText) findViewById(R.id.body);

        RetrofitCalls retrofitCalls = new RetrofitCalls();

        /** Check if activity is called from InboxActivity or from ViewHostProfile
         * Get conversation object and all relevant messages **/
        if (bextras.containsKey("conversationId")) {
            isNewMessage = false;
            conversationId = bextras.getInt("conversationId");

            Messages = retrofitCalls.getMessagesByConversation(token, conversationId);
            messagesSize = Messages.size();
            conversation = retrofitCalls.getConversationById(token, conversationId);
        } else if (bextras.containsKey("residenceId")) {
            residenceId = bextras.getInt("residenceId");

            ArrayList<Conversations> conversationData = retrofitCalls.getConversationsByResidenceId(token, residenceId, currentUserId);
            if (conversationData.size() > 0) {
                conversation = conversationData.get(0);
                isNewMessage = false;
                conversationId = conversation.getId();
                Messages = retrofitCalls.getMessagesByConversation(token, conversationId);
                messagesSize = Messages.size();
            } else {
                isNewMessage = true;
                messagesSize = 0;
            }
        }

        if (conversation != null) {
            if (currentUserId == conversation.getSenderId().getId()) {
                userType = USER_SENDER;
            } else if (currentUserId == conversation.getReceiverId().getId()) {
                userType = USER_RECEIVER;
            }

            if ((userType == USER_SENDER && conversation.getDeletedFromSender() == 1) || (userType == USER_RECEIVER && conversation.getDeletedFromReceiver() == 1)) {
                token = retrofitCalls.restoreConversation(token, conversation.getId(), currentUserId, userType);
            }
        }

        /** Set up the RecyclerView**/
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setHasFixedSize(true);
        registerForContextMenu(mRecyclerView);
        try {
            if (Messages.size() > 0) {
                mAdapter = new RecyclerAdapterMessages(this, Messages, user, currentUserId, userType);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
        catch (Exception e) {
                Log.e("", e.getMessage());
        }
        send = (ImageButton) findViewById(R.id.message_send_btn);
        sendMessage();
    }

    /** Menu Options (Delete/Copy) **/
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        super.onContextItemSelected(item);
        int position = -1;
        try {
            position = mAdapter.getPosition();
        } catch (Exception e) {
//            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case 1:
                new AlertDialog.Builder(MessageActivity.this)
                        .setTitle("Delete Message").setMessage("Do you really want to delete this message?").setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                RetrofitCalls retrofitCalls = new RetrofitCalls();
                                token = retrofitCalls.deleteMessage(token, Messages.get(item.getItemId()).getId(), currentUserId, userType);
                                if (!token.isEmpty() && token!=null && token!="not") {
                                    Toast.makeText(c, "Message deleted!", Toast.LENGTH_SHORT).show();
                                    Messages.remove(Messages.get(item.getItemId()));
                                    mAdapter.setmMessages(Messages);
                                    mAdapter.notifyDataSetChanged();
                                } else if (token.equals("not")) {
                                    Toast.makeText(c, "Failed to delete message! Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                                    Utils.logout(MessageActivity.this);
                                    finish();
                                } else {
                                    Toast.makeText(c, "Something went wrong, message is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
                    break;
            case 2:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied text", Messages.get(item.getItemId()).getBody());
                clipboard.setPrimaryClip(clip);
                break;
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);

    }

    /** Send new message **/
    public void sendMessage() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            msgBody = body.getText().toString();
            if (msgBody == null || msgBody == "" || msgBody.length() == 0) {
                Toast.makeText(c, "Please write a message!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                RetrofitCalls retrofitCalls = new RetrofitCalls();
                Users senderUser = retrofitCalls.getUserbyId(token, currentUserId.toString());
                Users receiverUser = retrofitCalls.getUserbyId(token, toUserId.toString());

                if (!isNewMessage) {
                    token = PostMessageResult(senderUser, conversation, msgBody);
                } else {
                    token = PostConversationResult(senderUser, receiverUser, retrofitCalls.getResidenceById(token, Integer.toString(residenceId)), msgSubject);
                    if (!token.isEmpty() && token!=null && token!="not") {
                        /** Last Conversation entry in dbtable **/
                        conversation = retrofitCalls.getLastConversation(token, currentUserId, toUserId).get(0);
                        conversationId = conversation.getId();

                        token = PostMessageResult(senderUser, conversation, msgBody);
                    } else {
                        Toast.makeText(c, "Message failed to send! Maybe your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                        Utils.logout(MessageActivity.this);
                        finish();
                    }
                }

                if (!token.isEmpty() && token!=null && token!="not") {
                    String userUnreadType = "";
                    if (currentUserId == conversation.getSenderId().getId()) {
                        userUnreadType = USER_RECEIVER;
                    } else if (currentUserId == conversation.getReceiverId().getId()) {
                        userUnreadType = USER_SENDER;
                    }
                    token = retrofitCalls.updateConversation(token, "0", userUnreadType, Integer.toString(conversation.getId()));
                    reloadConversation();
                } else {
                    Toast.makeText(c, "Message failed to send! Maybe your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                    Utils.logout(MessageActivity.this);
                    finish();
                }
            }
            }
        });
    }

    public void reloadConversation() {
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

    /** Create Conversations object from user input if it is a new conversation **/
    public String PostConversationResult(Users senderUser, Users receiverUser, Residences residence, String subject) {
        short val_zero = 0;
        short val_one = 1;
        Conversations ConversationParams = new Conversations(senderUser, receiverUser, residence, subject, val_one, val_zero, val_zero, val_zero);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.startConversation(token, ConversationParams);
        return token;
    }

    /** Create Messages object to be sent for posting **/
    public String PostMessageResult(Users mUser, Conversations mConversation, String body) {
        short val_zero = 0;
        String currDate = getCurrentDate(FORMAT_DATE_YMD);
        Messages MessagesParams = new Messages(mUser, mConversation, body, ConvertStringToDate(currDate, FORMAT_DATE_YMD), val_zero, val_zero);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.sendMessage(token, MessagesParams);
        return token;
    }

    /** Check the previous activity in order to correctly handle the back action **/
    public void handleBackAction(){
        if (bextras.containsKey("residenceId"))
        {
            Bundle btores = new Bundle();
            btores.putBoolean("type", user);
            btores.putInt("residenceId", residenceId);
            Utils.goToActivity(MessageActivity.this, ViewHostProfileActivity.class, btores);
        }
        else if (bextras.containsKey("conversationId")) {
            Utils.manageBackButton(MessageActivity.this, InboxActivity.class, user);
        }
        else
        {
            Utils.manageBackButton(MessageActivity.this, (user)?HomeActivity.class:HostActivity.class, user);
        }
    }

    /** Back action by pressing the back button of the smartphone **/
    @Override
    public void onBackPressed() {
        handleBackAction();
    }
}