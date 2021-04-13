package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.PaymentProductListSkuAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.SliderPagerAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.PaymentSliderModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.dotsindicator.SpringDotsIndicator;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class PaymentsActivityGoogle extends AppCompatActivity implements PaymentProductListSkuAdapter.ItemViewHolder.OnItemSelectedListener, PurchasesUpdatedListener {

    public static String DATOO_PAYMENT_TYPE = "PAYMENT_TYPE";
    public static String DATOO_PREMIUM_TYPE = "PREMIUM_TYPE";

    public static String TYPE_DATOO_PREMIUM = "DATOO_PREMIUM";
    public static String TYPE_DATOO_PREMIUM_LIKED = "DATOO_PREMIUM_LIKED";
    public static String TYPE_DATOO_PREMIUM_UNDO = "DATOO_PREMIUM_UNDO";
    public static String TYPE_DATOO_PREMIUM_INCOGNITO = "DATOO_PREMIUM_INCOGNITO";
    public static String TYPE_DATOO_PREMIUM_MESSAGES = "DATOO_PREMIUM_MESSAGES";

    public static String TYPE_RISE_UP = "RISE_UP";
    public static String TYPE_GET_MORE_VISITS = "GET_MORE_VISITS";
    public static String TYPE_ADD_EXTRA_SHOWS = "ADD_EXTRA_SHOWS";
    public static String TYPE_SHOW_IM_ONLINE = "SHOW_IM_ONLINE";
    public static String TYPE_3X_POPULAR = "3X_POPULAR";
    public RecyclerView mRecyclerView;
    public ViewPager viewPager;
    String mType, mPremiumType;
    Toolbar mToolbar;
    TextView mToolbarText;
    AppCompatButton mContinueBtn;
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private List<SkuDetails> mPaymentProductModels;
    private PaymentProductListSkuAdapter mPaymentProductListAdapter;
    private SliderPagerAdapter mSliderPagerAdapter;
    private ArrayList<PaymentSliderModel> mPaymentSliderModel;
    private SkuDetails productModel;
    private BillingClient mBillingClient;
    private User mCurrentUser;
    private LinearLayout mEmptyView, mEmptyLayout, mLoadingLayout;
    private TextView mErrorDesc, mErrorTitle;
    private ImageView mErrorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        setContentView(R.layout.activity_payments_google);

        mType = getIntent().getStringExtra(DATOO_PAYMENT_TYPE);
        mPremiumType = getIntent().getStringExtra(DATOO_PREMIUM_TYPE);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        mPaymentProductModels = new ArrayList<>();
        mPaymentProductListAdapter = new PaymentProductListSkuAdapter(this, this, mPaymentProductModels);

        mPaymentSliderModel = new ArrayList<>();
        mSliderPagerAdapter = new SliderPagerAdapter(this, mPaymentSliderModel);

        viewPager = findViewById(R.id.productList_carouselPager);
        mRecyclerView = findViewById(R.id.productList_productPackageList);

        mEmptyView = findViewById(R.id.empty_view);
        mEmptyLayout= findViewById(R.id.empty_layout);
        mLoadingLayout= findViewById(R.id.loading);

        mErrorImage = findViewById(R.id.image);
        mErrorTitle = findViewById(R.id.title);
        mErrorDesc = findViewById(R.id.brief);

        mToolbar = findViewById(R.id.toolbar);
        mToolbarText = findViewById(R.id.productList_title);
        mContinueBtn = findViewById(R.id.payments_purchaseButton);


        setLoading();

        initComponent();
    }

    private void initComponent() {
        SpringDotsIndicator dotsIndicator = findViewById(R.id.productList_carouselIndicator);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setElevation(2.0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setAdapter(mPaymentProductListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        if (mType.equals(TYPE_DATOO_PREMIUM)){

            mToolbarText.setText(String.format("%s %s %s", getString(R.string.activate), getString(R.string.app_name), getString(R.string.premium)));

        } else {

            mToolbarText.setText(R.string.refill_cre);
        }

        viewPager.setAdapter(mSliderPagerAdapter);
        dotsIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        mContinueBtn.setOnClickListener(v -> initPurchaseFlow());

        createSlider();
        initPurchase();
    }

    public void createSlider(){

        List<PaymentSliderModel> paymentSliderModelList = new ArrayList<>();

        if (mType.equals(TYPE_DATOO_PREMIUM)){

            PaymentSliderModel unlimitedMessages = new PaymentSliderModel();
            unlimitedMessages.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            unlimitedMessages.setTitle(getString(R.string.unlimited_messages));
            unlimitedMessages.setBadgeImage(R.drawable.ic_badge_feature_chat);
            //unlimitedMessages.setOtherImage();

            PaymentSliderModel findFavorite = new PaymentSliderModel();
            findFavorite.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            findFavorite.setTitle(getString(R.string.who_added_you));
            findFavorite.setBadgeImage(R.drawable.ic_badge_feature_favourites);
            //findFavorite.setOtherImage();

            PaymentSliderModel whoLiked = new PaymentSliderModel();
            whoLiked.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            whoLiked.setTitle(getString(R.string.who_liked_you));
            whoLiked.setBadgeImage(R.drawable.ic_badge_feature_liked_you);
            //whoLiked.setOtherImage();

            PaymentSliderModel messageReadFirst = new PaymentSliderModel();
            messageReadFirst.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            messageReadFirst.setTitle(getString(R.string.get_messsage_read));
            messageReadFirst.setBadgeImage(R.drawable.ic_badge_feature_special_delivery);
            //messageReadFirst.setOtherImage();

            PaymentSliderModel chatWithNewGirls = new PaymentSliderModel();
            chatWithNewGirls.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            chatWithNewGirls.setTitle(getString(R.string.chat_with_girls));
            chatWithNewGirls.setBadgeImage(R.drawable.ic_badge_feature_chat_with_newbies);
            //chatWithNewGirls.setOtherImage();

            PaymentSliderModel undo = new PaymentSliderModel();
            undo.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            undo.setTitle(getString(R.string.undo_no));
            undo.setBadgeImage(R.drawable.ic_badge_feature_undo);
            //undo.setOtherImage();

            PaymentSliderModel chatPopular = new PaymentSliderModel();
            chatPopular.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            chatPopular.setTitle(getString(R.string.chat_popular));
            chatPopular.setBadgeImage(R.drawable.ic_badge_feature_chat_with_tired);
            //chatPopular.setOtherImage();

            PaymentSliderModel goIncognito = new PaymentSliderModel();
            goIncognito.setType(PaymentSliderModel.SLIDER_YPE_PREMIUM);
            goIncognito.setTitle(getString(R.string.go_incognito));
            goIncognito.setBadgeImage(R.drawable.ic_badge_feature_invisible_mode);
            //goIncognito.setOtherImage();


            if (mPremiumType.equals(TYPE_DATOO_PREMIUM_LIKED)){

                paymentSliderModelList.add(whoLiked);
                if (Config.isPaidMessagesActivated){
                    paymentSliderModelList.add(unlimitedMessages);
                }
                paymentSliderModelList.add(undo);
                paymentSliderModelList.add(findFavorite);
                paymentSliderModelList.add(messageReadFirst);
                paymentSliderModelList.add(chatWithNewGirls);
                paymentSliderModelList.add(chatPopular);
                paymentSliderModelList.add(goIncognito);

            } else if (mPremiumType.equals(TYPE_DATOO_PREMIUM_UNDO)){

                paymentSliderModelList.add(undo);
                if (Config.isPaidMessagesActivated){
                    paymentSliderModelList.add(unlimitedMessages);
                }
                paymentSliderModelList.add(whoLiked);
                paymentSliderModelList.add(findFavorite);
                paymentSliderModelList.add(messageReadFirst);
                paymentSliderModelList.add(chatWithNewGirls);
                paymentSliderModelList.add(chatPopular);
                paymentSliderModelList.add(goIncognito);

            } else if (mPremiumType.equals(TYPE_DATOO_PREMIUM_INCOGNITO)){

                paymentSliderModelList.add(goIncognito);
                if (Config.isPaidMessagesActivated){
                    paymentSliderModelList.add(unlimitedMessages);
                }
                paymentSliderModelList.add(undo);
                paymentSliderModelList.add(whoLiked);
                paymentSliderModelList.add(findFavorite);
                paymentSliderModelList.add(messageReadFirst);
                paymentSliderModelList.add(chatWithNewGirls);
                paymentSliderModelList.add(chatPopular);

            } else if (mPremiumType.equals(TYPE_DATOO_PREMIUM_MESSAGES)){

                if (Config.isPaidMessagesActivated){
                    paymentSliderModelList.add(unlimitedMessages);
                }
                paymentSliderModelList.add(whoLiked);
                paymentSliderModelList.add(undo);
                paymentSliderModelList.add(findFavorite);
                paymentSliderModelList.add(messageReadFirst);
                paymentSliderModelList.add(chatWithNewGirls);
                paymentSliderModelList.add(chatPopular);
                paymentSliderModelList.add(goIncognito);

            } else {

                if (Config.isPaidMessagesActivated){
                    paymentSliderModelList.add(unlimitedMessages);
                }
                paymentSliderModelList.add(findFavorite);
                paymentSliderModelList.add(whoLiked);
                paymentSliderModelList.add(messageReadFirst);
                paymentSliderModelList.add(chatWithNewGirls);
                paymentSliderModelList.add(undo);
                paymentSliderModelList.add(chatPopular);
                paymentSliderModelList.add(goIncognito);
            }

        } else {

            PaymentSliderModel popular3x = new PaymentSliderModel();
            popular3x.setType(PaymentSliderModel.SLIDER_YPE_NORMAL);
            popular3x.setCredit(String.format("%s %s", Config.TYPE_3X_POPULAR, getString(R.string.credits_)));
            popular3x.setTitle(getString(R.string.enyoy_service));
            popular3x.setBadgeImage(R.drawable.ic_badge_feature_bundle_sale);

            PaymentSliderModel riseUp = new PaymentSliderModel();
            riseUp.setType(PaymentSliderModel.SLIDER_YPE_NORMAL);
            riseUp.setCredit(String.format("%s %s", Config.TYPE_RISE_UP, getString(R.string.credits_)));
            riseUp.setTitle(getString(R.string.first_nearby));
            riseUp.setBadgeImage(R.drawable.ic_badge_feature_riseup);


            PaymentSliderModel getMoreVisits = new PaymentSliderModel();
            getMoreVisits.setType(PaymentSliderModel.SLIDER_YPE_NORMAL);
            getMoreVisits.setCredit(String.format("%s %s", Config.TYPE_GET_MORE_VISITS, getString(R.string.credits_)));
            getMoreVisits.setTitle(getString(R.string.more_visits));
            getMoreVisits.setBadgeImage(R.drawable.ic_badge_feature_spotlight);


            PaymentSliderModel extraShow = new PaymentSliderModel();
            extraShow.setType(PaymentSliderModel.SLIDER_YPE_NORMAL);
            extraShow.setCredit(String.format("%s %s", Config.TYPE_ADD_EXTRA_SHOWS, getString(R.string.credits_)));
            extraShow.setTitle(getString(R.string.more_matches));
            extraShow.setBadgeImage(R.drawable.ic_badge_feature_extra_shows);

            PaymentSliderModel imOnline = new PaymentSliderModel();
            imOnline.setType(PaymentSliderModel.SLIDER_YPE_NORMAL);
            imOnline.setCredit(String.format("%s %s", Config.TYPE_SHOW_IM_ONLINE, getString(R.string.credits_)));
            imOnline.setTitle(getString(R.string.you_re_online));
            imOnline.setBadgeImage(R.drawable.ic_badge_feature_attention_boost);

            if (mType.equals(TYPE_3X_POPULAR)){

                paymentSliderModelList.add(popular3x);
                paymentSliderModelList.add(riseUp);
                paymentSliderModelList.add(getMoreVisits);
                paymentSliderModelList.add(extraShow);
                paymentSliderModelList.add(imOnline);

            } else if (mType.equals(TYPE_RISE_UP)){

                paymentSliderModelList.add(riseUp);
                paymentSliderModelList.add(getMoreVisits);
                paymentSliderModelList.add(extraShow);
                paymentSliderModelList.add(imOnline);

            } else if (mType.equals(TYPE_GET_MORE_VISITS)){

                paymentSliderModelList.add(getMoreVisits);
                paymentSliderModelList.add(riseUp);
                paymentSliderModelList.add(extraShow);
                paymentSliderModelList.add(imOnline);

            } else if (mType.equals(TYPE_ADD_EXTRA_SHOWS)){

                paymentSliderModelList.add(extraShow);
                paymentSliderModelList.add(riseUp);
                paymentSliderModelList.add(getMoreVisits);
                paymentSliderModelList.add(imOnline);

            } else if (mType.equals(TYPE_SHOW_IM_ONLINE)){

                paymentSliderModelList.add(imOnline);
                paymentSliderModelList.add(riseUp);
                paymentSliderModelList.add(getMoreVisits);
                paymentSliderModelList.add(extraShow);
            }

        }

        mPaymentSliderModel.clear();
        mPaymentSliderModel.addAll(paymentSliderModelList);
        mSliderPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(SkuDetails item) {

        mPaymentProductListAdapter.notifyDataSetChanged();
        productModel = item;

    }

    private void setLoading(){

        mRecyclerView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContinueBtn.setVisibility(View.GONE);
    }

    private void hideLoading(boolean isLoaded){

        if (isLoaded){
            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mContinueBtn.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mContinueBtn.setVisibility(View.GONE);
        }
        mLoadingLayout.setVisibility(View.GONE);
    }

    private void initPurchase() {


        mBillingClient = BillingClient.newBuilder(PaymentsActivityGoogle.this).setListener(this).enablePendingPurchases().build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    if (mType.equals(TYPE_DATOO_PREMIUM)){
                       initSKUPurchase();
                    } else {
                        initSKUCredits();
                    }

                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });


    }

    public void initSKUCredits() {

        List<String> creditsPurchase = new ArrayList<> ();
        creditsPurchase.add(Config.CREDIT_100);
        creditsPurchase.add(Config.CREDIT_550);
        creditsPurchase.add(Config.CREDIT_1250);
        creditsPurchase.add(Config.CREDIT_2750);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(creditsPurchase).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    // Process the result.

                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        if (skuDetailsList != null && skuDetailsList.size() > 0){

                            createList(skuDetailsList, skuDetailsList.get(0));

                            hideLoading(true);

                        } else {

                            hideLoading(false);

                            mErrorImage.setImageResource(R.drawable.ic_badge_feature_premium);
                            mErrorTitle.setText(R.string.no_subs_found);
                            mErrorDesc.setText(R.string.no_subs_found_explain);
                        }

                    } else {

                        hideLoading(false);

                        mErrorImage.setImageResource(R.drawable.ic_close);
                        mErrorTitle.setText(R.string.error_ocurred);
                        mErrorDesc.setText(R.string.error_try_again_later);
                    }
                });
    }

    private void createList(List<SkuDetails> skuDetailsList, SkuDetails skuDetails) {

        mPaymentProductModels.clear();
        mPaymentProductModels.addAll(skuDetailsList);
        mPaymentProductModels.add(skuDetails);
        mPaymentProductListAdapter.notifyDataSetChanged();

        productModel = mPaymentProductModels.get(0);
    }

    public void initSKUPurchase() {

        List<String> creditsPurchase = new ArrayList<> ();
        creditsPurchase.add(Config.SUBS_1_WEEK);
        creditsPurchase.add(Config.SUBS_1_MONTH);
        creditsPurchase.add(Config.SUBS_3_MONTHS);
        creditsPurchase.add(Config.SUBS_6_MONTHS);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(creditsPurchase).setType(BillingClient.SkuType.SUBS);
        mBillingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    // Process the result.

                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        if (skuDetailsList != null && skuDetailsList.size() > 0){

                            createList(skuDetailsList, skuDetailsList.get(0));

                            hideLoading(true);

                        } else {

                            hideLoading(false);

                            mErrorImage.setImageResource(R.drawable.ic_badge_feature_premium);
                            mErrorTitle.setText(R.string.no_subs_found);
                            mErrorDesc.setText(R.string.no_subs_found_explain);
                        }

                    } else {

                        hideLoading(false);

                        mErrorImage.setImageResource(R.drawable.ic_close);
                        mErrorTitle.setText(R.string.error_ocurred);
                        mErrorDesc.setText(R.string.error_try_again_later);
                    }
                });
    }

    public void initPurchaseFlow(){

        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(productModel)
                .build();
        mBillingClient.launchBillingFlow(PaymentsActivityGoogle.this, flowParams);

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {

                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                    // Acknowledge purchase and grant the item to the user


                    switch (purchase.getSku()) {
                        case Config.CREDIT_100:

                            acknowledgePurchaseConsume(100, purchase);

                            break;
                        case Config.CREDIT_550:

                            acknowledgePurchaseConsume(550, purchase);
                            break;
                        case Config.CREDIT_1250:

                            acknowledgePurchaseConsume(1250, purchase);

                            break;
                        case Config.CREDIT_2750:

                            acknowledgePurchaseConsume(2750, purchase);
                            break;

                        case Config.PAY_LIFETIME:

                            addPremiumLifeTime(true, purchase);
                            break;

                        case Config.SUBS_1_WEEK:

                            acknowledgePurchaseSubs(7, purchase);
                            break;

                        case Config.SUBS_1_MONTH:

                            acknowledgePurchaseSubs(30, purchase);
                            break;

                        case Config.SUBS_3_MONTHS:

                            acknowledgePurchaseSubs(90, purchase);
                            break;

                        case Config.SUBS_6_MONTHS:

                            acknowledgePurchaseSubs(180, purchase);
                            break;
                    }

                }
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.

            QuickHelp.showNotification(PaymentsActivityGoogle.this, getString(R.string.purchase_canceled), true);

        } else {
            // Handle any other error codes.

            QuickHelp.showNotification(PaymentsActivityGoogle.this, getString(R.string.error_try_again_later), true);
        }
    }

    public void acknowledgePurchaseConsume(int credits, Purchase purchase){


        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();

        ConsumeResponseListener consumeResponseListener = (billingResult, purchaseToken) -> addCreditToAccount(credits);

        mBillingClient.consumeAsync(consumeParams, consumeResponseListener);
    }

    public void acknowledgePurchaseSubs(int days, Purchase purchase){


        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken()).build();

        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult1 -> {
            // Purchase finishes
            addPremiumLimitDate(days);
        };

        mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
    }

    public void addCreditToAccount(int credits) {

        mCurrentUser.addCredit(credits);
        mCurrentUser.saveEventually(e -> {

            if (e == null){

                QuickHelp.showNotification(PaymentsActivityGoogle.this, credits + " " + getString(R.string.credit_added_to_yo_a), false);
            }
        });
    }

    public void addPremiumLimitDate(int days) {

        Calendar calendar = Calendar.getInstance();

        if (days == 7){

            calendar.add(Calendar.DAY_OF_WEEK, 7);

        } else if (days == 30){

            calendar.add(Calendar.DAY_OF_MONTH, 30);

        } else if (days == 90){

            calendar.add(Calendar.MONTH, 3);

        } else if (days == 180){

            calendar.add(Calendar.MONTH, 6);

        }

        mCurrentUser.setPremium(calendar.getTime());
        mCurrentUser.saveEventually(e -> {

            if (e == null){

                QuickHelp.showNotification(PaymentsActivityGoogle.this, getString(R.string.you_are_premium), false);
            }
        });
    }

    public void addPremiumLifeTime(boolean isLifetime, Purchase purchase) {


        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken()).build();

        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult1 -> {
            // Purchase finishes
            mCurrentUser.setPremiumLifetime(isLifetime);
            mCurrentUser.saveEventually(e -> {

                if (e == null){

                    QuickHelp.showNotification(PaymentsActivityGoogle.this, getString(R.string.you_are_premium), false);
                }
            });
        };

        mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

    }

    /**
     * View pager adapter
     */


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}