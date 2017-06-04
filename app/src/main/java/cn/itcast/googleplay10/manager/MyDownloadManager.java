package cn.itcast.googleplay10.manager;

import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.bean.DownloadInfo;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.IOUtils;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/8,9:59.
 * 下载的管理类
 */

public class MyDownloadManager {

    public static final int STATE_NONE = 0;//未下载
    public static final int STATE_WAITING = 1;//等待中
    public static final int STATE_DOWNLOADING= 2;//下载中
    public static final int STATE_PAUSED= 3;//下载暂停
    public static final int STATE_SUCCESS = 4;//下载成功
    public static final int STATE_ERROR = 5;//下载失败

    private MyDownloadManager() {}

    private static MyDownloadManager instance;

    public synchronized  static MyDownloadManager getInstance() {
        if (instance == null) {
            instance = new MyDownloadManager();
        }
        return instance;
    }

    public HashMap<String, DownloadInfo> mSavedDownloadInfo = new HashMap<>();
    public HashMap<String, DownloadTask> mSavedDownloadTask = new HashMap<>();

    //对于AppInfo来说，downloadUrl、size、id、name、packageName
    //存在本地的路径，当前下载的状态，当前下载的进度
    public void startDownload(AppInfo info) {
        //需要将downloadInfo缓存起来，以便我们继续下载的时候来使用
        DownloadInfo downloadInfo = mSavedDownloadInfo.get(info.id);//DownloadInfo.createDownloadInfoFromAppInfo(info);
        if(downloadInfo == null) {
            downloadInfo = DownloadInfo.createDownloadInfoFromAppInfo(info);
            mSavedDownloadInfo.put(info.id, downloadInfo);
        }

        //开始真正的下载了
        DownloadTask task = new DownloadTask(downloadInfo);
        mSavedDownloadTask.put(info.id, task);
        downloadInfo.currentState = MyDownloadManager.STATE_WAITING;
        notifyDownloadStateChanged(downloadInfo);
        MyThreadPoolManager.getInstance().execute(task);

    }

    public void pauseDownload(AppInfo data) {
        //暂停下载
        DownloadInfo downloadInfo = mSavedDownloadInfo.get(data.id);
        downloadInfo.currentState = STATE_PAUSED;

        //如果有一个任务已经丢到了线程池中，但是run方法还没有执行
        //将任务从等待区域中移除
        DownloadTask task = mSavedDownloadTask.get(data.id);
        MyThreadPoolManager.getInstance().cancle(task);

    }

    public void installApk(AppInfo data) {
        DownloadInfo downloadInfo = mSavedDownloadInfo.get(data.id);
        //打开系统的安装界面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(
                Uri.fromFile(new File(downloadInfo.filePath)),
                "application/vnd.android.package-archive");
        UiUtils.getContext().startActivity(intent);
    }

    class DownloadTask implements  Runnable {

        private DownloadInfo downloadInfo;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            FileOutputStream fos = null;
            try {
                //由于是由线程池来进行管理的，所以只有走到了run方法才代表这个任务被线程池中的线程执行
                downloadInfo.currentState = MyDownloadManager.STATE_DOWNLOADING;
                notifyDownloadStateChanged(downloadInfo);
                //区分一下是否是第一次下载
                File downloadFile = new File(downloadInfo.filePath);
                //下载apk
                String url = "";
                if(!downloadFile.exists()
                        ||downloadInfo.currentPosition==0
                        ||(downloadInfo.currentPosition!=0&&downloadInfo.currentPosition != downloadFile.length())) {
                    //第一次下载
                    downloadFile.delete();
                    downloadInfo.currentPosition = 0;
                    url = HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl;
                } else {
                    //代表的是断电下载,告诉服务器从这个文件的哪个位置开始给我吐数据
                    url = HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl+"&range=" + downloadInfo.currentPosition;
                }


                HttpHelper.HttpResult httpResult = HttpHelper.download(url);
                if(httpResult != null) {
                    //获取文件的输入流
                    InputStream inputStream = httpResult.getInputStream();
                    if(inputStream != null) {
                        //第二个参数必须传true，否则的话，就会覆盖之前已经下载好的那一小部分文件
                        fos = new FileOutputStream(downloadFile,true);
                        byte[] buffer = new byte[1024];//30
                        int length = 0;
                        while((length = inputStream.read(buffer)) != -1 && downloadInfo.currentState == STATE_DOWNLOADING) {
                            fos.write(buffer, 0, length);
                            downloadInfo.currentPosition = downloadInfo.currentPosition + length;
                            notifyDownloadProgressChanged(downloadInfo);
                            fos.flush();
                        }

                        //下载完成
                        //判断一下下载是否成功
                        long serverFileSize = Long.parseLong(downloadInfo.size);
                        long localFileSize = downloadInfo.currentPosition;
                        if(serverFileSize == localFileSize) {
                            //下载成功
                            downloadInfo.currentState = STATE_SUCCESS;
                            notifyDownloadStateChanged(downloadInfo);
                        } else {

                            if(downloadInfo.currentState == STATE_PAUSED) {
                                //2、下载暂停
                                downloadInfo.currentState = STATE_PAUSED;
                                notifyDownloadStateChanged(downloadInfo);
                            } else {
                                //1、下载失败
                                downloadInfo.currentState = STATE_ERROR;
                                notifyDownloadStateChanged(downloadInfo);
                            }
                        }

                    } else {
                        //此时代表服务器访问成功，但是服务器找不到你所要下载的文件
                        //下载失败
                        downloadInfo.currentState = STATE_ERROR;
                        notifyDownloadStateChanged(downloadInfo);
                    }

                } else {
                    //下载失败
                    downloadInfo.currentState = STATE_ERROR;
                    notifyDownloadStateChanged(downloadInfo);
                }

            } catch (Exception e) {
                downloadInfo.currentState = STATE_ERROR;
                notifyDownloadStateChanged(downloadInfo);
            } finally {
                IOUtils.close(fos);
            }

        }
    }

    public interface  DownloadObserver{
        public void onDownloadStateChanged(DownloadInfo downloadInfo);
        public void onDownloadProgressChanged(DownloadInfo downloadInfo);
    }

    private ArrayList<DownloadObserver> observers = new ArrayList<>();

    public void addDownloadObserver(DownloadObserver observer) {
        if(observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    private void notifyDownloadStateChanged(DownloadInfo downloadInfo) {
        for(int i=0;i<observers.size();i++) {
            DownloadObserver downloadObserver = observers.get(i);
            downloadObserver.onDownloadStateChanged(downloadInfo);
        }
    }

    private void notifyDownloadProgressChanged(DownloadInfo downloadInfo) {
        for(int i=0;i<observers.size();i++) {
            DownloadObserver downloadObserver = observers.get(i);
            downloadObserver.onDownloadProgressChanged(downloadInfo);
        }
    }

}
