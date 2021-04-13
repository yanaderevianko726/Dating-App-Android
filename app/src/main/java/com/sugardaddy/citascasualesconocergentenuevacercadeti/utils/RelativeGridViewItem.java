package com.sugardaddy.citascasualesconocergentenuevacercadeti.utils;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.RelativeLayout;


public class RelativeGridViewItem extends RelativeLayout {

    public RelativeGridViewItem(Context context) {
        super(context);
    }

    public RelativeGridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeGridViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}
