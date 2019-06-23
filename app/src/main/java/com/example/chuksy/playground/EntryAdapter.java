package com.example.chuksy.playground;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chuksy on 5/23/19.
 */

public class EntryAdapter extends ArrayAdapter<Entry> {

    private int mLayoutResourceID;

    //Storing the list of currently selected Items in a sparseArray

    protected SparseArrayCompat<Boolean> selectedItems = new SparseArrayCompat<Boolean>();


    public EntryAdapter(Context context, int resource, List<Entry> objects){

        super(context, resource, objects);

        this.mLayoutResourceID = resource;

    }

    public void addSelection(int position){

        selectedItems.put(position, true);

        //reloading the data sets to allow the adapter refresh and then add the selection background

        notifyDataSetChanged();

    }

    public void removeSelection(int position){

        selectedItems.remove(position);

        //reloading the data sets to allow the adapter refresh and then add the selection background

        notifyDataSetChanged();

    }

    public void clearSelection(){

        Log.d("PLAY", "Clearing selected items");

        selectedItems.clear();

        //reloading the data sets to allow the adapter refresh and then add the selection background

        notifyDataSetChanged();

    }

    public int countSelection(){

        return selectedItems.size();

    }


    public int[] getSelectedPositions(){

        int selectedItemSize = selectedItems.size();

        int[] selectedItemPositions = new int[selectedItemSize];

        for(int i = 0; i < selectedItems.size(); i++){

            selectedItemPositions[i] = selectedItems.keyAt(i);

        }

        return selectedItemPositions;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        View row = layoutInflater.inflate(mLayoutResourceID, parent, false);

        CardView entryCardView = row.findViewById(R.id.entryCardView);

        ImageView selectedEntryCheck = row.findViewById(R.id.selectedEntryCheck);

        TextView entryTitle = row.findViewById(R.id.entryTitle);

        TextView entryMessageExcerpt = row.findViewById(R.id.entryMessageExcerpt);

        TextView entryDate = row.findViewById(R.id.entryDate);

        TextView entryFeelingMessage = row.findViewById(R.id.entryFeelingMessage);

        ImageView entryFeelingIcon = row.findViewById(R.id.entryFeelingIcon);


        Entry entry = this.getItem(position);

        entryTitle.setText(entry.getReadableTitle());

        entryMessageExcerpt.setText(entry.getMessageExcerpt());

        entryDate.setText(entry.getRelativeDateCreated());

        if(entry.getEmotion() != 0){

            entryFeelingIcon.setVisibility(View.VISIBLE);

            entryFeelingMessage.setVisibility(View.VISIBLE);

            entryFeelingMessage.setText(entry.getFeelingMessage());

            entryFeelingIcon.setImageResource(entry.getFeelingIcon());

            entryFeelingIcon.setColorFilter(getContext().getResources().getColor(entry.getFeelingColor()));

        }

        //Default unselected view

        selectedEntryCheck.setVisibility(View.GONE);

        entryCardView.setCardBackgroundColor(row.getResources().getColor(R.color.cardview_light_background));

        //Check if it is selected and update the view

        if(selectedItems.get(position) != null){

            selectedEntryCheck.setVisibility(View.VISIBLE);

            entryCardView.setCardBackgroundColor(row.getResources().getColor(R.color.chuksy_color_selected));

        }


        return row;

    }


}
