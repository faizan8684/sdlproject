package com.goobers.steganography;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

public class ImageActivity extends Activity {

    private static final String LOG_TAG = ImageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent intent = getIntent();
        String displayPath = intent.getStringExtra(EncodeActivity.EXTRA_FILE_TAG);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(displayPath);
        imageView.setImageBitmap(bitmap);
        Calendar calendar = Calendar.getInstance();
        String name = "" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get
                (Calendar.DATE) + calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.SECOND);
        File tempFile = new File(Environment.getExternalStorageDirectory().getPath() +
                "/Pictures/Screenshots/" + name + ".png");
        try {
            tempFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

}
