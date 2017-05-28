package util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sissy on 13/5/2017.
 */

public class Utils {
    public static final String APP_DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";
    public static final String DATABASE_DATETIME = "yyyy-MM-dd'T'hh:mm:ssz";
    public static Date ConvertStringToDate (String date, String format){
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

        if( ! jsonObject.has(name)) return false;
        try {
            if( null == jsonObject.get(name)) return false;

        } catch (JSONException e) {
            return false;
        }
        return true;
    }
}
