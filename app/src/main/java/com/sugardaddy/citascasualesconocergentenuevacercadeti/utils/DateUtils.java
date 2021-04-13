package com.sugardaddy.citascasualesconocergentenuevacercadeti.utils;

import android.util.Log;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {

    // This class should not be initialized
    private DateUtils() {

    }


    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    private static String formatTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * If the given time is of a different date, display the date.
     * If it is of the same date, display the time.
     * @param timeInMillis  The time to convert, in milliseconds.
     * @return  The time or date.
     */
    public static String formatDateTime(long timeInMillis) {
        if(isToday(timeInMillis)) {
            return formatTime(timeInMillis);
        } else {
            return formatDate(timeInMillis);
        }
    }

    public static String formatDateAndTime(long timeInMillis) {
        if(isToday(timeInMillis)) {
            return formatTime(timeInMillis);
        } else {
            return formatTimeAndDate(timeInMillis);
        }
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3, 2019').
     */
    private static String formatTimeAndDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy, HH:mm", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3').
     */
    private static String formatDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Returns whether the given date is today, based on the user's current locale.
     */
    private static boolean isToday(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String date = dateFormat.format(timeInMillis);
        return date.equals(dateFormat.format(System.currentTimeMillis()));
    }

    /*
     * Get time ago
     *
     */

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        Log.e("eee", "" + now + "   " + time);
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return Application.getInstance().getBaseContext().getString(R.string.time_now);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return Application.getInstance().getBaseContext().getString(R.string.time_one_minute_ago);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " " + Application.getInstance().getBaseContext().getString(R.string.time_minutes_ago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return Application.getInstance().getBaseContext().getString(R.string.time_one_hour_ago);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " " + Application.getInstance().getBaseContext().getString(R.string.time_hours_ago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return Application.getInstance().getBaseContext().getString(R.string.time_yesterday);
        } else {
            if ((diff / DAY_MILLIS) > 365) {
                return ((int) ((diff / DAY_MILLIS) / 365)) + " " + Application.getInstance().getBaseContext().getString(R.string.time_years_ago);
            } else if ((diff / DAY_MILLIS) > 31) {
                return ((int) (diff / DAY_MILLIS) / 31) + " " + Application.getInstance().getBaseContext().getString(R.string.time_months_ago);
            } else {
                return diff / DAY_MILLIS + " " + Application.getInstance().getBaseContext().getString(R.string.time_days_ago);
            }
        }
    }

}
