/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whatsoft.contactbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whatsoft.contactbook.R;
import com.whatsoft.contactbook.activity.MainActivity;
import com.whatsoft.contactbook.adapter.ContactListAdapter;
import com.whatsoft.contactbook.base.BaseFragment;
import com.whatsoft.contactbook.base.MyApplication;
import com.whatsoft.contactbook.constant.ListType;
import com.whatsoft.contactbook.database.TableContact;
import com.whatsoft.contactbook.model.Contact;
import com.whatsoft.contactbook.module.contactlist.ContactListPresenter;
import com.whatsoft.contactbook.module.contactlist.ContactListView;
import com.whatsoft.contactbook.utils.Utils;
import com.whatsoft.contactbook.view.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsListFragment extends BaseFragment implements ContactListView {
    public static final String LIST_TYPE = "list_type";
    private ListType listType = ListType.ALL;

    @Bind(R.id.rcv_contact_list)
    RecyclerView rcvContactList;

    @Bind(R.id.tv_empty)
    TextView tvEmpty;

    private List<Contact> contacts;
    private ContactListAdapter contactListAdapter;
    private EndlessScrollListener endlessScrollListener;
    private ContactListPresenter contactListPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listType = (ListType) getArguments().getSerializable(LIST_TYPE);
        }
        contacts = new ArrayList<>();
        contactListAdapter = new ContactListAdapter(contacts);
        contactListPresenter = new ContactListPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ButterKnife.bind(this, view);
        contactListPresenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(rcvContactList);
        Utils.setUpHideSoftKeyboard(getActivity(), rcvContactList);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        if (listType == ListType.FAVORITE) {
            refreshList((MyApplication.getInstance().getDatabaseHelper().getTableContact().filterBy(TableContact.KEY_IS_FAVORITE, "1")));
        } else {
            refreshList(new ArrayList<Contact>());
        }
        recyclerView.setAdapter(contactListAdapter);

        ((MainActivity) getActivity()).btnAddContact.attachToRecyclerView(recyclerView);

        if (listType == ListType.ALL) {
            endlessScrollListener = new EndlessScrollListener(recyclerView.getLayoutManager(), new EndlessScrollListener.Callback() {
                @Override
                public void onLoadMore() {
//                    if (listType == ListType.ALL) {
//                        contacts.addAll(getRandomSubList(contacts.size() + 20));
//                        contactListAdapter.notifyDataSetChanged();
//                    }
                }

                @Override
                public void onScroll(int visibleItemCount, int pastVisibleItem) {

                }
            });
            recyclerView.addOnScrollListener(endlessScrollListener);
        }
    }

    private void refreshList(List<Contact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        contactListAdapter.notifyDataSetChanged();
        if (contacts.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rcvContactList.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rcvContactList.setVisibility(View.VISIBLE);
        }
    }

    public void doSearch(String words) {
        if (listType == ListType.FAVORITE) {
            contactListAdapter.getFilter().filter(words);
        } else {
            contactListPresenter.loadContactList(0, words);
        }
    }

    private List<Contact> getRandomSubList(int amount) {
        ArrayList<Contact> list = new ArrayList<>(amount);
        int i = contacts.size();
        while (list.size() < amount) {
            Contact contact = new Contact();
            contact.setId(String.valueOf(i));
            if (i % 2 == 0) {
                contact.setGender("Nam");
            } else {
                contact.setGender("Nữ");
            }
            contact.setName("Name Number " + i);
            contact.setAddress("Khu Phố 2 Tổ 18 Đường 5, Hẻm 115, Hẻm 115/12, Phường Linh Xuân");
            contact.setDayOfBirth("6/3/42");
            contact.setHomeNumber("444");
            contact.setNation("Kinh");
            contact.setRelationship("Chủ hộ");
            contact.setAddressNo("Trọ 60/7/10/1b Đ4");
            list.add(contact);
            i++;
        }
        return list;
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onLoadContactListSuccess(List<Contact> contacts) {
        refreshList(contacts);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listType == ListType.FAVORITE) {
            contacts.clear();
            contacts.addAll(MyApplication.getInstance().getDatabaseHelper().getTableContact().filterBy(TableContact.KEY_IS_FAVORITE, "1"));
            contactListAdapter.notifyDataSetChanged();
        }
    }
}
