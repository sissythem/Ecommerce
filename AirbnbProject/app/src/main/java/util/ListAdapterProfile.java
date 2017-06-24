package util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

public class ListAdapterProfile extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] userdetails;

    public ListAdapterProfile(Activity context, String[] userdetails) {
        super(context, R.layout.list_profile, userdetails);
        this.context=context;
        this.userdetails=userdetails;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_profile, null, true);
        try{
            TextView tvuserinput = (TextView) rowView.findViewById(R.id.userdetails);
            tvuserinput.setText(userdetails[position]);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return rowView;
    }
}
