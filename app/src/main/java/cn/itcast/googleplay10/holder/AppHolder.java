package cn.itcast.googleplay10.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
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
 * Created by zhengping on 2016/12/4,11:24.
 */

public class AppHolder extends BaseHolder<AppInfo> {
    @InjectView(R.id.app_icon)
    ImageView appIcon;
    @InjectView(R.id.app_name)
    TextView appName;
    @InjectView(R.id.rating_bar)
    RatingBar ratingBar;
    @InjectView(R.id.app_size)
    TextView appSize;
    @InjectView(R.id.app_des)
    TextView appDes;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.list_item_app);
        ButterKnife.inject(this, view);
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
    }
}
