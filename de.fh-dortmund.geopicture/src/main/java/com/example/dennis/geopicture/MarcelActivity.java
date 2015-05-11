package com.example.dennis.geopicture;

import android.app.Activity;
import android.app.Service;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import de.do1900.android.utils.BitmapCache;
import de.do1900.android.utils.ImageReceiver;
import de.do1900.android.views.ImageReceivingView;
import de.do1900.persistence.data.ImageInformation;
import de.do1900.persistence.data.Location;
import de.do1900.rest.service.APIServiceConnection;
import de.do1900.rest.service.ServiceResponseInterface;
import de.do1900.rest.service.ServiceStateInterface;

public class MarcelActivity extends Activity implements ServiceStateInterface, ServiceResponseInterface {

    ImageView bild;

    private APIServiceConnection serviceConnection = new APIServiceConnection(
            this);

    BitmapCache bitmapCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcel);
        Location location;

        bild = (ImageView) findViewById(R.id.bild);
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
        Location[] lo = (Location[]) o2;

        for (Location item : lo) {
            Log.d("Test:", item.getName());
        }

        ImageInformation i = lo[0].getImageInformation().get(0);
        bitmapCache = BitmapCache.instance(this);
        bitmapCache.setImage(new ImageReceiver() {
            @Override
            public void setImageBitmap(Bitmap bitmap) {
                bild.setImageBitmap(bitmap);
            }
        }, i, serviceConnection, 2);
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