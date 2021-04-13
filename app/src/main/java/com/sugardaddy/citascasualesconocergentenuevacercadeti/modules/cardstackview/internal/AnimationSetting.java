package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.internal;

import android.view.animation.Interpolator;

import com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.cardstackview.Direction;


public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
