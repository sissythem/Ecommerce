package util;

/**
 * Created by sissy on 1/5/2017.
 */

public class RestCallParameters {
    public enum DATA_TYPE
    {
        JSON("JSON"),TEXT("TEXT");
        String str;
        DATA_TYPE(String str)
        {
            this.str = str;
        }
        @Override
        public String toString(){ return str;}
    }

    String Url;
    String RequestType; //GET or POST
    String ReturnType;  //JSON or Plain Text
    String parameters;


    public RestCallParameters(String url, String requestType, String returnType, String parameters) {
        this.Url = url;
        this.RequestType = requestType;
        this.ReturnType = returnType;
        this.parameters = parameters;

    }

    public String getUrl(){
        return Url;
    }
    public String getRequestType(){
        return RequestType;
    }
    public String getReturnType(){
        return ReturnType;
    }

    public String getparameters() {return parameters;}
}
