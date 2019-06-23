package com.example.chuksy.playground;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chuksy on 5/27/19.
 */

public class TakePhoto implements View.OnClickListener {

    private Activity theActivity;

    public static final int KEY_REQUEST_IMAGE_CAPTURE_CODE = 10;

    public static final int KEY_READ_EXTERNAL_STORAGE_PERMISSION_CODE = 12;

    public TakePhoto(Activity activity){

        theActivity = activity;

    }

    String currentPhotoPath;

    public static boolean getExternalDirectoryPermissions(Activity theActivity){

        if (ContextCompat.checkSelfPermission(theActivity, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED ) {

            return true;


        } else {

            ActivityCompat.requestPermissions(theActivity, new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE },
                    KEY_READ_EXTERNAL_STORAGE_PERMISSION_CODE);

            return (ContextCompat.checkSelfPermission(theActivity, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED);

        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = theActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(theActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                // Error occurred while creating the File

                Log.d("PLAY", "An exception occured while creating the file " + ex.getMessage());

                Toast.makeText(theActivity, "Unable to save image", Toast.LENGTH_LONG).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(theActivity.getApplicationContext(),
                        theActivity.getPackageName(),
                        photoFile);
                Log.d("PLAY", "THE URI ISS" + photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                theActivity.startActivityForResult(takePictureIntent, TakePhoto.KEY_REQUEST_IMAGE_CAPTURE_CODE);
            }
        }
    }

    @Override
    public void onClick(View v) {

        Log.d("PLAY", "Take Photo Clicked");

        if(!getExternalDirectoryPermissions(theActivity)){

            Toast.makeText(theActivity, "You need to allow permissions for this", Toast.LENGTH_LONG).show();

            return;

        }

        dispatchTakePictureIntent();

    }

}
