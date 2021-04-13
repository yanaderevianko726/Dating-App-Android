package com.sugardaddy.citascasualesconocergentenuevacercadeti.home.live;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.StatusBarUtil;
import com.parse.ParseUser;

public class LiveActivity extends AppCompatActivity {

    User mCurrentUser;

    ImageView mAvatar, mClose;
    AppCompatButton mGotIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.hideStatusBar(this);

        setContentView(R.layout.activity_live);


        mCurrentUser = (User) ParseUser.getCurrentUser();

        mClose = findViewById(R.id.initial_close);
        mAvatar = findViewById(R.id.avatar);
        mGotIt = findViewById(R.id.streamerRules_agreeButton);

        QuickHelp.setAvatarFull(mAvatar, mCurrentUser);

        mClose.setOnClickListener(v -> onBackPressed());

        mGotIt.setOnClickListener(v -> {

            if (QuickHelp.isInternetAvailable(this)){
                QuickHelp.goToActivityStreaming(this, LiveStreamingActivity.LIVE_STREAMING_STREAMER);
            } else {
                QuickHelp.showNotification(this, getString(R.string.not_internet_connection), true);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}