package com.whatsoft.contactbook.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.whatsoft.contactbook.base.BaseObject;

public class BaseResponseModel extends BaseObject {
    @Expose
    @SerializedName("status")
    String status;

    @Expose
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
