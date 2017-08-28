package util;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import fromRESTful.Residences;
import gr.uoa.di.airbnbproject.HostActivity;
import gr.uoa.di.airbnbproject.R;
import gr.uoa.di.airbnbproject.ResidenceActivity;

import static util.Utils.DELETE_ACTION;
import static util.Utils.EDIT_ACTION;
import static util.Utils.RESERVATIONS_ACTION;
import static util.Utils.VIEW_RESIDENCE_ACTION;

/** RecyclerView and CardView for Residences used in HomeActivity and HostActivity **/
public class RecyclerAdapterResidences extends RecyclerView.Adapter<RecyclerAdapterResidences.ResidencesCardHolder> {
    Context context;
    Boolean user;
    ArrayList<Residences> residences = new ArrayList<>();
    int guests;
    String startDate, endDate;
    public void setSearchList(ArrayList<Residences> residences) {this.residences = residences;}
    public void setGuests(int guests){this.guests=guests;}
    public void setStartDate(String startDate){this.startDate=startDate;}
    public void setEndDate(String endDate){this.endDate=endDate;}

    public RecyclerAdapterResidences(Context context, Boolean user, ArrayList<Residences> residences, int guests, String startDate, String endDate) {
        this.context    = context;
        this.user       = user;
        this.residences = residences;
        this.guests     = guests;
        this.startDate  = startDate;
        this.endDate    = endDate;
    }

    @Override
    public ResidencesCardHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_residences, parent, false);
        return new ResidencesCardHolder(view);
    }

    @Override
    public void onBindViewHolder(ResidencesCardHolder holder, final int position) {
        Utils.loadResidenceImage(context, holder.rPhoto, residences.get(position).getPhotos());
        holder.rTitle.setText(residences.get(position).getTitle());
        holder.rCity.setText(residences.get(position).getCity());
        holder.rPrice.setText(Double.toString(residences.get(position).getMinPrice()));
        holder.rRatingBar.setRating((float)residences.get(position).getAverageRating());

        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (context.getClass().equals(HostActivity.class)) {
                    menu.setHeaderTitle("Reservations - Host Options");

                    menu.add(0, position, 0, VIEW_RESIDENCE_ACTION);
                    menu.add(0, position, 1, EDIT_ACTION);
                    menu.add(0, position, 2, RESERVATIONS_ACTION);
                    menu.add(0, position, 3, DELETE_ACTION);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle btype = new Bundle();
                btype.putBoolean("type", user);
                btype.putString("source", "home");
                btype.putInt("residenceId", residences.get(position).getId());
                btype.putString("guests", Integer.toString(guests));
                btype.putString("startDate", startDate);
                btype.putString("endDate", endDate);
                Utils.goToActivity(context, ResidenceActivity.class, btype);
            }
        });
    }

    @Override
    public int getItemCount() {
        return residences.size();
    }

    public static class ResidencesCardHolder extends RecyclerView.ViewHolder
    {
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

        public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }
    }
}
