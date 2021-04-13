package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings.accountPreferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import java.util.Objects;

public class SettingsPrivacyActivity extends AppCompatActivity {

    Toolbar toolbar;

    User mCurrentUser;

    SwitchCompat mShowDistance, mShowOnlineStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_privacy);

        toolbar = findViewById(R.id.toolbar);

        mShowDistance = findViewById(R.id.show_distance);
        mShowOnlineStatus = findViewById(R.id.show_online);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.setting_privacy));
        getSupportActionBar().setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mCurrentUser = (User) User.getCurrentUser();

        if (mCurrentUser != null){

            if (mCurrentUser.getPrivacyDistanceEnabled()){
                mShowDistance.setChecked(true);
            } else {
                mShowDistance.setChecked(false);
            }

            if (mCurrentUser.getPrivacyOnlineStatusEnabled()){
                mShowOnlineStatus.setChecked(true);
            } else {
                mShowOnlineStatus.setChecked(false);
            }
        }


        mShowDistance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPrivacyDistanceEnabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPrivacyDistanceEnabled(true);
                mCurrentUser.saveEventually();
            }
        });

        mShowOnlineStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPrivacyOnlineStatusEnabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPrivacyOnlineStatusEnabled(true);
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