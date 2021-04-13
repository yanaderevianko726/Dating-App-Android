package com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.parse.Parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * Configurations for the AngopapoLoginActivity.
 */
public class AngopapoLoginConfig {
    private static final String APP_LOGO = "AngopapoLogin.APP_LOGO";
    private static final String PARSE_LOGIN_ENABLED = "AngopapoLogin.PARSE_LOGIN_ENABLED";
    private static final String PARSE_LOGIN_BUTTON_TEXT = "AngopapoLogin.PARSE_LOGIN_BUTTON_TEXT";
    private static final String PARSE_SIGNUP_BUTTON_TEXT = "AngopapoLogin.PARSE_SIGNUP_BUTTON_TEXT";
    private static final String PARSE_LOGIN_HELP_TEXT = "AngopapoLogin.PARSE_LOGIN_HELP_TEXT";
    private static final String PARSE_LOGIN_INVALID_CREDENTIALS_TOAST_TEXT = "AngopapoLogin.PARSE_LOGIN_INVALID_CREDENTIALS_TEXT";
    private static final String PARSE_LOGIN_EMAIL_AS_USERNAME = "AngopapoLogin.PARSE_LOGIN_EMAIL_AS_USERNAME";
    private static final String PARSE_SIGNUP_MIN_PASSWORD_LENGTH = "AngopapoLogin.PARSE_SIGNUP_MIN_PASSWORD_LENGTH";
    private static final String PARSE_SIGNUP_SUBMIT_BUTTON_TEXT = "AngopapoLogin.PARSE_SIGNUP_SUBMIT_BUTTON_TEXT";
    private static final String PARSE_SIGNUP_NAME_FIELD_ENABLED = "AngopapoLogin.PARSE_SIGNUP_NAME_FIELD_ENABLED";
    private static final String FACEBOOK_LOGIN_ENABLED = "AngopapoLogin.FACEBOOK_LOGIN_ENABLED";
    private static final String FACEBOOK_LOGIN_BUTTON_TEXT = "AngopapoLogin.FACEBOOK_LOGIN_BUTTON_TEXT";
    private static final String FACEBOOK_LOGIN_PERMISSIONS = "AngopapoLogin.FACEBOOK_LOGIN_PERMISSIONS";
    private static final String TWITTER_LOGIN_ENABLED = "AngopapoLogin.TWITTER_LOGIN_ENABLED";
    private static final String TWITTER_LOGIN_BUTTON_TEXT = "AngopapoLogin.TWITTER_LOGIN_BUTTON_TEXT";

    // For internally serializing to/from string array (the public analog above is for resource from activity meta-data).
    private static final String FACEBOOK_LOGIN_PERMISSIONS_STRING_ARRAY = "AngopapoLogin.FACEBOOK_LOGIN_PERMISSIONS_STRING_ARRAY";

    private static final String LOG_TAG = "com.parse.ui.AngopapoLoginConfig";

    // Use boxed types so that we can differentiate between a setting not set,
    // versus its default value.  This is useful for merging options set from code
    // with options set by activity metadata.
    private Integer appLogo;
    private Boolean parseLoginEnabled;
    private CharSequence parseLoginButtonText;
    private CharSequence parseSignupButtonText;
    private CharSequence parseLoginHelpText;
    private CharSequence parseLoginInvalidCredentialsToastText;
    private Boolean parseLoginEmailAsUsername;
    private Integer parseSignupMinPasswordLength;
    private CharSequence parseSignupSubmitButtonText;
    private Boolean parseSignupNameFieldEnabled;

    private Boolean facebookLoginEnabled;
    private CharSequence facebookLoginButtonText;
    private Collection<String> facebookLoginPermissions;

    private Boolean twitterLoginEnabled;
    private CharSequence twitterLoginButtonText;

    void setAppLogo(Integer appLogo) {
        this.appLogo = appLogo;
    }

    public boolean isParseLoginEnabled() {
        if (parseLoginEnabled != null) {
            return parseLoginEnabled;
        } else {
            return false;
        }
    }

    void setParseLoginEnabled(boolean parseLoginEnabled) {
        this.parseLoginEnabled = parseLoginEnabled;
    }

    void setParseLoginButtonText(CharSequence parseLoginButtonText) {
        this.parseLoginButtonText = parseLoginButtonText;
    }

    void setParseSignupButtonText(CharSequence parseSignupButtonText) {
        this.parseSignupButtonText = parseSignupButtonText;
    }

    void setParseLoginHelpText(CharSequence parseLoginHelpText) {
        this.parseLoginHelpText = parseLoginHelpText;
    }

    void setParseLoginInvalidCredentialsToastText(
            CharSequence parseLoginInvalidCredentialsToastText) {
        this.parseLoginInvalidCredentialsToastText = parseLoginInvalidCredentialsToastText;
    }

    void setParseLoginEmailAsUsername(boolean parseLoginEmailAsUsername) {
        this.parseLoginEmailAsUsername = parseLoginEmailAsUsername;
    }

