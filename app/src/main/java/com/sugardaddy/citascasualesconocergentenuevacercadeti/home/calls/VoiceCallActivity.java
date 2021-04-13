package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.calls;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.CallsModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.MessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.AGEventHandler;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.rtcUtils.ConstantApp;
import com.greysonparrelli.permiso.Permiso;
import com.parse.ParseQuery;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;

public class VoiceCallActivity extends BaseActivity implements AGEventHandler {

    private static final String LOG_TAG = VoiceCallActivity.class.getSimpleName();

    private User mCaleeUser, mCurrentUser;
    private String mChannel;

    private int callType = ConstantApp.UNKNOWN;

    private TextView mCalleeName, mCalleeAge, mCallTimer, mMicBtnText, mSpeakerBtnText;
    private ImageView mVideoBgImage;
    private AppCompatTextView mCallStatus;
    private ImageButton mCallHangUp, mSwitchSpeaker, mEnableDisableCamera;

    private MediaPlayer mPlayer;

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
        setContentView(R.layout.activity_voice_call);

        mCurrentUser = (User) User.getCurrentUser();

        Permiso.getInstance().setActivity(this);

        mCaleeUser = getIntent().getParcelableExtra(ConstantApp.ACTION_KEY_SUBSCRIBER_OBJECT);
        mChannel = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        callType = getIntent().getIntExtra(ConstantApp.ACTION_KEY_MakeOrReceive, ConstantApp.UNKNOWN);

        mMicBtnText = findViewById(R.id.videoChat_microphoneText);
        mSpeakerBtnText = findViewById(R.id.videoChat_videoText);

        mCallTimer = findViewById(R.id.videoChat_timerText);

        mVideoBgImage = findViewById(R.id.videoChat_backgroundImage);
        mCalleeName = findViewById(R.id.videoChat_userName);
        mCalleeAge = findViewById(R.id.videoChat_userAge);
        mCallStatus = findViewById(R.id.videoChat_callStatus);

        mCallHangUp = findViewById(R.id.videoChat_hungUp);
        mSwitchSpeaker = findViewById(R.id.videoChat_switchSpeaker);
        mEnableDisableCamera = findViewById(R.id.videoChat_switchCamera);

        QuickHelp.setScreenOn(this, true);

