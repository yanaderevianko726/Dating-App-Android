package com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.ForgotFragment;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.LoginFragment;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.SignupFragment;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.parse.Parse;
import com.parse.facebook.ParseFacebookUtils;
import com.parse.ui.login.ParseOnLoadingListener;
import com.parse.ui.login.ParseOnLoginSuccessListener;


public class AngopapoLoginActivity extends AppCompatActivity implements
        LoginFragment.ParseLoginFragmentListener,
        ForgotFragment.ParseOnLoginHelpSuccessListener,
        ParseOnLoginSuccessListener, ParseOnLoadingListener {

    public static final String LOG_TAG = "AngopapoLoginActivity";

    // All login UI fragment transactions will happen within this parent layout element.
    // Change this if you are modifying this code to be hosted in your own activity.
    private final int fragmentContainer = android.R.id.content;

    private ProgressDialog progressDialog;
    private Bundle configOptions;

    // Although Activity.isDestroyed() is in API 17, we implement it anyways for older versions.
    private boolean destroyed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        // Combine options from incoming intent and the activity metadata
        configOptions = getMergedOptions();

        // Show the login form
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(fragmentContainer, LoginFragment.newInstance(configOptions))
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        destroyed = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Required for making Facebook login work
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Called when the user clicked the sign up button on the login form.
     */
    @Override
    public void onSignUpClicked() {
        // Show the signup form, but keep the transaction on the back stack
        // so that if the user clicks the back button, they are brought back
        // to the login form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer,
                SignupFragment.newInstance(configOptions));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Called when the user clicked the log in button on the login form.
     */
    @Override
    public void onLoginHelpClicked() {
        // Show the login help form for resetting the user's password.
        // Keep the transaction on the back stack so that if the user clicks
        // the back button, they are brought back to the login form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer, ForgotFragment.newInstance(configOptions));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Called when the user successfully completes the login help flow.
     */
    @Override
    public void onLoginHelpSuccess() {
        // Display the login form, which is the previous item onto the stack
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Called when the user successfully logs in or signs up.
     */
    @Override
    public void onLoginSuccess() {
        // This default implementation returns to the parent activity with
        // RESULT_OK.
        // You can change this implementation if you want a different behavior.
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Called when we are in progress retrieving some data.
     *
     * @param showSpinner
     *     Whether to show the loading dialog.
     */
    @Override
    public void onLoadingStart(boolean showSpinner) {
        if (showSpinner) {
            progressDialog = ProgressDialog.show(this, null,
                    getString(R.string.com_parse_ui_progress_dialog_text), true, false);
        }
    }

    /**
     * Called when we are finished retrieving some data.
     */
    @Override
    public void onLoadingFinish() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * @see android.app.Activity#isDestroyed()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        }
        return destroyed;
    }

    private Bundle getMergedOptions() {
        // Read activity metadata from AndroidManifest.xml
        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    this.getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            if (Parse.getLogLevel() <= Parse.LOG_LEVEL_ERROR &&
                    Log.isLoggable(LOG_TAG, Log.WARN)) {
                Log.w(LOG_TAG, e.getMessage());
            }
        }

        // The options specified in the Intent (from AngopapoLoginBuilder) will
        // override any duplicate options specified in the activity metadata
        Bundle mergedOptions = new Bundle();
        if (activityInfo != null && activityInfo.metaData != null) {
            mergedOptions.putAll(activityInfo.metaData);
        }
        if (getIntent().getExtras() != null) {
            mergedOptions.putAll(getIntent().getExtras());
        }

        return mergedOptions;
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {

            super.onBackPressed();
        }
    }
}
