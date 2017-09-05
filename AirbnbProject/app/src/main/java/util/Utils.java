package util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import gr.uoa.di.airbnbproject.GreetingActivity;
import gr.uoa.di.airbnbproject.HomeActivity;
import gr.uoa.di.airbnbproject.HostActivity;
import gr.uoa.di.airbnbproject.InboxActivity;
import gr.uoa.di.airbnbproject.ProfileActivity;
import gr.uoa.di.airbnbproject.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static util.RestClient.BASE_URL;

public class Utils {

    /** SHARED PREFERENCES TITLE FOR LOGGED IN USER **/
    public static String USER_LOGIN_PREFERENCES         = "login_preferences";
    public static String workerPrefStr                  = "WORKER_PREFS";

    /** DATE FORMATS **/
    public static final String FORMAT_DATE_YMD          = "yyyy-MM-dd";
    public static final String FORMAT_DATE_DMY          = "dd-MM-yyyy";
    public static final String FORMAT_DATETIME_DMY_HMS  = "dd-MM-yyyy'T'HH:mm:ss";
    public static final String FORMAT_DATE_DM           = "dd MMMM";
    public static final String FORMAT_DATE_DB           = "Y-m-d H:i:s";

    /** Message & Conversation Constants **/
    public static final String USER_SENDER              = "sender";
    public static final String USER_RECEIVER            = "receiver";

    /** Options Menus on ContextMenu **/
    public static final String EDIT_ACTION                  = "Edit";
    public static final String DELETE_ACTION                = "Delete";
    public static final String COPY_ACTION                  = "Copy";
    public static final String RESERVATIONS_ACTION          = "Reservations";
    public static final String VIEW_RESIDENCE_ACTION        = "View Residence";
    public static final String OPEN_MESSAGES_ACTION         = "Open Messages";
    public static final String CONTACT_HOST_ACTION          = "Contact Host";
    public static final String CONTACT_USER_ACTION          = "Contact User";
    public static final String CANCEL_RESERVATION_ACTION    = "Cancel Reservation";
    public static final String SET_AS_MAIN_IMAGE_ACTION     = "Set as Main Image";


    /** When an activity starts, it is checked if token is expired in order to terminate the session **/
    public static boolean isTokenExpired(String token) {
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        boolean isExpired = retrofitCalls.isTokenExpired(token);
        return isExpired;
    }

    /** Convert date format **/
    public static Date ConvertStringToDate(String date, String format) {
        Date convertedDate= Calendar.getInstance().getTime();
        try {
            DateFormat df = new SimpleDateFormat(format);
            convertedDate = df.parse(date);
        } catch (ParseException e) {
            Log.i("",e.getMessage());
        }
        catch (Exception e){
            Log.i("",e.getMessage());
        }
        return convertedDate;
    }

    public static String ConvertDateToString (Date date, String format){
        String stringDate="";
        try{
            DateFormat dateFormat = new SimpleDateFormat(format);
            stringDate = dateFormat.format(date);
        } catch (Exception e){
            Log.i("",e.getMessage());
        }
        return stringDate;
    }

    public static long convertDateToMillisSec(Date date, String format) {
        return convertDateToMillisSec(ConvertDateToString(date, format), format);
    }

    public static long convertDateToMillisSec(String date, String format) {
        long milliseconds = 0;
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            Date d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public static String convertTimestampToDateStr(long milliseconds, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String finaldate;
        switch (format) {
            case FORMAT_DATE_DMY:
                finaldate = mDay + "-" + (mMonth+1) + "-" + mYear;
                break;
            case FORMAT_DATE_YMD:
            default:
                finaldate = mYear + "-" + (mMonth+1) + "-" + mDay;
                break;
        }
        return finaldate;
    }

    public static Date convertTimestampToDate(long milliseconds, String format) {
        String strDate = convertTimestampToDateStr(milliseconds, format);
        Date date = ConvertStringToDate(strDate, format);
        return date;
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
        } catch (ParseException e) {
            Log.i("",e.getMessage());
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

    /** Navigation using the footer toolbar **/
    /** Check the user role each time **/
    public static void manageFooter(final Activity context, Boolean user) {
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
                Bundle buser = new Bundle();
                if(this_user == true) {
                    Intent homeIntent = new Intent(this_context, HomeActivity.class);
                    buser.putBoolean("type", true);
                    buser.putString("source", this_context.toString());
                    homeIntent.putExtras(buser);
                    this_context.startActivity(homeIntent);
                } else {
                    Intent hostIntent = new Intent(this_context, HostActivity.class);
                    buser.putBoolean("type", false);
                    buser.putString("source", this_context.toString());
                    hostIntent.putExtras(buser);
                    this_context.startActivity(hostIntent);
                }

            }
        });

        binbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxintent = new Intent(this_context, InboxActivity.class);
                Bundle buser = new Bundle();
                buser.putBoolean("type", this_user);
                buser.putString("source", this_context.toString());
                inboxintent.putExtras(buser);
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
                buser.putString("source", this_context.toString());
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
                new AlertDialog.Builder(context)
                        .setTitle("Switch Role")
                        .setMessage("You switch role as " + (!this_user ? "User":"Host"))
                        .setIcon(R.drawable.ic_profile)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent newIntent;
                                Bundle buser = new Bundle();
                                if(this_user == false) {
                                    newIntent = new Intent(this_context, HomeActivity.class);
                                    buser.putBoolean("type", true);
                                    buser.putString("source", this_context.toString());
                                } else {
                                    newIntent = new Intent(this_context, HostActivity.class);
                                    buser.putBoolean("type", false);
                                    buser.putString("source", this_context.toString());
                                }

                                newIntent.putExtras(buser);
                                this_context.startActivity(newIntent);
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
            }
        });

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Show confirmation message to user in order to logout **/
                new AlertDialog.Builder(context)
                    .setTitle("Logout").setMessage("Are you sure you want to logout from the application?").setIcon(android.R.drawable.ic_lock_lock)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            logout(this_context);
                            return;
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    /** MANAGE LOGOUT ACTION **/
    /** SharedPreferences are cleared and user is redirected in the GreetingActivity **/
    public static void logout(Activity context)
    {
        /** Clear worker notifications **/
        runWorker(context, false);
        callAsynchronousTask(context, null, null);

    /* Reset Shared Preferences */
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();

        // reset the retrofit client - the current one's key has to be deleted
        // since the getClient initialization was done only if retrofit was null, we set it to null.
        RestClient.resetClient();

        Intent greetingintent = new Intent(context, GreetingActivity.class);
        greetingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(greetingintent);
        context.finish();
    }
    /** MANAGE BACK TOOLBAR **/
    public static void manageBackButton(Activity context, Class newContext, boolean user) {
        final Activity this_context = context;
        final Class this_new_context = newContext;
        final boolean this_user = user;

        Intent backintent = new Intent(this_context, this_new_context);
        Bundle buser = new Bundle();
        buser.putBoolean("type", this_user);
        if (this_new_context.getClass().toString().equals(HomeActivity.class.toString()))
        {
            buser.putBoolean("type", true);
        }
        else if (this_new_context.toString().equals(HostActivity.class.toString()))
        {
            buser.putBoolean("type", false);
        }
        backintent.putExtras(buser);

        try {
            this_context.startActivity(backintent);
            this_context.finish();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    /** Methods for going back or reload and activity **/
    public static void goToActivity(Context context, Class newIntentClass, Bundle extras) {
        Intent newIntent = new Intent(context, newIntentClass);
        newIntent.putExtras(extras);
        try {
            context.startActivity(newIntent);
            ((Activity) context).finish();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void reloadActivity(Context context, Bundle extras) {
        Intent currentIntent = ((Activity) context).getIntent();

        if (extras != null) {
            currentIntent.putExtras(extras);
        }

        try {
            context.startActivity(currentIntent);
            ((Activity) context).finish();
        } catch (Exception e) {
            Log.e("",e.getMessage());
        }
    }

    /** Session Data contains the username, the token and the user login state **/
    /** Checks if user is logged in **/
    public static Session getSessionData(Activity context) {
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        Session sessionData = new Session(sharedPrefs.getString("token", ""), sharedPrefs.getString("username", ""),
                sharedPrefs.getBoolean("userLoggedInState", false));
        return sessionData;
    }
    /** Initialization of SharedPreferences when user selects to register or to login **/
    public static Session updateSessionData(Activity context, Session sessionData) {
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("token", sessionData.getToken());
        editor.putString("username", sessionData.getUsername());
        editor.putBoolean("userLoggedInState", sessionData.getUserLoggedInState());
        editor.commit();

        return sessionData;
    }

    /** Used in Map URL **/
    public static String encodeParameterizedURL(ArrayList<String> paramNames, ArrayList<String> paramValues) {
        if(paramNames.size() != paramValues.size()) {
            System.err.printf("Unequal number of params + values : %d vs %d \n",paramNames.size(), paramValues.size());
            return "";
        }
        String result = "";
        for(int i=0;i<paramNames.size(); ++i) {
            if(i==0) result += "?";
            else result += "&";
            result += paramNames.get(i) + "=";

            try {
                result += java.net.URLEncoder.encode(paramValues.get(i), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.err.println("Failed to encode URL parameter [" + paramValues.get(i) + "]");
                Log.i("",e.getMessage());
                return "";
            }
        }
        return result;
    }
    /** MANAGE IMAGE UPLOAD**/
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void loadProfileImage(Context context, ImageView imgView, String imgName) {
        String imgpath = BASE_URL + "images/img/" + imgName;
        PicassoTrustAll.getInstance(context).load(imgpath)
                .placeholder(R.drawable.ic_profile)
                .transform(new CircleTransform())
                .error(R.drawable.ic_profile)
                .resize(200, 200)
                .into(imgView);
    }

    public static void loadResidenceImage(Context context, ImageView imgView, String imgName) {
        String imgpath = BASE_URL + "images/img/" + imgName;
        PicassoTrustAll.getInstance(context).load(imgpath)
                .placeholder(R.drawable.ic_upload_image)
                .error(R.drawable.ic_upload_image)
                .resize(300, 300)
                .into(imgView);
    }

    /** MANAGE MESSAGES AND NOTIFICATIONS **/
    public static boolean workerIsRunning(Context context) {
        SharedPreferences workerPrefs = context.getApplicationContext().getSharedPreferences(workerPrefStr, Context.MODE_PRIVATE);

        boolean workerExists = workerPrefs.contains("workerRuns");
        boolean workerVar = workerPrefs.getBoolean("workerRuns", false);
        if (workerExists && workerVar) return true;
        return false;
    }

    public static void runWorker(Context context, boolean workerRuns) {
        SharedPreferences workerPrefs = context.getApplicationContext().getSharedPreferences(workerPrefStr, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = workerPrefs.edit();
        if (!workerRuns) {
            editor.clear();
        } else {
            editor.putBoolean("workerRuns", workerRuns);
        }
        editor.commit();
    }

    protected static Integer getNewMessages(String token, Integer userId) {
        RetrofitCalls retrofitCalls = new RetrofitCalls();
        Integer countMsg = retrofitCalls.countNewMessages(token, userId);
        return countMsg;
    }

    public static NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;


    public static TimerTask setupAsyncTask(final Context context, final String token, final Integer userId) {
        final Handler handler = new Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                if (!workerIsRunning(context)) {
                    cancel();
                }
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (token!=null && userId!=null) {
                                notification = new NotificationCompat.Builder(context);
                                notification.setAutoCancel(true);
                                Integer unreadConversations = getNewMessages(token, userId);
                                if (unreadConversations > 0) {
                                    buckysButtonClicked(context,
                                            HomeActivity.class,
                                            InboxActivity.class,
                                            "New messages",
                                            "Residence Conversation",
                                            "You have new messages in " + unreadConversations + " " + (unreadConversations == 1 ? "conversation" : "conversations")
                                    );
                                }
                            }

//                            PerformBackgroundTask performBackgroundTask = new PerformBackgroundTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
//                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        return doAsynchronousTask;
    }

    public static void callAsynchronousTask(Context context, String token, Integer userId) {
        Timer timer = new Timer();

        if (!workerIsRunning(context)) {
            timer.cancel();
        } else {
            timer.schedule(setupAsyncTask(context, token, userId), 0, 240000); //execute in every 240000 (4 minutes)
        }
    }

    public static void buckysButtonClicked(Context context, Class newContextClass, Class newIntentClass, String ticker, String title, String text){
        //Build the notification
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker(ticker);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(title);
        notification.setContentText(text);
        notification.setPriority(Notification.PRIORITY_HIGH);
        notification.setDefaults(Notification.DEFAULT_ALL);

        Intent intent = new Intent(context, newContextClass);
        Intent newIntent = new Intent(context, newIntentClass);
        newIntent.putExtra("openActivity", newIntentClass);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds notification and issues it
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }
}