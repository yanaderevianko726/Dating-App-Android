package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.PhotosListAdapter;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.TabUploadsAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ItemOffsetDecoration;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;


public class UploadsActivity extends AppCompatActivity {
    private TabUploadsAdapter adapter;
    private TabLayout tabLayout;
    ViewPager viewPager;
    TextView title;
    Toolbar toolbar;
    ImageView cloceImg;

    LinearLayoutManager layoutManager;
    RelativeLayout mPhotoLayout;
    Button mAddBtn;
    RecyclerView mRecyclerView;

    private ArrayList<String> mPhotosUploads;
    private PhotosListAdapter mPhotosUploadAdapter;

    User mCurrentUser;

    private ArrayList<String> arrayList;
    private ArrayList<Bitmap> bitmapArrayList;

    int count = 0;
    boolean isUploading = false;
    boolean isShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        cloceImg = findViewById(R.id.icon_close);

        title = findViewById(R.id.text_title);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        mRecyclerView = findViewById(R.id.photos);
        mPhotoLayout = findViewById(R.id.added_layout);
        mAddBtn = findViewById(R.id.add_button);

        title.setText(getString(R.string.add_photos));

        mPhotoLayout.setVisibility(View.GONE);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mPhotosUploads = new ArrayList<>();
        arrayList = new ArrayList<>();

        mPhotosUploadAdapter = new PhotosListAdapter(mPhotosUploads);

        cloceImg.setOnClickListener(v -> onBackPressed());

        adapter = new TabUploadsAdapter(getSupportFragmentManager());

        adapter.addFragment(new GalleryFragment(), getString(R.string.gallery));
        adapter.addFragment(new InstagramFragment(), getString(R.string.instagram));
        adapter.addFragment(new FacebookFragment(), getString(R.string.facebook));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        highLightCurrentTab(0); // for initial selected tab view
        unselectedLightCurrentTab(1);
        unselectedLightCurrentTab(2);

        mRecyclerView.setAdapter(mPhotosUploadAdapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setItemViewCacheSize(25);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                highLightCurrentTab(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                unselectedLightCurrentTab(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                highLightCurrentTab(tab.getPosition());

            }
        });
    }

    private void unselectedLightCurrentTab(int position) {

        TabLayout.Tab tab = tabLayout.getTabAt(position);

        if (tab != null) {
            tab.setCustomView(null);
            tab.setCustomView(adapter.getUnSelectedTabView(position));
        }
    }

    private void highLightCurrentTab(int position) {

        TabLayout.Tab tab = tabLayout.getTabAt(position);

        if (tab != null) {
            tab.setCustomView(null);
            tab.setCustomView(adapter.getSelectedTabView(position));
        }

    }

    public void onAddedUrls(String arrayListUrl){

        arrayList.add(arrayListUrl);
        updateList();
    }

    public void onOnRemovedUrls(String arrayListUrl){

        arrayList.remove(arrayListUrl);
        updateList();
    }

    private void updateList(){

        Log.v("FILES LIST", arrayList.toString());

        if (arrayList.size() > 0){

            if (!isShowing){

                isShowing = true;
                QuickHelp.animateAndShow(mPhotoLayout, this);
            }


        } else {

            isShowing = false;

            QuickHelp.animateAndHide(mPhotoLayout, this);

        }

        mPhotosUploads.clear();
        mPhotosUploads.addAll(arrayList);
        mPhotosUploadAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(arrayList.size() -1);
        layoutManager.canScrollHorizontally();


       mAddBtn.setOnClickListener(v -> {

           if (!isUploading){

               runOnUiThread(() -> QuickHelp.showLoading(UploadsActivity.this, false));

           }

           Handler handler = new Handler();
           handler.postDelayed(() -> runOnUiThread(this::uploadPhotos), 1500);

       });

    }

    public void uploadPhotos(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        bitmapArrayList = new ArrayList<>(QuickHelp.getBitmapFromURLsOrPath(arrayList));
        for (int i = 0; i < bitmapArrayList.size(); i++) {

            // All Images
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapArrayList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();


            ParseFile file  = new ParseFile("picture.jpg", image, "jpeg");
            ArrayList<ParseFile> parseFileArrayList  = new ArrayList<>();
            parseFileArrayList.add(file);

            file.saveInBackground((SaveCallback) e -> {

                if (e == null){

                    mCurrentUser.setProfilePhotos(parseFileArrayList);
                    mCurrentUser.saveInBackground(e1 -> {

                        if (e1 == null){

                            count = count+1;

                            Log.v("SAVED_IMAGES", String.valueOf(count));
                            Log.v("SENT_IMAGES", String.valueOf(bitmapArrayList.size()));

                            if (count >= bitmapArrayList.size()){

                                isUploading = false;
                                QuickHelp.hideLoading(this);
                                QuickHelp.showNotification(this, getString(R.string.phot_uploa), false);
                                finish();


                            }

                        } else {

                            count = count+1;

                            Log.v("ERROR_IMAGES", String.valueOf(count));
                            Log.v("SENT_IMAGES", String.valueOf(bitmapArrayList.size()));

                            if (count >= bitmapArrayList.size()){

                                isUploading = false;
                                QuickHelp.showNotification(this, e1.getMessage(), true);
                                QuickHelp.hideLoading(this);
                            }
                        }
                    });
                }
            });

        }
    }

    @Override
    public void onBackPressed(){

        finish();
    }
}
