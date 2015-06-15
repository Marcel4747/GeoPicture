package com.example.dennis.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.do1900.android.utils.BitmapCache;
import de.do1900.android.utils.ImageReceiver;
import de.do1900.persistence.data.ImageInformation;
import de.do1900.rest.service.APIServiceConnection;
import de.do1900.rest.service.ServiceStateInterface;

/**
 * Created by Dennis on 04.05.2015.
 */
public class Checkpoint implements Serializable {
    static public List<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
    private LatLng position;
    private Bitmap image;
    private String name;
    private String description;

    private de.do1900.persistence.data.Location item;
    private Context context;
    private APIServiceConnection apiSc;

    public Checkpoint(de.do1900.persistence.data.Location item, Context context, APIServiceConnection apiSc) {
        this.item = item;
        this.context = context;
        this.apiSc = apiSc;
    }

    public LatLng getPosition() {
        if (position == null)
            this.position = new LatLng(item.getLatitude(), item.getLongitude());
        return position;
    }

    public void loadImage() {
        if (image == null) {
            if (item.getImageInformation().size() > 0) {

                Log.d("SSSSSSS", "" + item.getImageInformation().size());
                ImageInformation i = item.getImageInformation().get(0);
                BitmapCache bitmapCache = BitmapCache.instance(context);
                bitmapCache.setImage(new ImageReceiver() {
                    @Override
                    public void setImageBitmap(Bitmap bitmap) {
//                      bild.setImageBitmap(bitmap);
                        image = bitmap;
                    }
                }, i, apiSc, 2);
            }
        }
    }

    public boolean hasImages() {
        return item.getImageInformation().size() > 0;
    }

    public Bitmap getImage() {
        if (image == null)
            loadImage();
        return image;
    }

    public String getDescription() {
        if (description == null)
            this.description = item.getDescription();
        return description;
    }

    public String getName() {
        if (name == null)
            this.name = item.getName();
        return name;
    }

    public long getId() {
        return item.getId();
    }
}
