package cn.itcast.googleplay10.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.LoadingPage;

/**
 * Created by zhengping on 2016/12/1,11:27.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TextView tv = new TextView(UiUtils.getContext());
        //tv.setText(this.getClass().getSimpleName());
        loadingPage = new LoadingPage(UiUtils.getContext()){
            @Override
            public ResultState onLoad() {
                return fragmentLoadData();
            }

            @Override
            public View createSuccessView() {
                return fragmentCreateSuccessView();
            }
        };
        return loadingPage;
    }

    //由于BaseFragment是基类，无法确定每一个子类成功之后长什么样子，因此它也无法完成这项工作，需要把它转交出去
    public abstract View fragmentCreateSuccessView() ;


    public abstract LoadingPage.ResultState fragmentLoadData();

    public void loadData() {
        if (loadingPage != null) {
            loadingPage.loadData();
        }
    }


    public LoadingPage.ResultState checkData(Object object) {
        if (object == null) {
            return LoadingPage.ResultState.ERROR;
        } else {
            if (object instanceof List) {
                List list = (List) object;
                if(list.size() == 0) {
                    return LoadingPage.ResultState.EMPTY;
                } else {
                    return LoadingPage.ResultState.SUCCESS;
                }
            } else {
                return LoadingPage.ResultState.ERROR;
            }
        }


    }
}
