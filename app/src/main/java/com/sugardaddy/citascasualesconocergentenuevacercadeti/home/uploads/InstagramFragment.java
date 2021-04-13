package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.PhotosUploadAdapter;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Constants;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;


import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.InstagramUser;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.UploadModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.instagram.AuthCallback;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.instagram.AuthInstagram;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ItemOffsetDecoration;

import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class InstagramFragment extends Fragment implements PhotosUploadAdapter.ViewHolder.OnItemSelectedListener, PhotosUploadAdapter.ViewHolder.OnItemUnSelectedListener {

    private User mCurrentUser;
    private ArrayList<UploadModel> mPhotosUploads;
    private PhotosUploadAdapter mPhotosUploadAdapter;

    private LinearLayout mNoAccountLayout;
    private ProgressBar mProgressbar;
    private StaggeredGridLayoutManager layoutManager;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instagram, container, false);

        mNoAccountLayout = v.findViewById(R.id.layout_no_account);
        AppCompatButton mConenctButton = v.findViewById(R.id.btn_connect_instagram);
        mProgressbar = v.findViewById(R.id.progress_instagram);
        mRecyclerView = v.findViewById(R.id.recyclerView);

        mPhotosUploads = new ArrayList<>();
        mPhotosUploadAdapter = new PhotosUploadAdapter(this, this, this, mPhotosUploads);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        if (mCurrentUser != null){

            if (mCurrentUser.getInstagramToken().isEmpty()){

                isLoading(false, false);

            } else {
                getMyInstagramPhotos();
            }
        }

        mConenctButton.setOnClickListener(v1 -> connectToInstagram());

        mRecyclerView.setAdapter(mPhotosUploadAdapter);
        layoutManager = new StaggeredGridLayoutManager(3 , StaggeredGridLayoutManager.VERTICAL);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(Objects.requireNonNull(getContext()), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setItemViewCacheSize(25);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        return v;

    }

    private void connectToInstagram() {

        List<String> permissions = new ArrayList<>();
        permissions.add("user_profile");
        permissions.add("user_media");

        AuthInstagram.connectInstagram(permissions, new AuthCallback() {
            @Override
            public void onSuccess(InstagramUser instagramUser) {


                isLoading(true, false);

                linkInstagramToAccount(instagramUser);

            }

            @Override
            public void onError(Throwable error) {

                QuickHelp.showNotification(getActivity(), getString(R.string.insta_error), true);
            }

            @Override
            public void onCancel() {

                QuickHelp.showNotification(getActivity(), getString(R.string.insta_canceled), true);
            }
        });
    }

    private void linkInstagramToAccount(InstagramUser socialUser){


        if (mCurrentUser != null){

            // Identification of our module in Parse Server inside
            String authType = "instagram";
            Map<String, String> authData = new HashMap<>();

            // Get accountKit session and then passe it to our module in Parse Server
            authData.put("api_type", "new_api");
            authData.put("id", socialUser.getUser_id());
            authData.put("access_token", socialUser.getAccess_token());

            if (mCurrentUser.isLinked(authType)){

                isLoading(false, false);

                QuickHelp.showNotification(getActivity(), getString(R.string.account_link), true);

            } else {

                // Method used to communicate to our server
                mCurrentUser.linkWithInBackground(authType, authData);

                mCurrentUser.setInstagramId(socialUser.getUser_id());

                mCurrentUser.setInstagramToken(socialUser.getAccess_token());
                mCurrentUser.saveInBackground(e -> {

                    if (e == null){

                        getMyInstagramPhotos();

                    } else {

                        isLoading(false, false);
                        QuickHelp.showNotification(getActivity(), getString(R.string.insta_error), true);
                    }

                });
            }

        }

    }

    private void isLoading(boolean isLoading, boolean isLoaded) {

        if (isLoading){

            mProgressbar.setVisibility(View.VISIBLE);
            mNoAccountLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);

        } else {

            if (isLoaded){

                mProgressbar.setVisibility(View.GONE);
                mNoAccountLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

            } else {

                mProgressbar.setVisibility(View.GONE);
                mNoAccountLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }

        }
    }

    private void getMyInstagramPhotos() {

        isLoading(true, false);

        volleyRequest();
    }

    private void volleyRequest() {

        String URL_FEED = Constants.INSTAGRAM_URL +mCurrentUser.getInstagramId()+"/media?fields=id,media_type,media_url&access_token="+ mCurrentUser.getInstagramToken() +"&count=" + "30";

        // We first check for cached request
        Cache cache = Application.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();

                    isLoading(false, false);
                    QuickHelp.showNotification(getActivity(), getString(R.string.insta_error), true);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

                isLoading(false, false);
                QuickHelp.showNotification(getActivity(), getString(R.string.insta_error), true);
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, URL_FEED, null, response -> {
                VolleyLog.d(Constants.LOG_TAG, "Response: " + response.toString());
                parseJsonFeed(response);
            }, error -> {
                VolleyLog.d(Constants.LOG_TAG, "Error: " + error.getMessage());

                isLoading(false, false);
                QuickHelp.showNotification(getActivity(), getString(R.string.insta_error), true);
            });

            // Adding request to volley request queue
            Application.getInstance().addToRequestQueue(jsonReq);
        }
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("data");

            Log.v("IG PHOTOS", feedArray.toString());

            ArrayList<UploadModel> igModelArrayList = new ArrayList<>();

            UploadModel grModelPhoto = new UploadModel();
            grModelPhoto.setPhotoPath("camera");
            grModelPhoto.setPhotoPath("gallery");


            igModelArrayList.remove(grModelPhoto);

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                String standardResolution = feedObj.getString("media_url");
                String mediaType = feedObj.getString("media_type");

                UploadModel igModel = new UploadModel();

                if (mediaType.equals("IMAGE")){
                    igModel.setInstagramUrl(standardResolution);
                    igModelArrayList.add(igModel);
                }

                isLoading(false, true);
            }

            mPhotosUploads.clear();
            mPhotosUploads.addAll(igModelArrayList);
            mPhotosUploadAdapter.notifyDataSetChanged();
            layoutManager.scrollToPositionWithOffset(0, 0);

            // notify data changes to list adapater


        } catch (JSONException e) {
            e.printStackTrace();

            isLoading(false, false);
            QuickHelp.hideLoading(getActivity());
        }
    }

    @Override
    public void onItemUnSelected(UploadModel item) {

        if (getActivity() != null){

            ((UploadsActivity) getActivity()).onOnRemovedUrls(item.getImageUrl());
        }
    }

    @Override
    public void onItemSelected(UploadModel item) {

        if (getActivity() != null){

            ((UploadsActivity) getActivity()).onAddedUrls(item.getImageUrl());
        }
    }
}
