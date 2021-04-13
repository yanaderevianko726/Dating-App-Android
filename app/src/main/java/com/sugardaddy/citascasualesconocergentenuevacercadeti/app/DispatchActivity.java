package com.sugardaddy.citascasualesconocergentenuevacercadeti.app;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.auth.WelcomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.helpers.QuickHelp;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.home.HomeActivity;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.models.datoo.User;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.SharedPrefUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.StatusBarUtil;
import com.sugardaddy.citascasualesconocergentenuevacercadeti.utils.Tools;
import com.parse.ParseUser;


public class DispatchActivity extends AppCompatActivity {

    User mCurrentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    mCurrentUser = (User)User.getCurrentUser();

      StatusBarUtil.useTransparentBarWithCurrentBackground(this);
      StatusBarUtil.setLightMode(this);

      Tools.setSystemBarColor(this, R.color.white);
      Tools.setSystemBarLight(this);


    // Check if there is a valid user session or not

    if (mCurrentUser != null )  {

      if (!mCurrentUser.getLanguage().isEmpty()){
        new SharedPrefUtil(this).setLanguage(this, mCurrentUser.getLanguage());
      } else {
        mCurrentUser.setLanguage(new SharedPrefUtil(this).getLanguage(this));
        mCurrentUser.saveInBackground();
      }

      if (mCurrentUser.isUserBlocked()){

        QuickHelp.showToast(this, getString(R.string.user_blocked_by_admin), true);

        ParseUser.logOut();
        QuickHelp.goToActivityAndFinish(this, WelcomeActivity.class);

      } else {

        QuickHelp.goToActivityAndFinish(this, HomeActivity.class);
      }

    } else {

        QuickHelp.goToActivityAndFinish(this, WelcomeActivity.class);
    }


  }

}
