package com.whatsoft.contactbook.module.profile;

import com.whatsoft.contactbook.base.BaseView;
import com.whatsoft.contactbook.model.GGLocation;

/**
 * Created by mb on 3/6/16
 */
public interface ProfileView extends BaseView {
    void onAddContactSuccess(String message);

    void onError(String message);

    void onGetAddressResult(GGLocation ggLocation);
}
