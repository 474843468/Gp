package cn.itcast.googleplay10.holder;

import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.bean.DownloadInfo;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.manager.MyBitmapManager;
import cn.itcast.googleplay10.manager.MyDownloadManager;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.ProgressArc;

import static android.text.format.Formatter.formatFileSize;
import static cn.itcast.googleplay10.R.id.tv_download;

/**
 * Created by zhengping on 2016/12/2,10:04.
 */

public class HomeHolder extends BaseHolder<AppInfo> implements View.OnClickListener, MyDownloadManager.DownloadObserver {


    @InjectView(R.id.app_icon)
    ImageView appIcon;
    @InjectView(R.id.app_name)
    TextView appName;
    @InjectView(R.id.rating_bar)
    RatingBar ratingBar;
    @InjectView(R.id.app_size)
    TextView appSize;
    @InjectView(R.id.flProgress)
    FrameLayout flProgress;
    @InjectView(tv_download)
    TextView tvDownload;
    @InjectView(R.id.app_des)
    TextView appDes;
    private ProgressArc progressArc;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.list_item_home);
        //必须要进行注入
        ButterKnife.inject(this,view);

        tvDownload.setOnClickListener(this);
        MyDownloadManager.getInstance().addDownloadObserver(this);

        progressArc = new ProgressArc(UiUtils.getContext());
        progressArc.setArcDiameter(UiUtils.dip2px(26));
        progressArc.setProgressColor(R.color.progress);
        ViewGroup.LayoutParams layoutParams = flProgress.getLayoutParams();
        layoutParams.height = UiUtils.dip2px(27);
        layoutParams.width = UiUtils.dip2px(27);
        flProgress.setLayoutParams(layoutParams);

        flProgress.addView(progressArc);

        return view;
    }

    @Override
    public void refreshView() {
        appName.setText(data.name);
        float stars = Float.parseFloat(data.stars);
        ratingBar.setRating(stars);
        //格式化文件大小的显示
        String fileSize = Formatter.formatFileSize(UiUtils.getContext(), Long.parseLong(data.size));
        appSize.setText(fileSize);
        appDes.setText(data.des);


        /**
         * LRUCache:容器
         */
        //BitmapUtils bitmapUtils = MyBitmapManager.getInstance().getBitmapUtils();//new BitmapUtils(UiUtils.getContext());
        //主域名＋模块名称＋参数
        String url = HttpHelper.URL + "image?name=" + data.iconUrl;
        //bitmapUtils.display(appIcon,url);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)//加载中的图片
                .showImageForEmptyUri(R.drawable.ic_empty)//ｎｕｌｌ　　地址为ｎｕｌｌ的情况
                .showImageOnFail(R.drawable.ic_error)//加载失败
                .cacheInMemory(true)//是否缓存在内存
                .cacheOnDisk(true)//释放缓存在磁盘
                .considerExifParams(true)//Exif：图片的附加信息
                .displayer(new FadeInBitmapDisplayer(500))//设置展示器
                .build();
        ImageLoader.getInstance().displayImage(url, appIcon, options);

        DownloadInfo downloadInfo = MyDownloadManager.getInstance().mSavedDownloadInfo.get(data.id);
        if (downloadInfo == null) {
            downloadInfo = DownloadInfo.createDownloadInfoFromAppInfo(data);
        }

        updateUi(downloadInfo);

    }

    @Override
    public void onClick(View v) {
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

        if (downloadInfo.id.equals(data.id)) {
            long currentPosition = downloadInfo.currentPosition;
            int currentState = downloadInfo.currentState;
            float floatPercent = currentPosition * 1.0f / Long.parseLong(data.size);
            int intPercent = (int) ((currentPosition * 1.0f / Long.parseLong(data.size)) * 100);
            String percent = (intPercent) + "%";
            String tips = "";
            switch (currentState) {
                case MyDownloadManager.STATE_NONE:
                    tips = "下载";
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    progressArc.setBackgroundResource(R.drawable.ic_download);
                    break;
                case MyDownloadManager.STATE_DOWNLOADING:
                    tips = "下载中";
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                    progressArc.setBackgroundResource(R.drawable.ic_pause);
                    break;
                case MyDownloadManager.STATE_ERROR:
                    tips = "下载失败";
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    progressArc.setBackgroundResource(R.drawable.ic_error);
                    break;
                case MyDownloadManager.STATE_SUCCESS:
                    tips = "点击安装";
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    progressArc.setBackgroundResource(R.drawable.ic_install);
                    break;
                case MyDownloadManager.STATE_WAITING:
                    tips = "等待中";
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    progressArc.setBackgroundResource(R.drawable.ic_pause);
                    break;
                case MyDownloadManager.STATE_PAUSED:
                    tips = "继续下载";
                    progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                    progressArc.setBackgroundResource(R.drawable.ic_download);
                    break;
            }
            tvDownload.setText(tips);
            progressArc.setProgress(floatPercent,true);

        }
    }
}
