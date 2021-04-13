package com.sugardaddy.citascasualesconocergentenuevacercadeti.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;


public class CustomProgressView extends View {
    private static final float INDETERMINANT_MIN_SWEEP = 15f;
    private int mSize;
    private Paint mPaint;
    private RectF mRect;
    private float mIndeterminateSweep;
    private float mstartAngle;
    private AnimatorSet mIndeterminateAnimator;

    public CustomProgressView(Context context) {
        super(context);
        init();
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mRect = new RectF(10, 10, 100, 100);

        mIndeterminateSweep = 10f;

    }


    public void setColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRect, mstartAngle, mIndeterminateSweep, false, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int xPad = getPaddingLeft() + getPaddingRight();
        int yPad = getPaddingTop() + getPaddingBottom();
        int width = getMeasuredWidth() - xPad;
        int height = getMeasuredHeight() - yPad;
        mSize = (width < height) ? width : height;
        setMeasuredDimension(mSize + xPad, mSize + yPad);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    private AnimatorSet createIndeterminateAnimator() {
        final ValueAnimator frontEndExtend = ValueAnimator.ofFloat(0, 360);
        frontEndExtend.setDuration(5000);
        frontEndExtend.setInterpolator(new DecelerateInterpolator(1));
        frontEndExtend.addUpdateListener(animation -> {
            mstartAngle = (Float) animation.getAnimatedValue();
            mIndeterminateSweep += 2;
            if (mIndeterminateSweep > 360)
                mIndeterminateSweep = INDETERMINANT_MIN_SWEEP;
            invalidate();
        });

        AnimatorSet set = new AnimatorSet();
        set.play(frontEndExtend);
        return set;
    }

    private void resetAnimation() {

        if (mIndeterminateAnimator != null && mIndeterminateAnimator.isRunning())
            mIndeterminateAnimator.cancel();
        mIndeterminateSweep = INDETERMINANT_MIN_SWEEP;

        // Build the whole AnimatorSet
        mIndeterminateAnimator = createIndeterminateAnimator();
        // Listen to end of animation so we can infinitely loop
        mIndeterminateAnimator.addListener(new AnimatorListenerAdapter() {
            boolean wasCancelled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                wasCancelled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!wasCancelled)
                    resetAnimation();
            }
        });
        mIndeterminateAnimator.start();
    }

    /**
     * Starts the progress bar animation.
     * (This is an alias of resetAnimation() so it does the same thing.)
     */
    public void startAnimation() {
        resetAnimation();
    }

    public void stopAnimation() {
        if (mIndeterminateAnimator != null) {
            mIndeterminateAnimator.cancel();
            mIndeterminateAnimator = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        int currentVisibility = getVisibility();
        super.setVisibility(visibility);
        if (visibility != currentVisibility) {
            if (visibility == View.VISIBLE) {
                resetAnimation();
            } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
                stopAnimation();
            }
        }
    }

}