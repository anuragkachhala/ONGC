package com.tekit.software.ongc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tekit.software.ongc.Utils.SessionManager;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tekit.software.ongc.WellLocationActivity.WELL_ID;
import static com.tekit.software.ongc.WellLocationActivity.WELL_LATITUDE;
import static com.tekit.software.ongc.WellLocationActivity.WELL_LONGITUDE;
import static com.tekit.software.ongc.WellLocationActivity.WELL_RELEASE_NAME;
import static com.tekit.software.ongc.WellLocationActivity.WELL_SHORT_NAME;
import static com.tekit.software.ongc.WellLocationActivity.WELL_UWI;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = HomeActivity.class.getName();
    private static final int REQUEST_CODE_WELL_SEARCH =1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 100;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_search_well)
    EditText editTextSearchWell;
    @BindView(R.id.widget_well_lat)
    TextInputLayout textInputLayoutWellLat;
    @BindView(R.id.et_well_long)
    EditText editTextWellLong;
    @BindView(R.id.widget_well_long)
    TextInputLayout textInputLayoutWellLong;
    @BindView(R.id.et_current_lat)
    EditText editTextCurrentLat;
    @BindView(R.id.et_current_long)
    EditText editTextCurrentLong;
    @BindView(R.id.et_well_lat)
    EditText editTextWellLat;
    @BindView(R.id.btn_search_well)
    Button buttonSearchWell;

    @BindView(R.id.et_well_uwi)
    EditText editTextUWI;

    @BindView(R.id.et_well_release_name)
    EditText editTextWellReleaseName;

    @BindView(R.id.et_well_short_name)
    EditText editTextWellShortName;

    private Boolean mRequestingLocationUpdates = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private SessionManager mSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        startLocation();
        SessionManager.setContext(this);
        mSessionManager = SessionManager.getInstance();
        setListener();


    }

    @Override
    protected void setListener() {
        editTextSearchWell.setOnClickListener(this);
        buttonSearchWell.setOnClickListener(this);

    }

    @Override
    protected void setResources() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected String getToolBarTitle() {
        return null;
    }

    @Override
    protected void startActivity(Class<?> cls) {

    }

    private boolean updateLocation = false;
    private void init() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();


                updateLocationUI();


            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }





    public void startLocation() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        init();
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());


                        //
                        //
                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:




                        }

                        updateLocationUI();
                    }
                });
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void stopLocationUpdates() {

        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       }
                });
    }


    private void updateLocationUI() {
        if (mCurrentLocation != null && !updateLocation) {
            updateLocation = true;
            editTextCurrentLat.setText(mCurrentLocation.getLatitude() + "");
            editTextCurrentLong.setText(mCurrentLocation.getLongitude() + "");
        }


    }




    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.et_search_well:
                openWellLocationActivity();
                break;
            case R.id.btn_search_well:
                openMap();
        }
    }

    private void openWellLocationActivity() {
        startActivityForResult(new Intent(this, WellLocationActivity.class), REQUEST_CODE_WELL_SEARCH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WELL_SEARCH) {
            if (resultCode == RESULT_OK) {
                editTextSearchWell.setText(data.getStringExtra(WELL_ID));
                editTextWellLat.setText(data.getStringExtra(WELL_LATITUDE));
                editTextWellLong.setText(data.getStringExtra(WELL_LONGITUDE));
                editTextWellShortName.setText(data.getStringExtra(WELL_SHORT_NAME));
                editTextWellReleaseName.setText(data.getStringExtra(WELL_RELEASE_NAME));
                editTextUWI.setText(data.getStringExtra(WELL_UWI));
                updateLocationUI();

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_unlock:
                showAlertDialog();
                break;
            case R.id.action_feedback:
                openMailForFeedBack();
        }

        return super.onOptionsItemSelected(item);


    }

    private void openMailForFeedBack() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.contact_us_mail)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        startActivity(Intent.createChooser(intent, "Send feedback"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setMessage("Are you sure to logout?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mSessionManager.logoutUser();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });


        alertDialog.show();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }


    public void openMap() {
            if (mCurrentLocation != null) {
            final Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            "http://maps.google.com/maps?" +
                                    "saddr=" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude() + "&daddr=" + editTextWellLat.getText().toString() + "," + editTextWellLong.getText().toString()));
            intent.setClassName(
                    "com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            startActivity(intent);


        }
    }


}
