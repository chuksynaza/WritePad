package com.example.chuksy.playground;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chuksy on 5/6/19.
 */

public class ChuksyUtils {

    public static int resourceIDFromString(Context context, String theString){

        return context.getResources().getIdentifier(theString, "id", context.getPackageName());

    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    @SuppressLint("NewApi")
    public static int generateViewId() {

        if (Build.VERSION.SDK_INT < 17) {

            for (;;) {

                final int result = sNextGeneratedId.get();

                int newValue = result + 1;

                if (newValue > 0x00FFFFFF)

                    newValue = 1; // Roll over to 1, not 0.

                if (sNextGeneratedId.compareAndSet(result, newValue)) {

                    return result;

                }
            }

        } else {

            return View.generateViewId();

        }
    }



}
