package cn.itcast.googleplay10.holder;

import android.graphics.Color;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.bean.DownloadInfo;
import cn.itcast.googleplay10.manager.MyDownloadManager;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.ProgressHorizontal;

/**
 * Created by zhengping on 2016/12/7,11:28.
 */

public class DetailActionHolder extends BaseHolder<AppInfo> implements MyDownloadManager.DownloadObserver {
    @InjectView(R.id.fav)
    Button fav;
    @InjectView(R.id.share)
    Button share;
    @InjectView(R.id.download_bt)
    Button downloadBt;
    @InjectView(R.id.download_fl)
    FrameLayout downloadFl;
    private ProgressHorizontal ph;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_detail_download);
        ButterKnife.inject(this, view);

        ph = new ProgressHorizontal(UiUtils.getContext());
        ph.setProgressBackgroundResource(R.drawable.progress_bg);
        ph.setProgressResource(R.drawable.progress_pressed);
        ph.setProgressTextColor(Color.WHITE);
        ph.setProgressTextSize(30);

        downloadFl.addView(ph);

        MyDownloadManager.getInstance().addDownloadObserver(this);
        return view;
    }

    @Override
    public void refreshView() {
        DownloadInfo downloadInfo = MyDownloadManager.getInstance().mSavedDownloadInfo.get(data.id);
        if(downloadInfo == null) {
            downloadInfo = DownloadInfo.createDownloadInfoFromAppInfo(data);
        }
        updateUi(downloadInfo);

    }

    @OnClick(R.id.download_bt)
    public void onClick() {
        //应用程序apk的下载  onClick的方法肯定运行在主线程中
        //下载文件必须得要启动子线程  new Thread(new Runnable(){run}).start()
        //后果就是子线程的数量太多，子线程的数量太多，会造成的结果就是UI卡顿
        //CPU处理任务的能力是有限的，子线程太多，势必会造成CPU分配给主线程的时间片变少了
        //此时需要有一个机制，能够保证子线程的数量得到有效的控制  线程池
        //LRUCache：容器，存的是图片，造成的效果，LRUCache里面的图片总量在一定的控制范围之内
        //线程池：容器，存的是线程，造成的效果就是能够保证线程池中的线程数量得到有效的控制


        //应该判断一下当前的状态
        DownloadInfo downloadInfo = MyDownloadManager.getInstance().mSavedDownloadInfo.get(data.id);
        int state = MyDownloadManager.STATE_NONE;
        if(downloadInfo != null) {
            state = downloadInfo.currentState;
        } else {
            //代表之前什么按钮都没有点击过
        }
        switch (state) {
            case MyDownloadManager.STATE_NONE:
                MyDownloadManager.getInstance().startDownload(data);
                break;
            case MyDownloadManager.STATE_DOWNLOADING:
                MyDownloadManager.getInstance().pauseDownload(data);
                break;
            case MyDownloadManager.STATE_ERROR:
                MyDownloadManager.getInstance().startDownload(data);
                break;
            case MyDownloadManager.STATE_SUCCESS:
                MyDownloadManager.getInstance().installApk(data);
                break;
            case MyDownloadManager.STATE_WAITING:
                MyDownloadManager.getInstance().pauseDownload(data);
                break;
            case MyDownloadManager.STATE_PAUSED:
                MyDownloadManager.getInstance().startDownload(data);
                break;
        }


    }

    @Override
    public void onDownloadStateChanged(final DownloadInfo downloadInfo) {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUi(downloadInfo);
            }
        });

    }

    @Override
    public void onDownloadProgressChanged(final DownloadInfo downloadInfo) {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUi(downloadInfo);
            }
        });
    }

    private void updateUi(DownloadInfo downloadInfo) {

        if(downloadInfo.id.equals(data.id)) {
            long currentPosition = downloadInfo.currentPosition;
            int currentState = downloadInfo.currentState;

            int intPercent = (int)((currentPosition*1.0f/Long.parseLong(data.size))*100);
            String percent = (intPercent) + "%";
            String tips = "";
            switch (currentState) {
                case MyDownloadManager.STATE_NONE:
                    tips = "下载";
                    break;
                case MyDownloadManager.STATE_DOWNLOADING:
                    tips = "下载中";
                    break;
                case MyDownloadManager.STATE_ERROR:
                    tips = "下载失败";
                    break;
                case MyDownloadManager.STATE_SUCCESS:
                    tips = "点击安装";
                    break;
                case MyDownloadManager.STATE_WAITING:
                    tips = "等待中";
                    break;
                case MyDownloadManager.STATE_PAUSED:
                    tips = "继续下载";
                    break;
            }

            ph.setCenterText(tips);

            ph.setProgress(intPercent);

            //downloadBt.setText(""+currentState + "," + percent);
        }


    }
}
