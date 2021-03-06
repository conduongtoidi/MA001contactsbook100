package com.whatsoft.contactbook.api;

import com.whatsoft.contactbook.model.Contact;
import com.whatsoft.contactbook.model.GGResponse;
import com.whatsoft.contactbook.model.ResponseModel;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @GET("/api/v1/ucontact/")
    Call<ResponseModel<List<Contact>>> getContacts(@Query("q") String keyword);

    @GET("/maps/api/geocode/json")
    Call<GGResponse> getAddress(@Query("latlng") String latlng, @Query("location_type") String locationType, @Query("result_type") String resultType, @Query("key") String key);

    @GET("/maps/api/geocode/json")
    Call<GGResponse> getAddressNoKey(@Query("latlng") String latlng, @Query("sensor") String sensor);

    @POST("/api/v1/ucontact/")
    Call<ResponseModel<Contact>> addContacts(@Body RequestBody requestBody);
}
