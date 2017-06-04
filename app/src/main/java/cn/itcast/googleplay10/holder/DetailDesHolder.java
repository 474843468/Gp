package cn.itcast.googleplay10.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/7,11:25.
 */

public class DetailDesHolder extends BaseHolder<AppInfo> {
    @InjectView(R.id.inte)
    TextView inte;
    @InjectView(R.id.des)
    TextView des;
    @InjectView(R.id.appauthor)
    TextView appauthor;
    @InjectView(R.id.arrow)
    ImageView arrow;
    @InjectView(R.id.rlBottom)
    RelativeLayout rlBottom;
    @InjectView(R.id.ll_root)
    LinearLayout llRoot;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_detail_des);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void refreshView() {
        ViewGroup.LayoutParams layoutParams = des.getLayoutParams();
        layoutParams.height = getMinHeight();
        des.setLayoutParams(layoutParams);
        des.setText(data.des);
        appauthor.setText(data.author);

    }


    private boolean isOpen = false;

    @OnClick(R.id.rlBottom)
    public void onClick() {
        if(isOpen) {
            close();
        } else {
            open();
        }

        isOpen = !isOpen;
    }

    private void open() {
        ValueAnimator animator = ValueAnimator.ofInt(getMinHeight(), getMaxHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int temp = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = des.getLayoutParams();
                layoutParams.height = temp;
                des.setLayoutParams(layoutParams);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //将ScrollView滚动到最底部
                ScrollView tempSc = getScrollView(des);
                if (tempSc != null) {
                    tempSc.fullScroll(View.FOCUS_DOWN);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(500);
        animator.start();
    }

    private void close() {
        ValueAnimator animator = ValueAnimator.ofInt(getMaxHeight(), getMinHeight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int temp = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = des.getLayoutParams();
                layoutParams.height = temp;
                des.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    //此时应该获取7行的高度
    private int getMinHeight() {
        //des.setMaxLines(7);
        //模拟一个和des完全一致的TextView
        TextView tv = new TextView(UiUtils.getContext());
        tv.setText(data.des);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tv.getPaint().setFakeBoldText(true);// 设置粗体
        tv.setMaxLines(7);

        int maxWidth = UiUtils.getScreenWidth() - UiUtils.dip2px(10)*4;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.AT_MOST);
        tv.measure(widthMeasureSpec,0);

        return tv.getMeasuredHeight();
    }
    private int getMaxHeight() {
        TextView tv = new TextView(UiUtils.getContext());
        tv.setText(data.des);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tv.getPaint().setFakeBoldText(true);// 设置粗体
        //tv.setMaxLines(7);

        int maxWidth = UiUtils.getScreenWidth() - UiUtils.dip2px(10)*4;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.AT_MOST);
        tv.measure(widthMeasureSpec,0);

        return tv.getMeasuredHeight();
    }
    private ScrollView sc;
    public void setScrollView(ScrollView sc) {
        this.sc = sc;
    }

    //此方法使用的时候需要注意，就是视图树中必须得有一个ScrollView，如果没有的话，会栈溢出
    private ScrollView getScrollView(View v) {
        ViewParent parent = v.getParent();
        if(parent instanceof  ScrollView) {
            return (ScrollView) parent;
        } else {
            return getScrollView((View) parent);
        }
    }
}
