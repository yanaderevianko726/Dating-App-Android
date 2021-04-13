package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.RoundedImage;
import com.parse.ParseFile;

import java.util.List;

public class OwnProfilePhotosAdapter extends RecyclerView.Adapter<OwnProfilePhotosAdapter.ViewHolder> {

    private User mUser;
    private List<ParseFile> parseFiles;
    private Activity mActivity;


    public OwnProfilePhotosAdapter(Activity activity, List<ParseFile> parseFileList, User user) {
        parseFiles = parseFileList;
        mActivity = activity;
        mUser = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.list_item_my_profile_photo, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ParseFile parseFile = parseFiles.get(position);

        QuickHelp.getProfilePhotos(parseFile.getUrl(), viewHolder.mImage);

        if (position == User.getUser().getAvatarPhotoPosition()){
            viewHolder.mPhotoSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mPhotoSelected.setVisibility(View.GONE);
        }

        viewHolder.mImage.setOnClickListener(v -> QuickHelp.goToActivityPhotoViewer(mActivity, mUser, position));

    }

    @Override
    public int getItemCount() {
        return parseFiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mPhotoSelected;
        RoundedImage mImage;

        ViewHolder(View v) {
            super(v);

            mPhotoSelected = v.findViewById(R.id.photo_photoSelected);
            mImage = v.findViewById(R.id.photo_photo);

        }
    }

}