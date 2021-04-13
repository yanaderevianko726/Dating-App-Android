package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.ConnectionsAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.ConnectionsSpotLightAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.HomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.encounters.LikedYouActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.popularity.PopularityActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.expansionpanel.ExpansionLayout;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.SharedPrefUtil;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class ConnectionsFragment extends Fragment {

    private ParseLiveQueryClient liveQueryClient;

    public User mCurrentUser;

    // Spotlight
    private ArrayList<ConnectionListModel> mConnectionSpotlight;
    private ConnectionsSpotLightAdapter mConnectionsSpotLightAdapter;
    private View mLineView;

    private ArrayList<ConnectionListModel> mConnectionsList;
    private ConnectionsAdapter mConnectionsAdapter;
    private RecyclerView mRecyclerViewConnections;

    private ExpansionLayout expansionLayout;

    private LinearLayout mChatsLayout, mOnlineLayout, mVistsLayout, mLikesLayout, mFavoritesLayout;

    private ImageView mIconHeader, mClearHeader, mIndicatorHeader;
    private TextView mCounterHeader, mTitleHeader;

    private TextView mCounterChats, mCounterFavorites;
    private ImageView mClearChats, mClearOnline, mClearVisits, mClearFavorites;

    private LinearLayout mEmptyView, mEmptyLayout, mLoadingLayout;
    private TextView mErrorDesc, mErrorTitle;
    private ImageView mErrorImage;

    private boolean hasHeaderIcon;
    private boolean clearHeader;
    private String lastTypeName = "";

    private SharedPrefUtil sharedPrefUtil;

    private int chatCounter, favoriteCounter;

    private ParseQuery<ConnectionListModel> messageQuery;
    private SubscriptionHandling<ConnectionListModel> liveQueryChatSubscription;

    private boolean isAllSelected = true, isChatSelected = false, isVisitsSelected = false, isFavoritesSelected = false;

    public ConnectionsFragment() {
        // Required empty public constructor
    }

    public static ConnectionsFragment newInstance() {
        return new ConnectionsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle b) {
        super.onViewCreated(view, b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConnectionSpotlight = new ArrayList<>();
        mConnectionsSpotLightAdapter = new ConnectionsSpotLightAdapter(getActivity(), mConnectionSpotlight);

        mConnectionsList = new ArrayList<>();
        mConnectionsAdapter = new ConnectionsAdapter(getActivity(), mConnectionsList);

        sharedPrefUtil = new SharedPrefUtil(getActivity());

        chatCounter = sharedPrefUtil.getInt(Constants.CONNECTION_COUNTER_MESSAGES, 0);
        favoriteCounter = sharedPrefUtil.getInt(Constants.CONNECTION_COUNTER_FAVORITES, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_connections, container, false);

        mCurrentUser  = (User) ParseUser.getCurrentUser();

        RecyclerView mRecyclerViewSpotLight = v.findViewById(R.id.spotlight_rv);

        mRecyclerViewConnections = v.findViewById(R.id.connections_rv);
        expansionLayout = v.findViewById(R.id.expansionLayout);
        mChatsLayout = v.findViewById(R.id.chats_layout);
        mOnlineLayout = v.findViewById(R.id.online_layout);
        mVistsLayout = v.findViewById(R.id.vitits_layout);
        mLikesLayout = v.findViewById(R.id.likes_layout);
        mFavoritesLayout = v.findViewById(R.id.favorites_layout);

        mIconHeader = v.findViewById(R.id.icon_header);
        mCounterHeader = v.findViewById(R.id.counter_txt);
        mTitleHeader = v.findViewById(R.id.connection_type);
        mIndicatorHeader = v.findViewById(R.id.headerIndicator);
        mClearHeader = v.findViewById(R.id.clear_header);

        mCounterChats = v.findViewById(R.id.chats_counter_txt);
        mCounterFavorites = v.findViewById(R.id.favorites_counter_txt);

        mClearChats = v.findViewById(R.id.clear_chats);
        mClearOnline = v.findViewById(R.id.clear_online);
        mClearVisits = v.findViewById(R.id.clear_visits);
        mClearFavorites = v.findViewById(R.id.clear_favorites);

        mEmptyView = v.findViewById(R.id.empty_view);
        mEmptyLayout= v.findViewById(R.id.empty_layout);
        mLoadingLayout= v.findViewById(R.id.loading);
        mErrorImage = v.findViewById(R.id.image);
        mErrorTitle = v.findViewById(R.id.title);
        mErrorDesc = v.findViewById(R.id.brief);
        mLineView = v.findViewById(R.id.line_view);

        mClearChats.setOnClickListener(mOnClearClickListener);
        mClearOnline.setOnClickListener(mOnClearClickListener);
        mClearVisits.setOnClickListener(mOnClearClickListener);
        mClearFavorites.setOnClickListener(mOnClearClickListener);
        mClearHeader.setOnClickListener(mOnClearClickListener);

        mOnlineLayout.setVisibility(View.GONE);


        if (getActivity() != null) {

            ((HomeActivity)getActivity()).initializeToolBar(QuickHelp.getPopularityLevelIndicator(mCurrentUser), 0, HomeActivity.VIEW_TYPE_CONNECTIONS);
        }

        setHasOptionsMenu(true);

        LinearLayoutManager layoutManagerSpotlight = new LinearLayoutManager(getActivity());
        layoutManagerSpotlight.setOrientation(RecyclerView.HORIZONTAL);

        mRecyclerViewSpotLight.setAdapter(mConnectionsSpotLightAdapter);
        mRecyclerViewSpotLight.setItemViewCacheSize(12);
        mRecyclerViewSpotLight.setHasFixedSize(true);
        mRecyclerViewSpotLight.setNestedScrollingEnabled(true);
        mRecyclerViewSpotLight.setBackgroundResource(R.color.white);
        mRecyclerViewSpotLight.setBackgroundColor(Color.WHITE);
        mRecyclerViewSpotLight.setLayoutManager(layoutManagerSpotlight);

        LinearLayoutManager layoutManagerConnections = new LinearLayoutManager(getActivity());
        layoutManagerConnections.setOrientation(RecyclerView.VERTICAL);

        mRecyclerViewConnections.setAdapter(mConnectionsAdapter);
        mRecyclerViewConnections.setItemViewCacheSize(12);
        mRecyclerViewConnections.setHasFixedSize(true);
        mRecyclerViewConnections.setNestedScrollingEnabled(true);
        mRecyclerViewConnections.setBackgroundResource(R.color.white);
        mRecyclerViewConnections.setBackgroundColor(Color.WHITE);
        mRecyclerViewConnections.setLayoutManager(layoutManagerConnections);

        sharedPrefUtil.saveInt(Constants.CONNECTION_COUNTER_MESSAGES, 0);
        sharedPrefUtil.saveInt(Constants.CONNECTION_COUNTER_FAVORITES, 0);

        InitializeConnections(true);

        initializeDropDown();

        return v;

    }

    private void setLoading(){

        if (isAdded()){
            mRecyclerViewConnections.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading(){

        if (isAdded()){

            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerViewConnections.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    private void hideLoading(String title, String description, int errorImage){

        if (isAdded()){

            mErrorTitle.setText(title);
            mErrorDesc.setText(description);
            mErrorImage.setImageResource(errorImage);

            mEmptyView.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
            mRecyclerViewConnections.setVisibility(View.GONE);
        }


    }

    private void initializeSpotlight(){

        // Get Matches
        ParseQuery<ConnectionListModel> messageFromQuery = ConnectionListModel.getConnectionsQuery();
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        //messageFromQuery.whereEqualTo(ConnectionListModel.COL_MESSAGE_TYPE, ConnectionListModel.MESSAGE_TYPE_MATCHED);

        ParseQuery<ConnectionListModel> messageToQuery = ConnectionListModel.getConnectionsQuery();
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        messageToQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        //messageToQuery.whereEqualTo(ConnectionListModel.COL_MESSAGE_TYPE, ConnectionListModel.MESSAGE_TYPE_MATCHED);

        ParseQuery<ConnectionListModel> parseQuery = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery));
        parseQuery.whereEqualTo(ConnectionListModel.COL_MESSAGE_TYPE, ConnectionListModel.MESSAGE_TYPE_MATCHED);

        parseQuery.orderByDescending(ConnectionListModel.COL_UPDATED_AT);

        parseQuery.whereExists(ConnectionListModel.COL_USER_FROM);
        parseQuery.whereExists(ConnectionListModel.COL_USER_TO);

        parseQuery.include(ConnectionListModel.COL_USER_FROM);
        parseQuery.include(ConnectionListModel.COL_USER_TO);
        parseQuery.include(ConnectionListModel.COL_MESSAGE);

        parseQuery.findInBackground((objects, e) -> {

            if (objects != null && objects.size() > 0){

                mConnectionSpotlight.clear();
                mConnectionSpotlight.addAll(objects);
                mConnectionsSpotLightAdapter.notifyDataSetChanged();

                mLineView.setVisibility(View.VISIBLE);

            } else {

                mLineView.setVisibility(View.GONE);

            }
        });
    }

    private View.OnClickListener mOnClearClickListener = v -> {

        ImageView imageView = ((ImageView) v);

        setCounter(chatCounter, favoriteCounter);

        hasHeaderIcon = false;
        clearHeader = false;

        mTitleHeader.setText(Application.getInstance().getBaseContext().getString(R.string.connection_all_connections));

        lastTypeName = mTitleHeader.getText().toString();

        if (imageView == mClearHeader){

            mClearChats.setVisibility(View.GONE);
            mClearOnline.setVisibility(View.GONE);
            mClearVisits.setVisibility(View.GONE);
            mClearFavorites.setVisibility(View.GONE);
            mClearHeader.setVisibility(View.GONE);

            mIconHeader.setVisibility(View.GONE);
            mCounterHeader.setVisibility(View.VISIBLE);

            mIndicatorHeader.setVisibility(View.VISIBLE);

        } else {

            imageView.setVisibility(View.GONE);
            mClearHeader.setVisibility(View.GONE);
            expansionLayout.toggle(true);
        }

        InitializeConnections(true);

    };

    private void setCounterHeader(int headerCounter){

        mCounterHeader.setText(String.valueOf(headerCounter));
    }

    private void setCounter(int chat, int favorite){

        mCounterChats.setText(String.valueOf(chat));
        mCounterFavorites.setText(String.valueOf(favorite));

        setCounterHeader(chat+favorite);
    }

    private void setHeaderIcon(boolean isVisible, boolean hasClear){

        if (isVisible){

            mIconHeader.setVisibility(View.VISIBLE);
            mCounterHeader.setVisibility(View.GONE);

        } else {

            mIconHeader.setVisibility(View.GONE);
            mCounterHeader.setVisibility(View.VISIBLE);
        }

        if (hasClear){

            mClearHeader.setVisibility(View.VISIBLE);
            mIndicatorHeader.setVisibility(View.GONE);

        } else {

            mIndicatorHeader.setVisibility(View.VISIBLE);
            mClearHeader.setVisibility(View.GONE);
        }

    }

    private void initializeDropDown(){

        setCounter(chatCounter, favoriteCounter);

        expansionLayout.addListener((expansionLayout, expanded) -> {

            if (expanded){

                mTitleHeader.setText(Application.getInstance().getBaseContext().getString(R.string.connection_all_connections));

                mIconHeader.setVisibility(View.GONE);
                mCounterHeader.setVisibility(View.GONE);
                mClearHeader.setVisibility(View.GONE);
                mIndicatorHeader.setVisibility(View.VISIBLE);

            } else {

                if (lastTypeName.isEmpty()){
                    mTitleHeader.setText(Application.getInstance().getBaseContext().getString(R.string.connection_all_connections));
                } else {
                    mTitleHeader.setText(lastTypeName);
                }


                setHeaderIcon(hasHeaderIcon, clearHeader);
            }
        });

        mChatsLayout.setOnClickListener(v -> {

            hasHeaderIcon = false;
            clearHeader = true;
            expansionLayout.toggle(true);
            mTitleHeader.setText(Application.getInstance().getBaseContext().getString(R.string.connection_chats));

            lastTypeName = mTitleHeader.getText().toString();

            mClearChats.setVisibility(View.VISIBLE);
            mClearOnline.setVisibility(View.GONE);
            mClearVisits.setVisibility(View.GONE);
            mClearFavorites.setVisibility(View.GONE);

            setCounterHeader(chatCounter);

            InitializeChat(true);

        });


        mOnlineLayout.setOnClickListener(v -> {

            hasHeaderIcon = true;
            clearHeader = true;
            mIconHeader.setImageResource(R.drawable.ic_connections_filter_online);
            expansionLayout.toggle(true);
            mTitleHeader.setText(Application.getInstance().getBaseContext().getString(R.string.connection_online));

            lastTypeName = mTitleHeader.getText().toString();

            mClearChats.setVisibility(View.GONE);
            mClearOnline.setVisibility(View.VISIBLE);
            mClearVisits.setVisibility(View.GONE);
            mClearFavorites.setVisibility(View.GONE);

            InitializeChatOnline();

        });

        mVistsLayout.setOnClickListener(v -> {

            hasHeaderIcon = true;
            clearHeader = true;
            mIconHeader.setImageResource(R.drawable.ic_navigation_bar_visible);
            expansionLayout.toggle(true);
            mTitleHeader.setText(Application.getInstance().getBaseContext().getString(R.string.connection_visits));

            lastTypeName = mTitleHeader.getText().toString();

            mClearChats.setVisibility(View.GONE);
            mClearOnline.setVisibility(View.GONE);
            mClearVisits.setVisibility(View.VISIBLE);
            mClearFavorites.setVisibility(View.GONE);

            InitializeVisits(true);

        });

        mLikesLayout.setOnClickListener(v -> {

            expansionLayout.toggle(true);

            if (Config.isPremiumEnabled){

                if (mCurrentUser.isPremium()){

                    QuickHelp.goToActivityWithNoClean(getActivity(), LikedYouActivity.class);

                } else {

                    QuickHelp.goToActivityWithNoClean(getActivity(), PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM, PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM_LIKED);
                }

            } else {

                QuickHelp.goToActivityWithNoClean(getActivity(), LikedYouActivity.class);
            }

        });

        mFavoritesLayout.setOnClickListener(v -> {

            hasHeaderIcon = false;
            clearHeader = true;
            expansionLayout.toggle(true);
            mTitleHeader.setText(Application.getInstance().getBaseContext().getString(R.string.connection_favorites));

            lastTypeName = mTitleHeader.getText().toString();

            mClearChats.setVisibility(View.GONE);
            mClearOnline.setVisibility(View.GONE);
            mClearVisits.setVisibility(View.GONE);
            mClearFavorites.setVisibility(View.VISIBLE);

            setCounterHeader(favoriteCounter);

            InitializeFavorites(true);

        });
    }

    private void getCounters(){

        ParseQuery<ConnectionListModel> unReadMessageQuery = ConnectionListModel.getConnectionsQuery();
        unReadMessageQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        unReadMessageQuery.whereEqualTo(ConnectionListModel.COL_READ, false);
        unReadMessageQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        unReadMessageQuery.countInBackground((count, e) -> {
            if (e == null) {
                sharedPrefUtil.saveInt(Constants.CONNECTION_COUNTER_MESSAGES,count);
                setCounter(count, sharedPrefUtil.getInt(Constants.CONNECTION_COUNTER_FAVORITES));
            }
        });


        ParseQuery<ConnectionListModel> unReadFavoriteQuery = ConnectionListModel.getConnectionsQuery();
        unReadFavoriteQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        unReadFavoriteQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_FAVORITE);
        unReadFavoriteQuery.countInBackground((count, e) -> {
            if (e == null) {
                sharedPrefUtil.saveInt(Constants.CONNECTION_COUNTER_FAVORITES, count);
                setCounter(sharedPrefUtil.getInt(Constants.CONNECTION_COUNTER_MESSAGES), count);
            }

        });


    }

    private void InitializeConnections(boolean isUnique){

        isAllSelected = true;
        isChatSelected = false;
        isVisitsSelected = false;
        isFavoritesSelected = false;

        if (isUnique){
            setLoading();
        }


        // Get Messages
        ParseQuery<ConnectionListModel> messageFromQuery = ConnectionListModel.getConnectionsQuery();
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        ParseQuery<ConnectionListModel> messageToQuery = ConnectionListModel.getConnectionsQuery();
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        messageToQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        // Get Favorites
        ParseQuery<ConnectionListModel> favoriteFromParseQuery = ConnectionListModel.getConnectionsQuery();
        favoriteFromParseQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);
        favoriteFromParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_FAVORITE);

        ParseQuery<ConnectionListModel> favoriteToParseQuery = ConnectionListModel.getConnectionsQuery();
        favoriteToParseQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        favoriteToParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_FAVORITE);


        // Get Visitors
        ParseQuery<ConnectionListModel> visitorModelParseQuery = ConnectionListModel.getConnectionsQuery();
        visitorModelParseQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        visitorModelParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_VISITOR);

        messageQuery = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery, favoriteFromParseQuery, favoriteToParseQuery, visitorModelParseQuery));

        messageQuery.orderByDescending(ConnectionListModel.COL_UPDATED_AT);

        messageQuery.include(ConnectionListModel.COL_USER_FROM);
        messageQuery.include(ConnectionListModel.COL_USER_TO);
        messageQuery.include(ConnectionListModel.COL_MESSAGE);
        messageQuery.include(ConnectionListModel.COL_CALLS);

        messageQuery.findInBackground((messages,e) -> {

            if (e == null) {

                if (messages.size() > 0){

                    mConnectionsList.clear();
                    mConnectionsList.addAll(messages);
                    mConnectionsAdapter.notifyDataSetChanged();

                    hideLoading();

                } else {

                    if (isUnique && isAdded()) hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_connection_yet), Application.getInstance().getBaseContext().getString(R.string.no_chat_yet_explain), R.drawable.ic_tabbar_mess);
                }

            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                if (isUnique && isAdded()) hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_connection_yet), Application.getInstance().getBaseContext().getString(R.string.no_chat_yet_explain), R.drawable.ic_tabbar_mess);

            } else if (e.getCode() == ParseException.CONNECTION_FAILED){

                if (isUnique && isAdded()) hideLoading(Application.getInstance().getBaseContext().getString(R.string.not_internet_connection), Application.getInstance().getBaseContext().getString(R.string.settings_no_inte), R.drawable.ic_blocker_large_connection_grey1);

            } else {

                if (isUnique && isAdded()) hideLoading(e.getLocalizedMessage(), Application.getInstance().getBaseContext().getString(R.string.error_ocurred), R.drawable.ic_close);
            }

        });
    }

    private void InitializeChat(boolean isUnique){

        isAllSelected = false;
        isChatSelected = true;
        isVisitsSelected = false;
        isFavoritesSelected = false;

        if (isUnique){
            setLoading();
        }

        ParseQuery<ConnectionListModel> messageFromQuery = ConnectionListModel.getConnectionsQuery();
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        ParseQuery<ConnectionListModel> messageToQuery = ConnectionListModel.getConnectionsQuery();
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        messageToQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        messageQuery = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery));

        messageQuery.whereEqualTo(ConnectionListModel.COL_MESSAGE_TYPE, ConnectionListModel.MESSAGE_TYPE_CHAT);

        messageQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        messageQuery.orderByDescending(ConnectionListModel.COL_UPDATED_AT);

        messageQuery.include(ConnectionListModel.COL_USER_FROM);
        messageQuery.include(ConnectionListModel.COL_USER_TO);
        messageQuery.include(ConnectionListModel.COL_MESSAGE);

        messageQuery.findInBackground((messages,e) -> {

            if (e == null) {

                if (messages.size() > 0){

                    mConnectionsList.clear();
                    mConnectionsList.addAll(messages);
                    mConnectionsAdapter.notifyDataSetChanged();

                    hideLoading();

                } else {

                    if (isUnique && isAdded()){
                        hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_chat_yet), Application.getInstance().getBaseContext().getString(R.string.no_chat_yet_explain), R.drawable.ic_tabbar_mess);
                    }
                }

            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                if (isUnique && isAdded()){
                    hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_chat_yet), Application.getInstance().getBaseContext().getString(R.string.no_chat_yet_explain), R.drawable.ic_tabbar_mess);
                }

            } else if (e.getCode() == ParseException.CONNECTION_FAILED){

                if (isUnique && isAdded()){
                    hideLoading(Application.getInstance().getBaseContext().getString(R.string.not_internet_connection), Application.getInstance().getBaseContext().getString(R.string.settings_no_inte), R.drawable.ic_blocker_large_connection_grey1);
                }

            } else {

                if (isUnique && isAdded()){
                    hideLoading(e.getLocalizedMessage(), Application.getInstance().getBaseContext().getString(R.string.error_ocurred), R.drawable.ic_close);
                }
            }

        });
    }

    private void InitializeChatOnline(){

        isAllSelected = false;
        isChatSelected = true;
        isVisitsSelected = false;
        isFavoritesSelected = false;

        setLoading();

        // Query for online users
        ParseQuery<User> UsersNearQuery = User.getUserQuery();
        UsersNearQuery.whereGreaterThanOrEqualTo(User.KEY_UPDATED_AT, QuickHelp.getMinutesToOnline());


        // Query for messages
        ParseQuery<ConnectionListModel> messageFromQuery = ConnectionListModel.getConnectionsQuery();
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        messageFromQuery.whereMatchesQuery(ConnectionListModel.COL_USER_TO, UsersNearQuery);

        ParseQuery<ConnectionListModel> messageToQuery = ConnectionListModel.getConnectionsQuery();
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        messageToQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        messageToQuery.whereMatchesQuery(ConnectionListModel.COL_USER_FROM, UsersNearQuery);

        messageQuery = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery));

        messageQuery.whereEqualTo(ConnectionListModel.COL_MESSAGE_TYPE, ConnectionListModel.MESSAGE_TYPE_CHAT);
        messageQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        messageQuery.orderByDescending(ConnectionListModel.COL_UPDATED_AT);
        messageQuery.include(ConnectionListModel.COL_USER_FROM);
        messageQuery.include(ConnectionListModel.COL_USER_TO);
        messageQuery.include(ConnectionListModel.COL_MESSAGE);

        messageQuery.findInBackground((messages,e) -> {

            if (e == null) {

                if (messages.size() > 0){

                    mConnectionsList.clear();
                    mConnectionsList.addAll(messages);
                    mConnectionsAdapter.notifyDataSetChanged();

                    hideLoading();

                } else {

                    hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_chat_yet), Application.getInstance().getBaseContext().getString(R.string.no_chat_yet_explain), R.drawable.ic_tabbar_mess);
                }

            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_chat_yet), Application.getInstance().getBaseContext().getString(R.string.no_chat_yet_explain), R.drawable.ic_tabbar_mess);

            } else if (e.getCode() == ParseException.CONNECTION_FAILED){

                hideLoading(Application.getInstance().getBaseContext().getString(R.string.not_internet_connection), Application.getInstance().getBaseContext().getString(R.string.settings_no_inte), R.drawable.ic_blocker_large_connection_grey1);

            } else {

                hideLoading(e.getLocalizedMessage(), Application.getInstance().getBaseContext().getString(R.string.error_ocurred), R.drawable.ic_close);
            }

        });
    }

    private void InitializeVisits(boolean isUnique){

        isAllSelected = false;
        isChatSelected = false;
        isVisitsSelected = true;
        isFavoritesSelected = false;

        if (isUnique){
            setLoading();
        }

        ParseQuery<ConnectionListModel> visitorModelParseQuery = ConnectionListModel.getConnectionsQuery();
        visitorModelParseQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        visitorModelParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_VISITOR);
        visitorModelParseQuery.include(ConnectionListModel.COL_USER_TO);
        visitorModelParseQuery.orderByDescending(ConnectionListModel.KEY_CREATED_AT);
        visitorModelParseQuery.findInBackground((visitors, e) -> {

            if (e == null){

                if (visitors.size() > 0){

                    mConnectionsList.clear();
                    mConnectionsList.addAll(visitors);
                    mConnectionsAdapter.notifyDataSetChanged();

                    hideLoading();

                } else {

                    if (isUnique){
                        hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_visit_yet), Application.getInstance().getBaseContext().getString(R.string.no_visit_yet_explain), R.drawable.ic_navigation_bar_visible);
                    }
                }

            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                if (isUnique){
                    hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_visit_yet), Application.getInstance().getBaseContext().getString(R.string.no_visit_yet_explain), R.drawable.ic_navigation_bar_visible);
                }
            } else if (e.getCode() == ParseException.CONNECTION_FAILED){

                if (isUnique){
                    hideLoading(Application.getInstance().getBaseContext().getString(R.string.not_internet_connection), Application.getInstance().getBaseContext().getString(R.string.settings_no_inte), R.drawable.ic_blocker_large_connection_grey1);
                }
            } else {

                if (isUnique){
                    hideLoading(e.getLocalizedMessage(), Application.getInstance().getBaseContext().getString(R.string.error_ocurred), R.drawable.ic_close);
                }
            }
        });

    }

    private void InitializeFavorites(boolean isUnique){


        isAllSelected = false;
        isChatSelected = false;
        isVisitsSelected = false;
        isFavoritesSelected = true;

        if (isUnique){
            setLoading();
        }

        ParseQuery<ConnectionListModel> favoriteFromParseQuery = ConnectionListModel.getConnectionsQuery();
        favoriteFromParseQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);
        favoriteFromParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_FAVORITE);

        ParseQuery<ConnectionListModel> favoriteToParseQuery = ConnectionListModel.getConnectionsQuery();
        favoriteToParseQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
        favoriteToParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_FAVORITE);

        ParseQuery<ConnectionListModel> query = ParseQuery.or(Arrays.asList(favoriteFromParseQuery, favoriteToParseQuery));
        query.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_FAVORITE);
        query.include(ConnectionListModel.COL_USER_TO);
        query.include(ConnectionListModel.COL_USER_FROM);
        query.orderByDescending(ConnectionListModel.KEY_CREATED_AT);

        query.findInBackground((favorites, e) -> {

            if (e == null){

                if (favorites.size() > 0){

                    mConnectionsList.clear();
                    mConnectionsList.addAll(favorites);
                    mConnectionsAdapter.notifyDataSetChanged();

                    hideLoading();

                } else {

                    if (isUnique){
                        hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_favorite_yet), Application.getInstance().getBaseContext().getString(R.string.no_favorite_yet_explain), R.drawable.ic_profile_favorite);
                    }

                }



            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                if (isUnique){
                    hideLoading(Application.getInstance().getBaseContext().getString(R.string.no_favorite_yet), Application.getInstance().getBaseContext().getString(R.string.no_favorite_yet_explain), R.drawable.ic_profile_favorite);
                }

            } else if (e.getCode() == ParseException.CONNECTION_FAILED){

                if (isUnique){
                    hideLoading(Application.getInstance().getBaseContext().getString(R.string.not_internet_connection), Application.getInstance().getBaseContext().getString(R.string.settings_no_inte), R.drawable.ic_blocker_large_connection_grey1);
                }
            } else {

                if (isUnique){
                    hideLoading(e.getLocalizedMessage(), Application.getInstance().getBaseContext().getString(R.string.error_ocurred), R.drawable.ic_close);
                }
            }
        });
    }

    public void getIconLeft(Activity activity){

        QuickHelp.goToActivityWithNoClean(activity, PopularityActivity.class);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause(){
        super.onPause();

        unsubscribeToLiveQuery();
    }




    @Override
    public void onResume() {
        super.onResume();

        initializeSpotlight();
        getCounters();

        try {

            liveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(Config.LIVE_QUERY_URL));
            liveQueryClient.connectIfNeeded();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        setupLiveQuery();

        if (isAllSelected){

            InitializeConnections(false);

        } else if (isChatSelected){

            InitializeChat(false);

        } else if (isVisitsSelected){

            InitializeVisits(false);

        } else if (isFavoritesSelected){

            InitializeFavorites(false);
        }
    }

    private void setupLiveQuery() {

        ParseQuery<ConnectionListModel> messageToQuery = ConnectionListModel.getConnectionsQuery();
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);

        ParseQuery<ConnectionListModel> messageFromQuery = ConnectionListModel.getConnectionsQuery();
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);

        ParseQuery<ConnectionListModel> parseQuery  = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery));

        liveQueryChatSubscription = getLiveQueryClient().subscribe(parseQuery);
        liveQueryChatSubscription.handleEvent(SubscriptionHandling.Event.CREATE, (query, connection) -> {

            if (isAllSelected){

                InitializeConnections(false);

                //mConnectionsList.clear();
                mConnectionsList.add(connection);
                mConnectionsAdapter.notifyItemInserted(0);

            } else if (isChatSelected){

                //mConnectionsList.clear();
                mConnectionsList.add(connection);
                mConnectionsAdapter.notifyItemInserted(0);

                InitializeChat(false);
            }

        }).handleEvent(SubscriptionHandling.Event.UPDATE, (query, message) -> {

            if (isAllSelected){

                InitializeConnections(false);

            } else if (isChatSelected){

                InitializeChat(false);
            }

        }).handleUnsubscribe((query) -> liveQueryChatSubscription = null);

    }

    private void unsubscribeToLiveQuery() {

        getLiveQueryClient().unsubscribe(messageQuery, liveQueryChatSubscription);
    }

    private ParseLiveQueryClient getLiveQueryClient (){
        return liveQueryClient;
    }
}