package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.WithdrawModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.circularimageview.CircleImageView;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.DateUtils;

import java.util.List;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ViewHolder> {

    private List<WithdrawModel> withdrawModelList;
    private Activity mActivity;


    public WithdrawAdapter(Activity activity, List<WithdrawModel> withdrawModels) {
        withdrawModelList = withdrawModels;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_withdraw, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        WithdrawModel withdrawModel = withdrawModelList.get(position);

        QuickHelp.getAvatars(withdrawModel, viewHolder.mAvatar);

        if (withdrawModel.getType().equals(WithdrawModel.TYPE_WITHDRAW)){
            viewHolder.mTokens.setText(String.format(mActivity.getString(R.string.withdraw_money), withdrawModel.getTokens()));
            viewHolder.mCurrency.setText(mActivity.getString(R.string.us_dollar));
            viewHolder.mWithdrawMethod.setText(withdrawModel.getPayPalEmail());

        } else {
            viewHolder.mTokens.setText(String.format(mActivity.getString(R.string.withdraw_credits), withdrawModel.getTokens()));
            viewHolder.mCurrency.setText(mActivity.getString(R.string.credits));
            viewHolder.mWithdrawMethod.setVisibility(View.GONE);

        }

        viewHolder.mCredits.setText(String.valueOf(withdrawModel.getCredits()));

        if (withdrawModel.isCompleted()){
            viewHolder.mDate.setText(String.format(mActivity.getString(R.string.withdraw_completed), DateUtils.formatDateAndTime(withdrawModel.getUpdatedAt().getTime())));
        } else {
            viewHolder.mDate.setText(String.format(mActivity.getString(R.string.withdraw_pending), DateUtils.formatDateAndTime(withdrawModel.getCreatedAt().getTime())));
        }
    }

    @Override
    public int getItemCount() {
        return withdrawModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mAvatar;
        TextView mTokens, mCredits, mDate, mCurrency, mWithdrawMethod;

        ViewHolder(View v) {
            super(v);

            mAvatar = v.findViewById(R.id.avatar);
            mTokens = v.findViewById(R.id.tokens);
            mDate = v.findViewById(R.id.date);
            mCredits = v.findViewById(R.id.credit);
            mCurrency = v.findViewById(R.id.currency);
            mWithdrawMethod = v.findViewById(R.id.withdraw_method);
        }
    }

}