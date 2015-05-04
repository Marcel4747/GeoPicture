package com.example.dennis.classes;

import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Dennis on 04.05.2015.
 */
public class Checkpoint {
    private LatLng position;
    private Image image;

    public Checkpoint(LatLng pos) {
        this.position = pos;
//        this.image = im;
    }

    public LatLng getPosition() {
        return position;
    }

    public Image getImage() {
        return image;
    }
}
