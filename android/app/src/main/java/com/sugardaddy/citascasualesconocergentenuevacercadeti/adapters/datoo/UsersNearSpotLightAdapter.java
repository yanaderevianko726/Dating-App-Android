package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.payments.PaymentsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.RoundedImage;

import java.util.Date;
import java.util.List;

public class UsersNearSpotLightAdapter extends RecyclerView.Adapter<UsersNearSpotLightAdapter.ViewHolder> {

    private List<User> mUserList;
    private Activity mActivity;


    public UsersNearSpotLightAdapter(Activity activity, List<User> userList) {
        mUserList = userList;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.spotlight_add_user_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        User user = mUserList.get(position);

        QuickHelp.getAvatarsSpotlight(user, viewHolder.mImage);

        if (user.getObjectId().equals(User.getUser().getObjectId())){

            if (user.getVipMoreVisits() != null && new Date().before(user.getVipMoreVisits())){
                viewHolder.mPlusBtn.setVisibility(View.GONE);
            } else {
                viewHolder.mPlusBtn.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.mPlusBtn.setVisibility(View.GONE);
        }

        viewHolder.mImage.setOnClickListener(v -> {

            if (user.getObjectId().equals(User.getUser().getObjectId())){

                if (user.getVipMoreVisits() != null && new Date().before(user.getVipMoreVisits())){

                    QuickActions.showProfile(mActivity, user, true);

                } else if (user.getCredits() >= Config.TYPE_GET_MORE_VISITS){

                    QuickHelp.goToActivityFeatureActivation(mActivity, PaymentsActivity.TYPE_GET_MORE_VISITS);

                } else {

                    QuickHelp.goToActivityWithNoClean(mActivity, PaymentsActivity.class, PaymentsActivity.DATOO_PAYMENT_TYPE, PaymentsActivity.TYPE_GET_MORE_VISITS);
                }

            } else {
                QuickActions.showProfile(mActivity, user, false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImage mImage;
        FrameLayout mPlusBtn;

        ViewHolder(View v) {
            super(v);
            mImage = v.findViewById(R.id.spotlightProfilePhoto);
            mPlusBtn = v.findViewById(R.id.add_btn);

        }
    }

}