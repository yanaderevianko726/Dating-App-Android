package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.settings.accountPreferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import java.util.Objects;

public class SettingsNotificationsActivity extends AppCompatActivity {

    Toolbar toolbar;

    User mCurrentUser;

    SwitchCompat mMessages, mMatches, mLikedYou, mProfileVisitor, mFavoritedYou, mDatooLive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_notifications);

        toolbar = findViewById(R.id.toolbar);

        mMessages = findViewById(R.id.noti_messages);
        mMatches = findViewById(R.id.noti_matches);
        mLikedYou = findViewById(R.id.noti_liked_you);
        mProfileVisitor = findViewById(R.id.noti_profile_visitor);
        mFavoritedYou = findViewById(R.id.noti_favorited_you);
        mDatooLive = findViewById(R.id.noti_datoo_live);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.setting_notifications));
        getSupportActionBar().setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mCurrentUser = (User) User.getCurrentUser();

        if (mCurrentUser != null){

            if (mCurrentUser.getPushNotificationsMassagesEnabled()){
                mMessages.setChecked(true);
            } else {
                mMessages.setChecked(false);
            }

            if (mCurrentUser.getPushNotificationsMatchesEnabled()){
                mMatches.setChecked(true);
            } else {
                mMatches.setChecked(false);
            }

            if (mCurrentUser.getPushNotificationsLikedYouEnabled()){
                mLikedYou.setChecked(true);
            } else {
                mLikedYou.setChecked(false);
            }

            if (mCurrentUser.getPushNotificationsProfileVisitorEnabled()){
                mProfileVisitor.setChecked(true);
            } else {
                mProfileVisitor.setChecked(false);
            }

            if (mCurrentUser.getPushNotificationsFavoritedYouEnabled()){
                mFavoritedYou.setChecked(true);
            } else {
                mFavoritedYou.setChecked(false);
            }

            if (mCurrentUser.getPushNotificationsLiveEnabled()){
                mDatooLive.setChecked(true);
            } else {
                mDatooLive.setChecked(false);
            }
        }


        mMessages.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPushNotificationsMassagesDisabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPushNotificationsMassagesDisabled(true);
                mCurrentUser.saveEventually();
            }
        });

        mMatches.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPushNotificationsMatchesDisabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPushNotificationsMatchesDisabled(true);
                mCurrentUser.saveEventually();
            }
        });

        mLikedYou.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPushNotificationsLikedYouDisabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPushNotificationsLikedYouDisabled(true);
                mCurrentUser.saveEventually();
            }
        });

        mProfileVisitor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPushNotificationsProfileVisitorDisabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPushNotificationsProfileVisitorDisabled(true);
                mCurrentUser.saveEventually();
            }
        });

        mFavoritedYou.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPushNotificationsFavoritedYouDisabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPushNotificationsFavoritedYouDisabled(true);
                mCurrentUser.saveEventually();
            }
        });

        mDatooLive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mCurrentUser.setPushNotificationsLiveDisabled(false);
                mCurrentUser.saveEventually();
            } else {
                mCurrentUser.setPushNotificationsLiveDisabled(true);
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