package cn.itcast.googleplay10.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

import cn.itcast.googleplay10.bean.AppInfo;

/**
 * Created by zhengping on 2016/12/1,10:03.
 * 1、生命周期长
 * 2、单实例
 * 3、onCreate方法可以简单的认为是一个应用程序的入口,onCreate是运行在主线程中
 *
 * 问题：onCreate这个方法只执行一次么？
 *
 * 注意事项：需要清单文件中注册
 */

public class MyApplication extends Application {

    public  Context context;
    public  Handler handler;
    public  int mainThreadId;

    public static MyApplication instance;

    public AppInfo appInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //Context 获取全局的context对象    new出一个View，加载布局文件，Toast
        context = getApplicationContext();

        //线程间的通信
        //handler.sendMessage:发送一个消息到消息队列
        //主线程有主线程的消息队列，子线程有子线程的消息队列
        //到底发送到哪一个线程的消息队列，得看handler维护的是哪个线程的消息队列
        //指定Handler维护的是主线程消息队列的方式：1、2、
        handler = new Handler();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Handler mHandler = new Handler(Looper.getMainLooper());
            }
        }).start();*/

        //判断当前线程是主线程还是子线程
        mainThreadId = Process.myTid();

        initImageLoader(getApplicationContext());
    }

    private ArrayList<Activity> activityArrayList = new ArrayList<>();

    public void addActivity(Activity activity) {
        activityArrayList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityArrayList.remove(activity);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
