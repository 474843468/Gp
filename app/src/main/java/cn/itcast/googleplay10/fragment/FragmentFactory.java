package cn.itcast.googleplay10.fragment;

import android.content.Intent;

import java.util.HashMap;

import static android.R.attr.fragment;

/**
 * Created by zhengping on 2016/12/1,14:11.
 * 创建Fragment对象，并且对Fragment的对象进行缓存
 */

public class FragmentFactory {

    private static HashMap<Integer, BaseFragment> sSavedFragment = new HashMap<>();


    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = sSavedFragment.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new SubjectFragment();
                    break;
                case 4:
                    fragment = new RecommendFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;
            }

            sSavedFragment.put(position, fragment);
        }


        return fragment;
    }
}
