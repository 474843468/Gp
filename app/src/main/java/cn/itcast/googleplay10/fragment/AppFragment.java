package cn.itcast.googleplay10.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import cn.itcast.googleplay10.adapter.MyBaseAdapter;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.holder.AppHolder;
import cn.itcast.googleplay10.holder.BaseHolder;
import cn.itcast.googleplay10.protocol.AppProtocol;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.LoadingPage;

/**
 * Created by zhengping on 2016/12/1,11:27.
 * 应用的Fragment
 */

public class AppFragment extends BaseFragment {


    private ArrayList<AppInfo> data;

    @Override
    public View fragmentCreateSuccessView() {

        PullToRefreshListView listView = new PullToRefreshListView(UiUtils.getContext());
        listView.setAdapter(new MyAdater(data));
        return listView;
    }

    @Override
    public LoadingPage.ResultState fragmentLoadData() {

        AppProtocol appProtocol = new AppProtocol();
        data = appProtocol.getData();
        return checkData(data);

    }

    class MyAdater extends MyBaseAdapter<AppInfo> {

        public MyAdater(ArrayList<AppInfo> dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder getHolder(int position) {
            return new AppHolder();
        }
    }

}
