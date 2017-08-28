package util;

public class RestCallParameters
{
    /** For HTTP GET, PUT, POST, DELETE requests, not handled with Retrofit 2.0 **/
    /** Create an object with the url, the return type (JSON, PLAIN_TEXT, XML), the request type (GET, PUT, POST, DELETE)
     * and in case of POST request the fields of the object as parameters **/
    public enum DATA_TYPE {
        JSON("JSON"),TEXT("TEXT");
        String str;
        DATA_TYPE(String str)
        {
            this.str = str;
        }
        @Override
        public String toString(){ return str;}
    }

    public enum CALL_RESOURCE {
        STRING("STRING"),STREAM("STREAM");
        String str;
        CALL_RESOURCE(String str)
        {
            this.str = str;
        }
        @Override
        public String toString(){ return str;}
    }

    String Url;
    String RequestType; //GET or POST or PUT or DELETE
    String ReturnType;  //JSON or Plain Text
    String parameters;

    public String getCallResource() {
        return CallResource;
    }

    String CallResource;

    public RestCallParameters(String url, String requestType, String returnType, String parameters) {
        this.Url = url;
        this.RequestType = requestType;
        this.ReturnType = returnType;
        this.parameters = parameters;
        this.CallResource = CALL_RESOURCE.STRING.toString();
    }

    public RestCallParameters(String url, String requestType, String returnType, String parameters, String callResource) {
        this.Url = url;
        this.RequestType = requestType;
        this.ReturnType = returnType;
        this.parameters = parameters;
        this.CallResource = callResource;
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
