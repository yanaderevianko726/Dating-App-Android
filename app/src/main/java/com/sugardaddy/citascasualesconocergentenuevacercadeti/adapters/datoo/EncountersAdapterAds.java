package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.encounters.EncountersFragmentAds;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

public class EncountersAdapterAds extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_USERS = 0;
    private static final int ITEM_VIEW_TYPE_ADS = 1;

    private EncountersFragmentAds mEncountersFragment;
    private List<Object> mObjctsList;

    public EncountersAdapterAds(List<Object> likedYouModels, EncountersFragmentAds encountersFragmentAds) {
        mObjctsList = likedYouModels;
        mEncountersFragment = encountersFragmentAds;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW_TYPE_ADS) {
            View unifiedNativeLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_encounters_native_ads, parent, false);
            return new NativeAdViewHolder(unifiedNativeLayoutView);
        }

        View menuItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encounters, parent, false);
        return new ViewHolder(menuItemLayoutView);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

        int viewType = getItemViewType(i);
        if (viewType == ITEM_VIEW_TYPE_ADS) {

            UnifiedNativeAd nativeAd = (UnifiedNativeAd) mObjctsList.get(i);
            setNativeAdView(nativeAd, ((NativeAdViewHolder) viewHolder).getAdView());

        } else {

            User user = (User) mObjctsList.get(i);

            ViewHolder holder = (ViewHolder) viewHolder;

            QuickHelp.getEncountersAvatars(user, holder.userPhoto);

            if (user.getBirthDate() != null){
                holder.nameAndAge.setText(String.format("%s, %s", user.getColFullName(), QuickHelp.getAgeFromDate(user.getBirthDate())));
            } else {
                holder.nameAndAge.setText(user.getColFullName());
            }


            holder.userCity.setText(QuickHelp.getOnlyCityFromLocation(user));

            holder.userPhoto.setOnClickListener(v1 -> {

                if (user.getProfilePhotos().size() > 0){

                    QuickHelp.goToActivityPhotoViewer(mEncountersFragment.getActivity(), user, user.getAvatarPhotoPosition());
                }

            });


            holder.likeBtn.setOnClickListener(v -> mEncountersFragment.likeUser());
            holder.dislikeBtn.setOnClickListener(v -> mEncountersFragment.skipUser());

            holder.setIsRecyclable(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = mObjctsList.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {

            return ITEM_VIEW_TYPE_ADS;
        }
        return ITEM_VIEW_TYPE_USERS;
    }

    @Override
    public int getItemCount() {
        return mObjctsList.size();
    }


    class NativeAdViewHolder extends RecyclerView.ViewHolder {

        private UnifiedNativeAdView adView;

        UnifiedNativeAdView getAdView() {
            return adView;
        }

        NativeAdViewHolder(View view) {
            super(view);
            adView = view.findViewById(R.id.ad_view);

            adView.setMediaView(adView.findViewById(R.id.ad_media));
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userPhoto;
        TextView nameAndAge;
        TextView userCity;

        ImageView dislikeBtn;
        ImageView likeBtn;

        ViewHolder(View v) {
            super(v);

            userPhoto = v.findViewById(R.id.profilePhoto);
            nameAndAge = v.findViewById(R.id.nameAndAge);
            userCity = v.findViewById(R.id.location);
            dislikeBtn = v.findViewById(R.id.dislikeBtn);
            likeBtn = v.findViewById(R.id.likeBtn);
        }
    }

    private void setNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }
}
