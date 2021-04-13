package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.nearby;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.greysonparrelli.permiso.Permiso;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.PlacesListAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.PlacesModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ManualLocationActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 2334;
    PlacesClient mPlacesClient;

    EditText mEditText;
    Toolbar mToolbar;
    ProgressBar mProgressBar;
    LinearLayout mBtnNearby;

    FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    User mCurrentUser;

    RecyclerView mRecyclerView;
    ArrayList<PlacesModel> mPlacesModels;
    PlacesListAdapter mPlacesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_location);


        mToolbar = findViewById(R.id.toolbar);
        mEditText = findViewById(R.id.search);
        mProgressBar = findViewById(R.id.loading);
        mRecyclerView = findViewById(R.id.places_recycler_view);
        mBtnNearby = findViewById(R.id.people_nearby);

        setLoading(false, false);


        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Permiso.getInstance().setActivity(this);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_api_key_geo));
        }

        mPlacesClient = Places.createClient(this);

        mBtnNearby.setOnClickListener(v -> checkLastLocation());

        mPlacesModels = new ArrayList<>();
        mPlacesListAdapter = new PlacesListAdapter(this, mPlacesModels);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mRecyclerView.setAdapter(mPlacesListAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        setupTextWatcher(mEditText);
    }


    public  void setupTextWatcher(EditText editText) {

        TextWatcher textWatcher  = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 4){
                    setLoading(false, false);

                    mBtnNearby.setVisibility(View.VISIBLE);
                } else {
                    setLoading(true, false);

                    mBtnNearby.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 3){

                    getPlaces(s.toString());
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
    }

    public void setLoading(boolean isLoading, boolean isLoaded){

        if (isLoading){

            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);

        } else {

            if (isLoaded){

                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

            } else {

                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    public void getPlaces(String queryText){

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.CITIES)
                .setSessionToken(token)
                .setQuery(queryText)
                .build();

        ArrayList<PlacesModel> placesModels = new ArrayList<>();


        mPlacesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            StringBuilder mResult = new StringBuilder();

            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                mResult.append(" ").append(prediction.getFullText(null)).append("\n");

                PlacesModel place = new PlacesModel();

                place.setPlaceId(prediction.getPlaceId());
                place.setAddress(prediction.getFullText(null).toString());

                placesModels.add(place);
            }

            mPlacesModels.clear();
            mPlacesModels.addAll(placesModels);
            mPlacesListAdapter.notifyDataSetChanged();

            setLoading(false, true);

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("DATOO_LOCATION", exception.getLocalizedMessage() + " " + apiException.getStatusCode());

                setLoading(false, false);
            }
        });
    }

    public void saveNewLocation(String placeId, String address){

        QuickHelp.showLoading(ManualLocationActivity.this, false);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        FetchPlaceRequest requestLocation = FetchPlaceRequest.builder(placeId, placeFields).build();

        mPlacesClient.fetchPlace(requestLocation).addOnSuccessListener(task -> {

            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(Objects.requireNonNull(task.getPlace().getLatLng()).latitude, task.getPlace().getLatLng().longitude);
            mCurrentUser.setGeoPoint(parseGeoPoint);
            mCurrentUser.setHasGeoPoint(true);
            mCurrentUser.setLocation(address);
            mCurrentUser.setLocationTypeNearBy(false);

            mCurrentUser.saveInBackground(e -> {

                if (e == null){

                    QuickHelp.hideLoading(this);

                    finish();

                } else {

                    QuickHelp.hideLoading(this);

                    QuickHelp.showToast(ManualLocationActivity.this, getString(R.string.error_ocurred), true);
                }
            });
        }).addOnFailureListener(e -> {
            e.printStackTrace();

            QuickHelp.hideLoading(this);
            QuickHelp.showToast(ManualLocationActivity.this, getString(R.string.error_ocurred), true);
        });
    }

    private void checkLastLocation(){

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    mFusedLocationClient.getLastLocation().addOnSuccessListener(ManualLocationActivity.this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            QuickHelp.showLoading(ManualLocationActivity.this, false);

                            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                            mCurrentUser.setGeoPoint(parseGeoPoint);
                            mCurrentUser.setHasGeoPoint(true);
                            mCurrentUser.setLocation(QuickHelp.getCityFromLocation(parseGeoPoint).replaceAll("null,", ""));
                            mCurrentUser.setLocationTypeNearBy(true);

                            mCurrentUser.saveInBackground(e -> {

                                if (e == null){

                                    QuickHelp.hideLoading(ManualLocationActivity.this);

                                    finish();

                                } else {

                                    QuickHelp.hideLoading(ManualLocationActivity.this);

                                    QuickHelp.showToast(ManualLocationActivity.this, getString(R.string.error_ocurred), true);
                                }
                            });

                        } //else QuickHelp.showToast(ManualLocationActivity.this, getString(R.string.error_ocurred), true);

                        else checkLocationSettings();
                    }).addOnFailureListener(e -> checkLocationSettings());

                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(null,
                        getString(R.string.msg_permission_required),
                        null, callback);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);


    }

    protected void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void checkLocationSettings(){

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...

            checkLastLocation();

        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}