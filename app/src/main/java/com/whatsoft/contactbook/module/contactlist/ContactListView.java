package com.whatsoft.contactbook.module.contactlist;

import com.whatsoft.contactbook.base.BaseView;
import com.whatsoft.contactbook.model.Contact;

import java.util.List;

/**
 * Created by mb on 3/6/16
 */
public interface ContactListView extends BaseView {
    void onLoadContactListSuccess(List<Contact> contacts);

    void onError(String error);
}
