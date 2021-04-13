package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.parse.ParseUser;

public class FeatureActivationActivity extends AppCompatActivity {

    public static String FEATURE_TYPE = "feature_type";

    User mCurrentUser;
    String mFeatureType;

    AppCompatButton mButtonActivate;
    CircleImageView mAvatarImg;
    ImageView mTypeImg, mIcon1, mIcon2, mIcon3;
    TextView mTypeTitle, mTypeExplain, mConstTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_activation);

        mCurrentUser = (User) ParseUser.getCurrentUser();
        mFeatureType = getIntent().getStringExtra(FEATURE_TYPE);

        mAvatarImg = findViewById(R.id.avatar_img);
        mTypeImg = findViewById(R.id.type_img);
        mIcon1 = findViewById(R.id.icon1);
        mIcon2 = findViewById(R.id.icon2);
        mIcon3 = findViewById(R.id.icon3);
        mButtonActivate = findViewById(R.id.activate_btn);

        mTypeTitle = findViewById(R.id.title_txt);
        mTypeExplain = findViewById(R.id.explain_txt);
        mConstTxt = findViewById(R.id.cost_txt);

        if (mFeatureType != null) setFeatureType(mFeatureType);

        QuickHelp.getAvatars(mCurrentUser, mAvatarImg);


        mButtonActivate.setOnClickListener(v -> {

            if (mFeatureType.equals(PaymentsActivity.TYPE_RISE_UP)){

                activateFeature(PaymentsActivity.TYPE_RISE_UP, Config.TYPE_RISE_UP, Config.DAYS_TO_ACTIVATE_FEATURES);

            } else if (mFeatureType.equals(PaymentsActivity.TYPE_ADD_EXTRA_SHOWS)){

                activateFeature(PaymentsActivity.TYPE_ADD_EXTRA_SHOWS, Config.TYPE_ADD_EXTRA_SHOWS, Config.DAYS_TO_ACTIVATE_FEATURES);

            } else if (mFeatureType.equals(PaymentsActivity.TYPE_GET_MORE_VISITS)){

                activateFeature(PaymentsActivity.TYPE_GET_MORE_VISITS, Config.TYPE_GET_MORE_VISITS, Config.DAYS_TO_ACTIVATE_FEATURES);

            } else if (mFeatureType.equals(PaymentsActivity.TYPE_SHOW_IM_ONLINE)){

                activateFeature(PaymentsActivity.TYPE_SHOW_IM_ONLINE, Config.TYPE_SHOW_IM_ONLINE, Config.DAYS_TO_ACTIVATE_FEATURES);

            } else if (mFeatureType.equals(PaymentsActivity.TYPE_3X_POPULAR)){

                activateFeature(PaymentsActivity.TYPE_3X_POPULAR, Config.TYPE_3X_POPULAR, Config.DAYS_TO_ACTIVATE_FEATURES);

            } else QuickHelp.goToActivityWithNoClean(FeatureActivationActivity.this, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, mFeatureType);
        });

    }

    public void setFeatureType(String type){

        if (type.equals(PaymentsActivity.TYPE_RISE_UP)){

            mTypeImg.setImageResource(R.drawable.ic_badge_feature_riseup);
            mTypeTitle.setText(getString(R.string.rise_up_title));
            mTypeExplain.setText(getString(R.string.rise_up));
            mConstTxt.setText(String.format("%s: %s %s", getString(R.string.cost_of_service), Config.TYPE_RISE_UP, getString(R.string.credits_)));

            setPopularActivated(false);

        } else if (type.equals(PaymentsActivity.TYPE_ADD_EXTRA_SHOWS)){

            mTypeImg.setImageResource(R.drawable.ic_badge_feature_extra_shows);
            mTypeTitle.setText(getString(R.string.add_extra_shows_btn));
            mTypeExplain.setText(getString(R.string.add_extra_shows_btn_explain));
            mConstTxt.setText(String.format("%s: %s %s", getString(R.string.cost_of_service), Config.TYPE_ADD_EXTRA_SHOWS, getString(R.string.credits_)));

            setPopularActivated(false);

        } else if (type.equals(PaymentsActivity.TYPE_GET_MORE_VISITS)){

            mTypeImg.setImageResource(R.drawable.ic_badge_feature_spotlight);
            mTypeTitle.setText(getString(R.string.get_more_visits));
            mTypeExplain.setText(getString(R.string.get_more_visits_explain));
            mConstTxt.setText(String.format("%s: %s %s", getString(R.string.cost_of_service), Config.TYPE_GET_MORE_VISITS, getString(R.string.credits_)));

            setPopularActivated(false);

        } else if (type.equals(PaymentsActivity.TYPE_SHOW_IM_ONLINE)){

            mTypeImg.setImageResource(R.drawable.ic_badge_feature_attention_boost);
            mTypeTitle.setText(getString(R.string.show_im_online));
            mTypeExplain.setText(getString(R.string.show_im_online_explain));
            mConstTxt.setText(String.format("%s: %s %s", getString(R.string.cost_of_service), Config.TYPE_SHOW_IM_ONLINE, getString(R.string.credits_)));

            setPopularActivated(false);

        } else if (type.equals(PaymentsActivity.TYPE_3X_POPULAR)){

            mTypeTitle.setText(getString(R.string.get_3x_more_popular));
            mTypeExplain.setText(getString(R.string.get_3x_more_popular_explain));
            mConstTxt.setText(String.format("%s: %s %s", getString(R.string.cost_of_service), Config.TYPE_3X_POPULAR, getString(R.string.credits_)));

            setPopularActivated(true);

        }

    }

    public void activateFeature(String type, int credits, int days){

        QuickHelp.showLoading(this, false);

        if (type.equals(PaymentsActivity.TYPE_RISE_UP)){

            mCurrentUser.setVipMoveToTop(QuickHelp.incrementDate(days));
            mCurrentUser.removeCredit(credits);

        } else if (type.equals(PaymentsActivity.TYPE_GET_MORE_VISITS)){

            mCurrentUser.setVipMoreVisits(QuickHelp.incrementDate(days));
            mCurrentUser.removeCredit(credits);

        } if (type.equals(PaymentsActivity.TYPE_ADD_EXTRA_SHOWS)){

            mCurrentUser.setVipExtraShow(QuickHelp.incrementDate(days));
            mCurrentUser.removeCredit(credits);

        } else if (type.equals(PaymentsActivity.TYPE_SHOW_IM_ONLINE)){

            mCurrentUser.setVipShowOnline(QuickHelp.incrementDate(days));
            mCurrentUser.removeCredit(credits);

        } else if (type.equals(PaymentsActivity.TYPE_3X_POPULAR)){

            mCurrentUser.setVip3xPopular(QuickHelp.incrementDate(days));
            mCurrentUser.removeCredit(credits);
        }

        mCurrentUser.saveInBackground(e -> {

            if (e == null){

                QuickHelp.hideLoading(this);
                QuickHelp.showNotification(FeatureActivationActivity.this, getString(R.string.feature_activated), false);

                finish();

            } else {

                QuickHelp.hideLoading(this);
                QuickHelp.showNotification(FeatureActivationActivity.this, getString(R.string.error_ocurred), true);
            }
        });
    }

    public void setPopularActivated(boolean isPopular){

        if (isPopular){

            mTypeImg.setVisibility(View.GONE);

            mIcon1.setVisibility(View.VISIBLE);
            mIcon2.setVisibility(View.VISIBLE);
            mIcon3.setVisibility(View.VISIBLE);

        } else {

            mTypeImg.setVisibility(View.VISIBLE);

            mIcon1.setVisibility(View.GONE);
            mIcon2.setVisibility(View.GONE);
            mIcon3.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}