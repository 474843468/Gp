package cn.itcast.googleplay10.manager;

import com.lidroid.xutils.BitmapUtils;

import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/4,10:08.
 * 处理和图片相关的管理类，单例模式
 */

public class MyBitmapManager {

    private MyBitmapManager() {}

    private static MyBitmapManager instance;

    public synchronized static MyBitmapManager getInstance() {
        if (instance == null) {
            instance = new MyBitmapManager();
        }
        return instance;
    }

    private BitmapUtils bitmapUtils;

    public BitmapUtils getBitmapUtils() {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(UiUtils.getContext());
        }
        return bitmapUtils;
    }


}
