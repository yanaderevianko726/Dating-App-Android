package com.sugardaddy.citascasualesconocergentenuevacercadeti.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils.AngopapoLoginActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ui.login.ParseLoginFragmentBase;
import com.parse.ui.login.ParseOnLoadingListener;

import java.util.Objects;


/**
 * Fragment for the login help screen for resetting the user's password.
 */
public class ForgotFragment extends ParseLoginFragmentBase implements OnClickListener {

    public interface ParseOnLoginHelpSuccessListener {
        void onLoginHelpSuccess();
    }

    private TextView instructionsTextView;
    private EditText emailField;
    private Button submitButton;
    private boolean emailSent = false;
    private ParseOnLoginHelpSuccessListener onLoginHelpSuccessListener;

    private static final String LOG_TAG = "ForgotFragment";

    public static ForgotFragment newInstance(Bundle configOptions) {
        ForgotFragment loginHelpFragment = new ForgotFragment();
        loginHelpFragment.setArguments(configOptions);
        return loginHelpFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_forgot, parent, false);

        instructionsTextView = v.findViewById(R.id.login_help_instructions);
        emailField = v.findViewById(R.id.login_emailLoginEditText);
        submitButton = v.findViewById(R.id.signIn);
        Toolbar mToolbar = v.findViewById(R.id.toolbar);

        setHasOptionsMenu(true);

        if (getActivity() != null){

            ((AngopapoLoginActivity)getActivity()).setSupportActionBar(mToolbar);

            Objects.requireNonNull(((AngopapoLoginActivity) getActivity()).getSupportActionBar()).setTitle("");
            Objects.requireNonNull(((AngopapoLoginActivity) getActivity()).getSupportActionBar()).setElevation(3.0f);
            Objects.requireNonNull(((AngopapoLoginActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setBackgroundResource(R.color.transparent);

        mToolbar.setOnMenuItemClickListener(menuItem -> false);

        submitButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }

        if (activity instanceof ParseOnLoginHelpSuccessListener) {
            onLoginHelpSuccessListener = (ParseOnLoginHelpSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoginHelpSuccessListener");
        }
    }

    @Override
    public void onClick(View v) {
        if (!emailSent) {
            String email = emailField.getText().toString();
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                emailField.setError(getString(R.string.invalid_email));
            } else {
                loadingStart();
                ParseUser.requestPasswordResetInBackground(email,
                        e -> {
                            if (isActivityDestroyed()) {
                                return;
                            }

                            loadingFinish();
                            if (e == null) {
                                instructionsTextView.setText(R.string.com_parse_ui_login_help_email_sent);
                                emailField.setVisibility(View.INVISIBLE);
                                submitButton.setText(R.string.com_parse_ui_login_help_login_again_button_label);
                                emailSent = true;
                            } else {
                                debugLog(getString(R.string.com_parse_ui_login_warning_password_reset_failed) +
                                        e.toString());
                                if (e.getCode() == ParseException.INVALID_EMAIL_ADDRESS ||
                                        e.getCode() == ParseException.EMAIL_NOT_FOUND) {
                                    showToast(R.string.com_parse_ui_invalid_email_toast);
                                } else {
                                    showToast(R.string.com_parse_ui_login_help_submit_failed_unknown);
                                }
                            }
                        });
            }
        } else {
            onLoginHelpSuccessListener.onLoginHelpSuccess();
        }
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
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

