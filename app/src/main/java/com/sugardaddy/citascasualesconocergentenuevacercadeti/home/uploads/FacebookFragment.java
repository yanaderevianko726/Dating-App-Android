package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.PhotosUploadAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.fbAlbumsAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.FbAlbumModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.UploadModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ItemOffsetDecoration;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;

import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FacebookFragment extends Fragment implements Spinner.OnItemSelectedListener, PhotosUploadAdapter.ViewHolder.OnItemSelectedListener, PhotosUploadAdapter.ViewHolder.OnItemUnSelectedListener {

    private User mCurrentUser;
    private ArrayList<UploadModel> mPhotosUploads;
    private PhotosUploadAdapter mPhotosUploadAdapter;
    private ArrayAdapter<FbAlbumModel> stringArrayAdapter;

    private LinearLayout mNoAccountLayout;
    private ProgressBar mProgressbar;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager layoutManager;
    private Spinner mAlbumsSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_facebook, container, false);

        mNoAccountLayout = v.findViewById(R.id.layout_no_account);
        AppCompatButton mConenctButton = v.findViewById(R.id.btn_connect_instagram);
        mProgressbar = v.findViewById(R.id.progress_instagram);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mAlbumsSpinner = v.findViewById(R.id.spinner);

        mAlbumsSpinner.setOnItemSelectedListener(this);

        mPhotosUploads = new ArrayList<>();
        mPhotosUploadAdapter = new PhotosUploadAdapter(this, this, this, mPhotosUploads);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        if (mCurrentUser != null){

            if (ParseFacebookUtils.isLinked(mCurrentUser)){

                if (mCurrentUser.getFacebookId() != null && !mCurrentUser.getFacebookId().isEmpty()){

                    getFacebookAlbums();
                } else {

                    isLoading(false, false);
                }


            } else {

                isLoading(false, false);
            }
        }

        mConenctButton.setOnClickListener(v1 -> connectTofacebook());

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

    private void connectTofacebook() {

        QuickHelp.showLoading(getActivity(), false);

        ParseUser user = ParseUser.getCurrentUser();

        List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_gender", "user_photos", "user_location");

        if (!ParseFacebookUtils.isLinked(user)) {

            ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, permissions, ex -> {
                if (ParseFacebookUtils.isLinked(user)) {
                    Log.d("Dateyou", "Woohoo, user linked with your facebook account");

                    getFacebookData();

                } else {
                    // Falid to link your facebook account
                    Log.d("Dateyou", "Failed to link with your facebook account");

                    QuickHelp.showNotification(getActivity(), getString(R.string.failed_to_link_facebook), true);
                    QuickHelp.hideLoading(getActivity());
                }
            });

        } else {

            QuickHelp.showNotification(getActivity(), getString(R.string.account_link), true);
            QuickHelp.hideLoading(getActivity());
        }
    }

    private void getFacebookData(){

        Log.d("Dateyou","Saving Facebook data to Server");

        GraphRequest.GraphJSONObjectCallback callback = (fbUser, response) -> {

            User parseUser = (User) ParseUser.getCurrentUser();
            if (fbUser != null && parseUser != null) {


                if (fbUser.optString("id").length() > 0){
                    //parseUser.setFacebookId(fbUser.optLong("id"));
                    parseUser.setFacebookId(fbUser.optString("id"));

                    Log.d("Dateyou","Facebook id: " + fbUser.optString("id"));
                }


                if (fbUser.optString("link").length() > 0){
                    parseUser.setFacebookLink(fbUser.optString("link"));

                    Log.d("Dateyou","Facebook link: " + fbUser.optString("link"));
                } else {

                    Log.d("Dateyou","Facebook no LINK");
                }


            }


            if (parseUser != null) {
                parseUser.saveInBackground(e -> {
                    if (e != null) {

                        Log.d("Dateyou","Failed to save Facebook data to Server");
                        QuickHelp.showNotification(getActivity(), getString(R.string.failed_to_save_facebook), true);
                        QuickHelp.hideLoading(getActivity());

                    } else {

                        getFacebookAlbums();
                        QuickHelp.hideLoading(getActivity());
                    }

                });
            }

        };

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields",  "id,link");
        request.setParameters(parameters);
        request.executeAsync();
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
                mAlbumsSpinner.setVisibility(View.VISIBLE);

            } else {

                mProgressbar.setVisibility(View.GONE);
                mNoAccountLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mAlbumsSpinner.setVisibility(View.GONE);
            }

        }
    }

    private void getMyFacebookPhotos(String albumId) {


        GraphRequest.GraphJSONObjectCallback callback = (user, response) -> {

            if (response != null){


                parseJsonFeed(response.getJSONObject());


            } else {

                isLoading(false, false);
                QuickHelp.hideLoading(getActivity());
            }
        };

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields",  "id,source");
        parameters.putString("limit", "100");
        request.setParameters(parameters);
        request.setGraphPath("/"+albumId+"/photos");
        request.executeAsync();

    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {

            String fbData = response.getJSONArray("data").toString();
            Log.i("FB PHOTOS", fbData);

            JSONArray feedArray = response.getJSONArray("data");

            ArrayList<UploadModel> fbModelArrayList = new ArrayList<>();

            UploadModel grModelPhoto = new UploadModel();
            grModelPhoto.setPhotoPath("camera");
            grModelPhoto.setPhotoPath("gallery");


            fbModelArrayList.remove(grModelPhoto);

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                UploadModel fbModel = new UploadModel();

                fbModel.setFacebookUrl(feedObj.getString("source"));

                fbModelArrayList.add(fbModel);

                isLoading(false, true);
            }

            mPhotosUploads.clear();
            mPhotosUploads.addAll(fbModelArrayList);
            mPhotosUploadAdapter.notifyDataSetChanged();
            layoutManager.scrollToPositionWithOffset(0, 0);

            // notify data changes to list adapater


        } catch (JSONException e) {
            e.printStackTrace();

            isLoading(false, false);
            QuickHelp.hideLoading(getActivity());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void getFacebookAlbums() {

        isLoading(true, false);
        QuickHelp.hideLoading(getActivity());

        GraphRequest.GraphJSONObjectCallback callback = (user, response) -> {

            if (response != null){

                parseAlbumJson(response.getJSONObject());


            } else {

                isLoading(false, false);
                QuickHelp.hideLoading(getActivity());
            }
        };

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields",  "id,name");
        request.setParameters(parameters);
        request.setGraphPath("/"+mCurrentUser.getFacebookId()+"/albums");
        request.executeAsync();
    }

    private void parseAlbumJson(JSONObject response) {
        try {

            String fbData = response.getJSONArray("data").toString();
            Log.i("FB PHOTOS", fbData);

            JSONArray feedArray = response.getJSONArray("data");


            ArrayList<FbAlbumModel> fbAlbumModels = new ArrayList<>();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FbAlbumModel albumModel = new FbAlbumModel();

                albumModel.setId(feedObj.getString("id"));
                albumModel.setName(feedObj.getString("name"));

                fbAlbumModels.add(albumModel);


                mAlbumsSpinner.setVisibility(View.VISIBLE);

                stringArrayAdapter = new fbAlbumsAdapter(getActivity(), R.layout.fb_album_item, R.id.title, fbAlbumModels);

                mAlbumsSpinner.setAdapter(stringArrayAdapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            isLoading(false, false);
            QuickHelp.hideLoading(getActivity());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String albumId = Objects.requireNonNull(stringArrayAdapter.getItem(position)).getId();

        getMyFacebookPhotos(albumId);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}
