package cn.itcast.googleplay10.bean;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import cn.itcast.googleplay10.manager.MyDownloadManager;

/**
 * Created by zhengping on 2016/12/8,10:05.
 */

public class DownloadInfo {

    public String downloadUrl;
    public String id;
    public String name;
    public String packageName;
    public String size;
    public long currentPosition;//当前下载的位置
    public int currentState;//下载的状态
    public String filePath;//下载存储的本地路径


    public static DownloadInfo createDownloadInfoFromAppInfo(AppInfo appInfo) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.id = appInfo.id;
        downloadInfo.downloadUrl = appInfo.downloadUrl;
        downloadInfo.name = appInfo.name;
        downloadInfo.packageName = appInfo.packageName;
        downloadInfo.size = appInfo.size;
        downloadInfo.currentState = MyDownloadManager.STATE_NONE;
        downloadInfo.currentPosition = 0;
        downloadInfo.filePath = getFilePath(appInfo.name);//   /sdcard/GooglePlay10/xxx.apk
        return downloadInfo;
    }

    public static String getFilePath(String name) {
        File rootDir = Environment.getExternalStorageDirectory();
        File appDir = new File(rootDir, "GooglePlay10");
        if(!appDir.exists()||appDir.isFile()) {
            if(appDir.mkdirs()) {

            } else {
                return null;
            }
        }
        File apkFile = new File(appDir, name + ".apk");
        return apkFile.getAbsolutePath();
    }





}
