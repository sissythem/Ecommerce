package util;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import fromRESTful.Residences;
import gr.uoa.di.airbnbproject.R;
import gr.uoa.di.airbnbproject.ResidenceActivity;

import static util.Utils.DELETE_ACTION;
import static util.Utils.EDIT_ACTION;
import static util.Utils.RESERVATIONS_ACTION;
import static util.Utils.VIEW_RESIDENCE_ACTION;

public class RecyclerAdapterHostResidences extends RecyclerView.Adapter<RecyclerAdapterHostResidences.ResidencesCardHolder> implements View.OnCreateContextMenuListener{
    Context context;
    Boolean user;
    ArrayList<Residences> residences = new ArrayList<>();
    public RecyclerAdapterHostResidences(Context context, Boolean user, ArrayList<Residences> residences) {
        this.context    = context;
        this.user       = user;
        this.residences = residences;
    }

    @Override
    public ResidencesCardHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_host_residences, parent, false);
        return new ResidencesCardHolder(view);
    }

    @Override
    public void onBindViewHolder(final ResidencesCardHolder holder, final int position) {
        Utils.loadResidenceImage(context, holder.rPhoto, residences.get(position).getPhotos());
        holder.rTitle.setText(residences.get(position).getTitle());
        holder.rCity.setText(residences.get(position).getCity());
        holder.rPrice.setText(Double.toString(residences.get(position).getMinPrice()));
        holder.rRatingBar.setRating((float)residences.get(position).getAverageRating());

        //TODO: fix menu
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle btype = new Bundle();
                btype.putBoolean("type", user);
                btype.putString("source", "host");
                btype.putInt("residenceId", residences.get(position).getId());
                Utils.goToActivity(context, ResidenceActivity.class, btype);
            }
        });
    }

    @Override
    public int getItemCount() {
        return residences.size();
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //menuInfo is null
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Reservations - Host Options");

        menu.add(0, info.position, 0, VIEW_RESIDENCE_ACTION);
        menu.add(0, info.position, 1, EDIT_ACTION);
        menu.add(0, info.position, 2, RESERVATIONS_ACTION);
        menu.add(0, info.position, 3, DELETE_ACTION);
    }


    public static class ResidencesCardHolder extends RecyclerView.ViewHolder {
        CardView rCardView;
        ImageView rPhoto;
        TextView rTitle, rCity, rPrice;
        RatingBar rRatingBar;
        public ResidencesCardHolder(View itemView) {
            super(itemView);

            rCardView   = (CardView) itemView.findViewById(R.id.rHostCardView);
            rPhoto      = (ImageView) itemView.findViewById(R.id.representativePhoto);
            rTitle      = (TextView) itemView.findViewById(R.id.title);
            rCity       = (TextView) itemView.findViewById(R.id.city);
            rPrice      = (TextView) itemView.findViewById(R.id.price);
            rRatingBar  = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }
}
