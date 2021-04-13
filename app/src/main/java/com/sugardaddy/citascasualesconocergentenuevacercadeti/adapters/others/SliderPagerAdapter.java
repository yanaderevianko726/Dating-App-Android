package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.PaymentSliderModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext;
    private View mView;
    private List<PaymentSliderModel> mPaymentSliderModel;

    public SliderPagerAdapter(Context context, List<PaymentSliderModel> paymentSliderModels) {
        mContext = context;
        mPaymentSliderModel = paymentSliderModels;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (layoutInflater != null) {
            mView = layoutInflater.inflate(R.layout.item_payment_slider, container, false);
        }

        PaymentSliderModel paymentSlider = mPaymentSliderModel.get(position);

        TextView titleText = mView.findViewById(R.id.title);
        ImageView avatarImg = mView.findViewById(R.id.avatar);
        ImageView badgeImg = mView.findViewById(R.id.popularity_promoBadgeRight);

        if (paymentSlider.getType().equals(PaymentSliderModel.SLIDER_YPE_NORMAL)){

            String sliderText = String.format("%s %s %s", mContext.getString(R.string.you_need), paymentSlider.getCredit(), paymentSlider.getTitle());


            SpannableStringBuilder sliderBuilder = new SpannableStringBuilder(sliderText);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

            // Apply the bold text style span
            sliderBuilder.setSpan(
                    boldSpan, // Span to add
                    sliderText.indexOf(paymentSlider.getCredit()), // Start of the span (inclusive)
                    sliderText.indexOf(paymentSlider.getCredit()) + String.valueOf(paymentSlider.getCredit()).length(), // End of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
            );

            titleText.setText(sliderBuilder);

            if (paymentSlider.getUser() != null){
                QuickHelp.getAvatar(avatarImg, paymentSlider.getUser());
            } else {
                QuickHelp.getAvatar(avatarImg, User.getUser());
            }



        } else if (paymentSlider.getType().equals(PaymentSliderModel.SLIDER_YPE_PREMIUM)){

            titleText.setText(paymentSlider.getTitle());

            if (paymentSlider.getUser() != null){
                QuickHelp.getAvatar(avatarImg, paymentSlider.getUser());
            } else {
                QuickHelp.getAvatar(avatarImg, User.getUser());
            }
        }



        badgeImg.setImageResource(paymentSlider.getBadgeImage());

        container.addView(mView);

        return mView;
    }

    @Override
    public int getCount() {
        return mPaymentSliderModel.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
