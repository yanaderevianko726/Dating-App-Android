package com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others;


import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;

import java.util.ArrayList;
import java.util.List;

public class TabUploadsAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabUploadsAdapter(FragmentManager fm) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(Application.getInstance().getApplicationContext()).inflate(R.layout.upload_tabbar, null);

        ImageView img = view.findViewById(R.id.img);
        TextView title = view.findViewById(R.id.text);

        if (position == 0){

            img.setImageResource(R.drawable.ic_upload_photo_source_gallery);
            title.setTypeface(null, Typeface.BOLD);
            title.setTextColor(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.black));

        } else if (position == 1){

            img.setImageResource(R.drawable.ic_import_instagram);
            title.setTypeface(null, Typeface.BOLD);
            title.setTextColor(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.black));

        } else if (position == 2){

            img.setImageResource(R.drawable.ic_facebook);
            title.setTypeface(null, Typeface.BOLD);
            title.setTextColor(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.black));

        }

        title.setText(mFragmentTitleList.get(position));

        return view;
    }

    public View getUnSelectedTabView(int position) {
        View view = LayoutInflater.from(Application.getInstance().getApplicationContext()).inflate(R.layout.upload_tabbar, null);

        ImageView img = view.findViewById(R.id.img);
        TextView title = view.findViewById(R.id.text);

        if (position == 0){

            img.setImageResource(R.drawable.ic_upload_photo_source_gallery);

            title.setTypeface(null, Typeface.NORMAL);
            title.setTextColor(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.grey_400));

        } else if (position == 1){

            img.setImageResource(R.drawable.ic_import_instagram);

            title.setTypeface(null, Typeface.NORMAL);
            title.setTextColor(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.grey_400));

        } else if (position == 2){

            img.setImageResource(R.drawable.ic_facebook);

            title.setTypeface(null, Typeface.NORMAL);
            title.setTextColor(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.grey_400));

        }

        title.setText(mFragmentTitleList.get(position));
        return view;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
