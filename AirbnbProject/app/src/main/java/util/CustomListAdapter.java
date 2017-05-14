package util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

/**
 * Created by sissy on 13/5/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] city;
    private final Double[] price;
    private final float[] rating;


    public CustomListAdapter(Activity context, String[] city, Double[] price, float[] rating) {
        super(context, R.layout.list_layout, city);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.city=city;
        this.price=price;
        this.rating=rating;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_layout, null,true);

        //ImageView imageView = (ImageView) rowView.findViewById(R.id.representativePhoto);
        TextView tvCity = (TextView) rowView.findViewById(R.id.city);
        TextView tvPrice = (TextView) rowView.findViewById(R.id.price);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rating);


       // imageView.setImageResource(imgid[position]);
        tvCity.setText(city[position]);
        String[] priceString = new String [price.length];
        for(int i=0; i<price.length;i++){
            priceString[i] = price[i].toString();
        }
        tvPrice.setText(priceString[position]);
        ratingBar.setRating(rating[position]);
        return rowView;

    };
}
