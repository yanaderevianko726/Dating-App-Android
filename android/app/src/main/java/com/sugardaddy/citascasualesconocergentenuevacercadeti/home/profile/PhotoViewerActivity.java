package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.profile;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Application;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.StatusBarUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.bumptech.glide.Glide;


public class PhotoViewerActivity extends AppCompatActivity {

    public static final String imageUrl = "imageUrl";

    String getImageUrl;

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        getImageUrl = getIntent().getStringExtra(imageUrl);
        mImageView = findViewById(R.id.image_view);

        Tools.setSystemBarColor(this, R.color.black);
        StatusBarUtil.setDarkMode(this);

        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(Application.getInstance().getApplicationContext(), R.color.highlight_light_ripple));

        Glide.with(this)
                .load(getImageUrl)
                .placeholder(colorDrawable)
                .into(mImageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}