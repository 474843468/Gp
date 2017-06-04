package cn.itcast.googleplay10.fragment;

import android.view.View;

import cn.itcast.googleplay10.widget.LoadingPage;

/**
 * Created by zhengping on 2016/12/1,11:27.
 * 游戏的Fragment
 */

public class GameFragment extends BaseFragment {
    @Override
    public View fragmentCreateSuccessView() {
        return null;
    }

    @Override
    public LoadingPage.ResultState fragmentLoadData() {
        return LoadingPage.ResultState.ERROR;
    }
}
