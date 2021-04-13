package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.popularity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;


import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.StatusBarUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ToggleableRadioButton;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Date;
import java.util.Objects;

public class PopularityActivity extends AppCompatActivity {

    User mCurrentUser;

    Toolbar toolbar;
    TextView mPopularityLevel, mTextAverage, mMonthStat, mDayStat, mNewContacts, mNewVisitors, mNewLikes;
    ImageView mPopularityAverage, mPopularityAverageToolbar;
    AppBarLayout maAppBarLayout;
    LinearLayout mLinearToolbar, mStatisticGroups;
    ImageView mGetFreeCreditsImg, mRiseUpImg, mGetMoreVisitsImg, mAddExtraShowsImg, mGetDatooPremiumImg, mShowImOnlineImg, mGet3xMorePopularImg;
    AppCompatButton mGetFreeCreditsBtn, mRiseUpBtn, mGetMoreVisitsBtn, mAddExtraShowsBtn, mGetDatooPremiumBtn, mShowImOnlineBtn, mGet3xMorePopularBtn;

    ToggleableRadioButton mPopularityDay1, mPopularityDay2, mPopularityDay3, mPopularityDay4, mPopularityDay5, mPopularityDay6, mPopularityToday;

    RewardedAd rewardedAd;

    int creditsAdded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularity);

        toolbar = findViewById(R.id.toolbar);
        maAppBarLayout = findViewById(R.id.app_bar_layout);

        mPopularityAverage = findViewById(R.id.popularity_level_indicator);
        mPopularityAverageToolbar = findViewById(R.id.popularity_level_indicator_toolbar);
        mPopularityLevel = findViewById(R.id.popularity_level);
        mTextAverage = findViewById(R.id.text_avetage);

        mLinearToolbar = findViewById(R.id.toolbar_popularityContainer);
        mStatisticGroups = findViewById(R.id.popularity_statisticGroup);

        mMonthStat = findViewById(R.id.popularity_selectedMonth);
        mDayStat = findViewById(R.id.popularity_selectedDay);

        mNewContacts = findViewById(R.id.new_contacts);
        mNewVisitors = findViewById(R.id.new_visitor);
        mNewLikes = findViewById(R.id.new_likes);

        mPopularityDay1 = findViewById(R.id.popularity_days_1);
        mPopularityDay2 = findViewById(R.id.popularity_days_2);
        mPopularityDay3 = findViewById(R.id.popularity_days_3);
        mPopularityDay4 = findViewById(R.id.popularity_days_4);
        mPopularityDay5 = findViewById(R.id.popularity_days_5);
        mPopularityDay6 = findViewById(R.id.popularity_days_6);
        mPopularityToday = findViewById(R.id.popularity_today);

        mPopularityDay1.setOnClickListener(mOnKeyClickListener);
        mPopularityDay2.setOnClickListener(mOnKeyClickListener);
        mPopularityDay3.setOnClickListener(mOnKeyClickListener);
        mPopularityDay4.setOnClickListener(mOnKeyClickListener);
        mPopularityDay5.setOnClickListener(mOnKeyClickListener);
        mPopularityDay6.setOnClickListener(mOnKeyClickListener);
        mPopularityToday.setOnClickListener(mOnKeyClickListener);

        mGetFreeCreditsImg = findViewById(R.id.get_free_credits_rv_image);
        mRiseUpImg = findViewById(R.id.rise_up_rv_image);
        mGetMoreVisitsImg = findViewById(R.id.get_more_visits_rv_image);
        mAddExtraShowsImg = findViewById(R.id.add_extra_shows_rv_image);
        mGetDatooPremiumImg = findViewById(R.id.get_datoo_premium_rv_image);
        mShowImOnlineImg = findViewById(R.id.show_im_online_rv_image);
        mGet3xMorePopularImg = findViewById(R.id.get_3x_more_popular_img);

        mGetFreeCreditsBtn = findViewById(R.id.get_free_credits_rv_button);
        mRiseUpBtn = findViewById(R.id.rise_up_rv_button);
        mGetMoreVisitsBtn = findViewById(R.id.get_more_visits_rv_button);
        mAddExtraShowsBtn = findViewById(R.id.add_extra_shows_rv_button);
        mGetDatooPremiumBtn = findViewById(R.id.get_datoo_premium_rv_button);
        mShowImOnlineBtn = findViewById(R.id.show_im_online_rv_button);
        mGet3xMorePopularBtn = findViewById(R.id.get_3x_more_popular_button);

        mRiseUpBtn.setOnClickListener(v -> goToPurchase(PaymentsActivity.TYPE_RISE_UP));
        mGetMoreVisitsBtn.setOnClickListener(v -> goToPurchase(PaymentsActivity.TYPE_GET_MORE_VISITS));
        mAddExtraShowsBtn.setOnClickListener(v -> goToPurchase(PaymentsActivity.TYPE_ADD_EXTRA_SHOWS));
        mGetDatooPremiumBtn.setOnClickListener(v -> goToPurchasePremium());
        mShowImOnlineBtn.setOnClickListener(v -> goToPurchase(PaymentsActivity.TYPE_SHOW_IM_ONLINE));
        mGet3xMorePopularBtn.setOnClickListener(v -> goToPurchase(PaymentsActivity.TYPE_3X_POPULAR));


        mStatisticGroups.setVisibility(View.GONE);

        StatusBarUtil.useTransparentBarWithCurrentBackground(this);
        StatusBarUtil.setDarkMode(this);

        Tools.setSystemBarColor(this, R.color.blue_3);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGetFreeCreditsBtn.setEnabled(false);

        loadRewardedVideo();

        mCurrentUser = (User) User.getCurrentUser();


        if (mCurrentUser != null){

            QuickHelp.getPopularityLevel(mCurrentUser);

            mPopularityAverage.setImageResource(QuickHelp.getPopularityLevelIndicator(mCurrentUser));
            mPopularityAverageToolbar.setImageResource(QuickHelp.getPopularityLevelIndicatorAlpha(mCurrentUser));
            mPopularityLevel.setText(QuickHelp.getPopularityLevel(mCurrentUser));

            if (mCurrentUser.getProfilePhotos().size() == 0){

                QuickHelp.getAvatar(mRiseUpImg, mCurrentUser);
                QuickHelp.getAvatar(mGetMoreVisitsImg, mCurrentUser);
                QuickHelp.getAvatar(mAddExtraShowsImg, mCurrentUser);
                QuickHelp.getAvatar(mGetDatooPremiumImg, mCurrentUser);
                QuickHelp.getAvatar(mShowImOnlineImg, mCurrentUser);
                QuickHelp.getAvatar(mGet3xMorePopularImg, mCurrentUser);

            } else if (mCurrentUser.getProfilePhotos().size() == 1){

                QuickHelp.getAvatar(mRiseUpImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetMoreVisitsImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mAddExtraShowsImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetDatooPremiumImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mShowImOnlineImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGet3xMorePopularImg, mCurrentUser, 0);

            } else if (mCurrentUser.getProfilePhotos().size() == 2){

                QuickHelp.getAvatar(mRiseUpImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetMoreVisitsImg, mCurrentUser, 1);
                QuickHelp.getAvatar(mAddExtraShowsImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetDatooPremiumImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mShowImOnlineImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGet3xMorePopularImg, mCurrentUser, 0);

            } else if (mCurrentUser.getProfilePhotos().size() == 3){

                QuickHelp.getAvatar(mRiseUpImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetMoreVisitsImg, mCurrentUser, 1);
                QuickHelp.getAvatar(mAddExtraShowsImg, mCurrentUser, 2);
                QuickHelp.getAvatar(mGetDatooPremiumImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mShowImOnlineImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGet3xMorePopularImg, mCurrentUser, 0);

            } else if (mCurrentUser.getProfilePhotos().size() == 4){

                QuickHelp.getAvatar(mRiseUpImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetMoreVisitsImg, mCurrentUser, 1);
                QuickHelp.getAvatar(mAddExtraShowsImg, mCurrentUser, 2);
                QuickHelp.getAvatar(mGetDatooPremiumImg, mCurrentUser, 3);
                QuickHelp.getAvatar(mShowImOnlineImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGet3xMorePopularImg, mCurrentUser, 0);

            } else if (mCurrentUser.getProfilePhotos().size() == 5){

                QuickHelp.getAvatar(mRiseUpImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetMoreVisitsImg, mCurrentUser, 1);
                QuickHelp.getAvatar(mAddExtraShowsImg, mCurrentUser, 2);
                QuickHelp.getAvatar(mGetDatooPremiumImg, mCurrentUser, 3);
                QuickHelp.getAvatar(mShowImOnlineImg, mCurrentUser, 4);
                QuickHelp.getAvatar(mGet3xMorePopularImg, mCurrentUser, 0);

            } else if (mCurrentUser.getProfilePhotos().size() > 5){

                QuickHelp.getAvatar(mRiseUpImg, mCurrentUser, 0);
                QuickHelp.getAvatar(mGetMoreVisitsImg, mCurrentUser, 1);
                QuickHelp.getAvatar(mAddExtraShowsImg, mCurrentUser, 2);
                QuickHelp.getAvatar(mGetDatooPremiumImg, mCurrentUser, 3);
                QuickHelp.getAvatar(mShowImOnlineImg, mCurrentUser, 4);
                QuickHelp.getAvatar(mGet3xMorePopularImg, mCurrentUser, 5);

            }

            // Check of services activated

            if (mCurrentUser.getVipMoveToTop() != null && new Date().before(mCurrentUser.getVipMoveToTop())){
                mRiseUpBtn.setEnabled(false);
                mRiseUpBtn.setTextSize(15);
                mRiseUpBtn.setText(String.format("%s: %s", getString(R.string.active_until), QuickHelp.getDateFormat(mCurrentUser.getVipMoveToTop())));
            } else {
                mRiseUpBtn.setEnabled(true);
            }

            if (mCurrentUser.getVipMoreVisits() != null && new Date().before(mCurrentUser.getVipMoreVisits())){
                mGetMoreVisitsBtn.setEnabled(false);
                mGetMoreVisitsBtn.setTextSize(15);
                mGetMoreVisitsBtn.setText(String.format("%s: %s", getString(R.string.active_until), QuickHelp.getDateFormat(mCurrentUser.getVipMoreVisits())));
            } else {
                mGetMoreVisitsBtn.setEnabled(true);
            }

            if (mCurrentUser.getVipAppearMore() != null && new Date().before(mCurrentUser.getVipAppearMore())){
                mAddExtraShowsBtn.setEnabled(false);
                mAddExtraShowsBtn.setTextSize(15);
                mAddExtraShowsBtn.setText(String.format("%s: %s", getString(R.string.active_until), QuickHelp.getDateFormat(mCurrentUser.getVipAppearMore())));
            } else {
                mAddExtraShowsBtn.setEnabled(true);
            }

            if (mCurrentUser.isPremiumLifeTime()){
                mGetDatooPremiumBtn.setEnabled(false);
                mGetDatooPremiumBtn.setTextSize(15);
                mGetDatooPremiumBtn.setText(getString(R.string.lifetime));
            } else if (mCurrentUser.getPremium() != null && new Date().before(mCurrentUser.getPremium())){
                mGetDatooPremiumBtn.setEnabled(false);
                mGetDatooPremiumBtn.setTextSize(15);
                mGetDatooPremiumBtn.setText(String.format("%s: %s", getString(R.string.active_until), QuickHelp.getDateFormat(mCurrentUser.getPremium())));
            } else {
                mGetDatooPremiumBtn.setEnabled(true);
            }

            if (mCurrentUser.getVipShowOnline() != null && new Date().before(mCurrentUser.getVipShowOnline())){
                mShowImOnlineBtn.setEnabled(false);
                mShowImOnlineBtn.setTextSize(15);
                mShowImOnlineBtn.setText(String.format("%s: %s", getString(R.string.active_until), QuickHelp.getDateFormat(mCurrentUser.getVipShowOnline())));
            } else {
                mShowImOnlineBtn.setEnabled(true);
            }

            if (mCurrentUser.getVip3xPopular() != null && new Date().before(mCurrentUser.getVip3xPopular())){
                mGet3xMorePopularBtn.setEnabled(false);
                mGet3xMorePopularBtn.setTextSize(15);
                mGet3xMorePopularBtn.setText(String.format("%s: %s", getString(R.string.active_until), QuickHelp.getDateFormat(mCurrentUser.getVip3xPopular())));
            } else {
                mGet3xMorePopularBtn.setEnabled(true);
            }

        }

        QuickHelp.setPopularityDays(mPopularityDay1, mPopularityDay2, mPopularityDay3, mPopularityDay4, mPopularityDay5, mPopularityDay6, mPopularityToday);

        maAppBarLayout.addOnOffsetChangedListener(new Tools.AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());

                if (state == State.EXPANDED){

                    mLinearToolbar.setVisibility(View.INVISIBLE);

                } else if (state == State.COLLAPSED){

                    mLinearToolbar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public RewardedAd createAndLoadRewardedAd() {
        rewardedAd = new RewardedAd(this, Constants.getRewardedAdsId());
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                mGetFreeCreditsBtn.setEnabled(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.

                mGetFreeCreditsBtn.setEnabled(false);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void loadRewardedVideo(){

        createAndLoadRewardedAd();

        mGetFreeCreditsBtn.setOnClickListener(v -> {

            if (rewardedAd.isLoaded()) {

                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.
                        QuickHelp.hideLoading(PopularityActivity.this);
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                        mGetFreeCreditsBtn.setEnabled(false);
                        rewardedAd = createAndLoadRewardedAd();

                        if (creditsAdded != 0){

                            QuickHelp.showNotification(PopularityActivity.this, creditsAdded + " " + getString(R.string.credit_added_to_yo_a), false);

                            creditsAdded = 0;
                        }
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        // User earned reward.

                        creditsAdded = reward.getAmount();

                        mCurrentUser.addCredit(reward.getAmount());
                        mCurrentUser.saveEventually();
                    }

                    @Override
                    public void onRewardedAdFailedToShow(int errorCode) {
                        // Ad failed to display
                        QuickHelp.showNotification(PopularityActivity.this, getString(R.string.video_ads_failed_loaded), true);

                        QuickHelp.hideLoading(PopularityActivity.this);
                    }
                };

                rewardedAd.show(PopularityActivity.this, adCallback);

            } else {

                QuickHelp.hideLoading(PopularityActivity.this);

                QuickHelp.showNotification(PopularityActivity.this, getString(R.string.video_ads_not_loaded), true);
                //Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            }
        });
    }


    public void goToPurchase(String type){


        if (type.equals(PaymentsActivity.TYPE_RISE_UP) && mCurrentUser.getCredits() >= Config.TYPE_RISE_UP){

            QuickHelp.goToActivityFeatureActivation(this, type);

        } else if (type.equals(PaymentsActivity.TYPE_GET_MORE_VISITS) && mCurrentUser.getCredits() >= Config.TYPE_GET_MORE_VISITS){

            QuickHelp.goToActivityFeatureActivation(this, type);

        } else if (type.equals(PaymentsActivity.TYPE_ADD_EXTRA_SHOWS) && mCurrentUser.getCredits() >= Config.TYPE_ADD_EXTRA_SHOWS){

            QuickHelp.goToActivityFeatureActivation(this, type);

        } else if (type.equals(PaymentsActivity.TYPE_SHOW_IM_ONLINE) && mCurrentUser.getCredits() >= Config.TYPE_SHOW_IM_ONLINE){

            QuickHelp.goToActivityFeatureActivation(this, type);

        }  else if (type.equals(PaymentsActivity.TYPE_3X_POPULAR) && mCurrentUser.getCredits() >= Config.TYPE_3X_POPULAR){

            QuickHelp.goToActivityFeatureActivation(this, type);

        } else QuickHelp.goToActivityWithNoClean(this, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, type);

    }

    public void goToPurchasePremium(){

        QuickHelp.goToActivityWithNoClean(this, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM, PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.DATOO_PREMIUM_TYPE);

    }

    private View.OnClickListener mOnKeyClickListener = v -> {
        if (v instanceof ToggleableRadioButton) {
            String day = ((ToggleableRadioButton) v).getText().toString();
            ToggleableRadioButton toggleableRadioButton = ((ToggleableRadioButton) v);

            int dayToCount ;
            int dayOfToday = QuickHelp.getTodayDay();

            mMonthStat.setText(QuickHelp.getDateFromMilliSeconds(new Date().getTime(), QuickHelp.DATE_MONTH));

            if (day.equals(getString(R.string.popularity_today))){

                String mDay = QuickHelp.getDateFromMilliSeconds(new Date().getTime(), QuickHelp.DATE_DAY_OF_MONTH);
                mDayStat.setText(mDay);

                dayToCount = dayOfToday - Integer.valueOf(mDay);

            } else {

                mDayStat.setText(day);
                dayToCount = dayOfToday - Integer.valueOf(day);
            }

            Date date =  QuickHelp.getDate(dayToCount);

            if (!toggleableRadioButton.isChecked()){

                mStatisticGroups.setVisibility(View.GONE);
                toggleableRadioButton .setSelected(false);


            } else {

                mStatisticGroups.setVisibility(View.VISIBLE);
                toggleableRadioButton.setSelected(true);

            }

            if (mCurrentUser != null){
                mCurrentUser.fetchIfNeededInBackground();
            }

            if (toggleableRadioButton == mPopularityDay1){


                if (mCurrentUser != null){

                    runOnUiThread(() -> {

                    });

                }

            } else if (toggleableRadioButton == mPopularityDay2){


                if (mCurrentUser != null){

                    runOnUiThread(() -> {


                    });

                }

            } else if (toggleableRadioButton == mPopularityDay3){


                if (mCurrentUser != null){

                    runOnUiThread(() -> {


                    });

                }

            } else if (toggleableRadioButton == mPopularityDay4){

                if (mCurrentUser != null){

                    runOnUiThread(() -> {

                    });

                }

            } else if (toggleableRadioButton == mPopularityDay5){

                if (mCurrentUser != null){

                    runOnUiThread(() -> {

                    });

                }

            } else if (toggleableRadioButton == mPopularityDay6){

                if (mCurrentUser != null){

                    runOnUiThread(() -> {


                    });

                }

            } else if (toggleableRadioButton == mPopularityToday){

                if (mCurrentUser != null){

                    runOnUiThread(() -> {

                    });

                }

            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
