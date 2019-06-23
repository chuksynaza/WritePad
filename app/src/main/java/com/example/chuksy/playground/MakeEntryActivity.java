package com.example.chuksy.playground;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MakeEntryActivity extends AppCompatActivity {

    public static int lastEditTextID = R.id.editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_entry);

    }


    public void addImage(View view){

        Log.d("PLAY", "Add Image Clicked");

        EditText lastEditText = findViewById(lastEditTextID);

        lastEditText.addTextChangedListener(new EditTextChangedListener(lastEditTextID));

        LinearLayout entryMainContainer = findViewById(R.id.entryMainContainer);

        lastEditText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Button myButton = new Button(this);

        lastEditText.setHint("");

        entryMainContainer.addView(myButton);

        EditText myEditText = new EditText(this);

        myEditText.addTextChangedListener(new EditTextChangedListener(myEditText.getId()));

        myEditText.setGravity(Gravity.TOP);

        myEditText.setHint(R.string.entryTextHint);

        myEditText.setBackgroundResource(0);

        myEditText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        lastEditTextID = ChuksyUtils.generateViewId();

        myEditText.setId(lastEditTextID);

        entryMainContainer.addView(myEditText);

    }

    class EditTextChangedListener implements TextWatcher {

        public int currentEditTextId;

        public EditTextChangedListener(int currentEditTextId){

            this.currentEditTextId = currentEditTextId;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {

            String theCurrentEditTextString = s.toString();

            Log.d("PLAY", "The after Text Changed value is: (" + theCurrentEditTextString + ")");

            if(theCurrentEditTextString.equals("") && this.currentEditTextId != lastEditTextID){

                //Remove the edittext if the text is empty

                Log.d("PLAY", "Removing the edit text since it is empty");

                LinearLayout entryMainContainer = findViewById(R.id.entryMainContainer);


                entryMainContainer.removeView(findViewById(this.currentEditTextId));

            }

        }

    }



}



