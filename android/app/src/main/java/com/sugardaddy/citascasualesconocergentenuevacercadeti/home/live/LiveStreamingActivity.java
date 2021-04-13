package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.live;

import android.animation.Animator;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.GiftLiveAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.GiftLiveFooterAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.LiveMessageAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.FollowModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.GiftModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveMessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveStreamModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ReportModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.StatusBarUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.liveUtils.VideoGridContainer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

import static com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants.VIDEO_DIMENSIONS;
import static com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.GiftModel.SEND_GIFT_PARAM;

public class LiveStreamingActivity extends AppCompatActivity {

    public static final String LIVE_STREAMING_CHANNEL = "LIVE_STREAMING_CHANNEL";
    public static final String LIVE_STREAMER_OBJECT = "LIVE_STREAMER_OBJECT";
    public static final String LIVE_STREAMING_TYPE = "LIVE_STREAMING_TYPE";

    public static final int LIVE_STREAMING_STREAMER = 0x01;
    public static final int LIVE_STREAMING_VIEWER = 0x03;
    public static final int LIVE_STREAMING_UNKNOWN = -99;

    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    boolean isStreamingTimeInitialed = false;

    ParseLiveQueryClient liveQueryClient;

    public TextView mTimeText, mViewersText, mTokensText;

    private RtcEngine mRtcEngine;

    LiveStreamModel liveStreamModel;

    LinearLayoutManager mLayoutManager;

    private ParseQuery<LiveStreamModel> liveStreamModelParseQuery = LiveStreamModel.getSteamingQuery();
    private SubscriptionHandling<LiveStreamModel> liveQueryStreamSubscription;

    private ParseQuery<LiveMessageModel> liveMessageModelParseQuery = LiveMessageModel.getLiveMessageQuery();
    private SubscriptionHandling<LiveMessageModel> liveMessageQueryStreamSubscription;

    User mCurrentUser;
    LiveStreamModel mLiveStreamObject;
    int mStreamType;
    String mStreamChannel;

    FrameLayout mVideoControlLyt;

    ViewStub mHeaderViewStub, mFooterViewStub;

    ConstraintLayout mGetReadyLyt, mLoadingViewer, mLiveStreamEndedLyt, mLiveStreamTerminatedLyt;

    ImageView mAvatarBg, mCloseIn, mCloseOut, mCloseEnded, mCloseTerminated, mCloseStreaming;
    CircleImageView mAvatar;

    AppCompatButton mFinishStreaming;

    private VideoGridContainer mVideoLayout;

    boolean isStreamingStarted = false;
    boolean isFollowingUser = false;

    ImageView followingImg;

    ////////////// Comments /////////////

    BottomSheetDialog mMoreBottomSheet, mReportBottomSheet, mGiftBottomShet;

    ImageView mGiftBtn, mMoreBtn, mCameraBtn;
    EditText mMessageEditTxt;
    ImageButton mSendBtn;

    RecyclerView mRecyclerView;

    ArrayList<LiveMessageModel> mLiveMessages;
    LiveMessageAdapter mLiveMessageAdapter;

    LottieAnimationView mFullScreenGift;


    // Footer Gift
    RecyclerView mFooterRecyclerView;
    ArrayList<GiftModel> mGiftFooterList;
    GiftLiveFooterAdapter mGiftLiveFooterAdapter;

    final IRtcEngineEventHandler mRtcEngineEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onError(int errorCode) {
            super.onError(errorCode);
            sendMsg("-->onError<--" + errorCode);
        }

