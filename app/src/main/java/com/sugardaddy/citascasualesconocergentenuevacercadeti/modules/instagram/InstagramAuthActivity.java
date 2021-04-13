package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.app.Config;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.InstagramUser;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InstagramAuthActivity extends AuthActivity {

  private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/?client_id=%1$s&redirect_uri=%2$s&response_type=code&scope=%3$s";
  private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
  private static final String TOKEN_GRAPH_URL = "https://graph.instagram.com/access_token";
  private static final String PAGE_LINK = "https://www.instagram.com/%1$s/";

  public final String AccessTokenShort = "";
  public final String AccessTokenLong = "";
  public final String UserId = "";

  private String clientId;
  private String clientSecret;
  private String redirectUrl;
  //private ProgressDialog loadingDialog;
  private WebView mWebView;
  private LinearLayout mProgressBar;
  private TextView mProgressTitle;

  public static void start(Context context) {
    Intent intent = new Intent(context, InstagramAuthActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.fragment_auth_instagram);

    Tools.setSystemBarColor(this, R.color.white);
    Tools.setSystemBarLight(this);

    setFinishOnTouchOutside(false);

    mWebView = findViewById(R.id.web_view);
    mProgressBar = findViewById(R.id.loading);
    mProgressTitle = findViewById(R.id.loading_text);


    clientId = Config.INSTAGRAM_APP_ID;
    clientSecret = Config.INSTAGRAM_APP_SECRET;
    redirectUrl = Config.INSTAGRAM_REDIRECT_URI;

    //loadingDialog = DialogFactory.createLoadingDialog(this);

    String scopes = TextUtils.join("+", getAuthData().getScopes());

    String url = String.format(AUTH_URL, clientId, redirectUrl, scopes);

    //WebView webView = new WebView(this);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.loadUrl(url);
    mWebView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        //loadingDialog.show();

        isLoading(true);
        mProgressTitle.setText(getString(R.string.connecting_inst));
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //loadingDialog.dismiss();
        isLoading(false);
      }

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(redirectUrl)) {
          getCode(Uri.parse(url));
          return true;
        }
        return false;
      }
    });

    //setContentView(webView);
  }

  @Override
  protected AuthData getAuthData() {
    return AuthDataHolder.getInstance().instagramAuthData;
  }

  @Override
  public void onBackPressed() {
    handCancel();
  }

  private void getCode(Uri uri) {
    String code = uri.getQueryParameter("code");
    if (code != null) {
      getAccessToken(code);
    } else if (uri.getQueryParameter("error") != null) {
      String errorMsg = uri.getQueryParameter("error_description");
      handleError(new Throwable(errorMsg));
    }
  }

  private void getAccessToken(String code) {
    RequestBody formBody = new FormBody.Builder()
      .add("client_id", clientId)
      .add("client_secret", clientSecret)
      .add("grant_type", "authorization_code")
      .add("redirect_uri", redirectUrl)
      .add("code", code)
      .build();

    Request request = new Request.Builder().post(formBody)
      .url(TOKEN_URL)
      .build();


    isLoading(true);
    mProgressTitle.setText(getString(R.string.splash_loading));
    //loadingDialog.show();

    new OkHttpClient().newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, final IOException e) {
        runOnUiThread(() -> {
          //loadingDialog.dismiss();
          isLoading(false);
          handleError(e);
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          runOnUiThread(() -> {
            //loadingDialog.dismiss();
            isLoading(false);
            handleError(new Throwable("Failed to get access token."));
          });
          return;
        }

        String body = response.body().string();

        Log.v("Instagram", body);

        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(body);
          String accessToken = jsonObject.getString("access_token");
          String userId = jsonObject.getString("user_id");

          exchangeAccessToken(accessToken, userId);

          /*InstagramUser instagramUser = new InstagramUser();
          instagramUser.setAccess_token(accessToken);
          instagramUser.setUser_id(userId);

          runOnUiThread(() -> {
            isLoading(false);

            handleSuccess(instagramUser);
          });*/

        } catch (JSONException e) {
          e.printStackTrace();

          runOnUiThread(() -> {
            //loadingDialog.dismiss();
            isLoading(false);
            handleError(new Throwable("Failed to get access token."));
          });
        }
      }
    });
  }

  private void exchangeAccessToken(String accessToken, String userId) {

    Log.e("Instagram token", accessToken);

    RequestBody formBody = new FormBody.Builder()
            //.add("client_id", clientId)
            .add("client_secret", clientSecret)
            .add("grant_type", "ig_exchange_token")
            //.add("redirect_uri", redirectUrl)
            //.add("code", code)
            .add("access_token", accessToken)
            .build();

    Request request = new Request.Builder().get()
            .url(TOKEN_GRAPH_URL +"?grant_type=ig_exchange_token&client_secret="+clientSecret +"&access_token="+accessToken )
            .build();

    new OkHttpClient().newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, final IOException e) {
        runOnUiThread(() -> {
          //loadingDialog.dismiss();
          Log.e("Instagram error1", e.getMessage());
          isLoading(false);
          handleError(e);
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          runOnUiThread(() -> {
            //loadingDialog.dismiss();
            try {
              Log.e("Instagram error2", response.body().string());
            } catch (IOException e) {
              e.printStackTrace();
            }
            isLoading(false);
            handleError(new Throwable("Failed to get access token."));
          });
          return;
        }

        String body = response.body().string();

        Log.v("Instagram graph", body);

        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(body);
          String accessToken = jsonObject.getString("access_token");
          //String userId = jsonObject.getString("user_id");

          InstagramUser instagramUser = new InstagramUser();
          instagramUser.setAccess_token(accessToken);
          instagramUser.setUser_id(userId);

          runOnUiThread(() -> {
            isLoading(false);

            handleSuccess(instagramUser);
          });

        } catch (JSONException e) {
          e.printStackTrace();

          Log.e("Instagram error3", e.getMessage());

          runOnUiThread(() -> {
            //loadingDialog.dismiss();
            isLoading(false);
            handleError(new Throwable("Failed to get access token."));
          });
        }
      }
    });


  }

  public void isLoading(boolean isLoading){

    if (isLoading){
      mProgressBar.setVisibility(View.VISIBLE);
    } else mProgressBar.setVisibility(View.GONE);
  }
}
