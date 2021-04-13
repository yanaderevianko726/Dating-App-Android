package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.live.LiveStreamingActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.FollowModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.LiveStreamModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.greysonparrelli.permiso.Permiso;
import com.parse.ParseException;

import java.util.List;

public class UsersLiveAdapter extends RecyclerView.Adapter<UsersLiveAdapter.ViewHolder> {
    private List<LiveStreamModel> mStreaming;
    private Activity mActivity;


    public UsersLiveAdapter(Activity activity, List<LiveStreamModel> streamModels) {
        mStreaming = streamModels;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.list_item_live_broadcast_following_user, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        LiveStreamModel liveStreamModel = mStreaming.get(position);

        QuickHelp.getAvatars(liveStreamModel.getAuthor(), viewHolder.mUserPhoto);

        viewHolder.mFirstName.setText(liveStreamModel.getAuthor().getColFirstName());
        viewHolder.mViewerCount.setText(String.valueOf(liveStreamModel.getViewsLive()));

        FollowModel.queryFollowSingleUser(liveStreamModel.getAuthor(), new FollowModel.QueryFollowListener() {
            @Override
            public void onQueryFollowSuccess(boolean isFollowing) {

                if (isFollowing) {
                    viewHolder.mAddFavorite.setVisibility(View.GONE);
                    viewHolder.mAddFavorite.setEnabled(false);
                    viewHolder.mAddFavorite.setImageResource(R.drawable.ic_live_action_already_following);
                } else {
                    viewHolder.mAddFavorite.setVisibility(View.VISIBLE);
                    viewHolder.mAddFavorite.setEnabled(true);
                    viewHolder.mAddFavorite.setImageResource(R.drawable.ic_live_action_follow);
                }
            }

            @Override
            public void onQueryFollowError(ParseException error) {

                viewHolder.mAddFavorite.setVisibility(View.GONE);
            }
        });

        viewHolder.mUserPhoto.setOnClickListener(v -> {

            Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                @Override
                public void onPermissionResult(Permiso.ResultSet resultSet) {
                    if (resultSet.areAllPermissionsGranted()) {

                        if (QuickHelp.isInternetAvailable(mActivity)){
                            QuickHelp.goToActivityStreaming(mActivity, LiveStreamingActivity.LIVE_STREAMING_VIEWER, liveStreamModel, liveStreamModel.getObjectId());
                        } else {
                            QuickHelp.showNotification(mActivity, mActivity.getString(R.string.not_internet_connection), true);
                        }

                    } else {

                        QuickHelp.showToast(mActivity, mActivity.getString(R.string.msg_permission_required), true);
                    }
                }

                @Override
                public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                    Permiso.getInstance().showRationaleInDialog(null,
                            mActivity.getString(R.string.msg_permission_required),
                            null, callback);
                }
            }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        });

        viewHolder.mAddFavorite.setOnClickListener(v1 -> {

            viewHolder.mAddFavorite.setVisibility(View.VISIBLE);
            viewHolder.mAddFavorite.setEnabled(false);
            viewHolder.mAddFavorite.setImageResource(R.drawable.ic_live_action_already_following);

            if (liveStreamModel.getObjectId() != null){

                FollowModel followModel = new FollowModel();
                followModel.setFromAuthor(User.getUser());
                followModel.setToAuthor(liveStreamModel.getAuthor());
                followModel.saveInBackground(e -> {

                    if (e != null){

                        viewHolder.mAddFavorite.setVisibility(View.VISIBLE);
                        viewHolder.mAddFavorite.setEnabled(true);
                        viewHolder.mAddFavorite.setImageResource(R.drawable.ic_live_action_follow);

                    }
                });

            }

        });

    }

    @Override
    public int getItemCount() {
        return mStreaming.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mUserPhoto;
        TextView mFirstName, mViewerCount;
        ImageView mAddFavorite, mGoalIcon;

        ViewHolder(View v) {
            super(v);

            mUserPhoto = v.findViewById(R.id.liveBroadcastItem_userPhoto);
            mFirstName = v.findViewById(R.id.liveBroadcastItem_userName);
            mViewerCount = v.findViewById(R.id.liveBroadcastItem_viewersCount);
            mAddFavorite = v.findViewById(R.id.liveBroadcastItem_favouriteButton);
            mGoalIcon = v.findViewById(R.id.goalIcon);
        }
    }

}