package cn.itcast.googleplay10.fragment;

import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import cn.itcast.googleplay10.adapter.MyBaseAdapter;
import cn.itcast.googleplay10.bean.SubjectInfo;
import cn.itcast.googleplay10.holder.BaseHolder;
import cn.itcast.googleplay10.holder.SubjectHolder;
import cn.itcast.googleplay10.protocol.SubjectProtocol;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.LoadingPage;

/**
 * Created by zhengping on 2016/12/1,11:27.
 * 专题的Fragment
 */

public class SubjectFragment extends BaseFragment {

    private ArrayList<SubjectInfo> data;

    @Override
    public View fragmentCreateSuccessView() {
        PullToRefreshListView listView = new PullToRefreshListView(UiUtils.getContext());
        listView.setAdapter(new MyAdapter(data));
        return listView;
    }

    @Override
    public LoadingPage.ResultState fragmentLoadData() {


        SubjectProtocol subjectProtocol = new SubjectProtocol();
        data = subjectProtocol.getData();
        return checkData(data);
    }

    class MyAdapter extends MyBaseAdapter<SubjectInfo> {

        public MyAdapter(ArrayList<SubjectInfo> dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder getHolder(int position) {
            return new SubjectHolder();
        }
    }
}
