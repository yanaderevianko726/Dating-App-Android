package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings.accountPreferences;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import java.util.Objects;

public class AccountPreferencesActivity extends AppCompatActivity {

    Toolbar toolbar;

    User mCurrentUser;

    LinearLayout mPrivacy, mInvisibleMode, mNotifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_preferences);

        toolbar = findViewById(R.id.toolbar);

        mPrivacy = findViewById(R.id.setting_privacy);
        mNotifications = findViewById(R.id.setting_notifications);
        mInvisibleMode = findViewById(R.id.setting_invisible_mode);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.account_preference));
        getSupportActionBar().setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mCurrentUser = (User) User.getCurrentUser();

        mPrivacy.setOnClickListener(v -> goToPrivacy());
        mNotifications.setOnClickListener(v -> goToNotifications());
        mInvisibleMode.setOnClickListener(v -> goToInvisibleMode());

    }


    public void goToPrivacy(){

        QuickHelp.goToActivityWithNoClean(this, SettingsPrivacyActivity.class);

    }

    public void goToNotifications(){

        QuickHelp.goToActivityWithNoClean(this, SettingsNotificationsActivity.class);
    }

    public void goToInvisibleMode(){

        if (Config.isPremiumEnabled){

            if (mCurrentUser.isPremium()){

                QuickHelp.goToActivityWithNoClean(this, SettingsInvisibleModeActivity.class);

            } else {

                QuickHelp.goToActivityWithNoClean(this, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM, PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM_INCOGNITO);
            }

        } else {

            QuickHelp.goToActivityWithNoClean(this, SettingsInvisibleModeActivity.class);
        }

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