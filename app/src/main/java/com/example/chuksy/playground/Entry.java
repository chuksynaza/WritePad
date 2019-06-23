package com.example.chuksy.playground;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chuksy on 5/22/19.
 */

public class Entry {

    private long ID;
    private int emotion;
    private String title = "";
    private String message = "";
    private long dateCreated = System.currentTimeMillis();
    private long dateUpdated;
    private String imageUrl = "";
    private String address = "";


    public static int[] feelingIcons = {

            R.drawable.ic_feedback_black_24dp,
            R.drawable.ic_sentiment_very_dissatisfied_black_24dp,
            R.drawable.ic_sentiment_dissatisfied_black_24dp,
            R.drawable.ic_sentiment_neutral_black_24dp,
            R.drawable.ic_sentiment_satisfied_black_24dp,
            R.drawable.ic_sentiment_very_satisfied_black_24dp

    };

    public static String[] feelingMessages = {

            "Select feeling",
            "Feeling terrible",
            "Feeling down",
            "Feeling neutral",
            "Feeling good",
            "Feeling awesome"

    };

    public static int[] feelingColors = {

            R.color.chuksy_color_dark_grey,
            R.color.chuksy_color_dark_red,
            R.color.chuksy_color_dark_orange,
            R.color.chuksy_color_neutral,
            R.color.chuksy_color_dark_blue,
            R.color.chuksy_color_dark_green

    };

    public Entry(long ID, int emotion, String title, String message, String address, String imageUrl, long dateCreated, long dateUpdated){

        this.ID = ID;
        this.emotion = emotion;
        this.title = title;
        this.message = message;
        this.address = address;
        this.imageUrl = imageUrl;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;

    }

    public Entry(long ID, int emotion, String title, String message){

        this.ID = ID;
        this.emotion = emotion;
        this.title = title;
        this.message = message;


    }

    public Entry(long ID){

        setID(ID);

    }

    public void setID(long ID){

        this.ID = ID;
    }

    public void setEmotion(int emotion){

        this.emotion = emotion;

    }

    public void setImageUrl(String url){

        this.imageUrl = url;

    }

    public void setAddress(String address){

        this.address = address;

    }

    public void updateData(int emotion, String title, String message){

        this.emotion = emotion;
        this.title = title;
        this.message = message;

    }

    public String getImageUrl(){

        return this.imageUrl;

    }

    public String getAddress(){

        return this.address;

    }

    public String getShortAddress(){

        String theAddress = getAddress();

        if(theAddress.length() > 30){

            theAddress = theAddress.substring(0, 30) + "...";

        }

        return theAddress;

    }



    public long getID(){

        return this.ID;

    }

    public int getEmotion(){

        return this.emotion;

    }

    public String getTitle(){

        return this.title;

    }

    public String getReadableTitle(){

        return getTitle().trim().equals("") ? "Untitled" : getTitle();

    }

    public String getReadableTitleWithPrefix(){

        return getTitle().trim().equals("") ? "Untitled" : "Title: " + getTitle();

    }

    public String getMessage(){

        return this.message;

    }

    public String getMessageExcerpt(){

        String theMessage = getMessage();

        if(theMessage.length() > 30){

            theMessage = theMessage.substring(0, 30) + "... ";

        }

        return theMessage;

    }

    public long getDateCreated(){

        return this.dateCreated;

    }

    public void setDateCreated(long milliseconds){

        this.dateCreated = milliseconds;

    }

    public long getDateUpdated(){

        return this.dateUpdated;

    }

    public String getRelativeDateCreated(){

        //Convert to readable date

        long now = System.currentTimeMillis();

        String relativeDate = DateUtils.getRelativeTimeSpanString(getDateCreated(), now, DateUtils.MINUTE_IN_MILLIS).toString();

        if(relativeDate.trim().equals("0 minutes ago")){

            relativeDate = "Just now";

        }

        return relativeDate;

    }

    public Date getDateObjectTimeCreated(){

        return new Date(getDateCreated());

    }

    public String getDateTimeCreated(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

        return dateFormat.format(new Date(getDateCreated()));

    }

    public int getFeelingIcon(){

        return feelingIcons[getEmotion()];

    }

    public static String getFeelingMessage(int emotion){

        return feelingMessages[emotion];

    }

    public String getFeelingMessage(){

        return getFeelingMessage(getEmotion());

    }

    public static int getFeelingColor(int emotion){

        return feelingColors[emotion];

    }

    public int getFeelingColor(){

        return getFeelingColor(getEmotion());

    }

    public String getShareAbleMessage(){

        return "On " + getDateTimeCreated()
                + "\n\n" + getReadableTitleWithPrefix() + "\n"
                + (getEmotion() == 0 ? "" : "\n" + getFeelingMessage() + "\n")
                + "\n" + getMessage()
                + "\n\n";

    }


}
