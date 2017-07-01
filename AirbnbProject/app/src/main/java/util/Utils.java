package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gr.uoa.di.airbnbproject.GreetingActivity;
import gr.uoa.di.airbnbproject.HomeActivity;
import gr.uoa.di.airbnbproject.HostActivity;
import gr.uoa.di.airbnbproject.InboxActivity;
import gr.uoa.di.airbnbproject.ProfileActivity;
import gr.uoa.di.airbnbproject.R;

public class Utils {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    public static String USER_LOGIN_PREFERENCES = "login_preferences";

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
        //final Boolean this_user = user;
        final Boolean this_user = true;

        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

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
                    homeIntent.putExtras(buser);
                    this_context.startActivity(homeIntent);
                } else {
                    Intent hostIntent = new Intent(this_context, HostActivity.class);
                    buser.putBoolean("type", false);
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
//                buser.putString("token", token);
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
                Bundle buser = new Bundle();
                if(this_user == false) {
                    Intent homeIntent = new Intent(this_context, HomeActivity.class);
                    buser.putBoolean("type", true);
                    homeIntent.putExtras(buser);
                    this_context.startActivity(homeIntent);
                } else {
                    Intent hostIntent = new Intent(this_context, HostActivity.class);
                    buser.putBoolean("type", false);
                    hostIntent.putExtras(buser);
                    this_context.startActivity(hostIntent);
                }

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
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();
        context.finish();
        Intent greetingintent = new Intent(context, GreetingActivity.class);
        context.startActivity(greetingintent);
    }

    public static void manageBackButton(Activity context, Class newContext, boolean user) {
        final Activity this_context = context;
        final Class this_new_context = newContext;
        final boolean this_user = user;

        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        ImageButton bback = (ImageButton) this_context.findViewById(R.id.ibBack);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent(this_context, this_new_context);
                Bundle buser = new Bundle();
                buser.putBoolean("type", this_user);
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

    public static Session getSessionData(Activity context) {
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        Session sessionData = new Session();

        sessionData.setToken(sharedPrefs.getString("token", ""));
        sessionData.setUsername(sharedPrefs.getString("username", "")); //currentLoggedInUser
        sessionData.setUserLoggedInState(sharedPrefs.getBoolean("userLoggedInState", false));
        return sessionData;
    }

    public static Session updateSessionData(Activity context, Session sessionData) {
        SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("token", sessionData.getToken());
        editor.putString("username", sessionData.getUsername());
        editor.putBoolean("userLoggedInState", sessionData.getUserLoggedInState());
        editor.commit();

        return sessionData;
    }

    public static void manageSharedPreferences() {}

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
                e.printStackTrace();
                return "";
            }
        }
        return result;
    }

    /*
    //addPermission method, used below for Runtime Permissions. Checks if permission is already approved by the user
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    //method used for runtime permissions as well, message shown to user in order to approve permissions
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //Below method is used when multiple permission are asked, in this case was not necessary to use this method
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for permissions given
                if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(HomeActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getPermissions(){
        //Runtime permissions
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            List<String> permissionsNeeded = new ArrayList<>();

            final List<String> permissionsList = new ArrayList<>();

            if (!addPermission(permissionsList, Manifest.permission.INTERNET))
                permissionsNeeded.add("Access Internet");
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
                permissionsNeeded.add("Access Network State");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Access External Storage");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write to External Storage");
            if(!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("Access Fine Location");
            if(!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("Access Coarse Location");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    } */
}