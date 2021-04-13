package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.bumptech.glide.Glide;

import java.util.List;

public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.ViewHolder> {
    private List<String> mPhotosList;

    public PhotosListAdapter(List<String> list) {
        mPhotosList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.photos_list_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String url = mPhotosList.get(position);


        QuickHelp.setAnimation(viewHolder.itemView, position);


        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.white_alpha_15));

        Glide.with(Application.getInstance().getApplicationContext())
                .load(url)
                .error(colorDrawable)
                .centerCrop()
                .circleCrop()
                .fitCenter()
                .placeholder(colorDrawable)
                .into(viewHolder.photo);

    }

    @Override
    public int getItemCount() {
        return mPhotosList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;

        ViewHolder(View v) {
            super(v);

            photo = v.findViewById(R.id.photo);
        }
    }

}