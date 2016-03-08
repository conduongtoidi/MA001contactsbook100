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

package com.whatsoft.contactbook.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;
import com.whatsoft.contactbook.R;
import com.whatsoft.contactbook.base.BaseActivity;
import com.whatsoft.contactbook.constant.ListType;
import com.whatsoft.contactbook.constant.ProfileType;
import com.whatsoft.contactbook.fragment.ContactsListFragment;
import com.whatsoft.contactbook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static final int REQUEST_ADD_CONTACT = 10;
    private DrawerLayout mDrawerLayout;
    public FloatingActionButton btnAddContact;
    public android.support.v7.widget.SearchView searchView;
    private Adapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            show menu
//            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
//            actionBar.setDisplayHomeAsUpEnabled(true);
            //hide menu
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        btnAddContact = (FloatingActionButton) findViewById(R.id.fab);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_TYPE, ProfileType.ADD);
                startActivityForResult(intent, REQUEST_ADD_CONTACT);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Utils.hideSoftKeyboard(MainActivity.this);
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                if (fragment instanceof ContactsListFragment) {
                    ((ContactsListFragment) fragment).doSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search with keyword length >= 3
//                if (!TextUtils.isEmpty(newText) && newText.trim().length() < 3) {
//                    return false;
//                }
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                if (fragment instanceof ContactsListFragment) {
                    ((ContactsListFragment) fragment).doSearch(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ContactsListFragment(), getString(R.string.contacts_list));
        ContactsListFragment contactsListFragment = new ContactsListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ContactsListFragment.LIST_TYPE, ListType.FAVORITE);
        contactsListFragment.setArguments(bundle);
        adapter.addFragment(contactsListFragment, getString(R.string.favorite_list));
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        public List<Fragment> getmFragments() {
            return mFragments;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (Fragment fragment : adapter.getmFragments()) {
            fragment.onResume();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : adapter.getmFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
