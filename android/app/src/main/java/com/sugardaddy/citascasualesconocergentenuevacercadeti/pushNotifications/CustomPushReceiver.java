package com.sugardaddy.citascasualesconocergentenuevacercadeti.pushNotifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.SendNotifications;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.HomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.encounters.LikedYouActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = CustomPushReceiver.class.getSimpleName();

    // private Intent parseIntent;

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {

        String pushDataStr = intent.getStringExtra(KEY_PUSH_DATA);
        JSONObject pushData = null;
        try {
            assert pushDataStr != null;
            pushData = new JSONObject(pushDataStr);
        } catch (JSONException e) {
            Log.e(TAG, "Unexpected JSONException when receiving push data: ", e);

        }

        if (User.getUser() != null){

            // Check if app is opened
            if (Application.getInstance().isAppOpened()) {

                //Check if ChatActivity is Opened and objectId is not null
                if (Application.getInstance().isChatOpened() && Application.getInstance().getChatObjectId() != null){

                    // Check if Push is not from User in conversation

                    if (pushData != null) {

                        if (pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE).equals(SendNotifications.PUSH_TYPE_MESSAGES)){

                            if (!pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER).equals(Application.getInstance().getChatObjectId())){

                                showInAppNotification(pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER), pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER_NAME), pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER_AVATAR), pushData.optString(SendNotifications.PUSH_NOTIFICATION_MESSAGE), pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE));
                            }

                        } else {

                            showInAppNotification(pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER), pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER_NAME), pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER_AVATAR), pushData.optString(SendNotifications.PUSH_NOTIFICATION_MESSAGE), pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE));
                        }
                    }

                } else {

                    if (pushData != null) {
                        showInAppNotification(pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER), pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER_NAME), pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER_AVATAR), pushData.optString(SendNotifications.PUSH_NOTIFICATION_MESSAGE), pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE));
                    }

                }

            } else {

                showNotificationMessage(context, intent);
            }
        }
    }

    public void showInAppNotification(String objectId, String name, String avatar, String message, String type){

        QuickHelp.showInAppNotification(Application.getInstance().getCurrentActivity(), type, name, avatar, message, objectId);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        ParseAnalytics.trackAppOpenedInBackground(intent);
        super.onPushOpen(context, intent);
    }


    private void showNotificationMessage(Context context, Intent intent) {

        NotificationUtils notificationUtils = new NotificationUtils(context);

        String pushDataStr = intent.getStringExtra(KEY_PUSH_DATA);
        if (pushDataStr == null) {
            Log.v(TAG, "Can not get push data from intent.");
            return;
        }
        Log.v(TAG, "Received push data: " + pushDataStr);

        JSONObject pushData = null;
        try {
            pushData = new JSONObject(pushDataStr);
        } catch (JSONException e) {
            Log.e(TAG, "Unexpected JSONException when receiving push data: ", e);

        }

        // If the push data includes an action string, that broadcast intent is fired.
        String action = null;
        if (pushData != null) {
            action = pushData.optString("action", null);
        }
        if (action != null) {
            Bundle extras = intent.getExtras();
            Intent broadcastIntent = new Intent();
            assert extras != null;
            broadcastIntent.putExtras(extras);
            broadcastIntent.setAction(action);
            broadcastIntent.setPackage(context.getPackageName());
            context.sendBroadcast(broadcastIntent);
        }

        final NotificationCompat.Builder notificationBuilder = notificationUtils.getNotification(context, intent);

         Intent myIntent;

        if (pushData != null && pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE).equals(SendNotifications.PUSH_TYPE_VISITOR)) {

            myIntent = new Intent(context, HomeActivity.class);
            myIntent.putExtra(HomeActivity.EXTRA_USER_TO_ID, pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER));
            myIntent.putExtra(HomeActivity.EXTRA_TYPE, SendNotifications.PUSH_TYPE_VISITOR);

        } else if (pushData != null && pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE).equals(SendNotifications.PUSH_TYPE_FAVORITED_YOU)) {

            myIntent = new Intent(context, HomeActivity.class);
            myIntent.putExtra(HomeActivity.EXTRA_USER_TO_ID, pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER));
            myIntent.putExtra(HomeActivity.EXTRA_TYPE, SendNotifications.PUSH_TYPE_FAVORITED_YOU);

        } else if (pushData != null && pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE).equals(SendNotifications.PUSH_TYPE_LIKED_YOU)) {


            if (Config.isPremiumEnabled){

                if (User.getUser().isPremium()){

                    myIntent = new Intent(context, LikedYouActivity.class);

                } else {

                    myIntent = new Intent(context, PaymentsActivity.class);
                    myIntent.putExtra(PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM);
                    myIntent.putExtra(PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM_LIKED);
                }

            } else {

                myIntent = new Intent(context, LikedYouActivity.class);
            }


        } else if (pushData != null && pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE).equals(SendNotifications.PUSH_TYPE_MATCHES)) {

            myIntent = new Intent(context, HomeActivity.class);
            myIntent.putExtra(HomeActivity.EXTRA_USER_TO_ID, pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER));
            myIntent.putExtra(HomeActivity.EXTRA_TYPE, SendNotifications.PUSH_TYPE_MATCHES);


        } else if (pushData != null && pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE).equals(SendNotifications.PUSH_TYPE_MESSAGES)) {


            myIntent = new Intent(context, HomeActivity.class);
            myIntent.putExtra(HomeActivity.EXTRA_USER_TO_ID, pushData.optString(SendNotifications.PUSH_NOTIFICATION_SENDER));
            myIntent.putExtra(HomeActivity.EXTRA_TYPE, SendNotifications.PUSH_TYPE_MESSAGES);



        } else if (pushData != null && pushData.optString(SendNotifications.PUSH_NOTIFICATION_TYPE).equals(SendNotifications.PUSH_TYPE_DATOO_LIVE)) {

            myIntent = new Intent(context, HomeActivity.class);

        } else {

            myIntent = new Intent(context, HomeActivity.class);
        }

        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent myPendingIntent = PendingIntent.getActivity(context,0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert notificationBuilder != null;
        notificationBuilder.setContentIntent(myPendingIntent);

        Notification notification;
        notification = notificationBuilder.build();

        if (notification != null) {
            ParseNotificationManager.getInstance().showNotification(context, notification);
        }
    }
}