package cn.itcast.googleplay10.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.SubjectInfo;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/4,14:16.
 */

public class SubjectHolder extends BaseHolder<SubjectInfo> {
    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.tv)
    TextView tv;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.list_item_subject);
        ButterKnife.inject(this,view);

        return view;
    }

    @Override
    public void refreshView() {
        tv.setText(data.des);

        String url = HttpHelper.URL + "image?name=" + data.url;
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
        ImageLoader.getInstance().displayImage(url, image, options);
    }
}
