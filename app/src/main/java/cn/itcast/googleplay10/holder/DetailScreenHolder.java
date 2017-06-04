package cn.itcast.googleplay10.holder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.activity.ImageDetailActivity;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/7,11:18.
 */

public class DetailScreenHolder extends BaseHolder<AppInfo> {

    private ImageView[] screenImages;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_detail_screen);
        screenImages = new ImageView[5];
        screenImages[0] = (ImageView) view.findViewById(R.id.image1);
        screenImages[1] = (ImageView) view.findViewById(R.id.image2);
        screenImages[2] = (ImageView) view.findViewById(R.id.image3);
        screenImages[3] = (ImageView) view.findViewById(R.id.image4);
        screenImages[4] = (ImageView) view.findViewById(R.id.image5);
        return view;
    }

    @Override
    public void refreshView() {

        final ArrayList<String> screenList = data.screen;


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

        for(int i=0;i<screenList.size();i++) {
            String picUrl = screenList.get(i);
            String url = HttpHelper.URL + "image?name=" + picUrl;
            ImageLoader.getInstance().displayImage(url, screenImages[i], options);
            screenImages[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //在安卓中，所有的组件中，只有Activity是有栈的，其他的都没有栈
                    Intent intent = new Intent();
                    intent.setClass(UiUtils.getContext(), ImageDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("picList", screenList);
                    UiUtils.getContext().startActivity(intent);
                }
            });
        }

    }
}
