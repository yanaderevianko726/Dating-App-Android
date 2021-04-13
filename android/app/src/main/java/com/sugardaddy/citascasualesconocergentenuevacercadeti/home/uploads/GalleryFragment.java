package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.greysonparrelli.permiso.Permiso;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.others.PhotosUploadAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.UploadModel;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.ItemOffsetDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment implements PhotosUploadAdapter.ViewHolder.OnItemSelectedListener, PhotosUploadAdapter.ViewHolder.OnItemUnSelectedListener {

    private static final int GALLERY_REQUEST_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 1002;

    private ArrayList<UploadModel> mPhotosUploads;
    private PhotosUploadAdapter mPhotosUploadAdapter;

    private LinearLayout mNoAccountLayout;
    private TextView mNoPhotosText;
    private ProgressBar mProgressbar;
    private RecyclerView mRecyclerView;

    private ArrayList<UploadModel> igModelArrayList;

    private Uri mImageCaptureUri;

    //private Cursor imagecursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        mNoAccountLayout = v.findViewById(R.id.layout_no_account);
        mNoPhotosText = v.findViewById(R.id.no_image_text);
        AppCompatButton mConenctButton = v.findViewById(R.id.btn_connect_instagram);
        mProgressbar = v.findViewById(R.id.progress_instagram);
        mRecyclerView = v.findViewById(R.id.recyclerView);

        mPhotosUploads = new ArrayList<>();
        mPhotosUploadAdapter = new PhotosUploadAdapter(this, this, this, mPhotosUploads);

        if (getActivity() != null) {
            Permiso.getInstance().setActivity(getActivity());
        }

        isLoading(true, false);
        if(Build.VERSION.SDK_INT >= 21) {

            checkPermissions();

        } else {
            getMyGalleryPhotos();
        }

        mConenctButton.setOnClickListener(v1 -> checkPermissions());

        mRecyclerView.setAdapter(mPhotosUploadAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(Objects.requireNonNull(getContext()), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setBackgroundResource(R.color.white);
        mRecyclerView.setBackgroundColor(Color.WHITE);
        mRecyclerView.setLayoutManager(layoutManager);

        return v;

    }

    private void checkPermissions() {

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {

                    // User has permissions
                    getMyGalleryPhotos();

                } else if (resultSet.isPermissionPermanentlyDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per_whrite_storage);
                    });

                    // No permissions or missing permissions
                } else if (resultSet.isPermissionPermanentlyDenied(Manifest.permission.READ_EXTERNAL_STORAGE)){

                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per_whrite_storage);
                    });

                    // No permissions or missing permissions
                } else if (resultSet.isPermissionPermanentlyDenied(Manifest.permission.CAMERA)){

                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per_camera);
                    });

                    // No permissions or missing permissions
                } else {

                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        isLoading(false, false);
                        mNoPhotosText.setText(R.string.missing_per);
                    });

                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(null, getString(R.string.msg_permission_required), getString(R.string.ok), callback);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

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

    private void getMyGalleryPhotos() {

        //imagePro = new ImagePro(getActivity());

        igModelArrayList = new ArrayList<>();

        UploadModel camera = new UploadModel();
        camera.setPhotoPath("camera");

        UploadModel gallery = new UploadModel();
        gallery.setPhotoPath("gallery");


        igModelArrayList.add(camera);
        igModelArrayList.add(gallery);

        igModelArrayList.addAll(fetchGalleryImages(Objects.requireNonNull(getActivity())));

        if (igModelArrayList.size() > 0){

            isLoading(false, true);

            mPhotosUploads.clear();
            mPhotosUploads.addAll(igModelArrayList);
            mPhotosUploadAdapter.notifyDataSetChanged();

        } else {

            isLoading(false, false);
            mNoPhotosText.setText(R.string.no_photo_found);
        }

    }

    private ArrayList<UploadModel> fetchGalleryImages(Activity context) {
        ArrayList<UploadModel> galleryImageUrls;
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};//get all columns of type images
        @SuppressLint("InlinedApi") final String orderBy = MediaStore.Images.Media.DATE_TAKEN;//order data by date

        Cursor imagecursor = context.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");//get all data in Cursor by sorting in DESC order
        //imagecursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

        galleryImageUrls = new ArrayList<>();

        if (imagecursor != null) {
            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);//get column index

                UploadModel grModel = new UploadModel();
                grModel.setPhotoPath(imagecursor.getString(dataColumnIndex));

                galleryImageUrls.add(grModel);
                //imagecursor.close();
            }
        }
        Log.e("fatch in","images");
        return galleryImageUrls;
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.v("RECYCLER","OnResume");

        checkPermissions();
        if (getActivity() != null) {
            Permiso.getInstance().setActivity(getActivity());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);

    }

    public void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void takeFromCamera(){

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), QuickHelp.getRandomString(10) +".jpg");
        mImageCaptureUri = Uri.fromFile(f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void addNewPhoto(String photoPath){

        Log.v("IMAGE", "ADDED NEW PHOTO" + "Path: " + photoPath);

        UploadModel uploadModel = new UploadModel();
        uploadModel.setPhotoPath(photoPath);
        uploadModel.setSelected(true);

        mPhotosUploads.add(2, uploadModel);
        mPhotosUploadAdapter.notifyItemInserted(2);
        mPhotosUploadAdapter.setSelected(uploadModel);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == RESULT_OK){

            if (requestCode == GALLERY_REQUEST_CODE) {//data.getData return the content URI for the selected Image

                Log.v("GALLEY", "GOT NEW PHOTO");

                if (data != null){
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    Cursor cursor = null;
                    if (selectedImage != null) {

                        cursor = Application.getInstance().getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    }
                    // Move to first row
                    if (cursor != null) {
                        cursor.moveToFirst();
                    }
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = 0;
                    if (cursor != null) {
                        columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    }
                    //Gets the String value in the column
                    String imgDecodableString = null;
                    if (cursor != null) {
                        imgDecodableString = cursor.getString(columnIndex);
                    }

                    Log.v("GALLEY", "BEFORE NEW PHOTO" + "Size: " + igModelArrayList.size());

                    if (imgDecodableString != null && !imgDecodableString.isEmpty()){


                        addNewPhoto(imgDecodableString);
                        Log.v("GALLEY", "ADDED NEW PHOTO" + "Size: " + igModelArrayList.size());
                    }


                    if (cursor != null) {
                        cursor.close();
                    }
                    // Set the Image in ImageView after decoding the String
                } else {

                    Log.v("GALLEY", "ERROR");

                    if (getActivity() != null){
                        QuickHelp.showToast(getActivity(), getActivity().getString(R.string.error_ocurred), true);
                    }
                }

            } else if (requestCode == CAMERA_REQUEST_CODE) {

                if (mImageCaptureUri.getPath() != null && !mImageCaptureUri.getPath().isEmpty()){

                    addNewPhoto(mImageCaptureUri.getPath());
                }

            } else {

                Log.v("CAMERA", "data null");
            }

        } else {
            Log.v("GALLEY", "result not good");

            if (getActivity() != null){
                QuickHelp.showToast(getActivity(), getActivity().getString(R.string.error_ocurred), true);
            }
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();

        /*if (imagecursor != null) {
            imagecursor.close();
        }*/
    }
}
