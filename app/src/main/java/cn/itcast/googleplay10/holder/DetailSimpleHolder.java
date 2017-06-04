package cn.itcast.googleplay10.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/7,10:51.
 */

public class DetailSimpleHolder extends BaseHolder<AppInfo> {
    @InjectView(R.id.appicon)
    ImageView appicon;
    @InjectView(R.id.appname)
    TextView appname;
    @InjectView(R.id.appstar)
    RatingBar appstar;
    @InjectView(R.id.appdownload)
    TextView appdownload;
    @InjectView(R.id.appversion)
    TextView appversion;
    @InjectView(R.id.apptime)
    TextView apptime;
    @InjectView(R.id.appsize)
    TextView appsize;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_appinfo_item);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void refreshView() {
        appname.setText(data.name);
        appstar.setIsIndicator(true);
        appstar.setRating(Float.parseFloat(data.stars));
        appdownload.setText(data.downloadNum);
        appversion.setText(data.version);
        apptime.setText(data.date);
        appsize.setText(Formatter.formatFileSize(UiUtils.getContext(), Long.parseLong(data.size)));


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
        ImageLoader.getInstance().displayImage(url, appicon, options);

    }
}
