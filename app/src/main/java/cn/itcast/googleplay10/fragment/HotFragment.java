package cn.itcast.googleplay10.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.itcast.googleplay10.protocol.HotProtocol;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.FlowLayout;
import cn.itcast.googleplay10.widget.LoadingPage;
import cn.itcast.googleplay10.widget.MyFlowLayout;

/**
 * Created by zhengping on 2016/12/1,11:27.
 * 排行的Fragment
 */

public class HotFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View fragmentCreateSuccessView() {
        ScrollView sc = new ScrollView(UiUtils.getContext());
        MyFlowLayout flowLayout = new MyFlowLayout(UiUtils.getContext());

        int padding = UiUtils.dip2px(5);
        sc.setPadding(padding,padding,padding,padding);
        flowLayout.setPadding(padding,padding,padding,padding);
        for(int i=0;i<data.size();i++) {
            String keyword = data.get(i);
            TextView tv = new TextView(UiUtils.getContext());
            //tv.setBackgroundColor(UiUtils.getRandomColor());
            GradientDrawable normalDrawable = UiUtils.getShape(UiUtils.dip2px(5), UiUtils.getRandomColor());

            GradientDrawable pressedDrawable = UiUtils.getShape(UiUtils.dip2px(5), Color.rgb(200, 200, 200));
            StateListDrawable selector = UiUtils.getSelector(pressedDrawable, normalDrawable);

            tv.setBackgroundDrawable(selector);
            tv.setPadding(padding, padding, padding, padding);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            if(i==10) {
                tv.setText(keyword + "\n" + keyword);
            } else {
                tv.setText(keyword);
            }

            tv.setOnClickListener(onClickListener);
            tv.setTag(keyword);
            flowLayout.addView(tv);
        }

        sc.addView(flowLayout);

        return sc;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //String msg = (String) v.getTag();
            TextView tv = (TextView) v;
            String msg = tv.getText().toString();
            UiUtils.toast(msg);
        }
    };

    @Override
    public LoadingPage.ResultState fragmentLoadData() {
        HotProtocol protocol = new HotProtocol();
        data = protocol.getData();
        return checkData(data);
    }
}
