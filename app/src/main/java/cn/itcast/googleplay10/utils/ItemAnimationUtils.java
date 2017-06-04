package cn.itcast.googleplay10.utils;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * Created by zhengping on 2016/12/4,10:45.
 * 处理ＬｉｓｔＶｉｅｗ的ｉｔｅｍ的动画效果
 */

public class ItemAnimationUtils {

    public static void startAnim(View view) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"alpha",0.0f,1.0f);
        objectAnimator.setDuration(500);
        objectAnimator.start();

        /*AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);*/

        //view.setScaleX();

        /*view.setPivotX(0);
        view.setPivotY(0);*/
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view,"scaleX",0.5f,1.0f);
        objectAnimatorX.setDuration(500);
        objectAnimatorX.start();
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view,"scaleY",0.5f,1.0f);
        objectAnimatorY.setDuration(500);
        objectAnimatorY.start();

       /* ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f,1.0f,0.5f,1.0f);

        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(true);
        //设置动画的插入器
        animationSet.setInterpolator(new OvershootInterpolator(2));

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);


        view.startAnimation(animationSet);*/

    }
}
