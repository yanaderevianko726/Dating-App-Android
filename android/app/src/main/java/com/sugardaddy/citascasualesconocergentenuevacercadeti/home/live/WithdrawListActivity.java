package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.live;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.WithdrawAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.WelcomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.WithdrawModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Objects;

public class WithdrawListActivity extends AppCompatActivity {

    public ArrayList<WithdrawModel> withdrawModels = new ArrayList<>();
    Toolbar toolbar;
    User mCurrentUser;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayout mEmptyLayout, mLoadingLayout;
    TextView mErrorDesc;
    WithdrawAdapter mWithdrawAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);


        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        mRecyclerView = findViewById(R.id.rvWithdraw);

        mEmptyLayout = findViewById(R.id.empty_layout);
        mLoadingLayout = findViewById(R.id.loading);
        mErrorDesc = findViewById(R.id.brief);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setElevation(4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mWithdrawAdapter = new WithdrawAdapter(this, withdrawModels);

        setLoading();

        LinearLayoutManager layoutManagerSpotlight = new LinearLayoutManager(this);
        layoutManagerSpotlight.setOrientation(RecyclerView.VERTICAL);

        mRecyclerView.setAdapter(mWithdrawAdapter);
        mRecyclerView.setItemViewCacheSize(15);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManagerSpotlight);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this::getWithdrawList);
    }

    public void getWithdrawList(){

        ParseQuery<WithdrawModel> withdrawModelParseQuery = WithdrawModel.getWithdrawQuery();
        withdrawModelParseQuery.whereEqualTo(WithdrawModel.AUTHOR, mCurrentUser);
        withdrawModelParseQuery.orderByDescending(WithdrawModel.KEY_CREATED_AT);
        withdrawModelParseQuery.findInBackground((withdrawModelList, e) -> {

            if (withdrawModelList != null) {

                if (withdrawModelList.size() > 0) {

                    withdrawModels.clear();
                    withdrawModels.addAll(withdrawModelList);
                    mWithdrawAdapter.notifyDataSetChanged();

                    hideLoading(true);

                } else {

                    hideLoading(false);

                    mEmptyLayout.setVisibility(View.VISIBLE);

                    mLoadingLayout.setVisibility(View.GONE);

                    mErrorDesc.setText(R.string.no_withdraw_found);
                }
            } else {

                hideLoading(false);

                if (e.getCode() == ParseException.CONNECTION_FAILED) {

                    mErrorDesc.setText(R.string.settings_no_inte);

                } else if (e.getCode() == ParseException.INVALID_SESSION_TOKEN) {

                    User.logOut();
                    QuickHelp.goToActivityAndFinish(WithdrawListActivity.this, WelcomeActivity.class);

                } else {

                    mErrorDesc.setText(e.getLocalizedMessage());

                }
            }

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setLoading() {

        mLoadingLayout.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading(boolean isLoaded) {

        if (isLoaded) {
            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
        mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        getWithdrawList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
