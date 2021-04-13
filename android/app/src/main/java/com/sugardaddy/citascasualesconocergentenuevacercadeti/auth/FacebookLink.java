package com.sugardaddy.citascasualesconocergentenuevacercadeti.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.BaseActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.DispatchActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.WelcomeActivity.DownloadImageBitmap;

public class FacebookLink extends BaseActivity {


    User mCurrentUser;
    AppCompatButton mFacebookConnect;
    TextView mContibue;
    Bitmap bitmap;
    String profilePic = null;
    ProgressBar mProgressBar;
    TextView mFacebookText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_facebook);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mCurrentUser = (User)User.getCurrentUser();

        mFacebookConnect = findViewById(R.id.onBoardingConnectFacebook_actionPrimary);
        mContibue = findViewById(R.id.onBoardingConnectFacebook_actionSecondary);
        mProgressBar = findViewById(R.id.progress_bar);
        mFacebookText = findViewById(R.id.text_connect_facebook);

        mProgressBar.setVisibility(View.INVISIBLE);

        if (Config.isFakeMessagesActivated){
            QuickActions.sendFakeMessage(mCurrentUser);
        }

        mFacebookConnect.setOnClickListener(v -> {

            ParseUser user = ParseUser.getCurrentUser();

            startFbLink();

            List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_gender", "user_photos", "user_location");

            if (!ParseFacebookUtils.isLinked(user)) {

                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, FacebookLink.this, permissions, ex -> {
                    if (ParseFacebookUtils.isLinked(user)) {
                        Log.d("Dateyou", "Woohoo, user logged in with Facebook!");
                        retrieveFromFb();
                    } else {
                        // Falid to link your facebook account
                        Log.d("Dateyou", "Failed to link with your facebook account");
                        Toast.makeText(FacebookLink.this, getString(R.string.failed_to_link_facebook), Toast.LENGTH_LONG).show();
                        LinkSuccess();
                    }
                });
            }
        });

        mContibue.setOnClickListener(v -> LinkSuccess());


    }

    public void retrieveFromFb(){

        Log.d("Dateyou","Saving Facebook InstagramData to Server");

        GraphRequest.GraphJSONObjectCallback callback = (fbUser, response) -> {

            User parseUser = (User) ParseUser.getCurrentUser();
            if (fbUser != null && parseUser != null) {


                if (fbUser.optString("id").length() > 0){
                    parseUser.setFacebookId(fbUser.optString("id"));
                }

                try {
                    if (fbUser.getJSONObject("location").getString("name").length() > 0){

                        parseUser.setLocation(fbUser.getJSONObject("location").optString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            if (parseUser != null) {
                parseUser.saveInBackground(e -> {
                    if (e != null) {
                        Log.d("Dateyou","Failed to save Facebook InstagramData to Server");
                    }

                    Log.d("Dateyou","Saved Facebook InstagramData to Server");

                    try {
                        assert fbUser != null;
                        profilePic = fbUser.getJSONObject("picture").getJSONObject("data").getString("url");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    new ProfilePhotoAsync(profilePic).execute();

                });
            }

        };

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields",  "id,link,first_name,last_name,picture.width(720).height(720),location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @SuppressLint("StaticFieldLeak")
    public class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        //Bitmap bitmap;
        String url;

        ProfilePhotoAsync(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {

            bitmap = DownloadImageBitmap(url);

            saveFbPtofilePic();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void saveFbPtofilePic() {

        Log.d("Dateyou","Trying to save Facebook InstagramData to Server");

        ArrayList<ParseFile> pFileList = new ArrayList<>();

        if (bitmap != null) {

            Log.d("Dateyou","Saving Facebook Picture to Server");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();

            // Create the ParseFile
            ParseFile file  = new ParseFile("picture.jpeg", image);
            pFileList.add(file);

            file.saveInBackground((SaveCallback) e -> {
                if (e != null) {

                    LinkSuccess();

                } else {

                    addPhotoToProfile(pFileList);

                }
            });

        } else {

            LinkSuccess();

        }

    }

    public void addPhotoToProfile(ArrayList<ParseFile> photoFiles){

        User user = (User) ParseUser.getCurrentUser();

        user.setProfilePhotos(photoFiles);
        user.saveInBackground(e -> {

            if (e == null) {

                LinkSuccess();

                Log.d("Dateyou","Saved Facebook Picture to Server");

                user.saveInBackground();

            } else {

                LinkSuccess();
                Log.d("Dateyou","Failed to save Facebook Picture to Server");
            }

        });
    }

    private void LinkSuccess() {

        stopFbLink();

        QuickHelp.saveInstallation(mCurrentUser);


        QuickHelp.goToActivityAndFinish(this, DispatchActivity.class);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void startFbLink(){
        mFacebookConnect.setEnabled(false);
        mFacebookConnect.setVisibility(View.INVISIBLE);

        mContibue.setEnabled(false);
        mContibue.setVisibility(View.INVISIBLE);

        mFacebookText.setVisibility(View.INVISIBLE);

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setBackgroundColor(Color.WHITE);
        mProgressBar.setBackgroundResource(R.color.white);

    }

    public void stopFbLink(){
        mFacebookConnect.setEnabled(true);
        mFacebookConnect.setVisibility(View.VISIBLE);

        mContibue.setEnabled(true);
        mContibue.setVisibility(View.VISIBLE);

        mFacebookText.setVisibility(View.VISIBLE);

        mProgressBar.setVisibility(View.INVISIBLE);

    }
}
