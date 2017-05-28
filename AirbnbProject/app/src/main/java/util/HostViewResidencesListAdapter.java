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

import gr.uoa.di.airbnbproject.EditResidenceActivity;
import gr.uoa.di.airbnbproject.R;
import gr.uoa.di.airbnbproject.ResidenceActivity;

/**
 * Created by sissy on 27/5/2017.
 */

public class HostViewResidencesListAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] representativePhoto;
    private final String[] city;
    private final Double[] price;
    private final float[] rating;
    private final int[] residenceId;

    public HostViewResidencesListAdapter(Activity context, String[] representativePhoto, String[] city, Double[] price, float[] rating, int[] residenceId) {
        super(context, R.layout.host_residences_list_layout, city);

        this.representativePhoto=representativePhoto;
        this.context=context;
        this.city=city;
        this.price=price;
        this.rating=rating;
        this.residenceId=residenceId;
    }


    public View getView(final int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.host_residences_list_layout, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.representativePhoto);
        TextView tvCity = (TextView) rowView.findViewById(R.id.city);
        TextView tvPrice = (TextView) rowView.findViewById(R.id.price);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.rating);
        ImageButton bedit = (ImageButton) rowView.findViewById(R.id.editResidence);

        //String url = "https://downloadcentrum.com/wp-content/uploads/2017/02/chrome-cookies.png";
        // InputStream responseStream;
        // Bitmap bitmap = RestCalls.getPhoto(url);
        //BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);

        // imageView.setImageBitmap(bitmap);
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
        imageView.setTag(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
