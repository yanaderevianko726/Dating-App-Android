package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.dotsindicator;

import android.content.Context;
import android.util.TypedValue;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.R;


public class UiUtils {
  public static int getThemePrimaryColor(final Context context) {
    final TypedValue value = new TypedValue();
    context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
    return value.data;
  }
}
