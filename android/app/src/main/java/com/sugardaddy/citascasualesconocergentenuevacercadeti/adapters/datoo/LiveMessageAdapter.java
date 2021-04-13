package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveMessageModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;

import java.util.List;

public class LiveMessageAdapter extends RecyclerView.Adapter<LiveMessageAdapter.ViewHolder> {

    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_FOLLOW = 1;
    private static final int TYPE_GIFT = 2;

    private List<LiveMessageModel> mLiveMessage;
    private Activity mActivity;

    public LiveMessageAdapter(Activity activity, List<LiveMessageModel> liveMessageModels) {
        mLiveMessage = liveMessageModels;
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {

        switch (mLiveMessage.get(position).getMessageType()) {
            case LiveMessageModel.MESSAGE_TYPE_COMMENT:

                return TYPE_COMMENT;

            case LiveMessageModel.MESSAGE_TYPE_FOLLOW:

                return TYPE_FOLLOW;

            default:
                return TYPE_GIFT;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_COMMENT:

                View TYPE_COMMENT = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream_message_regular, parent, false);
                return new ViewHolder(TYPE_COMMENT);

            case TYPE_FOLLOW:

                View TYPE_FOLLOW = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream_system_message_with_avatar, parent, false);
                return new ViewHolder(TYPE_FOLLOW);

            case TYPE_GIFT:

                View TYPE_GIFT = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream_gift_message, parent, false);
                return new ViewHolder(TYPE_GIFT);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        LiveMessageModel liveMessage = mLiveMessage.get(position);

        QuickHelp.getAvatars(liveMessage.getSenderAuthor(), viewHolder.mUserPhoto);

        switch (liveMessage.getMessageType()) {
            case LiveMessageModel.MESSAGE_TYPE_COMMENT:

                viewHolder.mFirstName.setText(liveMessage.getSenderAuthor().getColFirstName());
                viewHolder.mMessage.setText(liveMessage.getMessage());

                break;
            case LiveMessageModel.MESSAGE_TYPE_FOLLOW:

                if (liveMessage.getSenderAuthorId().equals(User.getUser().getObjectId())) {
                    viewHolder.mSystemMessage.setText(String.format(mActivity.getString(R.string.live_you_followed_user), liveMessage.getLiveStream().getAuthor().getColFirstName()));

                } else {
                    viewHolder.mSystemMessage.setText(String.format(mActivity.getString(R.string.live_user_followed_user), liveMessage.getSenderAuthor().getColFirstName(), liveMessage.getLiveStream().getAuthor().getColFirstName()));
                }

                break;
            case LiveMessageModel.MESSAGE_TYPE_GIFT:

                if (liveMessage.getLiveGift().getGiftFile() != null){

                    viewHolder.mGiftImg.setAnimationFromUrl(liveMessage.getLiveGift().getGiftFile().getUrl());
                    viewHolder.mGiftImg.setSpeed(0.8f);
                }

                if (liveMessage.getSenderAuthorId().equals(User.getUser().getObjectId())) {
                    viewHolder.mSystemMessage.setText(String.format(mActivity.getString(R.string.live_you_sent_gift_user), liveMessage.getLiveStream().getAuthor().getColFirstName()));

                } else {
                    viewHolder.mSystemMessage.setText(String.format(mActivity.getString(R.string.live_user_sent_gift_user), liveMessage.getSenderAuthor().getColFirstName(), liveMessage.getLiveStream().getAuthor().getColFirstName()));
                }
                break;
        }

        viewHolder.mUserPhoto.setOnClickListener(v -> {

            if (!liveMessage.getSenderAuthorId().equals(User.getUser().getObjectId())){
                QuickActions.showProfile(mActivity, liveMessage.getSenderAuthor(), false);
            }

        });

    }

    @Override
    public int getItemCount() {
        return mLiveMessage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mUserPhoto;
        TextView mFirstName, mMessage, mSystemMessage;
        LottieAnimationView mGiftImg;

        ViewHolder(View v) {
            super(v);

            //All
            mUserPhoto = v.findViewById(R.id.userImageView);

            // Comments
            mFirstName = v.findViewById(R.id.displayName);
            mMessage = v.findViewById(R.id.displayMessage);

            // Follow and Gift
            mSystemMessage = v.findViewById(R.id.systemMessage);

            // Gift
            mGiftImg = v.findViewById(R.id.giftImageView);
        }

    }
}