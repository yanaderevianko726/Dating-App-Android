package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings.accountPreferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import java.util.Objects;

public class SettingsInvisibleModeActivity extends AppCompatActivity {

    Toolbar toolbar;

    User mCurrentUser;

    SwitchCompat mAlmostInvisible, mCloakedInvisibility, mHidePremiumStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_invisible_mode);

        toolbar = findViewById(R.id.toolbar);

        mAlmostInvisible = findViewById(R.id.almost_invisible);
        mCloakedInvisibility = findViewById(R.id.cloaked_invisibility);
        mHidePremiumStatus = findViewById(R.id.hide_premium_status);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.setting_invisible_mode));
        getSupportActionBar().setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mCurrentUser = (User) User.getCurrentUser();

        if (mCurrentUser != null){

            if (mCurrentUser.getPrivacyAlmostInvisible()){

                mAlmostInvisible.setChecked(true);

            } else {

                mAlmostInvisible.setChecked(false);
            }

            if (mCurrentUser.getPrivacyCloakedInvisibility()){

                mCloakedInvisibility.setChecked(true);

            } else {

                mCloakedInvisibility.setChecked(false);
            }

            if (mCurrentUser.getPrivacyHidePremiumStatus()){

                mHidePremiumStatus.setChecked(true);

            } else {

                mHidePremiumStatus.setChecked(false);
            }
        }


        mAlmostInvisible.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mCurrentUser.setPrivacyEnabledAlmostInvisible(true);
                mCurrentUser.saveEventually();

            } else {

                mCurrentUser.setPrivacyEnabledAlmostInvisible(false);
                mCurrentUser.saveEventually();
            }

        });

        mCloakedInvisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mCurrentUser.setPrivacyEnabledCloakedInvisibility(true);
                mCurrentUser.saveEventually();

            } else {

                mCurrentUser.setPrivacyEnabledCloakedInvisibility(false);
                mCurrentUser.saveEventually();
            }

        });

        mHidePremiumStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mCurrentUser.setPrivacyHidePremiumStatus(true);
                mCurrentUser.saveEventually();

            } else {

                mCurrentUser.setPrivacyHidePremiumStatus(false);
                mCurrentUser.saveEventually();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}