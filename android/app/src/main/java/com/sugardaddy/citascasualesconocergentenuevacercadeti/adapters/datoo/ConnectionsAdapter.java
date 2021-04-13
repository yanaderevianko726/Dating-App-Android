package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.DateUtils;

import java.util.List;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ViewHolder> {
    private List<ConnectionListModel> mConnectionsModels;
    private Activity mActivity;


    public ConnectionsAdapter(Activity activity, List<ConnectionListModel> connectionsModels) {
        mConnectionsModels = connectionsModels;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.list_item_conversation, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ConnectionListModel connection = mConnectionsModels.get(position);

        if (connection != null){

            if (connection.getConnectionType() != null && connection.getConnectionType().equals(ConnectionListModel.CONNECTION_TYPE_MESSAGE)){

                viewHolder.favorite.setVisibility(View.GONE);

                if (connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){

                    QuickHelp.getAvatars(connection.getUserTo(), viewHolder.userPhoto);
                    viewHolder.fullName.setText(connection.getUserTo().getColFullName());

                } else {

                    QuickHelp.getAvatars(connection.getUserFrom(), viewHolder.userPhoto);
                    viewHolder.fullName.setText(connection.getUserFrom().getColFullName());
                }

                if (connection.getColMessageType().equals(ConnectionListModel.MESSAGE_TYPE_MATCHED)){

                    viewHolder.countContainer.setVisibility(View.GONE);
                    viewHolder.iconBadge.setVisibility(View.VISIBLE);
                    viewHolder.iconBadge.setImageResource(R.drawable.ic_badge_feature_match);

                    viewHolder.description.setText(String.format("%s %s", mActivity.getString(R.string.you_matched), DateUtils.getTimeAgo(connection.getUpdatedAt().getTime())));
                    viewHolder.description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                } else {

                    viewHolder.iconBadge.setVisibility(View.GONE);

                    switch (connection.getColMessageType()) {
                        case ConnectionListModel.MESSAGE_TYPE_CHAT:
                            viewHolder.description.setText(connection.getText());

                            if (!connection.isRead() && !connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){
                                viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.black));

                            } else {
                                viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.gray_dark));
                            }

                            viewHolder.description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                            break;

                        case ConnectionListModel.MESSAGE_TYPE_IMAGE:

                            if (connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())) {
                                viewHolder.description.setText(mActivity.getString(R.string.image_message));

                            } else {

                                viewHolder.description.setText(mActivity.getString(R.string.image_message_received));
                            }

                            if (!connection.isRead() && !connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){
                                viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.black));

                            } else {
                                viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.gray_dark));
                            }

                            viewHolder.description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                            break;

                        case ConnectionListModel.MESSAGE_TYPE_CALL:

                            viewHolder.description.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_generic_media_video, 0, 0, 0);
                            int padding = mActivity.getResources().getDimensionPixelSize(R.dimen.call_connection_padding);
                            viewHolder.description.setCompoundDrawablePadding(padding);

                            if (connection.getCall() != null && connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())) {

                                if (connection.getCall().isAccepted()) {

                                    if (connection.getCall().isVoiceCall()){
                                        viewHolder.description.setText(String.format(mActivity.getString(R.string.calls_caller_user_voice), connection.getUserTo().getColFirstName(), connection.getCall().getDuration()));
                                    } else {
                                        viewHolder.description.setText(String.format(mActivity.getString(R.string.calls_caller_user), connection.getUserTo().getColFirstName(), connection.getCall().getDuration()));
                                    }

                                    viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.gray_dark));
                                    QuickHelp.setDrawableTint(viewHolder.description, R.color.gray_dark);
                                } else {
                                    viewHolder.description.setText(String.format(mActivity.getString(R.string.calls_caller_missed_user), connection.getUserTo().getColFirstName()));
                                    viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.generic_red));
                                    QuickHelp.setDrawableTint(viewHolder.description, R.color.generic_red);
                                }

                            } else if (connection.getCall() != null && connection.getUserTo().getObjectId().equals(User.getUser().getObjectId())) {

                                if (connection.getCall() != null && connection.getCall().isAccepted()) {

                                    if (connection.getCall().isVoiceCall()){
                                        viewHolder.description.setText(String.format(mActivity.getString(R.string.calls_callee_user_voice), connection.getUserTo().getColFirstName(), connection.getCall().getDuration()));
                                    } else {
                                        viewHolder.description.setText(String.format(mActivity.getString(R.string.calls_callee_user), connection.getUserFrom().getColFirstName(), connection.getCall().getDuration()));
                                    }

                                    viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.gray_dark));
                                    QuickHelp.setDrawableTint(viewHolder.description, R.color.gray_dark);
                                } else {
                                    viewHolder.description.setText(String.format(mActivity.getString(R.string.calls_callee_missed_user), connection.getUserFrom().getColFirstName()));
                                    viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.generic_red));
                                    QuickHelp.setDrawableTint(viewHolder.description, R.color.generic_red);
                                }

                            } else {

                                viewHolder.description.setTextColor(mActivity.getResources().getColor(R.color.gray_dark));
                                viewHolder.description.setText(mActivity.getString(R.string.call_no_object));
                            }

                            break;
                    }

                    if (!connection.isRead() && !connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){
                        viewHolder.countContainer.setVisibility(View.VISIBLE);
                        viewHolder.counter.setVisibility(View.VISIBLE);
                        viewHolder.counter.setText(String.valueOf(connection.getCount()));

                    } else {

                        viewHolder.countContainer.setVisibility(View.GONE);
                    }
                }


            } else if (connection.getConnectionType() != null && connection.getConnectionType().equals(ConnectionListModel.CONNECTION_TYPE_VISITOR)){

                viewHolder.favorite.setVisibility(View.GONE);

                viewHolder.countContainer.setVisibility(View.GONE);
                viewHolder.iconBadge.setVisibility(View.GONE);

                QuickHelp.getAvatars(connection.getUserFrom(), viewHolder.userPhoto);
                viewHolder.fullName.setText(connection.getUserFrom().getColFullName());
                viewHolder.description.setText(String.format("%s %s", mActivity.getString(R.string.visited_you), DateUtils.getTimeAgo(connection.getUpdatedAt().getTime())));

                viewHolder.description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            } else if (connection.getConnectionType() != null && connection.getConnectionType().equals(ConnectionListModel.CONNECTION_TYPE_FAVORITE)){

                viewHolder.description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                viewHolder.favorite.setVisibility(View.VISIBLE);

                viewHolder.countContainer.setVisibility(View.GONE);
                viewHolder.iconBadge.setVisibility(View.GONE);

                viewHolder.description.setText(String.format("%s %s", mActivity.getString(R.string.your_favorite), DateUtils.getTimeAgo(connection.getUpdatedAt().getTime())));

                if (connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){

                    QuickHelp.getAvatars(connection.getUserTo(), viewHolder.userPhoto);
                    viewHolder.fullName.setText(connection.getUserTo().getColFullName());
                    viewHolder.favorite.setImageResource(R.drawable.ic_profile_favorite_added);

                } else {

                    QuickHelp.getAvatars(connection.getUserFrom()  , viewHolder.userPhoto);
                    viewHolder.fullName.setText(connection.getUserFrom().getColFullName());
                    viewHolder.favorite.setImageResource(R.drawable.ic_profile_favorite);
                }
            }

            viewHolder.userItemLayout.setOnClickListener(v -> {

                switch (connection.getConnectionType()) {
                    case ConnectionListModel.CONNECTION_TYPE_MESSAGE:

                        if (connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())) {

                            if (connection.getUserTo().isUserBlocked()){
                                mActivity.getString(R.string.user_blocked_by_admin_alert);
                            } else {
                                QuickHelp.goToActivityChat(mActivity, connection.getUserTo());
                            }

                        } else {

                            if (connection.getUserFrom().isUserBlocked()){
                                mActivity.getString(R.string.user_blocked_by_admin_alert);
                            } else {
                                QuickHelp.goToActivityChat(mActivity, connection.getUserFrom());
                            }
                        }

                        break;
                    case ConnectionListModel.CONNECTION_TYPE_VISITOR:

                        if (connection.getUserFrom().isUserBlocked()){
                            mActivity.getString(R.string.user_blocked_by_admin_alert);
                        } else {
                            QuickActions.showProfile(mActivity, connection.getUserFrom(), false);
                        }

                        break;
                    case ConnectionListModel.CONNECTION_TYPE_FAVORITE:

                        if (connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())) {

                            if (connection.getUserTo().isUserBlocked()){
                                mActivity.getString(R.string.user_blocked_by_admin_alert);
                            } else {
                                QuickActions.showProfile(mActivity, connection.getUserTo(), false);
                            }

                        } else {

                            QuickActions.showProfile(mActivity, connection.getUserFrom(), false);
                        }
                        break;
                }
            });

            if (connection.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){

                if (connection.getUserTo().getLastOnline() != null){


                    if (!User.getUser().getPrivacyOnlineStatusEnabled()){

                        viewHolder.userStatus.setVisibility(View.GONE);

                    } else if (!connection.getUserTo().getPrivacyOnlineStatusEnabled()){

                        viewHolder.userStatus.setVisibility(View.GONE);

                    } else {

                        if (System.currentTimeMillis() - connection.getUserTo().getUpdatedAt().getTime() > Constants.TIME_TO_OFFLINE) {

                            viewHolder.userStatus.setVisibility(View.GONE);

                        } else if (System.currentTimeMillis() - connection.getUserTo().getUpdatedAt().getTime() > Constants.TIME_TO_SOON) {

                            viewHolder.userStatus.setVisibility(View.VISIBLE);
                            viewHolder.userStatus.setImageResource(R.color.orange_500);

                        } else {

                            viewHolder.userStatus.setVisibility(View.VISIBLE);
                            viewHolder.userStatus.setImageResource(R.color.green_500);
                        }
                    }

                } else {

                    viewHolder.userStatus.setVisibility(View.GONE);
                }

            } else {

                if (connection.getUserFrom().getLastOnline() != null){


                    if (!User.getUser().getPrivacyOnlineStatusEnabled()){

                        viewHolder.userStatus.setVisibility(View.GONE);

                    } else if (!connection.getUserFrom().getPrivacyOnlineStatusEnabled()){

                        viewHolder.userStatus.setVisibility(View.GONE);

                    } else {

                        if (System.currentTimeMillis() - connection.getUserFrom().getUpdatedAt().getTime() > Constants.TIME_TO_OFFLINE) {

                            viewHolder.userStatus.setVisibility(View.GONE);

                        } else if (System.currentTimeMillis() - connection.getUserFrom().getUpdatedAt().getTime() > Constants.TIME_TO_SOON) {

                            viewHolder.userStatus.setVisibility(View.VISIBLE);
                            viewHolder.userStatus.setImageResource(R.color.orange_500);

                        } else {

                            viewHolder.userStatus.setVisibility(View.VISIBLE);
                            viewHolder.userStatus.setImageResource(R.color.green_500);
                        }
                    }

                } else {

                    viewHolder.userStatus.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mConnectionsModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout userItemLayout;
        CircleImageView userPhoto, userStatus;
        TextView fullName, description, counter;
        ImageView favorite, iconBadge;
        FrameLayout countContainer;

        ViewHolder(View v) {
            super(v);

            userItemLayout = v.findViewById(R.id.root);
            userPhoto = v.findViewById(R.id.image);
            fullName = v.findViewById(R.id.title);
            countContainer = v.findViewById(R.id.countContainer);
            counter = v.findViewById(R.id.countBadge);
            iconBadge = v.findViewById(R.id.iconBadge);
            description = v.findViewById(R.id.message);
            favorite = v.findViewById(R.id.favourite);
            userStatus = v.findViewById(R.id.peopleNearby_personStatus);
        }
    }

}