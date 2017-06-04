package cn.itcast.googleplay10.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.CategoryInfo;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/5,10:06.
 */

public class CategoryNormalHolder extends BaseHolder<CategoryInfo> {
    @InjectView(R.id.image1)
    ImageView image1;
    @InjectView(R.id.text1)
    TextView text1;
    @InjectView(R.id.llItem1)
    LinearLayout llItem1;
    @InjectView(R.id.image2)
    ImageView image2;
    @InjectView(R.id.text2)
    TextView text2;
    @InjectView(R.id.llItem2)
    LinearLayout llItem2;
    @InjectView(R.id.image3)
    ImageView image3;
    @InjectView(R.id.text3)
    TextView text3;
    @InjectView(R.id.llItem3)
    LinearLayout llItem3;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_category_item);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void refreshView() {
        text1.setText(data.getName1());
        text2.setText(data.getName2());
        text3.setText(data.getName3());

        String url1 = HttpHelper.URL + "image?name=" + data.getUrl1();
        String url2 = HttpHelper.URL + "image?name=" + data.getUrl2();
        String url3 = HttpHelper.URL + "image?name=" + data.getUrl3();
        //bitmapUtils.display(appIcon,url);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//是否缓存在内存
                .cacheOnDisk(true)//释放缓存在磁盘
                .considerExifParams(true)//Exif：图片的附加信息
                .displayer(new FadeInBitmapDisplayer(500))//设置展示器
                .build();
        ImageLoader.getInstance().displayImage(url1, image1, options);
        ImageLoader.getInstance().displayImage(url2, image2, options);
        ImageLoader.getInstance().displayImage(url3, image3, options);

    }
}
