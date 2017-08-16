package util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import gr.uoa.di.airbnbproject.EditResidenceActivity;
import gr.uoa.di.airbnbproject.HostActivity;
import gr.uoa.di.airbnbproject.R;
import gr.uoa.di.airbnbproject.ResidenceActivity;

import static util.RestClient.BASE_URL;

public class ListAdapterHostResidences extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] representativePhoto;
    private final String[] title;
    private final String[] city;
    private final Double[] price;
    private final float[] rating;
    private final int[] residenceId;

    RetrofitCalls retrofitCalls = new RetrofitCalls();

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
        ImageButton bedit   = (ImageButton) rowView.findViewById(R.id.editResidence);
        ImageButton bdelete = (ImageButton) rowView.findViewById(R.id.deleteResidence);

        tvTitle.setText(title[position]);
        tvCity.setText(city[position]);
        String[] priceString = new String[price.length];
        for (int i = 0; i < price.length; i++) {
            priceString[i] = price[i].toString();
        }
        tvPrice.setText(priceString[position]);
        ratingBar.setRating(rating[position]);

        bedit.setTag(position);
        bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean user = false;
                Intent editResidenceIntent = new Intent(context, EditResidenceActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type", user);
                btype.putInt("residenceId", residenceId[position]);
                editResidenceIntent.putExtras(btype);
                try {
                    context.startActivity(editResidenceIntent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bdelete.setTag(position);
        bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session sessionData = Utils.getSessionData(context);
                System.out.println(sessionData);
                if (retrofitCalls.deleteResidenceById(sessionData.getToken(), Integer.toString(residenceId[position])) == null) {
                    Toast.makeText(context, "Residence was successfully deleted!", Toast.LENGTH_SHORT).show();
                    Intent hostIntent = new Intent(context, HostActivity.class);
                    Bundle btype = new Bundle();
                    hostIntent.putExtras(btype);
                    try {
                        context.startActivity(hostIntent);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong, residence is not deleted. Please try again!", Toast.LENGTH_SHORT).show();
                }



            }
        });

        String imgpath = BASE_URL + "images/img/" + representativePhoto[position];
        com.squareup.picasso.Picasso.with(context).load(imgpath).placeholder(R.mipmap.ic_launcher).resize(300, 300).into(imageView);

        imageView.setTag(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean user=false;
                Intent showResidenceIntent = new Intent(context, ResidenceActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type",user);
                btype.putInt("residenceId", residenceId[position]);
                showResidenceIntent.putExtras(btype);
                try {
                    context.startActivity(showResidenceIntent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        return rowView;
    }
}
