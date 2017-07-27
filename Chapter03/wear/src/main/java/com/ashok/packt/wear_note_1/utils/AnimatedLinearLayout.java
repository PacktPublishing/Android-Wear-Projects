package com.ashok.packt.wear_note_1.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by ashok.kumar on 10/04/17.
 */

public class AnimatedLinearLayout extends LinearLayout {
    Animation animation;
    View currentChild;

    public AnimatedLinearLayout(Context context) {
        super(context);
    }

    public AnimatedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        final String SLIDE_DOWN = "SlideDown";
        final String SLIDE_DOWN_MORE = "SlideDownMore";
        final String ROTATE_CLOCKWISE = "RotateClockWise";
        final String ROTATE_ANTI_CLOCKWISE = "RotateAntiClockWise";
        final String ZOOMIN_AND_ROTATE_CLOCKWISE = "ZoomInAndRotateClockWise";
        if (hasWindowFocus) {
            for (int index = 0; index < getChildCount(); index++) {
                View child = getChildAt(index);
                currentChild=child;
                if(!(child instanceof ViewGroup)) {
                    switch (child.getTag().toString()) {
                        case SLIDE_DOWN:
                            animation = new TranslateAnimation(0, 0, -((child.getMeasuredHeight()/2) * (index + 1)), 0);
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.setFillAfter(true);
                            animation.setDuration(1000);
                            child.post(new AnimationRunnable(animation,child));
                            //child.startAnimation(animation);
                            break;
                        case SLIDE_DOWN_MORE:
                            animation = new TranslateAnimation(0, 0, -(child.getMeasuredHeight() * (index + 25)), 0);
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.setFillAfter(true);
                            animation.setDuration(1000);
                            child.post(new AnimationRunnable(animation,child));
                            //child.startAnimation(animation);
                            break;
                        case ROTATE_CLOCKWISE:
                            animation = new RotateAnimation(0, 360, child.getMeasuredWidth() / 2, child.getMeasuredHeight() / 2);
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.setFillAfter(true);
                            animation.setDuration(1000);
                            child.post(new AnimationRunnable(animation,child));
                            //child.startAnimation(animation);
                            break;
                        case ROTATE_ANTI_CLOCKWISE:
                            animation = new RotateAnimation(0, -360, child.getMeasuredWidth() / 2, child.getMeasuredHeight() / 2);
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.setFillAfter(true);
                            animation.setDuration(1000);
                            child.post(new AnimationRunnable(animation,child));
                            //child.startAnimation(animation);
                            break;
                        case ZOOMIN_AND_ROTATE_CLOCKWISE:
                            AnimationSet animationSet = new AnimationSet(true);
                            animationSet.setInterpolator(new DecelerateInterpolator());
                            animation = new ScaleAnimation(0, 1, 0, 1, child.getMeasuredWidth() / 2, child.getMeasuredHeight() / 2);
                            animation.setStartOffset(0);
                            animation.setFillAfter(true);
                            animation.setDuration(1000);
                            animationSet.addAnimation(animation);
                            animation = new RotateAnimation(0, 360, child.getMeasuredWidth() / 2, child.getMeasuredHeight() / 2);
                            animation.setStartOffset(0);
                            animation.setFillAfter(true);
                            animation.setDuration(1000);
                            animationSet.addAnimation(animation);
                            child.post(new AnimationSetRunnable(animationSet,child));
                            //child.startAnimation(animationSet);
                            break;
                    }
                }
            }
        }
    }

    private class AnimationRunnable implements Runnable{
        private Animation animation;
        private View child;
        AnimationRunnable(Animation animation, View child) {
            this.animation=animation;
            this.child=child;
        }

        @Override
        public void run() {
            child.startAnimation(animation);
        }
    }
    private class AnimationSetRunnable implements Runnable{
        private AnimationSet animation;
        private View child;
        AnimationSetRunnable(AnimationSet animation, View child) {
            this.animation=animation;
            this.child=child;
        }

        @Override
        public void run() {
            child.startAnimation(animation);
        }
    }
}