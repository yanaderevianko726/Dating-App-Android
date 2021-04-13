package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.instagram;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others.InstagramUser;

public interface AuthCallback {
  void onSuccess(InstagramUser socialUser);

  void onError(Throwable error);

  void onCancel();
}
