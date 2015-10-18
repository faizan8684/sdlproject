package com.goobers.steganography;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.software.shell.fab.ActionButton;

import java.io.File;

public class EncodeActivity extends Activity {

    private File baseImage;
    private boolean isBase = true;
    private File secretImage;
    private File encodedTempImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        ActionButton actionButton = (ActionButton) findViewById(R.id.fab_encode);
        actionButton.setImageResource(R.drawable.ic_arrow_forward_black_24dp);

        encodedTempImage = new File(getCacheDir(), "temp.png");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encode, menu);
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

    public void uploadImage1(View v) {
        isBase = true;
        uploadImage();
    }

    public void uploadImage2(View v) {
        isBase = false;
        uploadImage();
    }

    public static final int SELECT_PICTURE = 1; //for the result listener
    private String selectedImagePath;

    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
            if (isBase) {
                baseImage = new File(selectedImagePath);
            } else {
                secretImage = new File(selectedImagePath);
            }
        }
    }


    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


    public void encode(View v) {
        if (baseImage != null && secretImage != null) {
            Encoder.encode(baseImage, secretImage, encodedTempImage);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "You need two images", Toast
                    .LENGTH_SHORT);
            toast.show();
        }
    }
}