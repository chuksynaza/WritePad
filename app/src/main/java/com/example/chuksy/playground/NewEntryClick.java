package com.example.chuksy.playground;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

/**
 * Created by chuksy on 4/30/19.
 */

class NewEntryClick implements View.OnClickListener {

    private Context theContext;

    public NewEntryClick(Context c){

        theContext = c;

    }

    @Override
    public void onClick(View v) {

        Log.d("PLAY", "New Entry FAB Clicked");

        Intent newEntryIntent = new Intent(theContext, NewEntry.class);

        newEntryIntent.putExtra(MainActivity.KEY_ENTRY_ID, (long) 0);

        theContext.startActivity(newEntryIntent);


    }
}
