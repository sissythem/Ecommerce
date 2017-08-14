package util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

public class ListAdapterReviews extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] representativePhoto;
    private final String[] username;
    private final String[] comment;

    public ListAdapterReviews(Activity context, String[] representativePhoto, String[] username, String[] comment)
    {
        super(context, R.layout.list_reviews, username);
        this.representativePhoto    = representativePhoto;
        this.context                = context;
        this.username               = username;
        this.comment                = comment;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_reviews, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.profilepic);
        TextView tvUsername = (TextView) rowView.findViewById(R.id.username);
        TextView tvComment  = (TextView) rowView.findViewById(R.id.comment);
        tvUsername.setText(username[position]);
        tvComment.setText(comment[position]);

        return rowView;
    }
}
