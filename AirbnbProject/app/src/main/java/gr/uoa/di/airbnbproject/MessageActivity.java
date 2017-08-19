package gr.uoa.di.airbnbproject;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import util.ListAdapterMessages;
import util.RetrofitCalls;
import util.Session;
import util.Utils;

import static util.Utils.COPY_ACTION;
import static util.Utils.ConvertStringToDate;
import static util.Utils.FORMAT_DATE_YMD;
import static util.Utils.DELETE_ACTION;
import static util.Utils.USER_RECEIVER;
import static util.Utils.USER_SENDER;
import static util.Utils.getCurrentDate;

public class MessageActivity extends AppCompatActivity {
    Context c;
    Integer currentUserId, toUserId;
    Toolbar toolbar;
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

    String userType, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;

        Session sessionData = Utils.getSessionData(MessageActivity.this);
        token = sessionData.getToken();
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

        setContentView(R.layout.activity_message);

        toolbar = (Toolbar) findViewById(R.id.backToolbar);
        toolbar.setTitle("Send a message");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bextras  = getIntent().getExtras();
        user            = bextras.getBoolean("type");
        currentUserId   = bextras.getInt("currentUserId");
        toUserId        = bextras.getInt("toUserId");
        msgSubject      = bextras.getString("msgSubject");

        subject = (TextView) findViewById(R.id.subject);
        subject.setText(msgSubject);
        body = (EditText) findViewById(R.id.body);

        RetrofitCalls retrofitCalls = new RetrofitCalls();

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

        int[] msguserid             = new int[messagesSize];
        String[] msgname            = new String[messagesSize];
        String[] msgbody            = new String[messagesSize];
        String[] msgtimestamp       = new String[messagesSize];
        short[] deletedFromSender   = new short[messagesSize];
        short[] deletedFromReceiver = new short[messagesSize];

        if (messagesSize > 0) {
            for(int i=0; i < messagesSize; i++) {
                msguserid[i]            = Messages.get(i).getUserId().getId();
                msgname[i]              = Messages.get(i).getUserId().getUsername();
                msgbody[i]              = Messages.get(i).getBody();
                msgtimestamp[i]         = Messages.get(i).getTimestamp().toString();
                deletedFromSender[i]    = Messages.get(i).getDeletedFromSender();
                deletedFromReceiver[i]  = Messages.get(i).getDeletedFromReceiver();
            }
        }

        messageslist = (ListView) findViewById(R.id.messageslist);
        msgadapter = new ListAdapterMessages(this, currentUserId, userType, msguserid, msgname, msgbody, msgtimestamp, deletedFromSender, deletedFromReceiver);
        messageslist.setAdapter(msgadapter);
        registerForContextMenu(messageslist);

        send = (Button)findViewById(R.id.message_send_btn);
        sendMessage();

//        /** BACK BUTTON **/
//        Utils.manageBackButton(this, InboxActivity.class, user);
//        if (bextras.containsKey("residenceId")) {
//            System.out.println("yes");
//            backToResidence();
//        } else {
//            System.out.println("no");
//            Utils.manageBackButton(this, InboxActivity.class, user);
//        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Message Options");

        menu.add(0, info.position, 0, DELETE_ACTION);
        menu.add(0, info.position, 1, COPY_ACTION);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        super.onContextItemSelected(item);
        if (item.getTitle().equals(DELETE_ACTION)) {
            new AlertDialog.Builder(MessageActivity.this)
                .setTitle("Delete Message").setMessage("Do you really want to delete this message?").setIcon(R.drawable.ic_delete)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RetrofitCalls retrofitCalls = new RetrofitCalls();
                        token = retrofitCalls.deleteMessage(token, Messages.get(item.getItemId()).getId(), currentUserId, userType);
                        if (!token.isEmpty() && token!=null && token!="not") {
                            Toast.makeText(c, "Message deleted!", Toast.LENGTH_SHORT).show();
                            reloadConversation();
                        } else if (token.equals("not")) {
                            Toast.makeText(c, "Failed to delete message! Your session has finished, please log in again!", Toast.LENGTH_SHORT).show();
                            Utils.logout(MessageActivity.this);
                            finish();
                        } else {
                            Toast.makeText(c, "Something went wrong, message is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();
        } else if (item.getTitle().equals(COPY_ACTION)) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(Messages.get(item.getItemId()).getBody());
        } else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        Utils.manageBackButton(this, InboxActivity.class, user);
////        moveTaskToBack(true);
//    }

//    public void backToResidence() {
//        ImageButton bback = (ImageButton) this.findViewById(R.id.ibBack);
//        bback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent backintent = new Intent(MessageActivity.this, ResidenceActivity.class);
//                Bundle btores = new Bundle();
//                btores.putBoolean("type", user);
//                btores.putInt("residenceId", residenceId);
//                backintent.putExtras(btores);
//                try {
//                    MessageActivity.this.startActivity(backintent);
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                    ex.printStackTrace();
//                }
//            }
//        });
//    }

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

    public String PostConversationResult(Users senderUser, Users receiverUser, Residences residence, String subject) {
        short val_zero = 0;
        short val_one = 1;
        Conversations ConversationParams = new Conversations(senderUser, receiverUser, residence, subject, val_one, val_zero, val_zero, val_zero);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.startConversation(token, ConversationParams);
        return token;
    }

    public String PostMessageResult(Users mUser, Conversations mConversation, String body) {
        short val_zero = 0;
        String currDate = getCurrentDate(FORMAT_DATE_YMD);
        Messages MessagesParams = new Messages(mUser, mConversation, body, ConvertStringToDate(currDate, FORMAT_DATE_YMD), val_zero, val_zero);
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        token = retrofitCalls.sendMessage(token, MessagesParams);
        return token;
    }
}