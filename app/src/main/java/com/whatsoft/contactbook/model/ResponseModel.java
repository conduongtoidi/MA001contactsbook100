package com.whatsoft.contactbook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> extends BaseResponseModel {

    @Expose
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }
}