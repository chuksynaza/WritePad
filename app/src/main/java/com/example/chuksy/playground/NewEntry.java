package com.example.chuksy.playground;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class NewEntry extends AppCompatActivity implements SelectDateClickListener.SelectDateClickInterface, LocationProvider.LocationListenerInterface {

    public static int KEY_SELECT_FEELING_REQUEST_CODE = 1;

    public static String KEY_DEFAULT_FEELING_LEVEL = "defaultFeelingLevel";

    protected TextInputEditText entryTitle;

    protected EditText entryMessage;

    protected ImageButton feelingIcon;

    protected TextView feelingMessage;

    protected TextView entryDate;

    protected TextView entryLocation;

    protected ImageButton entryDatePicker;

    protected ImageButton entryLocationSelector;

    protected ImageButton entryImageSelector;

    protected SQLiteDatabase database;

    protected Entry entry;

    protected LocationProvider locationProvider;

    protected TakePhoto takePhoto;

    protected ImageView entryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        //Open the database, so that we can read and write
        Database databaseConnection = new Database(this);

        database = databaseConnection.open();

        Intent theIntent = getIntent();

        feelingIcon = findViewById(R.id.feelingIcon);

        feelingMessage = findViewById(R.id.feelingMessage);

        FloatingActionButton completeEntry = findViewById(R.id.completeEntry);

        entryTitle = findViewById(R.id.entryTitle);

        entryMessage = findViewById(R.id.entryMessage);

        entryDate = findViewById(R.id.entryDate);

        entryLocation = findViewById(R.id.entryLocation);

        entryDatePicker = findViewById(R.id.entryDatePicker);

        entryLocationSelector = findViewById(R.id.entryLocationSelector);

        entryImageSelector = findViewById(R.id.entryImageSelector);

        entryImage = findViewById(R.id.entryImage);

        feelingIcon.setOnClickListener(new SelectFeelingClick());

        feelingMessage.setOnClickListener(new SelectFeelingClick());

        SelectDateClickListener dateClickListener = new SelectDateClickListener(this);

        locationProvider = new LocationProvider(this);

        entryDatePicker.setOnClickListener(dateClickListener);

        entryLocationSelector.setOnClickListener(locationProvider);

        takePhoto = new TakePhoto(this);

        entryImageSelector.setOnClickListener(takePhoto);


        completeEntry.setOnClickListener(new CompleteEntry());


        long entryID = theIntent.getLongExtra(MainActivity.KEY_ENTRY_ID, 0);

        if(entryID == 0){

            Log.d("PLAY", "Creating a new entry");

            entry = new Entry(entryID);

            updateView();

        } else {

            Log.d("PLAY", "Fetching entry: " + entryID);

            entry = EntryTable.getEntry(database, entryID);

            Log.d("PLAY", "Retrieved the entry with ID: " + entry.getID() + ", Title: " + entry.getTitle() + ", Message: " + entry.getMessage() + ", Feeling Level: " + entry.getEmotion());

            updateView();

        }


    }

    private void setPic(ImageView theImageView, String imagePath){

        Bitmap theImageBitmap = BitmapFactory.decodeFile(imagePath);

        theImageView.setImageBitmap(theImageBitmap);

        theImageView.setVisibility(View.VISIBLE);

    }

    void updateView(){

        entryTitle.setText(entry.getTitle());

        entryMessage.setText(entry.getMessage());

        feelingIcon.setImageResource(entry.getFeelingIcon());

        feelingMessage.setText(entry.getFeelingMessage());

        feelingIcon.setColorFilter(getResources().getColor(entry.getFeelingColor()));

        entryDate.setText(entry.getRelativeDateCreated());

        entryLocation.setText(entry.getShortAddress());

        String entryImageUrl = entry.getImageUrl();

        Log.d("PLAY", "ENTTRTT " + entryImageUrl);

        File imgFile = new  File(entryImageUrl);


        if(imgFile.exists())
        {
            Log.d("PLAY", "Setting the entryImage " + Uri.fromFile(imgFile).toString());

            //Checking for permissions and displaying image

            if(!TakePhoto.getExternalDirectoryPermissions(this)){

                Toast.makeText(getApplicationContext(), "You need to allow permissions for this", Toast.LENGTH_LONG).show();

                return;

            }

            setPic(entryImage, entryImageUrl);

        }


    }

    void updateData(){

        String theEntryTitle = entryTitle.getText().toString();

        String theEntryMessage = entryMessage.getText().toString();

        entry.updateData(entry.getEmotion(), theEntryTitle, theEntryMessage);

    }

    void updateDate(long newDate){

        entry.setDateCreated(newDate);

    }

    boolean saveEntry(){

        updateData();

        Log.d("PLAY", "Saving the Entry with ID: " + entry.getID() + ", Title: " + entry.getTitle() + ", Message: " + entry.getMessage() + ", Feeling Level: " + entry.getEmotion());

        if(entry.getID() == 0){

            long insertID = EntryTable.create(database, entry);

            if(insertID != -1){

                Log.d("PLAY", "The new entry ID is: " + insertID);

                entry.setID(insertID);

            } else {

                return false;

            }

        } else {

            EntryTable.update(database, entry);

        }

        return true;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == NewEntry.KEY_SELECT_FEELING_REQUEST_CODE){

            if(resultCode == Activity.RESULT_OK){

                int selectedFeelingLevel = data.getIntExtra(SelectFeeling.KEY_FEELING_LEVEL, 0);

                Log.d("PLAY", "The Current Feeling Selected is: " + selectedFeelingLevel);

                entry.setEmotion(selectedFeelingLevel);

                updateView();

            }

        } else if(requestCode == TakePhoto.KEY_REQUEST_IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {

            Log.d("PLAY", "Returned from Take Photo with path: " + takePhoto.currentPhotoPath);

            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");



            //imageView.setImageBitmap(imageBitmap);

            entry.setImageUrl(takePhoto.currentPhotoPath);

            updateData();

            updateView();

        }

    }

    @Override
    public void retrieveSelectedDate(long selectedDate) {

        Log.d("PLAY", "New Date Selected: " + selectedDate);

        updateDate(selectedDate);

        updateData();

        updateView();

    }

    @Override
    public void retrieveLocation(String[] address, Location location) {

        Log.d("PLAY", "Location Updated - City: " + address[0] + " State: " + address[1] + " Country: " + address[2]);

        entry.setAddress(address[0]);

        updateData();

        updateView();


    }


    class SelectFeelingClick implements View.OnClickListener {

        public SelectFeelingClick(){

        }

        @Override
        public void onClick(View v) {

            Log.d("PLAY", "Select Feeling Clicked");

            updateData();

            Intent selectFeelingIntent = new Intent(getApplicationContext(), SelectFeeling.class);

            selectFeelingIntent.putExtra(NewEntry.KEY_DEFAULT_FEELING_LEVEL, entry.getEmotion());

            startActivityForResult(selectFeelingIntent, NewEntry.KEY_SELECT_FEELING_REQUEST_CODE);


        }

    }

    class CompleteEntry implements View.OnClickListener {

        public CompleteEntry(){

        }

        @Override
        public void onClick(View v) {

            Log.d("PLAY", "Complete Entry Clicked");

            saveEntry();

            Intent returnToListIntent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(returnToListIntent);


        }

    }

}
