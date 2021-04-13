package com.sugardaddy.citascasualesconocergentenuevacercadeti.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.DispatchActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils.AngopapoLoginActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils.AngopapoLoginConfig;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.login.ParseLoginFragmentBase;
import com.parse.ui.login.ParseOnLoadingListener;

import java.util.Objects;

/**
 * Fragment for the user login screen.
 */
public class LoginFragment extends ParseLoginFragmentBase {

    public interface ParseLoginFragmentListener {
        void onSignUpClicked();

        void onLoginHelpClicked();

        void onLoginSuccess();
    }

    private static final String LOG_TAG = "LoginFragment";

    private View parseLogin;
    private EditText usernameField;
    private EditText passwordField;
    private TextView parseForgotButton;
    private Button parseLoginButton;
    private ParseLoginFragmentListener loginFragmentListener;

    private AngopapoLoginConfig config;

    public static LoginFragment newInstance(Bundle configOptions) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(configOptions);
        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            config = AngopapoLoginConfig.fromBundle(getArguments(), getActivity());
        }

        View v = inflater.inflate(R.layout.fragment_login, parent, false);

        parseLogin = v.findViewById(R.id.parse_login);
        usernameField =  v.findViewById(R.id.login_emailLoginEditText);
        passwordField =  v.findViewById(R.id.password);
        parseForgotButton = v.findViewById(R.id.forgotPassword);
        parseLoginButton = v.findViewById(R.id.signIn);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);

        setHasOptionsMenu(true);

        ((AngopapoLoginActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AngopapoLoginActivity) getActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AngopapoLoginActivity) getActivity()).getSupportActionBar()).setElevation(3.0f);
        Objects.requireNonNull(((AngopapoLoginActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mToolbar.setBackgroundResource(R.color.transparent);

        mToolbar.setOnMenuItemClickListener(menuItem -> false);


        if (allowParseLoginAndSignup()) {

            setUpParseLoginAndSignup();
        }


        return v;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();
        if (activity instanceof ParseLoginFragmentListener) {
            loginFragmentListener = (ParseLoginFragmentListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseLoginFragmentListener");
        }

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    private void setUpParseLoginAndSignup() {
        parseLogin.setVisibility(View.VISIBLE);

        parseLoginButton.setOnClickListener(v -> {

            if (isInternetAvailable()) {

            String username = usernameField.getText().toString().toLowerCase().trim();
            String password = passwordField.getText().toString().trim();


            if (username.contains("@")) {

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {

                    usernameField.setError(getString(R.string.invalid_email));

                } else if (password.length() == 0) {

                    passwordField.setError(getString(R.string.com_parse_ui_no_password_toast));
                } else {
                    loadingStart(true);

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", username);
                    query.getFirstInBackground((user,e) -> {
                        if (user == null ) {

                            Log.d("score", "The getFirst request failed.");
                            loadingFinish();

                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                                usernameField.setError(getString(R.string.login_verify));

                            } else if (e.getCode() == ParseException.CONNECTION_FAILED){


                                Snackbar.make(parseLogin, R.string.login_email_fail_internet,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                }).setActionTextColor(Color.WHITE).show();

                            } else if (e.getCode() == ParseException.TIMEOUT){


                                Snackbar.make(parseLogin, R.string.login_email_fail_internet,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                }).setActionTextColor(Color.WHITE).show();

                            } else {

                                Snackbar.make(parseLogin, R.string.login_email_fail,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                }).setActionTextColor(Color.WHITE).show();
                            }


                        } else {

                            String actualUsername = user.getUsername();

                            ParseUser.logInInBackground(actualUsername, password, (user1, e1) -> {
                                if (isActivityDestroyed()) {
                                    return;
                                }

                                if (user1 != null) {
                                    loadingFinish();
                                    loginSuccess();

                                } else {

                                    loadingFinish();

                                        if (e1 != null) {
                                            debugLog(getString(R.string.com_parse_ui_login_warning_parse_login_failed) + e1.toString());

                                            passwordField.selectAll();
                                            passwordField.requestFocus();

                                            if (e1.getCode() == ParseException.OBJECT_NOT_FOUND){

                                                passwordField.setError(getString(R.string.login_verify_password));

                                            } else if (e1.getCode() == ParseException.CONNECTION_FAILED){


                                                Snackbar.make(parseLogin, R.string.login_email_fail_internet,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                                }).setActionTextColor(Color.WHITE).show();

                                            } else if (e1.getCode() == ParseException.TIMEOUT){


                                                Snackbar.make(parseLogin, R.string.login_email_fail_internet,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                                }).setActionTextColor(Color.WHITE).show();

                                            } else {

                                                Snackbar.make(parseLogin, R.string.login_email_fail,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                                }).setActionTextColor(Color.WHITE).show();
                                            }
                                        }
                                }
                            });
                        }
                    });

                }

            } else {

                // Go to Username Login

                if (username.length() == 0) {

                    usernameField.setError(getString(R.string.email_username));

                } else if (password.length() == 0) {

                    passwordField.setError(getString(R.string.com_parse_ui_no_password_toast));

                } else {

                    loadingStart(true);
                    ParseUser.logInInBackground(username, password, (user, e) -> {
                        if (isActivityDestroyed()) {
                            return;
                        }

                        if (user != null) {
                            loadingFinish();
                            loginSuccess();

                        } else {

                            loadingFinish();

                            if (e != null) {
                                debugLog(getString(R.string.com_parse_ui_login_warning_parse_login_failed) + e.toString());

                                passwordField.selectAll();
                                passwordField.requestFocus();

                                if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                                    usernameField.setError(getString(R.string.login_verify_username_password));

                                } else if (e.getCode() == ParseException.PASSWORD_MISSING){

                                    passwordField.setError(getString(R.string.login_verify_password));

                                } else if (e.getCode() == ParseException.CONNECTION_FAILED){


                                    Snackbar.make(parseLogin, R.string.login_email_fail_internet,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                    }).setActionTextColor(Color.WHITE).show();

                                } else if (e.getCode() == ParseException.TIMEOUT){


                                    Snackbar.make(parseLogin, R.string.login_email_fail_internet,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                    }).setActionTextColor(Color.WHITE).show();

                                } else {

                                    Snackbar.make(parseLogin, R.string.login_email_fail,Snackbar.LENGTH_INDEFINITE).setAction(R.string.login_ok, v2 -> {

                                    }).setActionTextColor(Color.WHITE).show();
                                }
                            }
                        }
                    });
                }
            }

            } else showInternetConnectionLostMessage();

        });


        parseForgotButton.setOnClickListener(v -> loginFragmentListener.onLoginHelpClicked());
    }


    private boolean allowParseLoginAndSignup() {
        if (!config.isParseLoginEnabled()) {
            return false;
        }

        if (usernameField == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_username_field);
        }
        if (passwordField == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_password_field);
        }
        if (parseLoginButton == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_login_button);
        }

        if (parseForgotButton == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_login_help_button);
        }

        boolean result = (usernameField != null) && (passwordField != null) && (parseLoginButton != null) &&  (parseForgotButton != null);

        if (!result) {
            debugLog(R.string.com_parse_ui_login_warning_disabled_username_password_login);
        }
        return result;
    }

    private void loginSuccess() {

        User parseUser = User.getUser();

        QuickHelp.saveInstallation(parseUser);

        Intent mainIntent = new Intent(getActivity(), DispatchActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    private void showInternetConnectionLostMessage() {
        Snackbar.make(parseLogin, R.string.login_no_int,Snackbar.LENGTH_SHORT).show();

    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
