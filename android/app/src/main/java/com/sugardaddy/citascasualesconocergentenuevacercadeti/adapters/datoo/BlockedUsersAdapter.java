package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;

import java.util.List;


public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.ViewHolder> {
    private List<User> mUsers;
    private Activity mActivity;


    public BlockedUsersAdapter(Activity activity, List<User> users) {
        mUsers = users;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.list_item_blocked, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        User user = mUsers.get(position);

        QuickHelp.getAvatars(user, viewHolder.mImage);
        viewHolder.mFullName.setText(user.getColFullName());

        viewHolder.mLayout.setOnClickListener(v -> QuickActions.showProfile(mActivity, user, false));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLayout;
        TextView mFullName;
        CircleImageView mImage;

        ViewHolder(View v) {
            super(v);

            mLayout = v.findViewById(R.id.blocked_users);
            mImage = v.findViewById(R.id.image);
            mFullName = v.findViewById(R.id.name);
        }
    }

}