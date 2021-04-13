package com.sugardaddy.citascasualesconocergentenuevacercadeti.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.BaseActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.DispatchActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils.AngopapoLoginBuilder;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.authUtils.AngopapoSignupBuilder;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickActions;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.HomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.parsegooglesignin.ParseGoogleSignIn;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.SharedPrefUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WelcomeActivity extends BaseActivity {

    private View parseLogin;
    Button mFacebook;
    ProgressBar mProgress;
    TextView mFacebookText, mLogin;
    Bitmap bitmap;
    Button mButtonMale;
    Button mButtonFemale;
    LinearLayout mGenderLayout;

    User mCurrentUser;

    BottomSheetDialog sheetDialog;

    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IMAGE","Error getting bitmap",e);
        }
        return bm;
    }

    String genderSelected = null, profilePic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);

        mCurrentUser = (User) User.getCurrentUser();

        mLogin = findViewById(R.id.landingScreen_otherOptions);
        mFacebook = findViewById(R.id.button_login_facebook);
        mProgress = findViewById(R.id.progress_bar);
        mFacebookText = findViewById(R.id.text_login_facebook);
        parseLogin = findViewById(R.id.Landing_scroll);
        mButtonMale = findViewById(R.id.Registration_buttonMale);
        mButtonFemale = findViewById(R.id.Registration_buttonFemale);
        mGenderLayout = findViewById(R.id.register_gender);
        FrameLayout facebookLyt = findViewById(R.id.layout_sign_in);
        FrameLayout genderSignupLyt = findViewById(R.id.gender_signup_lyt);
        LinearLayout orLyt = findViewById(R.id.or_layout);
        TextView fbAlert = findViewById(R.id.facebook_alert);

        mProgress.setVisibility(View.GONE);

        if (!Config.FACEBOOK_LOGIN) {

            facebookLyt.setVisibility(View.GONE);
            orLyt.setVisibility(View.INVISIBLE);
            fbAlert.setVisibility(View.INVISIBLE);
        }

        if (!Config.EMAIL_LOGIN && !Config.GOOGLE_LOGIN && !Config.PHONE_LOGIN) mLogin.setVisibility(View.GONE);

        if (!Config.EMAIL_LOGIN) {

            genderSignupLyt.setVisibility(View.GONE);
            orLyt.setVisibility(View.INVISIBLE);
        }


        mLogin.setOnClickListener(view -> OpenLogin());

        mButtonMale.setOnClickListener(view -> {

            new SharedPrefUtil(this).saveString(User.COL_GENDER, User.GENDER_MALE);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.exit_to_left);
            animation.setDuration(400);
            mGenderLayout.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mGenderLayout.setVisibility(View.INVISIBLE);

                    AngopapoSignupBuilder builder = new AngopapoSignupBuilder(WelcomeActivity.this);
                    builder.setAppLogo(R.mipmap.ic_launcher);
                    builder.setParseLoginButtonText((R.string.sign_in));
                    startActivityForResult(builder.build(), 0);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        });

        mButtonFemale.setOnClickListener(view -> {

            new SharedPrefUtil(this).saveString(User.COL_GENDER, User.GENDER_FEMALE);

            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit_to_left);
            animation.setDuration(400);
            mGenderLayout.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mGenderLayout.setVisibility(View.INVISIBLE);

                    AngopapoSignupBuilder builder = new AngopapoSignupBuilder(WelcomeActivity.this);
                    builder.setAppLogo(R.mipmap.ic_launcher);
                    builder.setParseLoginButtonText((R.string.sign_in));
                    startActivityForResult(builder.build(), 0);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        });

        mFacebook.setOnClickListener(view -> {

            List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_gender", "user_photos", "user_location");

            if (QuickHelp.isInternetAvailable(this)){

                startFbLogin();

                ParseFacebookUtils.logInWithReadPermissionsInBackground(WelcomeActivity.this, permissions,(user, err) -> {

                    if (user == null) {

                        Snackbar.make(parseLogin, R.string.fb_error,Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, v1 -> {

                        }).setActionTextColor(Color.WHITE).show();

                        stopFbLogin();
                    } else if (user.isNew()) {

                        retrieveFromFb();

                    } else {

                        loginSuccess();


                    }
                });

            } else QuickHelp.noInternetConnect(this);
        });
    }

    public void OpenLogin(){

        sheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.setContentView(R.layout.include_login_sheet);
        sheetDialog.setCancelable(true);

        LinearLayout phone = sheetDialog.findViewById(R.id.phone_login);
        LinearLayout google = sheetDialog.findViewById(R.id.google_login);
        LinearLayout email = sheetDialog.findViewById(R.id.email_login);

        assert phone != null;
        assert google != null;
        assert email != null;

        if (!Config.PHONE_LOGIN) phone.setVisibility(View.GONE);
        if (!Config.GOOGLE_LOGIN) google.setVisibility(View.GONE);
        if (!Config.EMAIL_LOGIN) email.setVisibility(View.GONE);

            phone.setOnClickListener(v -> {
            if (sheetDialog.isShowing()){
                sheetDialog.dismiss();

                QuickHelp.goToActivityWithNoClean(this, PhoneLoginActivity.class);
            }

        });

        google.setOnClickListener(v -> {
            if (sheetDialog.isShowing()){
                sheetDialog.dismiss();

                loginWithGoogle();
            }

        });

        email.setOnClickListener(view -> {

            if (sheetDialog.isShowing()){
                sheetDialog.dismiss();

                goToLogin();
            }

        });

        if (sheetDialog != null && !sheetDialog.isShowing()){
            sheetDialog.show();
        }
    }

    public void goToLogin(){

        AngopapoLoginBuilder builder = new AngopapoLoginBuilder(WelcomeActivity.this);
        builder.setAppLogo(R.mipmap.ic_launcher);
        builder.setParseLoginButtonText((R.string.sign_in));
        builder.setParseLoginHelpText(getString(R.string.forgot_pass));
        startActivityForResult(builder.build(), 0);
    }

    public void loginWithGoogle(){

        if (QuickHelp.isInternetAvailable(this)){

            QuickHelp.showLoading(WelcomeActivity.this, false);

            ParseGoogleSignIn.LoginInBackGround(this, (parseUser, e) -> {

                if (parseUser != null){

                    if (parseUser.isNew()){

                        Log.d("GOOGLE_LOGIN", "User signed up and logged in through Google!");

                        retrieveFromGg(parseUser);

                    } else {

                        Log.d("GOOGLE_LOGIN", "User logged in through Google!");

                        User user = (User) parseUser;

                        Intent mainIntent = new Intent(this, HomeActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();

                        QuickHelp.saveInstallation(user);

                        QuickHelp.hideLoading(WelcomeActivity.this);
                    }

                } else {

                    Log.d("GOOGLE_LOGIN", "Google Login error: " + e.getMessage());

                    Snackbar.make(parseLogin, R.string.gg_error,Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, v1 -> {

                    }).setActionTextColor(Color.WHITE).show();

                    QuickHelp.hideLoading(WelcomeActivity.this);
                }
            });

        } else QuickHelp.noInternetConnect(this);
    }

    public void retrieveFromGg(ParseUser user){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        User parseUser = (User) user;

        if (account != null) {

            Log.d("GOOGLE_LOGIN", "result success");

            if (parseUser != null) {

                parseUser.setUsername(account.getFamilyName().trim().toLowerCase(Locale.US) + account.getGivenName().trim().toLowerCase(Locale.US));

                parseUser.setColFullName(account.getDisplayName());
                parseUser.setGoogleId(account.getId());
                parseUser.setColFirstName(account.getGivenName());
                parseUser.setEmail(account.getEmail());

                bitmap = QuickHelp.getBitmapFromURLOrPath(account.getPhotoUrl().toString());

                parseUser.setUid(QuickHelp.generateUId());
                parseUser.setPopularity(0);
                parseUser.setRole(User.ROLE_USER);
                parseUser.setPrefMinAge(Config.MinimumAgeToRegister);
                parseUser.setPrefMaxAge(Config.MaximumAgeToRegister);
                parseUser.setLocationTypeNearBy(true);
                parseUser.addCredit(Config.WelcomeCredit);
                parseUser.setBio(Config.bio);
                parseUser.setPasswordEnabled(false);
                parseUser.saveInBackground(e -> {
                    if (e != null) {

                        Log.d("GOOGLE_LOGIN","Failed to save Google data to Server");
                    }

                    Log.d("GOOGLE_LOGIN","Saved Google to Server");

                    saveFbPtofilePic();

                    Log.d("GOOGLE_LOGIN","url: " + profilePic);

                });
            }

        } else {

            Log.d("GOOGLE_LOGIN", "result error");

            if (parseUser != null) {

                parseUser.setUid(QuickHelp.generateUId());
                parseUser.setPopularity(0);
                parseUser.setRole(User.ROLE_USER);
                parseUser.setPrefMinAge(Config.MinimumAgeToRegister);
                parseUser.setPrefMaxAge(Config.MaximumAgeToRegister);
                parseUser.setLocationTypeNearBy(true);
                parseUser.addCredit(Config.WelcomeCredit);
                parseUser.setBio(Config.bio);
                parseUser.setPasswordEnabled(false);
                parseUser.saveInBackground(e -> {
                    if (e != null) {

                        Log.d("GOOGLE_LOGIN","Parse User failed to update");
                    }

                    Log.d("GOOGLE_LOGIN","Parse User updated to to login");

                    loginSuccess();

                });
            } else {
                Log.d("GOOGLE_LOGIN","Parse User null");
            }
        }
    }

    public void retrieveFromFb(){

        GraphRequest.GraphJSONObjectCallback callback = (fbUser, response) -> {

            User parseUser = (User) ParseUser.getCurrentUser();
            if (fbUser != null && parseUser != null) {

                if (fbUser.optString("name").length() > 0){
                    parseUser.setColFullName(fbUser.optString("name"));
                }

                if (fbUser.optString("id").length() > 0){

                    parseUser.setFacebookId(fbUser.optString("id"));
                }

                if (fbUser.optString("first_name").length() > 0){

                    parseUser.setColFirstName(fbUser.optString("first_name"));

                    parseUser.setUsername(fbUser.optString("last_name").trim().toLowerCase(Locale.US) + fbUser.optString("first_name").trim().toLowerCase(Locale.US));
                }

                if (fbUser.optString("email").length() > 0){

                    parseUser.setEmail(fbUser.optString("email"));
                }


                if (fbUser.optString("gender").length() > 0){
                    parseUser.setColGender(fbUser.optString("gender"));
                    genderSelected = fbUser.optString("gender");
                }


                if (fbUser.optString("birthday").length() > 0){

                    String dateString = fbUser.optString("birthday");

                    SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                    Date date;
                    try {
                        date = fmt.parse(dateString);
                        parseUser.setColBirthdate(date);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    if (fbUser.getJSONObject("location").getString("name").length() > 0){

                        parseUser.setLocation(fbUser.getJSONObject("location").optString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                parseUser.setUid(QuickHelp.generateUId());
                parseUser.setPopularity(0);
                parseUser.setRole(User.ROLE_USER);
                parseUser.setPrefMinAge(Config.MinimumAgeToRegister);
                parseUser.setPrefMaxAge(Config.MaximumAgeToRegister);
                parseUser.setLocationTypeNearBy(true);
                parseUser.addCredit(Config.WelcomeCredit);
                parseUser.setBio(Config.bio);
                parseUser.setPasswordEnabled(false);
                parseUser.saveInBackground(e -> {
                    if (e != null) {

                        Log.d("Dateyou","Failed to save Facebook data to Server");
                    }

                    Log.d("Dateyou","Saved Facebook InstagramData to Server");

                    if (Config.isFakeMessagesActivated){
                        QuickActions.sendFakeMessage(parseUser);
                    }

                    try {
                        profilePic = fbUser.getJSONObject("picture").getJSONObject("data").getString("url");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    new ProfilePhotoAsync(profilePic).execute();

                    Log.d("Dateyou","url: " + profilePic);

                });

            }
        };

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields",  "id,email,name,first_name,last_name,gender,birthday,picture.width(720).height(720),location");
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
            // Fetching data from URI and storing in bitmap
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

        ArrayList<ParseFile> pFileList = new ArrayList<>();

        if (bitmap != null) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();

            ParseFile file  = new ParseFile("picture.jpeg", image);
            pFileList.add(file);

            file.saveInBackground((SaveCallback) e -> {
                if (e != null) {

                    loginSuccess();

                } else {

                    addPhotoToProfile(pFileList);
                }
            });

        } else {

            loginSuccess();

        }

    }

    public void addPhotoToProfile(ArrayList<ParseFile> photoFiles){

        User user = (User) ParseUser.getCurrentUser();
        user.setProfilePhotos(photoFiles);

        user.saveInBackground(e -> loginSuccess());
    }

    private void loginSuccess() {

        User user = User.getUser();

        ParseInstallation.getCurrentInstallation().put("user", user);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        user.setInstallation(ParseInstallation.getCurrentInstallation());
        user.saveInBackground();

        ParsePush.subscribeInBackground(Config.CHANNEL);

        QuickHelp.hideLoading(WelcomeActivity.this);
        QuickHelp.goToActivityAndFinish(this, DispatchActivity.class);

        stopFbLogin();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        ParseGoogleSignIn.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){

        QuickHelp.onBackPressed(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        mGenderLayout.setVisibility(View.VISIBLE);
    }


    public void startFbLogin(){
        mLogin.setEnabled(false);
        mLogin.setVisibility(View.INVISIBLE);

        mFacebook.setEnabled(false);
        mFacebook.setVisibility(View.INVISIBLE);

        mFacebookText.setVisibility(View.INVISIBLE);

        mProgress.setVisibility(View.VISIBLE);
        
        mButtonMale.setEnabled(false);
        mButtonFemale.setEnabled(false);

    }

    public void stopFbLogin(){
        mLogin.setEnabled(true);
        mLogin.setVisibility(View.VISIBLE);

        mFacebook.setEnabled(true);
        mFacebook.setVisibility(View.VISIBLE);

        mFacebookText.setVisibility(View.VISIBLE);

        mProgress.setVisibility(View.INVISIBLE);

        mButtonMale.setEnabled(true);
        mButtonFemale.setEnabled(true);

    }
}
