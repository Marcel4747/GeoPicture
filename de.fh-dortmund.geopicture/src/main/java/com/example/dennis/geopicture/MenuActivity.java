package com.example.dennis.geopicture;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.dennis.classes.Checkpoint;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import de.do1900.rest.service.APIServiceConnection;
import de.do1900.rest.service.ServiceResponseInterface;
import de.do1900.rest.service.ServiceStateInterface;


public class MenuActivity extends Activity implements ServiceStateInterface, ServiceResponseInterface {

    private Button buttonMap;
    private SeekBar seekBar;
    private List<Checkpoint> checkpoints;
    private APIServiceConnection serviceConnection = new APIServiceConnection(
            this);

    private LatLng position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        checkpoints = new ArrayList<Checkpoint>();

        // Positions bestimmung
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                position = new LatLng(location.getLatitude(), location.getLongitude());

                Log.d("Change ","");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        //SeekBar holen
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        int distanceValue = (seekBar.getProgress()+ 1) * 500;

        // Button Definieren
        buttonMap = (Button) findViewById(R.id.buttonStartMap);
        buttonMap.setEnabled(false);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                // Filterung nach Distance
                int distanceValue = ((SeekBar) findViewById(R.id.seekBar)).getProgress() + 1;

                float[] results = new float[3];

                for (Checkpoint item : checkpoints) {
                    Location.distanceBetween(position.latitude, position.longitude, item.getPosition().latitude, item.getPosition().longitude, results);
                    if (results[0] > distanceValue) {

                        Checkpoint.checkpoints.add(item);
                    }
                }

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(APIServiceConnection.createServiceConnectionIntent(),
                serviceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
    }

    @Override
    public void onServiceConnected() {
        serviceConnection.getLocations("locations", this, "Dortmund", null);
    }

    @Override
    public void onServiceDisconnected() {

    }

    @Override
    public void handleServiceResponse(Object o, Object o2) {
        de.do1900.persistence.data.Location[] lo = (de.do1900.persistence.data.Location[]) o2;

        for (de.do1900.persistence.data.Location item : lo) {
            checkpoints.add(new Checkpoint(item, this, this));
        }


        buttonMap.setEnabled(true);
    }

    @Override
    public void handleServerTimeout(Object o) {

    }

    @Override
    public void handleServerError(Object o) {

    }

    @Override
    public void handleProgressChanged(Object o, int i) {

    }
}

//        e:
//        cd "E:\Programme\Android\SDK\platform-tools"
//        adb install "E:\Programme\Android\SDK\platform-tools\Do1900Client.apk"


//        d:
//        cd "D:\Programme\sdk\platform-tools"
//        adb install "C:\\Users\\Marcel\\Desktop\\Do1900Client.apk"

