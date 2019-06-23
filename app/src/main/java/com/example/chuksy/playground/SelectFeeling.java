package com.example.chuksy.playground;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectFeeling extends AppCompatActivity {

    public static String KEY_FEELING_LEVEL = "selectedFeelingLevel";

    private int currentFeeling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_feeling);

        Intent theIntent = getIntent();

        int defaultFeeling = theIntent.getIntExtra(NewEntry.KEY_DEFAULT_FEELING_LEVEL, 0);

        Log.d("PLAY", "Default Feeling is: " + defaultFeeling);

        currentFeeling = defaultFeeling;

        updateFeelingView(0, currentFeeling);


        ImageView feelingLevel1 = findViewById(R.id.feelingLevel1);
        ImageView feelingLevel2 = findViewById(R.id.feelingLevel2);
        ImageView feelingLevel3 = findViewById(R.id.feelingLevel3);
        ImageView feelingLevel4 = findViewById(R.id.feelingLevel4);
        ImageView feelingLevel5 = findViewById(R.id.feelingLevel5);

        feelingLevel1.setOnClickListener(new FeelingClick(1));
        feelingLevel2.setOnClickListener(new FeelingClick(2));
        feelingLevel3.setOnClickListener(new FeelingClick(3));
        feelingLevel4.setOnClickListener(new FeelingClick(4));
        feelingLevel5.setOnClickListener(new FeelingClick(5));

        FloatingActionButton completeFeelingSelection = findViewById(R.id.completeFeelingSelection);

        completeFeelingSelection.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Log.d("PLAY", "Returning back with selected feeling as: " + currentFeeling);

                Intent returnIntent = getIntent();

                returnIntent.putExtra("selectedFeelingLevel", currentFeeling);

                setResult(Activity.RESULT_OK, returnIntent);

                finish();

            }

        });

    }

    void updateFeelingView(int previousFeelingLevel, int newFeelingLevel){

        if(previousFeelingLevel != 0){

            String previousResourceIDName = "feelingLevel" + previousFeelingLevel;

            int previousFeelingResourceID = ChuksyUtils.resourceIDFromString(getApplicationContext(), previousResourceIDName);

            Log.d("PLAY", "Previous Feeling Resource ID: " + previousFeelingResourceID);

            ImageView previousFeeling = findViewById(previousFeelingResourceID);

            previousFeeling.setColorFilter(getApplicationContext().getResources().getColor(Entry.getFeelingColor(0)));

        }

        if(newFeelingLevel != 0){

            String newResourceIDName = "feelingLevel" + newFeelingLevel;

            int newFeelingResourceID = ChuksyUtils.resourceIDFromString(getApplicationContext(), newResourceIDName);

            Log.d("PLAY", "New Feeling Resource ID: " + newFeelingResourceID);

            ImageView newFeeling = findViewById(newFeelingResourceID);

            newFeeling.setColorFilter(getApplicationContext().getResources().getColor(Entry.getFeelingColor(newFeelingLevel)));

        }


        TextView currentSelectionText = findViewById(R.id.currentSelectionText);

        currentSelectionText.setText(Entry.getFeelingMessage(newFeelingLevel));

    }


    class FeelingClick implements View.OnClickListener {

        private int theFeeling;

        public FeelingClick(int feeling){

            theFeeling = feeling;

        }

        @Override
        public void onClick(View v) {

            Log.d("PLAY", "User Selected a Feeling: " + theFeeling);

            int previousFeeling = currentFeeling;

            currentFeeling = theFeeling;

            Log.d("PLAY", "The New Current Feeling is: " + currentFeeling);

            updateFeelingView(previousFeeling, currentFeeling);

        }
    }

}
