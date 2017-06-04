package cn.itcast.googleplay10.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.itcast.googleplay10.protocol.RecommendProtocol;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.LoadingPage;
import cn.itcast.googleplay10.widget.random.ShakeListener;
import cn.itcast.googleplay10.widget.random.StellarMap;

import static android.R.attr.data;

/**
 * Created by zhengping on 2016/12/1,11:27.
 * 推荐的Fragment
 */

public class RecommendFragment extends BaseFragment implements ShakeListener.OnShakeListener {

    private ArrayList<String> data;
    private StellarMap stellarMapView;
    private MyAdapter myAdapter;

    @Override
    public View fragmentCreateSuccessView() {

        stellarMapView = new StellarMap(UiUtils.getContext());
        int padding = UiUtils.dip2px(10);
        stellarMapView.setInnerPadding(padding,padding,padding,padding);
        stellarMapView.setRegularity(9,9);
        myAdapter = new MyAdapter();
        stellarMapView.setAdapter(myAdapter);

        stellarMapView.setGroup(0, true);//默认自动切到第０组

        ShakeListener listener = new ShakeListener(UiUtils.getContext());
        listener.setOnShakeListener(this);

        return stellarMapView;
    }
    @Override
    public LoadingPage.ResultState fragmentLoadData() {

        RecommendProtocol recommendProtocol = new RecommendProtocol();
        data = recommendProtocol.getData();
        return checkData(data);

    }


    private int[] colors = new int[]{Color.RED,Color.GREEN};

    @Override
    public void onShake() {
        //当摇一摇动作发生的时候，会回调到此方法中
        //让ＳｔｅｌｌａｒＭａｐ滑动到下一页
        int currentGroup = stellarMapView.getCurrentGroup();
        int nextGroup = currentGroup + 1;
        if(nextGroup > myAdapter.getGroupCount()-1) {
            nextGroup = 0;
        }
        stellarMapView.setGroup(nextGroup, true);
    }

    class MyAdapter implements  StellarMap.Adapter {

        //返回一共有几组
        @Override
        public int getGroupCount() {
            return 3;//魔术数字，一共有３组
        }

        //某一组中，有几个元素
        @Override
        public int getCount(int group) {
            int singleCount = data.size()/getGroupCount();
            //需要考虑除不尽的情况
            if(group == getGroupCount()-1) {
                //最后一组
                singleCount = singleCount + data.size()%getGroupCount();
            }
            return singleCount;
        }

        //group:当前这组
        //position：当前组的某一个元素
        // 2 2  -->
        // 11 11 11
        // 11+11+2
        @Override
        public View getView(int group, int position, View convertView) {
            if(convertView == null) {
                convertView = new TextView(UiUtils.getContext());
            }
            TextView tv = (TextView) convertView;
            int realPosition = 0;
            for(int i=0;i<group;i++) {
                realPosition = realPosition + getCount(i);
            }

            realPosition = realPosition + position;

            final int tempPosition = realPosition;

            tv.setText(data.get(realPosition));

            //Color.rgb()

            tv.setTextColor(UiUtils.getRandomColor());
            //设置文字大小的单位为ｓｐ
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,UiUtils.getRandomTextSize());
            tv.setTag(data.get(realPosition));
            tv.setOnClickListener(onClickListener);

            return convertView;
        }

        //group当前这一组
        //此方法的含义是要获取下一组的索引
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            int nextGroup = group;
            if(isZoomIn) {
                //代表获取下一组
                nextGroup = group + 1;
                if(nextGroup > getGroupCount() - 1) {
                    nextGroup = 0;
                }
            } else {
                //代表获取上一组
                nextGroup = group-1;
                if(nextGroup < 0) {
                    nextGroup = getGroupCount() - 1;
                }
            }
            return nextGroup;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = (String) v.getTag();
            UiUtils.toast(msg);

        }
    };
}
