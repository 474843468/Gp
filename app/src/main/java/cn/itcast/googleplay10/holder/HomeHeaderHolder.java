package cn.itcast.googleplay10.holder;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.UiUtils;

import static cn.itcast.googleplay10.R.id.viewPager;

/**
 * Created by zhengping on 2016/12/5,15:52.
 */

public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {

    private ViewPager viewPager;
    private LinearLayout llDot;

    @Override
    public View initView() {
        RelativeLayout rootLayout = new RelativeLayout(UiUtils.getContext());
        ListView.LayoutParams rootParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, UiUtils.dip2px(180));
        rootLayout.setLayoutParams(rootParams);
        viewPager = new ViewPager(UiUtils.getContext());

        RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(viewPagerParams);
        rootLayout.addView(viewPager);


        llDot = new LinearLayout(UiUtils.getContext());
        RelativeLayout.LayoutParams dotParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dotParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dotParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        dotParams.rightMargin = UiUtils.dip2px(10);
        dotParams.bottomMargin = UiUtils.dip2px(10);
        llDot.setLayoutParams(dotParams);

        rootLayout.addView(llDot);

        return rootLayout;
    }

    @Override
    public void refreshView() {
        //给ViewPager刷新数据
        viewPager.setAdapter(new MyAdapter());
        for(int i=0;i<data.size();i++) {
            ImageView dotImage = new ImageView(UiUtils.getContext());
            if(i==0) {
                dotImage.setImageResource(R.drawable.indicator_selected);
            } else {
                dotImage.setImageResource(R.drawable.indicator_normal);
            }


            llDot.addView(dotImage);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int childCount = llDot.getChildCount();
                for(int i=0;i<childCount;i++) {
                    ImageView imageView = (ImageView) llDot.getChildAt(i);
                    if (position == i) {
                        imageView.setImageResource(R.drawable.indicator_selected);
                    } else {
                        imageView.setImageResource(R.drawable.indicator_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mHandler.sendEmptyMessageDelayed(0, 2000);//促发第一个消息

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosisition = viewPager.getCurrentItem();
            int nextPosition = currentPosisition + 1;
            if(nextPosition > data.size()-1) {
                nextPosition = 0;
            }
            viewPager.setCurrentItem(nextPosition, true);

            mHandler.sendEmptyMessageDelayed(0, 2000);

        }
    };

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView iv = new ImageView(UiUtils.getContext());

            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String url = HttpHelper.URL + "image?name=" + data.get(position);
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
            ImageLoader.getInstance().displayImage(url, iv, options);

            container.addView(iv);


            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
