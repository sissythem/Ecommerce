package util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gr.uoa.di.airbnbproject.R;

/**
 * Created by sissy on 2/9/2017.
 */

public class MyTextSlider extends MyBaseSliderView {
    public MyTextSlider(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());
        Log.e("","RUnning bind and show ");
        bindEventAndShow(v, target);
        return v;
    }
}
