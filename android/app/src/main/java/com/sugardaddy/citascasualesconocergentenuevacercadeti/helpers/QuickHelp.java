package com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.CountriesAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections.ChatActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections.FeatureActivationActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections.IncomingActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.encounters.LikedYouActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.encounters.MutualAttractionActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.live.LiveStreamingActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile.EditProfileActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile.PhotosViewerActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile.ReportBlockActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads.UploadsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveStreamModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.WithdrawModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.CountriesModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.LanguageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.RoundedImage;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.inAppNotification.AppNotification;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.shimmer.ShimmerFrameLayout;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.SharedPrefUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ToggleableRadioButton;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ViewAnimation;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.vanniktech.emoji.EmojiEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class QuickHelp {

    public static final String DATE_MONTH = "MMM";
    public static final String DATE_DAY_OF_MONTH = "dd";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    private final static int LOADING_DURATION = 999999999;

    private static boolean doubleBackToExitPressedOnce;
    private static Dialog dialog;
    private static LinearLayout hotelarProgress;
    private static int lastPosition = -1;

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isAndroid6() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static String getBirthdayFromDate(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        return sdf.format(date);
    }

    public static String getDateFormat(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        return sdf.format(date);
    }

    /**
     * method to ask user before to quit the app.
     */
    public static void onBackPressed(Activity activity){

        if(doubleBackToExitPressedOnce)
        {
            activity.finish();
        }
        doubleBackToExitPressedOnce = true;

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        @SuppressLint("InflateParams") View custom_view = activity.getLayoutInflater().inflate(R.layout.exit_layout, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(R.string.login_press_again);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_exit_to_app_white_24dp);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(activity.getResources().getColor(R.color.red_600));

        toast.setView(custom_view);
        toast.show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }


    /**
     * method to show the progress dialog
     */
    public static void showLoading(Activity activity, boolean cancelable) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.progress_layout);
        dialog.setCancelable(cancelable);

        hotelarProgress = dialog.findViewById(R.id.progress);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        if (!dialog.isShowing() && !activity.isFinishing()){

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            showAnimationDots();
        }
    }

    private static void showAnimationDots() {
        hotelarProgress.setVisibility(View.VISIBLE);
        hotelarProgress.setAlpha(1.0f);

        new Handler().postDelayed(() -> ViewAnimation.fadeOut(hotelarProgress), LOADING_DURATION);

        new Handler().postDelayed(() -> {
        }, LOADING_DURATION + 400);
    }

    /**
     * method to hide the progress dialog
     */
    public static void hideLoading(Activity activity) {
        if (activity == null) return;
        if (dialog != null && dialog.isShowing() && !activity.isFinishing()){

            dialog.cancel();
            hotelarProgress.setVisibility(View.GONE);
        }
    }

    /**
     * method to hide the progress dialog
     */
    public static void hideLoading() {

        if (dialog != null && dialog.isShowing()){

            dialog.cancel();
            hotelarProgress.setVisibility(View.GONE);
        }
    }

    /**
     * method to check if internet connection exist
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (connectivityManager != null) {
            netInfo = connectivityManager.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * method to show no internet connection toast
     */
    public static void noInternetConnect(Activity activity) {

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        @SuppressLint("InflateParams") View custom_view = activity.getLayoutInflater().inflate(R.layout.exit_layout, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(R.string.not_internet_connection);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_cloud_off_white_24dp);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(activity.getResources().getColor(R.color.red_600));

        toast.setView(custom_view);
        toast.show();
    }

    /**
     * method to show custom toast
     */
    public static void showToast(Activity activity, String message, boolean isError) {

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        @SuppressLint("InflateParams") View custom_view = activity.getLayoutInflater().inflate(R.layout.exit_layout, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        if (isError){
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_highlight_off_white_24dp);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(activity.getResources().getColor(R.color.red_600));
        } else {
            ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_error_outline_white_24dp);
            ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(activity.getResources().getColor(R.color.green_600));
        }

        toast.setView(custom_view);
        toast.show();
    }

    /**
     * method to show custom toast
     */

    public static void showInAppNotification(Activity activity, String type, String name, String avatarUrl, String message, String objectId) {

        if (activity == null) {
            return;
        }

        Toast toast = new Toast(Application.getInstance().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        @SuppressLint("InflateParams") View custom_view = activity.getLayoutInflater().inflate(R.layout.control_inapp_notification_rethink, null);
        FrameLayout layout = custom_view.findViewById(R.id.inapp_notification_body);
        RoundedImage avatar = custom_view.findViewById(R.id.inapp_notification_photo1);
        TextView badgeMessage = custom_view.findViewById(R.id.inapp_notification_badgeValue);
        ImageView badge = custom_view.findViewById(R.id.inapp_notification_badgeDrawable);
        TextView fromName = custom_view.findViewById(R.id.inapp_notification_title);
        TextView messageText = custom_view.findViewById(R.id.inapp_notification_message);
        ImageView nextBtn = custom_view.findViewById(R.id.inapp_notification_arrow);

        switch (type) {
            case SendNotifications.PUSH_TYPE_MESSAGES:

                messageText.setText(Application.getInstance().getBaseContext().getString(R.string.in_app_notification_message));
                badgeMessage.setVisibility(View.VISIBLE);
                badgeMessage.setText("1");

                QuickHelp.getAvatarInApp(avatar, avatarUrl);
                fromName.setText(name);

                break;
            case SendNotifications.PUSH_TYPE_VISITOR:

                QuickHelp.getAvatarInApp(avatar, avatarUrl);
                fromName.setText(name);

                messageText.setText(Application.getInstance().getBaseContext().getString(R.string.in_app_notification_visitor));
                badge.setVisibility(View.VISIBLE);
                badge.setImageResource(R.drawable.ic_badge_feature_spotlight);

                break;
            case SendNotifications.PUSH_TYPE_FAVORITED_YOU:

                QuickHelp.getAvatarInApp(avatar, avatarUrl);
                fromName.setText(name);

                messageText.setText(Application.getInstance().getBaseContext().getString(R.string.in_app_notification_favorited));
                badge.setVisibility(View.VISIBLE);
                badge.setImageResource(R.drawable.ic_badge_feature_favourites);

                break;
            case SendNotifications.PUSH_TYPE_LIKED_YOU:

                QuickHelp.getAvatarInApp(avatar, avatarUrl);
                fromName.setText(name);

                messageText.setText(Application.getInstance().getBaseContext().getString(R.string.in_app_notification_liked_you));
                badge.setVisibility(View.VISIBLE);
                badge.setImageResource(R.drawable.ic_badge_feature_liked_you);

                break;
            case SendNotifications.PUSH_TYPE_MATCHES:

                QuickHelp.getAvatarInApp(avatar, avatarUrl);
                fromName.setText(name);

                messageText.setText(Application.getInstance().getBaseContext().getString(R.string.in_app_notification_matches));
                badge.setVisibility(View.VISIBLE);
                badge.setImageResource(R.drawable.ic_badge_feature_match);

                break;
            case SendNotifications.PUSH_TYPE_DATOO_LIVE:

                QuickHelp.getAvatarInApp(avatar, avatarUrl);
                fromName.setText(name);

                messageText.setText(Application.getInstance().getBaseContext().getString(R.string.in_app_notification_live));
                badge.setVisibility(View.VISIBLE);

                break;

            default:

                nextBtn.setVisibility(View.GONE);

                fromName.setText(Application.getInstance().getBaseContext().getString(R.string.app_name));
                messageText.setText(message);
                badge.setVisibility(View.VISIBLE);
                badge.setImageResource(R.drawable.ic_badge_feature_attention_boost);

                QuickHelp.getAvatarInApp(avatar, null);
                fromName.setText(name);

                break;

        }

        toast.setView(custom_view);
        toast.setGravity(Gravity.TOP|Gravity.START|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.show();

        layout.setOnClickListener(v -> {

            QuickHelp.showLoading(activity, true);

            ParseQuery<User> parseQuery = User.getUserQuery();
            parseQuery.getInBackground(objectId, (user, e) -> {

                if (e == null){

                    QuickHelp.hideLoading(activity);

                    switch (type) {
                        case SendNotifications.PUSH_TYPE_MESSAGES:

                            QuickHelp.goToActivityChat(activity, user);

                            break;

                        case SendNotifications.PUSH_TYPE_VISITOR:
                        case SendNotifications.PUSH_TYPE_MATCHES:
                        case SendNotifications.PUSH_TYPE_FAVORITED_YOU:

                            QuickActions.showProfile(activity, user, false);

                            break;

                        case SendNotifications.PUSH_TYPE_LIKED_YOU:

                            if (Config.isPremiumEnabled){

                                if (user.isPremium()){

                                    QuickHelp.goToActivityWithNoClean(activity, LikedYouActivity.class);

                                } else {

                                    QuickHelp.goToActivityWithNoClean(activity, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM, PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM_LIKED);
                                }

                            } else {

                                QuickHelp.goToActivityWithNoClean(activity, LikedYouActivity.class);
                            }

                            break;

                        case SendNotifications.PUSH_TYPE_DATOO_LIVE:

                            QuickActions.showProfile(Application.getInstance().getCurrentActivity(), user, false);

                            break;
                    }
                } else QuickHelp.hideLoading(activity);
            });
        });
    }

    /**
     * method to show custom toast
     */

    public static void showNotification(Activity activity, String message, boolean isError) {

        AppNotification.buildToast(activity, message, AppNotification.LENGTH_LONG, AppNotification.GRAVITY_TOP, isError);

    }

    /**
     * method to save current device installation to Current User Object
     */
    public static void saveInstallation(User user){

        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();

        parseInstallation.put("user",user);
        parseInstallation.put("GCMSenderId", Application.getInstance().getApplicationContext().getString(R.string.gcm_sender_id));
        parseInstallation.saveInBackground();

        user.setInstallation(parseInstallation);
        user.saveInBackground();

        ParsePush.subscribeInBackground(Config.CHANNEL);
    }

    /**
     * method to remove current device installation to Current User Object
     */
    public static void removeUserToInstallation(){

        ParseInstallation.getCurrentInstallation().remove("user");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.unsubscribeInBackground(Config.CHANNEL);
    }


    /**
     * method to go to any activity
     */
    public static void goToActivityInComing(Application application, Class<?> otherActivity, User user, String channel, String callType) {
        // process to account creation

        Intent mainIntent = new Intent(application, otherActivity);
        mainIntent.putExtra(IncomingActivity.EXTRA_USER_TO_ID, user);
        mainIntent.putExtra(IncomingActivity.EXTRA_CALL_CHANNEL, channel);
        mainIntent.putExtra(IncomingActivity.EXTRA_CALL_TYPE, callType);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(mainIntent);
    }

    /**
     * method to go to any activity
     */
    public static void goToActivityStreaming(Activity activity, int type) {
        // process to account creation

        Intent mainIntent = new Intent(activity, LiveStreamingActivity.class);
        mainIntent.putExtra(LiveStreamingActivity.LIVE_STREAMING_TYPE, type);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    /**
     * method to go to any activity
     */
    public static void goToActivityStreaming(Activity activity, int type, LiveStreamModel liveStreamModel, String channel) {
        // process to account creation

        Intent mainIntent = new Intent(activity, LiveStreamingActivity.class);
        mainIntent.putExtra(LiveStreamingActivity.LIVE_STREAMING_TYPE, type);
        mainIntent.putExtra(LiveStreamingActivity.LIVE_STREAMER_OBJECT, liveStreamModel);
        mainIntent.putExtra(LiveStreamingActivity.LIVE_STREAMING_CHANNEL, channel);
        activity.startActivity(mainIntent);

    }

    /**
     * method to go to any activity
     */
    public static void goToActivityLogout(Activity currentActivity, Class<?> otherActivity) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(mainIntent);
        currentActivity.finish();
    }

    /**
     * method to go to any activity
     */
    public static void goToActivityAndFinish(Activity currentActivity, Class<?> otherActivity) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(mainIntent);
        currentActivity.finish();
    }

    /**
     * method to go to any activity
     */
    public static void goToActivityWithNoClean(Activity currentActivity, Class<?> otherActivity) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        currentActivity.startActivity(mainIntent);
    }

    static void goToActivityBlockOrReport(Activity currentActivity, User user) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, ReportBlockActivity.class);
        mainIntent.putExtra(ReportBlockActivity.USER_OBJECT, user);
        currentActivity.startActivity(mainIntent);
    }

    public static void goToActivityFeatureActivation(Activity currentActivity, String type) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, FeatureActivationActivity.class);
        mainIntent.putExtra(FeatureActivationActivity.FEATURE_TYPE, type);
        currentActivity.startActivity(mainIntent);
    }

    public static void goToActivityWithNoClean(Activity currentActivity, Class<?> otherActivity, String extra, String extraValue) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        mainIntent.putExtra(extra, extraValue);
        currentActivity.startActivity(mainIntent);
    }

    public static void goToActivityPhotoViewer(Activity currentActivity, User user, int position) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, PhotosViewerActivity.class);
        mainIntent.putExtra(PhotosViewerActivity.UserObject, user);
        mainIntent.putExtra(PhotosViewerActivity.Position, position);
        currentActivity.startActivity(mainIntent);
    }

    public static void goToActivityWithNoClean(Activity currentActivity, Class<?> otherActivity, String extra, String extraValue, String extra2, String extraValue2) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        mainIntent.putExtra(extra, extraValue);
        mainIntent.putExtra(extra2, extraValue2);
        currentActivity.startActivity(mainIntent);
    }

    public static void goToActivityCreditCrush(Activity currentActivity, Class<?> otherActivity,  User user) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, otherActivity);
        mainIntent.putExtra(PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_ENCOUNTERS_ADS);
        mainIntent.putExtra(PaymentsActivity.DATOO_CRUSH_USER, user);
        currentActivity.startActivity(mainIntent);
    }

    static void goToActivityMutualActivity(Activity currentActivity, User user) {
        // process to account creation

        Intent mainIntent = new Intent(currentActivity, MutualAttractionActivity.class);
        mainIntent.putExtra(MutualAttractionActivity.OtherUserObject, user);

        if (currentActivity != null) currentActivity.startActivity(mainIntent);
    }

    public static void goToActivityChat(Activity currentActivity, User user) {

        Intent mainIntent = new Intent(currentActivity, ChatActivity.class);
        mainIntent.putExtra(ChatActivity.EXTRA_USER_TO_ID, user);
        if (currentActivity != null) currentActivity.startActivity(mainIntent);
    }

    public static void animateAndHide(View view, Context context) {
        Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        view.startAnimation(slide_up);
        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void animateAndShow(View view, Context context) {
        Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        view.startAnimation(slide_up);
        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void setPulseAnimation(ImageView imageView)
    {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(imageView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));

        scaleDown.setInterpolator(new FastOutSlowInInterpolator());

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);


        scaleDown.start();
    }

    public static void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(Application.getInstance(), android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public static void setAds(Context context, RelativeLayout adView, User user){
        if (user == null) return;
        if (Config.isAdsActivated && !user.isPremium()){

            adView.setVisibility(View.VISIBLE);

            MobileAds.initialize(context);

            AdView mAdView = new AdView(context);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(Constants.getHomeBannerAdsId());
            adView.addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            } else {

            adView.setVisibility(View.GONE);
        }

    }

    public static int getPopularityLevelIndicator(User user){


        if (user == null) return 0;

        if (user.getColPopularity() == 1){

            return R.drawable.ic_popularity_low;
        } else if (user.getColPopularity() == 2){

            return R.drawable.ic_popularity_medium;
        } else if (user.getColPopularity() == 3){

            return R.drawable.ic_popularity_high;
        } else if (user.getColPopularity() == 4){

            return R.drawable.ic_popularity_highest_two;
        } else{

            return R.drawable.ic_popularity_lowest;
        }
    }

    public static int getPopularityLevelIndicatorAlpha(User user){

        if (user == null) return 0;

        if (user.getColPopularity() == 1){

            return R.drawable.ic_popularity_alpha_low;
        } else if (user.getColPopularity() == 2){

            return R.drawable.ic_popularity_alpha_medium;
        } else if (user.getColPopularity() == 3){

            return R.drawable.ic_popularity_alpha_high;
        } else if (user.getColPopularity() == 4){

            return R.drawable.ic_popularity_highest_two;
        } else{

            return R.drawable.ic_popularity_alpha_lowest;
        }
    }

    public static String getPopularityLevel(User user){

        if (user == null) return Application.getInstance().getBaseContext().getString(R.string.popularity_lowest);

        if (user.getColPopularity() == 1){

            return Application.getInstance().getBaseContext().getString(R.string.popularity_low);

        } else if (user.getColPopularity() == 2){

            return Application.getInstance().getBaseContext().getString(R.string.popularity_medium);
        } else if (user.getColPopularity() == 3){

            return Application.getInstance().getBaseContext().getString(R.string.popularity_hight);
        } else if (user.getColPopularity() == 4){

            return Application.getInstance().getBaseContext().getString(R.string.popularity_highest);
        } else{

            return Application.getInstance().getBaseContext().getString(R.string.popularity_lowest);
        }
    }

    public static Date getMinutesToOnline(){

        Calendar today = Calendar.getInstance();
        today.setTime(new Date(System.currentTimeMillis()));
        today.add(Calendar.MINUTE, -2);

        return today.getTime();
    }

    public static int getAgeFromDate(Date birthday){

        Calendar dob = Calendar.getInstance();
        dob.setTime(birthday);
        Calendar today = Calendar.getInstance();

        dob.set(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), dob.get(Calendar.DAY_OF_MONTH));

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }

    public static Date getDateFromString(String birthday, String dateFormat){

        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = format.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    public static String getGenderName(User user){

        if (!user.getColGender().isEmpty()){

            if (user.getColGender().equals(User.GENDER_MALE)){
                return Application.getInstance().getBaseContext().getString(R.string.male);
            } else if (user.getColGender().equals(User.GENDER_FEMALE)){
                return Application.getInstance().getBaseContext().getString(R.string.female);
            }
        }

        return "";
    }

    public static void  updateUserLocation(User user){

        if (user != null){

            Geocoder gcd = new Geocoder(Application.getInstance().getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            try {

                addresses = gcd.getFromLocation(user.getGeoPoint().getLatitude(), user.getGeoPoint().getLongitude(), 1);

                if (addresses.size() > 0) {

                    String AdminArea = addresses.get(0).getAdminArea();
                    String City = addresses.get(0).getLocality();
                    String Country = addresses.get(0).getCountryName();

                    if (City != null && !City.equals("null")){

                        String Location = City + ", " + Country;
                        user.setLocation(Location);
                        user.setCity(City);


                    } else if (AdminArea != null && !AdminArea.equals("null")){

                        String Location = AdminArea + ", " + Country;
                        user.setLocation(Location);
                        user.setCity(AdminArea);

                    } else {

                        user.setLocation(Country);
                        user.setCity(Country);
                    }

                    user.saveInBackground();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static String getCityFromLocation(ParseGeoPoint location){

        if (location == null){

            return "";

        } else {

            Geocoder gcd = new Geocoder(Application.getInstance().getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            try {

                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (addresses != null){

                    if (addresses.size() > 0) {

                        String City = addresses.get(0).getLocality();
                        String Country = addresses.get(0).getCountryName();

                        return City + ", " + Country;

                    } else {

                        return "";
                    }

                } else {

                    return "";
                }


            } catch (IOException e) {
                e.printStackTrace();

                return "";
            }

        }
    }

    public static String getOnlyCityFromLocation(User user){

        if (!user.getCity().isEmpty()){

            return user.getCity().replaceAll("null", "");

        } else if (!user.getLocation().isEmpty()){

            return user.getLocation().replaceAll("null,", "");

        } else if (user.getGeoPoint() != null ){

            Geocoder gcd = new Geocoder(Application.getInstance().getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            try {

                if (user.getGeoPoint() == null){

                    return "";

                } else {

                    addresses = gcd.getFromLocation(user.getGeoPoint().getLatitude(), user.getGeoPoint().getLongitude(), 1);

                    if (addresses.size() > 0) {

                        return addresses.get(0).getLocality();

                    } else {

                        return "";
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();

                return "";
            }

        } else {

            return "";
        }
    }

    public static void getAvatars(User user, CircleImageView circleImageView){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

            // Load profile Photos
            if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

                Glide.with(Application.getInstance().getApplicationContext())
                        .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                        .error(colorDrawable)
                        .centerCrop()
                        .circleCrop()
                        .fitCenter()
                        .placeholder(colorDrawable)
                        .into(circleImageView);

            } else {

                Glide.with(Application.getInstance().getApplicationContext())
                        .load(colorDrawable)
                        .into(circleImageView);
            }
    }

    public static void getAvatars(WithdrawModel withdrawModel, CircleImageView circleImageView){

        Glide.with(Application.getInstance().getApplicationContext())
                .load(withdrawModel.getType().equals(WithdrawModel.TYPE_WITHDRAW) ? R.drawable.ic_paypal : R.drawable.ic_credits_small_heart)
                .into(circleImageView);
    }

    public static void getAvatars(User user, CircleImageView circleImageView, boolean isBlocked){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        Glide.with(Application.getInstance().getApplicationContext())
                .load(colorDrawable)
                .into(circleImageView);
    }

    public static void getAvatars(User user, ImageView imageView){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                    .error(colorDrawable)
                    .centerCrop()
                    .circleCrop()
                    .fitCenter()
                    .placeholder(colorDrawable)
                    .into(imageView);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(imageView);
        }
    }

    public static void getMessageImage(String imagePath, RoundedImage roundedImage){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        Glide.with(Application.getInstance().getApplicationContext())
                .load(imagePath)
                .error(colorDrawable)
                .centerCrop()
                .placeholder(colorDrawable)
                .into(roundedImage);
    }

    public static void getAvatars(User user, RoundedImage roundedImage, int cornerRadius){

        roundedImage.setRoundedRadius(cornerRadius);

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                    .error(colorDrawable)
                    .centerCrop()
                    .circleCrop()
                    .fitCenter()
                    .placeholder(colorDrawable)
                    .into(roundedImage);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(roundedImage);
        }
    }

    private static void getAvatarInApp(RoundedImage roundedImage, String url){

        roundedImage.setRoundedRadius(35);

        if (url == null){

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(R.mipmap.ic_launcher)
                    .into(roundedImage);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(url)
                    .into(roundedImage);
        }
    }

    public static void getAvatars(User user, RoundedImage roundedImage){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                    .error(colorDrawable)
                    .centerCrop()
                    .circleCrop()
                    .fitCenter()
                    .placeholder(colorDrawable)
                    .into(roundedImage);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(roundedImage);
        }
    }

    public static void getAvatarsSpotlight(User user, RoundedImage roundedImage){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                    .error(colorDrawable)
                    .centerCrop()
                    .placeholder(colorDrawable)
                    .into(roundedImage);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(roundedImage);
        }
    }

    public static void getAvatars(String photoUrl, RoundedImage roundedImage){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (!photoUrl.isEmpty()) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(photoUrl)
                    .error(colorDrawable)
                    .centerCrop()
                    .circleCrop()
                    .fitCenter()
                    .placeholder(colorDrawable)
                    .into(roundedImage);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(roundedImage);
        }
    }

    public static void getProfilePhotos(String photoUrl, RoundedImage roundedImage){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (!photoUrl.isEmpty()) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(photoUrl)
                    .error(colorDrawable)
                    .centerCrop()
                    .placeholder(colorDrawable)
                    .into(roundedImage);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(roundedImage);
        }
    }

    public static void getAvatars(String photoUrl, RoundedImage roundedImage, int cornerRadius){

        roundedImage.setRoundedRadius(cornerRadius);
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (!photoUrl.isEmpty()) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(photoUrl)
                    .error(colorDrawable)
                    .centerCrop()
                    .circleCrop()
                    .fitCenter()
                    .placeholder(colorDrawable)
                    .into(roundedImage);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(roundedImage);
        }
    }

    public static void getEncountersAvatars(User user, ImageView imageView){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                    .error(colorDrawable)
                    .centerCrop()
                    .placeholder(colorDrawable)
                    .into(imageView);

        }  else imageView.setImageDrawable(colorDrawable);
    }

    public static void getAvatar(ImageView avatar, User user, int position){

        Glide.with(Application.getInstance().getApplicationContext())
                .load(user.getProfilePhotos().get(position).getUrl())
                //.placeholder(colorDrawable)
                .circleCrop()
                //.error(colorDrawable)
                .into(avatar);


    }

    public static void getAvatar(ImageView avatar, User user){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        // Load profile Photos
        if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                    //.placeholder(colorDrawable)
                    .circleCrop()
                    //.error(colorDrawable)
                    .into(avatar);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .circleCrop()
                    .into(avatar);
        }
    }

    public static void setAvatarFull(ImageView avatar, User user){

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.live_bg_color));

        // Load profile Photos
        if (user.getProfilePhotos() != null && user.getProfilePhotos().size() > 0) {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(user.getProfilePhotos().get(user.getAvatarPhotoPosition()).getUrl())
                    .into(avatar);

        } else {

            Glide.with(Application.getInstance().getApplicationContext())
                    .load(colorDrawable)
                    .into(avatar);
        }
    }

    public static boolean isProfilePhoto(User user, int position){

        return user.getAvatarPhotoPosition() == position;

    }

    public static Bitmap getBitmapFromPath(String path) {
        try {
            Bitmap bitmap;
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);

            return  bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromURLOrPath(String UrlsOrPath) {

        Bitmap bitmap = null;

        try {

            if (UrlsOrPath.contains("http")){


                URL url = new URL(UrlsOrPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                bitmap = myBitmap;

            } else {

                File file = new File(UrlsOrPath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = ARGB_8888;

                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //return myBitmap;
        } catch (IOException e) {
            // Log exception
            //return null;
        }

        return bitmap;
    }

    public static Bitmap getBitmapFromUri(Activity activity, Uri uri) {

        InputStream is = null;
        try {
            is = activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        //is.close();

        return bitmap;
    }

    public static ArrayList<Bitmap> getBitmapFromURLsOrPath (ArrayList<String> UrlsOrPath) {

        //has to be an ArrayList
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        //convert from paths to Android friendly Parcelable Uri's
        for (String urlOrPath : UrlsOrPath)
        {


            try {

                if (urlOrPath.contains("http")){


                    URL url = new URL(urlOrPath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    bitmapArrayList.add(myBitmap);

                } else {

                    File file = new File(urlOrPath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = ARGB_8888;

                    try {
                        bitmapArrayList.add(BitmapFactory.decodeStream(new FileInputStream(file), null, options));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                //return myBitmap;
            } catch (IOException e) {
                // Log exception
                //return null;
            }
        }

        return bitmapArrayList;
    }

    public static void setPopularityDays(ToggleableRadioButton popularityDays1, ToggleableRadioButton popularityDays2, ToggleableRadioButton popularityDays3, ToggleableRadioButton popularityDays4, ToggleableRadioButton popularityDays5, ToggleableRadioButton popularityDays6, ToggleableRadioButton popularityToday){

        Calendar dob = Calendar.getInstance();
        dob.setTime(new Date());
        Calendar today = Calendar.getInstance();

        dob.set(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), dob.get(Calendar.DAY_OF_MONTH));

        int day1 = today.get(Calendar.DAY_OF_MONTH) - 1;
        int day2 = today.get(Calendar.DAY_OF_MONTH) - 2;
        int day3 = today.get(Calendar.DAY_OF_MONTH) - 3;
        int day4 = today.get(Calendar.DAY_OF_MONTH) - 4;
        int day5 = today.get(Calendar.DAY_OF_MONTH) - 5;
        int day6 = today.get(Calendar.DAY_OF_MONTH) - 6;

        popularityDays1.setText(String.valueOf(day6));
        popularityDays2.setText(String.valueOf(day5));
        popularityDays3.setText(String.valueOf(day4));
        popularityDays4.setText(String.valueOf(day3));
        popularityDays5.setText(String.valueOf(day2));
        popularityDays6.setText(String.valueOf(day1));

        int Today = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
        if (Today == 0){

            popularityToday.setText(Application.getInstance().getBaseContext().getString(R.string.popularity_today));
        }

    }

    public static int getTodayDay () {

        Calendar today = Calendar.getInstance();

        return today.get(Calendar.DAY_OF_MONTH);

    }

    public static Date getDate(int day) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -day);  // number of days to add

        return c.getTime();
    }

    public static String getDateFromMilliSeconds(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getRandomString(final int sizeOfRandomString) {

        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static boolean validatePurchaseDate(Date untilDate) {

        Calendar todayCalendar = Calendar.getInstance();
        Date today = todayCalendar.getTime();


        Calendar limitDate = Calendar.getInstance();
        limitDate.add(Calendar.DAY_OF_MONTH,2); //
        //limitDate.setTime(untilDate);
        Date limit = limitDate.getTime();

        // today is a present date and purchase is tomorrow date

        //  0 comes when two date are same,
        //  1 comes when today is higher than limit
        // -1 comes when today is lower than limit

        if (limit.compareTo(today) < 0) {
            return true;
        } else if (limit.compareTo(today) == 0) {
            return true;
        } if (limit.compareTo(today) > 0) {
            return false;
        } else  {
            return false;
        }
    }

    public static Date incrementDate(int days) {

        Calendar limitDate = Calendar.getInstance();
        limitDate.add(Calendar.DAY_OF_MONTH,days); //

        return limitDate.getTime();
    }


    // Profile Edit

    static String getWhatIWantProfile(User user) {

        switch (user.getWhatIWant()) {
            case User.WHAT_I_WANT_JUST_TO_CHAT:

                return Application.getInstance().getBaseContext().getString(R.string.what_profile_to_chat);

            case User.WHAT_I_WANT_LET_SEE_WHAT_HAPPENS:

                return Application.getInstance().getBaseContext().getString(R.string.what_profile_lets_see);

            case User.WHAT_I_WANT_SOMETHING_CASUAL:

                return Application.getInstance().getBaseContext().getString(R.string.what_profile__casual);

            case User.WHAT_I_WANT_SOMETHING_SERIOUS:

                return Application.getInstance().getBaseContext().getString(R.string.what_profile__serious);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.what_profile_lets_see);
        }
    }

    public static String getWhatIWant(User user) {

        switch (user.getWhatIWant()) {
            case User.WHAT_I_WANT_JUST_TO_CHAT:

                return Application.getInstance().getBaseContext().getString(R.string.what_just_chat);

            case User.WHAT_I_WANT_LET_SEE_WHAT_HAPPENS:

                return Application.getInstance().getBaseContext().getString(R.string.what_lets_see);

            case User.WHAT_I_WANT_SOMETHING_CASUAL:

                return Application.getInstance().getBaseContext().getString(R.string.what_casual);

            case User.WHAT_I_WANT_SOMETHING_SERIOUS:

                return Application.getInstance().getBaseContext().getString(R.string.what_serious);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.what_lets_see);
        }
    }

    public static String getRelationShip(User user) {

        switch (user.getRelationship()) {
            case User.RELATIONSHIP_COMPLICATED:

                return Application.getInstance().getBaseContext().getString(R.string.rela_compli);

            case User.RELATIONSHIP_SINGLE:

                return Application.getInstance().getBaseContext().getString(R.string.rela_single);

            case User.RELATIONSHIP_TAKEN:

                return Application.getInstance().getBaseContext().getString(R.string.rela_taken);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getSexuality(User user) {

        switch (user.getSexuality()) {
            case User.SEXUALITY_BISEXUAL:

                return Application.getInstance().getBaseContext().getString(R.string.sex_bi);

            case User.SEXUALITY_LESBIAN:

                return Application.getInstance().getBaseContext().getString(R.string.sex_lesbian);

            case User.SEXUALITY_ASK_ME:

                return Application.getInstance().getBaseContext().getString(R.string.sex_ask);

            case User.SEXUALITY_STRAIGHT:

                return Application.getInstance().getBaseContext().getString(R.string.sex_stra);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getHeight(User user) {

        if (user.getHeight() > 0){

            return String.format("%s cm", user.getHeight());

        } else {
            return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getBodyType(User user) {

        switch (user.getBodyType()) {
            case User.BODY_TYPE:

                return Application.getInstance().getBaseContext().getString(R.string.sex_bi);

            case User.BODY_TYPE_ATHLETIC:

                return Application.getInstance().getBaseContext().getString(R.string.body_athl);

            case User.BODY_TYPE_AVERAGE:

                return Application.getInstance().getBaseContext().getString(R.string.body_average);

            case User.BODY_TYPE_BIG_AND_BEAUTIFUL:

                return Application.getInstance().getBaseContext().getString(R.string.body_big);

            case User.BODY_TYPE_FEW_EXTRA_POUNDS:

                return Application.getInstance().getBaseContext().getString(R.string.body_extra);

            case User.BODY_TYPE_MUSCULAR:

                return Application.getInstance().getBaseContext().getString(R.string.body_musc);

            case User.BODY_TYPE_SLIM:

                return Application.getInstance().getBaseContext().getString(R.string.body_slim);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getLiving(User user) {

        switch (user.getLiving()) {
            case User.LIVING_BY_MYSELF:

                return Application.getInstance().getBaseContext().getString(R.string.living_myself);

            case User.LIVING_STUDENT_DORMITORY:

                return Application.getInstance().getBaseContext().getString(R.string.living_student);

            case User.LIVING_WITH_PARENTS:

                return Application.getInstance().getBaseContext().getString(R.string.living_parents);

            case User.LIVING_WITH_PARTNER:

                return Application.getInstance().getBaseContext().getString(R.string.living_partner);

            case User.LIVING_WITH_ROOMMATES:

                return Application.getInstance().getBaseContext().getString(R.string.living_room);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getKids(User user) {

        switch (user.getKids()) {
            case User.KIDS_ALREADY_HAVE:

                return Application.getInstance().getBaseContext().getString(R.string.kids_already);

            case User.KIDS_GROWN_UP:

                return Application.getInstance().getBaseContext().getString(R.string.kids_grown);

            case User.KIDS_NO_NOVER:

                return Application.getInstance().getBaseContext().getString(R.string.kids_no_never);

            case User.KIDS_SOMEDAY:

                return Application.getInstance().getBaseContext().getString(R.string.kids_someday);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

     static String getKidsProfile(User user) {

        switch (user.getKids()) {
            case User.KIDS_ALREADY_HAVE:

                return Application.getInstance().getBaseContext().getString(R.string.kids_already_profile);

            case User.KIDS_GROWN_UP:

                return Application.getInstance().getBaseContext().getString(R.string.kids_grown_profile);

            case User.KIDS_NO_NOVER:

                return Application.getInstance().getBaseContext().getString(R.string.kids_no_never_profile);

            case User.KIDS_SOMEDAY:

                return Application.getInstance().getBaseContext().getString(R.string.kids_someday);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getSmoking(User user) {

        switch (user.getSmoking()) {
            case User.SMOKING_I_DO_NOT_LIKE_IT:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_not_like);

            case User.SMOKING_I_HATE_SMOKING:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_hate);

            case User.SMOKING_I_SMOKE_OCCASIONALLY:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_occasionally);

            case User.SMOKING_IAM_A_HEAVY_SMOKER:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_heavy);

            case User.SMOKING_IAM_A_SOCIAL_SMOKER:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_social);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    static String getSmokingProfile(User user) {

        switch (user.getSmoking()) {
            case User.SMOKING_I_DO_NOT_LIKE_IT:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_not_like_profile);

            case User.SMOKING_I_HATE_SMOKING:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_hate_profile);

            case User.SMOKING_I_SMOKE_OCCASIONALLY:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_occasionally_profile);

            case User.SMOKING_IAM_A_HEAVY_SMOKER:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_heavy_profile);

            case User.SMOKING_IAM_A_SOCIAL_SMOKER:

                return Application.getInstance().getBaseContext().getString(R.string.smoke_social_profile);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getDrinking(User user) {

        switch (user.getDrinking()) {
            case User.DRINKING_I_DO_NOT_DRINK:

                return Application.getInstance().getBaseContext().getString(R.string.drink_do_not);

            case User.DRINKING_I_DRINK_A_LOT:

                return Application.getInstance().getBaseContext().getString(R.string.drink_drink_lot);

            case User.DRINKING_I_DRINK_SOCIALLY:

                return Application.getInstance().getBaseContext().getString(R.string.drink_socially);

            case User.DRINKING_IAM_AGAINST_DRINKING:

                return Application.getInstance().getBaseContext().getString(R.string.drink_against);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    static String getDrinkingProfile(User user) {

        switch (user.getDrinking()) {
            case User.DRINKING_I_DO_NOT_DRINK:

                return Application.getInstance().getBaseContext().getString(R.string.drink_do_not_profile);

            case User.DRINKING_I_DRINK_A_LOT:

                return Application.getInstance().getBaseContext().getString(R.string.drink_drink_lot_profile);

            case User.DRINKING_I_DRINK_SOCIALLY:

                return Application.getInstance().getBaseContext().getString(R.string.drink_socially_profile);

            case User.DRINKING_IAM_AGAINST_DRINKING:

                return Application.getInstance().getBaseContext().getString(R.string.drink_against_profile);

            default:
                return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static String getLanguage(User user) {

        if (!user.getLanguage().isEmpty()){

            return user.getLanguage();

        } else {

            SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(Application.getInstance().getApplicationContext());
            sharedPrefUtil.getLanguage(Application.getInstance().getApplicationContext());
            return Application.getInstance().getBaseContext().getString(R.string.profile_no_answer);
        }
    }

    public static ArrayList<LanguageModel> getLanguages() {

        LanguageModel ar = new LanguageModel();
        ar.setName(Application.getInstance().getBaseContext().getString(R.string.lang_ar));
        ar.setCode("ar");

        LanguageModel de = new LanguageModel();
        de.setName(Application.getInstance().getBaseContext().getString(R.string.lang_de));
        de.setCode("de");

        LanguageModel fr = new LanguageModel();
        fr.setName(Application.getInstance().getBaseContext().getString(R.string.lang_fr));
        fr.setCode("fr");

        LanguageModel hi = new LanguageModel();
        hi.setName(Application.getInstance().getBaseContext().getString(R.string.lang_hi));
        hi.setCode("hi");

        LanguageModel in = new LanguageModel();
        in.setName(Application.getInstance().getBaseContext().getString(R.string.lang_in));
        in.setCode("in");

        LanguageModel it = new LanguageModel();
        it.setName(Application.getInstance().getBaseContext().getString(R.string.lang_it));
        it.setCode("it");

        LanguageModel ja = new LanguageModel();
        ja.setName(Application.getInstance().getBaseContext().getString(R.string.lang_ja));
        ja.setCode("ja");

        LanguageModel ko = new LanguageModel();
        ko.setName(Application.getInstance().getBaseContext().getString(R.string.lang_ko));
        ko.setCode("ko");

        LanguageModel pt = new LanguageModel();
        pt.setName(Application.getInstance().getBaseContext().getString(R.string.lang_pt));
        pt.setCode("pt");

        LanguageModel tr = new LanguageModel();
        tr.setName(Application.getInstance().getBaseContext().getString(R.string.lang_tr));
        tr.setCode("tr");

        LanguageModel ur = new LanguageModel();
        ur.setName(Application.getInstance().getBaseContext().getString(R.string.lang_ur));
        ur.setCode("ur");

        LanguageModel zh = new LanguageModel();
        zh.setName(Application.getInstance().getBaseContext().getString(R.string.lang_zh));
        zh.setCode("zh");

        LanguageModel en = new LanguageModel();
        en.setName(Application.getInstance().getBaseContext().getString(R.string.lang_en));
        en.setCode("en");


        ArrayList<LanguageModel> languageModelArrayList = new ArrayList<>();

        languageModelArrayList.add(en);
        languageModelArrayList.add(ar);
        languageModelArrayList.add(de);
        languageModelArrayList.add(fr);
        languageModelArrayList.add(hi);
        languageModelArrayList.add(in);
        languageModelArrayList.add(it);
        languageModelArrayList.add(ja);
        languageModelArrayList.add(ko);
        languageModelArrayList.add(pt);
        languageModelArrayList.add(tr);
        languageModelArrayList.add(ur);
        languageModelArrayList.add(zh);


        return languageModelArrayList;
    }

    public static String getLanguageName(LanguageModel languageModel) {

        switch (languageModel.getCode()) {
            case "ar":

                return Application.getInstance().getBaseContext().getString(R.string.lang_ar);

            case "de":

                return  Application.getInstance().getBaseContext().getString(R.string.lang_de);

            case "fr":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_fr);

            case "hi":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_hi);

            case "in":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_in);

            case "it":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_it);

            case "ja":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_ja);

            case "ko":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_ko);

            case "pt":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_pt);

            case "tr":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_tr);

            case "ur":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_ur);

            case "zh":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_zh);

            default:
                return Application.getInstance().getCurrentActivity().getString(R.string.lang_en);
        }
    }

    public static String getLanguageName(String code) {

        switch (code) {
            case "ar":

                return Application.getInstance().getCurrentActivity().getString(R.string.lang_ar);

            case "de":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_de);

            case "fr":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_fr);

            case "hi":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_hi);

            case "in":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_in);

            case "it":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_it);

            case "ja":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_ja);

            case "ko":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_ko);

            case "pt":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_pt);

            case "tr":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_tr);

            case "ur":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_ur);

            case "zh":

                return  Application.getInstance().getCurrentActivity().getString(R.string.lang_zh);

            default:
                return Application.getInstance().getCurrentActivity().getString(R.string.lang_en);
        }
    }

    public static String getAboutMe(User user) {

        if (!user.getAboutMe().isEmpty()){

            return user.getAboutMe();

        } else return Application.getInstance().getCurrentActivity().getString(R.string.profile_your_about_empty);
    }

    public static int convertDpToPixel( int dp) {

        Resources resources = Application.getInstance().getResources();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static int getActionBarSize() {

        TypedArray typedArray = Application.getInstance().getTheme().obtainStyledAttributes(new int[] {android.R.attr.actionBarSize});

        int actionBarSize = (int) typedArray.getDimension(0, 0);
        typedArray.recycle();

        return actionBarSize;
    }

    private static int getMargin(int dp) {

        TypedArray typedArray = Application.getInstance().getTheme().obtainStyledAttributes(new int[] {dp});

        int marginSize = (int) typedArray.getDimension(0, 0);
        typedArray.recycle();

        return marginSize;
    }

    static void setMargin (Activity activity, boolean isOwnProfile, LinearLayout rootView, View chat, ShimmerFrameLayout shimmerFrameLayout, NestedScrollView scrollViewProfile, RelativeLayout profileBasicInfo, ImageView chatAndAddBtn, ImageView likeAndEditBtn){

        LinearLayout.MarginLayoutParams layoutParamsCompat = (LinearLayout.MarginLayoutParams) rootView.getLayoutParams();

        if (isOwnProfile){

            layoutParamsCompat.topMargin = QuickHelp.getActionBarSize();
            layoutParamsCompat.bottomMargin = QuickHelp.getActionBarSize();

            layoutParamsCompat.rightMargin = QuickHelp.getMargin(R.dimen.encounters_card_margin);
            layoutParamsCompat.leftMargin = QuickHelp.getMargin(R.dimen.encounters_card_margin);

            chatAndAddBtn.setImageResource(R.drawable.ic_floating_action_add_photos);
            likeAndEditBtn.setImageResource(R.drawable.ic_floating_action_edit_profile);

            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.hideShimmer();

            shimmerFrameLayout.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
            scrollViewProfile.setVisibility(View.VISIBLE);
            profileBasicInfo.setVisibility(View.VISIBLE);

            likeAndEditBtn.setOnClickListener(v -> QuickHelp.goToActivityWithNoClean(activity, EditProfileActivity.class));
            chatAndAddBtn.setOnClickListener(v -> QuickHelp.goToActivityWithNoClean(activity, UploadsActivity.class));

        } else {

            layoutParamsCompat.topMargin = QuickHelp.getActionBarSize();
            layoutParamsCompat.bottomMargin = QuickHelp.getActionBarSize() /2;

            layoutParamsCompat.rightMargin = QuickHelp.getMargin(R.dimen.encounters_card_margin);
            layoutParamsCompat.leftMargin = QuickHelp.getMargin(R.dimen.encounters_card_margin);

            chatAndAddBtn.setImageResource(R.drawable.ic_floating_action_chat);
            likeAndEditBtn.setImageResource(R.drawable.ic_floating_action_yes);

            scrollViewProfile.setVisibility(View.GONE);
            profileBasicInfo.setVisibility(View.GONE);
        }

        rootView.setLayoutParams(layoutParamsCompat);
    }

    static void showOtherProfile (boolean isLoading, boolean isLoaded, boolean showChat, LinearLayout rootView, View chat, ShimmerFrameLayout shimmerFrameLayout, NestedScrollView scrollViewProfile, RelativeLayout profileBasicInfo){

        LinearLayout.MarginLayoutParams layoutParamsCompat = (LinearLayout.MarginLayoutParams) rootView.getLayoutParams();

        layoutParamsCompat.topMargin = QuickHelp.getActionBarSize();
        layoutParamsCompat.rightMargin = QuickHelp.getMargin(R.dimen.encounters_card_margin);
        layoutParamsCompat.leftMargin = QuickHelp.getMargin(R.dimen.encounters_card_margin);

        if (isLoading){

            layoutParamsCompat.bottomMargin = QuickHelp.getActionBarSize();

            chat.setVisibility(View.GONE);
            scrollViewProfile.setVisibility(View.GONE);
            profileBasicInfo.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);

            shimmerFrameLayout.showShimmer(true);

        } else if (isLoaded){

            scrollViewProfile.setVisibility(View.VISIBLE);
            profileBasicInfo.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.hideShimmer();

            if (showChat){
                layoutParamsCompat.bottomMargin = QuickHelp.getActionBarSize() /2;
                chat.setVisibility(View.VISIBLE);
            } else {
                layoutParamsCompat.bottomMargin = QuickHelp.getActionBarSize();
                chat.setVisibility(View.GONE);
            }

        }

    }

    public static void setDrawableTint(TextView textView, int color){

        for (Drawable drawable : textView.getCompoundDrawables()){

            if (drawable != null){
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    static void setDrawableTint(AppCompatButton compatButton, int color){

        for (Drawable drawable : compatButton.getCompoundDrawables()){

            if (drawable != null){
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(compatButton.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public static int generateUId(){

        Random rnd = new Random();
        return  1000000000 + rnd.nextInt(999999999);
    }

    public static void setupTextWatcher(EditText editText, ImageButton sendButton) {

        TextWatcher textWatcher  = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.length() < 1){

                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 1){

                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() < 1){

                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
    }

    public static void setupTextWatcher(EmojiEditText editText, ImageButton sendButton) {

        TextWatcher textWatcher  = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.length() < 1){

                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 1){

                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() < 1){

                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);

    }

    public static void setScreenOn(Activity activity, boolean isEnabled) {

        if (isEnabled){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    public static int getAge (String birthday){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar calendar = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }

    public static void nextTextWatcher (EditText currentText, EditText nextText, int maxNumber){

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0){
                    int number = Integer.parseInt(s.toString());
                    if(number > maxNumber){

                        currentText.setText("");
                        currentText.requestFocus();

                    } else {

                        if (s.length() == 1){
                            nextText.requestFocus();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        currentText.addTextChangedListener(textWatcher);
        currentText.setSelectAllOnFocus(true);

    }

    public static void deleteTextWatcher (EditText currentText, EditText previousText){

        currentText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                previousText.requestFocus();
            }
            return false;
        });

    }

    public static boolean isDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void warn(String msg) {
        Log.w("DATOO RTC", msg);
    }

    public static void error(String msg) {
        Log.e("DATOO RTC", msg);
    }

    public static boolean isToday(Date messageLimitCounterToday) {
        return DateUtils.isToday(messageLimitCounterToday.getTime());
    }

    /**
     * method to get color
     *
     * @param context this is the first parameter for getColor  method
     * @param id      this is the second parameter for getColor  method
     * @return return value
     */
    public static int getColor(Context context, int id) {
        if (isAndroid5()) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static void setCountryAdapter(Context context, List<CountriesModel> list, CountriesAdapter countriesAdapter){

        try {
            JSONArray countries = new JSONArray(loadJSONAllCountriesFromAsset(context));

            for (int i = 0; i < countries.length(); i++) {

                JSONObject country = (JSONObject) countries.get(i);

                list.add(new CountriesModel(country.getString("name"), country.getString("code"), country.getString("dial_code")));

                countriesAdapter.setCountries(list);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String loadJSONAllCountriesFromAsset(Context mContext) {
        String json;
        try {
            InputStream is = mContext.getAssets().open("all_country_phones.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                json = new String(buffer, StandardCharsets.UTF_8);
            } else {
                json = new String(buffer, "UTF-8");
            }

            //Log.v("COUNTRIES ALL", json);

        } catch (IOException ex) {
            ex.printStackTrace();

            //Log.v("COUNTRIES ALL", ex.getLocalizedMessage());

            return null;
        }
        return json;
    }

    //to retrieve currency code
    public static String getCurrencyCode(String countryCode) {
        return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
    }

    //to retrieve currency symbol
    public static String getCurrencySymbol(String countryCode) {
        return Currency.getInstance(new Locale("", countryCode)).getSymbol();
    }
}
