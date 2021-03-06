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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import static android.support.v4.app.ActivityCompat.startActivity;

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

        Log.d("CheckpointSize", Checkpoint.checkpoints.size()+"");
        for (int i = 0; i < Checkpoint.checkpoints.size(); i++) {
            Checkpoint c = Checkpoint.checkpoints.get(i);
            Log.d("Checkpoint"+i+" Lon", c.getPosition().longitude+"");
            Log.d("Checkpoint"+i+" Lat", c.getPosition().latitude+"");
//            mMap.addMarker(new MarkerOptions()
//                    .position(c.getPosition())
//                    .title(c.getName())
//                    .snippet("Checkpoint " + i));
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

                if(Checkpoint.checkpoints.size()>checkpointNumber) {
                    Checkpoint cp = Checkpoint.checkpoints.get(checkpointNumber);
                    LatLng target = cp.getPosition();
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
                                .title(cp.getName())
                                .snippet("Checkpoint " + (checkpointNumber + 1))));
//                    if (marker.size() > 1) {
//                        Polyline line = mMap.addPolyline(new PolylineOptions()
//                                .add(marker.get(marker.size()-2).getPosition(), marker.get(marker.size()-1).getPosition())
//                                .width(5)
//                                .color(Color.RED));
//                    }
                        checkpointNumber++;

                        if (Checkpoint.checkpoints.size() <= checkpointNumber) {
                            Toast.makeText(getBaseContext(), "Schnitzeljagt erfolgreich beendet", Toast.LENGTH_LONG).show();
                            ((Button) findViewById(R.id.button)).setEnabled(false);
                            ((Button) findViewById(R.id.button1)).setEnabled(false);
                        } else {
                            Toast.makeText(getBaseContext(), "Checkpoint erreicht!!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                            intent.putExtra("imageNumber", checkpointNumber);
                            startActivity(intent);
                        }
                    }
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

        Button showHint = (Button) findViewById(R.id.button1);
        showHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location pos=mMap.getMyLocation();
                Checkpoint cp = Checkpoint.checkpoints.get(checkpointNumber);

                LatLng target = cp.getPosition();
                if(target==null || pos==null)
                {
                    Toast.makeText(getBaseContext(), "Keine Positionsdaten gefunden", Toast.LENGTH_LONG).show();
                }
                else{
                float[] results = new float[3];
                Location.distanceBetween(target.latitude, target.longitude, pos.getLatitude(), pos.getLongitude(), results);
                Toast.makeText(getBaseContext(), "Entfernung nach \"" + cp.getName() +"\": "+(int)results[0]+"m", Toast.LENGTH_LONG).show();
                }
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
        mMap.setMyLocationEnabled(true);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            marker.clear();
            mMap.clear();
            Checkpoint.checkpoints.clear();
            checkpointNumber=0;
        }
        return super.onKeyDown(keyCode, event);
    }
}
