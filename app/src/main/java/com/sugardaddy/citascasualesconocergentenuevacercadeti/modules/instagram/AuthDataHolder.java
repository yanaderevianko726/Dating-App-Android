package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.instagram;

public class AuthDataHolder {
  private static final AuthDataHolder instance = new AuthDataHolder();

  public AuthData instagramAuthData;

  private AuthDataHolder() {
  }

  public static AuthDataHolder getInstance() {
    return instance;
  }
}
