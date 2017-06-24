package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RestCallManager extends AsyncTask<RestCallParameters, Integer, ArrayList<Object>> {
    static final String TAG = "REST_CALL_MANAGER";
    static final int TIMEOUT_SECONDS = 1000;
    ArrayList<Object> Responses=null;
    RestCallParameters[] Params;

    protected ArrayList<Object> doInBackground(RestCallParameters... parameters) {
        Responses = new ArrayList<>();
        Params = parameters;

        int i;
        for (i=0; i<parameters.length;i++){
            try {
                Log.i(TAG,"Calling " + parameters[i].getRequestType() + " rest with data: " + parameters[i].getparameters());

                if(parameters[i].getRequestType().equals("GET")) {
                    if(parameters[i].getCallResource().equals("STREAM"))
                        Responses.add(sendGETStream(parameters[i].getUrl()));
                    else
                        Responses.add(sendGET(parameters[i].getUrl()));
                }
                else if(parameters[i].getRequestType().equals("POST")) {
                    Responses.add(sendPOST(parameters[i].getparameters(), parameters[i].getUrl()));
                    if(parameters[i].getReturnType().isEmpty())
                        Responses.set(i,"OK");
                }
                else if(parameters[i].getRequestType().equals("PUT")) {
                    Responses.add(sendPUT(parameters[i].getparameters(), parameters[i].getUrl()));
                    if(parameters[i].getReturnType().isEmpty()) Responses.set(i,"OK");
                }
                else if(parameters[i].getRequestType().equals("DELETE")){
                    Responses.add(sendDELETE( parameters[i].getUrl()));

                }

            } catch (IOException e) {
                Responses.add("ERROR");
            } catch (Exception e) {
                Responses.add("ERROR");
            }
        }
        return Responses;
    }

    protected void onProgressUpdate(Integer... progress) {}

    protected void onPostExecute(ArrayList<String> result) {
        long id  = Thread.currentThread().getId(); // todo why the fuck debug fucks up
    }

    public ArrayList<JSONObject> getSingleJSONArray() {
        ArrayList<Object> ResponsesToGet = new ArrayList<>();
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

        if(!Params[0].getReturnType().equals(RestCallParameters.DATA_TYPE.JSON.toString())) return jsonResult;

        // parse json
        String resp = (String) ResponsesToGet.get(0);
        try {
            JSONArray arr = ((new JSONArray(resp)));
            for(int i=0;i< arr.length(); ++i) {
                Object o = arr.get(i);
                jsonResult.add((JSONObject) o);
            }
        } catch (JSONException e) {
            try {
                JSONObject jsonObject = ((new JSONObject(resp)));
                jsonResult.add(jsonObject);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return jsonResult;
    }

    public ArrayList<Object> getRawResponse (){
        ArrayList<Object> Response = new ArrayList<>();
        // call has not been done
        try {
            Response = this.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return Response;
    }

    public Bitmap getSingleBitmap(){
        ArrayList<Object> Response = new ArrayList<>();
        // call has not been done
        try {
            Response = this.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream((InputStream) Response.get(0));

        return bm;
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

    public static InputStream sendGETStream(String address) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try {
            InputStream instr = conn.getInputStream();
            return instr;
        }catch( Exception ex)
        {
            ex.printStackTrace();
        }
        if(conn!=null) conn.disconnect();
        return null;
    }

    public static String sendPOST(String payload, String address) {
        URL url;
        HttpURLConnection connection = null;
        String resp = "";
        try {
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
        } catch(MalformedURLException exc) {
            System.err.println("Malformed event processing URL:\n\t" + address);
            System.err.println("payload/address: ["+payload+"] , ["+address+"]");

            resp="";
        } catch(IOException exc) {
            System.err.println(exc.getMessage());
            System.err.println("payload/address: ["+payload+"] , ["+address+"]");
            exc.printStackTrace();
            resp="";
        } finally {
            if(connection != null) connection.disconnect();
        }
        return resp;
    }

    public static String sendPUT(String payload, String address) {
        URL url;
        HttpURLConnection connection = null;
        String resp = "";
        try {
            // open connection, set JSONic properties
            url = new URL(address);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("PUT");
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
        catch(MalformedURLException exc) {
            System.err.println("Malformed event processing URL:\n\t" + address);
            System.err.println("payload/address: ["+payload+"] , ["+address+"]");
            resp="";
        }
        catch(IOException exc) {
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
    public static String sendDELETE (String address){
        URL url = null;
        String resp = "";
        try {
            url = new URL(address);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("DELETE");
            int respcode = httpURLConnection.getResponseCode();
            if(respcode >= 400) {
                InputStream error = httpURLConnection.getErrorStream();
                resp = readStream(error);
                throw  new MalformedURLException(resp);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            return "ERROR";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
        finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
        }
        resp = "OK";
        return resp;
    }

    static String readStream(InputStream istr) throws IOException {
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