    public Integer getParseSignupMinPasswordLength() {
        return parseSignupMinPasswordLength;
    }

    void setParseSignupMinPasswordLength(Integer parseSignupMinPasswordLength) {
        this.parseSignupMinPasswordLength = parseSignupMinPasswordLength;
    }

    void setParseSignupSubmitButtonText(
            CharSequence parseSignupSubmitButtonText) {
        this.parseSignupSubmitButtonText = parseSignupSubmitButtonText;
    }

    public Boolean isParseSignupNameFieldEnabled() {
        if (parseSignupNameFieldEnabled != null) {
            return parseSignupNameFieldEnabled;
        } else {
            return true;
        }
    }

    void setParseSignupNameFieldEnabled(Boolean parseSignupNameFieldEnabled) {
        this.parseSignupNameFieldEnabled = parseSignupNameFieldEnabled;
    }

    void setFacebookLoginEnabled(boolean facebookLoginEnabled) {
        this.facebookLoginEnabled = facebookLoginEnabled;
    }

    void setFacebookLoginButtonText(CharSequence facebookLoginButtonText) {
        this.facebookLoginButtonText = facebookLoginButtonText;
    }

    void setFacebookLoginPermissions(Collection<String> permissions) {
        if (permissions != null) {
            facebookLoginPermissions = new ArrayList<>(permissions.size());
            facebookLoginPermissions.addAll(permissions);
        }
    }

    void setTwitterLoginEnabled(boolean twitterLoginEnabled) {
        this.twitterLoginEnabled = twitterLoginEnabled;
    }

    void setTwitterLoginButtonText(CharSequence twitterLoginButtonText) {
        this.twitterLoginButtonText = twitterLoginButtonText;
    }

    /**
     * Converts this object into a Bundle object. For options that are not
     * explicitly set, we do not include them in the Bundle so that this bundle
     * can be merged with any default configurations and override only those keys
     * that are explicitly set.
     *
     * @return The Bundle object containing configurations.
     */
    Bundle toBundle() {
        Bundle bundle = new Bundle();

        if (appLogo != null) {
            bundle.putInt(APP_LOGO, appLogo);
        }

        if (parseLoginEnabled != null) {
            bundle.putBoolean(PARSE_LOGIN_ENABLED, parseLoginEnabled);
        }
        if (parseLoginButtonText != null) {
            bundle.putCharSequence(PARSE_LOGIN_BUTTON_TEXT, parseLoginButtonText);
        }
        if (parseSignupButtonText != null) {
            bundle.putCharSequence(PARSE_SIGNUP_BUTTON_TEXT, parseSignupButtonText);
        }
        if (parseLoginHelpText != null) {
            bundle.putCharSequence(PARSE_LOGIN_HELP_TEXT, parseLoginHelpText);
        }
        if (parseLoginInvalidCredentialsToastText != null) {
            bundle.putCharSequence(PARSE_LOGIN_INVALID_CREDENTIALS_TOAST_TEXT,
                    parseLoginInvalidCredentialsToastText);
        }
        if (parseLoginEmailAsUsername != null) {
            bundle.putBoolean(PARSE_LOGIN_EMAIL_AS_USERNAME,
                    parseLoginEmailAsUsername);
        }
        if (parseSignupMinPasswordLength != null) {
            bundle.putInt(PARSE_SIGNUP_MIN_PASSWORD_LENGTH,
                    parseSignupMinPasswordLength);
        }
        if (parseSignupSubmitButtonText != null) {
            bundle.putCharSequence(PARSE_SIGNUP_SUBMIT_BUTTON_TEXT,
                    parseSignupSubmitButtonText);
        }
        if (parseSignupNameFieldEnabled != null) {
            bundle.putBoolean(PARSE_SIGNUP_NAME_FIELD_ENABLED, parseSignupNameFieldEnabled);
        }

        if (facebookLoginEnabled != null) {
            bundle.putBoolean(FACEBOOK_LOGIN_ENABLED, facebookLoginEnabled);
        }
        if (facebookLoginButtonText != null) {
            bundle.putCharSequence(FACEBOOK_LOGIN_BUTTON_TEXT,
                    facebookLoginButtonText);
        }
        if (facebookLoginPermissions != null) {
            bundle.putStringArray(FACEBOOK_LOGIN_PERMISSIONS_STRING_ARRAY,
                    facebookLoginPermissions.toArray(new String[0]));
        }

        if (twitterLoginEnabled != null) {
            bundle.putBoolean(TWITTER_LOGIN_ENABLED, twitterLoginEnabled);
        }
        if (twitterLoginButtonText != null) {
            bundle.putCharSequence(TWITTER_LOGIN_BUTTON_TEXT, twitterLoginButtonText);
        }

        return bundle;
    }

