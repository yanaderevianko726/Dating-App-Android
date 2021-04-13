package com.sugardaddy.citascasualesconocergentenuevacercadeti.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.BuildConfig;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections.IncomingActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.AutoMessagesModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.CallsModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.FollowModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.GiftModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveMessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveStreamModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.WithdrawModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.parsegooglesignin.ParseGoogleSignIn;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.parsephonelogin.ParsePhoneLogin;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.SharedPrefUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.AGEventHandler;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.WorkerThread;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.EncountersModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.MessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ReportModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.FontsOverride;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;

import static com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants.LOG_TAG;

public class Application extends MultiDexApplication implements AGEventHandler, LifecycleObserver, android.app.Application.ActivityLifecycleCallbacks {

    @SuppressLint("StaticFieldLeak")
    protected static Application Instance;
    public Activity mActivity;
    Timer mTimer;

    FirebaseAnalytics mFirebaseAnalytics;

    private WorkerThread mWorkerThread;

    private RequestQueue mRequestQueue;

    public void setInstance(Application instance) {
        Application.Instance = instance;
    }

    public boolean isAppOpened;

    public boolean isChatOpened;
    public String chatObjectId;
    public SharedPrefUtil sharedPrefUtil;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        Instance = this;
        sharedPrefUtil = new SharedPrefUtil(this);
        MultiDex.install(getBaseContext());

        registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        EmojiManager.install(new TwitterEmojiProvider());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        if (!sharedPrefUtil.getLanguage(this).equals(""))
            setDefaultLocale(new Locale(sharedPrefUtil.getLanguage(this)));
        else {
            sharedPrefUtil.setLanguage(this, Locale.getDefault().getLanguage().toLowerCase());
        }

        setupFonts();

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(EncountersModel.class);
        ParseObject.registerSubclass(ConnectionListModel.class);
        ParseObject.registerSubclass(ReportModel.class);
        ParseObject.registerSubclass(MessageModel.class);
        ParseObject.registerSubclass(LiveStreamModel.class);
        ParseObject.registerSubclass(FollowModel.class);
        ParseObject.registerSubclass(LiveMessageModel.class);
        ParseObject.registerSubclass(GiftModel.class);
        ParseObject.registerSubclass(CallsModel.class);
        ParseObject.registerSubclass(MessageModel.class);
        ParseObject.registerSubclass(AutoMessagesModel.class);
        ParseObject.registerSubclass(WithdrawModel.class);

        if (BuildConfig.DEBUG){
            Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        }

        Parse.Configuration config = new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(Config.SERVER_APP_ID)
                .clientKey(Config.SERVER_CLIENT_KEY)
                .server(Config.SERVER_URL)
                .build();
        Parse.initialize(config);

        ParseFacebookUtils.initialize(this);
        ParseGoogleSignIn.initialize(this);
        ParsePhoneLogin.initialize(this);

        User account = User.getUser();

