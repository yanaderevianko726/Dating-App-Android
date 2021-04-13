package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.AutoMessagesModel;
import com.greysonparrelli.permiso.Permiso;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.ChatAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.PhotosChatUploadAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.SendNotifications;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.MessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.UploadModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.camera.ImagePro;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.DateUtils;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ItemOffsetDecoration;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiImageView;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.camera.ImagePro.CAMERA_CODE;


public class ChatActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1003;

    //private Cursor imagecursor;

    public static String EXTRA_USER_TO_ID = "user_to_id";

    ParseLiveQueryClient liveQueryClient;

    Toolbar mToolBar;
    User mCurrentUser, mUser;
    LinearLayout rootView, mArrowBack;
    ArrayList<MessageModel> mMessages;
    ChatAdapter mChatAdapter;

    CircleImageView mToolbarAvatar;
    EmojiTextView mToolbarName;
    TextView mTooBarStatus;

    EmojiPopup emojiPopup;
    EmojiImageView mEmojiBtn;
    EmojiEditText mEditText;
    ImageButton mSend;
    ImageView mPickFile, mTooBarFavorite, mTooBarVoiceCall, mTooBarVideoCall;

    RecyclerView mRecyclerView;

    RelativeLayout mLoading, mNoMessages;
    TextView mTextError;

    private ParseQuery<MessageModel> messageQuery;
    private ParseQuery<User> userToQuery = User.getUserQuery();
    ParseQuery<MessageModel> messageFromQuery;
    ParseQuery<MessageModel> messageToQuery;

    private SubscriptionHandling<MessageModel> liveQueryChatSubscription;
    private SubscriptionHandling<User> liveQueryUserSubscription;

    ConnectionListModel messageList;

    boolean isFakeMessage = false;


    //////////// Gallery /////////////////////

    RelativeLayout mGalleryLayout;

    private ArrayList<UploadModel> mPhotosUploads;
    private PhotosChatUploadAdapter mPhotosUploadAdapter;

    private LinearLayout mNoAccountLayout;
    private TextView mNoPhotosText;
    private AppCompatButton mConenctButton;
    private ProgressBar mProgressbar;
    private RecyclerView mRecyclerViewGallery;

    private ArrayList<UploadModel> igModelArrayList;

    private ImagePro imagePro;

    boolean isSending = false;

    //////////// Gallery /////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mUser = getIntent().getParcelableExtra(EXTRA_USER_TO_ID);

        rootView = findViewById(R.id.activity_messages);
        mToolBar = findViewById(R.id.toolbar);

        mToolbarAvatar = findViewById(R.id.toolbar_image);
        mToolbarName = findViewById(R.id.toolbar_title);
        mTooBarStatus = findViewById(R.id.toolbar_status);
        mTooBarFavorite = findViewById(R.id.favorite_btn);
        mTooBarVoiceCall = findViewById(R.id.voice_call_btn);
        mTooBarVideoCall = findViewById(R.id.video_call_btn);

        mEmojiBtn = findViewById(R.id.emoji_btn);
        mEditText = findViewById(R.id.MessageWrapper);

        mSend = findViewById(R.id.sendMessageButton);
        mPickFile = findViewById(R.id.sendFileButton);

        mArrowBack = findViewById(R.id.arrow_back);

        mRecyclerView = findViewById(R.id.listMessages);

        mLoading = findViewById(R.id.prograss_layout);
        mNoMessages = findViewById(R.id.no_message_layout);

        mTextError = findViewById(R.id.textView8);

        setNoMessage(true, false);

        setSupportActionBar(mToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setElevation(2.0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        Permiso.getInstance().setActivity(this);

        mSend.setEnabled(false);
        mEditText.setEnabled(true);
        mEmojiBtn.setEnabled(true);
        mEditText.setHint(getString(R.string.type_msg));

        QuickHelp.setupTextWatcher(mEditText, mSend);

        mTooBarFavorite.setEnabled(false);

        if (!Config.isVoiceCallEnabled) mTooBarVoiceCall.setVisibility(View.GONE);
        if (!Config.isVideoCallEnabled) mTooBarVideoCall.setVisibility(View.GONE);

        mArrowBack.setOnClickListener(v -> finish());
        mSend.setOnClickListener(v -> ChatActivity.this.checkMessagesLimit(false, ""));
        mTooBarVoiceCall.setOnClickListener(v -> checkCallPermission(false));
        mTooBarVideoCall.setOnClickListener(v -> checkCallPermission(true));

        mMessages = new ArrayList<>();
        mChatAdapter = new ChatAdapter(this, mMessages);

        mCurrentUser = (User) User.getCurrentUser();

        setUpEmojiPopup();

        mRecyclerView.setAdapter(mChatAdapter);

        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setHasFixedSize(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.TRANSPARENT);
        mRecyclerView.setLayoutManager(layoutManager);


        //////////////// Gallery //////////////

        mGalleryLayout = findViewById(R.id.gallery);
        mNoAccountLayout = findViewById(R.id.layout_no_account);
        mNoPhotosText = findViewById(R.id.no_image_text);
        mConenctButton = findViewById(R.id.btn_connect_instagram);
        mProgressbar = findViewById(R.id.progress_instagram);
        mRecyclerViewGallery = findViewById(R.id.recyclerView);

        //////////////// Gallery //////////////

        mPickFile.setOnClickListener(v -> {

            if (mGalleryLayout.getVisibility() == View.VISIBLE){

                mGalleryLayout.setVisibility(View.GONE);
                mPickFile.setImageResource(R.drawable.ic_chat_control_action_multimedia);
                //showKeyboard(v);
                //mEditText.hasFocus();

            } else {

                mGalleryLayout.setVisibility(View.VISIBLE);
                mPickFile.setImageResource(R.drawable.ic_chat_control_action_keyboard);

                hideKeyboard(v);

                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                }

                initGallery();
            }
        });

        mEditText.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus){

                mGalleryLayout.setVisibility(View.GONE);
                mPickFile.setImageResource(R.drawable.ic_chat_control_action_multimedia);
            }
        });


        loadUser();

        loadMessages(false);

    }

    public void checkCallPermission(boolean isVideoCall) {

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    if (isVideoCall){
                        QuickActions.makeVideoCall(ChatActivity.this, mUser);
                    } else {
                        QuickActions.makeVoiceCall(ChatActivity.this, mUser);
                    }

                } else {

                    QuickHelp.showToast(ChatActivity.this, getString(R.string.msg_permission_required), true);
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

    public void initGallery() {

        mPhotosUploads = new ArrayList<>();
        mPhotosUploadAdapter = new PhotosChatUploadAdapter( this, mPhotosUploads);

        Permiso.getInstance().setActivity(this);

        isLoading(true, false);
        if(Build.VERSION.SDK_INT >= 21) {

            checkPermissions();

        } else {
            getMyGalleryPhotos();
        }

        mConenctButton.setOnClickListener(v1 -> checkPermissions());

        mRecyclerViewGallery.setAdapter(mPhotosUploadAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerViewGallery.addItemDecoration(itemDecoration);

        mRecyclerViewGallery.setBackgroundResource(R.color.white);
        mRecyclerViewGallery.setBackgroundColor(Color.WHITE);
        mRecyclerViewGallery.setLayoutManager(layoutManager);
    }

    private void isLoading(boolean isLoading, boolean isLoaded) {

        if (isLoading){

            mProgressbar.setVisibility(View.VISIBLE);
            mNoAccountLayout.setVisibility(View.GONE);
            mRecyclerViewGallery.setVisibility(View.GONE);

        } else {

            if (isLoaded){

                mProgressbar.setVisibility(View.GONE);
                mNoAccountLayout.setVisibility(View.GONE);
                mRecyclerViewGallery.setVisibility(View.VISIBLE);

            } else {

                mProgressbar.setVisibility(View.GONE);
                mNoAccountLayout.setVisibility(View.VISIBLE);
                mRecyclerViewGallery.setVisibility(View.GONE);
            }

        }
    }

    private void getMyGalleryPhotos() {

        imagePro = new ImagePro(this);

        igModelArrayList = new ArrayList<>();

        UploadModel camera = new UploadModel();
        camera.setPhotoPath("camera");

        UploadModel gallery = new UploadModel();
        gallery.setPhotoPath("gallery");


        igModelArrayList.add(camera);
        igModelArrayList.add(gallery);

        igModelArrayList.addAll(fetchGalleryImages());

        if (igModelArrayList.size() > 0){

            isLoading(false, true);

            mPhotosUploads.clear();
            mPhotosUploads.addAll(igModelArrayList);
            mPhotosUploadAdapter.notifyDataSetChanged();

        } else {

            isLoading(false, false);
            mNoPhotosText.setText(R.string.no_photo_found);
        }

    }

    private ArrayList<UploadModel> fetchGalleryImages() {
        ArrayList<UploadModel> galleryImageUrls;
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};//get all columns of type images
        @SuppressLint("InlinedApi") final String orderBy = MediaStore.Images.Media.DATE_TAKEN;//order data by date

        Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");//get all data in Cursor by sorting in DESC order
        //imagecursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

        galleryImageUrls = new ArrayList<>();

        if (imagecursor != null) {
            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);//get column index
                //galleryImageUrls.add(imagecursor.getString(dataColumnIndex));//get Image from column index

                UploadModel grModel = new UploadModel();
                grModel.setPhotoPath(imagecursor.getString(dataColumnIndex));

                galleryImageUrls.add(grModel);
            }
        }
        Log.e("fatch in","images");
        return galleryImageUrls;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);

    }

    public void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void takeFromCamera(){

        imagePro.openCameraTaker();
    }

    private void addNewPhoto(String photoPath){

        Log.v("IMAGE", "ADDED NEW PHOTO" + "Path: " + photoPath);

        UploadModel uploadModel = new UploadModel();
        uploadModel.setPhotoPath(photoPath);

        mPhotosUploads.add(2, uploadModel);
        mPhotosUploadAdapter.notifyItemInserted(2);
    }

    private void checkPermissions() {

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    // User has permissions
                    getMyGalleryPhotos();

                } else if (resultSet.isPermissionPermanentlyDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per_whrite_storage);
                    });

                    // No permissions or missing permissions
                } else if (resultSet.isPermissionPermanentlyDenied(Manifest.permission.READ_EXTERNAL_STORAGE)){

                    runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per_whrite_storage);
                    });

                    // No permissions or missing permissions
                } else if (resultSet.isPermissionPermanentlyDenied(Manifest.permission.CAMERA)){

                    runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per_camera);
                    });

                    // No permissions or missing permissions
                } else {

                    runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per);
                    });

                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(null, getString(R.string.msg_permission_required), getString(R.string.ok), callback);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

    }

    private void setupLiveQuery() {

        liveQueryChatSubscription = getLiveQueryClient().subscribe(messageQuery);
        liveQueryChatSubscription.handleEvent(SubscriptionHandling.Event.CREATE, (query, message) -> {

            if (message.getSenderAuthorId().equals(mCurrentUser.getObjectId())){

                // You can do things like send push, etc...
                //Log.v("Parse", "I sent the message");
                SendNotifications.SendPush(message.getSenderAuthor(), message.getReceiverAuthor(), SendNotifications.PUSH_TYPE_MESSAGES, message.getMessage());

                mChatAdapter.updateMessage(message);

            } else {

                if (!isFakeMessage){

                    mChatAdapter.addNewMessage(message, mRecyclerView);
                    setNoMessage(false, true);
                }


                // Add new message in your list
                //Log.v("Parse", "I received the message");
            }

        }).handleEvent(SubscriptionHandling.Event.UPDATE, (query, message) -> {

            if (message.getSenderAuthorId().equals(mCurrentUser.getObjectId())){

                // Update your sent message status
                Log.v("Parse", "My message updated");

                if (message.getRead()){
                    mChatAdapter.updateMessageSent(message);

                } else {
                    loadMessages(true);
                }
            }

        }).handleUnsubscribe((query) -> liveQueryChatSubscription = null);


        userToQuery.whereEqualTo(User.KEY_OBJECT_ID, mUser.getObjectId());
        liveQueryUserSubscription = getLiveQueryClient().subscribe(userToQuery);
        liveQueryUserSubscription.handleEvent(SubscriptionHandling.Event.UPDATE, (query, user) -> setUserStatus(user)).handleUnsubscribe((query) -> liveQueryUserSubscription = null);

    }

    public void setUserStatus(User user){

        runOnUiThread(() -> {

            if (user.getLastOnline() != null){

                if (!user.getPrivacyOnlineStatusEnabled()){

                    mTooBarStatus.setText(getString(R.string.user_status_private));

                } else {

                    if (System.currentTimeMillis() - user.getUpdatedAt().getTime() > Constants.TIME_TO_OFFLINE) {

                        mTooBarStatus.setText(String.format(getString(R.string.user_status_online_ago), DateUtils.getTimeAgo(user.getUpdatedAt().getTime())));

                    } else if (System.currentTimeMillis() - user.getUpdatedAt().getTime() > Constants.TIME_TO_SOON) {

                        mTooBarStatus.setText(String.format(getString(R.string.user_status_online_ago), DateUtils.getTimeAgo(user.getUpdatedAt().getTime())));

                    } else {

                        mTooBarStatus.setText(getString(R.string.user_status_online));
                    }
                }

            } else {

                mTooBarStatus.setText(getString(R.string.user_status_offline));
            }
        });
    }

    public void loadMessages(Boolean onUpdate){

        if (mCurrentUser == null) return;
        if (mUser == null) return;

        messageFromQuery = MessageModel.getMessageParseQuery();
        messageFromQuery.whereEqualTo(MessageModel.SENDER_AUTHOR_ID, mCurrentUser.getObjectId());
        messageFromQuery.whereEqualTo(MessageModel.RECEIVER_AUTHOR_ID, mUser.getObjectId());

        messageToQuery = MessageModel.getMessageParseQuery();
        messageToQuery.whereEqualTo(MessageModel.RECEIVER_AUTHOR_ID, mCurrentUser.getObjectId());
        messageToQuery.whereEqualTo(MessageModel.SENDER_AUTHOR_ID, mUser.getObjectId());

        messageQuery = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery));

        messageQuery.whereNotEqualTo(MessageModel.MESSAGE_FILE_UPLOADED, false);

        messageQuery.include(MessageModel.SENDER_AUTHOR);
        messageQuery.include(MessageModel.RECEIVER_AUTHOR);
        messageQuery.include(MessageModel.COL_CONNECTION);
        messageQuery.include(MessageModel.COL_CALLS);
        messageQuery.orderByDescending(MessageModel.KEY_CREATED_AT);

        messageQuery.findInBackground((objects, e) -> {

            if (e == null){

                if (objects.size() > 0){
                    // Add Messages to list

                    mMessages.clear();
                    mMessages.addAll(objects);
                    mChatAdapter.notifyDataSetChanged(); // update adapter
                    mRecyclerView.scrollToPosition(0);

                    setNoMessage(false, true);

                } else {
                    // Show no message
                    String gender;
                    if (mUser.getColGender().equals(User.GENDER_MALE)){
                        gender = getString(R.string.message_him);
                    } else {
                        gender = getString(R.string.message_her);
                    }
                    setNoMessage(String.format(getString(R.string.no_message), gender));

                }

            } else {

                // SHow error

                if (!onUpdate) setNoMessage(e.getMessage());
            }
        });

    }

    public void setNoMessage(boolean isLoading, boolean isLoaded){


        if (isLoading){

            mLoading.setVisibility(View.VISIBLE);
            mNoMessages.setVisibility(View.GONE);

        } else {

            if (isLoaded){

                mLoading.setVisibility(View.GONE);
                mNoMessages.setVisibility(View.GONE);
            }
        }

    }

    public void setNoMessage( String text){

        mLoading.setVisibility(View.GONE);
        mNoMessages.setVisibility(View.VISIBLE);
        mTextError.setText(text);

    }

    public void loadUser(){

        QuickHelp.getAvatars(mUser, mToolbarAvatar);
        mToolbarName.setText(mUser.getColFullName());
        setUserStatus(mUser);

        mToolbarAvatar.setOnClickListener(view -> QuickActions.showProfile(this, mUser, false));

        ConnectionListModel.queryFavorite(mUser, new ConnectionListModel.QueryFavoriteListener() {
            @Override
            public void onQueryFavorited(ConnectionListModel favoriteModel) {

                mTooBarFavorite.setEnabled(true);
                mTooBarFavorite.setImageResource(R.drawable.ic_profile_favorite_added);

                mTooBarFavorite.setOnClickListener(v -> {

                    mTooBarFavorite.setEnabled(true);
                    mTooBarFavorite.setImageResource(R.drawable.ic_profile_favorite);

                    favoriteModel.deleteEventually();
                });
            }

            @Override
            public void onQueryUnFavorited() {

                mTooBarFavorite.setEnabled(true);
                mTooBarFavorite.setImageResource(R.drawable.ic_profile_favorite);

                mTooBarFavorite.setOnClickListener(v -> {

                    mTooBarFavorite.setEnabled(true);
                    mTooBarFavorite.setImageResource(R.drawable.ic_profile_favorite_added);

                    ConnectionListModel favoriteModel = new ConnectionListModel();
                    favoriteModel.setUserFrom(mCurrentUser);
                    favoriteModel.setUserTo(mUser);
                    favoriteModel.setConnectionType(ConnectionListModel.CONNECTION_TYPE_FAVORITE);
                    favoriteModel.saveInBackground(e -> {

                        if (e == null){
                            SendNotifications.SendPush(mCurrentUser, mUser, SendNotifications.PUSH_TYPE_FAVORITED_YOU, null);
                        }

                    });
                });
            }

            @Override
            public void onQueryFavoriteError(ParseException error) {

                mTooBarFavorite.setEnabled(false);

            }
        });
    }

    Bitmap imageBitmap;

    public void sendMessageFile(String imagePath){

        if (isSending){
            QuickHelp.showNotification(this, getString(R.string.wait_previous_image), true);
            return;
        }

        MessageModel message = new MessageModel();

        imageBitmap = QuickHelp.getBitmapFromPath(imagePath);

        // Sender
        message.setSenderAuthor(mCurrentUser);
        message.setSenderAuthorId(mCurrentUser.getObjectId());

        // Receiver
        message.setReceiverAuthor(mUser);
        message.setReceiverAuthorId(mUser.getObjectId());

        message.setRead(false);

        message.setMessageFile(true);
        message.setFileUploaded(false);

        message.setImagePath(imagePath);

        if (imageBitmap != null) {

            isSending = true;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();

            ParseFile file  = new ParseFile("picture.jpg", image, "jpeg");
            file.saveInBackground((SaveCallback) e -> {

                if (e == null ){

                    // Message
                    message.setMessageFile(file);
                    //message.setFileUploaded(true);
                    message.saveInBackground(e1 -> {
                        if (e1 == null){

                            isSending = false;
                            mChatAdapter.updateMessage(message);
                            setChatList(message, ConnectionListModel.MESSAGE_TYPE_IMAGE);

                        } else if (e1.getCode() == -1){

                            isSending = false;
                            mChatAdapter.updateMessage(message);
                            setChatList(message, ConnectionListModel.MESSAGE_TYPE_IMAGE);

                            Log.v("MESSAGE_CHAT", e1.getCode() + " " + e1.getLocalizedMessage());

                        } else {

                            isSending = false;
                            QuickHelp.showToast(ChatActivity.this, getString(R.string.error_ocurred), true);
                        }

                    });

                } else {

                    isSending = false;
                    QuickHelp.showToast(ChatActivity.this, e.getMessage(), true);
                }
            });
        }

        mChatAdapter.addNewMessage(message, mRecyclerView);
        setNoMessage(false, true);
        mEditText.setText(null);
    }

    public void checkMessagesLimit(boolean isMessageFile, String imagePath){

        if (isMessageFile){

            prepareMessage(isMessageFile, imagePath);

        } else if (mEditText.getText() != null && mEditText.getText().length() > 0 ){

            prepareMessage(isMessageFile, "");
        }
    }

    public void prepareMessage(boolean isMessageFile, String imagePath){

        if (Config.isPaidMessagesActivated && mCurrentUser.getColGender().equals(User.GENDER_MALE) && !mCurrentUser.isPremium()) {

            if (mCurrentUser.getMessageLimitCounterToday() != null && QuickHelp.isToday(mCurrentUser.getMessageLimitCounterToday())){

                if (mCurrentUser.getMessageLimitCounterTotal() >= Config.freeMessagesInTotal){

                    QuickHelp.showNotification(ChatActivity.this, getString(R.string.your_total_message_quote), true);

                    QuickHelp.goToActivityWithNoClean(ChatActivity.this, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM, PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM_MESSAGES);

                } else if (mCurrentUser.getMessageLimitCounterDaily() >= Config.freeMessagesPerDay){

                    QuickHelp.showNotification(ChatActivity.this, getString(R.string.your_dailly_message_quote), true);

                    QuickHelp.goToActivityWithNoClean(ChatActivity.this, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM, PaymentsActivity.DATOO_PREMIUM_TYPE, PaymentsActivity.TYPE_DATOO_PREMIUM_MESSAGES);

                } else {

                    mCurrentUser.setMessageLimitCounterDaily();
                    mCurrentUser.setMessageLimitCounterTotal();
                    mCurrentUser.saveInBackground();

                    if (isMessageFile){
                        sendMessageFile(imagePath);
                    } else {
                        sendMessage();
                    }

                }

            } else {

                mCurrentUser.setMessageLimitCounterTotal();
                mCurrentUser.setMessageLimitCounterDailyOne();
                mCurrentUser.setMessageLimitCounterToday(new Date());
                mCurrentUser.saveInBackground();

                if (isMessageFile){
                    sendMessageFile(imagePath);
                } else {
                    sendMessage();
                }
            }

        } else {

            if (isMessageFile){
                sendMessageFile(imagePath);
            } else {
                sendMessage();
            }
        }
    }

    public void sendMessage(){

        if (mEditText.getText() != null && mEditText.getText().length() > 0){

            MessageModel message = new MessageModel();

            // Message
            message.setMessage(mEditText.getText().toString());
            message.setMessageFile(false);

            // Sender
            message.setSenderAuthor(mCurrentUser);
            message.setSenderAuthorId(mCurrentUser.getObjectId());

            // Receiver
            message.setReceiverAuthor(mUser);
            message.setReceiverAuthorId(mUser.getObjectId());

            message.setRead(false);

            message.saveInBackground(e -> {
                if (e == null){

                    mChatAdapter.updateMessage(message);
                    setChatList(message, ConnectionListModel.MESSAGE_TYPE_CHAT);
                }
            });

            mChatAdapter.addNewMessage(message, mRecyclerView);
            setNoMessage(false, true);
            mEditText.setText(null);
        }
    }

    public void setChatList(MessageModel message, String messageType){

        ParseQuery<ConnectionListModel> messageFromQuery = ConnectionListModel.getConnectionsQuery();
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, message.getSenderAuthor());
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, message.getReceiverAuthor());
        messageFromQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        ParseQuery<ConnectionListModel> messageToQuery = ConnectionListModel.getConnectionsQuery();
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_TO, message.getSenderAuthor());
        messageToQuery.whereEqualTo(ConnectionListModel.COL_USER_FROM, message.getReceiverAuthor());
        messageToQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);

        ParseQuery<ConnectionListModel> connectionListModelParseQuery = ParseQuery.or(Arrays.asList(messageToQuery, messageFromQuery));
        connectionListModelParseQuery.whereEqualTo(ConnectionListModel.COL_CONNECTION_TYPE, ConnectionListModel.CONNECTION_TYPE_MESSAGE);
        connectionListModelParseQuery.whereEqualTo(ConnectionListModel.COL_MESSAGE_TYPE, ConnectionListModel.MESSAGE_TYPE_CHAT);

        connectionListModelParseQuery.getFirstInBackground((lastMessage, e) -> {

            if (lastMessage != null){

                lastMessage.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);
                lastMessage.setMessageType(messageType);
                if (message.getMessage() != null){ lastMessage.setText(message.getMessage()); }
                lastMessage.setMessage(message);
                lastMessage.setMessageId(message.getObjectId());
                lastMessage.setUserFrom(message.getSenderAuthor());
                lastMessage.setUserTo(message.getReceiverAuthor());
                lastMessage.setUserFromId(message.getSenderAuthor().getObjectId());
                lastMessage.setUserToId(message.getReceiverAuthor().getObjectId());
                lastMessage.setRead(false);
                lastMessage.setCount();
                lastMessage.saveInBackground(e22 -> {
                    if (e22 == null) {

                        updateMessage(message, lastMessage);
                    }
                });

            } else if (e != null && e.getCode() == ParseException.OBJECT_NOT_FOUND){

                messageList = new ConnectionListModel();

                messageList.setConnectionType(ConnectionListModel.CONNECTION_TYPE_MESSAGE);
                messageList.setMessageType(messageType);
                if (message.getMessage() != null){ messageList.setText(message.getMessage()); }
                messageList.setUserFrom(message.getSenderAuthor());
                messageList.setUserTo(message.getReceiverAuthor());
                messageList.setUserFromId(message.getSenderAuthor().getObjectId());
                messageList.setUserToId(message.getReceiverAuthor().getObjectId());
                messageList.setRead(false);
                messageList.setCount();
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

    public void updateMessage(MessageModel messageModel, ConnectionListModel connectionListModel) {

        messageModel.setMessageOnListId(connectionListModel.getObjectId());
        messageModel.setMessageOnList(connectionListModel);
        messageModel.saveInBackground();

        AutoMessagesModel.queryQuestion(mUser.getObjectId(), messageModel.getMessage(), autoMessagesModel -> {

            isFakeMessage = true;

            MessageModel messageAuto = new MessageModel();

            // Message
            messageAuto.setMessage(autoMessagesModel.getResponse());
            messageAuto.setMessageFile(false);

            // Sender
            messageAuto.setSenderAuthor(mUser);
            messageAuto.setSenderAuthorId(mUser.getObjectId());

            // Receiver
            messageAuto.setReceiverAuthor(mCurrentUser);
            messageAuto.setReceiverAuthorId(mCurrentUser.getObjectId());

            messageAuto.setRead(false);

            messageAuto.saveInBackground(e -> {
                if (e == null){

                    if (isFakeMessage){
                        mChatAdapter.addNewMessage(messageAuto, mRecyclerView);
                        //isFakeMessage = false;
                    }

                    setChatList(messageAuto, ConnectionListModel.MESSAGE_TYPE_CHAT);
                }
            });
        });
    }

    private void setUpEmojiPopup() {

        mEmojiBtn.setColorFilter(ContextCompat.getColor(ChatActivity.this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);

        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(v -> { })
                .setOnEmojiClickListener((emoji, imageView) -> {

                    mGalleryLayout.setVisibility(View.GONE);
                    mPickFile.setImageResource(R.drawable.ic_chat_control_action_multimedia);
                })
                .setOnEmojiPopupShownListener(() -> mEmojiBtn.setImageResource(R.drawable.ic_chat_control_action_keyboard_small))
                .setOnSoftKeyboardOpenListener(keyBoardHeight -> { })
                .setOnEmojiPopupDismissListener(() -> mEmojiBtn.setImageResource(R.drawable.ic_chat_control_action_sticker_small))
                .setOnSoftKeyboardCloseListener(() -> { })
                .build(mEditText);

        mEditText.setOnClickListener(view -> {

            if (emojiPopup.isShowing()) {
                emojiPopup.dismiss();

                mGalleryLayout.setVisibility(View.GONE);
                mPickFile.setImageResource(R.drawable.ic_chat_control_action_multimedia);
            }
        });

        mEmojiBtn.setOnClickListener(view -> {

            mGalleryLayout.setVisibility(View.GONE);
            mPickFile.setImageResource(R.drawable.ic_chat_control_action_multimedia);

            emojiPopup.toggle();
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (mGalleryLayout.getVisibility() == View.VISIBLE){

            mGalleryLayout.setVisibility(View.GONE);
            mPickFile.setImageResource(R.drawable.ic_chat_control_action_multimedia);


        } else {

            finish();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void unsubscribeToLiveQuery() {

        getLiveQueryClient().unsubscribe(messageQuery, liveQueryChatSubscription);
        getLiveQueryClient().unsubscribe(userToQuery, liveQueryUserSubscription);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Parse","onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Parse","onResume invoked");

        Application.getInstance().getWorkerThread().connectToRtmService(String.valueOf(mCurrentUser.getUid()));


        try {

            liveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(Config.LIVE_QUERY_URL));
            liveQueryClient.connectIfNeeded();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        loadMessages(true);
        setupLiveQuery();

        Application.getInstance().setIsChatOpened(true, mUser.getObjectId());

        Permiso.getInstance().setActivity(this);
    }

    public ParseLiveQueryClient getLiveQueryClient (){

        return liveQueryClient;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == RESULT_OK)

            if (requestCode == GALLERY_REQUEST_CODE) {//data.getData return the content URI for the selected Image

                Log.v("GALLEY", "GOT NEW PHOTO");

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = null;
                if (selectedImage != null) {
                    cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                }
                // Move to first row
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                }
                //Gets the String value in the column
                String imgDecodableString = null;
                if (cursor != null) {
                    imgDecodableString = cursor.getString(columnIndex);
                }

                Log.v("GALLEY", "BEFORE NEW PHOTO" + "Size: " + igModelArrayList.size());

                if (imgDecodableString != null && !imgDecodableString.isEmpty()){


                    addNewPhoto(imgDecodableString);
                    Log.v("GALLEY", "ADDED NEW PHOTO" + "Size: " + igModelArrayList.size());

                }


                if (cursor != null) {
                    cursor.close();
                }
                // Set the Image in ImageView after decoding the String

            } else if (requestCode == CAMERA_CODE) {

                ImagePro.ImageDetails imageDetails = imagePro.getImagePath(CAMERA_CODE, RESULT_OK, data);

                if (imageDetails.getPath() != null && !imageDetails.getPath().isEmpty()){

                    addNewPhoto(imageDetails.getPath());

                }

            } else {

                Log.v("CAMERA", "ERROR");
            }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsubscribeToLiveQuery();
        Log.d("Parse","onPause invoked");
        Application.getInstance().setIsChatOpened(false, null);
    }
    @Override
    protected void onStop() {
        super.onStop();
        //unsubscribeToLiveQuery();
        Log.d("Parse","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Parse","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unsubscribeToLiveQuery();
        Log.d("Parse","onDestroy invoked");
    }
}
