package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.SendNotifications;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.EncountersModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;

import java.util.List;

public class LikedYouAdapter extends RecyclerView.Adapter<LikedYouAdapter.ViewHolder> {
    private List<EncountersModel> mLikedYou;

    public LikedYouAdapter(List<EncountersModel> likedYouModels) {
        mLikedYou = likedYouModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.item_liked_you, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        EncountersModel likes = mLikedYou.get(position);

        viewHolder.liked.setVisibility(View.GONE);
        viewHolder.desliked.setVisibility(View.GONE);
        viewHolder.panel.setVisibility(View.VISIBLE);

        QuickHelp.getAvatars(likes.getFromUser(), viewHolder.userPhoto);

        if (likes.getFromUser().getBirthDate() != null){
            viewHolder.nameAndAge.setText(String.format("%s, %s", likes.getFromUser().getColFirstName(), QuickHelp.getAgeFromDate(likes.getFromUser().getBirthDate())));

        } else {
            viewHolder.nameAndAge.setText(likes.getFromUser().getColFirstName());

        }

        viewHolder.dislikeBtn.setOnClickListener(v1 -> {

            viewHolder.desliked.setVisibility(View.VISIBLE);
            viewHolder.panel.setVisibility(View.GONE);

            EncountersModel.newMatch(User.getUser(), likes.getFromUser(), false, true, e -> {
                if (e == null) {
                    Log.d("CardStackView", "saveMatch");

                    likes.setSeen(true);
                    likes.saveInBackground();

                } else {
                    Log.d("CardStackView:saveMatch", e.toString());
                }
            });
        });

        viewHolder.likeBtn.setOnClickListener(v1 -> {

            viewHolder.liked.setVisibility(View.VISIBLE);
            viewHolder.panel.setVisibility(View.GONE);

            EncountersModel.newMatch(User.getUser(), likes.getFromUser(), true, true,  e -> {
                if (e == null) {

                    likes.setSeen(true);
                    likes.saveInBackground();

                    SendNotifications.SendPush(User.getUser(), likes.getFromUser(), SendNotifications.PUSH_TYPE_LIKED_YOU, null);

                }
            });
        });


    }

    @Override
    public int getItemCount() {
        return mLikedYou.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userPhoto;
        TextView nameAndAge;
        ImageView dislikeBtn;
        ImageView likeBtn;

        RelativeLayout liked;
        RelativeLayout desliked;
        RelativeLayout panel;

        ViewHolder(View v) {
            super(v);

            userPhoto = v.findViewById(R.id.profilePhoto);
            nameAndAge = v.findViewById(R.id.nameAndAge);
            dislikeBtn = v.findViewById(R.id.dislikeBtn);
            likeBtn = v.findViewById(R.id.likeBtn);

            liked = v.findViewById(R.id.liked);
            desliked = v.findViewById(R.id.disliked);
            panel = v.findViewById(R.id.panel);
        }
    }

}