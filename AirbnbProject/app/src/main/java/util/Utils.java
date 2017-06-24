package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import gr.uoa.di.airbnbproject.GreetingActivity;
import gr.uoa.di.airbnbproject.HomeActivity;
import gr.uoa.di.airbnbproject.HostActivity;
import gr.uoa.di.airbnbproject.InboxActivity;
import gr.uoa.di.airbnbproject.ProfileActivity;
import gr.uoa.di.airbnbproject.R;

public class Utils {
    public static final String APP_DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";
    public static final String DATABASE_DATETIME = "yyyy-MM-dd'T'hh:mm:ssz";

    public static final String DATE_YEAR_FIRST = "yyyy-MM-dd";
    public static final String DATE_TEXT_MONTH = "dd MMMM";

    public static Date ConvertStringToDate(String date, String format){
        Date convertedDate= Calendar.getInstance().getTime();
        try {
            DateFormat df = new SimpleDateFormat(format);
            convertedDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return convertedDate;
    }
    public static String ConvertDateToString (Date date, String format){
        String stringDate="";
        try{
            DateFormat dateFormat = new SimpleDateFormat(format);
            stringDate = dateFormat.format(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return stringDate;
    }
    public static boolean isFieldOK(JSONObject jsonObject, String name){

        if(!jsonObject.has(name)) return false;
        try {
            if(null == jsonObject.get(name)) return false;

        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public static boolean isThisDateValid(String dateToValidate, String dateFormat){
        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getCurrentDate(String format) {
        String currentdate = new SimpleDateFormat(format).format(new Date());
        return currentdate;
    }

    public static String getDefaultEndDate(String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +7);
        Date defaultenddate = cal.getTime();

        return ConvertDateToString(defaultenddate, format);
    }

    public static String formatDate(String strdate, String format) {
        Date date = ConvertStringToDate(strdate, format);
        String formatteddate = new SimpleDateFormat(format).format(date);
        return formatteddate;
    }

    public static Boolean checkLoggedUser() {



        return true;
    }

    public static void manageFooter(Activity context, Boolean user) {
        final Activity this_context = context;
        final Boolean this_user = user;

        ImageButton bhome = (ImageButton) this_context.findViewById(R.id.home);
        ImageButton binbox = (ImageButton) this_context.findViewById(R.id.inbox);
        ImageButton bprofile = (ImageButton) this_context.findViewById(R.id.profile);
        ImageButton bswitch = (ImageButton) this_context.findViewById(R.id.host);
        ImageButton blogout = (ImageButton) this_context.findViewById(R.id.logout);

        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeintent = new Intent(this_context, HomeActivity.class);
                this_context.startActivity(homeintent);
            }
        });

        binbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxintent = new Intent(this_context, InboxActivity.class);
                Bundle btype = new Bundle();
                btype.putBoolean("type", this_user);
                inboxintent.putExtras(btype);
                try {
                    this_context.startActivity(inboxintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileintent = new Intent(this_context, ProfileActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", this_user);
                profileintent.putExtras(buser);
                try {
                    this_context.startActivity(profileintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        bswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hostintent = new Intent(this_context, HostActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", false);
                hostintent.putExtras(buser);
                this_context.startActivity(hostintent);
            }
        });

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(this_context);
                return;
            }
        });
    }

    /** MANAGE LOGOUT ACTION **/
    public static void logout(Activity context) {
        String USER_LOGIN_PREFERENCES = "login_preferences";
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        Intent greetingintent = new Intent(context, GreetingActivity.class);
        context.startActivity(greetingintent);
    }

    public static void manageBackButton(Activity context, Class newContext) {
        final Activity this_context = context;
        final Class this_new_context = newContext;

        ImageButton bback = (ImageButton) this_context.findViewById(R.id.ibBack);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent(this_context, this_new_context);
                Bundle buser = new Bundle();
                if (this_new_context.getClass().toString() == HomeActivity.class.toString()) {
                    buser.putBoolean("type", true);
                } else if (this_new_context.toString() == HostActivity.class.toString()) {
                    buser.putBoolean("type", false);
                }
                backintent.putExtras(buser);

                try {
                    this_context.startActivity(backintent);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void manageSharedPreferences() {

    }
}