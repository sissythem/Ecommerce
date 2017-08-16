package util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

public class ListAdapterReservations extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] residenceTitle;
    private final String[] startDate;
    private final String[] endDate;

    public ListAdapterReservations(Activity context, String[] residenceTitle, String[] startDate, String[] endDate) {
        super(context, R.layout.list_reservations_history, residenceTitle);
        this.context=context;
        this.residenceTitle=residenceTitle;
        this.startDate=startDate;
        this.endDate=endDate;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_reservations_history, null, true);

        TextView tvResidenceTitle = (TextView) rowView.findViewById(R.id.residenceTitle);
        TextView tvPeriod  = (TextView) rowView.findViewById(R.id.period);
        tvResidenceTitle.setText(residenceTitle[position]);
        tvPeriod.setText(startDate[position] + "-" + endDate[position]);

        return rowView;
    }
}
