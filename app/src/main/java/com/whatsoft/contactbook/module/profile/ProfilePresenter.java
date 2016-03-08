package com.whatsoft.contactbook.module.profile;

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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Log.d(contact.toJsonString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), contact.toJsonString());
        Call<ResponseModel<Contact>> call = ApiService.getApiInstance().addContacts(requestBody);
        call.enqueue(new Callback<ResponseModel<Contact>>() {
            @Override
            public void onResponse(Call<ResponseModel<Contact>> call, Response<ResponseModel<Contact>> response) {
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
            public void onFailure(Call<ResponseModel<Contact>> call, Throwable t) {
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        String longlat = String.format("%s,%s", latitude, longitude);
        final Call<GGResponse> call = retrofit.create(Api.class).getAddressNoKey(longlat, "false");//"ROOFTOP", "street_address", activity.getString(R.string.google_maps_key
        call.enqueue(new Callback<GGResponse>() {
            @Override
            public void onResponse(Call<GGResponse> call, Response<GGResponse> response) {
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
            public void onFailure(Call<GGResponse> call, Throwable t) {
                Log.e(t);
                DialogUtils.hideProgressDialog(activity);
            }
        });
    }
}
