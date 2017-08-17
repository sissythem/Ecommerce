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

public class ListAdapterHostResidences extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] representativePhoto;
    private final String[] title;
    private final String[] city;
    private final Double[] price;
    private final float[] rating;
    private final int[] residenceId;

    public ListAdapterHostResidences(Activity context, String[] representativePhoto, String[] title, String[] city, Double[] price, float[] rating, int[] residenceId) {
        super(context, R.layout.list_host_residences, city);

        this.representativePhoto    = representativePhoto;
        this.context                = context;
        this.title                  = title;
        this.city                   = city;
        this.price                  = price;
        this.rating                 = rating;
        this.residenceId            = residenceId;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_host_residences, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.representativePhoto);
        TextView tvTitle    = (TextView) rowView.findViewById(R.id.title);
        TextView tvCity     = (TextView) rowView.findViewById(R.id.city);
        TextView tvPrice    = (TextView) rowView.findViewById(R.id.price);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rating);

        tvTitle.setText(title[position]);
        tvCity.setText(city[position]);
        String[] priceString = new String[price.length];
        for (int i = 0; i < price.length; i++) {
            priceString[i] = price[i].toString();
        }
        tvPrice.setText(priceString[position]);
        ratingBar.setRating(rating[position]);

        Utils.loadResidenceImage(context, imageView, representativePhoto[position]);
        imageView.setTag(position);
        return rowView;
    }
}
