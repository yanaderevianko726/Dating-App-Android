package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.calls;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.*;

import androidx.appcompat.widget.AppCompatTextView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.CallsModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.MessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.AGEventHandler;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.ConstantApp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;

public class CallActivity extends BaseActivity implements AGEventHandler {

    private User mCaleeUser, mCurrentUser;
    private String mChannel;

    private FrameLayout mLayoutSmallView, mLayoutBigView;
    private View mBgOverlay;

    private ImageView mVideoBgImage;
    private TextView mCalleeName, mCalleeAge, mCallTimer, mVideoBtnText, mMicBtnText;
    private AppCompatTextView mCallStatus;
    private ImageButton mCallHangUp, mSwitchSpeaker, mEnableDisableCamera;

    private MediaPlayer mPlayer;
    private int callType = ConstantApp.UNKNOWN;
    private boolean mIsCallInRefuse = false;
    private boolean mAudioEnabled = true, mVideoEnabled = true;
    private int mRemoteUid = 0;

    // Timer
    Timer timer;
    long autoHangupDelay = 30 * 1000;

    private Handler customHandler = new Handler();
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    boolean isCallAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_rtc);

        QuickHelp.setScreenOn(this, true);
    }

    @Override
    protected void initUIandEvent() {
        this.event().addEventHandler(this);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        mCaleeUser = getIntent().getParcelableExtra(ConstantApp.ACTION_KEY_SUBSCRIBER_OBJECT);
        mChannel = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        callType = getIntent().getIntExtra(ConstantApp.ACTION_KEY_MakeOrReceive, ConstantApp.UNKNOWN);

        mLayoutSmallView = findViewById(R.id.videoChat_localVideoRender);
        mLayoutBigView = findViewById(R.id.remote_video_view);
        mBgOverlay = findViewById(R.id.videoChat_backgroundOverlay);

        mVideoBgImage = findViewById(R.id.videoChat_backgroundImage);
        mCalleeName = findViewById(R.id.videoChat_userName);
        mCalleeAge = findViewById(R.id.videoChat_userAge);
        mCallStatus = findViewById(R.id.videoChat_callStatus);
        mCallHangUp = findViewById(R.id.videoChat_hungUp);
        mSwitchSpeaker = findViewById(R.id.videoChat_switchSpeaker);
        mEnableDisableCamera = findViewById(R.id.videoChat_switchCamera);
        mCallTimer = findViewById(R.id.videoChat_timerText);

        mVideoBtnText = findViewById(R.id.videoChat_videoText);
        mMicBtnText = findViewById(R.id.videoChat_microphoneText);

        mLayoutSmallView.setOnClickListener(v -> rtcEngine().switchCamera());

        initCalleeInfo();

        setupForCall();

        setupButtons();
    }

    @Override
    protected void deInitUIandEvent() {
        worker().leaveChannel(mChannel);
        this.event().removeEventHandler(this);
    }

    private void setupForCall() {

        Log.v("AGORA", String.format("Receiver ID: %s, Channel:  %s", mCaleeUser.getUid(), mChannel) );

        worker().configEngine(VideoEncoderConfiguration.VD_1280x720, null, null);

        setupLocalVideo();

        callPickedUp(false);

        if (callType == ConstantApp.CALL_IN) {
            mIsCallInRefuse = true;
            mCallStatus.setText(R.string.video_chat_connecting);

            answerTheCall();


        } else if (callType == ConstantApp.CALL_OUT) {
            mCallStatus.setText(R.string.video_chat_contacting);

            // Set the call to auto HangUp after 30 seconds without any response
            timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    callOutHangup(CallsModel.CALL_END_REASON_NO_ANSWER);
                }

            }, autoHangupDelay);

            try {
                mPlayer = MediaPlayer.create(this, R.raw.video_chat_dialing);
                mPlayer.setLooping(true);
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            worker().queryPeersOnlineStatus(String.valueOf(mCaleeUser.getUid()));

            worker().joinChannel(mChannel, mCurrentUser.getUid(), ConstantApp.VIDEO_CALL);
        }
    }

    public void initCalleeInfo(){

        mCalleeName.setText(mCaleeUser.getColFullName());
        if (mCaleeUser.getBirthDate() != null){
            mCalleeAge.setText(String.format(" , %s", QuickHelp.getAgeFromDate(mCaleeUser.getBirthDate())));
        }
        QuickHelp.getAvatars(mCaleeUser, mVideoBgImage);
    }

    public void localVideoChanged(boolean isEnable){
        if (isEnable){

            mEnableDisableCamera.setImageResource(R.drawable.ic_videocall_video_on);
            mVideoBtnText.setText(R.string.video_chat_on);
            mVideoBtnText.setVisibility(View.GONE);

        } else {

            mEnableDisableCamera.setImageResource(R.drawable.ic_videocall_video_off);
            mVideoBtnText.setText(R.string.video_chat_off);
            mVideoBtnText.setVisibility(View.VISIBLE);

        }
    }

    public void remoteVideoChanged(boolean isEnable){
        if (isEnable){

            mBgOverlay.setVisibility(View.GONE);
            mVideoBgImage.setVisibility(View.GONE);
            mLayoutBigView.setVisibility(View.VISIBLE);

        } else {

            mBgOverlay.setVisibility(View.VISIBLE);
            mVideoBgImage.setVisibility(View.VISIBLE);
            mLayoutBigView.setVisibility(View.GONE);

        }
    }

    public void localAudioChanged(boolean isEnable){
        if (isEnable){

            mSwitchSpeaker.setImageResource(R.drawable.ic_videocall_audio_on);
            mMicBtnText.setText(R.string.video_chat_on);
            mMicBtnText.setVisibility(View.GONE);

        } else {

            mSwitchSpeaker.setImageResource(R.drawable.ic_videocall_audio_off);
            mMicBtnText.setText(R.string.video_chat_off);
            mMicBtnText.setVisibility(View.VISIBLE);

        }
    }

    public void callPickedUp(boolean pickedUp){

        isCallAccepted = pickedUp;

        if (pickedUp){

            mSwitchSpeaker.setVisibility(View.VISIBLE);
            mEnableDisableCamera.setVisibility(View.VISIBLE);
            //mMicBtnText.setVisibility(View.GONE);

        } else {

            mSwitchSpeaker.setVisibility(View.GONE);
            mEnableDisableCamera.setVisibility(View.GONE);
            mMicBtnText.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v("AGORA CALL","onNewIntent " + intent);

        setupForCall();
    }

    public void setupButtons(){

        if (isCallAccepted){
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_END));
        } else {
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_GIVE_UP));
        }


        mSwitchSpeaker.setOnClickListener(v -> runOnUiThread(() -> {

            if (mAudioEnabled){ // is True

                mAudioEnabled = false;
                rtcEngine().muteLocalAudioStream(true);
                localAudioChanged(false);

            } else { // is False

                mAudioEnabled = true;
                rtcEngine().muteLocalAudioStream(false);
                localAudioChanged(true);
            }

        }));

        mEnableDisableCamera.setOnClickListener(v -> runOnUiThread(() -> {

            if (mVideoEnabled){

                mVideoEnabled = false;
                rtcEngine().muteLocalVideoStream(true);
                localVideoChanged(false);

            } else {

                mVideoEnabled = true;
                rtcEngine().muteLocalVideoStream(false);
                localVideoChanged(true);
            }
        }));
    }

    private void answerTheCall() {
        worker().joinChannel(mChannel, mCurrentUser.getUid(), ConstantApp.VIDEO_CALL);
        worker().answerTheCall(config().mRemoteInvitation);
    }

    private void callOutHangup(String reason) {
        onEncCallClicked(reason);

        worker().hangupTheCall(null); // local|out
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        QuickHelp.setScreenOn(this, false);

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        Log.v("AGORA CALL","onDestroy");
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    @Override
    public void onBackPressed() {
        Log.v("AGORA CALL","onBackPressed callType: " + callType + " mIsCallInRefuse: " + mIsCallInRefuse);

        if (isCallAccepted){
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_END));
        } else {
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_GIVE_UP));
        }
        super.onBackPressed();
    }

    public void onEncCallClicked(String reason) {

        stopTimer();

        if (callType == ConstantApp.CALL_OUT) {

            CallsModel callsModel = new CallsModel();

            callsModel.setCallerAuthor(mCurrentUser);
            callsModel.setCallerAuthorId(mCurrentUser.getObjectId());

            callsModel.setReceiverAuthor(mCaleeUser);
            callsModel.setReceiverAuthorId(mCaleeUser.getObjectId());

            callsModel.setAccepted(isCallAccepted);
            if (isCallAccepted){
                callsModel.setDuration(getTimer());
            } else {
                callsModel.setDuration("00:00");
            }

            callsModel.setCallEndReason(reason);

            callsModel.saveEventually(e -> {

                if (e == null){

                    MessageModel messageModel = new MessageModel();

                    messageModel.setSenderAuthor(mCurrentUser);
                    messageModel.setSenderAuthorId(mCurrentUser.getObjectId());

                    messageModel.setReceiverAuthor(mCaleeUser);
                    messageModel.setReceiverAuthorId(mCaleeUser.getObjectId());

                    messageModel.setCall(callsModel);
                    messageModel.setMessageFile(false);
                    messageModel.setRead(false);

                    messageModel.saveEventually(e1 -> {

                        if (e1 == null){

                            setMessageList(messageModel, callsModel, ConnectionListModel.MESSAGE_TYPE_CALL);
                        }
                    });
                }
            });

            new Handler(Looper.getMainLooper()).postDelayed(this::finish, 4000);

        } else {
            new Handler(Looper.getMainLooper()).postDelayed(this::finish, 2000);
        }
    }

    public void setMessageList(MessageModel message, CallsModel callsModel, String type){

        // Message Saved! or Sent
        // Look for these users conversation (Me has sender)
        ParseQuery<ConnectionListModel> query = ConnectionListModel.getConnectionsQuery();

        query.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCurrentUser);
        query.whereEqualTo(ConnectionListModel.COL_USER_TO, mCaleeUser);
        query.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        query.getFirstInBackground((messagerListClass, e) -> {

            if (e == null){

                messagerListClass.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);
                messagerListClass.setMessageType(type);
                if (message.getMessage() != null){ messagerListClass.setText(message.getMessage()); }
                messagerListClass.setMessage(message);
                messagerListClass.setCall(callsModel);
                messagerListClass.setMessageId(message.getObjectId());
                messagerListClass.setUserFrom(mCurrentUser);
                messagerListClass.setUserTo(mCaleeUser);
                messagerListClass.setUserFromId(mCurrentUser.getObjectId());
                messagerListClass.setUserToId(mCaleeUser.getObjectId());
                messagerListClass.setRead(false);
                messagerListClass.setCount();
                messagerListClass.saveInBackground(e12 -> {
                    if (e12 == null) {

                        updateMessage(message, messagerListClass);
                    }
                });

            } else {

                // Look for these users conversation (other has sender)
                ParseQuery<ConnectionListModel> query1 =  ConnectionListModel.getConnectionsQuery();

                query1.whereEqualTo(ConnectionListModel.COL_USER_TO, mCurrentUser);
                query1.whereEqualTo(ConnectionListModel.COL_USER_FROM, mCaleeUser);
                query1.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
                query1.getFirstInBackground((lastMessage, e1) -> {

                    if (e1 == null){

                        lastMessage.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);
                        lastMessage.setMessageType(type);
                        if (message.getMessage() != null){ lastMessage.setText(message.getMessage()); }
                        lastMessage.setMessage(message);
                        lastMessage.setCall(callsModel);
                        lastMessage.setMessageId(message.getObjectId());
                        lastMessage.setUserFrom(mCurrentUser);
                        lastMessage.setUserTo(mCaleeUser);
                        lastMessage.setUserFromId(mCurrentUser.getObjectId());
                        lastMessage.setUserToId(mCaleeUser.getObjectId());
                        lastMessage.setRead(false);
                        lastMessage.setCount();
                        lastMessage.saveInBackground(e22 -> {
                            if (e22 == null) {

                                updateMessage(message, lastMessage);
                            }
                        });

                    } else {

                        ConnectionListModel messageList = new ConnectionListModel();

                        messageList.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);
                        messageList.setMessageType(type);
                        if (message.getMessage() != null){ messageList.setText(message.getMessage()); }
                        messageList.setUserFrom(mCurrentUser);
                        messageList.setUserTo(mCaleeUser);
                        messageList.setUserFromId(mCurrentUser.getObjectId());
                        messageList.setUserToId(mCaleeUser.getObjectId());
                        messageList.setRead(false);
                        messageList.setCount();
                        messageList.setCall(callsModel);
                        messageList.setMessage(message);
                        messageList.setMessageId(message.getObjectId());
                        messageList.saveInBackground(e2 -> {
                            if (e2 ==null){

                                updateMessage(message, messageList);
                            }
                        });

                    }
                });


            }
        });
    }

    public void updateMessage(MessageModel messageModel, ConnectionListModel connectionListModel) {

        messageModel.setMessageOnListId(connectionListModel.getObjectId());
        messageModel.setMessageOnList(connectionListModel);
        messageModel.saveEventually();
    }

    private void setupLocalVideo() {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        mLayoutBigView.addView(surfaceView);

        worker().preview(true, surfaceView, 0);

        mLayoutBigView.setVisibility(View.VISIBLE);
    }

    private void setupRemoteVideo(int uid) {
        Log.v("AGORA CALL","setupRemoteVideo uid: " + (uid & 0xFFFFFFFFL) + " " + mLayoutBigView.getChildCount());
        View view = mLayoutBigView.getChildAt(0);
        mLayoutBigView.removeAllViews();
        ((SurfaceView) view).setZOrderMediaOverlay(true);
        ((SurfaceView) view).setZOrderOnTop(true);
        mLayoutSmallView.addView(view);
        mLayoutSmallView.setVisibility(View.VISIBLE);

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        mLayoutBigView.addView(surfaceView);

        rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mLayoutBigView.setVisibility(View.VISIBLE);
    }

    private void onRemoteUserLeft(int uid) {
        if (uid == mRemoteUid) {
            finish();
        }
    }

    private void onRemoteUserVideoMuted(int uid, boolean muted) {

        SurfaceView surfaceView = (SurfaceView) mLayoutBigView.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
            remoteVideoChanged(muted);
        }
    }

    @Override
    public void onLoginSuccess(String uid) {

    }

    @Override
    public void onLoginFailed(String uid, ErrorInfo error) {

    }

    @Override
    public void onPeerOnlineStatusQueried(String uid, boolean online) {
        if (online) {
            worker().makeACall(uid, mChannel, String.valueOf(ConstantApp.VIDEO_CALL));
            runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_callingg));
        } else {

            runOnUiThread(() -> {
                mCallStatus.setText(R.string.video_chat_offline);
                mCallStatus.setTextColor(Color.RED);
                callOutHangup(CallsModel.CALL_END_REASON_OFFLINE);
            });
            //showLongToast(uid + " is not available now");
        }
    }

    @Override
    public void onInvitationReceivedByPeer(LocalInvitation invitation) {
        Log.v("AGORA CALL","onInvitationReceivedByPeer " + invitation);

        runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_ringing));
    }

    @Override
    public void onLocalInvitationAccepted(LocalInvitation invitation, String response) {
        Log.v("AGORA CALL","onLocalInvitationAccepted " + invitation + " " + invitation.getResponse() + " " + response);

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_connected));
    }

    @Override
    public void onLocalInvitationRefused(LocalInvitation invitation, final String response) {
        Log.v("AGORA CALL","onLocalInvitationRefused " + invitation + " " + invitation.getResponse() + " " + response);

        //final String callee = invitation.getCalleeId();

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        runOnUiThread(() -> {
            if (response.contains("status") && response.contains("1")) {
                //showLongToast("line busy for " + callee);
                mCallStatus.setText(R.string.video_chat_busy_in_call);
                mCallStatus.setTextColor(Color.RED);

                onEncCallClicked(CallsModel.CALL_END_REASON_BUSY);

            } else {
                //showLongToast(callee + " reject your call");
                mCallStatus.setText(R.string.video_chat_busy);
                mCallStatus.setTextColor(Color.RED);

                onEncCallClicked(CallsModel.CALL_END_REASON_REFUSED);
            }
        });
    }

    @Override
    public void onLocalInvitationCanceled(LocalInvitation invitation) {
        runOnUiThread(() -> onEncCallClicked(CallsModel.CALL_END_REASON_GIVE_UP));
    }

    @Override
    public void onInvitationReceived(RemoteInvitation invitation) {
        invitation.setResponse("{\"status\":1}"); // Busy, already in call

        worker().hangupTheCall(invitation);
    }

    @Override
    public void onInvitationRefused(RemoteInvitation invitation) {
        String channel = config().mChannel;

        Log.v("AGORA CALL","onInvitationRefused " + invitation + " " + invitation.getResponse() + " " + channel);

        if (channel == null) {
            return;
        }

        // TODO RTM 1.0 will support getChannelId()
        if (TextUtils.equals(channel, invitation.getChannelId()) || TextUtils.equals(channel, invitation.getContent())) {
            runOnUiThread(() -> onEncCallClicked(CallsModel.CALL_END_REASON_REFUSED));
        }
    }



    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        Log.v("AGORA CALL","onFirstRemoteVideoDecoded " + (uid & 0xFFFFFFFFL) + " " + height + " " + width);

        runOnUiThread(() -> {
            if (mRemoteUid != 0) {
                return;
            }
            mRemoteUid = uid;
            setupRemoteVideo(uid);

            mCallStatus.setText(R.string.video_chat_connected);

            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.stop();
            }

            callPickedUp(true);

            setTimer();

            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        });
    }

    @Override
    public void onFirstRemoteAudioFrame(int uid, int elapsed) {

    }


    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        Log.v("AGORA CALL","onJoinChannelSuccess " + (uid & 0xFFFFFFFFL));

    }

    @Override
    public void onUserOffline(int uid, int reason) {
        onRemoteUserLeft(uid);
    }



    @Override
    public void onExtraCallback(int type, Object... data) {
        //needReLogin(type, data);

        if (EVENT_TYPE_ON_USER_VIDEO_MUTED == type) {
            onRemoteUserVideoMuted((int) data[0], (boolean) data[1]);
        } else if (EVENT_TYPE_ON_RELOGIN_NEEDED == type) {
            //final boolean banned = (boolean) data[0];
            Application.getInstance().getWorkerThread().connectToRtmService(String.valueOf(mCurrentUser.getUid()));
        }
    }

    @Override
    public void onLastmileQuality(int quality) {

    }

    @Override
    public void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result) {

    }

    private String getTimer() {
        return mCallTimer.getText().toString().trim();
    }

    private void setTimer() {
        runOnUiThread(() -> mCallTimer.setVisibility(View.VISIBLE));
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private void stopTimer() {

        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            mCallTimer.setText(String.format(Locale.US, "%d:%s", mins, String.format(Locale.getDefault(), "%02d", secs)));
            customHandler.postDelayed(this, 0);
        }

    };
}
