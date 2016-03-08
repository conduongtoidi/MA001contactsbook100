package com.whatsoft.contactbook.api;

import com.whatsoft.contactbook.utils.stringconverter.StringConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static Retrofit retrofit;
    public static String baseUrl = "http://128.199.133.102:8080";

    public synchronized static Api getApiInstance() {
        if (retrofit == null) {
            final OkHttpClient okHttpClient = new OkHttpClient();
//            okHttpClient.networkInterceptors().add(new StethoInterceptor());
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(new StringConverterFactory())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(Api.class);
    }
}
