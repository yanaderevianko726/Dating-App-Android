package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads.GalleryFragment;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.UploadModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PhotosUploadAdapter extends RecyclerView.Adapter {

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private static final int PHOTOS = 2;


    private ArrayList<UploadModel> uploadModelArrayList;
    private ViewHolder.OnItemSelectedListener itemSelectedListener;
    private ViewHolder.OnItemUnSelectedListener itemUnSelectedListener;
    private Fragment mActivity;


    public PhotosUploadAdapter(ViewHolder.OnItemSelectedListener itemSelectedListener, ViewHolder.OnItemUnSelectedListener itemUnSelectedListener, Fragment activity, ArrayList<UploadModel> uploadModels) {
        this.itemSelectedListener = itemSelectedListener;
        this.itemUnSelectedListener = itemUnSelectedListener;
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
        //Context context = parent.getContext();
        //LayoutInflater inflater = LayoutInflater.from(context);
        //View contactView = inflater.inflate(R.layout.photos_item, parent, false);

        switch (viewType) {
            case CAMERA:

                View CAMERA = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_camera_item, parent, false);
                return new ViewHolder(CAMERA, itemSelectedListener, itemUnSelectedListener);

            case GALLERY:

                View GALLERY = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_gallery_item, parent, false);
                return new ViewHolder(GALLERY, itemSelectedListener, itemUnSelectedListener);

            case PHOTOS:

                View PHOTOS = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_item, parent, false);
                return new ViewHolder(PHOTOS, itemSelectedListener, itemUnSelectedListener);

        }

        return null;
        //return new ViewHolder(contactView, itemSelectedListener, itemUnSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        UploadModel selectableItem = uploadModelArrayList.get(position);

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.white_alpha_15));

        int viewType = getItemViewType(position);

        switch (viewType) {

            case CAMERA:

                holder.ItemLayout.setOnClickListener(v -> ((GalleryFragment) mActivity).takeFromCamera());

                break;

            case GALLERY:

                holder.ItemLayout.setOnClickListener(v -> ((GalleryFragment) mActivity).pickFromGallery());

                break;

            case PHOTOS:

                Glide.with(Application.getInstance().getApplicationContext())
                        .load(selectableItem.getImageUrl())
                        .error(colorDrawable)
                        .centerCrop()
                        .circleCrop()
                        .fitCenter()
                        .placeholder(colorDrawable)
                        .into(holder.Photo);

                holder.Selection.setVisibility(View.VISIBLE);
                holder.ItemLayout.setBackgroundColor(Application.getInstance().getResources().getColor(R.color.white_alpha_15));

                holder.mItem = selectableItem;
                holder.setChecked(holder.mItem.isSelected());

                break;

        }
    }

    @Override
    public int getItemCount() {
        return uploadModelArrayList.size();
    }

    public void setSelected(UploadModel uploadModel){

        itemSelectedListener.onItemSelected(uploadModel);
    }


    public static class  ViewHolder extends RecyclerView.ViewHolder {

        UploadModel mItem;
        OnItemSelectedListener itemSelectedListener;
        OnItemUnSelectedListener itemUnSelectedListener;

        RelativeLayout ItemLayout;
        ImageView Photo;
        ImageView Selection;

        ViewHolder(View v, OnItemSelectedListener itemSelected, OnItemUnSelectedListener itemUnSelected) {
            super(v);

            itemSelectedListener = itemSelected;
            itemUnSelectedListener = itemUnSelected;

            ItemLayout = v.findViewById(R.id.layout_photo_item);
            Photo = v.findViewById(R.id.photo);
            Selection = v.findViewById(R.id.selection);

            ItemLayout.setOnClickListener(view -> {

                if (mItem.isSelected()) {
                    setChecked(false);
                    itemUnSelectedListener.onItemUnSelected(mItem);
                } else {
                    setChecked(true);
                    itemSelectedListener.onItemSelected(mItem);
                }

            });
        }

        public void setChecked(boolean value) {
            if (value) {
                Selection.setImageResource(R.drawable.ic_checkbox_checked);
            } else {
                Selection.setImageResource(R.drawable.ic_checkbox_unchecked);
            }
            mItem.setSelected(value);
        }

        public interface OnItemSelectedListener {

            void onItemSelected(UploadModel item);
        }

        public interface OnItemUnSelectedListener {

            void onItemUnSelected(UploadModel item);
        }
    }

}