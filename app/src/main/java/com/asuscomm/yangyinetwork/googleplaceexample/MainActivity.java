package com.asuscomm.yangyinetwork.googleplaceexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        chkPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        initPlacePicker();
    }

    private void initPlacePicker() {
        Log.d(TAG, "initPlacePicker: ");
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent build = null;
        try {
            build = builder.build(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        startActivityForResult(build, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == PLACE_PICKER_REQUEST) {
            Log.d(TAG, "onActivityResult: PLACE_PICKER_REQUEST");
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: RESULT_OK");
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "onActivityResult: RESULT NOT OK");
            }
        } else {
            Log.d(TAG, "onActivityResult: REQUESTCODE NOT PLACEPICKER");
        }
    }

    private void chkPermission(String accessFineLocation) {
        if (ContextCompat.checkSelfPermission(this,
                accessFineLocation) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{accessFineLocation}, 0);
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called with: connectionResult = [" + connectionResult + "]");
    }
}
