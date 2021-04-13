package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;

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
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.nearby.ManualLocationActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.PlacesModel;

import java.util.List;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private List<PlacesModel> mPlacesModels;
    private Activity mActivity;


    public PlacesListAdapter(Activity activity, List<PlacesModel> placesModels) {
        mPlacesModels = placesModels;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        view = inflater.inflate(R.layout.item_places, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PlacesModel place = mPlacesModels.get(position);

        viewHolder.mTextAddress.setText(place.getAddress());

        viewHolder.mLayout.setOnClickListener(v -> ((ManualLocationActivity) mActivity).saveNewLocation(place.placeId, place.getAddress()));

    }

    @Override
    public int getItemCount() {
        return mPlacesModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLayout;
        TextView mTextAddress;

        ViewHolder(View v) {
            super(v);
            mLayout = v.findViewById(R.id.place_layout);
            mTextAddress = v.findViewById(R.id.place_address);

        }
    }

}