    /**
     * Constructs a AngopapoLoginConfig object from a bundle. Unrecognized keys are
     * ignored.
     * <p>
     * This can be used to pass an AngopapoLoginConfig object between activities, or
     * to read settings from an activity's meta-data in Manefest.xml.
     *
     * @param bundle
     *     The Bundle representation of the AngopapoLoginConfig object.
     * @param context
     *     The context for resolving resource IDs.
     * @return The AngopapoLoginConfig instance.
     */
    @SuppressLint("LongLogTag")
    public static AngopapoLoginConfig fromBundle(Bundle bundle, Context context) {
        AngopapoLoginConfig config = new AngopapoLoginConfig();
        Set<String> keys = bundle.keySet();

        if (keys.contains(APP_LOGO)) {
            config.setAppLogo(bundle.getInt(APP_LOGO));
        }

        if (keys.contains(PARSE_LOGIN_ENABLED)) {
            config.setParseLoginEnabled(bundle.getBoolean(PARSE_LOGIN_ENABLED));
        }
        if (keys.contains(PARSE_LOGIN_BUTTON_TEXT)) {
            config.setParseLoginButtonText(bundle.getCharSequence(PARSE_LOGIN_BUTTON_TEXT));
        }
        if (keys.contains(PARSE_SIGNUP_BUTTON_TEXT)) {
            config.setParseSignupButtonText(bundle.getCharSequence(PARSE_SIGNUP_BUTTON_TEXT));
        }
        if (keys.contains(PARSE_LOGIN_HELP_TEXT)) {
            config.setParseLoginHelpText(bundle.getCharSequence(PARSE_LOGIN_HELP_TEXT));
        }
        if (keys.contains(PARSE_LOGIN_INVALID_CREDENTIALS_TOAST_TEXT)) {
            config.setParseLoginInvalidCredentialsToastText(bundle
                    .getCharSequence(PARSE_LOGIN_INVALID_CREDENTIALS_TOAST_TEXT));
        }
        if (keys.contains(PARSE_LOGIN_EMAIL_AS_USERNAME)) {
            config.setParseLoginEmailAsUsername(bundle.getBoolean(PARSE_LOGIN_EMAIL_AS_USERNAME));
        }
        if (keys.contains(PARSE_SIGNUP_MIN_PASSWORD_LENGTH)) {
            config.setParseSignupMinPasswordLength(bundle.getInt(PARSE_SIGNUP_MIN_PASSWORD_LENGTH));
        }
        if (keys.contains(PARSE_SIGNUP_SUBMIT_BUTTON_TEXT)) {
            config.setParseSignupSubmitButtonText(bundle.getCharSequence(PARSE_SIGNUP_SUBMIT_BUTTON_TEXT));
        }
        if (keys.contains(PARSE_SIGNUP_NAME_FIELD_ENABLED)) {
            config.setParseSignupNameFieldEnabled(bundle.getBoolean(PARSE_SIGNUP_NAME_FIELD_ENABLED));
        }

        if (keys.contains(FACEBOOK_LOGIN_ENABLED)) {
            config.setFacebookLoginEnabled(bundle.getBoolean(FACEBOOK_LOGIN_ENABLED));
        }
        if (keys.contains(FACEBOOK_LOGIN_BUTTON_TEXT)) {
            config.setFacebookLoginButtonText(bundle.getCharSequence(FACEBOOK_LOGIN_BUTTON_TEXT));
        }
        if (keys.contains(FACEBOOK_LOGIN_PERMISSIONS) &&
                bundle.getInt(FACEBOOK_LOGIN_PERMISSIONS) != 0) {
            // Only for converting from activity meta-data.
            try {
                config.setFacebookLoginPermissions(stringArrayToCollection(context
                        .getResources().getStringArray(
                                bundle.getInt(FACEBOOK_LOGIN_PERMISSIONS))));
            } catch (NotFoundException e) {
                if (Parse.getLogLevel() <= Parse.LOG_LEVEL_ERROR) {
                    Log.w(LOG_TAG, "Facebook permission string array resource not found");
                }
            }
        } else if (keys.contains(FACEBOOK_LOGIN_PERMISSIONS_STRING_ARRAY)) {
            // For converting from a bundle produced by this class's toBundle()
            config.setFacebookLoginPermissions(stringArrayToCollection(bundle
                    .getStringArray(FACEBOOK_LOGIN_PERMISSIONS_STRING_ARRAY)));
        }

        if (keys.contains(TWITTER_LOGIN_ENABLED)) {
            config.setTwitterLoginEnabled(bundle.getBoolean(TWITTER_LOGIN_ENABLED));
        }
        if (keys.contains(TWITTER_LOGIN_BUTTON_TEXT)) {
            config.setTwitterLoginButtonText(bundle
                    .getCharSequence(TWITTER_LOGIN_BUTTON_TEXT));
        }

        return config;
    }



    private static Collection<String> stringArrayToCollection(String[] array) {
        if (array == null) {
            return null;
        }
        return Arrays.asList(array);
    }
}
