package com.example.dennis.geopicture;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button buttonMap =  (Button)findViewById(R.id.buttonStartMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        Button buttonDennis =  (Button)findViewById(R.id.buttonStartDennis);
        buttonDennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DennisActivity.class);
                startActivity(intent);
            }
        });

        Button buttonMarcel =  (Button)findViewById(R.id.buttonStartMarcel);
        buttonMarcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MarcelActivity.class);
                startActivity(intent);
            }
        });
    }
}

//        e:
//        cd "E:\Programme\Android\SDK\platform-tools"
//        adb install "E:\Programme\Android\SDK\platform-tools\Do1900Client.apk"
