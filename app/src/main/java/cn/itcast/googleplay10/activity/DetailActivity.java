package cn.itcast.googleplay10.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.application.MyApplication;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.holder.DetailActionHolder;
import cn.itcast.googleplay10.holder.DetailDesHolder;
import cn.itcast.googleplay10.holder.DetailSafeHolder;
import cn.itcast.googleplay10.holder.DetailScreenHolder;
import cn.itcast.googleplay10.holder.DetailSimpleHolder;
import cn.itcast.googleplay10.holder.HomeHolder;
import cn.itcast.googleplay10.protocol.DetailProtocol;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.LoadingPage;

import static cn.itcast.googleplay10.R.id.des;
import static cn.itcast.googleplay10.R.id.flDes;

/**
 * Created by zhengping on 2016/12/7,9:09.
 */

public class DetailActivity extends BaseActivity {

    private AppInfo appInfo;
    private AppInfo data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);
        LoadingPage loadingPage = new LoadingPage(UiUtils.getContext()) {
            @Override
            public ResultState onLoad() {
                return activityLoadData();
            }

            @Override
            public View createSuccessView() {
                return activityCreateSuccessView();
            }
        };
        loadingPage.loadData();
        setContentView(loadingPage);

        appInfo = MyApplication.instance.appInfo;
        MyApplication.instance.appInfo = null;
       // TextView tv = (TextView) findViewById(R.id.tv);
      //  String name = getIntent().getStringExtra("name");
       // tv.setText(appInfo.name);
    }

   /* @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in,R.anim.to_right_out);
    }*/

    private LoadingPage.ResultState activityLoadData() {
        DetailProtocol protocol = new DetailProtocol();
        protocol.setPackageName(appInfo.packageName);
        data = protocol.getData();
        if (data == null) {
            return LoadingPage.ResultState.ERROR;
        } else {
            return LoadingPage.ResultState.SUCCESS;
        }
       // return LoadingPage.ResultState.SUCCESS;
    }

    private View activityCreateSuccessView() {
       /* TextView tv = new TextView(UiUtils.getContext());
        tv.setText(data.name + "加载数据成功");
        return tv;*/
        View view = UiUtils.inflateView(R.layout.layout_detail_success);
        //基本信息区域
        FrameLayout flSimple = (FrameLayout) view.findViewById(R.id.flSimple);
       // HomeHolder homeHolder = new HomeHolder();
        //homeHolder.setData(data);
        //flSimple.addView(homeHolder.convertView);
        DetailSimpleHolder simpleHolder = new DetailSimpleHolder();
        simpleHolder.setData(data);
        flSimple.addView(simpleHolder.convertView);
        //审核区域
        FrameLayout flSafe = (FrameLayout) view.findViewById(R.id.flSafe);
        DetailSafeHolder safeHolder = new DetailSafeHolder();
        safeHolder.setData(data);
        flSafe.addView(safeHolder.convertView);
        //截图区域
        FrameLayout flScreen = (FrameLayout) view.findViewById(R.id.flScreen);
        HorizontalScrollView hsc = new HorizontalScrollView(UiUtils.getContext());
        DetailScreenHolder screenHolder = new DetailScreenHolder();
        screenHolder.setData(data);
        hsc.addView(screenHolder.convertView);
        flScreen.addView(hsc);

        //描述区域
        ScrollView sc = (ScrollView) view.findViewById(R.id.sc);
        FrameLayout flDes = (FrameLayout) view.findViewById(R.id.flDes);
        DetailDesHolder desHolder = new DetailDesHolder();
       // desHolder.setScrollView(sc);
        desHolder.setData(data);
        flDes.addView(desHolder.convertView);
        //操作区域
        FrameLayout flBottom = (FrameLayout) view.findViewById(R.id.flBottom);
        DetailActionHolder actionHolder = new DetailActionHolder();
        actionHolder.setData(data);
        flBottom.addView(actionHolder.convertView);
        return view;
    }
}
