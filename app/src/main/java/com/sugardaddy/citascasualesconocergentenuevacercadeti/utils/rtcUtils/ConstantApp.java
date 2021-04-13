package com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils;


public class ConstantApp {

    public static int CALL_IN = 0x01;
    public static int CALL_OUT = 0x02;
    public static int UNKNOWN = -99;

    public static int VOICE_CALL = 112;
    public static int VIDEO_CALL = 113;


    static class PrefManager {
        static final String PREF_PROPERTY_UID = "pOCXx_uid";
    }

    public static final String ACTION_KEY_CHANNEL_NAME = "ecHANEL";
    public static final String ACTION_KEY_UID = "ecUID";
    public static final String ACTION_KEY_SUBSCRIBER_OBJECT = "exSubscriberObj";
    public static final String ACTION_KEY_SUBSCRIBER = "exSubscriber";
    public static final String ACTION_KEY_MakeOrReceive = "ecxxMakeOrRece";

    static class AppError {
        static final int NO_NETWORK_CONNECTION = 3;
    }
}
