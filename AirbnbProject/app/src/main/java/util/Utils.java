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

    public static final String DATE_YEAR_FIRST = "yyyy-MM-dd";
    public static final String DATE_TEXT_MONTH = "dd MMMM";

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
}
