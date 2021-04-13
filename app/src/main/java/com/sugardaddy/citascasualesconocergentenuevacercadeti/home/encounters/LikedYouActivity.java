package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.encounters;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.LikedYouAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.WelcomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.EncountersModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Objects;


public class LikedYouActivity extends AppCompatActivity {

    private User mCurrentUser;

    private ArrayList<EncountersModel> mLikedYouList;
    private LikedYouAdapter mLikedYouAdapter;

    private LinearLayout mEmptyView, mEmptyLayout, mLoadingLayout, mLikeExplainLayout;

    private TextView mErrorDesc, mErrorTitle;

    private ImageView mErrorImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_you);

        Toolbar toolbar = findViewById(R.id.toolbar);

        mLikedYouList = new ArrayList<>();
        mLikedYouAdapter = new LikedYouAdapter(mLikedYouList);

        RecyclerView mRecyclerView = findViewById(R.id.rvChat);

        mEmptyView = findViewById(R.id.empty_view);
        mEmptyLayout= findViewById(R.id.empty_layout);
        mLoadingLayout= findViewById(R.id.loading);

        mLikeExplainLayout= findViewById(R.id.header);


        mErrorImage = findViewById(R.id.image);
        mErrorTitle = findViewById(R.id.title);
        mErrorDesc = findViewById(R.id.brief);

        mCurrentUser = User.getUser();

        setLoading();

        mRecyclerView.setAdapter(mLikedYouAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setItemViewCacheSize(12);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.activity_liked_you_title));
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void setLoading(){

        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLikeExplainLayout.setVisibility(View.GONE);
    }

    private void hideLoading(boolean isLoaded){

        if (isLoaded){
            mEmptyLayout.setVisibility(View.GONE);
            mLikeExplainLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mLikeExplainLayout.setVisibility(View.GONE);
        }
        mLoadingLayout.setVisibility(View.GONE);
    }

    private void getUsersLikedYou(){

        setLoading();
        mLikedYouList.clear();


        ParseQuery<EncountersModel> likedYouModelParseQuery = EncountersModel.getQuery();
        likedYouModelParseQuery.whereEqualTo(EncountersModel.COL_TO_USER, mCurrentUser);
        likedYouModelParseQuery.whereEqualTo(EncountersModel.COL_SEEN, false);
        likedYouModelParseQuery.include(EncountersModel.COL_FROM_USER);
        likedYouModelParseQuery.findInBackground((users, e) -> {

            if (users != null){

                if (users.size() > 0){

                    mLikedYouList.clear();

                    mLikedYouList.addAll(users);

                    mLikedYouAdapter.notifyDataSetChanged();

                    hideLoading(true);

                } else {

                    hideLoading(false);

                    mLikedYouList.clear();

                    mEmptyLayout.setVisibility(View.VISIBLE);

                    mLoadingLayout.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                    mErrorImage.setImageResource(R.drawable.ic_navigation_bar_liked_you_indicator);
                    mErrorTitle.setText(R.string.you_dont_have_any_people_near);
                    mErrorDesc.setText(R.string.no_one_liked_you);
                }

            } else {

                hideLoading(false);

                if (e.getCode() == ParseException.CONNECTION_FAILED){

                    mErrorImage.setImageResource(R.drawable.ic_blocker_large_connection_grey1);
                    mErrorTitle.setText(R.string.not_internet_connection);
                    mErrorDesc.setText(R.string.settings_no_inte);

                } else if (e.getCode() == ParseException.INVALID_SESSION_TOKEN){

                    User.logOut();
                    QuickHelp.goToActivityAndFinish(this, WelcomeActivity.class);

                } else {

                    mErrorImage.setImageResource(R.drawable.ic_close);
                    mErrorTitle.setText(R.string.error_ocurred);
                    mErrorDesc.setText(e.getLocalizedMessage());

                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        getUsersLikedYou();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}