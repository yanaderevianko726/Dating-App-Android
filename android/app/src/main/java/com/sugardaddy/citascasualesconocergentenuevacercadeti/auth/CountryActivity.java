package com.sugardaddy.citascasualesconocergentenuevacercadeti.auth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.CountriesAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.CountriesFetcher;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.TextWatcherAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.CountriesModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

public class CountryActivity extends AppCompatActivity {

    TextInputEditText searchInput;
    AppCompatImageView clearBtn;
    AppCompatImageView closeBtn;
    RecyclerView CountriesList;
    RecyclerViewFastScroller fastScroller;
    List<CountriesModel> list;
    private CountriesAdapter mCountriesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        searchInput = findViewById(R.id.search_input);
        clearBtn = findViewById(R.id.clear_btn_search_view);
        closeBtn = findViewById(R.id.close_btn_search_view);
        CountriesList = findViewById(R.id.CounrtriesList);
        fastScroller = findViewById(R.id.fastscroller);

        initializerView();
    }

    @SuppressLint("WrongConstant")
    void initializerView() {

        initializerSearchView(searchInput, clearBtn);
        clearBtn.setOnClickListener(v -> clearSearchView());
        closeBtn.setOnClickListener(v -> closeSearchView());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CountriesList.setLayoutManager(mLinearLayoutManager);
        mCountriesAdapter = new CountriesAdapter(this);
        CountriesList.setAdapter(mCountriesAdapter);

        list = new CountriesFetcher.CountryList();

        QuickHelp.setCountryAdapter(this, list, mCountriesAdapter);

        // set recycler view to fastScroller
        fastScroller.setRecyclerView(CountriesList);
        fastScroller.setViewsToUse(R.layout.countries_fragment_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }


    /**
     * method to clear/reset search view content
     */
    public void clearSearchView() {
        if (searchInput.getText() != null) {
            searchInput.setText("");

            QuickHelp.setCountryAdapter(this, list, mCountriesAdapter);
        }

    }

    /**
     * method to initial the search view
     */
    public void initializerSearchView(TextInputEditText searchInput, ImageView clearSearchBtn) {

        final Context context = this;
        searchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });
        searchInput.addTextChangedListener(new TextWatcherAdapter() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                clearSearchBtn.setVisibility(View.GONE);
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCountriesAdapter.setString(s.toString());
                Search(s.toString().trim());
                clearSearchBtn.setVisibility(View.VISIBLE);
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    clearSearchBtn.setVisibility(View.GONE);

                    QuickHelp.setCountryAdapter(getApplicationContext(), list, mCountriesAdapter);
                }
            }
        });

    }

    /**
     * method to start searching
     *
     * @param string this is parameter of Search method
     */
    public void Search(String string) {

        final List<CountriesModel> filteredModelList;
        filteredModelList = FilterList(string);
        if (filteredModelList.size() != 0) {
            mCountriesAdapter.animateTo(filteredModelList);
            CountriesList.scrollToPosition(0);
        }
    }

    /**
     * method to filter the list
     *
     * @param query this is parameter of FilterList method
     * @return this for what method return
     */
    private List<CountriesModel> FilterList(String query) {
        query = query.toLowerCase();
        List<CountriesModel> countriesModelList = mCountriesAdapter.getCountries();
        final List<CountriesModel> filteredModelList = new ArrayList<>();
        for (CountriesModel countriesModel : countriesModelList) {
            final String name = countriesModel.getName().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(countriesModel);
            }
        }
        return filteredModelList;
    }

    /**
     * method to close the search view
     */
    public void closeSearchView() {
        finish();
        //AnimationsUtil.setSlideOutAnimation(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //AnimationsUtil.setSlideOutAnimation(this);
    }
}