        @Override
        public void onWarning(int warn) {
            super.onWarning(warn);
            sendMsg("-->onWarning<--" + warn);

            if (warn == WarnCode.WARN_LOOKUP_CHANNEL_TIMEOUT) {

                runOnUiThread(() -> showWarning(true));

            } else if (warn == WarnCode.WARN_OPEN_CHANNEL_TIMEOUT) {

                runOnUiThread(() -> showWarning(true));
            }
        }

        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            sendMsg("-->onJoinChannelSuccess<--" + channel + "  -->uid<--" + uid);
            runOnUiThread(() -> {

                if (mStreamType == LIVE_STREAMING_STREAMER && uid == mCurrentUser.getUid()) {
                    startBroadcast();

                    setupLiveQuery(channel);

                } else if (mStreamType == LIVE_STREAMING_VIEWER && uid == mCurrentUser.getUid()) {
                    stopBroadcast();

                    initStreamerInfo();

                    if (mLiveStreamObject == null) {

                        setupLiveQuery(channel);
                        loadMessages();
                    }
                }

                isStreamingStarted = true;
            });
        }

        @Override
        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
            super.onFirstLocalVideoFrame(width, height, elapsed);
            sendMsg("-->onFirstLocalVideoFrame<--");

            if (!isStreamingTimeInitialed){
                setTimer(SystemClock.elapsedRealtime());
                isStreamingTimeInitialed = true;
            }

            liveStreamModel.setStartedAt(SystemClock.elapsedRealtime());
            liveStreamModel.saveInBackground();
        }

        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
            sendMsg("-->onFirstRemoteVideoDecoded<--");

            if (mLiveStreamObject != null) {

                mLiveStreamObject.setViewersUid(mCurrentUser.getUid());
                mLiveStreamObject.setViewersObjectId(mCurrentUser.getObjectId());
                mLiveStreamObject.setViewsLive(1);
                mLiveStreamObject.saveInBackground();

                sendMsg("-->updated mLiveStreamObject<--");
            }

            runOnUiThread(() -> renderRemoteUser(uid));
        }

        @Override
        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onRejoinChannelSuccess(channel, uid, elapsed);
            sendMsg(uid + " -->RejoinChannel<--");

            runOnUiThread(() -> showWarning(false));
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            super.onLeaveChannel(stats);
            sendMsg("-->leaveChannel<--");

            if (mLiveStreamObject != null) {

                if (mLiveStreamObject.getViewsLive() > 0){

                    mLiveStreamObject.setViewsLive(-1);
                    mLiveStreamObject.saveInBackground();
                }
            }
        }

        @Override
        public void onConnectionLost() {
            super.onConnectionLost();
            sendMsg("-->onConnectionLost<--");
        }

        @Override
        public void onStreamPublished(String url, final int error) {
            super.onStreamPublished(url, error);
            sendMsg("-->onStreamUrlPublished<--" + url + " -->error code<--" + error);
            runOnUiThread(() -> {

            });

        }

        @Override
        public void onStreamUnpublished(String url) {
            super.onStreamUnpublished(url);
            sendMsg("-->onStreamUrlUnpublished<--" + url);
        }

        @Override
        public void onTranscodingUpdated() {
            super.onTranscodingUpdated();
        }

        @Override
        public void onUserJoined(final int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            sendMsg("-->onUserJoined<--" + uid);
            runOnUiThread(() -> {

            });

        }


        @Override
        public void onUserOffline(final int uid, int reason) {
            super.onUserOffline(uid, reason);
            sendMsg("-->unPublishedByHost<--" + uid);
            unsubscribeToLiveQuery();
            runOnUiThread(() -> liveStreamTerminated());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.hideStatusBar(this);
        QuickHelp.setScreenOn(this, true);

        setContentView(R.layout.activity_live_streaming);

        mStreamType = getIntent().getIntExtra(LIVE_STREAMING_TYPE, LIVE_STREAMING_UNKNOWN);
        mStreamChannel = getIntent().getStringExtra(LIVE_STREAMING_CHANNEL);
        mLiveStreamObject = getIntent().getParcelableExtra(LIVE_STREAMER_OBJECT);

        mHeaderViewStub = findViewById(R.id.liveStreaming_headerStub);
        mFooterViewStub = findViewById(R.id.liveStreaming_footer);
        mVideoControlLyt = findViewById(R.id.liveStreaming_videoAndControlsContainer);
        mGetReadyLyt = findViewById(R.id.streamerReady_root);
        mLoadingViewer = findViewById(R.id.loading_joining);
        mLiveStreamEndedLyt = findViewById(R.id.streamer_final_screen_root);
        mLiveStreamTerminatedLyt = findViewById(R.id.viewer_final_screen_root);

        mFinishStreaming = findViewById(R.id.shareButton);

        mVideoLayout = findViewById(R.id.liveStreaming_videoContainer);

        mAvatarBg = findViewById(R.id.streamLoadingProgress_backgroundAvatar);
        mAvatar = findViewById(R.id.streamLoadingProgress_foregroundAvatar);

        mGiftBtn = findViewById(R.id.sendGiftButton);
        mMoreBtn = findViewById(R.id.more_btn);
        mCameraBtn = findViewById(R.id.camera_btn);
        mMessageEditTxt = findViewById(R.id.MessageWrapper);
        mSendBtn = findViewById(R.id.sendMessageButton);

        mFullScreenGift = findViewById(R.id.liveStreaming_fullscreenGift);
        mFullScreenGift.setVisibility(View.GONE);

        mSendBtn.setEnabled(false);

        QuickHelp.setupTextWatcher(mMessageEditTxt, mSendBtn);

        if (mStreamType == LIVE_STREAMING_STREAMER){

            mHeaderViewStub.setLayoutResource(R.layout.view_livestreaming_streamer_header);
            mHeaderViewStub.inflate();

            mGiftBtn.setVisibility(View.GONE);
            mMoreBtn.setVisibility(View.GONE);
            mCameraBtn.setVisibility(View.VISIBLE);

        } else if (mStreamType == LIVE_STREAMING_VIEWER){

            mHeaderViewStub.setLayoutResource(R.layout.view_livestreaming_viewer_header);
            mHeaderViewStub.inflate();

            mGiftBtn.setVisibility(View.VISIBLE);
            mMoreBtn.setVisibility(View.VISIBLE);
            mCameraBtn.setVisibility(View.GONE);

            mFooterViewStub.setLayoutResource(R.layout.view_stream_viewer_footer);
            mFooterViewStub.inflate();

            mFooterRecyclerView = findViewById(R.id.liveStreaming_footer_rv);

            initViewerFooter();
        }

        mCloseIn = findViewById(R.id.close_in);
        mCloseOut = findViewById(R.id.close_out);
        mCloseEnded = findViewById(R.id.close_ended);
        mCloseTerminated = findViewById(R.id.close_terminated);
        mCloseStreaming = findViewById(R.id.livestreamingHeader_close);

        mTimeText = findViewById(R.id.livestreamingHeader_status);
        mViewersText = findViewById(R.id.livestreamingHeader_viewers);
        mTokensText = findViewById(R.id.livestreamingHeader_tokens);

        mRecyclerView = findViewById(R.id.liveStreaming_messageList);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        mCloseIn.setOnClickListener(v -> onBackPressed());
        mCloseOut.setOnClickListener(v -> onBackPressed());
        mCloseEnded.setOnClickListener(v -> finish());
        mCloseTerminated.setOnClickListener(v -> finish());
        mFinishStreaming.setOnClickListener(v -> finish());
        mCloseStreaming.setOnClickListener(v -> onBackPressed());

        mSendBtn.setOnClickListener(v -> sendComment(mMessageEditTxt.getText().toString(), LiveMessageModel.MESSAGE_TYPE_COMMENT));

        try {
            mRtcEngine = RtcEngine.create(this, Config.AGORA_APP_ID, mRtcEngineEventHandler);
            mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();

            if (mStreamType == LIVE_STREAMING_STREAMER){

                mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            } else if (mStreamType == LIVE_STREAMING_VIEWER){
                mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        configVideo();

        init();

        mCameraBtn.setOnClickListener(v -> mRtcEngine.switchCamera());
        mMoreBtn.setOnClickListener(v -> showMoreOptions());
        mGiftBtn.setOnClickListener(v -> initGiftBottomSheet());

        mLiveMessages = new ArrayList<>();
        mLiveMessageAdapter = new LiveMessageAdapter(this, mLiveMessages);

        mRecyclerView.setAdapter(mLiveMessageAdapter);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setBackgroundResource(R.color.transparent);
        mRecyclerView.setBackgroundColor(Color.TRANSPARENT);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void sendComment(String text, String type) {

        LiveMessageModel liveMessage = new LiveMessageModel();

        liveMessage.setSenderAuthor(mCurrentUser);
        liveMessage.setSenderAuthorId(mCurrentUser.getObjectId());
        liveMessage.setMessage(text);
        liveMessage.setMessageType(type);

        if (mStreamType == LIVE_STREAMING_STREAMER){

            liveMessage.setLiveStream(liveStreamModel);
            liveMessage.setLiveStreamId(liveStreamModel.getObjectId());

        } else if (mStreamType == LIVE_STREAMING_VIEWER){

            liveMessage.setLiveStream(mLiveStreamObject);
            liveMessage.setLiveStreamId(mLiveStreamObject.getObjectId());
        }

        liveMessage.saveInBackground();

        mLiveMessages.add(0, liveMessage);
        // RecyclerView updates need to be run on the UI thread
        runOnUiThread(() -> {
            mLiveMessageAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
            mMessageEditTxt.setText(null);
        });
    }

    public void sendComment(GiftModel giftModel, String type) {

        LiveMessageModel liveMessage = new LiveMessageModel();

        liveMessage.setSenderAuthor(mCurrentUser);
        liveMessage.setSenderAuthorId(mCurrentUser.getObjectId());
        liveMessage.setLiveGift(giftModel);
        liveMessage.setMessageType(type);

        if (mStreamType == LIVE_STREAMING_STREAMER){

            liveMessage.setLiveStream(liveStreamModel);
            liveMessage.setLiveStreamId(liveStreamModel.getObjectId());

        } else if (mStreamType == LIVE_STREAMING_VIEWER){

            liveMessage.setLiveStream(mLiveStreamObject);
            liveMessage.setLiveStreamId(mLiveStreamObject.getObjectId());
        }

        liveMessage.saveInBackground();

        mLiveMessages.add(0, liveMessage);
        // RecyclerView updates need to be run on the UI thread
        runOnUiThread(() -> {
            mLiveMessageAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
            mMessageEditTxt.setText(null);
        });
    }

    public void initStreamerInfo() {

        CircleImageView streamerAvatar = findViewById(R.id.livestreamingHeader_streamerImage);
        TextView streamerName = findViewById(R.id.livestreamingHeader_name);
        TextView streamerAge = findViewById(R.id.livestreamingHeader_age);
        followingImg = findViewById(R.id.livestreamingHeader_followedStreamer);

        QuickHelp.getAvatars(mLiveStreamObject.getAuthor(), streamerAvatar);
        streamerName.setText(String.format("%s, ", mLiveStreamObject.getAuthor().getColFirstName()));

        if (mLiveStreamObject.getAuthor().getBirthDate() != null){
            streamerAge.setText(String.valueOf(QuickHelp.getAgeFromDate(mLiveStreamObject.getAuthor().getBirthDate())));
        }

        setTimer(mLiveStreamObject.getStartedAt());

        FollowModel.queryFollowSingleUser(mLiveStreamObject.getAuthor(), new FollowModel.QueryFollowListener() {
            @Override
            public void onQueryFollowSuccess(boolean isFollowing) {

                if (isFollowing){
                    followingImg.setVisibility(View.VISIBLE);
                    isFollowingUser  = true;
                } else {
                    followingImg.setVisibility(View.GONE);
                    isFollowingUser  = false;
                }
            }

            @Override
            public void onQueryFollowError(ParseException error) {
                followingImg.setVisibility(View.GONE);
                isFollowingUser  = false;
            }
        });
    }


    public void init() {

        isStreamingStarted = false;

        if (mStreamType == LIVE_STREAMING_STREAMER){

            mGetReadyLyt.setVisibility(View.VISIBLE);
            mLoadingViewer.setVisibility(View.GONE);
            mVideoControlLyt.setVisibility(View.GONE);
            mLiveStreamEndedLyt.setVisibility(View.GONE);
            mLiveStreamTerminatedLyt.setVisibility(View.GONE);

            createLiveStream();

        } else if (mStreamType == LIVE_STREAMING_VIEWER){

            QuickHelp.setAvatarFull(mAvatarBg, mLiveStreamObject.getAuthor());
            QuickHelp.getAvatars(mLiveStreamObject.getAuthor(), mAvatar);

            mLoadingViewer.setVisibility(View.VISIBLE);
            mGetReadyLyt.setVisibility(View.GONE);
            mVideoControlLyt.setVisibility(View.GONE);
            mLiveStreamEndedLyt.setVisibility(View.GONE);
            mLiveStreamTerminatedLyt.setVisibility(View.GONE);

            enterLiveStream(mStreamChannel);
        }
    }

    public void createLiveStream() {

        liveStreamModel = new LiveStreamModel();
        liveStreamModel.setAuthor(mCurrentUser);
        liveStreamModel.setStreaming(true);
        liveStreamModel.setViewsLive(0);
        liveStreamModel.saveEventually(e -> {

            if (e == null){

                // Initialize streaming and create channel
                mCurrentUser.addLiveSteamsCount(1);
                mCurrentUser.saveEventually();

                if (isFinishing()){

                    liveStreamModel.deleteEventually();

                } else {

                    enterLiveStream(liveStreamModel.getObjectId());

                    mStreamChannel = liveStreamModel.getObjectId();
                }

            } else {

                QuickHelp.showToast(LiveStreamingActivity.this, getString(R.string.error_ocurred), true);
                sendMsg("createLiveStream " + e.getLocalizedMessage());
                finish();
            }
        });

    }

    public void enterLiveStream(String channel) {
        mRtcEngine.joinChannel(null, channel, "", mCurrentUser.getUid());

    }

    public void finishStreaming(boolean isStreamer){

        isStreamingStarted = false;

        stopTimer();

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
        RtcEngine.destroy();
        mRtcEngine = null;

        if (isStreamer){

            unsubscribeToLiveQuery();

            liveStreamEnded();

        } else {

            finish();
        }
    }

    private void configVideo() {
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VIDEO_DIMENSIONS[3],
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        ));
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            liveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(Config.LIVE_QUERY_URL));
            liveQueryClient.connectIfNeeded();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (mStreamChannel != null && !mStreamChannel.isEmpty()) {
            setupLiveQuery(mStreamChannel);
            loadMessages();
        }
    }


    @Override
    public void onBackPressed() {

        if (mStreamType == LIVE_STREAMING_STREAMER){

            if (isStreamingStarted){

                setupFinishAsk(true);

            } else {

                finish();
            }

        } else if (mStreamType == LIVE_STREAMING_VIEWER){

            if (isStreamingStarted){

                setupFinishAsk(false);

            } else {

                finish();
            }
        }
    }

    public void setupFinishAsk(boolean isStreamer){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        if (isStreamer){

            alertDialog.setTitle(getString(R.string.live_alert_streamer_title));
            alertDialog.setMessage(getString(R.string.live_alert_streamer_message));

        } else {

            alertDialog.setTitle(getString(R.string.live_alert_viewer_title));
            alertDialog.setMessage(getString(R.string.live_alert_viewer_message));
        }

        alertDialog.setCancelable(true);

        alertDialog.setNegativeButton(getString(R.string.NO), (dialog, which) -> dialog.cancel());
        alertDialog.setPositiveButton(getString(R.string.YES), (dialog, which) -> finishStreaming(isStreamer));

        alertDialog.create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsubscribeToLiveQuery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mHandler.removeHandler(this);

        QuickHelp.setScreenOn(this, false);

        if (liveStreamModel != null){
            liveStreamModel.setStreaming(false);
            liveStreamModel.setStreamingTime(getTimer());
            liveStreamModel.saveEventually();
        }

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    protected SurfaceView prepareRtcVideo(int uid, boolean local) {
        SurfaceView surface = RtcEngine.CreateRendererView(getApplicationContext());
        if (local) {
            mRtcEngine.setupLocalVideo(new VideoCanvas(surface, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        } else {
            mRtcEngine.setupRemoteVideo(new VideoCanvas(surface, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        }
        return surface;
    }


    protected void removeRtcVideo(int uid, boolean local) {
        if (local) {
            mRtcEngine.setupLocalVideo(null);
        } else {
            mRtcEngine.setupRemoteVideo(new VideoCanvas(null, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        }
    }

    private void startBroadcast() {
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        SurfaceView surface = prepareRtcVideo(0, true);
        mVideoLayout.addUserVideoSurface(0, surface, true);

        mLoadingViewer.setVisibility(View.GONE);
        mGetReadyLyt.setVisibility(View.GONE);
        mLiveStreamEndedLyt.setVisibility(View.GONE);
        mLiveStreamTerminatedLyt.setVisibility(View.GONE);

        mVideoControlLyt.setVisibility(View.VISIBLE);
        mVideoLayout.setVisibility(View.VISIBLE);
    }

    private void stopBroadcast() {
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        removeRtcVideo(0, true);
        mVideoLayout.removeUserVideo(0, true);

        mLoadingViewer.setVisibility(View.GONE);
        mGetReadyLyt.setVisibility(View.GONE);
        mLiveStreamEndedLyt.setVisibility(View.GONE);
        mLiveStreamTerminatedLyt.setVisibility(View.GONE);

        mVideoControlLyt.setVisibility(View.VISIBLE);
        mVideoLayout.setVisibility(View.VISIBLE);
    }

    private void renderRemoteUser(int uid) {
        SurfaceView surface = prepareRtcVideo(uid, false);
        mVideoLayout.addUserVideoSurface(uid, surface, false);
    }

    private void liveStreamTerminated() {

        mLiveStreamTerminatedLyt.setVisibility(View.VISIBLE);

        mLoadingViewer.setVisibility(View.GONE);
        mGetReadyLyt.setVisibility(View.GONE);
        mLiveStreamEndedLyt.setVisibility(View.GONE);

        mVideoControlLyt.setVisibility(View.GONE);
        mVideoLayout.setVisibility(View.GONE);

        ImageView bgAvatar = findViewById(R.id.bg_avatar_terminated);
        AppCompatButton followingImg = findViewById(R.id.goFollow);
        TextView text = findViewById(R.id.shareStreamText_);

        QuickHelp.setAvatarFull(bgAvatar, mLiveStreamObject.getAuthor());

        followingImg.setText(String.format(getString(R.string.follow_user), mLiveStreamObject.getAuthor().getColFirstName()));
        text.setText(String.format(getString(R.string.live_stream_viewer_title), mLiveStreamObject.getAuthor().getColFirstName()));

        followingImg.setEnabled(false);

        FollowModel.queryFollowSingleUser(mLiveStreamObject.getAuthor(), new FollowModel.QueryFollowListener() {
            @Override
            public void onQueryFollowSuccess(boolean isFollowing) {

                if (isFollowing){

                    runOnUiThread(() -> {
                        followingImg.setText(String.format(getString(R.string.follow_user_following), mLiveStreamObject.getAuthor().getColFirstName()));
                        followingImg.setEnabled(false);
                    });
                } else {

                    runOnUiThread(() -> {
                        followingImg.setText(String.format(getString(R.string.follow_user), mLiveStreamObject.getAuthor().getColFirstName()));
                        followingImg.setEnabled(true);
                    });

                }
            }

            @Override
            public void onQueryFollowError(ParseException error) {

                runOnUiThread(() -> followingImg.setVisibility(View.INVISIBLE));
            }
        });

        followingImg.setOnClickListener(v -> {

            QuickHelp.showLoading(this, false);

            runOnUiThread(() -> {
                followingImg.setText(String.format(getString(R.string.follow_user_following), mLiveStreamObject.getAuthor().getColFirstName()));
                followingImg.setEnabled(false);
            });


            FollowModel followModel = new FollowModel();
            followModel.setFromAuthor(User.getUser());
            followModel.setToAuthor(mLiveStreamObject.getAuthor());
            followModel.saveInBackground(e -> {

                if (e == null){

                    QuickHelp.hideLoading(this);

                } else {

                    runOnUiThread(() -> {
                        followingImg.setText(String.format(getString(R.string.follow_user), mLiveStreamObject.getAuthor().getColFirstName()));
                        followingImg.setEnabled(true);
                    });



                    QuickHelp.showToast(LiveStreamingActivity.this, getString(R.string.error_ocurred), true);
                    sendMsg("liveStreamTerminated " + e.getLocalizedMessage());

                    QuickHelp.hideLoading(this);

                }
            });
        });

    }

    private void liveStreamEnded() {

        mLiveStreamEndedLyt.setVisibility(View.VISIBLE);

        mLiveStreamTerminatedLyt.setVisibility(View.GONE);
        mLoadingViewer.setVisibility(View.GONE);
        mGetReadyLyt.setVisibility(View.GONE);

        mVideoControlLyt.setVisibility(View.GONE);
        mVideoLayout.setVisibility(View.GONE);

        TextView tokens = findViewById(R.id.tokensValue);
        TextView followers = findViewById(R.id.followersValue);
        TextView viewers = findViewById(R.id.viewersValue);
        TextView duration = findViewById(R.id.timeValue);
        ImageView bgAvatar = findViewById(R.id.bg_avatar_end);

        tokens.setText(String.valueOf(liveStreamModel.getStreamingTokens()));

        if (liveStreamModel.getFollowers() != null){
            followers.setText(String.valueOf(liveStreamModel.getFollowers().size()));
        } else followers.setText("0");

        if (liveStreamModel.getViewersObjectId() != null){
            viewers.setText(String.valueOf(liveStreamModel.getViewersObjectId().size()));
        } else viewers.setText("0");

        duration.setText(getTimer());

        QuickHelp.setAvatarFull(bgAvatar, mCurrentUser);

        if (liveStreamModel != null){

            liveStreamModel.setStreaming(false);

            liveStreamModel.setStreamingTime(getTimer());
            liveStreamModel.saveEventually();
        }
    }

    private void sendMsg(final String msg) {
        runOnUiThread(() -> Log.v("LIVE STREAM", msg));
    }

    public ParseLiveQueryClient getLiveQueryClient (){

        return liveQueryClient;
    }

    private void unsubscribeToLiveQuery() {

        getLiveQueryClient().unsubscribe(liveStreamModelParseQuery, liveQueryStreamSubscription);
        getLiveQueryClient().unsubscribe(liveMessageModelParseQuery, liveMessageQueryStreamSubscription);
    }

    private void setupLiveQuery(String objectId) {

        liveStreamModelParseQuery.whereEqualTo(LiveStreamModel.KEY_OBJECT_ID, objectId);

        liveQueryStreamSubscription = getLiveQueryClient().subscribe(liveStreamModelParseQuery);
        liveQueryStreamSubscription.handleEvent(SubscriptionHandling.Event.CREATE, (query, liveStream) -> {

            sendMsg("Parse stream created");
            sendMsg("Parse stream created");

        }).handleEvent(SubscriptionHandling.Event.UPDATE, (query, liveStream) -> {

            sendMsg("Parse stream updated");

            if (mLiveStreamObject != null) {
                mLiveStreamObject = liveStream;
            }

            if (liveStreamModel != null){
                liveStreamModel = liveStream;

                mCurrentUser.setLiveStreamsViewersCount(1);

                if (liveStream.getViewersUid() != null){
                    mCurrentUser.setLiveStreamsViewersUid(liveStream.getViewersUid());
                }

                mCurrentUser.saveInBackground();
            }

            runOnUiThread(() -> {
                mViewersText.setText(String.valueOf(liveStream.getViewsLive()));
                mTokensText.setText(String.valueOf(liveStream.getStreamingTokens()));
            });


        }).handleUnsubscribe((query) -> liveQueryStreamSubscription = null);


        /////// LIVE MESSAGE //////

        liveMessageModelParseQuery.whereEqualTo(LiveMessageModel.LIVE_STREAM_ID, objectId);
        liveMessageModelParseQuery.include(LiveMessageModel.LIVE_STREAM);
        liveMessageModelParseQuery.include(LiveMessageModel.GIFT_LIVE);
        liveMessageModelParseQuery.include(LiveMessageModel.SENDER_AUTHOR);

        liveMessageQueryStreamSubscription = getLiveQueryClient().subscribe(liveMessageModelParseQuery);
        liveMessageQueryStreamSubscription.handleEvent(SubscriptionHandling.Event.CREATE, (query, liveMessage) -> {

            sendMsg("Live Message received");

            if (!liveMessage.getSenderAuthorId().equals(mCurrentUser.getObjectId())){

                mLiveMessages.add(0, liveMessage);
                runOnUiThread(() -> {
                    mLiveMessageAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);

                });
            }

        }).handleUnsubscribe((query) -> liveMessageQueryStreamSubscription = null);

    }

    private String getTimer() {
        return mTimeText.getText().toString().trim();
    }

    private void setTimer(Long elapsed) {

        startTime = elapsed;
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private void stopTimer() {

        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.elapsedRealtime() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            mTimeText.setText(String.format(Locale.US, "%02d:%s", mins, String.format(Locale.getDefault(), "%02d", secs)));
            customHandler.postDelayed(this, 0);
        }

    };

    public void showWarning(boolean warning){

        if (warning){

            sendMsg("No connection");

        } else {

            sendMsg("Reconnected");
        }
    }

    public void loadMessages(){


        liveMessageModelParseQuery.orderByDescending(LiveMessageModel.KEY_CREATED_AT);
        liveMessageModelParseQuery.findInBackground((messages, e) -> {

            if (e == null && messages.size() > 0){

                mLiveMessages.clear();
                mLiveMessages.addAll(messages);
                mLiveMessageAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(0);
            }
        });
    }

    public void showMoreOptions(){

        mMoreBottomSheet = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        mMoreBottomSheet.setContentView(R.layout.include_streamer_more_sheet);
        mMoreBottomSheet.setCancelable(true);
        mMoreBottomSheet.setCanceledOnTouchOutside(true);

        TextView viewProfile = mMoreBottomSheet.findViewById(R.id.live_view_profile);
        TextView follow = mMoreBottomSheet.findViewById(R.id.live_view_follow);
        TextView report = mMoreBottomSheet.findViewById(R.id.live_view_report);

        assert viewProfile != null;
        assert follow != null;
        assert report != null;

        viewProfile.setOnClickListener(v -> {

            if (mMoreBottomSheet != null && mMoreBottomSheet.isShowing()){
                mMoreBottomSheet.cancel();

                QuickActions.showProfile(LiveStreamingActivity.this, mLiveStreamObject.getAuthor(), false);
            }
        });

        report.setOnClickListener(v -> {

            if (mMoreBottomSheet != null && mMoreBottomSheet.isShowing()){
                mMoreBottomSheet.cancel();

                showReportOptions();
            }

        });

        runOnUiThread(() -> follow.setText(String.format(getString(R.string.live_follow_user), mLiveStreamObject.getAuthor().getColFirstName())));

        if (isFollowingUser){

            runOnUiThread(() -> followingImg.setVisibility(View.VISIBLE));

            follow.setText(String.format(getString(R.string.live_unfollow_user), mLiveStreamObject.getAuthor().getColFirstName()));

            follow.setOnClickListener(v -> {

                runOnUiThread(() -> followingImg.setVisibility(View.GONE));
                isFollowingUser = false;

                if (mMoreBottomSheet != null && mMoreBottomSheet.isShowing()){
                    mMoreBottomSheet.cancel();

                    ParseQuery<FollowModel> followModelParseQuery = FollowModel.getQuery();
                    followModelParseQuery.whereEqualTo(FollowModel.FROM_AUTHOR, mCurrentUser);
                    followModelParseQuery.whereEqualTo(FollowModel.TO_AUTHOR, mLiveStreamObject.getAuthor());
                    followModelParseQuery.getFirstInBackground((object, e) -> {

                        if (e == null){

                            isFollowingUser = false;
                            object.deleteInBackground();
                            mLiveStreamObject.removeFollowers(mCurrentUser.getUid());
                            mLiveStreamObject.saveInBackground();

                        } else {

                            isFollowingUser = true;
                            runOnUiThread(() -> followingImg.setVisibility(View.VISIBLE));
                        }
                    });
                }

            });

        } else {

            runOnUiThread(() -> followingImg.setVisibility(View.GONE));

            runOnUiThread(() -> follow.setText(String.format(getString(R.string.live_follow_user), mLiveStreamObject.getAuthor().getColFirstName())));

            follow.setOnClickListener(v -> {

                runOnUiThread(() -> followingImg.setVisibility(View.VISIBLE));
                isFollowingUser = true;

                if (mMoreBottomSheet != null && mMoreBottomSheet.isShowing()){
                    mMoreBottomSheet.cancel();

                    FollowModel followModel = new FollowModel();
                    followModel.setFromAuthor(mCurrentUser);
                    followModel.setToAuthor(mLiveStreamObject.getAuthor());
                    followModel.saveInBackground(e -> {

                        if (e == null){

                            isFollowingUser = true;

                            sendComment("", LiveMessageModel.MESSAGE_TYPE_FOLLOW);
                            mLiveStreamObject.setFollowers(mCurrentUser.getUid());
                            mLiveStreamObject.saveInBackground();

                        } else {
                            runOnUiThread(() -> followingImg.setVisibility(View.GONE));
                            isFollowingUser = false;
                        }
                    });
                }
            });
        }

        if (mMoreBottomSheet != null && !mMoreBottomSheet.isShowing()){
            mMoreBottomSheet.show();
        }

    }

    public void showReportOptions(){

        mReportBottomSheet = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        mReportBottomSheet.setContentView(R.layout.include_streamer_report_sheet);
        mReportBottomSheet.setCancelable(true);
        mReportBottomSheet.setCanceledOnTouchOutside(true);

        TextView like = mReportBottomSheet.findViewById(R.id.live_report_dont_like);
        TextView fraud = mReportBottomSheet.findViewById(R.id.live_report_fraud);
        TextView sale = mReportBottomSheet.findViewById(R.id.live_report_sale);
        TextView rud = mReportBottomSheet.findViewById(R.id.live_report_rud);
        TextView hate = mReportBottomSheet.findViewById(R.id.live_report_hate);
        TextView nud = mReportBottomSheet.findViewById(R.id.live_report_nud);
        TextView violence = mReportBottomSheet.findViewById(R.id.live_report_violence);
        TextView injury = mReportBottomSheet.findViewById(R.id.live_report_injury);

        assert like != null;
        assert fraud != null;
        assert sale != null;
        assert rud != null;
        assert hate != null;
        assert nud != null;
        assert violence != null;
        assert injury != null;

        like.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_LIKE));
        fraud.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_FRAUD));
        sale.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_SALE));
        rud.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_RUD));
        hate.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_HATE));
        nud.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_NUD));
        violence.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_VIOLENCE));
        injury.setOnClickListener(v -> finishReport(ReportModel.REPORT_LIVE_INJURY));

        if (mReportBottomSheet != null && !mReportBottomSheet.isShowing()){
            mReportBottomSheet.show();
        }

    }

    public void finishReport(String type){

        if (mReportBottomSheet != null && mReportBottomSheet.isShowing()){
            mReportBottomSheet.cancel();
        }

        ReportModel reportModel = new ReportModel();
        reportModel.setFromAuthor(mCurrentUser);
        reportModel.setToAuthor(mLiveStreamObject.getAuthor());
        reportModel.setToLiveStream(mLiveStreamObject);
        reportModel.setReportType(type);
        reportModel.saveInBackground();
    }

    public void initViewerFooter(){

        mGiftFooterList = new ArrayList<>();
        mGiftLiveFooterAdapter = new GiftLiveFooterAdapter(this, mGiftFooterList);

        mFooterRecyclerView.setAdapter(mGiftLiveFooterAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(false);

        mFooterRecyclerView.setHasFixedSize(true);
        mFooterRecyclerView.setBackgroundResource(R.color.transparent);
        mFooterRecyclerView.setLayoutManager(layoutManager);

        ParseQuery<GiftModel> giftModelParseQuery = GiftModel.getGiftQuery();
        giftModelParseQuery.findInBackground((giftList, e) -> {

            if (e == null){

                mGiftFooterList.clear();
                mGiftFooterList.addAll(giftList);
                mGiftLiveFooterAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(0);
            }
        });


    }

    public void initGiftBottomSheet() {

        ArrayList<GiftModel> mGiftFooterList = new ArrayList<>();
        GiftLiveAdapter mGiftLiveAdapter = new GiftLiveAdapter(this, mGiftFooterList);

        mGiftBottomShet = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        mGiftBottomShet.setContentView(R.layout.view_stream_viewer_bottom_sheet);
        mGiftBottomShet.setCancelable(true);
        mGiftBottomShet.setCanceledOnTouchOutside(true);

        RecyclerView recyclerView = mGiftBottomShet.findViewById(R.id.liveStreaming_bottom_rv);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        assert recyclerView != null;
        recyclerView.setAdapter(mGiftLiveAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundResource(R.color.transparent);
        recyclerView.setLayoutManager(layoutManager);

        ParseQuery<GiftModel> giftModelParseQuery = GiftModel.getGiftQuery();
        giftModelParseQuery.findInBackground((giftList, e) -> {

            if (e == null){

                mGiftFooterList.clear();
                mGiftFooterList.addAll(giftList);
                mGiftLiveAdapter.notifyDataSetChanged();
            }
        });

        if (mGiftBottomShet != null && !mGiftBottomShet.isShowing()){
            mGiftBottomShet.show();
        }
    }

    public void sendLiveGift(GiftModel giftModel){

        if (mGiftBottomShet != null && mGiftBottomShet.isShowing()){
            mGiftBottomShet.cancel();
        }

        if (mCurrentUser.getCredits() >= giftModel.getCredits()){

            showFullScreenGift(giftModel);

            mCurrentUser.removeCredit(giftModel.getCredits());
            mCurrentUser.saveInBackground();

            Map<String, Object> params = new HashMap<>();
            params.put(GiftModel.CREDITS_PARAM, giftModel.getCredits());
            params.put(GiftModel.KEY_OBJECT_ID, mLiveStreamObject.getAuthor().getObjectId());
            ParseCloud.callFunctionInBackground(SEND_GIFT_PARAM, params, (FunctionCallback<String>) (result, e) -> {

                if (e == null){

                    sendMsg("gift sent");

                    sendComment(giftModel, LiveMessageModel.MESSAGE_TYPE_GIFT);

                    mLiveStreamObject.setStreamingTokens(giftModel.getCredits());
                    mLiveStreamObject.saveInBackground();

                } else {
                    QuickHelp.showNotification(LiveStreamingActivity.this, getString(R.string.error_ocurred), true);
                    sendMsg("sendLiveGift " + e.getLocalizedMessage());
                }
            });

        } else {

            QuickHelp.showNotification(this, getString(R.string.no_credits), true);
        }
    }

    public void showFullScreenGift(GiftModel giftModel){

        mFullScreenGift.setVisibility(View.VISIBLE);
        mFullScreenGift.setAnimationFromUrl(giftModel.getGiftFile().getUrl());
        mFullScreenGift.playAnimation();
        mFullScreenGift.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                mFullScreenGift.cancelAnimation();
                mFullScreenGift.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}