        Handler mHandler = new Handler();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(() -> {
                    // run main thread code
                    updateTimeOnline(account);
                });
            }
        }, 1000, 30 * 1000);

        initWorkerThread();

        getWorkerThread().eventHandler().addEventHandler(this); // Move to User session if error

        if(isAccountValid(account)) {

            if (!account.getLanguage().isEmpty()){

                setDefaultLocale(new Locale(account.getLanguage()));
                sharedPrefUtil.setLanguage(this, account.getLanguage());
            }

            account.setInstallation(ParseInstallation.getCurrentInstallation());
            account.saveInBackground();

            QuickHelp.saveInstallation(account);

            mFirebaseAnalytics.setUserId(account.getObjectId());

            FirebaseCrashlytics.getInstance().setUserId(account.getObjectId());

            if (account.getColFullName() != null && !account.getColFullName().isEmpty()){
                FirebaseCrashlytics.getInstance().setCustomKey("FullName", account.getColFullName());
            }

            if (account.getEmail() != null && !account.getEmail().isEmpty()){

                FirebaseCrashlytics.getInstance().setCustomKey("Email", account.getEmail());
            }

            if (account.getColGender() != null && !account.getColGender().isEmpty()){
                if (account.getColGender().equals(User.GENDER_MALE)){
                    FirebaseCrashlytics.getInstance().setCustomKey("Gender", User.GENDER_MALE);
                } else if (account.getColGender().equals(User.GENDER_FEMALE)){
                    FirebaseCrashlytics.getInstance().setCustomKey("Gender", User.GENDER_FEMALE);
                }
            }


            getWorkerThread().connectToRtmService(String.valueOf(account.getUid()));

        } else {

            QuickHelp.removeUserToInstallation();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppStared() {
        isAppOpened = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAppResumed() {
        isAppOpened = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onAppPaused() {
        isAppOpened = false;
        setIsChatOpened(false, null);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppStoped() {
        isAppOpened = false;
        setIsChatOpened(false, null);
    }

    public void setIsChatOpened(boolean isOpened, String objectId){

        isChatOpened = isOpened;
        chatObjectId = objectId;
    }

    public boolean isChatOpened(){
        return isChatOpened;
    }

    public String getChatObjectId(){
        return chatObjectId;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        RtcEngine.destroy();
        mTimer.cancel();
        setIsChatOpened(false, null);

        deInitWorkerThread();

        getWorkerThread().eventHandler().removeEventHandler(this);
    }

    public void updateTimeOnline(User user) {

        if (user != null){

            // Get Calendar Date and Time
            Calendar now = Calendar.getInstance();
            Date date = now.getTime();

            user.setLastOnline(date);
            if (user.getBirthDate() != null){
                user.setAge(QuickHelp.getAgeFromDate(user.getBirthDate()));
            }
            user.saveInBackground();

            getWorkerThread().connectToRtmService(String.valueOf(user.getUid()));

        }

    }

    public static boolean isAccountValid(ParseUser account) {
        return !(account == null || TextUtils.isEmpty(account.getObjectId()));
    }

    public static synchronized Application getInstance() {
        return Instance;
    }

    protected void setDefaultLocale(Locale locale) {

        Locale.setDefault(locale);
        Configuration configLang = new Configuration();
        configLang.locale = locale;
        getInstance().getApplicationContext().getResources().updateConfiguration(configLang, getInstance().getApplicationContext().getResources().getDisplayMetrics());

        Log.d("LANG: ",  "setDefaultLocale: " + Locale.getDefault().getLanguage());
    }

    public void setupFonts(){

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/mabry_regular_pro.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/mabry_regular_pro.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/mabry_regular_pro.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/mabry_regular_pro.ttf");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


        Locale locale = new Locale(new SharedPrefUtil(this).getLanguage(this));

        Locale.setDefault(locale);
        Configuration configLang = new Configuration();
        configLang.locale = locale;
        base.getResources().updateConfiguration(configLang, base.getResources().getDisplayMetrics());

        Log.d("LANG: ",  "attachBaseContext: " + Locale.getDefault().getLanguage());
    }

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {

        if (mWorkerThread != null){

            mWorkerThread.exit();
            try {
                mWorkerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mWorkerThread = null;
        }


    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(LOG_TAG);
        getRequestQueue().add(req);
    }

    public boolean isAppOpened(){
        return isAppOpened;
    }

    /////////////////////////// Calls ////////////////////////////

    @Override
    public void onLoginSuccess(String uid) {

    }

    @Override
    public void onLoginFailed(String uid, ErrorInfo error) {
    }

    @Override
    public void onPeerOnlineStatusQueried(String uid, boolean online) {
    }

    @Override
    public void onInvitationReceivedByPeer(LocalInvitation invitation) {
    }

    @Override
    public void onLocalInvitationAccepted(LocalInvitation invitation, String response) {
    }

    @Override
    public void onLocalInvitationRefused(LocalInvitation invitation, String response) {
    }

    @Override
    public void onLocalInvitationCanceled(LocalInvitation invitation) {
    }

    @Override
    public void onInvitationReceived(RemoteInvitation invitation) {

        getInstance().getWorkerThread().getEngineConfig().mRemoteInvitation = invitation;

        Log.v("AGORA", "onInvitationReceived");

        ParseQuery<User> parseQuery = User.getUserQuery();
        parseQuery.whereEqualTo(User.UID, Integer.valueOf(invitation.getCallerId()));
        parseQuery.getFirstInBackground((user, e) -> {

            if (e == null){
                QuickHelp.goToActivityInComing(this, IncomingActivity.class, user, invitation.getChannelId(), invitation.getContent());
            }
        });
    }

    @Override
    public void onInvitationRefused(RemoteInvitation invitation) {
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
    }

    @Override
    public void onFirstRemoteAudioFrame(int uid, int elapsed) {

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    @Override
    public void onUserOffline(int uid, int reason) {
    }

    @Override
    public void onExtraCallback(int type, Object... data) {
    }

    @Override
    public void onLastmileQuality(int quality) {
    }

    @Override
    public void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result) {
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        Locale locale = new Locale(new SharedPrefUtil(this).getLanguage(this));

        Locale.setDefault(locale);
        Configuration configLang = new Configuration();
        configLang.locale = locale;
        activity.getResources().updateConfiguration(configLang, activity.getResources().getDisplayMetrics());

        Log.d("LANG: ",  "onActivityCreated: " + Locale.getDefault().getLanguage());
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

        Locale locale = new Locale(new SharedPrefUtil(this).getLanguage(this));

        Locale.setDefault(locale);
        Configuration configLang = new Configuration();
        configLang.locale = locale;
        activity.getResources().updateConfiguration(configLang, activity.getResources().getDisplayMetrics());

        Log.d("LANG: ",  "onActivityStarted: " + Locale.getDefault().getLanguage());

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        mActivity = activity;

        Locale locale = new Locale(new SharedPrefUtil(this).getLanguage(this));

        Locale.setDefault(locale);
        Configuration configLang = new Configuration();
        configLang.locale = locale;
        activity.getResources().updateConfiguration(configLang, activity.getResources().getDisplayMetrics());

        Log.d("LANG: ",  "onActivityResumed: " + Locale.getDefault().getLanguage());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        mActivity = null;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public Activity getCurrentActivity(){
        return mActivity;
    }
}