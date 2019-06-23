package com.example.chuksy.playground;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by chuksy on 5/26/19.
 */

public class ShareEntries {

    protected String sharedText = "";

    public ShareEntries(Entry[] entries, Context c){

        for(Entry entry : entries){

            sharedText += entry.getShareAbleMessage();

        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
        sendIntent.setType("text/plain");
        c.startActivity(Intent.createChooser(sendIntent, c.getResources().getText(R.string.shareText)));

    }


}
