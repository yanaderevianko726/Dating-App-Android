package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.connections.ChatActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.UploadModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class PhotosChatUploadAdapter extends RecyclerView.Adapter {

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private static final int PHOTOS = 2;

    private ArrayList<UploadModel> uploadModelArrayList;
    private Activity mActivity;


    public PhotosChatUploadAdapter(Activity activity, ArrayList<UploadModel> uploadModels) {
        this.mActivity = activity;
        uploadModelArrayList = uploadModels;
    }

    @Override
    public int getItemViewType(int position) {
        UploadModel item = uploadModelArrayList.get(position);

        if (position == 0 && item.getImageUrl().equals("camera")){

            return CAMERA;

        } else if (position == 1 && item.getImageUrl().equals("gallery")){

            return GALLERY;

        } else {

            return PHOTOS;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.photos_item_chat, parent, false);

        return new ViewHolder(contactView);*/

        switch (viewType) {
            case CAMERA:

                View CAMERA = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_camera_item, parent, false);
                return new ViewHolder(CAMERA);

            case GALLERY:

                View GALLERY = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_gallery_item, parent, false);
                return new ViewHolder(GALLERY);

            case PHOTOS:

                View PHOTOS = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_item_chat, parent, false);
                return new ViewHolder(PHOTOS);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        UploadModel selectableItem = uploadModelArrayList.get(position);

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.white_alpha_15));

        int viewType = getItemViewType(position);

        switch (viewType) {

            case CAMERA:

                holder.ItemLayout.setOnClickListener(v -> ((ChatActivity) mActivity).takeFromCamera());

                break;

            case GALLERY:

                holder.ItemLayout.setOnClickListener(v -> ((ChatActivity) mActivity).pickFromGallery());

                break;

            case PHOTOS:

                holder.ItemLayout.setOnClickListener(v -> ((ChatActivity) mActivity).checkMessagesLimit(true, selectableItem.getImageUrl()));

                Glide.with(Application.getInstance().getApplicationContext())
                        .load(selectableItem.getImageUrl())
                        .error(colorDrawable)
                        .centerCrop()
                        .circleCrop()
                        .fitCenter()
                        .placeholder(colorDrawable)
                        .into(holder.Photo);

                break;

        }
    }

    @Override
    public int getItemCount() {
        return uploadModelArrayList.size();
    }


    public static class  ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout ItemLayout;
        ImageView Photo;

        ViewHolder(View v) {
            super(v);

            ItemLayout = v.findViewById(R.id.layout_photo_item);
            Photo = v.findViewById(R.id.photo);
        }
    }

}