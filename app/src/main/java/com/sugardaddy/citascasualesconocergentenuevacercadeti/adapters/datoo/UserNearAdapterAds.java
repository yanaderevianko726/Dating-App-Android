package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;
import java.util.Locale;

public class UserNearAdapterAds extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_USERS = 0;
    private static final int ITEM_VIEW_TYPE_ADS = 1;

    private Activity mActivity;
    private List<Object> mObjctsList;

    public UserNearAdapterAds(Activity activity, List<Object> objectList) {
        this.mObjctsList = objectList;
        this.mActivity = activity;
        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW_TYPE_ADS) {
            View unifiedNativeLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_nearby_native_ads, parent, false);
            return new NativeAdViewHolder(unifiedNativeLayoutView);
        }

        View menuItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_near, parent, false);
        return new ViewHolder(menuItemLayoutView);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

        int viewType = getItemViewType(i);

        StaggeredGridLayoutManager.LayoutParams layoutParams;
        if (viewType == ITEM_VIEW_TYPE_ADS) {

            UnifiedNativeAd nativeAd = (UnifiedNativeAd) mObjctsList.get(i);
            setNativeAdView(nativeAd, ((NativeAdViewHolder) viewHolder).getAdView());
            layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);

        } else {

            User user = (User) mObjctsList.get(i);

            ViewHolder holder = (ViewHolder) viewHolder;
            holder.firstName.setText(user.getColFirstName());
            holder.userPhoto.setClipToOutline(true);

            layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(false);

            QuickHelp.getAvatars(user, holder.userPhoto);

            holder.setIsRecyclable(false);

            if (user.getGeoPoint() != null && User.getUser().getGeoPoint() != null) {

                if (user.getGeoPoint().distanceInKilometersTo(User.getUser().getGeoPoint()) <= Config.DistanceForRealBadge) {

                    holder.userNearBadge.setVisibility(View.VISIBLE);
                    holder.mUserDistance.setVisibility(View.GONE);

                } else if (user.getGeoPoint().distanceInKilometersTo(User.getUser().getGeoPoint()) <= Config.DistanceForRealKm) {

                    holder.userNearBadge.setVisibility(View.GONE);

                    if (!User.getUser().getPrivacyDistanceEnabled()) {

                        holder.mUserDistance.setVisibility(View.GONE);

                    } else if (!user.getPrivacyDistanceEnabled()) {

                        holder.mUserDistance.setVisibility(View.GONE);

                    } else {

                        holder.mUserDistance.setVisibility(View.VISIBLE);
                        holder.mUserDistance.setText(String.format(Locale.US, "%.2f km", user.getGeoPoint().distanceInKilometersTo(User.getUser().getGeoPoint())));
                    }

                } else {

                    holder.mUserDistance.setVisibility(View.GONE);
                    holder.userNearBadge.setVisibility(View.GONE);
                }

            } else {

                holder.mUserDistance.setVisibility(View.GONE);
                holder.userNearBadge.setVisibility(View.GONE);
            }

            holder.userPhoto.setOnClickListener(v1 -> {

                if (user.getObjectId() != null) {
                    // Go to user profile

                    QuickActions.showProfile(mActivity, user, false);

                }

            });

            if (user.getLastOnline() != null) {


                if (!User.getUser().getPrivacyOnlineStatusEnabled()) {

                    holder.userStatus.setVisibility(View.GONE);

                } else if (!user.getPrivacyOnlineStatusEnabled()) {

                    holder.userStatus.setVisibility(View.GONE);

                } else {

                    if (System.currentTimeMillis() - user.getUpdatedAt().getTime() > Constants.TIME_TO_OFFLINE) {

                        holder.userStatus.setVisibility(View.GONE);

                    } else if (System.currentTimeMillis() - user.getUpdatedAt().getTime() > Constants.TIME_TO_SOON) {

                        holder.userStatus.setVisibility(View.VISIBLE);
                        holder.userStatus.setImageResource(R.color.orange_500);

                    } else {

                        holder.userStatus.setVisibility(View.VISIBLE);
                        holder.userStatus.setImageResource(R.color.green_500);
                    }
                }

            } else {

                holder.userStatus.setVisibility(View.GONE);
            }
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
    public long getItemId(int position) {
        Object recyclerViewItem = mObjctsList.get(position);

        if (recyclerViewItem instanceof UnifiedNativeAd){
            UnifiedNativeAd nativeAd = (UnifiedNativeAd) mObjctsList.get(position);
            return nativeAd.hashCode();
        } else {
            User user = (User) mObjctsList.get(position);
            return user.getUid();
        }
        //return position;
    }

    @Override
    public int getItemCount() {
        return mObjctsList.size();
    }


    static class NativeAdViewHolder extends RecyclerView.ViewHolder {

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

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout userItemLayout;
        CircleImageView userStatus;
        CircleImageView userPhoto;
        ImageView userNearBadge;
        TextView firstName, mUserDistance;

        ViewHolder(View v) {
            super(v);

            userItemLayout = v.findViewById(R.id.users_item_layout);
            userPhoto = v.findViewById(R.id.peopleNearby_personImage);
            firstName = v.findViewById(R.id.peopleNearby_personName);
            userNearBadge = v.findViewById(R.id.peopleNearby_personBadge);
            userStatus = v.findViewById(R.id.peopleNearby_personStatus);
            mUserDistance = v.findViewById(R.id.peopleNearby_distance);
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
