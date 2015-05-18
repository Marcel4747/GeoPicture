package com.example.dennis.geopicture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.dennis.classes.Checkpoint;


public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        int imageNumber = intent.getIntExtra("imageNumber", 0);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        if (Checkpoint.checkpoints.size() > imageNumber) {
            Log.d("image", Checkpoint.checkpoints.get(imageNumber).getImage().toString());
            Bitmap image = Checkpoint.checkpoints.get(imageNumber).getImage();
            imageView.setImageBitmap(image);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
