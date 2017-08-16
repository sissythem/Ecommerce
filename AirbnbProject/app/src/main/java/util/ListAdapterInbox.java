package util;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import gr.uoa.di.airbnbproject.R;

import static util.Utils.USER_RECEIVER;
import static util.Utils.USER_SENDER;

public class ListAdapterInbox extends ArrayAdapter<String> {
    private final Activity context;
    private final int currentUserId;
    private final String[] userType, name, subject;
    private final short[] readFromSender, readFromReceiver, deletedFromSender, deletedFromReceiver;

    public ListAdapterInbox(Activity context, int currentUserId, String[] userType, String[] name, String[] subject,
                            short[] readFromSender,
                            short[] readFromReceiver,
                            short[] deletedFromSender,
                            short[] deletedFromReceiver) {
        super(context, R.layout.list_inbox, name);

        this.context                = context;
        this.currentUserId          = currentUserId;
        this.userType               = userType;
        this.name                   = name;
        this.subject                = subject;
        this.readFromSender         = readFromSender;
        this.readFromReceiver       = readFromReceiver;
        this.deletedFromSender      = deletedFromSender;
        this.deletedFromReceiver    = deletedFromReceiver;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_inbox, null, true);

        if ((userType[position] == USER_SENDER && deletedFromSender[position] == 1) || (userType[position] == USER_RECEIVER && deletedFromReceiver[position] == 1)) {
            rowView.setVisibility(View.GONE);
        }

        TextView msg_name       = (TextView) rowView.findViewById(R.id.name);
        TextView msg_subject    = (TextView) rowView.findViewById(R.id.subject);

        if ((userType[position] == USER_SENDER && readFromSender[position] != 1) || (userType[position] == USER_RECEIVER && readFromReceiver[position] != 1)) {
            msg_name.setTypeface(null, Typeface.BOLD);
            msg_subject.setTypeface(null, Typeface.BOLD);
        }

        msg_name.setText(name[position]);
        msg_subject.setText(subject[position]);
        return rowView;
    }

}
