package cn.itcast.googleplay10.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.application.MyApplication;
import cn.itcast.googleplay10.fragment.AppFragment;
import cn.itcast.googleplay10.fragment.BaseFragment;
import cn.itcast.googleplay10.fragment.CategoryFragment;
import cn.itcast.googleplay10.fragment.FragmentFactory;
import cn.itcast.googleplay10.fragment.GameFragment;
import cn.itcast.googleplay10.fragment.HomeFragment;
import cn.itcast.googleplay10.fragment.HotFragment;
import cn.itcast.googleplay10.fragment.RecommendFragment;
import cn.itcast.googleplay10.fragment.SubjectFragment;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.PagerTab;

import static cn.itcast.googleplay10.application.MyApplication.instance;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MyApplication.instance.addActivity(this);

        //instance = this;

        //getResources().getString(R.string.app_name);

        //View.inflate(this, R.layout.activity_main, null);
        /*UiUtils.inflateView(R.layout.activity_main);
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();*/

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });*/

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerTab pagerTab = (PagerTab) findViewById(R.id.pagerTab);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        pagerTab.setViewPager(viewPager);//将页签控件和ViewPager绑定起来

        pagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.getFragment(position);
                fragment.loadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MyApplication.instance.removeActivity(this);
    }

    /**
     * 如果ViewPager只是显示简单的View对象，此时使用PagerAdapter
     * 如果ViewPager显示的是Fragment，此时使用FragmentPagerAdapter
     */
    class MyAdapter extends FragmentPagerAdapter {

        private  String[] mTabNames;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = UiUtils.getStringArray(R.array.tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.getFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            //return 7;//魔术数字
            return mTabNames.length;
        }

        //这个方法是给页签控件调用的，告诉页签控件在哪个位置上显示什么样的字符串
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }

    @Override
    public void onClick(View v) {

    }
}
