package com.whatsoft.contactbook.module.profile;

import com.whatsoft.contactbook.model.Contact;

/**
 * Created by mb on 3/6/16
 */
public interface IProfilePresenter {
    void addContact(Contact contact);

    void getAddress(String latitude, String longitude);
}
