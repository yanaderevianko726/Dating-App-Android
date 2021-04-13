package com.sugardaddy.citascasualesconocergentenuevacercadeti.auth;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.CountriesFetcher;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.TextWatcherAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.DispatchActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.CountriesModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.CustomProgressView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    Toolbar mToolbar;
    AppCompatEditText phoneNumberWrapper;
    AppCompatTextView btnNext;
    NestedScrollView numberPhoneLayoutSv;
    NestedScrollView layoutVerificationSv;
    ViewPager viewPager;
    TextInputEditText inputOtpWrapper;
    AppCompatTextView countryCode;
    AppCompatImageView changeNumberBtn;
    AppCompatImageView btnVerifyOtp;
    TextView textViewShowTime;
    TextView ResendBtn;
    AppCompatTextView shortDescriptionPhone;
    AppCompatTextView countryName;
    CustomProgressView progressBarLoad;
    TextView currentMobileNumber;
    ProgressDialog LoadingDialog;
    private String mCountryIso;
    private String mCountryName;
    private String mCountryDialCode;
    private PhoneNumberUtil mPhoneUtil = PhoneNumberUtil.getInstance();
    private CountriesModel mSelectedCountry;
    private CountriesFetcher.CountryList mCountries;
    // [END declare_auth]
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private String mPhoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        mToolbar = findViewById(R.id.toolbar);
        btnNext = findViewById(R.id.btn_request_sms);
        numberPhoneLayoutSv = findViewById(R.id.numberPhone_layout_sv);
        layoutVerificationSv = findViewById(R.id.layout_verification_sv);
        viewPager = findViewById(R.id.viewPagerVertical);
        inputOtpWrapper = findViewById(R.id.inputOtpWrapper);
        countryCode = findViewById(R.id.country_code);
        changeNumberBtn = findViewById(R.id.btn_change_number);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        textViewShowTime = findViewById(R.id.TimeCount);
        ResendBtn = findViewById(R.id.Resend);
        phoneNumberWrapper = findViewById(R.id.numberPhone);

        shortDescriptionPhone = findViewById(R.id.short_description_phone);
        countryName = findViewById(R.id.country_name);
        progressBarLoad = findViewById(R.id.progress_bar_load);
        currentMobileNumber = findViewById(R.id.current_mobile_number);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LoadingDialog = new ProgressDialog(PhoneLoginActivity.this);

        initializeViews();

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("PHONE_LOGIN", "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;

                updateUI(credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("PHONE_LOGIN", "onVerificationFailed", e);

                mVerificationInProgress = false;

                HideProgress();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    ErrorCode();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    QuickHelp.showToast(PhoneLoginActivity.this, getString(R.string.sms_quota_out), true);

                    ErrorCode();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("PHONE_LOGIN", "onCodeSent:" + verificationId);

                HideProgress();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                viewPager.setCurrentItem(1);

                setTimer();
                startTimer();
                currentMobileNumber.setText(mPhoneNumber);
            }
        };
    }

    private void updateUI(PhoneAuthCredential credential) {

        // Set the verification text based on the credential
        if (credential != null) {
            if (credential.getSmsCode() != null) {
                inputOtpWrapper.setText(credential.getSmsCode());
            } else {
                inputOtpWrapper.setText(R.string.instant_validation);
            }
        }
    }

    public void initializeViews(){

        btnNext.setText(getString(R.string.next));
        btnNext.setEnabled(true);
        btnNext.setVisibility(View.VISIBLE);
        layoutVerificationSv.setVisibility(View.VISIBLE);
        numberPhoneLayoutSv.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);

        hideKeyboard();

        mCountries = CountriesFetcher.getCountries(this);

        int defaultIdx = 0;

        String countryISO = Application.getInstance().getApplicationContext().getResources().getConfiguration().locale.getCountry().toUpperCase();

        if (!countryISO.isEmpty()){

            defaultIdx = mCountries.indexOfIso(countryISO);

        } else {

            defaultIdx = mCountries.indexOfIso(Constants.DEFAULT_COUNTRY_CODE);
        }

        //int defaultIdx = mCountries.indexOfIso(countryISO);
        mSelectedCountry = mCountries.get(defaultIdx);
        countryCode.setText(mSelectedCountry.getDial_code());
        countryName.setText(mSelectedCountry.getName());
        shortDescriptionPhone.setText(String.format("%s %s %s", getString(R.string.click_on), mSelectedCountry.getDial_code(), getString(R.string.to_choose_your_country)));
        setHint();

        btnNext.setOnClickListener(this);
        countryCode.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        ResendBtn.setOnClickListener(this);
        changeNumberBtn.setOnClickListener(this);
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        inputOtpWrapper.addTextChangedListener(new TextWatcherAdapter() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {

                    showProgress(getString(R.string.verifying));
                    verificationOfCode();
                }
            }
        });

        if (viewPager.getCurrentItem() == 1) {
            setOnKeyboardCodeDone();

        } else {
            setOnKeyboardDone();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Hide keyboard from phoneEdit field
     */
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(phoneNumberWrapper.getWindowToken(), 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    /**
     * Set hint number for country
     */
    private void setHint() {

        if (phoneNumberWrapper != null && mSelectedCountry != null && mSelectedCountry.getCode() != null) {
            Phonenumber.PhoneNumber phoneNumber = mPhoneUtil.getExampleNumberForType(mSelectedCountry.getCode(), PhoneNumberUtil.PhoneNumberType.MOBILE);
            if (phoneNumber != null) {
                String internationalNumber = mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                String finalPhone = internationalNumber.substring(mSelectedCountry.getDial_code().length());
                phoneNumberWrapper.setHint(finalPhone);
                int numberLength = internationalNumber.length();
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(numberLength);
                phoneNumberWrapper.setFilters(fArray);

            }
        }

    }

    /**
     * Get PhoneNumber object
     *
     * @return PhoneNumber | null on error
     */
    @SuppressWarnings("unused")
    public Phonenumber.PhoneNumber getPhoneNumber() {
        try {
            String iso = null;
            if (mSelectedCountry != null) {
                iso = mSelectedCountry.getCode();
            }
            String phone = countryCode.getText().toString().concat(phoneNumberWrapper.getText().toString());
            return mPhoneUtil.parse(phone, iso);
        } catch (NumberParseException ignored) {
            return null;
        }
    }

    /**
     * Check if number is valid
     *
     * @return boolean
     */
    @SuppressWarnings("unused")
    public boolean isValid() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
        return phoneNumber != null && mPhoneUtil.isValidNumber(phoneNumber);
    }

    public void setOnKeyboardDone() {
        phoneNumberWrapper.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard();
            }
            return false;
        });
    }

    public void setOnKeyboardCodeDone() {
        inputOtpWrapper.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                verificationOfCode();
            }
            return false;
        });
    }

    private void startPhoneNumberVerification(String country, String mobile) {

        mPhoneNumber = mobile;

        progressBarLoad.setVisibility(View.VISIBLE);
        progressBarLoad.setColor(QuickHelp.getColor(this, R.color.white));
        btnNext.setText(getString(R.string.wait_for_sms));
        btnNext.setEnabled(false);

        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,   // Phone number to verify
                60,               // Timeout duration
                TimeUnit.SECONDS,    // Unit of timeout
                this,       // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    public void ErrorCode(){

        QuickHelp.hideLoading(PhoneLoginActivity.this);

        QuickHelp.showNotification(PhoneLoginActivity.this, getString(R.string.error_ocurred), true);
        btnNext.setText(getString(R.string.next));
        btnNext.setEnabled(true);
        progressBarLoad.setVisibility(View.GONE);
        hideKeyboard();
    }
    // [END resend_verification]

    /**
     * method to verify the code received by user then activating the user
     */
    private void verificationOfCode() {
        hideKeyboard();
        String code = inputOtpWrapper.getText().toString().trim();
        if (!code.isEmpty()) {

            //doLogin(code);
            verifyPhoneNumberWithCode(mVerificationId, code);

        } else {

            QuickHelp.showToast(PhoneLoginActivity.this, getString(R.string.please_enter_your_ver_code), true);
        }
    }

    /**
     * method to validate user information
     */
    private void validateInformation() {
        hideKeyboard();
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
        if (phoneNumber != null) {
            String phoneNumberFinal = mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            if (isValid()) {
                String internationalFormat = phoneNumberFinal.replace("-", "");
                String finalResult = internationalFormat.replace(" ", "");
                //Constant.setMobileNumber(this, finalResult);
                //requestForSMS(finalResult, mSelectedCountry.getName());
                startPhoneNumberVerification(mSelectedCountry.getName(), finalResult);
            } else {
                phoneNumberWrapper.setError(getString(R.string.enter_a_val_number));
            }
        } else {
            phoneNumberWrapper.setError(getString(R.string.enter_a_val_number));
        }
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("PHONE_LOGIN", "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        LoginWithPhoneNumberInBackground(user);

                        //HideProgress();
                        // [START_EXCLUDE]
                        //updateUI(STATE_SIGNIN_SUCCESS, user);
                        // [END_EXCLUDE]
                    } else {

                        HideProgress();

                        // Sign in failed, display a message and update the UI
                        Log.w("PHONE_LOGIN", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE silent]
                            //mBinding.fieldVerificationCode.setError("Invalid code.");
                            ErrorCode();
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE silent]
                        // Update UI
                        ErrorCode();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SELECT_COUNTRY) {
                phoneNumberWrapper.setEnabled(true);
                numberPhoneLayoutSv.pageScroll(View.FOCUS_DOWN);
                String codeIso = data.getStringExtra("countryIso");
                int defaultIdx = mCountries.indexOfIso(codeIso);
                mSelectedCountry = mCountries.get(defaultIdx);
                this.countryCode.setText(mSelectedCountry.getDial_code());
                this.countryName.setText(mSelectedCountry.getName());
                mCountryName = mSelectedCountry.getName();
                mCountryDialCode = mSelectedCountry.getDial_code();
                mCountryIso = mSelectedCountry.getCode();
                shortDescriptionPhone.setText(String.format("%s %s %s", getString(R.string.click_on), mSelectedCountry.getDial_code(), getString(R.string.to_choose_your_country)));
                setHint();
            }
        }
    }

    public void LoginWithPhoneNumberInBackground(FirebaseUser firebaseUser){

        Log.d("PHONE_LOGIN", "Id: " + firebaseUser.getUid());
        Log.d("PHONE_LOGIN", "Phone: " + firebaseUser.getPhoneNumber());

        ParseQuery<User> userParseQuery = User.getUserQuery();
        userParseQuery.whereEqualTo(User.PHONE_NUMBER_FULL, firebaseUser.getPhoneNumber());
        userParseQuery.getFirstInBackground((user, e) -> {
            if (user != null){

                ParseUser.logInInBackground(user.getUsername(), user.getPasswordSecondary(), (user1, e12) -> {
                    if (e12 == null){
                        loginSuccess();
                    } else {
                        ErrorCode();
                    }
                });

            } else if (e != null && e.getCode() == ParseException.OBJECT_NOT_FOUND){

                String passwordSecondary = String.valueOf(QuickHelp.generateUId());

                User parseUser = ParseObject.create(User.class);

                parseUser.setUsername(firebaseUser.getPhoneNumber());
                parseUser.setCountry(mCountryName);
                parseUser.setCountryCode(mCountryIso);
                parseUser.setCountryDialCode(mCountryDialCode);
                parseUser.setPhoneNumber(phoneNumberWrapper.getText().toString());
                parseUser.setPhoneNumberFull(mPhoneNumber);
                parseUser.setPasswordEnabled(true);
                parseUser.setPassword(passwordSecondary);
                parseUser.setPasswordSecondary(passwordSecondary);
                parseUser.setUid(QuickHelp.generateUId());
                parseUser.setPopularity(0);
                parseUser.setRole(User.ROLE_USER);
                parseUser.setPrefMinAge(Config.MinimumAgeToRegister);
                parseUser.setPrefMaxAge(Config.MaximumAgeToRegister);
                parseUser.setLocationTypeNearBy(true);
                parseUser.addCredit(Config.WelcomeCredit);
                parseUser.setBio(Config.bio);
                parseUser.signUpInBackground(e1 -> {
                    if (e1 != null) {

                        Log.d("PHONE_LOGIN","Parse User failed to update");

                        ErrorCode();

                    } else {

                        Log.d("PHONE_LOGIN","Parse User Logged with phone number");

                        loginSuccess();
                    }

                });
            }
        });
    }

    private void loginSuccess() {

        User user = User.getUser();

        ParseInstallation.getCurrentInstallation().put("user", user);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        user.setInstallation(ParseInstallation.getCurrentInstallation());
        user.saveInBackground();

        ParsePush.subscribeInBackground(Config.CHANNEL);

        QuickHelp.hideLoading(PhoneLoginActivity.this);
        QuickHelp.goToActivityAndFinish(this, DispatchActivity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_sms:
                validateInformation();
                break;

            case R.id.country_code:
                Intent mIntent = new Intent(this, CountryActivity.class);
                startActivityForResult(mIntent, Constants.SELECT_COUNTRY);
                break;

            case R.id.btn_verify_otp:
                verificationOfCode();
                break;

            case R.id.Resend:
                resendVerificationCode(mPhoneNumber, mResendToken);
                break;

            case R.id.btn_change_number:
                viewPager.setCurrentItem(0);
                btnNext.setText(getString(R.string.next));
                btnNext.setEnabled(true);
                progressBarLoad.setVisibility(View.GONE);
                break;
        }
    }

    public void showProgress(String string) {

        if (!LoadingDialog.isShowing() && !isFinishing()) {

            LoadingDialog.setMessage(string);
            LoadingDialog.setCancelable(false);
            LoadingDialog.show();

        }
    }

    public void HideProgress() {

        if (LoadingDialog.isShowing() && !isFinishing()) {
            LoadingDialog.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTimer() {
        textViewShowTime.setVisibility(View.VISIBLE);
        int time = 1;
        totalTimeCountInMilliseconds = 60 * time * 1000;

    }

    private void startTimer() {
        textViewShowTime.setVisibility(View.VISIBLE);
        countDownTimer = new BaxPayCounter(totalTimeCountInMilliseconds, 500).start();
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void resumeTimer() {
        textViewShowTime.setVisibility(View.VISIBLE);
        countDownTimer = new BaxPayCounter(totalTimeCountInMilliseconds, 500).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.numberPhone_layout;
                    break;
                case 1:
                    resId = R.id.layout_verification;
                    break;
            }
            return findViewById(resId);
        }
    }

    public class BaxPayCounter extends CountDownTimer {

        BaxPayCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long leftTimeInMilliseconds) {
            long seconds = leftTimeInMilliseconds / 1000;
            textViewShowTime.setText(String.format("%s:%s", String.format(Locale.getDefault(), "%02d", seconds / 60), String.format(Locale.getDefault(), "%02d", seconds % 60)));
            //textViewShowTime.setText(String.format(Locale.getDefault(), "%02d", seconds / 60) + ":" + String.format(Locale.getDefault(), "%02d", seconds % 60));
        }

        @Override
        public void onFinish() {
            textViewShowTime.setVisibility(View.GONE);
            ResendBtn.setVisibility(View.VISIBLE);
        }
    }
}
