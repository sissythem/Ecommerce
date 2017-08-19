package util;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import fromRESTful.Residences;
import gr.uoa.di.airbnbproject.R;
import gr.uoa.di.airbnbproject.ResidenceActivity;

import static util.Utils.goToActivity;

public class RecyclerAdapterResidences extends RecyclerView.Adapter<RecyclerAdapterResidences.ResidencesCardHolder> {
    Context context;
    Boolean user;
    ArrayList<Residences> residences = new ArrayList<>();
    public RecyclerAdapterResidences(Context context, Boolean user, ArrayList<Residences> residences) {
        this.context    = context;
        this.user       = user;
        this.residences = residences;
    }

    @Override
    public ResidencesCardHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ResidencesCardHolder(view);
    }

    @Override
    public void onBindViewHolder(ResidencesCardHolder holder, final int position) {
        Utils.loadResidenceImage(context, holder.rPhoto, residences.get(position).getPhotos());
        holder.rTitle.setText(residences.get(position).getTitle());
        holder.rCity.setText(residences.get(position).getCity());
        holder.rPrice.setText(Double.toString(residences.get(position).getMinPrice()));
        holder.rRatingBar.setRating((float)residences.get(position).getAverageRating());

        holder.rCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Implement onClick **/
                System.out.println("Clicked");

                Bundle btype = new Bundle();
                btype.putBoolean("type", user);
                btype.putInt("residenceId", residences.get(position).getId());
                goToActivity(context, ResidenceActivity.class, btype);
            }
        });
    }

    @Override
    public int getItemCount() {
        return residences.size();
    }

    public static class ResidencesCardHolder extends RecyclerView.ViewHolder {
        CardView rCardView;
        ImageView rPhoto;
        TextView rTitle, rCity, rPrice;
        RatingBar rRatingBar;
        public ResidencesCardHolder(View itemView) {
            super(itemView);

            rCardView   = (CardView) itemView.findViewById(R.id.rCardView);
            rPhoto      = (ImageView) itemView.findViewById(R.id.representativePhoto);
            rTitle      = (TextView) itemView.findViewById(R.id.title);
            rCity       = (TextView) itemView.findViewById(R.id.city);
            rPrice      = (TextView) itemView.findViewById(R.id.price);
            rRatingBar  = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }
}
