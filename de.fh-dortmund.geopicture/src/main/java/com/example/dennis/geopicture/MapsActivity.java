package com.example.dennis.geopicture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.location.*;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dennis.classes.Checkpoint;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Marker> marker;
    private List<LatLng> way;
    private int checkpointNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


        for (int i = 0; i < Checkpoint.checkpoints.size(); i++) {
            Checkpoint c=Checkpoint.checkpoints.get(i);
            mMap.addMarker(new MarkerOptions()
                .position(c.getPosition())
                .title("checkpoint "+i)
                .snippet(i+". Position"));
            if (i < 10)
                c.loadImage();
        }

//        checkpoints.add(new Checkpoint(new LatLng(51.514568, 7.465091)));
//        checkpoints.add(new Checkpoint(new LatLng(51.514758, 7.467934)));
//        checkpoints.add(new Checkpoint(new LatLng(51.510742, 7.464630)));
        checkpointNumber = 0;
        marker = new ArrayList<Marker>();
        way = new ArrayList<LatLng>();

//        LatLng point1 = new LatLng(51.514568, 7.465091);
//        Marker pos1 = mMap.addMarker(new MarkerOptions()
//                .position(point1)
//                .title("pos1")
//                .snippet("1. Position"));
//        LatLng point2 = new LatLng(51.515666, 7.455821);
//
//        Marker pos2 = mMap.addMarker(new MarkerOptions()
//                .position(point2)
//                .title("pos2")
//                .snippet("2. Position"));
//        Polyline line = mMap.addPolyline(new PolylineOptions()
//                .add(pos1.getPosition(), pos2.getPosition())
//                .width(5)
//                .color(Color.RED));

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                LatLng target = Checkpoint.checkpoints.get(checkpointNumber).getPosition();
                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                float[] results = new float[3];
                if (way.size() > 0) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(way.get(way.size() - 1), pos)
                            .width(5)
                            .color(Color.RED));
                }
                way.add(pos);
                Location.distanceBetween(target.latitude, target.longitude, pos.latitude, pos.longitude, results);

                if (results[0] <= 50) {
                    //Checkpoint checked!
                    marker.add(mMap.addMarker(new MarkerOptions()
                            .position(target)
                            .title("Checkpoint " + (checkpointNumber + 1))
                            .snippet("blablabalbalbala")));
//                    if (marker.size() > 1) {
//                        Polyline line = mMap.addPolyline(new PolylineOptions()
//                                .add(marker.get(marker.size()-2).getPosition(), marker.get(marker.size()-1).getPosition())
//                                .width(5)
//                                .color(Color.RED));
//                    }
                    checkpointNumber++;
                }

//                mMap.addMarker(new MarkerOptions()
//                        .position(p)
//                        .title("neuer Marker")
//                        .snippet("blablabalbalbala"));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        Button showImage = (Button) findViewById(R.id.button);
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.putExtra("imageNumber", checkpointNumber);
                startActivity(intent);
            }
        });

// Register the listener with the Location Manager to receive location updates
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(51.5138273, 7.4671297))
                .zoom(13)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

    }
}
