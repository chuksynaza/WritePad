package com.example.chuksy.playground;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class StatsTracking extends AppCompatActivity {

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_tracking);

        //Open the database, so that we can read and write
        Database databaseConnection = new Database(this);

        database = databaseConnection.open();

        //Navigation

        new NavigationDrawer(this);

        ArrayList<Entry> entries = EntryTable.getAllWithEmotions(database);



        GraphView graph = (GraphView) findViewById(R.id.graph);

        int entriesSize = entries.size();

        DataPoint[] dataPoints = new DataPoint[entriesSize];

        for(int i = 0; i < entries.size(); i++){

            dataPoints[i] = new DataPoint(entries.get(i).getDateObjectTimeCreated(), entries.get(i).getEmotion());

        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(entries.get(0).getDateCreated());
        graph.getViewport().setMaxX(entries.get(entriesSize - 1).getDateCreated());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinY(1);

        graph.getViewport().setMaxY(5);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);



    }
}
