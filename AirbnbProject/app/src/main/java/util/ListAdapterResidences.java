package util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.HomeActivity;
import gr.uoa.di.airbnbproject.R;

import static util.RestClient.BASE_URL;

public class ListAdapterResidences extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] title;
    private final String[] representativePhoto;
    private final String[] city;
    private final Double[] price;
    private final float[] rating;

    public ListAdapterResidences(Activity context, String[] title, String[] representativePhoto, String[] city, Double[] price, float[] rating) {
        super(context, R.layout.list_layout, city);

        this.title                  = title;
        this.representativePhoto    = representativePhoto;
        this.context                = context;
        this.city                   = city;
        this.price                  = price;
        this.rating                 = rating;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_layout, null,true);

        ImageView resImg = (ImageView) rowView.findViewById(R.id.representativePhoto);
        String imgpath = BASE_URL + "images/img/" + representativePhoto[position];
        com.squareup.picasso.Picasso.with(context).load(imgpath).placeholder(R.mipmap.ic_launcher).resize(200, 200).into(resImg);

        TextView tvTitle = (TextView) rowView.findViewById(R.id.title);
        TextView tvCity = (TextView) rowView.findViewById(R.id.city);
        TextView tvPrice = (TextView) rowView.findViewById(R.id.price);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rating);

        tvTitle.setText(title[position]);
        tvCity.setText(city[position]);
        String[] priceString = new String[price.length];
        for(int i=0; i<price.length;i++){
            priceString[i] = price[i].toString();
        }
        tvPrice.setText(priceString[position]);
        ratingBar.setRating(rating[position]);
        return rowView;
    }
}