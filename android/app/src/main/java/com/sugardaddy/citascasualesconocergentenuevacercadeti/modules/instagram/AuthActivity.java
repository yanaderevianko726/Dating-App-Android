package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.instagram;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.InstagramUser;

abstract class AuthActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
  }

  protected void handCancel() {

    if (getAuthData() != null){

      getAuthData().getCallback().onCancel();
    }

    finish();
  }

  protected void handleError(Throwable error) {

    if (getAuthData() != null){
      getAuthData().getCallback().onError(error);
    }

    finish();
  }

  protected void handleSuccess(InstagramUser user) {

    if (getAuthData() != null){
      getAuthData().getCallback().onSuccess(user);
    }

    finish();
  }

  protected abstract AuthData getAuthData();

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (getAuthData() != null){
      getAuthData().clearCallback();
    }

  }
}
