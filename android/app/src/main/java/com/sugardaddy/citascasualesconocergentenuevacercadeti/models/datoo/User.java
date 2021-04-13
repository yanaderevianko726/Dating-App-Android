package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo;

import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants.LOG_TAG;

@ParseClassName("_User")
public class User extends ParseUser {

    public static final String ROLE_USER = "user";

    public static final String UID = "uid";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_UPDATED_AT = "updatedAt";

    private static final String HAS_PASSWORD = "has_password";
    public static final String COL_GENDER = "gender";
    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";
    public static final String GENDER_BOTH = "both";

    public static final String STATUS_ALL = "all";
    public static final String STATUS_ONLINE = "online";
    public static final String STATUS_NEW = "new";

    private static final String COL_INSTALLATION = "installation";
    public static final String COL_ID = "objectId";
    public static final String COL_ROLE = "role";
    public static final String COL_FULL_NAME = "name";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_BIO = "bio";
    public static final String COL_USERNAME = "username";
    private static final String COL_POPULARITY = "popularity";
    private static final String COL_EMAIL = "email";
    private static final String COL_EMAIL_VERIFIED = "emailVerified";
    private static final String COL_PHOTO_POSITION = "avatar_position";
    public static final String COL_PHOTOS = "photos";
    public static final String COL_GEO_POINT = "geopoint";
    public static final String COL_HAS_GEO_POINT = "hasGeopoint";
    public static final String COL_BIRTHDATE = "birthday";
    private static final String COL_LOCATION = "location";
    private static final String COL_CITY = "city";
    private static final String COL_LAST_ONLINE = "lastOnline";
    public static final String COL_AGE = "age";
    private static final String COL_ABOUT_ME = "aboutMe";

    // Filter preferences
    private static final String PREF_LOCATION_TYPE = "prefLocationType";
    //public static final String PREF_LOCATION = "prefLocation";
    private static final String PREF_GENDER = "prefGender";
    private static final String PREF_STATUS = "prefStatus";
    private static final String PREF_MIN_AGE = "prefMinAge";
    private static final String PREF_MAX_AGE = "prefMaxAge";

    // Vip and Premium features

    private static final String PREMIUM_LIFETIME = "premium_lifetime";
    private static final String PREMIUM = "premium";
    private static final String CREDIT = "credit";
    private static final String TOKENS = "tokens";

    private static final String VIP_ADS_DISABLED = "AdsDisabled";
    private static final String VIP_3X_POPULAR = "3xpopular";
    private static final String VIP_SHOW_ONLINE = "showOnline";
    private static final String VIP_EXTRA_SHOWS = "extraShows";
    public static final String VIP_MORE_VISITS = "getMoreVisits";
    public static final String VIP_MOVE_TO_TOP = "moveToTop";

    private static final String VIP_INVISIBLE_MODE = "invisibleMode";

    private static final String VIP_IS_INVISIBLE = "isInvisible";

    // Instagram
    private static final String INSTAGRAM_ID = "instaId";
    private static final String INSTAGRAM_LINK = "instaLink";
    private static final String INSTAGRAM_TOKEN = "instaToken";

    // Facebook
    private static final String FACEBOOK_ID = "fbId";
    private static final String FACEBOOK_LINK = "fbLink";

    // Google
    private static final String GOOGLE_ID = "ggId";

    // Privacy
    private static final String PRIVACY_SHOW_DISTANCE = "privacyShowDistance";
    private static final String PRIVACY_SHOW_ONLINE_STATUS = "privacyShowOnlineStatus";


    // Invisible mode
    public static final String PRIVACY_ALMOST_INVISIBLE = "privacyAlmostInvisible";
    private static final String PRIVACY_CLOAKED_INVISIBILITY = "privacyCloakedInvisibility";
    private static final String PRIVACY_HIDE_PREMIUM_STATUS = "privacyHidePremiumStatus";

    // Notification-
    private static final String PUSH_NOTIFICATIONS_MASSAGES = "pushMessages";
    private static final String PUSH_NOTIFICATIONS_MATCHES = "pushMatches";
    private static final String PUSH_NOTIFICATIONS_LIKED_YOU = "pushLikedYou";
    private static final String PUSH_NOTIFICATIONS_PROFILE_VISITOR = "pushProfileVisitor";
    private static final String PUSH_NOTIFICATIONS_FAVORITED_YOU = "pushFavoritedYou";
    private static final String PUSH_NOTIFICATIONS_DATOO_LIVE = "pushLive";

