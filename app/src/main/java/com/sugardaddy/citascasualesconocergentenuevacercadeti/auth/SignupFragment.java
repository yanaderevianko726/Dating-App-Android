package com.sugardaddy.citascasualesconocergentenuevacercadeti.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils.AngopapoLoginConfig;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils.AngopapoSignupActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings.WebUrlsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.SharedPrefUtil;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ui.login.ParseLoginFragmentBase;
import com.parse.ui.login.ParseOnLoadingListener;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Fragment for the user signup screen.
 */
public class SignupFragment extends ParseLoginFragmentBase {

    private EditText passwordField;
    private EditText emailField;
    private EditText nameField;
    private Button createAccountButton;
    private Toolbar mToolbar;
    private TextView mTermsText;
    private AppCompatCheckBox mCheckTerms;
    private EditText mDay1, mDay2, mMonth1, mMonth2, mYear1, mYear2, mYear3, mYear4;
    private String dateDay, dateMonth, dateYear, mBirthday;

    private AngopapoLoginConfig config;
    private int minPasswordLength;

    private static final String LOG_TAG = "SignupFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
    private static final String USER_OBJECT_NAME_FIELD = "name";

    public static SignupFragment newInstance(Bundle configOptions) {
        SignupFragment signupFragment = new SignupFragment();
        Bundle args = new Bundle(configOptions);
        signupFragment.setArguments(args);
        return signupFragment;
    }

    protected String gender = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        config = AngopapoLoginConfig.fromBundle(args, getActivity());

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
        if (config.getParseSignupMinPasswordLength() != null) {
            minPasswordLength = config.getParseSignupMinPasswordLength();
        }

        View v = inflater.inflate(R.layout.include_email, parent, false);

        passwordField = v.findViewById(R.id.password);
        emailField = v.findViewById(R.id.Registration_emailOrPhone);
        nameField = v.findViewById(R.id.Registration_name);
        //mBirthday = v.findViewById(R.id.Registration_birthday);
       // mBirthdayWrapper = v.findViewById(R.id.Registration_birthdayWrapper);
        mDay1 = v.findViewById(R.id.day_1);
        mDay2 = v.findViewById(R.id.day_2);
        mMonth1 = v.findViewById(R.id.month_1);
        mMonth2 = v.findViewById(R.id.month_2);
        mYear1 = v.findViewById(R.id.year_1);
        mYear2 = v.findViewById(R.id.year_2);
        mYear3 = v.findViewById(R.id.year_3);
        mYear4 = v.findViewById(R.id.year_4);

        mToolbar = v.findViewById(R.id.toolbar);
        mTermsText = v.findViewById(R.id.Registration_termsAndConditions);
        mCheckTerms = v.findViewById(R.id.check_terms);
        createAccountButton = v.findViewById(R.id.Registration_buttonCreateAccount);

        createAccountButton.setEnabled(false);

        setHasOptionsMenu(true);
        mToolbar.setOnMenuItemClickListener(menuItem -> false);

        initBirthdayWatcher();


        mCheckTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                createAccountButton.setEnabled(true);
            } else {
                createAccountButton.setEnabled(false);
            }
        });

        setmTermsText(mTermsText);

        ((AngopapoSignupActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AngopapoSignupActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AngopapoSignupActivity)getActivity()).getSupportActionBar().setElevation(3.0f);
        ((AngopapoSignupActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setBackgroundResource(R.color.transparent);

        gender = new SharedPrefUtil(getActivity()).getString(User.COL_GENDER, "");

        createAccountButton.setOnClickListener(v14 -> registerNow());

        return v;
    }

    public void initBirthdayWatcher(){

        QuickHelp.nextTextWatcher(mDay1, mDay2, 3);
        QuickHelp.nextTextWatcher(mDay2, mMonth1, 9);
        QuickHelp.nextTextWatcher(mMonth1, mMonth2, 1);
        QuickHelp.nextTextWatcher(mMonth2, mYear1, 9);
        QuickHelp.nextTextWatcher(mYear1, mYear2, 2);
        QuickHelp.nextTextWatcher(mYear2, mYear3, 9);
        QuickHelp.nextTextWatcher(mYear3, mYear4, 9);
        QuickHelp.nextTextWatcher(mYear4, passwordField, 9);

        QuickHelp.deleteTextWatcher(mDay1, mDay1);
        QuickHelp.deleteTextWatcher(mDay2, mDay1);
        QuickHelp.deleteTextWatcher(mMonth1, mDay2);
        QuickHelp.deleteTextWatcher(mMonth2, mMonth1);
        QuickHelp.deleteTextWatcher(mYear1, mMonth2);
        QuickHelp.deleteTextWatcher(mYear2, mYear1);
        QuickHelp.deleteTextWatcher(mYear3, mYear2);
        QuickHelp.deleteTextWatcher(mYear4, mYear3);

    }

    public void initBirthday(){

        dateDay = mDay1.getText().toString() + "" + mDay2.getText().toString();
        dateMonth = mMonth1.getText().toString() + "" + mMonth2.getText().toString();
        dateYear = mYear1.getText().toString() + "" + mYear2.getText().toString() + "" + mYear3.getText().toString() + "" + mYear4.getText().toString();
        mBirthday = dateDay + "/" + dateMonth + "/" + dateYear;

        Log.d("Signup birthday", mBirthday);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }
    }

    private void setmTermsText(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(getString(R.string.signup_by_clinging) + " ");spanTxt.append(getString(R.string.signup_terms)).append(" ");spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                QuickHelp.goToActivityWithNoClean(getActivity(), WebUrlsActivity.class, WebUrlsActivity.WEB_URL_TYPE, Config.TERMS_OF_USE);

            }
        }, spanTxt.length()-1 - getString(R.string.signup_terms).length(), spanTxt.length(), 0);

        spanTxt.setSpan(new StyleSpan(Typeface.BOLD),spanTxt.length()-1 - getString(R.string.signup_terms).length(), spanTxt.length(),0);

        //spanTxt.append(getString(R.string.signup_we_will));

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    public void registerNow(){

        if (QuickHelp.isInternetAvailable(Application.getInstance().getApplicationContext())){

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            initBirthday();

            String password = passwordField.getText().toString().trim();

            String email = emailField.getText().toString().replaceAll(" ", "").trim();

            String name = nameField.getText().toString().trim();

            if (email.length() == 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                emailField.setError(getString(R.string.com_parse_ui_no_email_toast));
            } else if (name.length() == 0 && config.isParseSignupNameFieldEnabled()) {

                nameField.setError(getString(R.string.com_parse_ui_no_name_toast));

            } else if (mDay1.getText().length() < 1){ mDay1.setError("?"); } else if (mDay2.getText().length() < 1){ mDay2.setError("?"); } else if (mMonth1.getText().length() < 1){ mMonth1.setError("?"); } else if (mMonth2.getText().length() < 1){ mMonth2.setError("?"); } else if (mYear1.getText().length() < 1){ mYear1.setError("?"); } else if (mYear2.getText().length() < 1){ mYear2.setError("?"); } else if (mYear3.getText().length() < 1){ mYear3.setError("?"); } else if (mYear4.getText().length() < 1){ mYear4.setError("?");

            } else if (!QuickHelp.isDateValid(mBirthday, QuickHelp.DATE_FORMAT)) {

                QuickHelp.showToast(getActivity(), getString(R.string.signup_user_date), true);

            } else if (Integer.parseInt(dateYear) > calendar.get(Calendar.YEAR)){

                QuickHelp.showToast(getActivity(), getString(R.string.signup_user_date), true);

            } else if (QuickHelp.getAge(mBirthday) < Config.MinimumAgeToRegister){

                QuickHelp.showToast(getActivity(), String.format(getString(R.string.mimiun_age_to_register), Config.MinimumAgeToRegister), true);

            } else if (password.length() == 0) {

                passwordField.setError(getString(R.string.com_parse_ui_no_password_toast));
            } else if (password.length() < minPasswordLength) {

                passwordField.setError(getResources().getQuantityString(R.plurals.com_parse_ui_password_too_short_toast, minPasswordLength, minPasswordLength));

            } else if (gender == null){

                QuickHelp.showToast(getActivity(), getString(R.string.gender_missing), true);
            } else {

                String[] parts = name.split(" ");

                String firstName = parts[0];

                String username = (name+firstName).toLowerCase().trim().replaceAll(" ", "");

                User user = ParseObject.create(User.class);

                user.setUid(QuickHelp.generateUId());
                user.setColBirthdate(QuickHelp.getDateFromString(mBirthday, QuickHelp.DATE_FORMAT));
                user.setAge(QuickHelp.getAge(mBirthday));
                user.setColFirstName(firstName);
                user.setColFullName(name);
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.setColGender(gender);
                user.setPopularity(0);
                user.setRole(User.ROLE_USER);
                user.setPrefMinAge(Config.MinimumAgeToRegister);
                user.setPrefMaxAge(Config.MaximumAgeToRegister);
                user.setLocationTypeNearBy(true);
                user.addCredit(Config.WelcomeCredit);
                user.setBio(Config.bio);
                user.setPasswordEnabled(true);

                loadingStart();
                user.signUpInBackground(e -> {
                    if (isActivityDestroyed()) {
                        return;
                    }

                    if (e == null) {
                        loadingFinish();
                        signupSuccessFbLink();

                    } else {
                        loadingFinish();
                        debugLog(getString(R.string.com_parse_ui_login_warning_parse_signup_failed) +
                                e.toString());
                        switch (e.getCode()) {
                            case ParseException.INVALID_EMAIL_ADDRESS:
                                showToast(R.string.com_parse_ui_invalid_email_toast);
                                emailField.setError(getString(R.string.com_parse_ui_invalid_email_toast));
                                break;
                            case ParseException.USERNAME_TAKEN:
                                showToast(R.string.com_parse_ui_username_taken_toast);
                                nameField.setError(getString(R.string.com_parse_ui_username_taken_toast));
                                break;
                            case ParseException.EMAIL_TAKEN:
                                showToast(R.string.com_parse_ui_email_taken_toast);
                                emailField.setError(getString(R.string.com_parse_ui_invalid_email_toast));
                                break;
                            default:
                                showToast(R.string.com_parse_ui_signup_failed_unknown_toast);
                        }
                    }
                });
            }

        } else {

            QuickHelp.noInternetConnect(getActivity());
        }

    }

    private void signupSuccessFbLink() {

        User parseUser = User.getUser();

        QuickHelp.saveInstallation(parseUser);

        Intent mainIntent = new Intent(getActivity(), FacebookLink.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

