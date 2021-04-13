package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.encounters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.EncountersAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.WelcomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.SendNotifications;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.HomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.EncountersModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.CardStackLayoutManager;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.CardStackListener;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.CardStackView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.Direction;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.Duration;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.SwipeAnimationSetting;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.rangeBarView.OnRangeChangedListener;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.rangeBarView.RangeSeekBar;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.shimmer.ShimmerFrameLayout;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.topsheet.TopSheetBehavior;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.topsheet.TopSheetDialog;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.greysonparrelli.permiso.Permiso;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EncountersFragment extends Fragment implements CardStackListener {

    private static final int REQUEST_CHECK_SETTINGS = 1124;

    private User mCurrentUser;

    private TopSheetDialog sheetDialog;
    private BottomSheetDialog bottomSheetDialog;

    private ArrayList<User> mUsersNear;
    private EncountersAdapter adapter;

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;

    private CardView mCardView;

    private LinearLayout mEmptyView, mEmptyLayout;
    private ShimmerFrameLayout mLoadingLayout;
    private FrameLayout mEncountersCards;

    private TextView mErrorDesc, mErrorTitle;

    private ImageView mErrorImage;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    private boolean isLikeCrush = false;
    private boolean isAdsUsed = false;

    RewardedAd rewardedAd;
    private boolean isAdsLoaded = false;
    boolean EarnedReward = false;


    public EncountersFragment() {
        // Required empty public constructor

    }

    public static EncountersFragment newInstance() {
        return new EncountersFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle b) {
        super.onViewCreated(view, b);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUsersNear = new ArrayList<>();
        adapter = new EncountersAdapter(mUsersNear, this);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_encounters_cards, container, false);

        mEncountersCards = v.findViewById(R.id.encountersCards_root);

        mCardView = v.findViewById(R.id.parent_layout);

        mEmptyView = v.findViewById(R.id.empty_view);
        mEmptyLayout= v.findViewById(R.id.empty_layout);
        mLoadingLayout= v.findViewById(R.id.shimmer_view_container);


        mErrorImage = v.findViewById(R.id.image);
        mErrorTitle = v.findViewById(R.id.title);
        mErrorDesc = v.findViewById(R.id.brief);

        cardStackView = v.findViewById(R.id.encounters_stackView);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        Permiso.getInstance().setActivity(Objects.requireNonNull(getActivity()));

        if (getActivity() != null) {

            ((HomeActivity)getActivity()).initializeToolBar(R.drawable.ic_navigation_bar_liked_you_indicator, R.drawable.ic_navigation_bar_filter, HomeActivity.VIEW_TYPE_ENCOUNTERS);
        }

        setHasOptionsMenu(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        createLocationRequest();

        setLoading();


        manager = new CardStackLayoutManager(getContext(), this);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);


        updateLocation();

        loadAll(mCurrentUser);

        createAndLoadRewardedAd();

        return v;

    }


    public void likeUser(){

        isLikeCrush = false;
        isAdsUsed = false;

        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();

    }

    protected void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void skipUser(){

        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();

    }

    public void crushUser(User user){

        if (Config.isCrushCreditNeeded){

            bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(R.layout.include_crush);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

            CircleImageView avatar = bottomSheetDialog.findViewById(R.id.avatar_img);
            Button credits = bottomSheetDialog.findViewById(R.id.btn_credit);
            Button ads = bottomSheetDialog.findViewById(R.id.btn_ads);
            LinearLayout adsLyt = bottomSheetDialog.findViewById(R.id.ads_lyt);

            assert avatar != null;
            assert credits != null;
            assert adsLyt != null;
            assert ads != null;

            QuickHelp.getAvatars(user, avatar);

            credits.setText(String.format(getString(R.string.ads_credits_amount), Config.CrushCreditNeeded));

            if (!Config.isCrushAdsEnabled)  adsLyt.setVisibility(View.GONE);
            if (!isAdsLoaded)  adsLyt.setVisibility(View.GONE);

            if (mCurrentUser.getCrushAdsLimitCounterToday() != null && QuickHelp.isToday(mCurrentUser.getCrushAdsLimitCounterToday()) && mCurrentUser.getCrushAdsCounter() >= Config.CrushAdsLimitPerDay){
                adsLyt.setVisibility(View.GONE);
            }

            credits.setOnClickListener(view -> {

                if (mCurrentUser.getCredits() >= Config.CrushCreditNeeded){

                    QuickHelp.showLoading(getActivity(), false);

                    mCurrentUser.removeCredit(Config.CrushCreditNeeded);
                    mCurrentUser.saveInBackground(e -> {

                        if (e == null){

                            QuickHelp.hideLoading(getActivity());

                            if (bottomSheetDialog != null && bottomSheetDialog.isShowing()){
                                bottomSheetDialog.hide();
                            }

                            likeCrushUser(false);

                        } else {

                            QuickHelp.hideLoading(getActivity());
                            QuickHelp.showNotification(getActivity(), getString(R.string.error_ocurred), true);
                        }
                    });

                } else {

                    QuickHelp.goToActivityCreditCrush(getActivity(), PaymentsActivity.class, user);
                }
            });

            ads.setOnClickListener(v -> {

                if (rewardedAd.isLoaded()) {

                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                            QuickHelp.hideLoading(getActivity());
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                            rewardedAd = createAndLoadRewardedAd();

                            if (EarnedReward){

                                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()){
                                    bottomSheetDialog.dismiss();
                                }

                                likeCrushUser(true);
                            }
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                            EarnedReward = true;
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError) {
                            super.onRewardedAdFailedToShow(adError);

                            EarnedReward = false;

                            QuickHelp.showNotification(getActivity(), getString(R.string.video_ads_failed_loaded), true);

                            QuickHelp.hideLoading(getActivity());
                        }
                    };

                    rewardedAd.show(getActivity(), adCallback);

                } else {

                    QuickHelp.hideLoading(getActivity());

                    QuickHelp.showNotification(getActivity(), getString(R.string.video_ads_not_loaded), true);
                    //Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            });

            if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()){
                bottomSheetDialog.show();
            }

        } else {

            likeCrushUser(false);
        }
    }

    private void likeCrushUser(boolean adsUsed){

        isLikeCrush = true;
        isAdsUsed = adsUsed;

        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Slow.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();

    }

    @Override
    public void onCardAppeared(View view, int position) {
        //Log.v("CardStackView", "onCardAppeared: d = " + position);

    }

    @Override
    public void onCardDisappeared(View view, int position) {
        //Log.v("CardStackView", "onCardAppeared: d = " + position);

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        //Log.v("CardStackView", "onCardDragging: d = " + direction.name() + ", r = " + ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {
        //Log.v("CardStackView", "onCardSwiped: p = " + manager.getTopPosition() + ", d = " + direction);

        User user = mUsersNear.get(manager.getTopPosition()-1);

        if (direction == Direction.Left){
            if (getActivity() != null) { ((HomeActivity)getActivity()).isUndoShow(true, manager, cardStackView);}

            EncountersModel.newMatch(mCurrentUser, user, false, true, e -> {
                if (e == null) {
                    //Log.d("CardStackView", "saveMatch");

                    queryForUnMatch(user);
                }
            });


        } else if (direction == Direction.Right){
            if (getActivity() != null) { ((HomeActivity)getActivity()).isUndoShow(false); }

            EncountersModel.newMatch(mCurrentUser, user, true, false, e -> {
                if (e == null) {
                    //Log.d("CardStackView", "saveMatch");

                    if (isLikeCrush){
                        SendNotifications.SendPush(mCurrentUser, user, SendNotifications.PUSH_TYPE_CRUSH, null);

                        if (isAdsUsed){

                            if (mCurrentUser.getCrushAdsLimitCounterToday() != null && QuickHelp.isToday(mCurrentUser.getCrushAdsLimitCounterToday())){

                                mCurrentUser.setCrushAdsCounter();

                            } else {

                                mCurrentUser.setCrushAdsLimitCounterToday(new Date());
                                mCurrentUser.setCrushAdsCounterInit();
                            }

                            mCurrentUser.saveInBackground();
                        }

                    } else {

                        SendNotifications.SendPush(mCurrentUser, user, SendNotifications.PUSH_TYPE_LIKED_YOU, null);
                    }

                    QuickActions.queryForMatch(user, getActivity());

                }
            });

        }
    }

    private void queryForUnMatch(User user){

        ParseQuery<EncountersModel> modelParseQuery = EncountersModel.getQuery();
        modelParseQuery.whereEqualTo(EncountersModel.COL_FROM_USER, user);
        modelParseQuery.whereEqualTo(EncountersModel.COL_TO_USER, mCurrentUser);
        modelParseQuery.getFirstInBackground((encountersModel, e) -> {

            if (e == null){

                encountersModel.setSeen(true);
                encountersModel.saveEventually();
            }
        });

    }

    @Override
    public void onCardRewound() {
        //Log.v("CardStackView", "onCardRewound: " + manager.getTopPosition());

        if (getActivity() != null) {

            ((HomeActivity)getActivity()).isUndoShow(false);
        }

        User user = mUsersNear.get(manager.getTopPosition());

        ParseQuery<EncountersModel> modelParseQuery = EncountersModel.getQuery();
        modelParseQuery.whereEqualTo(EncountersModel.COL_FROM_USER, mCurrentUser);
        modelParseQuery.whereEqualTo(EncountersModel.COL_TO_USER, user);
        modelParseQuery.getFirstInBackground((encountersModel, e) -> {

            if (e == null){

                encountersModel.deleteEventually();
            }
        });
    }

    @Override
    public void onCardCanceled() {
        //Log.v("CardStackView", "onCardCanceled:" + manager.getTopPosition());
    }

    public void getIconLeft(Activity activity, User user){

        if (Config.isPremiumEnabled){

            if (user.isPremium()){

                QuickHelp.goToActivityWithNoClean(activity, LikedYouActivity.class);

            } else {

                QuickHelp.goToActivityWithNoClean(activity, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM, PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM_LIKED);
            }

        } else {

            QuickHelp.goToActivityWithNoClean(activity, LikedYouActivity.class);
        }

    }

    public void getIconRight(User user, Activity activity){

        sheetDialog = new TopSheetDialog(activity);
        sheetDialog.setContentView(R.layout.layout_encounters_filter);
        sheetDialog.getBehavior().setState(TopSheetBehavior.STATE_EXPANDED);
        sheetDialog.setCancelable(true);
        sheetDialog.show();
        sheetDialog.setCanceledOnTouchOutside(true);

        ProgressBar progressBar = sheetDialog.findViewById(R.id.loading);
        LinearLayout content = sheetDialog.findViewById(R.id.content);

        TextView ageRange = sheetDialog.findViewById(R.id.rangeBarLabel);
        RadioGroup gender = sheetDialog.findViewById(R.id.gender_radio_group);

        ImageView closeBtn = sheetDialog.findViewById(R.id.filter_decline);
        ImageView doneBtn = sheetDialog.findViewById(R.id.filter_confirm);
        RangeSeekBar rangeSeekBar = sheetDialog.findViewById(R.id.rangeBar);

        TextView distanceRange = sheetDialog.findViewById(R.id.rangeBarDistanceLabel);
        RangeSeekBar rangeSeekBarDistance = sheetDialog.findViewById(R.id.rangeBar_distance);

        RadioButton genderMale = sheetDialog.findViewById(R.id.radio_male);
        RadioButton genderFemale = sheetDialog.findViewById(R.id.radio_female);
        RadioButton genderBoth = sheetDialog.findViewById(R.id.radio_both);


        assert content != null;
        assert progressBar != null;
        content.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if (rangeSeekBar != null) {
            rangeSeekBar.setSeekBarMode(RangeSeekBar.SEEKBAR_MODE_RANGE);
            rangeSeekBar.setRange(Config.MinimumAgeToRegister, Config.MaximumAgeToRegister);
            rangeSeekBar.setValue(user.getPrefMinAge(), user.getPrefMaxAge());

            rangeSeekBar.setTickMarkMode(RangeSeekBar.TRICK_MARK_MODE_NUMBER);
            rangeSeekBar.setTickMarkGravity(RangeSeekBar.TRICK_MARK_GRAVITY_CENTER);
        }

        if (rangeSeekBarDistance != null) {
            rangeSeekBarDistance.setSeekBarMode(RangeSeekBar.SEEKBAR_MODE_SINGLE);
            rangeSeekBarDistance.setRange(Config.MinDistanceBetweenUsers, Config.MaxDistanceBetweenUsers);
            rangeSeekBarDistance.setValue(user.getPrefDistance());

            rangeSeekBarDistance.setTickMarkMode(RangeSeekBar.TRICK_MARK_MODE_NUMBER);
            rangeSeekBarDistance.setTickMarkGravity(RangeSeekBar.TRICK_MARK_GRAVITY_CENTER);
        }


        if (ageRange != null) {
            ageRange.setText(String.format(Locale.US, "%d - %d", user.getPrefMinAge(), user.getPrefMaxAge()));
        }


        if (distanceRange != null) {
            distanceRange.setText(String.format(Locale.US, Application.getInstance().getBaseContext().getString(R.string.distance_range_filter), user.getPrefDistance()));
        }


        assert genderMale != null;
        assert genderFemale != null;
        assert genderBoth != null;
        user.fetchInBackground();

        switch (user.getPrefGender()) {
            case User.GENDER_MALE:


                genderMale.setChecked(true);

                break;
            case User.GENDER_FEMALE:

                genderFemale.setChecked(true);

                break;
            case User.GENDER_BOTH:

                genderBoth.setChecked(true);
                break;
        }

        content.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);


        if (gender != null) {
            gender.setOnCheckedChangeListener((group, checkedId) -> {

                if (checkedId == R.id.radio_male){

                    user.setPrefGender(User.GENDER_MALE);

                } else if (checkedId == R.id.radio_female){

                    user.setPrefGender(User.GENDER_FEMALE);

                } else if (checkedId == R.id.radio_both){

                    user.setPrefGender(User.GENDER_BOTH);

                }
            });
        }

        assert rangeSeekBar != null;
        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                assert ageRange != null;
                ageRange.setText(String.format(Locale.US, "%d - %d", (int) leftValue, (int) rightValue));
                user.setPrefMinAge((int) leftValue);
                user.setPrefMaxAge((int) rightValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        assert distanceRange != null;
        assert rangeSeekBarDistance != null;

        rangeSeekBarDistance.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                distanceRange.setText(String.format(Locale.US, Application.getInstance().getBaseContext().getString(R.string.distance_range_filter), (int) leftValue));
                user.setPrefDistance((int) leftValue);
                ///user.setPrefMaxAge((int) rightValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

        });


        if (closeBtn != null) {
            closeBtn.setOnClickListener(v -> {

                if (sheetDialog.isShowing()){
                    sheetDialog.cancel();
                }
            });
        }

        if (doneBtn != null) {
            doneBtn.setOnClickListener(v -> {
                if (sheetDialog.isShowing()){
                    sheetDialog.cancel();
                }

                user.saveInBackground();
                user.fetchIfNeededInBackground();

                ((HomeActivity) activity).updateFilterEncounters();
            });
        }
    }

    private void loadAll(User mCurrentUser){


        if (mCurrentUser.getGeoPoint() != null){

            LoadFirst();
            updateLocation();

        } else {

            hideLoading(false);

            mErrorImage.setImageResource(R.drawable.ic_location);
            mErrorTitle.setText(R.string.loca_n_found);
            mErrorDesc.setText(R.string.trying_update);

            if (Build.VERSION.SDK_INT >= 23) {

                checkLastLocation();

            } else {

                getLastLocation();
            }
        }
    }

    private void refreshAll(){


        EncountersModel.userMatches(mCurrentUser, (matches, error) -> {
            if (error == null) {

                ArrayList<User> userArrayList = new ArrayList<>();
                userArrayList.add(mCurrentUser);

                ParseQuery<User> UsersNearQuery = User.getUserQuery();
                UsersNearQuery.whereNotEqualTo(User.COL_ID, mCurrentUser.getObjectId());
                UsersNearQuery.whereEqualTo(User.COL_HAS_GEO_POINT, true); // Only show users with location
                UsersNearQuery.whereExists(User.COL_PHOTOS); // Only show users with at lest one picture
                UsersNearQuery.whereExists(User.COL_BIRTHDATE); // Only show users with birthday
                UsersNearQuery.whereGreaterThanOrEqualTo(User.COL_AGE, mCurrentUser.getPrefMinAge()); // Minimum Age
                UsersNearQuery.whereLessThanOrEqualTo(User.COL_AGE, mCurrentUser.getPrefMaxAge()); // Maximum Age
                UsersNearQuery.whereNotEqualTo(User.PRIVACY_ALMOST_INVISIBLE, true);
                UsersNearQuery.whereNotContainedIn(User.BLOCKED_USERS, userArrayList);
                UsersNearQuery.whereNotEqualTo(User.USER_BLOCKED_STATUS, true);

                List<String> matchedUserIdList = new ArrayList<>();

                if (!Config.ShowBlockedUsersOnQuery &&  mCurrentUser.getBlockedUsers() != null && mCurrentUser.getBlockedUsers().size() > 0){

                    for (User user : mCurrentUser.getBlockedUsers()) {
                        if (!matchedUserIdList.contains(user.getObjectId())) {
                            matchedUserIdList.add(user.getObjectId());
                        }
                    }
                }

                UsersNearQuery.whereWithinKilometers(User.COL_GEO_POINT, mCurrentUser.getGeoPoint(), mCurrentUser.getPrefDistance());

                if (!mCurrentUser.getPrefGender().equals(User.GENDER_BOTH)){ // Gender
                    UsersNearQuery.whereEqualTo(User.COL_GENDER, mCurrentUser.getPrefGender());
                }

                UsersNearQuery.setLimit(Config.MaxUsersNearToShow);

                if (matches != null && matches.size() > 0) {
                    for (EncountersModel currentMatch : matches) {
                        if (!matchedUserIdList.contains(currentMatch.getToUser().getObjectId())) {
                            matchedUserIdList.add(currentMatch.getToUser().getObjectId());
                        }
                    }
                    UsersNearQuery.whereNotContainedIn(User.COL_ID, matchedUserIdList);
                }

                UsersNearQuery.findInBackground((usersNear, e) -> {

                    if (usersNear != null){

                        if (usersNear.size() > 0){

                            mUsersNear.clear();

                            mUsersNear.addAll(usersNear);

                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });
    }

    private void setLoading(){

        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mCardView.setVisibility(View.VISIBLE);
        mEncountersCards.setVisibility(View.GONE);

        mLoadingLayout.startShimmer();
    }

    private void hideLoading(boolean isLoaded){

        if (isLoaded){
            mEmptyLayout.setVisibility(View.GONE);
            mEncountersCards.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mEncountersCards.setVisibility(View.GONE);
        }

        mLoadingLayout.stopShimmer();
        mLoadingLayout.hideShimmer();
        mCardView.setVisibility(View.GONE);
    }

    private void LoadFirst(){

        setLoading();
        mUsersNear.clear();

        EncountersModel.userMatches(mCurrentUser, (matches, error) -> {
            if (error == null) {

                ArrayList<User> userArrayList = new ArrayList<>();
                userArrayList.add(mCurrentUser);

                ParseQuery<User> UsersNearQuery = User.getUserQuery();
                UsersNearQuery.whereNotEqualTo(User.COL_ID, mCurrentUser.getObjectId());
                UsersNearQuery.whereEqualTo(User.COL_HAS_GEO_POINT, true); // Only show users with location
                UsersNearQuery.whereExists(User.COL_PHOTOS); // Only show users with at lest one picture
                UsersNearQuery.whereExists(User.COL_BIRTHDATE); // Only show users with birthday
                UsersNearQuery.whereGreaterThanOrEqualTo(User.COL_AGE, mCurrentUser.getPrefMinAge()); // Minimum Age
                UsersNearQuery.whereLessThanOrEqualTo(User.COL_AGE, mCurrentUser.getPrefMaxAge()); // Maximum Age
                UsersNearQuery.whereNotEqualTo(User.PRIVACY_ALMOST_INVISIBLE, true);
                UsersNearQuery.whereNotContainedIn(User.BLOCKED_USERS, userArrayList);
                UsersNearQuery.whereNotEqualTo(User.USER_BLOCKED_STATUS, true);

                List<String> matchedUserIdList = new ArrayList<>();
                if (!Config.ShowBlockedUsersOnQuery && mCurrentUser.getBlockedUsers() != null && mCurrentUser.getBlockedUsers().size() > 0){

                    for (User user : mCurrentUser.getBlockedUsers()) {
                        if (!matchedUserIdList.contains(user.getObjectId())) {
                            matchedUserIdList.add(user.getObjectId());
                        }
                    }
                }

                UsersNearQuery.whereWithinKilometers(User.COL_GEO_POINT, mCurrentUser.getGeoPoint(), mCurrentUser.getPrefDistance());

                if (!mCurrentUser.getPrefGender().equals(User.GENDER_BOTH)){ // Gender
                    UsersNearQuery.whereEqualTo(User.COL_GENDER, mCurrentUser.getPrefGender());
                }

                UsersNearQuery.setLimit(Config.MaxUsersNearToShow);

                if (matches != null && matches.size() > 0) {
                    for (EncountersModel currentMatch : matches) {
                        if (!matchedUserIdList.contains(currentMatch.getToUser().getObjectId())) {
                            matchedUserIdList.add(currentMatch.getToUser().getObjectId());
                        }
                    }
                    UsersNearQuery.whereNotContainedIn(User.COL_ID, matchedUserIdList);
                }

                UsersNearQuery.findInBackground((usersNear, e) -> {

                    if (usersNear != null){

                        if (usersNear.size() > 0){

                            mUsersNear.clear();

                            mUsersNear.addAll(usersNear);

                            adapter.notifyDataSetChanged();

                            hideLoading(true);

                        } else {

                            hideLoading(false);

                            mUsersNear.clear();

                            mEmptyLayout.setVisibility(View.VISIBLE);

                            mEmptyView.setVisibility(View.VISIBLE);

                            mErrorImage.setImageResource(R.drawable.ic_encounters_achievement);
                            mErrorTitle.setText(R.string.you_dont_have_any_people_near);
                            mErrorDesc.setText(R.string.no_one_found_update);
                        }

                    } else {

                        hideLoading(false);

                        if (e.getCode() == ParseException.CONNECTION_FAILED){

                            mErrorImage.setImageResource(R.drawable.ic_blocker_large_connection_grey1);
                            mErrorTitle.setText(R.string.not_internet_connection);
                            mErrorDesc.setText(R.string.settings_no_inte);

                        } else if (e.getCode() == ParseException.INVALID_SESSION_TOKEN){

                            User.logOut();
                            QuickHelp.goToActivityAndFinish(getActivity(), WelcomeActivity.class);

                        } else {

                            mErrorImage.setImageResource(R.drawable.ic_close);
                            mErrorTitle.setText(R.string.error_ocurred);
                            mErrorDesc.setText(e.getLocalizedMessage());

                        }
                    }
                });

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkLastLocation(){

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    getLastLocation();

                } else {

                    // Enable Location button here.
                    Toast.makeText(getActivity(), Application.getInstance().getBaseContext().getString(R.string.msg_permission_required), Toast.LENGTH_LONG).show();

                    hideLoading(false);

                    mErrorImage.setImageResource(R.drawable.ic_location);
                    mErrorTitle.setText(R.string.permission_alert);
                    mErrorDesc.setText(R.string.permissin_in_location);
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(null,
                        Application.getInstance().getBaseContext().getString(R.string.msg_permission_required),
                        null, callback);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);


    }

    private void checkLocationSettings(){

        if (getActivity() == null) return;

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...

            getLastLocation();

        });

        task.addOnFailureListener(getActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    private void updateLocation(){

        if (!mCurrentUser.isLocationTypeNearby()){
            return;
        }

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    if (mFusedLocationClient == null){
                        return;
                    }

                    mFusedLocationClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                            mCurrentUser.setGeoPoint(parseGeoPoint);
                            mCurrentUser.setHasGeoPoint(true);

                            mCurrentUser.saveInBackground(e -> {

                                if (e == null){

                                    mCurrentUser.fetchIfNeededInBackground((GetCallback<User>) (user, e1) -> {

                                        // Location updated

                                    });

                                }
                            });

                        }
                    });


                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(null,
                        Application.getInstance().getBaseContext().getString(R.string.msg_permission_required),
                        null, callback);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);


    }


    @SuppressLint("MissingPermission")
    private void getLastLocation(){

        if (!mCurrentUser.isLocationTypeNearby()){
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                // Logic to handle location object

                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                mCurrentUser.setGeoPoint(parseGeoPoint);
                mCurrentUser.setHasGeoPoint(true);

                mCurrentUser.saveInBackground(e -> {

                    if (e == null){

                        mCurrentUser.fetchIfNeededInBackground((GetCallback<User>) (user, e1) -> LoadFirst());

                    } else {

                       hideLoading(false);

                       mErrorImage.setImageResource(R.drawable.ic_location);
                       mErrorTitle.setText(R.string.permission_alert);
                       mErrorDesc.setText(R.string.faailed_try_again);

                    }
                });

            } else checkLocationSettings();
        }).addOnFailureListener(e -> {

            checkLocationSettings();

        });
    }

    public RewardedAd createAndLoadRewardedAd() {
        rewardedAd = new RewardedAd(getActivity(), Constants.getRewardedAdsId());
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                isAdsLoaded = true;
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);

                isAdsLoaded = false;
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(Objects.requireNonNull(getActivity()));
        updateLocation();
        refreshAll();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean isInternetAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}