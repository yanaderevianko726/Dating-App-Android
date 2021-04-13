package com.sugardaddy.citascasualesconocergentenuevacercadeti.app;


public class Config {

    // Parse Server
    public static final String SERVER_URL = "https://parseapi.back4app.com/";
    public static final String LIVE_QUERY_URL = "ws://qiluapp.back4app.io/";

    static final String SERVER_APP_ID = "3fD1ZsWF248lpjkDIPbPTy03OTIrmdl0kjOJpRud";
    static final String SERVER_CLIENT_KEY = "vJexlhnlaBF08H4Vx9cXGYUznepZbDOetiYr5KiC";

    // Push notifications
    public static final String CHANNEL = "global";

    // Agora API
    public static final String AGORA_APP_ID = "7f87808dc4dd4d1486dfaffe95d970a6";

    // FlutterWave
    public static final String FLUTTERWAYE_PUBLISH_KEY = "FLWPUBK-b3as1d507e54545fadbace19b569-X";
    public static final String FLUTTERWAYE_ENCRYPTION_KEY = "92a7evb56f9584727dc313";

    // PayPal
    public static final String PAYPAL_CLIENT_ID = "AVH4tYrdpGln1H4SiJ4-44NBalxHZYG806bgGYjh8Kcr6zQuvMK6PAVognpciXERaOn4bV5s7OIyz-jZ";

    // Instagram API
    public static final String INSTAGRAM_APP_ID = "740734960067456";
    public static final String INSTAGRAM_APP_SECRET = "b87d6d5a43c35381995dc18f9fafdb19";
    public static final String INSTAGRAM_REDIRECT_URI = "http://qiluchat.jasaadwordsid.com/";

    // Google Admob
    static final String HOME_BANNER_ADS = "ca-app-pub-1856022005495230/8788617639";
    static final String REWARDED_ADS= "ca-app-pub-1856022005495230/4657800937";
    static final String NEARBY_NATIVE_ADS = "ca-app-pub-1856022045495340/4662682120";
    static final String ENCOUNTERS_NATIVE_ADS = "ca-app-pub-1856025005495340/4662682120";

    // Google Play In-app Purchases IDs
    public static final String CREDIT_550 = "qiluchat.550.credits";
    public static final String CREDIT_100 = "qiluchat.100.credits";
    public static final String CREDIT_1250 = "qiluchat.1250.credits";
    public static final String CREDIT_2750 = "qiluchat.2750.credits";

    public static final String PAY_LIFETIME = "qiluchat.pay.lifetime";

    //Google Play In-app Subscription IDs
    public static final String SUBS_3_MONTHS = "qiluchat.3.months";
    public static final String SUBS_1_WEEK = "qiluchat.1.week";
    public static final String SUBS_1_MONTH = "qiluchat.1.month";
    public static final String SUBS_6_MONTHS = "qiluchat.6.months";

    // Web links for help, privacy policy and terms of use.
    public static final String HELP_CENTER = "https://qiluchat.blogspot.com/p/popular-questions.html";
    public static final String PRIVACY_POLICY = "https://qiluchat.blogspot.com/p/privacy-policy.html";
    public static final String TERMS_OF_USE = "https://qiluchat.blogspot.com/p/terms-conditions.html";
    public static final String TERMS_OF_USE_IN_APP = "https://qiluchat.blogspot.com/p/terms-conditions.html";

    // Withdraw tokens to PayPal
    public static final int MinTokenWithdraw = 1500; // Minimum amount of tokens allowed to exchange
    public static final int WithdrawExchangeRate = 300; // 300 tokens equal to 1 USD

    // Enable or disable logins system
    public static final boolean EMAIL_LOGIN = true;
    public static final boolean PHONE_LOGIN = true;
    public static final boolean GOOGLE_LOGIN = true;
    public static final boolean FACEBOOK_LOGIN = true;
    // Enable or Disable Payment Systems
    public static final boolean PAYPAL_ENABLED = true;
    public static final boolean FLUTTER_WAVE_ENABLED = true;
    // Extra features to add in payment methods
    public static final boolean PAYPAL_CREDIT_CARD_ENABLED = false;
    // Exchange your tokens
    public static final int MinTokenExchange = 200; // Minimum amount of tokens allowed to exchange
    public static final int TokenExchangeRate = 5; // 5 tokens equal to 1 Credit

    // Enable or Disable paid messages.
    public static final boolean isPaidMessagesActivated = false;

    // Credits needed to activate features
    public static int TYPE_RISE_UP = 50;
    public static int TYPE_GET_MORE_VISITS = 100;
    public static int TYPE_ADD_EXTRA_SHOWS = 100;
    public static int TYPE_SHOW_IM_ONLINE = 100;
    public static int TYPE_3X_POPULAR = 200;

    // Amount of days to activate features
    public static int DAYS_TO_ACTIVATE_FEATURES = 7;

    // Enable or Disable Ads and Premium.
    public static final boolean isAdsActivated = true;
    public static final boolean isPremiumEnabled = true;
    public static final boolean isNearByNativeAdsActivated = false; //don't change false to true, it's still in development stage.
    public static final boolean isEncountersNativeAdsActivated = true;

    // Encounters crush
    public static final boolean isCrushCreditNeeded = true;
    public static final int CrushCreditNeeded = 50;
    public static final boolean isCrushAdsEnabled = true;
    public static final int CrushAdsLimitPerDay = 5;

    // Application setup
    public static final String bio = "Hey! i'm using QiLu!";
    public static final int WelcomeCredit = 10;
    public static final int MinimumAgeToRegister = 18;
    public static final int MaximumAgeToRegister = 80;
    public static final int MaxUsersNearToShow = 10000;
    public static final int DistanceForRealBadge = 5;
    public static final int DistanceForRealKm = 10000;
    public static final int MinDistanceBetweenUsers = 20;
    public static final int MaxDistanceBetweenUsers = 10000;
    public static final double DistanceBetweenUsersLive = 10000;
    public static final boolean ShowBlockedUsersOnQuery = true;
    public static final boolean isVideoCallEnabled = true;
    public static final boolean isVoiceCallEnabled = true;
    public static final int ShowNearbyNativeAdsAfter = 9;
    public static final int ShowEncountersNativeAdsAfter = 2;
    public static final int freeMessagesInTotal = 10;
    public static final int freeMessagesPerDay = 5;

    // Enable or Disable Fake messages.
    public static final boolean isFakeMessagesActivated = false;
    public static final String defaultFakeMessage = "Hello, how are you ?";
}