package com.whatsoft.contactbook.module.profile;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.whatsoft.contactbook.R;
import com.whatsoft.contactbook.api.Api;
import com.whatsoft.contactbook.api.ApiService;
import com.whatsoft.contactbook.base.BaseActivity;
import com.whatsoft.contactbook.base.BasePresenter;
import com.whatsoft.contactbook.model.Contact;
import com.whatsoft.contactbook.model.GGResponse;
import com.whatsoft.contactbook.model.ResponseModel;
import com.whatsoft.contactbook.utils.DialogUtils;
import com.whatsoft.contactbook.utils.Log;
import com.whatsoft.contactbook.utils.ToStringConverterFactory;

import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mb on 3/6/16
 */
public class ProfilePresenter extends BasePresenter<ProfileView> implements IProfilePresenter {
    BaseActivity activity;

    public ProfilePresenter(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void addContact(Contact contact) {
        DialogUtils.showProgressDialog(activity);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), contact.toJsonString());
        Call<ResponseModel<Contact>> call = ApiService.getApiInstance().addContacts(requestBody);
        call.enqueue(new Callback<ResponseModel<Contact>>() {
            @Override
            public void onResponse(Response<ResponseModel<Contact>> response, Retrofit retrofit) {
                try {
                    ResponseModel<Contact> responseModel = response.body();
                    if (responseModel != null) {
                        getBaseView().onAddContactSuccess(activity.getString(R.string.add_contact_success));
                        Log.d(responseModel.toJsonString());
                    }
                } catch (Exception e) {
                    Log.e(e);
                    getBaseView().onError(activity.getString(R.string.general_error_message));
                } finally {
                    DialogUtils.hideProgressDialog(activity);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(t);
                DialogUtils.hideProgressDialog(activity);
                getBaseView().onError(activity.getString(R.string.general_error_message));
            }
        });
    }

    @Override
    public void getAddress(String latitude, String longitude) {
        DialogUtils.showProgressDialog(activity);
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .client(okHttpClient)
                .build();
        String longlat = String.format("%s,%s", latitude, longitude);
        final Call<GGResponse> call = retrofit.create(Api.class).getAddressNoKey(longlat, "false");//"ROOFTOP", "street_address", activity.getString(R.string.google_maps_key
        call.enqueue(new Callback<GGResponse>() {
            @Override
            public void onResponse(Response<GGResponse> response, Retrofit retrofit) {
                try {
                    GGResponse ggResponse = response.body();
                    if (ggResponse != null && ggResponse.getLocations() != null && ggResponse.getLocations().size() > 0) {
                        getBaseView().onGetAddressResult(ggResponse.getLocations().get(0));
                    }
                } catch (Exception e) {
                    Log.e(e);
                } finally {
                    DialogUtils.hideProgressDialog(activity);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(t);
                DialogUtils.hideProgressDialog(activity);
            }
        });
    }
}
