package util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

import gr.uoa.di.airbnbproject.R;

/**
 * Created by sissy on 2/9/2017.
 */

public class MyBaseSliderView extends BaseSliderView {
    protected Context myContext;
//    private String myUrl;
    private File myFile;
    private int myRes;
    private ImageLoadListener myLoadListener;
    private Picasso myPicasso;
    private ScaleType myScaleType = ScaleType.Fit;
    protected OnSliderClickListener myOnSliderClickListener;

    protected MyBaseSliderView(Context context) {
        super(context);
        myContext=context;
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    protected void bindEventAndShow(final View v, ImageView targetImageView){
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOnSliderClickListener != null){
                    myOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        if (targetImageView == null) {
            Log.e("MBSV", "Null target image view ");
            return;
        }

        if (myLoadListener != null) {
            myLoadListener.onStart(me);
        }

        myPicasso = PicassoTrustAll.getInstance(myContext);
        RequestCreator rq;
        Log.e("MBSV","Populating rq");

        if(getUrl() !=null){
            rq = myPicasso.load(getUrl());
        }else if(myFile != null){
            rq = myPicasso.load(myFile);
        }else if(myRes != 0){
            rq = myPicasso.load(myRes);
        }else{
            Log.e("MBSV","rq error or something");
            return;
        }

        if(rq == null){
            Log.e("MBSV","Null request creator on mybasesliderview ! Returning..");
            return;
        }

        if(getEmpty() != 0){
            Log.e("MBSV","Gettting empty for url : " + getUrl());
            rq.placeholder(getEmpty());
        }

        if(getError() != 0){
            Log.e("MBSV","Getting error");
            rq.error(getError());
        }

        switch (myScaleType){
            case Fit:
                rq.fit();
                break;
            case CenterCrop:
                rq.fit().centerCrop();
                break;
            case CenterInside:
                rq.fit().centerInside();
                break;
        }

        rq.into(targetImageView,new Callback() {
            @Override
            public void onSuccess() {
                if(v.findViewById(R.id.loading_bar) != null){
                    v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
                }
                Log.e("MBSV","Entered success function with url " + getUrl());
            }

            @Override
            public void onError() {
                if(myLoadListener != null){
                    myLoadListener.onEnd(false,me);
                }
                if(v.findViewById(R.id.loading_bar) != null){
                    v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
                }

                Log.e("MBSV","Entered error function with url " + getUrl());
            }
        });
    }

    @Override
    public Picasso getPicasso() {
        return myPicasso;
    }

    @Override
    public void setPicasso(Picasso picasso) {
        myPicasso = picasso;
    }

}