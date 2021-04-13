package com.sugardaddy.citascasualesconocergentenuevacercadeti.pushNotifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.parse.ManifestInfo;
import com.parse.PLog;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class NotificationUtils {

    private String TAG = NotificationUtils.class.getSimpleName();

    /**
     * The name of the Intent extra which contains the JSON payload of the Notification.
     */
    private static final String KEY_PUSH_DATA = "com.parse.Data";

    private static final int SMALL_NOTIFICATION_MAX_CHARACTER_LIMIT = 38;
    private static final String PROPERTY_PUSH_ICON = "com.parse.push.notification_icon";

    private Context mContext;

    public NotificationUtils() {
    }

    NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Retrieves the channel to be used in a {@link Notification} if API >= 26, if not null. The default returns a new channel
     * with id "parse_push", name "Push notifications" and default importance.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The notification channel
     */
    @TargetApi(Build.VERSION_CODES.O)
    private NotificationChannel getNotificationChannel(Context context, Intent intent) {
        return new NotificationChannel("parse_push", "Push notifications", NotificationManager.IMPORTANCE_DEFAULT);
    }

    /**
     * Creates the notification channel with the NotificationManager. Channel is not recreated
     * if the channel properties are unchanged.
     *
     * @param context             The {@code Context} in which the receiver is running.
     * @param notificationChannel The {@code NotificationChannel} to be created.
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context, NotificationChannel notificationChannel) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Get the push data as a parsed JSONObject
     *
     * @param intent the intent of the notification
     * @return the parsed JSONObject, or null
     */
    private JSONObject getPushData(Intent intent) {
        try {
            return new JSONObject(Objects.requireNonNull(intent.getStringExtra(KEY_PUSH_DATA)));
        } catch (JSONException e) {
            PLog.e(TAG, "Unexpected JSONException when receiving push data: ", e);
            return null;
        }
    }

    /**
     * Get the content intent, which is the intent called when a notification is tapped. Note that if
     * you override this, you will want to set the action to {@link ParsePushBroadcastReceiver#ACTION_PUSH_OPEN} in order
     * to still trigger {@link //#onPushOpen(Context, Intent)}
     *
     * @param extras      the extras
     * @param packageName the app package name
     * @return the intent
     */
    private Intent getContentIntent(Bundle extras, String packageName) {
        Intent contentIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_OPEN);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);
        return contentIntent;
    }

    /**
     * Get the delete intent, which is the intent called when a notification is deleted (swiped away). Note that if
     * you override this, you will want to set the action to {@link ParsePushBroadcastReceiver#ACTION_PUSH_DELETE} in order
     * to still trigger {@link //#onPushOpen(Context, Intent)}
     *
     * @param extras      the extras
     * @param packageName the app package name
     * @return the intent
     */
    private Intent getDeleteIntent(Bundle extras, String packageName) {
        Intent contentIntent = new Intent(ParsePushBroadcastReceiver.ACTION_PUSH_DELETE);
        contentIntent.putExtras(extras);
        contentIntent.setPackage(packageName);
        return contentIntent;
    }

    /**
     * Retrieves the small icon to be used in a {@link Notification}. The default implementation uses
     * the icon specified by {@code com.parse.push.notification_icon} {@code meta-data} in your
     * {@code AndroidManifest.xml} with a fallback to the launcher icon for this package. To conform
     * to Android style guides, it is highly recommended that developers specify an explicit push
     * icon.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The resource id of the default small icon for the package
     * @see <a href="http://developer.android.com/design/style/iconography.html#notification">Android Notification Style Guide</a>
     */
    private int getSmallIconId(Context context, Intent intent) {
        Bundle metaData = ManifestInfo.getApplicationMetadata(context);
        int explicitId = 0;
        if (metaData != null) {
            explicitId = metaData.getInt(PROPERTY_PUSH_ICON);
        }
        return explicitId != 0 ? explicitId : ManifestInfo.getIconId();
    }

    /**
     * Retrieves the large icon to be used in a {@link Notification}. This {@code Bitmap} should be
     * used to provide special context for a particular {@link Notification}, such as the avatar of
     * user who generated the {@link Notification}. The default implementation returns {@code null},
     * causing the {@link Notification} to display only the small icon.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return Bitmap of the default large icon for the package
     * @see <a href="http://developer.android.com/guide/topics/ui/notifiers/notifications.html#NotificationUI">Android Notification UI Overview</a>
     */
    private Bitmap getLargeIcon(Context context, Intent intent) {
        return null;
    }

    /**
     * Creates a {@link Notification} with reasonable defaults. If "alert" and "title" are
     * both missing from data, then returns {@code null}. If the text in the notification is longer
     * than 38 characters long, the style of the notification will be set to
     * {@link android.app.Notification.BigTextStyle}.
     * <p/>
     * As a security precaution, developers overriding this method should be sure to set the package
     * on notification {@code Intent}s to avoid leaking information to other apps.
     *
     * @param context The {@code Context} in which the receiver is running.
     * @param intent  An {@code Intent} containing the channel and data of the current push notification.
     * @return The notification builder to be displayed.
     */
    @Nullable
    NotificationCompat.Builder getNotification(Context context, Intent intent) {
        JSONObject pushData = getPushData(intent);
        if (pushData == null || (!pushData.has("alert") && !pushData.has("title"))) {
            return null;
        }

        String title = pushData.optString("title", ManifestInfo.getDisplayName(context));
        String alert = pushData.optString("alert", "Notification received.");
        String tickerText = String.format(Locale.getDefault(), "%s: %s", title, alert);

        Bundle extras = intent.getExtras();

        Random random = new Random();
        int contentIntentRequestCode = random.nextInt();
        int deleteIntentRequestCode = random.nextInt();

        // Security consideration: To protect the app from tampering, we require that intent filters
        // not be exported. To protect the app from information leaks, we restrict the packages which
        // may intercept the push intents.
        String packageName = context.getPackageName();

        Intent contentIntent = getContentIntent(extras, packageName);

        Intent deleteIntent = getDeleteIntent(extras, packageName);

        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, contentIntentRequestCode,
                contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pDeleteIntent = PendingIntent.getBroadcast(context, deleteIntentRequestCode,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = getNotificationChannel(context, intent);
            createNotificationChannel(context, notificationChannel);
            channelId = notificationChannel.getId();
        }

        //we can ignore the fact that the channel Id might be null, it is fine for versions prior to O
        @SuppressWarnings("ConstantConditions")
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
        notificationBuilder.setContentTitle(title)
                .setContentText(alert)
                .setTicker(tickerText)
                .setSmallIcon(this.getSmallIconId(context, intent))
                .setLargeIcon(this.getLargeIcon(context, intent))
                .setContentIntent(pContentIntent)
                .setDeleteIntent(pDeleteIntent)
                .setAutoCancel(true)
                // The purpose of setDefaults(Notification.DEFAULT_ALL) is to inherit notification properties
                // from system defaults
                .setDefaults(Notification.DEFAULT_ALL);

        if (alert.length() > SMALL_NOTIFICATION_MAX_CHARACTER_LIMIT) {
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(alert));
        }
        return notificationBuilder;
    }
}
