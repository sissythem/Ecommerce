package util;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by sissy on 1/5/2017.
 */

public class RestCallManager extends AsyncTask<RestCallParameters, Integer, ArrayList<String>> {
    static final int TIMEOUT_SECONDS = 1000;
    ArrayList<String> Responses=null;
    RestCallParameters[] Params;

    protected ArrayList<String> doInBackground(RestCallParameters... parameters) {
        Responses = new ArrayList<>();
        Params = parameters;

        int i;
        for (i=0; i<parameters.length;i++){
            try {
                if(parameters[i].getRequestType().equals("GET"))
                {
                    Responses.add(sendGET(parameters[i].getUrl()));
                }
                else
                {
                    Responses.add(sendPOST(parameters[i].getparameters(), parameters[i].getUrl()));
                    if(parameters[i].getReturnType().isEmpty())
                        Responses.set(i,"OK");
                }

            } catch (IOException e) {
                Responses.add("ERROR");
            } catch (Exception e) {
                Responses.add("ERROR");
            }
        }
        return Responses;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(ArrayList<String> result) {
        long id  = Thread.currentThread().getId(); // todo why the fuck debug fucks up
    }

    public ArrayList<JSONObject> getSingleJSONArray()
    {
        ArrayList<String> ResponsesToGet = new ArrayList<>();

        // call has not been done
        try {
            ResponsesToGet = this.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }



        ArrayList<JSONObject> jsonResult = new ArrayList<>();
        if(ResponsesToGet.isEmpty()) return jsonResult;


        if( ! Params[0].getReturnType().equals(RestCallParameters.DATA_TYPE.JSON.toString())) return jsonResult;

        // parse json
        String resp = ResponsesToGet.get(0);
        try {
            JSONArray arr = ((new JSONArray(resp)));
            for(int i=0;i< arr.length(); ++i)
            {
                Object o = arr.get(i);
                jsonResult.add((JSONObject) o);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }

         return jsonResult;
    }

    public static String sendGET(String address) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = null;
        try {
            InputStream instr = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(instr);
            rd = new BufferedReader(isr);
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }catch( Exception ex)
        {
            ex.printStackTrace();
        }
        rd.close();
        if(conn!=null) conn.disconnect();
        return result.toString();
    }

    public static String sendPOST(String payload, String address)
    {
        URL url;
        HttpURLConnection connection = null;
        String resp = "";
        try
        {
            // open connection, set JSONic properties
            url = new URL(address);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(payload.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(payload);
//            String encodedPayload = URLEncoder.encode(payload,"UTF-8");
//            wr.write(encodedPayload.getBytes());


            wr.close();
            //Get Responses
            int status = connection.getResponseCode();
            if(status >= 400)
            {
                InputStream error = connection.getErrorStream();
                resp = readStream(error);
                throw  new MalformedURLException(resp);
            }

            InputStream is = connection.getInputStream();
            resp = readStream(is);

            //System.out.println("server Responses:\n\t" + resp);
        }
        catch(MalformedURLException exc)
        {
            System.err.println("Malformed event processing URL:\n\t" + address);
            System.err.println("payload/address: ["+payload+"] , ["+address+"]");

            resp="";
        }
        catch(IOException exc)
        {

            System.err.println(exc.getMessage());
            System.err.println("payload/address: ["+payload+"] , ["+address+"]");
            exc.printStackTrace();
            resp="";
        }
        finally
        {
            if(connection != null)
                connection.disconnect();
        }
        return resp;
    }

    static   String readStream(InputStream istr) throws IOException {
        String resp = "";
        BufferedReader rd = new BufferedReader(new InputStreamReader(istr));
        // parse to string
        StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
        String line;
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        resp = response.toString();
        rd.close();
        return resp;
    }
}


