package com.sugardaddy.citascasualesconocergentenuevacercadeti.app;


import com.sugardaddy.citascasualesconocergentenuevacercadeti.BuildConfig;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class Constants {

    public static final String LOG_TAG = Application.getInstance().getApplicationContext().getString(R.string.app_name);

    private static final boolean DEBUGGING_MODE = BuildConfig.DEBUG;

    public static final String DEFAULT_COUNTRY_CODE = "US";//this for country code
    public static final int SELECT_COUNTRY = 0x011;

    // Online/offline track
    public static long TIME_TO_SOON = 60 * 1000;
    public static long TIME_TO_OFFLINE = 2 * 60 * 1000;

    public static final String INSTAGRAM_URL = "https://graph.instagram.com/";

    // Shared preferences
    public static final String IS_NEARBY_NEW = "IS_NEARBY_NEW";
    public static final String IS_NEARBY_SHOWCASE_SHOWED = "IS_NEARBY_SHOWCASE_SHOWED";
    public static final String IS_ENCOUNTERS_SHOWCASE_SHOWED = "IS_ENCOUNTERS_SHOWCASE_SHOWED";
    public static final String CONNECTION_COUNTER_MESSAGES = "COUNTER_MESSAGES";
    public static final String CONNECTION_COUNTER_FAVORITES = "COUNTER_FAVORITES";

    private static final String HOME_BANNER_ADS_TEST = "ca-app-pub-3940256099942544/6300978111";
    private static final String REWARDED_ADS_TEST = "ca-app-pub-3940256099942544/5224354917";
    private static final String NATIVE_ADS_TEST = "ca-app-pub-3940256099942544/2247696110";

    public static final String FLUTTERWAYE_PUBLISH_KEY = "FLWPUBK_TEST-63f53fa73d352064dbc4ac125817eb11-X";
    public static final String FLUTTERWAYE_ENCRYPTION_KEY = "FLWSECK_TEST90a17db1f71c";

    public static final String PAYPAL_CLIENT_ID = "AeDV4Lr7Byih9L7tccceD18iI8ng6fXbtUdzzql6ibEdzP3xc-KYSAQnxsbYen2pI7U2ES5iKrq3YEAy";

    public static String getRewardedAdsId(){

        if (Constants.DEBUGGING_MODE){
            return REWARDED_ADS_TEST;
        } else {
            return Config.REWARDED_ADS;
        }
    }

    public static String getHomeBannerAdsId(){

        if (Constants.DEBUGGING_MODE){
            return HOME_BANNER_ADS_TEST;
        } else {
            return Config.HOME_BANNER_ADS;
        }
    }

    public static String getNearByNativeAdsId(){

        if (Constants.DEBUGGING_MODE){
            return NATIVE_ADS_TEST;
        } else {
            return Config.NEARBY_NATIVE_ADS;
        }
    }

    public static String getEncountersNativeAdsId(){

        if (Constants.DEBUGGING_MODE){
            return NATIVE_ADS_TEST;
        } else {
            return Config.ENCOUNTERS_NATIVE_ADS;
        }
    }

    public static String getFlutterwayePublishKey(){

        if (Constants.DEBUGGING_MODE){
            return FLUTTERWAYE_PUBLISH_KEY;
        } else {
            return Config.FLUTTERWAYE_PUBLISH_KEY;
        }
    }

    public static String getFlutterwayeEncryptionKey(){

        if (Constants.DEBUGGING_MODE){
            return FLUTTERWAYE_ENCRYPTION_KEY;
        } else {
            return Config.FLUTTERWAYE_ENCRYPTION_KEY;
        }
    }

    public static String getPaypalClientId(){

        if (Constants.DEBUGGING_MODE){
            return PAYPAL_CLIENT_ID;
        } else {
            return Config.PAYPAL_CLIENT_ID;
        }
    }

    public static boolean isProduction(){
        return !Constants.DEBUGGING_MODE;
    }

    public static VideoEncoderConfiguration.VideoDimensions[] VIDEO_DIMENSIONS = new VideoEncoderConfiguration.VideoDimensions[] {
            VideoEncoderConfiguration.VD_320x240,
            VideoEncoderConfiguration.VD_480x360,
            VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.VD_640x480,
            new VideoEncoderConfiguration.VideoDimensions(960, 540),
            VideoEncoderConfiguration.VD_1280x720
    };

}
