package util;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

import static util.Utils.USER_RECEIVER;
import static util.Utils.USER_SENDER;
/** ListAdapter for MessageActivity**/
public class ListAdapterMessages extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer currentUserId;
    private final String userType;
    private final int[] userid;
    private final String[] name;
    private final String[] body;
    private final String[] timestamp;
    private final short[] deletedFromSender;
    private final short[] deletedFromReceiver;

    public ListAdapterMessages(Activity context, Integer currentUserId, String userType, int[] userid, String[] name, String[] body, String[] timestamp, short[] deletedFromSender, short[] deletedFromReceiver) {
        super(context, R.layout.list_messages, name);

        this.context                = context;
        this.currentUserId          = currentUserId;
        this.userType               = userType;
        this.userid                 = userid;
        this.name                   = name;
        this.body                   = body;
        this.timestamp              = timestamp;
        this.deletedFromSender      = deletedFromSender;
        this.deletedFromReceiver    = deletedFromReceiver;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_messages, null, true);

        if ((userType == USER_SENDER && deletedFromSender[position] == 1) || (userType == USER_RECEIVER && deletedFromReceiver[position] == 1)) {
            rowView.setVisibility(View.GONE);
        }

        TextView msg_name       = (TextView) rowView.findViewById(R.id.msglist_name);
        TextView msg_body       = (TextView) rowView.findViewById(R.id.msglist_body);
        TextView msg_timestamp  = (TextView) rowView.findViewById(R.id.msglist_timestamp);

        msg_name.setText(name[position]);
        msg_name.setTypeface(null, Typeface.BOLD);
        msg_body.setText(body[position]);
        msg_timestamp.setText(timestamp[position]);

        if (currentUserId == userid[position]) {
            msg_name.setGravity(Gravity.RIGHT);
            msg_body.setGravity(Gravity.RIGHT);
            msg_timestamp.setGravity(Gravity.RIGHT);
        } else {
            msg_name.setGravity(Gravity.LEFT);
            msg_body.setGravity(Gravity.LEFT);
            msg_timestamp.setGravity(Gravity.LEFT);
        }
        return rowView;
    }

}
