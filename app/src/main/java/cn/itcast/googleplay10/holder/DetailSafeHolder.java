package cn.itcast.googleplay10.holder;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.bean.SafeInfo;
import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.UiUtils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static cn.itcast.googleplay10.R.id.appicon;

/**
 * Created by zhengping on 2016/12/7,11:00.
 */

public class DetailSafeHolder extends BaseHolder<AppInfo> implements View.OnClickListener {


    private ImageView[] imageViews;
    private TextView[] textViews;
    private ImageView[] imagesafes;
    private LinearLayout[] lls;
    private View ll_footer;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_detail_safe);
        //ButterKnife.inject(this, view);
        imageViews = new ImageView[4];
        imageViews[0] = (ImageView) view.findViewById(R.id.ll1_image1);
        imageViews[1] = (ImageView) view.findViewById(R.id.ll2_image2);
        imageViews[2] = (ImageView) view.findViewById(R.id.ll3_image3);
        imageViews[3] = (ImageView) view.findViewById(R.id.ll4_image4);

        textViews = new TextView[4];
        textViews[0] = (TextView) view.findViewById(R.id.ll1_text1);
        textViews[1] = (TextView) view.findViewById(R.id.ll2_text2);
        textViews[2] = (TextView) view.findViewById(R.id.ll3_text3);
        textViews[3] = (TextView) view.findViewById(R.id.ll4_text4);

        imagesafes = new ImageView[4];
        imagesafes[0] = (ImageView) view.findViewById(R.id.imagesafe1);
        imagesafes[1] = (ImageView) view.findViewById(R.id.imagesafe2);
        imagesafes[2] = (ImageView) view.findViewById(R.id.imagesafe3);
        imagesafes[3] = (ImageView) view.findViewById(R.id.imagesafe4);

        lls = new LinearLayout[4];
        lls[0] = (LinearLayout) view.findViewById(R.id.ll1);
        lls[1] = (LinearLayout) view.findViewById(R.id.ll2);
        lls[2] = (LinearLayout) view.findViewById(R.id.ll3);
        lls[3] = (LinearLayout) view.findViewById(R.id.ll4);

        View rlTop = view.findViewById(R.id.rlTop);
        rlTop.setOnClickListener(this);

        ll_footer = view.findViewById(R.id.ll_footer);
        //ll_footer.setVisibility(View.GONE);
        ViewGroup.LayoutParams layoutParams = ll_footer.getLayoutParams();
        layoutParams.height = 0;
        ll_footer.setLayoutParams(layoutParams);

        return view;
    }

    @Override
    public void refreshView() {

        ArrayList<SafeInfo> safeList = data.safe;


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


        for(int i=0;i<safeList.size();i++) {
            SafeInfo safeInfo = safeList.get(i);
            textViews[i].setText(safeInfo.safeDes+safeInfo.safeDes+safeInfo.safeDes);

            String url1 = HttpHelper.URL + "image?name=" + safeInfo.safeUrl;
            ImageLoader.getInstance().displayImage(url1, imagesafes[i], options);

            String url2 = HttpHelper.URL + "image?name=" + safeInfo.safeDesUrl;
            ImageLoader.getInstance().displayImage(url2, imageViews[i], options);

            lls[i].setVisibility(View.VISIBLE);



        }
    }

    private boolean isOpen = false;

    @Override
    public void onClick(View v) {

        if(isOpen) {
            close();
           // isOpen = false;
        } else {
            open();
            //isOpen = true;
        }
        isOpen = !isOpen;

    }

    //增加动画效果
    //0~300
    //0,1,2,3,4,5,6,....300
    //产生一系列的中间值
    private void open() {
        //ll_footer.setVisibility(View.VISIBLE);
        /*ViewGroup.LayoutParams layoutParams = ll_footer.getLayoutParams();
        layoutParams.height = 300;
        ll_footer.setLayoutParams(layoutParams);*/

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                for( int i=0;i<300;i++) {
                    final int temp = i;
                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.LayoutParams layoutParams = ll_footer.getLayoutParams();
                            layoutParams.height = temp;
                            ll_footer.setLayoutParams(layoutParams);
                        }
                    });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        //创建一个值的产生器，这个值的产生器能够产生0到300的中间值
        ValueAnimator animator = ValueAnimator.ofInt(getMinHeight(),getMaxHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //此方法运行在主线程中
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int temp = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = ll_footer.getLayoutParams();
                layoutParams.height = temp;
                ll_footer.setLayoutParams(layoutParams);
            }
        });

        //改变中间值产生的速率
        animator.setInterpolator(new OvershootInterpolator(5));
        animator.setDuration(500);
        animator.start();

        /**
         * target:将要作用动画的对象
         * propertyName：属性名称  XXX
         *
         * 当产生中间值的时候，会调用target.setXXX的方法，把中间值通过参数的方式传递进去
         *
         * 可变长度的数组
         */
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_footer, "alpha", 0, 1);
        objectAnimator.setDuration(2500);
        objectAnimator.start();

        //com.nineoldandroids.animation.ObjectAnimator




    }

    public void setHaha(int temp) {
        ViewGroup.LayoutParams layoutParams = ll_footer.getLayoutParams();
        layoutParams.height = temp;
        ll_footer.setLayoutParams(layoutParams);
    }

   /* public void setHaha(float temp) {

    }*/

    private void close() {
        //ll_footer.setVisibility(View.GONE);
       /* ViewGroup.LayoutParams layoutParams = ll_footer.getLayoutParams();
        layoutParams.height = 0;
        ll_footer.setLayoutParams(layoutParams);*/

        //创建一个值的产生器，这个值的产生器能够产生0到300的中间值
        ValueAnimator animator = ValueAnimator.ofInt(getMaxHeight(),getMinHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //此方法运行在主线程中
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int temp = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = ll_footer.getLayoutParams();
                layoutParams.height = temp;
                ll_footer.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    private int getMaxHeight() {
        //int height = ll_footer.getHeight();
        //手动测量
        int maxWidth = UiUtils.getScreenWidth() - UiUtils.dip2px(10)*2;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.AT_MOST);
        ll_footer.measure(widthMeasureSpec, 0);
        return ll_footer.getMeasuredHeight();
    }

    private int getMinHeight() {
        return 0;
    }
}
