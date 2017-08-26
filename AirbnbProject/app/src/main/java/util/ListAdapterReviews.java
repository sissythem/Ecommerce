package util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;
/** ListAdapter for Reviews and HistoryReviews activities **/
public class ListAdapterReviews extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] representativePhoto;
    private final String[] name;
    private final String[] comment;
    private final double[] rating;

    public ListAdapterReviews(Activity context, String[] representativePhoto, String[] name, String[] comment, double[] rating)
    {
        super(context, R.layout.list_reviews, name);
        this.representativePhoto    = representativePhoto;
        this.context                = context;
        this.name                   = name;
        this.comment                = comment;
        this.rating                 = rating;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_reviews, null, true);

        ImageView userImage = (ImageView) rowView.findViewById(R.id.profilepic);
        Utils.loadProfileImage(context, userImage, representativePhoto[position]);

        TextView tvUsername = (TextView) rowView.findViewById(R.id.name);
        TextView tvComment  = (TextView) rowView.findViewById(R.id.comment);
        RatingBar tvRatingBar  = (RatingBar) rowView.findViewById(R.id.rating);

        tvUsername.setText(name[position]);
        tvComment.setText(comment[position]);
        tvRatingBar.setRating((float)rating[position]);

        return rowView;
    }
}
