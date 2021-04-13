package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.ConnectionListModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;

import java.util.List;


public class ConnectionsSpotLightAdapter extends RecyclerView.Adapter<ConnectionsSpotLightAdapter.ViewHolder> {

    private List<ConnectionListModel> mConnectionListModels;
    private Activity mActivity;


    public ConnectionsSpotLightAdapter(Activity activity, List<ConnectionListModel> userList) {
        mConnectionListModels = userList;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.connection_spotlight_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ConnectionListModel connectionListModel = mConnectionListModels.get(position);

        if (connectionListModel.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){

            if (connectionListModel.getUserTo().isUserBlocked()){
                QuickHelp.getAvatars(connectionListModel.getUserTo(), viewHolder.mImage, true);
            } else {
                QuickHelp.getAvatars(connectionListModel.getUserTo(), viewHolder.mImage);
            }

            if (connectionListModel.isReadBySender()){

                viewHolder.mRead.setVisibility(View.GONE);

            } else {

                viewHolder.mRead.setVisibility(View.VISIBLE);
            }

        } else {

            if (connectionListModel.getUserFrom().isUserBlocked()){
                QuickHelp.getAvatars(connectionListModel.getUserTo(), viewHolder.mImage, true);
            } else {
                QuickHelp.getAvatars(connectionListModel.getUserFrom(), viewHolder.mImage);
            }

            if (connectionListModel.isReadByReceiver()){

                viewHolder.mRead.setVisibility(View.GONE);

            } else {

                viewHolder.mRead.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.mImage.setOnClickListener(v -> {

            if (connectionListModel.getUserFrom().getObjectId().equals(User.getUser().getObjectId())){

                if (connectionListModel.getUserTo().isUserBlocked()){
                    mActivity.getString(R.string.user_blocked_by_admin_alert);

                } else {

                    if (!connectionListModel.isReadBySender()){
                        connectionListModel.setReadBySender(true);
                        connectionListModel.saveEventually();
                    }

                    QuickHelp.goToActivityChat(mActivity, connectionListModel.getUserTo());
                }

            } else {

                if (connectionListModel.getUserFrom().isUserBlocked()){
                    mActivity.getString(R.string.user_blocked_by_admin_alert);

                } else {

                    if (!connectionListModel.isReadByReceiver()){
                        connectionListModel.setReadByReceiver(true);
                        connectionListModel.saveEventually();
                    }

                    QuickHelp.goToActivityChat(mActivity, connectionListModel.getUserFrom());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mConnectionListModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mImage;
        ImageView mRead;

        ViewHolder(View v) {
            super(v);
            mImage = v.findViewById(R.id.image);
            mRead = v.findViewById(R.id.read);

        }
    }

}