    public static final String BLOCKED_USERS = "blockedUsers";
    public static final String USER_BLOCKED_STATUS = "activationStatus";


    // Profile Edit
    private static final String RELATIONSHIP = "profile_relationship";
    public static final String RELATIONSHIP_COMPLICATED = "CP";
    public static final String RELATIONSHIP_SINGLE = "SG";
    public static final String RELATIONSHIP_TAKEN = "TK";

    private static final String SEXUALITY = "profile_sexuality";
    public static final String SEXUALITY_BISEXUAL = "BS";
    public static final String SEXUALITY_LESBIAN = "LB";
    public static final String SEXUALITY_ASK_ME = "AM";
    public static final String SEXUALITY_STRAIGHT = "ST";

    private static final String HEIGHT = "profile_body_height";

    public static final String BODY_TYPE = "profile_boty_type";
    public static final String BODY_TYPE_ATHLETIC = "AT";
    public static final String BODY_TYPE_AVERAGE = "AV";
    public static final String BODY_TYPE_FEW_EXTRA_POUNDS = "EP";
    public static final String BODY_TYPE_MUSCULAR = "ML";
    public static final String BODY_TYPE_BIG_AND_BEAUTIFUL = "BB";
    public static final String BODY_TYPE_SLIM = "SL";

    private static final String LIVING = "profile_living";
    public static final String LIVING_BY_MYSELF = "MS";
    public static final String LIVING_STUDENT_DORMITORY = "SD";
    public static final String LIVING_WITH_PARENTS = "PR";
    public static final String LIVING_WITH_PARTNER = "PN";
    public static final String LIVING_WITH_ROOMMATES = "RM";

    private static final String KIDS = "profile_kids";
    public static final String KIDS_GROWN_UP = "GU";
    public static final String KIDS_ALREADY_HAVE = "AH";
    public static final String KIDS_NO_NOVER = "NN";
    public static final String KIDS_SOMEDAY = "SY";

    private static final String SMOKING = "profile_smoking";
    public static final String SMOKING_IAM_A_HEAVY_SMOKER = "ES";
    public static final String SMOKING_I_HATE_SMOKING = "HS";
    public static final String SMOKING_I_DO_NOT_LIKE_IT = "DL";
    public static final String SMOKING_IAM_A_SOCIAL_SMOKER = "SM";
    public static final String SMOKING_I_SMOKE_OCCASIONALLY = "OC";

    private static final String DRINKING = "profile_drinking";
    public static final String DRINKING_I_DRINK_SOCIALLY = "DS";
    public static final String DRINKING_I_DO_NOT_DRINK = "DD";
    public static final String DRINKING_IAM_AGAINST_DRINKING = "AD";
    public static final String DRINKING_I_DRINK_A_LOT = "DT";

    private static final String LANGUAGE = "profile_language";

    private static final String WHAT_I_WANT = "profile_honestly_want";
    public static final String WHAT_I_WANT_JUST_TO_CHAT = "JC";
    public static final String WHAT_I_WANT_SOMETHING_CASUAL = "SC";
    public static final String WHAT_I_WANT_SOMETHING_SERIOUS = "SS";
    public static final String WHAT_I_WANT_LET_SEE_WHAT_HAPPENS = "WH";

    public static final String LIVE_STREAMS_COUNT = "live_stream_counter";
    private static final String LIVE_STREAMS_VIEWERS_COUNT = "live_viewers_counter";
    private static final String LIVE_STREAMS_VIEWERS = "live_viewers";
    private static final String LIVE_FOLLOWERS_COUNT = "live_followers_counter";

    private static final String PREF_DISTANCE = "distanceFilter";

    public static final String MESSAGE_LIMIT_COUNTER_TOTAL = "messagesLimitTotal";
    public static final String MESSAGE_LIMIT_COUNTER_DAILY = "messagesLimitDaily";
    public static final String MESSAGE_LIMIT_COUNTER_TODAY = "messagesLimitTotalToday";

    public static final String PASSWORD_SECONDARY = "password_secondary";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_CODE = "country_code";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String COUNTRY_DIAL_CODE = "country_dial_code";
    public static final String PHONE_NUMBER_FULL = "phone_number_full";

    public static final String CRUSH_ADS_COUNTER = "crush_ads_counter";
    public static final String CRUSH_ADS_LIMIT_COUNTER_TODAY = "crush_ads_date";

