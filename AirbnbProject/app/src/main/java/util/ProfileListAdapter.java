package util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

/**
 * Created by sissy on 17/5/2017.
 */

public class ProfileListAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] userdetails;

    public ProfileListAdapter(Activity context, String[] userdetails) {
        super(context, R.layout.profilelist, userdetails);
        this.context=context;
        this.userdetails=userdetails;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.profilelist, null, true);
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
