package cn.itcast.googleplay10.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import cn.itcast.googleplay10.application.MyApplication;

/**
 * Created by zhengping on 2016/12/1,10:29.
 * 处理和UI操作相关的工具类
 */

public class UiUtils {

    //获取全局Context对象
    public static Context getContext() {
        return MyApplication.instance.context;
    }

    //获取主线程的Handler对象
    public static Handler getMainThreadHandler() {
        return MyApplication.instance.handler;
    }

    //获取主线程的线程id
    public static int getMainThreadId() {
        return MyApplication.instance.mainThreadId;
    }

    //获取字符串
    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    //获取字符串数组
    public static String[] getStringArray(int resId) {
        return getContext().getResources().getStringArray(resId);
    }

    //获取drawable
    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId);
    }

    public static int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }

    //产生随机的颜色值　　90~230
    public static int getRandomColor() {
        Random random= new Random();
        int red =  90 + random.nextInt(141);;
        int green= 90 + random.nextInt(141);;
        int blue=  90 + random.nextInt(141);;
        int color = Color.rgb(red, green, blue);
        return color;
    }
    //获取文字大小　　　１６～２５
    public static int getRandomTextSize() {
        Random random= new Random();
        return 16+random.nextInt(10);
    }

    //获取颜色的状态选择器
    public static ColorStateList getColorStateList(int resId) {
        return getContext().getResources().getColorStateList(resId);
    }

    public static int getDimen(int resId) {
        return getContext().getResources().getDimensionPixelSize(resId);
    }

    //dip2px
    public static int dip2px(int dip) {
        //屏幕密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    //px2dip
    public static int px2dip(int px) {
        //屏幕密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px/density + 0.5f);
    }


    public static View inflateView(int resId) {
        return View.inflate(getContext(), resId, null);
    }

    public static void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    //判断是否是在主线程
    public static boolean isRunOnUiThread() {
        //1、获取当前线程的id
        int currentThreadId = android.os.Process.myTid();
        //2、获取主线程的id
        int mainThreadId = getMainThreadId();
        //3、做比较
        return currentThreadId == mainThreadId;
    }

    /**
     * 保证r这个任务一定是在主线程中执行
     *
     * Process:进程
     * Thread：线程
     * Runnable：任务
     *
     * @param r
     */
    public static void runOnUiThread(Runnable r) {
        if (isRunOnUiThread()) {
            //主线程
            //new Thread(r).start();
            r.run();
        } else {
            //子线程
            getMainThreadHandler().post(r);//将任务r丢到了主线程的消息队列
        }
    }

    //代码中创建shape标签对应的对象
    public static GradientDrawable getShape(int radius,int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }
    //代码中获取一个状态选择器  对应的类StateListDrawable
    public static StateListDrawable getSelector(Drawable pressedDrawable,Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},pressedDrawable);
        stateListDrawable.addState(new int[]{},normalDrawable);
        return stateListDrawable;
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

}
