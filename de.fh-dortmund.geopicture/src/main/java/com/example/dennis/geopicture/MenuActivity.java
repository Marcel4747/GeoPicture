package com.example.dennis.geopicture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dennis.classes.Checkpoint;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import de.do1900.rest.service.APIServiceConnection;
import de.do1900.rest.service.ServiceResponseInterface;
import de.do1900.rest.service.ServiceStateInterface;


public class MenuActivity extends Activity implements ServiceStateInterface, ServiceResponseInterface {

    private Button buttonMap;
    private SeekBar seekBarDistance;
    private SeekBar seekBarCount;
    private List<Checkpoint> checkpoints;

    private TextView textView_Entfernung;
    private TextView textView_Anzahl;

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

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        textView_Entfernung = (TextView) findViewById(R.id.textView_Entfernung);
        textView_Anzahl = (TextView) findViewById(R.id.textView_Anzahl);

        //seekBarDistance holen
        seekBarDistance = (SeekBar) findViewById(R.id.seekBarDistance);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView_Entfernung.setText("Entfernung: " + ((progress + 1) * 500) + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //seekBarCount holen
        seekBarCount = (SeekBar) findViewById(R.id.seekBarCount);
        seekBarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView_Anzahl.setText("Anzahl von Bildern: " + (progress + 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // Button Definieren
        buttonMap = (Button) findViewById(R.id.buttonStartMap);
        buttonMap.setEnabled(false);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                if (position != null) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                    int distanceValue = (seekBarDistance.getProgress() + 1) * 500;
                    int countValue = seekBarCount.getProgress() + 2;

                    float[] results = new float[3];
                    int i = 0;
                    Log.d("cSize", checkpoints.size() + "");
                    for (Checkpoint item : checkpoints) {
                        if (item != null && item.getPosition() != null && item.hasImages()) {
                            Location.distanceBetween(position.latitude, position.longitude, item.getPosition().latitude, item.getPosition().longitude, results);

                            if (results[0] <= distanceValue && i < countValue) {
                                Checkpoint.checkpoints.add(item);
                                i++;
                            } else if (i >= countValue) {
                                break;
                            }
                        }
                    }

                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Keine Positionsdaten geunden.", Toast.LENGTH_LONG).show();
                }
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
            checkpoints.add(new Checkpoint(item, this, serviceConnection));
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

