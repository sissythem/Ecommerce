package util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

public class ListAdapterMessages extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer currentUserId;
    private final int[] userid;
    private final String[] name;
    private final String[] body;
    private final String[] timestamp;

    public ListAdapterMessages(Activity context, Integer currentUserId, int[] userid, String[] name, String[] body, String[] timestamp) {
        super(context, R.layout.list_messages, name);

        this.context        = context;
        this.currentUserId  = currentUserId;
        this.userid         = userid;
        this.name           = name;
        this.body           = body;
        this.timestamp      = timestamp;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_messages, null, true);

        TextView msg_name       = (TextView) rowView.findViewById(R.id.msglist_name);
        TextView msg_body       = (TextView) rowView.findViewById(R.id.msglist_body);
        TextView msg_timestamp  = (TextView) rowView.findViewById(R.id.msglist_timestamp);

        msg_name.setText(name[position]);
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
