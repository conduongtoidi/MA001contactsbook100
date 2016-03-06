package com.whatsoft.contactbook.api;

import com.squareup.okhttp.OkHttpClient;
import com.whatsoft.contactbook.utils.ToStringConverterFactory;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiService {
    private static Retrofit retrofit;
    public static String baseUrl = "http://128.199.133.102:8080";

    public synchronized static Api getApiInstance() {
        if (retrofit == null) {
            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(new ToStringConverterFactory())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(Api.class);
    }
}
