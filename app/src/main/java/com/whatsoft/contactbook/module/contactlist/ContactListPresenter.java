package com.whatsoft.contactbook.module.contactlist;

import com.whatsoft.contactbook.api.ApiService;
import com.whatsoft.contactbook.base.BasePresenter;
import com.whatsoft.contactbook.model.Contact;
import com.whatsoft.contactbook.model.ResponseModel;
import com.whatsoft.contactbook.utils.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mb on 3/6/16
 */
public class ContactListPresenter extends BasePresenter<ContactListView> implements IContactListPresenter {
    @Override
    public void loadContactList(int page, String keyword) {
        Call<ResponseModel<List<Contact>>> call = ApiService.getApiInstance().getContacts(keyword);
        call.enqueue(new Callback<ResponseModel<List<Contact>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<Contact>>> call, Response<ResponseModel<List<Contact>>> response) {
                try {
//                    String content = response.body();
                    ResponseModel<List<Contact>> responseModel = response.body();
                    Log.d(responseModel.toJsonString());
                    if (responseModel.getData() != null) {
                        getBaseView().onLoadContactListSuccess(responseModel.getData());
                    }
//                    Log.d(content);
//                    if (!TextUtils.isEmpty(content)) {
//                        ResponseModel<List<Contact>> responseModel = ParserUtil.fromJson(content, new TypeToken<ResponseModel<List<Contact>>>() {
//                        }.getType());
//                        if (responseModel.getData() != null) {
//                            getBaseView().onLoadContactListSuccess(responseModel.getData());
//                        }
//                    }
                } catch (Exception e) {
                    Log.e(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<Contact>>> call, Throwable t) {

            }
        });
    }
}
