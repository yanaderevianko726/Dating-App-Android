package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;

import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.CountriesModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.RecyclerViewFastScroller;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    private Activity mActivity;
    private List<CountriesModel> mCountriesModel;

    private LayoutInflater mInflater;
    private String SearchQuery;


    public CountriesAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mInflater = LayoutInflater.from(mActivity);
    }

    public List<CountriesModel> getCountries() {
        return mCountriesModel;
    }

    public void setCountries(List<CountriesModel> mCountriesModel) {
        this.mCountriesModel = mCountriesModel;
        notifyDataSetChanged();
    }

    //Methods for search start
    public void setString(String SearchQuery) {
        this.SearchQuery = SearchQuery;
        notifyDataSetChanged();
    }

    public void animateTo(List<CountriesModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<CountriesModel> newModels) {
        int arraySize = mCountriesModel.size();
        for (int i = arraySize - 1; i >= 0; i--) {
            final CountriesModel model = mCountriesModel.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CountriesModel> newModels) {
        int arraySize = newModels.size();
        for (int i = 0, count = arraySize; i < count; i++) {
            final CountriesModel model = newModels.get(i);
            if (!mCountriesModel.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CountriesModel> newModels) {
        int arraySize = newModels.size();
        for (int toPosition = arraySize - 1; toPosition >= 0; toPosition--) {
            final CountriesModel model = newModels.get(toPosition);
            final int fromPosition = mCountriesModel.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private CountriesModel removeItem(int position) {
        final CountriesModel model = mCountriesModel.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, CountriesModel model) {
        mCountriesModel.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final CountriesModel model = mCountriesModel.remove(fromPosition);
        mCountriesModel.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    //Methods for search end

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_countries, parent, false);
        return new CountriesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CountriesViewHolder countriesViewHolder = (CountriesViewHolder) holder;
        final CountriesModel countriesModel = mCountriesModel.get(holder.getAdapterPosition());

        if (countriesModel != null){

            countriesViewHolder.setCountryName(countriesModel.getName());
            countriesViewHolder.setCountryCode(countriesModel.getDial_code());
            SpannableString countryName = SpannableString.valueOf(countriesModel.getName());
            if (SearchQuery == null) {
                countriesViewHolder.countryName.setText(countryName, TextView.BufferType.NORMAL);
            } else {
                int index = TextUtils.indexOf(countriesModel.getName().toLowerCase(), SearchQuery.toLowerCase());
                if (index >= 0) {
                    countryName.setSpan(new ForegroundColorSpan(QuickHelp.getColor(mActivity, R.color.colorPrimary)), index, index + SearchQuery.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    countryName.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), index, index + SearchQuery.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

                countriesViewHolder.countryName.setText(countryName, TextView.BufferType.SPANNABLE);
            }
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        if (mCountriesModel != null) {
            return mCountriesModel.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        try {
            return mCountriesModel.size() > pos ? Character.toString(mCountriesModel.get(pos).getName().charAt(0)) : null;
        } catch (Exception e) {
            //AppHelper.LogCat(e.getValue());
            return e.getMessage();
        }
    }

    public class CountriesViewHolder extends RecyclerView.ViewHolder {

        TextView countryName;
        TextView countryCode;

        CountriesViewHolder(View itemView) {
            super(itemView);

            countryName = itemView.findViewById(R.id.country_name);
            countryCode = itemView.findViewById(R.id.country_code);

            itemView.setOnClickListener(view -> {
                try {
                    CountriesModel countriesModel = mCountriesModel.get(getAdapterPosition());
                    Intent mIntent = new Intent();
                    mIntent.putExtra("countryIso", countriesModel.getCode());
                    mIntent.putExtra("countryName", countriesModel.getName());
                    mActivity.setResult(Activity.RESULT_OK, mIntent);
                    mActivity.finish();
                } catch (Exception e) {
                    //AppHelper.LogCat("Exception CountriesAdapter " + e.getValue());
                }
            });
        }

        void setCountryName(String countryN) {
            countryName.setText(countryN);
        }

        void setCountryCode(String countryC) {
            countryCode.setText(countryC);
        }

    }


}

