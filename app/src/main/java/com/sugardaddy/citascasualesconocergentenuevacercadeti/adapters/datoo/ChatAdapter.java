package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile.PhotoViewerActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.MessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.RoundedImage;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.DateUtils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.vanniktech.emoji.EmojiTextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    // A menu item view type.
    private static final int CHAT_IN = 0;

    // The Native Express ad view type.
    private static final int CHAT_OUT = 1;

    private  List<MessageModel> mMessages;
    private Activity mActivity;

    public ChatAdapter(Activity activity, List<MessageModel> messages) {
        mMessages = messages;
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        final boolean isMe = mMessages.get(position).getSenderAuthorId().equals(User.getUser().getObjectId());
        if(isMe){
            return CHAT_OUT;
        } else {
            return CHAT_IN;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case CHAT_IN:

                View CHAT_IN = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                return new ViewHolder(CHAT_IN);

            case CHAT_OUT:

                View CHAT_OUT = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
                return new ViewHolder(CHAT_OUT);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel message = mMessages.get(position);

        int viewType = getItemViewType(position);

        switch (viewType) {

            case CHAT_OUT:

                if (message.isMessageFile()){

                    if (!message.isFileUploaded()){

                        holder.messageText.setVisibility(View.GONE);
                        holder.mImageLayout.setVisibility(View.VISIBLE);
                        holder.mProgressBar.setVisibility(View.VISIBLE);

                        QuickHelp.getMessageImage(message.getImagePath(), holder.mImageMsg);


                    } else if (message.getMessageFile() != null){

                        holder.messageText.setVisibility(View.GONE);

                        holder.mImageLayout.setVisibility(View.VISIBLE);
                        holder.mProgressBar.setVisibility(View.VISIBLE);

                        message.getMessageFile().getFileInBackground((file, e) -> {

                            if (file != null && !file.getPath().isEmpty()) {

                                QuickHelp.getMessageImage(file.getPath(), holder.mImageMsg);
                            } else {
                                QuickHelp.getMessageImage(message.getMessageFile().getUrl(), holder.mImageMsg);
                            }

                            holder.mProgressBar.setVisibility(View.GONE);

                            holder.mImageLayout.setOnClickListener(v -> {

                                if (message.isMessageFile() && message.isFileUploaded()){

                                    QuickHelp.goToActivityWithNoClean(mActivity, PhotoViewerActivity.class, PhotoViewerActivity.imageUrl, file.getPath());
                                }
                            });

                        }, percentDone -> holder.mProgressBar.setProgress(percentDone));
                    }

                } else if (message.getCall() != null){

                    holder.mImageLayout.setVisibility(View.GONE);
                    holder.messageText.setVisibility(View.VISIBLE);
                    holder.messageText.setBackground(mActivity.getResources().getDrawable(R.drawable.bubble_right_rect_call));

                    if (message.getCall().isAccepted()){

                        if (message.getCall().isVoiceCall()){
                            holder.messageText.setText(String.format(mActivity.getString(R.string.calls_caller_user_voice), message.getReceiverAuthor().getColFirstName(), message.getCall().getDuration()));
                        } else {
                            holder.messageText.setText(String.format(mActivity.getString(R.string.calls_caller_user), message.getReceiverAuthor().getColFirstName(), message.getCall().getDuration()));
                        }

                        holder.messageText.setTextColor(Color.BLACK);
                    } else {
                        holder.messageText.setText(String.format(mActivity.getString(R.string.calls_caller_missed_user), message.getReceiverAuthor().getColFirstName()));
                        holder.messageText.setTextColor(Color.RED);
                    }


                } else if (message.getMessage() != null && !message.getMessage().isEmpty()){

                    holder.mImageLayout.setVisibility(View.GONE);
                    holder.messageText.setVisibility(View.VISIBLE);
                    holder.messageText.setBackground(mActivity.getResources().getDrawable(R.drawable.bubble_right_rect));
                    holder.messageText.setText(message.getMessage());
                    holder.messageText.setTextColor(Color.WHITE);
                }


                if(message.getCreatedAt() == null){

                    if (message.getCall() == null){
                        holder.status.setText(mActivity.getString(R.string.message_sending));
                    }

                } else {

                    if (message.getCall() == null){
                        if (message.getRead()){
                            holder.status.setText(String.format(mActivity.getString(R.string.message_seen_format), DateUtils.formatDateTime(message.getCreatedAt().getTime())));
                        } else {
                            holder.status.setText(String.format(mActivity.getString(R.string.message_delivered_format), DateUtils.formatDateTime(message.getCreatedAt().getTime())));
                        }

                    } else {

                        holder.status.setText(DateUtils.formatDateAndTime(message.getCreatedAt().getTime()));
                    }

                }


                break;

            case CHAT_IN:


                if (message.getMessageFile() != null){

                    holder.messageText.setVisibility(View.GONE);

                    holder.mImageLayout.setVisibility(View.VISIBLE);
                    holder.mProgressBar.setVisibility(View.VISIBLE);

                    message.getMessageFile().getFileInBackground((file, e) -> {

                        if (file != null && file.getPath() != null && !file.getPath().isEmpty()) {

                            QuickHelp.getMessageImage(file.getPath(), holder.mImageMsg);
                        } else {
                            QuickHelp.getMessageImage(message.getMessageFile().getUrl(), holder.mImageMsg);
                        }


                        holder.mProgressBar.setVisibility(View.GONE);

                        holder.mImageLayout.setOnClickListener(v -> {

                            if (message.isMessageFile() && message.isFileUploaded()){

                                QuickHelp.goToActivityWithNoClean(mActivity, PhotoViewerActivity.class, PhotoViewerActivity.imageUrl, file.getPath());
                            }
                        });

                    }, percentDone -> holder.mProgressBar.setProgress(percentDone));

                } else if (message.getCall() != null){

                    holder.mImageLayout.setVisibility(View.GONE);
                    holder.messageText.setVisibility(View.VISIBLE);
                    holder.messageText.setBackground(mActivity.getResources().getDrawable(R.drawable.bubble_left_rect_call));

                    if (message.getCall().isAccepted()){

                        if (message.getCall().isVoiceCall()){
                            holder.messageText.setText(String.format(mActivity.getString(R.string.calls_callee_user_voice), message.getReceiverAuthor().getColFirstName(), message.getCall().getDuration()));
                        } else {
                            holder.messageText.setText(String.format(mActivity.getString(R.string.calls_callee_user), message.getSenderAuthor().getColFirstName(), message.getCall().getDuration()));
                        }

                        holder.messageText.setTextColor(Color.BLACK);
                    } else {
                        holder.messageText.setText(String.format(mActivity.getString(R.string.calls_callee_missed_user), message.getSenderAuthor().getColFirstName()));
                        holder.messageText.setTextColor(Color.RED);
                    }


                } else if (message.getMessage() != null && !message.getMessage().isEmpty()){

                    holder.mImageLayout.setVisibility(View.GONE);
                    holder.messageText.setVisibility(View.VISIBLE);
                    holder.messageText.setBackground(mActivity.getResources().getDrawable(R.drawable.bubble_left_rect));
                    holder.messageText.setText(message.getMessage());
                    holder.messageText.setTextColor(Color.BLACK);
                }


                if (message.getCall() == null){

                    holder.timeText.setText(DateUtils.formatDateTime(message.getCreatedAt().getTime()));
                } else {
                    holder.timeText.setText(DateUtils.formatDateAndTime(message.getCreatedAt().getTime()));
                }


                if (!message.getRead()){

                    message.setRead(true);
                    message.saveInBackground();
                }

                ParseQuery<ConnectionListModel> connectionListModelParseQuery = ConnectionListModel.getConnectionsQuery();
                connectionListModelParseQuery.whereEqualTo(ConnectionListModel.COL_MESSAGE_ID, message.getObjectId());
                connectionListModelParseQuery.getFirstInBackground(new GetCallback<ConnectionListModel>() {
                    @Override
                    public void done(ConnectionListModel object, ParseException e) {
                        if (object != null && !object.isRead()){

                            object.setRead(true);
                            object.resetCount();
                            object.saveInBackground();
                        }
                    }
                });

                break;

        }

    }


    public void updateMessage(MessageModel newMessage) {

        int arraySize = mMessages.size();
        for (int i = 0; i < arraySize; i++) {
            MessageModel message = mMessages.get(i);
            if (message != null && message.getObjectId() != null && newMessage != null && newMessage.getObjectId() != null) {

                if (message.getObjectId().equals(newMessage.getObjectId())){

                    if (message.isMessageFile() && !message.isFileUploaded()){
                        message.setFileUploaded(true);
                        message.saveInBackground();
                    }
                    mActivity.runOnUiThread(this::notifyDataSetChanged);
                }
            }
        }
    }

    public void updateMessageSent(MessageModel newMessage) {

        int arraySize = mMessages.size();
        for (int i = 0; i < arraySize; i++) {
            MessageModel message = mMessages.get(i);
            if (message != null && newMessage != null && message.getObjectId().equals(newMessage.getObjectId())) {

                message.setRead(true);

                mActivity.runOnUiThread(this::notifyDataSetChanged);
            }
        }
    }

    public void addNewMessage(MessageModel newMessage, RecyclerView mRecyclerView) {

        mMessages.add(0, newMessage);
        // RecyclerView updates need to be run on the UI thread
        mActivity.runOnUiThread(() -> {
            notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
        });
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EmojiTextView messageText;
        TextView timeText;
        TextView status;
        ProgressBar mProgressBar;
        RelativeLayout mImageLayout;
        RoundedImage mImageMsg;


        public ViewHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            status = itemView.findViewById(R.id.status_messages);
            mProgressBar = itemView.findViewById(R.id.chatMessage_photo_loading);
            mImageLayout = itemView.findViewById(R.id.image_lyt);
            mImageMsg = itemView.findViewById(R.id.chatMessage_photo_image);
        }
    }
}