        /*if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {

        }*/

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    setupForCall();

                } else {

                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    finish();

                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(null,
                        getString(R.string.msg_permission_required),
                        null, callback);
            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

        mSwitchSpeaker.setOnClickListener(v -> onSwitchSpeakerphoneClicked());

        mEnableDisableCamera.setOnClickListener(v -> onLocalAudioMuteClicked());

        initCalleeInfo();

        setupButtons();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void initUIandEvent() {
        this.event().addEventHandler(this);
    }

    @Override
    protected void deInitUIandEvent() {
        worker().leaveChannel(mChannel);
        this.event().removeEventHandler(this);
    }

    private void setupForCall() {

        Log.v("AGORA", String.format("Receiver ID: %s, Channel:  %s", mCaleeUser.getUid(), mChannel) );

        callPickedUp(false);

        if (callType == ConstantApp.CALL_IN) {

            runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_connecting));
            answerTheCall();


        } else if (callType == ConstantApp.CALL_OUT) {

            runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_contacting));

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
            worker().joinChannel(mChannel, mCurrentUser.getUid(), ConstantApp.VOICE_CALL);
        }
    }

    private void answerTheCall() {
        worker().joinChannel(mChannel, mCurrentUser.getUid(), ConstantApp.VOICE_CALL);
        worker().answerTheCall(config().mRemoteInvitation);
    }

    public void callPickedUp(boolean pickedUp){

        isCallAccepted = pickedUp;

        if (pickedUp){

            mSwitchSpeaker.setVisibility(View.VISIBLE);
            mEnableDisableCamera.setVisibility(View.VISIBLE);

        } else {

            mSwitchSpeaker.setVisibility(View.GONE);
            mEnableDisableCamera.setVisibility(View.GONE);
            mMicBtnText.setVisibility(View.GONE);

        }
    }

    public void initCalleeInfo(){

        mCalleeName.setText(mCaleeUser.getColFullName());
        if (mCaleeUser.getBirthDate() != null){
            mCalleeAge.setText(String.format(" , %s", QuickHelp.getAgeFromDate(mCaleeUser.getBirthDate())));
        }
        QuickHelp.getAvatars(mCaleeUser, mVideoBgImage);
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
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

    // Tutorial Step 7
    public void onLocalAudioMuteClicked() {

        if (mEnableDisableCamera.isSelected()) {
            mEnableDisableCamera.setSelected(false);

            mEnableDisableCamera.setImageResource(R.drawable.ic_videocall_audio_off);
            mMicBtnText.setText(R.string.video_chat_off);
            mMicBtnText.setVisibility(View.VISIBLE);

        } else {
            mEnableDisableCamera.setSelected(true);

            mEnableDisableCamera.setImageResource(R.drawable.ic_videocall_audio_on);
            mMicBtnText.setText(R.string.video_chat_on);
            mMicBtnText.setVisibility(View.GONE);
        }

        rtcEngine().muteLocalAudioStream(mEnableDisableCamera.isSelected());
    }

    // Tutorial Step 5
    public void onSwitchSpeakerphoneClicked() {

        if (mSwitchSpeaker.isSelected()) {
            mSwitchSpeaker.setSelected(false);

            mSwitchSpeaker.setImageResource(R.drawable.ic_videocall_speaker_off);
            mSpeakerBtnText.setText(R.string.video_chat_off);
            mSpeakerBtnText.setVisibility(View.VISIBLE);

        } else {
            mSwitchSpeaker.setSelected(true);

            mSwitchSpeaker.setImageResource(R.drawable.ic_videocall_speaker_on);
            mSpeakerBtnText.setText(R.string.video_chat_on);
            mSpeakerBtnText.setVisibility(View.GONE);
        }

        rtcEngine().setEnableSpeakerphone(mSwitchSpeaker.isSelected());
    }

    private void callOutHangup(String reason) {

        runOnUiThread(() -> mCallStatus.setEnabled(false));


        onEncCallClicked(reason);

        worker().hangupTheCall(null); // local|out
    }

    @Override
    public void onBackPressed() {
        Log.v("AGORA CALL","onBackPressed callType: " + callType);

        if (isCallAccepted){
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_END));
        } else {
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_GIVE_UP));
        }
        super.onBackPressed();
    }

    public void setupButtons(){

        if (isCallAccepted){
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_END));
        } else {
            mCallHangUp.setOnClickListener(v -> callOutHangup(CallsModel.CALL_END_REASON_GIVE_UP));
        }
    }

    public void onEncCallClicked(String reason) {

        runOnUiThread(() -> mCallStatus.setTextColor(Color.RED));


        switch (reason) {
            case CallsModel.CALL_END_REASON_OFFLINE:
                runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_offline));
                break;
            case CallsModel.CALL_END_REASON_REFUSED:
                runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_busy));
                break;
            case CallsModel.CALL_END_REASON_BUSY:
                runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_busy_in_call));
                break;
            default:
                runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_call_ended));
                break;
        }

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
            callsModel.setVoiceCall(true);

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

    private void onRemoteUserLeft(int uid, int reason) {
        finish();
    }


    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
        //showLongToast(String.format(Locale.US, "user %d muted or unmuted %b", (uid & 0xFFFFFFFFL), muted));
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

    @Override
    public void onLoginSuccess(String uid) {

    }

    @Override
    public void onLoginFailed(String uid, ErrorInfo error) {

    }

    @Override
    public void onPeerOnlineStatusQueried(String uid, boolean online) {
        if (online) {
            worker().makeACall(uid, mChannel, String.valueOf(ConstantApp.VOICE_CALL));
            runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_callingg));
        } else {

            runOnUiThread(() -> {
                callOutHangup(CallsModel.CALL_END_REASON_OFFLINE);

                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
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
                onEncCallClicked(CallsModel.CALL_END_REASON_BUSY);
            } else {
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
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

    }

    @Override
    public void onFirstRemoteAudioFrame(int uid, int elapsed) {

        Log.v("AGORA CALL","onFirstRemoteVideoDecoded " + (uid & 0xFFFFFFFFL) + " ");

        runOnUiThread(() -> {

            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.stop();
            }

            mSwitchSpeaker.setSelected(true);
            mSwitchSpeaker.setImageResource(R.drawable.ic_videocall_speaker_off);
            rtcEngine().setEnableSpeakerphone(false);

            mEnableDisableCamera.setSelected(true);
            mEnableDisableCamera.setImageResource(R.drawable.ic_videocall_audio_on);
            rtcEngine().muteLocalAudioStream(false);

            runOnUiThread(() -> mCallStatus.setText(R.string.video_chat_connected));

            callPickedUp(true);

            setTimer();

            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        });
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserOffline(int uid, int reason) {
        runOnUiThread(() -> onRemoteUserLeft(uid, reason));
    }

    @Override
    public void onExtraCallback(int type, Object... data) {

    }

    @Override
    public void onLastmileQuality(int quality) {

    }

    @Override
    public void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result) {

    }
}
