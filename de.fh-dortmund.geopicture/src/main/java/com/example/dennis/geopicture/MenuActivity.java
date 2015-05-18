package com.example.dennis.geopicture;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dennis.classes.Checkpoint;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.do1900.android.utils.BitmapCache;
import de.do1900.android.utils.ImageReceiver;
import de.do1900.persistence.data.ImageInformation;
import de.do1900.rest.service.APIServiceConnection;
import de.do1900.rest.service.ServiceResponseInterface;
import de.do1900.rest.service.ServiceStateInterface;


public class MenuActivity extends Activity implements ServiceStateInterface, ServiceResponseInterface {

    private Button buttonMap;
    private List<Checkpoint> checkpoints;
    private APIServiceConnection serviceConnection = new APIServiceConnection(
            this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        checkpoints = new ArrayList<Checkpoint>();

        buttonMap = (Button) findViewById(R.id.buttonStartMap);
        buttonMap.setEnabled(false);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                startActivity(intent);
            }
        });

        Button buttonDennis = (Button) findViewById(R.id.buttonStartDennis);
        buttonDennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DennisActivity.class);
                startActivity(intent);
            }
        });

        Button buttonMarcel = (Button) findViewById(R.id.buttonStartMarcel);
        buttonMarcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MarcelActivity.class);
                startActivity(intent);
            }
        });

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
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

        Log.d("Test:", o.toString() + "    " + o2.getClass().toString());
        de.do1900.persistence.data.Location[] lo = (de.do1900.persistence.data.Location[]) o2;

        for (de.do1900.persistence.data.Location item : lo) {
            Log.d("Test:", item.getName());
            Checkpoint.checkpoints.add(new Checkpoint(item, this, this));
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

