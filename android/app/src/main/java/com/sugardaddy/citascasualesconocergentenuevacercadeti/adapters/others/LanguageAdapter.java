package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.SplashScreen;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.LanguageModel;

import java.util.List;
import java.util.Locale;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private List<LanguageModel> languageList;
    private Activity mActivity;

    public LanguageAdapter(Activity activity, List<LanguageModel> list) {
        languageList = list;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_language, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        LanguageModel languageModel = languageList.get(position);

        if (languageModel.getCode().equals(User.getUser().getLanguage())){
            viewHolder.layout.setBackgroundColor(QuickHelp.getColor(Application.getInstance().getApplicationContext(), R.color.colorPrimary));
            viewHolder.name.setTextColor(QuickHelp.getColor(Application.getInstance().getApplicationContext(), R.color.white));
        } else {
            viewHolder.layout.setBackgroundColor(QuickHelp.getColor(Application.getInstance().getApplicationContext(), R.color.white));
            viewHolder.name.setTextColor(QuickHelp.getColor(Application.getInstance().getApplicationContext(), R.color.black));
        }

        viewHolder.name.setText(languageModel.getName());

        viewHolder.layout.setOnClickListener(view -> {

            QuickHelp.showLoading(mActivity, false);

            User.getUser().setLanguage(languageModel.getCode());
            User.getUser().saveInBackground(e -> {
                if (e == null){
                    QuickHelp.hideLoading();
                    setDefaultLocale(new Locale(languageModel.getCode()));
                } else {
                    QuickHelp.hideLoading();
                    QuickHelp.showToast(mActivity, mActivity.getString(R.string.error_ocurred), true);
                }
            });
        });

    }

    protected void setDefaultLocale(Locale locale) {

        Locale.setDefault(locale);
        Configuration configLang = new Configuration();
        configLang.locale = locale;

        mActivity.getResources().updateConfiguration(configLang, mActivity.getResources().getDisplayMetrics());

        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setMessage(R.string.you_need_to_restart_the_application);
        alert.setPositiveButton(R.string.ok, (dialog, which) -> {

            QuickHelp.goToActivityAndFinish(mActivity, SplashScreen.class);

        });
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView name;

        ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.layout);
            name = v.findViewById(R.id.language_name);
        }
    }

}