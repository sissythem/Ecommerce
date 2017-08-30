package util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fromRESTful.Reservations;
import gr.uoa.di.airbnbproject.R;

import static util.Utils.CANCEL_RESERVATION_ACTION;
import static util.Utils.CONTACT_HOST_ACTION;
import static util.Utils.CONTACT_USER_ACTION;
import static util.Utils.FORMAT_DATE_DMY;
import static util.Utils.VIEW_RESIDENCE_ACTION;
import static util.Utils.convertTimestampToDateStr;
/** RecyclerView and CardView for Reservations used in HistoryReservationsActivity **/
public class RecyclerAdapterReservations extends RecyclerView.Adapter<RecyclerAdapterReservations.ReservationsCardHolder> {
    Context context;
    Boolean user, residenceExists;
    ArrayList<Reservations> reservations = new ArrayList<>();

    public ArrayList<Reservations> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservations> reservations) {
        this.reservations = reservations;
    }

    public RecyclerAdapterReservations(Context context, Boolean user, ArrayList<Reservations> reservations, Boolean residenceExists) {
        this.context            = context;
        this.user               = user;
        this.reservations       = reservations;
        this.residenceExists    = residenceExists;
    }

    @Override
    public ReservationsCardHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_reservations, parent, false);
        return new ReservationsCardHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationsCardHolder holder, final int position) {
        Utils.loadProfileImage(context, holder.rPhoto, reservations.get(position).getTenantId().getPhoto());
        holder.rTitle.setText(reservations.get(position).getResidenceId().getTitle());
        holder.rPlace.setText(reservations.get(position).getResidenceId().getCity() + ", " + reservations.get(position).getResidenceId().getCountry());
        holder.rUsername.setText(reservations.get(position).getTenantId().getUsername());

        String startDate = convertTimestampToDateStr(reservations.get(position).getStartDate(), FORMAT_DATE_DMY);
        String endDate = convertTimestampToDateStr(reservations.get(position).getEndDate(), FORMAT_DATE_DMY);
        holder.rPeriod.setText(startDate + "-" + endDate);

        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Reservation Options");

                if (user) {
                    menu.add(0, position, 0, VIEW_RESIDENCE_ACTION);
                    menu.add(0, position, 1, CONTACT_HOST_ACTION);
                    menu.add(0, position, 2, CANCEL_RESERVATION_ACTION);
                } else {
                    menu.add(0, position, 1, CONTACT_USER_ACTION);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ReservationsCardHolder extends RecyclerView.ViewHolder {
        CardView rCardView;
        ImageView rPhoto;
        TextView rTitle, rPlace, rUsername, rPeriod;

        public ReservationsCardHolder(final View itemView) {
            super(itemView);

            rCardView   = (CardView) itemView.findViewById(R.id.rrCardView);
            rPhoto      = (ImageView) itemView.findViewById(R.id.profilepic);
            rTitle      = (TextView) itemView.findViewById(R.id.title);
            rPlace      = (TextView) itemView.findViewById(R.id.place);
            rUsername   = (TextView) itemView.findViewById(R.id.username);
            rPeriod     = (TextView) itemView.findViewById(R.id.period);
        }

        public void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }
    }
}
