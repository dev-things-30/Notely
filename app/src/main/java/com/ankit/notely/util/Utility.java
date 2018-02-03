package com.ankit.notely.util;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by user on 23-01-2018.
 */

public class Utility {

    public static String getDate(long timeInMillis) {
        if (DateUtils.isToday(timeInMillis)) {
            return DateUtils.getRelativeTimeSpanString(timeInMillis).toString();
        } else {
            Date d = new Date(timeInMillis);
            return DateFormat.getDateTimeInstance().format(d);
        }
    }
}
