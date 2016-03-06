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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.whatsoft.contactbook.R;
import com.whatsoft.contactbook.base.BaseActivity;
import com.whatsoft.contactbook.base.MyApplication;
import com.whatsoft.contactbook.constant.ProfileType;
import com.whatsoft.contactbook.database.TableContact;
import com.whatsoft.contactbook.model.Contact;
import com.whatsoft.contactbook.model.GGLocation;
import com.whatsoft.contactbook.module.profile.ProfilePresenter;
import com.whatsoft.contactbook.module.profile.ProfileView;
import com.whatsoft.contactbook.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements ProfileView, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String EXTRA_CONTACT = "contact";
    public static final String EXTRA_TYPE = "type";
    private String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int REQUEST_LOCATION = 11;

    @Bind(R.id.edt_name)
    EditText edtName;

    @Bind(R.id.edt_address)
    EditText edtAddress;

    @Bind(R.id.edt_gender)
    EditText edtGender;

    @Bind(R.id.edt_birthday)
    EditText edtDOB;

    @Bind(R.id.edt_relationship)
    EditText edtRelationship;

    @Bind(R.id.edt_nation)
    EditText edtNation;

    @Bind(R.id.edt_home_number)
    EditText edtHomeNo;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.btn_direction)
    FloatingActionButton btnDirection;

    @Bind(R.id.btn_add_favorite)
    FloatingActionButton btnFavorite;

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

    @Bind(R.id.btn_add_contact)
    Button btnAddContact;

    private Contact contact;
    private ProfileType profileType = ProfileType.VIEW;
    private ProfilePresenter profilePresenter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private boolean hasPermission = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePresenter = new ProfilePresenter(this);
        profilePresenter.attachView(this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        contact = (Contact) intent.getSerializableExtra(EXTRA_CONTACT);
        profileType = (ProfileType) intent.getSerializableExtra(EXTRA_TYPE);
        if (profileType == null) {
            profileType = ProfileType.VIEW;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fillData(contact);
        switchState(profileType);
        loadBackdrop();
        setUpMap();
        Utils.setUpHideSoftKeyboard(this, findViewById(R.id.main_content));
    }

    private void setUpMap() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(1000);
    }

    private void switchState(ProfileType type) {
        switch (type) {
            case ADD:
                setEditable(true);
                btnFavorite.setVisibility(View.GONE);
                btnDirection.setVisibility(View.GONE);
                btnAddContact.setVisibility(View.VISIBLE);
                appBarLayout.setExpanded(false);
                appBarLayout.setEnabled(false);
                collapsingToolbar.setTitle("Thêm liên lạc");
                break;
            case VIEW:
                btnAddContact.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (profileType == ProfileType.VIEW) {
            getMenuInflater().inflate(R.menu.edit_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_edit:
                Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillData(Contact contact) {
        if (contact == null) {
            return;
        }
        if (!TextUtils.isEmpty(contact.getName())) {
            collapsingToolbar.setTitle(contact.getName());
        } else {
            collapsingToolbar.setTitle(getString(R.string.profile));
        }
        if (!TextUtils.isEmpty(contact.getName())) {
            edtName.setText(contact.getName());
        }

        String address = "";
        if (!TextUtils.isEmpty(contact.getAddressNo())) {
            address = contact.getAddressNo() + " ";
        }

        if (!TextUtils.isEmpty(contact.getAddress())) {
            address = contact.getAddress();
        }

        edtAddress.setText(address);

        if (!TextUtils.isEmpty(contact.getHomeNumber())) {
            edtHomeNo.setText(contact.getHomeNumber());
        }

        edtGender.setText(contact.getGender().getValue());
        if (!TextUtils.isEmpty(contact.getRelationship())) {
            edtRelationship.setText(contact.getRelationship());
        }
        if (!TextUtils.isEmpty(contact.getNation())) {
            edtNation.setText(contact.getNation());
        }
        if (!TextUtils.isEmpty(contact.getDayOfBirth())) {
            edtDOB.setText(contact.getDayOfBirth());
        }
        changeFavoriteStatus(contact.isFavorite());
    }

    @OnClick(R.id.btn_add_favorite)
    void onClickFavorite() {
        if (contact == null) {
            return;
        }
        contact.setFavorite(!contact.isFavorite());
        changeFavoriteStatus(contact.isFavorite());
        TableContact tableContact = MyApplication.getInstance().getDatabaseHelper().getTableContact();
        if (tableContact.containts(contact.getId())) {
            tableContact.updateRow(contact);
        } else {
            tableContact.insertRow(contact);
        }
    }

    @OnClick(R.id.btn_direction)
    void onClickDirection() {
        String s = String.format("google.navigation:q=%s", Utils.formatAddress(contact));
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        i.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        startActivity(i);
    }

    @OnClick(R.id.btn_add_contact)
    void onClickAddContact() {
        Contact contact = new Contact();
        contact.setName(edtName.getText().toString());
        contact.setAddress(edtAddress.getText().toString());
        contact.setRelationship(edtRelationship.getText().toString());
        contact.setGender(edtGender.getText().toString());
        contact.setDayOfBirth(edtDOB.getText().toString());
        contact.setHomeNumber(edtHomeNo.getText().toString());
        contact.setNation(edtNation.getText().toString());
        if (mCurrentLocation != null) {
            contact.setLatitude(String.valueOf(mCurrentLocation.getLatitude()));
            contact.setLongitude(String.valueOf(mCurrentLocation.getLongitude()));
        }
        profilePresenter.addContact(contact);
    }

    private void changeFavoriteStatus(boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.mipmap.ic_star_yellow_24dp);
        } else {
            btnFavorite.setImageResource(R.mipmap.ic_star_white_24dp);
        }
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.cheese_4).centerCrop().into(imageView);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    private void setEditable(boolean editable) {
        edtAddress.setEnabled(editable);
        edtName.setEnabled(editable);
        edtDOB.setEnabled(editable);
        edtRelationship.setEnabled(editable);
        edtGender.setEnabled(editable);
        edtHomeNo.setEnabled(editable);
        edtNation.setEnabled(editable);
    }

    @Override
    public void onAddContactSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetAddressResult(GGLocation ggLocation) {
        if (ggLocation != null) {
            edtAddress.setText(ggLocation.getFormattedAddress());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (profileType != ProfileType.VIEW) {
            if ((ActivityCompat.checkSelfPermission(this, PERMISSIONS[0])
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, PERMISSIONS[1])
                    != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_LOCATION);
                hasPermission = false;
                return;
            }
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!hasPermission) {
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            com.whatsoft.contactbook.utils.Log.i("Lat : Lng - " + mCurrentLocation.getLatitude() + " :" + mCurrentLocation.getLongitude());
            profilePresenter.getAddress(String.valueOf(mCurrentLocation.getLatitude()), String.valueOf(mCurrentLocation.getLongitude()));
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, ProfileActivity.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Location", "Connect suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Location", "Connect failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            com.whatsoft.contactbook.utils.Log.i("Lat : Lng - " + mCurrentLocation.getLatitude() + " :" + mCurrentLocation.getLongitude());
            profilePresenter.getAddress(String.valueOf(mCurrentLocation.getLatitude()), String.valueOf(mCurrentLocation.getLongitude()));
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, ProfileActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                // Check if the only required permission has been granted
                boolean isGranted = false;
                for (int grantResult : grantResults) {
                    isGranted = (grantResult == PackageManager.PERMISSION_GRANTED);
                }
                if (grantResults.length == PERMISSIONS.length && isGranted) {
                    Log.i("Location request", "LOCATION permission has now been granted. Showing preview.");
                    hasPermission = true;
                    mGoogleApiClient.connect();
                } else {
                    Log.i("Location request", "LOCATION permission was NOT granted.");
                }
                break;
        }
    }
}