    public void setMessageLimitCounterTotal(){
        increment(MESSAGE_LIMIT_COUNTER_TOTAL, +1);
    }

    public int getMessageLimitCounterTotal(){
        return getInt(MESSAGE_LIMIT_COUNTER_TOTAL);
    }

    public void setMessageLimitCounterDaily(){
        increment(MESSAGE_LIMIT_COUNTER_DAILY, +1);
    }

    public void setMessageLimitCounterDailyOne(){
        put(MESSAGE_LIMIT_COUNTER_DAILY, 1);
    }

    public int getMessageLimitCounterDaily(){
        return getInt(MESSAGE_LIMIT_COUNTER_DAILY);
    }

    public Date getMessageLimitCounterToday(){
        if (getDate(MESSAGE_LIMIT_COUNTER_TODAY) != null){
            return getDate(MESSAGE_LIMIT_COUNTER_TODAY);
        } else {
            return null;
        }

    }

    public void setMessageLimitCounterToday(Date date){
        put(MESSAGE_LIMIT_COUNTER_TODAY, date);
    }

    public Date getCrushAdsLimitCounterToday(){
        if (getDate(CRUSH_ADS_LIMIT_COUNTER_TODAY) != null){
            return getDate(CRUSH_ADS_LIMIT_COUNTER_TODAY);
        } else {
            return null;
        }

    }

    public void setCrushAdsLimitCounterToday(Date date){
        put(CRUSH_ADS_LIMIT_COUNTER_TODAY, date);
    }

    public int getCrushAdsCounter(){
        return getInt(CRUSH_ADS_COUNTER);
    }

    public void setCrushAdsCounter(){
        increment(CRUSH_ADS_COUNTER, 1);
    }

    public void setCrushAdsCounterInit(){
        put(CRUSH_ADS_COUNTER, 1);
    }

    public void setUid(int uid){
        put(UID, uid);
    }

    public String getPasswordSecondary(){
        return getString(PASSWORD_SECONDARY);
    }

    public void setPasswordSecondary(String passwordSecondary){
        put(PASSWORD_SECONDARY, passwordSecondary);
    }

    public String getCountry(){
        return getString(COUNTRY);
    }

    public void setCountry(String country){
        put(COUNTRY, country);
    }

    public String getCountryCode(){
        return getString(COUNTRY_CODE);
    }

    public void setCountryCode(String countryCode){
        put(COUNTRY_CODE, countryCode);
    }

    public String getCountryDialCode(){
        return getString(COUNTRY_DIAL_CODE);
    }

    public void setCountryDialCode(String countryDialCode){
        put(COUNTRY_DIAL_CODE, countryDialCode);
    }

