package com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.NestedScrollView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.calls.VoiceCallActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.ConstantApp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.calls.CallActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.EncountersModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.MessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.centersheet.CenterSheetBehavior;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.centersheet.CenterSheetDialog;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.flowlayout.FlowLayout;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.shimmer.ShimmerFrameLayout;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class QuickActions {

    private static final String EXTRA_COMPATIBLE = "COMPATIBLE";
    private static final String EXTRA_UN_COMPATIBLE = "UN_COMPATIBLE";
    private static final String EXTRA_HEIGHT = "HEIGHT";

    private static void setVisitor(User fromUser, User toUser){

        if (!toUser.getPrivacyCloakedInvisibility()){

            ParseQuery<ConnectionListModel> visitorModelParseQuery = ConnectionListModel.getConnectionsQuery();
            visitorModelParseQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, fromUser);
            visitorModelParseQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, toUser);
            visitorModelParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_VISITOR);
            visitorModelParseQuery.include(ConnectionListModel.COL_USER_TO);
            visitorModelParseQuery.getFirstInBackground((visitorModel, e) -> {

                if (e == null){

                    visitorModel.setUserFrom(fromUser);
                    visitorModel.setUserTo(toUser);
                    visitorModel.saveInBackground(e1 -> {

                        if (e1 == null){

                            SendNotifications.SendPush(fromUser, toUser, SendNotifications.PUSH_TYPE_VISITOR, null);
                        }
                    });

                } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND){

                    ConnectionListModel visitor = new ConnectionListModel();
                    visitor.setUserFrom(fromUser);
                    visitor.setUserTo(toUser);
                    visitor.setConnectionType(ConnectionListModel.CONNECTION_TYPE_VISITOR);
                    visitor.saveInBackground(e12 -> {

                        if (e12 == null){

                            SendNotifications.SendPush(fromUser, toUser, SendNotifications.PUSH_TYPE_VISITOR, null);
                        }
                    });
                }
            });
        }
    }

    public static void showProfile(Activity activity, User user, boolean isOwnProfile){

        User mCurrentUser = (User) ParseUser.getCurrentUser();

        CenterSheetDialog sheetDialog = new CenterSheetDialog(activity);
        sheetDialog.setContentView(R.layout.item_profile_viewer);
        sheetDialog.show();
        sheetDialog.setCanceledOnTouchOutside(true);
        sheetDialog.getBehavior().setCenterSheetCallback(new CenterSheetBehavior.CenterSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == CenterSheetBehavior.STATE_COLLAPSED){
                    if (sheetDialog.isShowing()){
                        sheetDialog.cancel();
                    }
                } else if (newState == CenterSheetBehavior.STATE_HIDDEN){
                    sheetDialog.getBehavior().setState(CenterSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ShimmerFrameLayout shimmerFrameLayout = sheetDialog.findViewById(R.id.shimmer_view_container);
        NestedScrollView profileScrollView = sheetDialog.findViewById(R.id.profile_scroll_view);
        RelativeLayout profileBasicInfo = sheetDialog.findViewById(R.id.profile_basic_info);

        AppCompatButton favorite = sheetDialog.findViewById(R.id.addToFavorites);
        AppCompatButton blockReport = sheetDialog.findViewById(R.id.profile_section_block_report_button);

        LinearLayout votePanelLayout = sheetDialog.findViewById(R.id.vote_panel);
        LinearLayout nameSectionLayout = sheetDialog.findViewById(R.id.view_name_section);
        View viewPlaceOrder = sheetDialog.findViewById(R.id.view_place_order);

        LinearLayout rootView = sheetDialog.findViewById(R.id.root_view);
        View chat = sheetDialog.findViewById(R.id.external_chat_input_view);

        AppCompatEditText editText = sheetDialog.findViewById(R.id.external_chat_input_text);
        ImageView sendMessage = sheetDialog.findViewById(R.id.send_message);

        ImageView userPhoto = sheetDialog.findViewById(R.id.profilePhoto);
        TextView nameAndAge = sheetDialog.findViewById(R.id.nameAndAge);
        TextView userCity = sheetDialog.findViewById(R.id.location);
        ImageView chatAndAddBtn = sheetDialog.findViewById(R.id.dislikeBtn);
        ImageView likeAndEditBtn = sheetDialog.findViewById(R.id.likeBtn);

        TextView aboutMe = sheetDialog.findViewById(R.id.profile_about_me_text);
        FlowLayout flowLayout = sheetDialog.findViewById(R.id.profile_section_about_me_badges_container);
        TextView location = sheetDialog.findViewById(R.id.profile_about_me_location);
        TextView whatIWant = sheetDialog.findViewById(R.id.profile_about_me_what_i_want);

        assert userCity != null;
        assert nameAndAge != null;
        assert userPhoto != null;
        assert rootView != null;
        assert chat != null;
        assert shimmerFrameLayout != null;
        assert chatAndAddBtn != null;
        assert likeAndEditBtn != null;
        assert favorite != null;
        assert blockReport != null;
        assert whatIWant != null;
        assert location != null;
        assert profileScrollView != null;
        assert votePanelLayout != null;
        assert viewPlaceOrder != null;
        assert nameSectionLayout != null;

        assert editText != null;
        assert sendMessage != null;

        profileScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {

            View view = profileScrollView.getChildAt(profileScrollView.getChildCount() -1);

            int scrollY = profileScrollView.getScrollY();
            int bottomScroll = (view.getBottom() - (profileScrollView.getHeight() + profileScrollView.getScrollY()));
            int oldScrollY = 0;

            if (scrollY > oldScrollY){

                viewPlaceOrder.setVisibility(View.GONE);
                nameSectionLayout.setVisibility(View.GONE);
                votePanelLayout.setVisibility(View.GONE);
            }

            if (scrollY == oldScrollY){

                viewPlaceOrder.setVisibility(View.VISIBLE);
                nameSectionLayout.setVisibility(View.VISIBLE);
                votePanelLayout.setVisibility(View.VISIBLE);
            }

            if (bottomScroll == oldScrollY){

                viewPlaceOrder.setVisibility(View.GONE);
                nameSectionLayout.setVisibility(View.GONE);
                votePanelLayout.setVisibility(View.GONE);
            }


        });

        QuickHelp.setMargin(activity, isOwnProfile, rootView, chat, shimmerFrameLayout, profileScrollView, profileBasicInfo, chatAndAddBtn, likeAndEditBtn);

        setProfileInfo(user, aboutMe, whatIWant, location);
        prepareFlowItems(activity, user, flowLayout);

        favorite.setCompoundDrawablePadding(QuickHelp.convertDpToPixel(4));
        favorite.setGravity(Gravity.CENTER);
        favorite.setPadding(QuickHelp.convertDpToPixel(10), 0, 0, 0);
        QuickHelp.setDrawableTint(favorite, R.color.colorPrimary);

        // Set Profile data
        QuickHelp.getEncountersAvatars(user, userPhoto);

        if (user.getBirthDate() != null){
            nameAndAge.setText(String.format("%s, %s", user.getColFullName(), QuickHelp.getAgeFromDate(user.getBirthDate())));
        } else {
            nameAndAge.setText(user.getColFullName());
        }

        userCity.setText(QuickHelp.getOnlyCityFromLocation(user));

        userPhoto.setOnClickListener(v1 -> {

            if (user.getProfilePhotos().size() > 0){
                // Go to user profile
                QuickHelp.goToActivityPhotoViewer(activity, user, user.getAvatarPhotoPosition());
            }

        });


        if (!isOwnProfile){

            // Loading
            QuickHelp.showOtherProfile(true, false, false, rootView, chat, shimmerFrameLayout, profileScrollView, profileBasicInfo);


            ConnectionListModel.queryFavorite(user, new ConnectionListModel.QueryFavoriteListener() {
                @Override
                public void onQueryFavorited(ConnectionListModel favoriteModel) {
                    favorite.setText(activity.getString(R.string.favorited));
                    favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_profile_favorite_added, 0,0,0);

                    favorite.setOnClickListener(v -> {

                        favorite.setText(activity.getString(R.string.add_to_favorites));
                        favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_profile_favorite, 0,0,0);

                        favoriteModel.deleteEventually();
                    });
                }

                @Override
                public void onQueryUnFavorited() {

                    favorite.setText(activity.getString(R.string.add_to_favorites));
                    favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_profile_favorite, 0,0,0);

                    favorite.setOnClickListener(v -> {

                        favorite.setText(activity.getString(R.string.favorited));
                        favorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_profile_favorite_added, 0,0,0);

                        ConnectionListModel favoriteModel = new ConnectionListModel();
                        favoriteModel.setUserFrom(mCurrentUser);
                        favoriteModel.setUserTo(user);
                        favoriteModel.setConnectionType(ConnectionListModel.CONNECTION_TYPE_FAVORITE);
                        favoriteModel.saveInBackground(e -> {

                            if (e == null){
                                SendNotifications.SendPush(mCurrentUser, user, SendNotifications.PUSH_TYPE_FAVORITED_YOU, null);
                            }

                        });
                    });
                }

                @Override
                public void onQueryFavoriteError(ParseException error) {

                }
            });

            EncountersModel.queryMatchSingleUser(user, new EncountersModel.QueryMatchProfileListener() {
                @Override
                public void onQueryMatchSuccess(boolean hasLiked) {
                    // Loaded

                    List<User> arrayList = new ArrayList<>();
                    arrayList.add(user);

                    if (mCurrentUser.getBlockedUsers() != null && mCurrentUser.getBlockedUsers().containsAll(arrayList)){

                        likeAndEditBtn.setVisibility(View.INVISIBLE);
                        chatAndAddBtn.setVisibility(View.INVISIBLE);

                        favorite.setVisibility(View.GONE);
                        blockReport.setText(activity.getString(R.string.unblock));

                        blockReport.setOnClickListener(v -> {

                            mCurrentUser.removeBlockedUser(arrayList);
                            mCurrentUser.saveEventually();

                            if (hasLiked){
                                likeAndEditBtn.setVisibility(View.GONE);
                            } else {
                                likeAndEditBtn.setVisibility(View.VISIBLE);
                            }

                            favorite.setVisibility(View.VISIBLE);
                            chatAndAddBtn.setVisibility(View.VISIBLE);
                            blockReport.setText(activity.getString(R.string.report_user_title));
                        });

                    } else {

                        favorite.setVisibility(View.VISIBLE);
                        chatAndAddBtn.setVisibility(View.VISIBLE);

                        if (hasLiked){
                            likeAndEditBtn.setVisibility(View.GONE);
                        } else {
                            likeAndEditBtn.setVisibility(View.VISIBLE);
                        }

                        blockReport.setText(activity.getString(R.string.report_user_title));

                        blockReport.setOnClickListener(v -> {
                            sheetDialog.cancel();
                            QuickHelp.goToActivityBlockOrReport(activity, user);
                        });
                    }

                    likeAndEditBtn.setOnClickListener(v -> {

                        EncountersModel.newMatch(mCurrentUser, user, true, false, e -> {
                            if (e == null) {
                                Log.d("CardStackView", "saveMatch");

                                SendNotifications.SendPush(mCurrentUser, user, SendNotifications.PUSH_TYPE_LIKED_YOU, null);

                                queryForMatch(user, activity);

                            } else {
                                Log.d("CardStackView:saveMatch", e.toString());
                            }
                        });

                        likeAndEditBtn.setVisibility(View.GONE);
                    });

                    chatAndAddBtn.setOnClickListener(v -> {

                        sheetDialog.cancel();
                        QuickHelp.goToActivityChat(activity, user);
                    });

                    ConnectionListModel.queryUserConversation(user, new ConnectionListModel.QueryUserConversationListener() {
                        @Override
                        public void onQueryExist(boolean exist) {

                            QuickHelp.showOtherProfile(false, true, !exist, rootView, chat, shimmerFrameLayout, profileScrollView, profileBasicInfo);
                        }

                        @Override
                        public void onQueryError(ParseException error) {

                            QuickHelp.showOtherProfile(false, true, false, rootView, chat, shimmerFrameLayout, profileScrollView, profileBasicInfo);
                        }
                    });
                }

                @Override
                public void onQueryMatchError(ParseException error) {

                }
            });

            setVisitor(mCurrentUser, user);

            favorite.setVisibility(View.VISIBLE);
            blockReport.setVisibility(View.VISIBLE);

            sendMessage.setOnClickListener(v -> {

                if (editText.getText() != null && editText.getText().length() > 0){
                    QuickActions.sendMessage(activity, chat , mCurrentUser, user, editText.getText().toString());
                } else {
                    QuickHelp.showToast(activity, activity.getString(R.string.say_hello), true);
                }
            });


        } else {

            favorite.setVisibility(View.GONE);
            blockReport.setVisibility(View.GONE);
        }
    }

    private static void setProfileInfo(User user, TextView aboutMe, TextView whatIWant, TextView location){

        whatIWant.setText(QuickHelp.getWhatIWantProfile(user));
        location.setText(QuickHelp.getOnlyCityFromLocation(user));

        if (!user.getAboutMe().isEmpty()){
            aboutMe.setText(user.getAboutMe());
        } else if (!user.getDefaultAboutMe().isEmpty()){
            aboutMe.setText(user.getDefaultAboutMe());
        } else {
            aboutMe.setVisibility(View.GONE);
        }

    }

    private static void prepareFlowItems(Activity activity, User user, FlowLayout flowLayout){

        User mCurrentUser = (User) ParseUser.getCurrentUser();

        // Relationship
        if (!user.getRelationship().isEmpty()){

            if (user.getRelationship().equals(mCurrentUser.getRelationship())){
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_relationship_small, QuickHelp.getRelationShip(user), EXTRA_COMPATIBLE);
            } else {
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_relationship_small, QuickHelp.getRelationShip(user), EXTRA_UN_COMPATIBLE);
            }
        }

        // Sexuality
        if (!user.getSexuality().isEmpty()){

            if (user.getSexuality().equals(mCurrentUser.getSexuality())){
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_sexuality_small, QuickHelp.getSexuality(user), EXTRA_COMPATIBLE);
            } else {
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_sexuality_small, QuickHelp.getSexuality(user), EXTRA_UN_COMPATIBLE);
            }
        }

        // Height
        if (user.getHeight() > 0){

            if (user.getObjectId().equals(mCurrentUser.getObjectId())){

                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_appearance_height_small, QuickHelp.getHeight(user), EXTRA_HEIGHT);

            } else {

                if (user.getHeight() == (mCurrentUser.getHeight())){
                    setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_appearance_height_small, QuickHelp.getHeight(user), EXTRA_COMPATIBLE);
                } else {
                    setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_appearance_height_small, QuickHelp.getHeight(user), EXTRA_UN_COMPATIBLE);
                }

            }

        }

        // Body type
        if (!user.getBodyType().isEmpty()){

            if (user.getBodyType().equals(mCurrentUser.getBodyType())){
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_appearance_body_type_small, QuickHelp.getBodyType(user), EXTRA_COMPATIBLE);
            } else {
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_appearance_body_type_small, QuickHelp.getBodyType(user), EXTRA_UN_COMPATIBLE);
            }
        }

        // Living
        if (!user.getLiving().isEmpty()){

            if (user.getLiving().equals(mCurrentUser.getLiving())){
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_living_small, QuickHelp.getLiving(user), EXTRA_COMPATIBLE);
            } else {
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_living_small, QuickHelp.getLiving(user), EXTRA_UN_COMPATIBLE);
            }
        }

        // Kids
        if (!user.getKids().isEmpty()){

            if (user.getKids().equals(mCurrentUser.getKids())){
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_children_small, QuickHelp.getKidsProfile(user), EXTRA_COMPATIBLE);
            } else {
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_children_small, QuickHelp.getKidsProfile(user), EXTRA_UN_COMPATIBLE);
            }
        }

        // Smoking
        if (!user.getSmoking().isEmpty()){

            if (user.getSmoking().equals(mCurrentUser.getSmoking())){
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_smoking_small, QuickHelp.getSmokingProfile(user), EXTRA_COMPATIBLE);
            } else {
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_smoking_small, QuickHelp.getSmokingProfile(user), EXTRA_UN_COMPATIBLE);
            }
        }

        // Drinking
        if (!user.getDrinking().isEmpty()){

            if (user.getDrinking().equals(mCurrentUser.getDrinking())){
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_drinking_small, QuickHelp.getDrinkingProfile(user), EXTRA_COMPATIBLE);
            } else {
                setFlowItems(activity, flowLayout, R.drawable.ic_profile_badge_drinking_small, QuickHelp.getDrinkingProfile(user), EXTRA_UN_COMPATIBLE);
            }
        }

    }

    private static void setFlowItems(Activity activity, FlowLayout flowLayout, int icon, String text, String extra){

        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = QuickHelp.convertDpToPixel(3);
        params.leftMargin = QuickHelp.convertDpToPixel(3);
        params.topMargin = QuickHelp.convertDpToPixel(3);
        params.bottomMargin = QuickHelp.convertDpToPixel(3);


        TextView textView = new TextView(activity);
        textView.setLayoutParams(params);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(QuickHelp.convertDpToPixel(5));
        textView.setPadding(QuickHelp.convertDpToPixel(10), QuickHelp.convertDpToPixel(7), QuickHelp.convertDpToPixel(12), QuickHelp.convertDpToPixel(7));
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0,0,0);
        textView.setCompoundDrawablePadding(QuickHelp.convertDpToPixel(4));

        switch (extra) {
            case EXTRA_COMPATIBLE:

                textView.setBackgroundResource(R.drawable.bg_profile_flow_compatible);
                textView.setTextColor(activity.getResources().getColor(R.color.white));
                QuickHelp.setDrawableTint(textView, R.color.white);

                break;
            case EXTRA_UN_COMPATIBLE:
            case EXTRA_HEIGHT:

                textView.setBackgroundResource(R.drawable.bg_profile_flow_uncompatible);
                textView.setTextColor(activity.getResources().getColor(R.color.grey_60));
                QuickHelp.setDrawableTint(textView, R.color.grey_60);

                break;
        }


        flowLayout.addView(textView);
    }

    public static void queryForMatch(User user, Activity activity){

        EncountersModel.queryMatch(user, new EncountersModel.QueryMatchListener() {
            @Override
            public void onQueryMatchSuccess(EncountersModel encountersModel) {

                Log.v("CardStackView", "It's a Match");

                encountersModel.setSeen(true);
                encountersModel.saveEventually();

                sendMatchedMessage(User.getUser(), user);

                SendNotifications.SendPush(User.getUser(), user, SendNotifications.PUSH_TYPE_MATCHES, null);

                QuickHelp.goToActivityMutualActivity(activity, user);
            }

            @Override
            public void onQueryMatchError(ParseException error) {

            }
        });

    }

    private static void sendMatchedMessage(User fromUser, User toUser){

        ConnectionListModel messageListModel = new ConnectionListModel();

        messageListModel.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        messageListModel.setCount();
        messageListModel.setRead(false);

        messageListModel.setUserFrom(fromUser);
        messageListModel.setUserFromId(fromUser.getObjectId());

        messageListModel.setUserTo(toUser);
        messageListModel.setUserToId(toUser.getObjectId());

        messageListModel.setText(ConnectionListModel.MESSAGE_TYPE_MATCHED);
        messageListModel.setMessageType(ConnectionListModel.MESSAGE_TYPE_MATCHED);

        messageListModel.saveEventually();
    }

    public static void sendMessage(Activity activity, View chat, User fromUser, User toUser, String text){

        QuickHelp.showLoading(activity, false);

        MessageModel message = new MessageModel();

        // Message
        message.setMessage(text);

        // Sender
        message.setSenderAuthor(fromUser);
        message.setSenderAuthorId(fromUser.getObjectId());

        // Receiver
        message.setReceiverAuthor(toUser);
        message.setReceiverAuthorId(toUser.getObjectId());

        message.setRead(false);
        message.setMessageFile(false);

        message.saveInBackground(e -> {

            if (e == null){

                sendQuickMessage(activity, chat, message);

            } else {

                QuickHelp.hideLoading(activity);
                QuickHelp.showToast(activity, activity.getString(R.string.error_ocurred), true);
            }
        });
    }

    private static void sendQuickMessage(Activity activity, View chat, MessageModel message){

        ConnectionListModel messageList = new ConnectionListModel();

        messageList.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        messageList.setCount();
        messageList.setRead(false);

        messageList.setUserFrom(message.getSenderAuthor());
        messageList.setUserFromId(message.getSenderAuthor().getObjectId());

        messageList.setUserTo(message.getReceiverAuthor());
        messageList.setUserToId(message.getReceiverAuthor().getObjectId());

        messageList.setMessage(message);
        messageList.setMessageId(message.getObjectId());
        messageList.setText(message.getMessage());

        messageList.setMessageType(ConnectionListModel.MESSAGE_TYPE_CHAT);

        messageList.saveEventually(e -> {

            final Runnable runnable = () -> chat.setVisibility(View.GONE);
            if (e == null){

                activity.runOnUiThread(runnable);


                QuickHelp.hideLoading(activity);
                QuickHelp.showToast(activity, activity.getString(R.string.message_sent), false);

                SendNotifications.SendPush(message.getSenderAuthor(), message.getReceiverAuthor(), SendNotifications.PUSH_TYPE_MESSAGES, null);

            } else {

                activity.runOnUiThread(runnable);

                QuickHelp.hideLoading(activity);
                QuickHelp.showToast(activity, activity.getString(R.string.error_ocurred), true);
            }
        });
    }

    public static void CallUser(Activity activity, User user){

        activity.runOnUiThread(() -> {
            Intent intent = new Intent(activity, CallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_UID, User.getUser().getUid());
            intent.putExtra(ConstantApp.ACTION_KEY_SUBSCRIBER_OBJECT, user);
            intent.putExtra(ConstantApp.ACTION_KEY_SUBSCRIBER, user.getUid());
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, User.getUser().getObjectId()+user.getObjectId());
            intent.putExtra(ConstantApp.ACTION_KEY_MakeOrReceive, ConstantApp.CALL_OUT);
            activity.startActivity(intent);
        });
    }

    public static void makeVideoCall(Activity activity, User user){

        activity.runOnUiThread(() -> {
            Intent intent = new Intent(activity, CallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_UID, User.getUser().getUid());
            intent.putExtra(ConstantApp.ACTION_KEY_SUBSCRIBER_OBJECT, user);
            intent.putExtra(ConstantApp.ACTION_KEY_SUBSCRIBER, user.getUid());
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, User.getUser().getObjectId()+user.getObjectId());
            intent.putExtra(ConstantApp.ACTION_KEY_MakeOrReceive, ConstantApp.CALL_OUT);
            activity.startActivity(intent);
        });
    }

    public static void makeVoiceCall(Activity activity, User user){

        activity.runOnUiThread(() -> {
            Intent intent = new Intent(activity, VoiceCallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_UID, User.getUser().getUid());
            intent.putExtra(ConstantApp.ACTION_KEY_SUBSCRIBER_OBJECT, user);
            intent.putExtra(ConstantApp.ACTION_KEY_SUBSCRIBER, user.getUid());
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, User.getUser().getObjectId()+user.getObjectId());
            intent.putExtra(ConstantApp.ACTION_KEY_MakeOrReceive, ConstantApp.CALL_OUT);
            activity.startActivity(intent);
        });
    }

    public static void sendFakeMessage(User receiver){

        if (receiver.getColGender().equals(User.GENDER_FEMALE)) return;

        ParseConfig.getInBackground((config, e) -> {
            if (e == null) {
                Log.d("TAG", "Yay! Config was fetched from the server.");
            } else {
                Log.e("TAG", "Failed to fetch. Using Cached Config.");
                config = ParseConfig.getCurrentConfig();
            }

            // Get the message from config or fallback to default value
            String fakeSignupMessageUser = config.getString("female_signup_user_id", null);
            String fakeSignupMessage = config.getString("fake_signup_message", Config.defaultFakeMessage);

            if (fakeSignupMessageUser != null && !fakeSignupMessageUser.isEmpty()){
                User sender = ParseUser.createWithoutData(User.class, fakeSignupMessageUser);
                setFakeMessage(receiver, sender, fakeSignupMessage);
            }

            //Log.d("TAG", String.format("Welcome Messsage From Config = %s", welcomeMessage));


        });
    }

    public static void setFakeMessage(User receiver, User sender, String messageText){

        MessageModel message = new MessageModel();

        // Message
        message.setMessage(messageText);
        message.setMessageFile(false);

        // Sender
        message.setSenderAuthor(sender);
        message.setSenderAuthorId(sender.getObjectId());

        // Receiver
        message.setReceiverAuthor(receiver);
        message.setReceiverAuthorId(receiver.getObjectId());

        message.setRead(false);

        message.saveEventually(e -> {
            if (e == null){

                setMessageList(receiver,sender, message);
            }
        });
    }

    private static void setMessageList(User receiver, User sender, MessageModel message){

        ConnectionListModel messageList = new ConnectionListModel();

        messageList.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        messageList.setMessageType(ConnectionListModel.MESSAGE_TYPE_CHAT);
        if (message.getMessage() != null){ messageList.setText(message.getMessage()); }
        messageList.setUserFrom(sender);
        messageList.setUserTo(receiver);
        messageList.setUserFromId(sender.getObjectId());
        messageList.setUserToId(receiver.getObjectId());
        messageList.setRead(false);
        messageList.setCount();
        messageList.setMessage(message);
        messageList.setMessageId(message.getObjectId());
        messageList.saveInBackground();
    }

}
