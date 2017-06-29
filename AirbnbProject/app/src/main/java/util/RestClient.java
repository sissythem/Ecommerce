package util;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by sissy on 25/6/2017.
 */

public class RestClient
{
    public static final String BASE_URL = "http://192.168.1.16:8080/ecommerce_rest/webresources/";
    private static Retrofit retrofit = null;
    private static Retrofit stringRetrofit = null;


    public static Retrofit getClient(String token) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient(token))
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getStringClient() {
        if (stringRetrofit==null) {
            stringRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient(null))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return stringRetrofit;
    }

    private static OkHttpClient getUnsafeOkHttpClient(final String token) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException
                        {

                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            /*Add authentication header*/
            if (token != null) {
                builder.addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request newRequest = chain.request().newBuilder()
                                        .addHeader("Authorization", token)
                                        .build();
                                return chain.proceed(newRequest);
                            }
                        }
                );
            }

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