    public String getPhoneNumber(){
        return getString(PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber){
        put(PHONE_NUMBER, phoneNumber);
    }

    public String getPhoneNumberFull(){

        if (getString(PHONE_NUMBER_FULL) != null){
            return getString(PHONE_NUMBER_FULL);
        } else return "";
    }

    public void setPhoneNumberFull(String phoneNumberFull){
        put(PHONE_NUMBER_FULL, phoneNumberFull);
    }

    public int getUid(){
        return getInt(UID);
    }

    public void setRole(String role){
        put(COL_ROLE, role);
    }

    public String getRole(){
        if (getString(COL_ROLE) != null){
            return getString(COL_ROLE);
        } else return ROLE_USER;
    }

    public void setRelationship(String relationship){
        put(RELATIONSHIP, relationship);
    }

    public String getRelationship(){

        if (getString(RELATIONSHIP) != null){
            return getString(RELATIONSHIP);
        } else return "";
    }

    public void setSexuality(String sexuality){
        put(SEXUALITY, sexuality);
    }

    public String getSexuality(){

        if (getString(SEXUALITY) != null){
            return getString(SEXUALITY);
        } else return "";
    }

    public void setHeight(int height){
        put(HEIGHT, height);
    }

    public int getHeight(){

        return getInt(HEIGHT);
    }

    public void setBodyType(String bodyType){
        put(BODY_TYPE, bodyType);
    }

    public String getBodyType(){

        if (getString(BODY_TYPE) != null){
            return getString(BODY_TYPE);
        } else return "";
    }

    public void setLiving(String living){
        put(LIVING, living);
    }

    public String getLiving(){

        if (getString(LIVING) != null){
            return getString(LIVING);
        } else return "";
    }

    public void setKids(String kids){
        put(KIDS, kids);
    }

    public String getKids(){

        if (getString(KIDS) != null){
            return getString(KIDS);
        } else return "";
    }

    public void setSmoking(String smoking){
        put(SMOKING, smoking);
    }

    public String getSmoking(){

        if (getString(SMOKING) != null){
            return getString(SMOKING);
        } else return "";
    }

    public void setDrinking(String drinking){
        put(DRINKING, drinking);
    }

    public String getDrinking(){

        if (getString(DRINKING) != null){
            return getString(DRINKING);
        } else return "";
    }

    public void setLanguage(String language){
        put(LANGUAGE, language);
    }

    public String getLanguage(){

        if (getString(LANGUAGE) != null){
            return getString(LANGUAGE);
        } else return "";
    }

    public void setAboutMe(String aboutMe){
        put(COL_ABOUT_ME, aboutMe);
    }

    public String getAboutMe(){

        if (getString(COL_ABOUT_ME) != null){
            return getString(COL_ABOUT_ME);
        } else return "";
    }

    public String getDefaultAboutMe(){

        if (getString(COL_BIO) != null){
            return getString(COL_BIO);
        } else return "";
    }

    public void setWhatIWant(String whatIWant){
        put(WHAT_I_WANT, whatIWant);
    }

    public String getWhatIWant(){

        if (getString(WHAT_I_WANT) != null){
            return getString(WHAT_I_WANT);
        } else return "";
    }

    public void setFacebookId(String facebookId){

        if (facebookId == null){

            remove(FACEBOOK_ID);
        } else {

            put(FACEBOOK_ID, facebookId);
        }
    }

    public void setGoogleId(String googleId){

        if (googleId == null){

            remove(GOOGLE_ID);
        } else {

            put(GOOGLE_ID, googleId);
        }
    }

    public String getFacebookId(){

        if (getString(FACEBOOK_ID) != null){
            return getString(FACEBOOK_ID);
        } else return "";

    }

    public void setFacebookLink(String facebookLink){

        if (facebookLink == null){

            remove(FACEBOOK_LINK);
        } else {

            put(FACEBOOK_LINK, facebookLink);
        }
    }

    public void setHasGeoPoint(boolean hasGeoPoint){

        put(COL_HAS_GEO_POINT, hasGeoPoint);
    }

    public boolean hasGeoPoint(){

        return getBoolean(COL_HAS_GEO_POINT);
    }

    public String getFacebookLink(){

        if (getString(FACEBOOK_LINK) != null){
            return getString(FACEBOOK_LINK);
        } else return "";
    }

    public void setInstagramId(String instagramId){

        if (instagramId == null){

            remove(INSTAGRAM_ID);
        } else {

            put(INSTAGRAM_ID, instagramId);
        }
    }

    public String getInstagramId(){

        if (getString(INSTAGRAM_ID) != null){
            return getString(INSTAGRAM_ID);
        } else return "";
    }

    public void setInstagramLink(String instagramLink){


        if (instagramLink == null){

            remove(INSTAGRAM_LINK);
        } else {

            put(INSTAGRAM_LINK, instagramLink);
        }
    }

    public String getInstagramLink(){

        if (getString(INSTAGRAM_LINK) != null){
            return getString(INSTAGRAM_LINK);
        } else return "";
    }

    public void setInstagramToken(String instagramToken){

        if (instagramToken == null){

            remove(INSTAGRAM_TOKEN);
        } else {

            put(INSTAGRAM_TOKEN, instagramToken);
        }
    }

    public String getInstagramToken(){

        if (getString(INSTAGRAM_TOKEN) != null){
            return getString(INSTAGRAM_TOKEN);
        } else return "";

    }

    public void setLocationTypeNearBy(boolean locationTypeNearBy){
        put(PREF_LOCATION_TYPE, locationTypeNearBy);
    }

    public boolean isLocationTypeNearby() {

        // return true if its disabled
        return  this.getBoolean(PREF_LOCATION_TYPE);
    }

    public void setPrefGender(String prefGender) {
        put(PREF_GENDER, prefGender);
    }

    public String getPrefGender() {

        if (getString(PREF_GENDER) != null){
            return getString(PREF_GENDER);
        } else return GENDER_BOTH;

    }

    public void setPrefStatus(String prefStatus) {
        put(PREF_STATUS, prefStatus);
    }

    public String getPrefStatus() {

        if (getString(PREF_STATUS) != null){
            return getString(PREF_STATUS);
        } else return STATUS_ALL;

    }

    public void setPrefMinAge(int minAgeDate) {
        put(PREF_MIN_AGE, minAgeDate);
    }

    public int getPrefMinAge() {
        if (getInt(PREF_MIN_AGE) > 17){
            return getInt(PREF_MIN_AGE);
        } else {
            return Config.MinimumAgeToRegister;
        }
    }


    public void setPrefMaxAge(int maxAgeDate) {
        put(PREF_MAX_AGE, maxAgeDate);
    }

    public int getPrefMaxAge() {

        if (getInt(PREF_MAX_AGE) > 17){
            return getInt(PREF_MAX_AGE);
        } else {
            return Config.MaximumAgeToRegister;
        }

    }

    public void setPrefDistance(int distance) {
        put(PREF_DISTANCE, distance);
    }

    public int getPrefDistance() {
        if (getInt(PREF_DISTANCE) > 0){
            return getInt(PREF_DISTANCE);
        } else {
            return Config.MaxDistanceBetweenUsers;
        }
    }

    public void setColGender(String gender){
        put(COL_GENDER, gender);
    }

    public String getColGender(){
        String gender = getString(COL_GENDER);
        if(gender != null){
            return gender;
        } else {
            return "";
        }
    }


    public void setLocation(String location) {
        put(COL_LOCATION, location);
    }

    public String getLocation(){

        if (this.getString(COL_LOCATION) != null){
            return this.getString(COL_LOCATION);
        } else {
            return "";
        }

    }

    public void setCity(String city) {
        put(COL_CITY, city);
    }

    public String getCity(){

        if (this.getString(COL_CITY) != null){
            return this.getString(COL_CITY);
        } else {
            return "";
        }

    }

    public ParseGeoPoint getGeoPoint(){
        return (ParseGeoPoint)get(COL_GEO_POINT);
    }

    public String getGeoPointString(){
        ParseGeoPoint geoPoint = (ParseGeoPoint)get(COL_GEO_POINT);
        if(geoPoint == null)
            return "";
        else {

            return String.format(Locale.getDefault(), "%.4f : %.4f", geoPoint.getLongitude(), geoPoint.getLatitude());
        }
    }

    public void setGeoPoint(double lat, double lon) {
        put(COL_GEO_POINT, new ParseGeoPoint(lat,lon));
    }

    public void setGeoPoint(Location location){
        put(COL_GEO_POINT, new ParseGeoPoint(location.getLatitude(),location.getLongitude()));
    }

    public void setGeoPoint(ParseGeoPoint geoPoint){
        put(COL_GEO_POINT, geoPoint);
    }

    ////////////////////////////////////////////////////////////////////


    public int getColPopularity() {
        return getInt(COL_POPULARITY);
    }

    public void setPopularity(int popularity) {
        put(COL_POPULARITY, popularity);
    }

    public void addPopularity(int popularity) {
        increment(COL_POPULARITY, popularity);
    }

    public boolean isInvisible() {
        return getBoolean(VIP_IS_INVISIBLE);
    }

    public void setInvisible(boolean invisible) {
        put(VIP_IS_INVISIBLE, invisible);
    }

    public Date getAdsDisabled() {

        if (getDate(VIP_ADS_DISABLED) != null){

            return getDate(VIP_ADS_DISABLED);
        } else {
            return null;
        }


    }

    public Date getVipMoveToTop() {

        if (getDate(VIP_MOVE_TO_TOP) != null){

            return getDate(VIP_MOVE_TO_TOP);
        } else {
            return null;
        }
    }

    public Date getVipAppearMore() {

        if (getDate(VIP_EXTRA_SHOWS) != null){

            return getDate(VIP_EXTRA_SHOWS);
        } else {
            return null;
        }
    }

    public boolean getVipInvisibleMode() {

        return getBoolean(VIP_INVISIBLE_MODE);
    }

    public Date getVip3xPopular() {

        if (getDate(VIP_3X_POPULAR) != null){

            return getDate(VIP_3X_POPULAR);
        } else {
            return null;
        }


    }

    public Date getVipMoreVisits() {

        if (getDate(VIP_MORE_VISITS) != null){

            return getDate(VIP_MORE_VISITS);
        } else {
            return null;
        }


    }
    public Date getVipShowOnline() {

        if (getDate(VIP_SHOW_ONLINE) != null){
            return getDate(VIP_SHOW_ONLINE);
        } else {
            return null;
        }
    }

    public Date getPremium() {

        if (getDate(PREMIUM) != null){
            return getDate(PREMIUM);
        } else {
            return null;
        }
    }

    public boolean isPremium() {

        if (getDate(PREMIUM) != null && QuickHelp.validatePurchaseDate(getDate(PREMIUM))){
            return true;
        } else return this.getBoolean(PREMIUM_LIFETIME);
    }

    public boolean isPremiumLifeTime() {

        return this.getBoolean(PREMIUM_LIFETIME);
    }

    public int getCredits() {
        return getInt(CREDIT);
    }

    public int getTokens() {
        return getInt(TOKENS);
    }

    public void setAdsDisabled(Date adsActive) {
        put(VIP_ADS_DISABLED, adsActive);
    }

    public void setVipMoveToTop(Date moveToTop) {
        put(VIP_MOVE_TO_TOP, moveToTop);
    }

    public void setVipInvisibleMode(boolean invisibleMode) {
        put(VIP_INVISIBLE_MODE, invisibleMode);
    }

    public void setVip3xPopular(Date vip3xPopular) {
        put(VIP_3X_POPULAR, vip3xPopular);
    }

    public void setVipExtraShow(Date appearMore) {
        put(VIP_EXTRA_SHOWS, appearMore);
    }

    public void setVipMoreVisits(Date moreVisits) {
        put(VIP_MORE_VISITS, moreVisits);
    }

    public void setVipShowOnline(Date showOnline) {
        put(VIP_SHOW_ONLINE, showOnline);
    }

    public void setPremium(Date premium) {
        put(PREMIUM, premium);
    }

    public void setPremiumLifetime(boolean premiumLifetime) {
        put(PREMIUM_LIFETIME, premiumLifetime);
    }

    public void addCredit(int credit) {
        increment(CREDIT, credit);
    }

    public void removeCredit(int credit) {
        increment(CREDIT, - credit);
    }

    public void removeTokens(int tokens) {
        increment(TOKENS, -tokens);
    }


    public String getColFullName(){

        String Fullname;
        try {
            Fullname = this.fetchIfNeeded().getString(COL_FULL_NAME);
            if(Fullname != null){
                return Fullname;
            } else {
                return "";
            }

        } catch (ParseException e) {
            Log.v(LOG_TAG, e.toString());
            e.printStackTrace();
        }

        return "";
    }

    public String getColFirstName(){

        String firstname;
        try {
            firstname = this.fetchIfNeeded().getString(COL_FIRST_NAME);
            if(firstname != null){
                return firstname;
            } else {
                return "";
            }

        } catch (ParseException e) {
            Log.v(LOG_TAG, e.toString());
            e.printStackTrace();
        }

        return "";
    }

    public Date getLastOnline(){

        Date date;
        try {
            date = this.fetchIfNeeded().getDate(COL_LAST_ONLINE);
            return date;

        } catch (ParseException e) {
            Log.v(LOG_TAG, e.toString());
            e.printStackTrace();
        }

        return null;
    }

    public void setLastOnline(Date lastOnline){
        put(COL_LAST_ONLINE, lastOnline);
    }

    public int getAge(){

        if (this.getInt(COL_AGE) == 0){
            return 18;
        } else {
            return this.getInt(COL_AGE);
        }

    }
    public void setAge(int age){
        put(COL_AGE, age);
    }

    public void setColFullName( String fullName){
        put(COL_FULL_NAME, fullName);
    }

    public void setColFirstName( String firstName){
        put(COL_FIRST_NAME, firstName);
    }

    public String getEmail(){

        String email;
        try {
            email = this.fetchIfNeeded().getString(COL_EMAIL);
            if(email != null){
                return email;
            } else {
                return "";
            }

        } catch (ParseException e) {
            ///Log.v(LOG_TAG, e.toString());
            //e.printStackTrace();
        }

        return "";
    }

    public void setPasswordEnabled(boolean hasPassword){
        put(HAS_PASSWORD, hasPassword);
    }

    public boolean isPasswordEnabled(){
        return this.getBoolean(HAS_PASSWORD);
    }


    public void setEmail(String email){
        put(COL_EMAIL, email);
    }

    public void setColBirthdate(Date birthdate){
        put(COL_BIRTHDATE, birthdate);
    }

    public void setColBirthDay(Date birthDay){
        put(COL_BIRTHDATE, birthDay);
    }

    public void setColBirthMonth(Date birthMonth){
        put(COL_BIRTHDATE, birthMonth);
    }

    public void setColBirthYear(Date birthYear){
        put(COL_BIRTHDATE, birthYear);
    }


    public Date getBirthDate(){

        return this.getDate(COL_BIRTHDATE);
    }

    public void setBio(String bio){
        put(COL_BIO, bio);
    }

    public String getVerified(){
        if(TextUtils.equals(this.getString(COL_EMAIL_VERIFIED), "true")){
            return "true";
        } else if(TextUtils.equals(this.getString(COL_EMAIL_VERIFIED), "false")){
            return "false";
        } else {
            return "false";
        }
    }

    public boolean isVerified() {
        return getBoolean(COL_EMAIL_VERIFIED);
    }

    public String stringVerified() {
        String Verified = getString(COL_EMAIL_VERIFIED);

        if (Verified != null && TextUtils.equals(Verified, "true")){

            return "true";

        } else if (Verified != null && TextUtils.equals(Verified, "false")){

            return "false";

        } else return "null";

    }


    public void setLiveSteamsCount(int count){
        put(LIVE_STREAMS_COUNT, count);
    }

    public void addLiveSteamsCount(int count){
        increment(LIVE_STREAMS_COUNT, count);
    }

    public int getLiveSteamsCount(){
        return  getInt(LIVE_STREAMS_COUNT);
    }

    public void setLiveStreamsViewersCount(int count){
        increment(LIVE_STREAMS_VIEWERS_COUNT, count);
    }

    public int getLiveStreamsViewersCount(){
        return  getInt(LIVE_STREAMS_VIEWERS_COUNT);
    }

    public void setLiveStreamsViewersUid(List<Integer> uid){
        addAllUnique(LIVE_STREAMS_VIEWERS, uid);
    }

    public List<Integer> getLiveStreamsViewersUid(){
        return  getList(LIVE_STREAMS_VIEWERS);
    }

    public void setFollowersCount(int count){
        put(LIVE_FOLLOWERS_COUNT, count);
    }

    public int getFollowersCount(){
        return  getInt(LIVE_FOLLOWERS_COUNT);
    }

    public void setAvatarPhotoPosition(int position) {
        put(COL_PHOTO_POSITION, position);
    }

    public int getAvatarPhotoPosition() {
        if(this.getInt(COL_PHOTO_POSITION) == -1){
            return 0;
        } else {
            return this.getInt(COL_PHOTO_POSITION);
        }
    }


    public void setProfilePhotos(ArrayList<ParseFile> files) {
        addAllUnique(COL_PHOTOS, files);
    }

    public List<ParseFile> getProfilePhotos() {

        List<ParseFile> parseFiles = new ArrayList<>();

        if (this.getList(COL_PHOTOS) != null && Objects.requireNonNull(this.getList(COL_PHOTOS)).size() > 0){

            return this.getList(COL_PHOTOS);

        } else {

            return parseFiles;
        }
    }

    public void removePhoto(ParseFile file) {

        ArrayList<ParseFile> parseFile = new ArrayList<>();
        parseFile.add(file);

        removeAll(COL_PHOTOS, parseFile);
    }

    public void setBlockedUser(List<User> blockedUser) {
        addAllUnique(BLOCKED_USERS, blockedUser);
    }

    public void setBlockedUser(User user) {

        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(user);

        addAllUnique(BLOCKED_USERS, userArrayList);
    }

    public void removeBlockedUser(List<User> blockedUser) {
        removeAll(BLOCKED_USERS, blockedUser);
    }

    public List<User> getBlockedUsers() {
        return getList(BLOCKED_USERS);
    }

    // Settings

    public void setInstallation(ParseInstallation installation){
        put(COL_INSTALLATION, installation);
    }

    public void deleteInstallation(){
        remove(COL_INSTALLATION);
    }


    public ParseInstallation getInstallation(){

        ParseInstallation Installation;
        try {
            Installation = (ParseInstallation) fetchIfNeeded().get("installation");
            if(Installation != null){
                return (ParseInstallation)get("installation");
            } else {
                return null ;
            }

        } catch (ParseException ignored) {

        }

        return null;
    }


    //////////////////////////////////////////


    public void setPrivacyDistanceEnabled(boolean showDistance){
        put(PRIVACY_SHOW_DISTANCE, showDistance);
    }

    public boolean getPrivacyDistanceEnabled(){ // Return False if true and true if false;
        return !this.getBoolean(PRIVACY_SHOW_DISTANCE);
    }

    public void setPrivacyOnlineStatusEnabled(boolean showDistance){
        put(PRIVACY_SHOW_ONLINE_STATUS, showDistance);
    }

    public boolean getPrivacyOnlineStatusEnabled(){
        return !this.getBoolean(PRIVACY_SHOW_ONLINE_STATUS);
    }

    public void setPrivacyEnabledAlmostInvisible(boolean enableAlmostInvisible){
        put(PRIVACY_ALMOST_INVISIBLE, enableAlmostInvisible);
    }

    public boolean getPrivacyAlmostInvisible(){
        return this.getBoolean(PRIVACY_ALMOST_INVISIBLE);
    }

    public void setPrivacyEnabledCloakedInvisibility(boolean enableAlmostInvisible){
        put(PRIVACY_CLOAKED_INVISIBILITY, enableAlmostInvisible);
    }

    public boolean getPrivacyCloakedInvisibility(){
        return this.getBoolean(PRIVACY_CLOAKED_INVISIBILITY);
    }

    public void setPrivacyHidePremiumStatus(boolean enableAlmostInvisible){
        put(PRIVACY_HIDE_PREMIUM_STATUS, enableAlmostInvisible);
    }

    public boolean getPrivacyHidePremiumStatus(){
        return this.getBoolean(PRIVACY_HIDE_PREMIUM_STATUS);
    }

    ////////////////////////////// Push Notifications  //////////////////////////////

    public void setPushNotificationsMassagesDisabled(boolean notificationsMassagesDisabled){
        put(PUSH_NOTIFICATIONS_MASSAGES, notificationsMassagesDisabled);
    }

    public boolean getPushNotificationsMassagesEnabled(){
        return !this.getBoolean(PUSH_NOTIFICATIONS_MASSAGES);
    }

    public void setPushNotificationsMatchesDisabled(boolean notificationsMatchesDisabled){
        put(PUSH_NOTIFICATIONS_MATCHES, notificationsMatchesDisabled);
    }

    public boolean getPushNotificationsMatchesEnabled(){
        return !this.getBoolean(PUSH_NOTIFICATIONS_MATCHES);
    }

    public void setPushNotificationsLikedYouDisabled(boolean notificationsLikedYouDisabled){
        put(PUSH_NOTIFICATIONS_LIKED_YOU, notificationsLikedYouDisabled);
    }

    public boolean getPushNotificationsLikedYouEnabled(){
        return !this.getBoolean(PUSH_NOTIFICATIONS_LIKED_YOU);
    }

    public void setPushNotificationsProfileVisitorDisabled(boolean notificationsProfileVisitorDisabled){
        put(PUSH_NOTIFICATIONS_PROFILE_VISITOR, notificationsProfileVisitorDisabled);
    }

    public boolean getPushNotificationsProfileVisitorEnabled(){
        return !this.getBoolean(PUSH_NOTIFICATIONS_PROFILE_VISITOR);
    }

    public void setPushNotificationsFavoritedYouDisabled(boolean notificationsProfileVisitorDisabled){
        put(PUSH_NOTIFICATIONS_FAVORITED_YOU, notificationsProfileVisitorDisabled);
    }

    public boolean getPushNotificationsFavoritedYouEnabled(){
        return !this.getBoolean(PUSH_NOTIFICATIONS_FAVORITED_YOU);
    }

    public void setPushNotificationsLiveDisabled(boolean notificationsProfileVisitorDisabled){
        put(PUSH_NOTIFICATIONS_DATOO_LIVE, notificationsProfileVisitorDisabled);
    }

    public boolean getPushNotificationsLiveEnabled(){
        return !this.getBoolean(PUSH_NOTIFICATIONS_DATOO_LIVE);
    }

    public boolean isUserBlocked(){
        return this.getBoolean(USER_BLOCKED_STATUS);
    }

    //////////////////////////////////////////

    public static User getUser(){

        User user = (User) ParseUser.getCurrentUser();

        if (user != null){

            user.fetchIfNeededInBackground();
        }

        return  user;
    }


    ////////////////////////////////////////////////////////////////////

    public static ParseQuery<User> getUserQuery(){
        return ParseQuery.getQuery(User.class);
    }
}
