package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.adapters.datoo.PhotoGalleryAdapter;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.uploads.UploadsActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.discretescrollview.DSVOrientation;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.discretescrollview.DiscreteScrollView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;


public class PhotosViewerActivity extends AppCompatActivity  implements DiscreteScrollView.ScrollListener<PhotoGalleryAdapter.ViewHolder>, DiscreteScrollView.OnItemChangedListener<PhotoGalleryAdapter.ViewHolder>{

    public static final String UserObject = "user_object";
    public static final String Position = "photo_position";

    DiscreteScrollView itemPicker;

    LinearLayout mOwnPhotosLayout;
    TextView mAddPhotos;
    ImageView mMore;
    BottomSheetDialog sheetDialog;

    ArrayList<ParseFile> parseFiles;
    PhotoGalleryAdapter photoGalleryAdapter;


    User mUser;
    User mCurrentUser;

    int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_viewer);

        mUser = getIntent().getParcelableExtra(UserObject);
        mPosition = getIntent().getIntExtra(Position, 0);

        mCurrentUser = (User) ParseUser.getCurrentUser();

        itemPicker = findViewById(R.id.imageSlider);

        mOwnPhotosLayout = findViewById(R.id.own_photos_options);

        mAddPhotos = findViewById(R.id.add_photos);
        mMore = findViewById(R.id.more);

        parseFiles = new ArrayList<>();
        photoGalleryAdapter = new PhotoGalleryAdapter(this, parseFiles);

        itemPicker.setAdapter(photoGalleryAdapter);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.setReverseLayout(false);
        itemPicker.addScrollListener(this);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setItemTransitionTimeMillis(100);
        itemPicker.scrollToPosition(mPosition);

        mMore.setOnClickListener(v -> OpenMore(mUser, mPosition));

        if (mUser != null && mCurrentUser != null){

            if (mUser.getObjectId().equals(mCurrentUser.getObjectId())){
                mOwnPhotosLayout.setVisibility(View.VISIBLE);

                mAddPhotos.setOnClickListener(v -> QuickHelp.goToActivityWithNoClean(this, UploadsActivity.class));

            } else {

                mOwnPhotosLayout.setVisibility(View.GONE);
            }

        }
    }

    public void OpenMore(User user, int position){

        sheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.setContentView(R.layout.include_photos_sheet);
        sheetDialog.setCancelable(true);

        TextView uploadNewPhoto = sheetDialog.findViewById(R.id.upalod_new_photo);
        TextView makeDefault = sheetDialog.findViewById(R.id.make_default);
        TextView delete = sheetDialog.findViewById(R.id.delete);

        if (uploadNewPhoto != null) {
            uploadNewPhoto.setOnClickListener(v -> {

                if (sheetDialog.isShowing()){
                    sheetDialog.dismiss();

                    QuickHelp.goToActivityWithNoClean(this, UploadsActivity.class);
                }
            });
        }

        if (makeDefault != null) {

            if (QuickHelp.isProfilePhoto(user, position)){
                makeDefault.setVisibility(View.GONE);
            } else {
                makeDefault.setVisibility(View.VISIBLE);
            }

            makeDefault.setOnClickListener(v -> {

                if (sheetDialog.isShowing()){
                    sheetDialog.dismiss();

                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setCancelable(false);
                    alert.setTitle(getString(R.string.photos));
                    alert.setMessage(getString(R.string.set_as_profile));
                    alert.setPositiveButton(getString(R.string.ok), (dialog, which) -> {

                        user.setAvatarPhotoPosition(position);
                        user.saveEventually();
                        user.fetchIfNeededInBackground();

                        parseFiles.clear();
                        parseFiles.addAll(user.getProfilePhotos());
                        photoGalleryAdapter.notifyDataSetChanged();

                    }).setNegativeButton(getString(R.string.cancel), null).create();

                    alert.show();
                }
            });
        }

        if (delete != null) {

            if (QuickHelp.isProfilePhoto(user, position)){
                delete.setVisibility(View.GONE);
            } else {
                delete.setVisibility(View.VISIBLE);
            }

            delete.setOnClickListener(v -> {

                if (sheetDialog.isShowing()){
                    sheetDialog.dismiss();

                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setCancelable(false);
                    alert.setTitle(getString(R.string.delete_photo));
                    alert.setMessage(getString(R.string.are_you_sure_delete));
                    alert.setPositiveButton(getString(R.string.delete_photo_text), (dialog, which) -> {

                        if (user.getProfilePhotos().size() == 1){

                            // Delete photos
                            QuickHelp.showToast(this, getString(R.string.can_not_delete_unique_photo), true);

                        } else if (user.getProfilePhotos().size() == 2){

                            // Delete photos
                            user.removePhoto(user.getProfilePhotos().get(position));
                            user.setAvatarPhotoPosition(0);
                            user.saveEventually();
                            user.fetchIfNeededInBackground();

                            parseFiles.clear();
                            parseFiles.addAll(user.getProfilePhotos());
                            photoGalleryAdapter.notifyDataSetChanged();



                        } else if (user.getProfilePhotos().size() > 2){

                            // Delete photos
                            user.removePhoto(user.getProfilePhotos().get(position));
                            user.setAvatarPhotoPosition(position -1);
                            user.saveEventually();
                            user.fetchIfNeededInBackground();

                            parseFiles.clear();
                            parseFiles.addAll(user.getProfilePhotos());
                            photoGalleryAdapter.notifyDataSetChanged();

                        }

                    }).setNegativeButton(getString(R.string.cancel), null).create();
                    alert.show();
                }
            });
        }

        if (sheetDialog != null && !sheetDialog.isShowing()){
            sheetDialog.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser.fetchIfNeededInBackground();

        if (mUser != null && mCurrentUser != null){

            parseFiles.clear();

            if (mUser.getObjectId().equals(mCurrentUser.getObjectId())){

                parseFiles.addAll(mCurrentUser.getProfilePhotos());
                itemPicker.scrollToPosition(mPosition);

            } else {

                parseFiles.addAll(mUser.getProfilePhotos());
            }

            photoGalleryAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onBackPressed() {
        if (sheetDialog != null && sheetDialog.isShowing()){
            sheetDialog.dismiss();
        } else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sheetDialog != null && sheetDialog.isShowing()){
            sheetDialog.dismiss();
        }
    }

    @Override
    public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable PhotoGalleryAdapter.ViewHolder currentHolder, @Nullable PhotoGalleryAdapter.ViewHolder newCurrent) {

        mPosition = currentPosition;
    }

    @Override
    public void onCurrentItemChanged(@Nullable PhotoGalleryAdapter.ViewHolder viewHolder, int adapterPosition) {

        mPosition = adapterPosition;
    }
}