package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.live;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.UsersLiveAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.WelcomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.HomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.FollowModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveStreamModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.topsheet.TopSheetBehavior;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.topsheet.TopSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.greysonparrelli.permiso.Permiso;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LiveFragment extends Fragment {

    private ArrayList<LiveStreamModel> mUsersLive;
    private UsersLiveAdapter mUsersLiveAdapter;


    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView mRecyclerView;

    public User mCurrentUser;

    private TopSheetDialog sheetDialog;

    private LinearLayout mEmptyView, mEmptyLayout, mLoadingLayout;
    private TextView mErrorDesc, mErrorTitle;
    private ImageView mErrorImage;

    private int mTabPosition = 0;

    private ParseLiveQueryClient liveQueryClient;

    private ParseQuery<LiveStreamModel> liveStreamModelParseQuery = LiveStreamModel.getSteamingQuery();
    private SubscriptionHandling<LiveStreamModel> liveQueryStreamSubscription;

    public LiveFragment() {
        // Required empty public constructor
    }

    public static LiveFragment newInstance() {
        return new LiveFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle b) {
        super.onViewCreated(view, b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUsersLive = new ArrayList<>();
        mUsersLiveAdapter = new UsersLiveAdapter(getActivity(), mUsersLive);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_broadcast, container,false);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        tabLayout = v.findViewById(R.id.tab);

        swipeRefreshLayout =v.findViewById(R.id.streamingList_swipeRefresh);
        mRecyclerView = v.findViewById(R.id.streamingList_recycler);

        mEmptyView = v.findViewById(R.id.empty_view);
        mEmptyLayout= v.findViewById(R.id.empty_layout);
        mLoadingLayout= v.findViewById(R.id.loading);

        mErrorImage = v.findViewById(R.id.image);
        mErrorTitle = v.findViewById(R.id.title);
        mErrorDesc = v.findViewById(R.id.brief);

        AppCompatButton mLiveStreamBtn = v.findViewById(R.id.streamingList_goLiveButton);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.live_title_popular), 0, true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.live_title_nearby), 1);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.live_title_following), 2);

        if (getActivity()!= null){

            Permiso.getInstance().setActivity(getActivity());
        }

        if (getActivity() != null) {

            ((HomeActivity)getActivity()).initializeToolBar(R.drawable.ic_navigation_bar_wallet, R.drawable.ic_navigation_bar_filter, HomeActivity.VIEW_TYPE_STREAMING);
        }

        mLiveStreamBtn.setOnClickListener(v1 -> initNewStream());


        swipeRefreshLayout.setRefreshing(false);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3 , StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setAdapter(mUsersLiveAdapter);
        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);


        initiatePopular(true);

        return v;
    }

    private void initNewStream(){

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    QuickHelp.goToActivityWithNoClean(getActivity(), LiveActivity.class);

                } else {

                    QuickHelp.showToast(getActivity(), getActivity().getString(R.string.msg_permission_required), true);
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(null,
                        getString(R.string.msg_permission_required),
                        null, callback);
            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    private void initiatePopular(boolean isUnique){

        if (isUnique) mUsersLive.clear();

        if (isUnique) setLoading();

        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(mCurrentUser);

        ParseQuery<User> UsersNearQuery = User.getUserQuery();
        UsersNearQuery.whereNotEqualTo(User.COL_ID, mCurrentUser.getObjectId());
        UsersNearQuery.whereEqualTo(User.COL_HAS_GEO_POINT, true); // Only show users with location
        UsersNearQuery.whereExists(User.COL_PHOTOS); // Only show users with at lest one picture
        UsersNearQuery.whereGreaterThan(User.LIVE_STREAMS_COUNT, 0);
        UsersNearQuery.whereExists(User.COL_BIRTHDATE); // Only show users with birthday
        UsersNearQuery.whereNotEqualTo(User.PRIVACY_ALMOST_INVISIBLE, true);
        UsersNearQuery.whereNotContainedIn(User.BLOCKED_USERS, userArrayList);
        UsersNearQuery.orderByAscending(User.LIVE_STREAMS_COUNT);
        UsersNearQuery.whereNotEqualTo(User.USER_BLOCKED_STATUS, true);

        if (!mCurrentUser.getPrefGender().equals(User.GENDER_BOTH)){ // Gender
            UsersNearQuery.whereEqualTo(User.COL_GENDER, mCurrentUser.getPrefGender());
        }

        if (!Config.ShowBlockedUsersOnQuery && mCurrentUser.getBlockedUsers() != null && mCurrentUser.getBlockedUsers().size() > 0){

            List<String> blockedUserId = new ArrayList<>();

            for (User user : mCurrentUser.getBlockedUsers()) {
                if (!blockedUserId.contains(user.getObjectId())) {
                    blockedUserId.add(user.getObjectId());
                }
            }

            UsersNearQuery.whereNotContainedIn(User.COL_ID, blockedUserId);
        }

        ParseQuery<LiveStreamModel> liveStreamModelParseQuery = LiveStreamModel.getSteamingQuery();
        liveStreamModelParseQuery.whereMatchesQuery(LiveStreamModel.AUTHOR, UsersNearQuery);
        liveStreamModelParseQuery.whereEqualTo(LiveStreamModel.STREAMING, true);

        liveStreamModelParseQuery.include(LiveStreamModel.AUTHOR);
        liveStreamModelParseQuery.findInBackground((usersNear, e) -> {

            if (usersNear != null){

                if (usersNear.size() > 0){

                    mUsersLive.clear();
                    mUsersLive.addAll(usersNear);
                    mUsersLiveAdapter.notifyDataSetChanged();

                    hideLoading(true);

                } else {

                    hideLoading(false);

                    mUsersLive.clear();

                    mEmptyLayout.setVisibility(View.VISIBLE);

                    mLoadingLayout.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                    mErrorImage.setImageResource(R.drawable.ic_tabbar_live);
                    mErrorTitle.setText(R.string.you_dont_have_any_popular);
                    mErrorDesc.setText(R.string.you_dont_have_any_popular_explain);
                }

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);

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

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void initiateNearby(boolean isUnique){

        if (isUnique) mUsersLive.clear();

        if (isUnique) setLoading();

        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(mCurrentUser);

        ParseQuery<User> UsersNearQuery = User.getUserQuery();
        UsersNearQuery.whereNotEqualTo(User.COL_ID, mCurrentUser.getObjectId());
        UsersNearQuery.whereEqualTo(User.COL_HAS_GEO_POINT, true); // Only show users with location
        UsersNearQuery.whereExists(User.COL_PHOTOS); // Only show users with at lest one picture
        UsersNearQuery.whereExists(User.COL_BIRTHDATE); // Only show users with birthday
        UsersNearQuery.whereNotEqualTo(User.PRIVACY_ALMOST_INVISIBLE, true);
        UsersNearQuery.whereNotContainedIn(User.BLOCKED_USERS, userArrayList);
        UsersNearQuery.whereWithinKilometers(User.COL_GEO_POINT, mCurrentUser.getGeoPoint(), Config.DistanceBetweenUsersLive);
        UsersNearQuery.whereNotEqualTo(User.USER_BLOCKED_STATUS, true);

        if (!mCurrentUser.getPrefGender().equals(User.GENDER_BOTH)){ // Gender
            UsersNearQuery.whereEqualTo(User.COL_GENDER, mCurrentUser.getPrefGender());
        }

        if (!Config.ShowBlockedUsersOnQuery && mCurrentUser.getBlockedUsers() != null && mCurrentUser.getBlockedUsers().size() > 0){

            List<String> blockedUserId = new ArrayList<>();

            for (User user : mCurrentUser.getBlockedUsers()) {
                if (!blockedUserId.contains(user.getObjectId())) {
                    blockedUserId.add(user.getObjectId());
                }
            }

            UsersNearQuery.whereNotContainedIn(User.COL_ID, blockedUserId);
        }

        ParseQuery<LiveStreamModel> liveStreamModelParseQuery = LiveStreamModel.getSteamingQuery();
        liveStreamModelParseQuery.whereMatchesQuery(LiveStreamModel.AUTHOR, UsersNearQuery);
        liveStreamModelParseQuery.whereEqualTo(LiveStreamModel.STREAMING, true);
        //liveStreamModelParseQuery.orderByAscending(LiveStreamModel.KEY_CREATED_AT);

        liveStreamModelParseQuery.include(LiveStreamModel.AUTHOR);
        liveStreamModelParseQuery.findInBackground((usersNear, e) -> {

            if (usersNear != null){

                if (usersNear.size() > 0){

                    mUsersLive.clear();
                    mUsersLive.addAll(usersNear);
                    mUsersLiveAdapter.notifyDataSetChanged();

                    hideLoading(true);

                } else {

                    hideLoading(false);

                    mUsersLive.clear();

                    mEmptyLayout.setVisibility(View.VISIBLE);

                    mLoadingLayout.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                    mErrorImage.setImageResource(R.drawable.ic_tabbar_live);
                    mErrorTitle.setText(R.string.you_dont_have_any_nearby);
                    mErrorDesc.setText(R.string.you_dont_have_any_nearby_explain);
                }

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);

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

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void initiateFollowing(boolean isUnique){

        if (isUnique) mUsersLive.clear();

        if (isUnique) setLoading();

        // First, query for the friends whom the current user follows
        ParseQuery<FollowModel> followingActivitiesQuery = FollowModel.getQuery();
        followingActivitiesQuery.whereEqualTo(FollowModel.FROM_AUTHOR, mCurrentUser);

        // Get Users returned in the previous query
        ParseQuery<LiveStreamModel> liveStreamModelParseQuery = LiveStreamModel.getSteamingQuery();
        liveStreamModelParseQuery.whereMatchesKeyInQuery(LiveStreamModel.AUTHOR, FollowModel.TO_AUTHOR, followingActivitiesQuery);

        liveStreamModelParseQuery.whereEqualTo(LiveStreamModel.STREAMING, true);

        liveStreamModelParseQuery.include(LiveStreamModel.AUTHOR);
        liveStreamModelParseQuery.findInBackground((usersNear, e) -> {

            if (usersNear != null){

                if (usersNear.size() > 0){

                    mUsersLive.clear();
                    mUsersLive.addAll(usersNear);
                    mUsersLiveAdapter.notifyDataSetChanged();

                    hideLoading(true);

                } else {

                    hideLoading(false);

                    mUsersLive.clear();

                    mEmptyLayout.setVisibility(View.VISIBLE);

                    mLoadingLayout.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                    mErrorImage.setImageResource(R.drawable.ic_tabbar_live);
                    mErrorTitle.setText(R.string.you_dont_have_any_favorites);
                    mErrorDesc.setText(R.string.you_dont_have_any_favorites_explain);
                }

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);

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

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }


    private void setLoading(){

        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading(boolean isLoaded){

        if (isLoaded){
            mEmptyLayout.setVisibility(View.GONE);

        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mLoadingLayout.setVisibility(View.GONE);
    }

    public void getIconLeft(Activity activity){

        QuickHelp.goToActivityWithNoClean(activity, WalletActivity.class);
    }

    public void getIconRight(Activity activity, User user){

        sheetDialog = new TopSheetDialog(activity);
        sheetDialog.setContentView(R.layout.layout_live_filter);
        sheetDialog.getBehavior().setState(TopSheetBehavior.STATE_EXPANDED);
        sheetDialog.setCancelable(true);
        sheetDialog.show();
        sheetDialog.setCanceledOnTouchOutside(true);

        ProgressBar progressBar = sheetDialog.findViewById(R.id.loading);
        LinearLayout content = sheetDialog.findViewById(R.id.content);

        RadioGroup gender = sheetDialog.findViewById(R.id.gender_radio_group);

        ImageView closeBtn = sheetDialog.findViewById(R.id.filter_decline);
        ImageView doneBtn = sheetDialog.findViewById(R.id.filter_confirm);

        RadioButton genderMale = sheetDialog.findViewById(R.id.radio_male);
        RadioButton genderFemale = sheetDialog.findViewById(R.id.radio_female);
        RadioButton genderBoth = sheetDialog.findViewById(R.id.radio_both);

        assert content != null;
        assert progressBar != null;
        assert genderMale != null;
        assert genderFemale != null;
        assert genderBoth != null;

        content.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

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

                ((HomeActivity) activity).updateFilterLive();
            });
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0){
                    mTabPosition = 0;
                    initiatePopular(true);
                } else if (tab.getPosition() == 1){
                    mTabPosition = 1;
                    initiateNearby(true);
                } else if (tab.getPosition() == 2){
                    mTabPosition = 3;
                    initiateFollowing(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        unsubscribeToLiveQuery();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (getActivity()!= null){

            Permiso.getInstance().setActivity(getActivity());
        }


        if (mTabPosition == 0){
            initiatePopular(false);
        } else if (mTabPosition == 1){
            initiateNearby(false);
        } else if (mTabPosition == 2){
            initiateFollowing(false);
        }

        try {

            liveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(Config.LIVE_QUERY_URL));
            liveQueryClient.connectIfNeeded();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        setupLiveQuery();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private ParseLiveQueryClient getLiveQueryClient(){

        return liveQueryClient;
    }

    private void unsubscribeToLiveQuery() {

        getLiveQueryClient().unsubscribe(liveStreamModelParseQuery, liveQueryStreamSubscription);
    }

    private void setupLiveQuery() {

        liveQueryStreamSubscription = getLiveQueryClient().subscribe(liveStreamModelParseQuery);
        liveQueryStreamSubscription.handleEvent(SubscriptionHandling.Event.CREATE, (query, liveStream) -> {

            if (mTabPosition == 0){
                initiatePopular(false);
            } else if (mTabPosition == 1){
                initiateNearby(false);
            } else if (mTabPosition == 2){
                initiateFollowing(false);
            }


        }).handleEvent(SubscriptionHandling.Event.UPDATE, (query, liveStream) -> {


            if (mTabPosition == 0){
                initiatePopular(false);
            } else if (mTabPosition == 1){
                initiateNearby(false);
            } else if (mTabPosition == 2){
                initiateFollowing(false);
            }

        }).handleUnsubscribe((query) -> liveQueryStreamSubscription = null);

    }
}