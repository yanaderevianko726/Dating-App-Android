package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.parse.ParseFile;

import java.util.List;


public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder> {

    private Activity mActivity;
    private List<ParseFile> mParseFiles;

    public PhotoGalleryAdapter(Activity activity, List<ParseFile> parseFiles) {
        this.mActivity = activity;
        this.mParseFiles = parseFiles;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_photo_viewer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mLoading.setVisibility(View.VISIBLE);

        ParseFile parseFile = mParseFiles.get(position);

        Glide.with(mActivity)
                .load(parseFile.getUrl())
                .fitCenter()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Glide.with(mActivity).load(resource).fitCenter().into(holder.mPhoto);

                        holder.mLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Remove the Drawable provided in onResourceReady from any Views and ensure
                        // no references to it remain.

                        holder.mLoading.setVisibility(View.GONE);
                    }
                });

        holder.mPhoto.setOnClickListener(v -> mActivity.onBackPressed());
    }

    @Override
    public int getItemCount() {
        return mParseFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;
        ProgressBar mLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.image_slider);
            mLoading = itemView.findViewById(R.id.progress_bar);
        }
    }
}
