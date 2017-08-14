package util;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

public class ListAdapterInbox extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] name;
    private final String[] subject;
    private final short[] isRead;

    public ListAdapterInbox(Activity context, String[] name, String[] subject, short[] isRead) {
        super(context, R.layout.list_inbox, name);

        this.context        = context;
        this.name           = name;
        this.subject        = subject;
        this.isRead         = isRead;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_inbox, null, true);

        TextView msg_name       = (TextView) rowView.findViewById(R.id.name);
        TextView msg_subject    = (TextView) rowView.findViewById(R.id.subject);

        if (isRead[position] == 0) {
            msg_name.setTypeface(null, Typeface.BOLD);
            msg_subject.setTypeface(null, Typeface.BOLD);
        }

        msg_name.setText(name[position]);
        msg_subject.setText(subject[position]);
        return rowView;
    }

}