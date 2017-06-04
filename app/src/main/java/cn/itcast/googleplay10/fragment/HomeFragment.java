package cn.itcast.googleplay10.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.activity.DetailActivity;
import cn.itcast.googleplay10.adapter.MyBaseAdapter;
import cn.itcast.googleplay10.application.MyApplication;
import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.holder.BaseHolder;
import cn.itcast.googleplay10.holder.HomeHeaderHolder;
import cn.itcast.googleplay10.holder.HomeHolder;
import cn.itcast.googleplay10.protocol.HomeProtocol;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.LoadingPage;

/**
 * Created by zhengping on 2016/12/1,11:27.
 * 首页的Fragment
 */

public class HomeFragment extends BaseFragment {

    private MyAdapter myAdapter;
    private PullToRefreshListView listView;
    private ArrayList<AppInfo> data;
    private ArrayList<String> picList;


    @Override

    public View fragmentCreateSuccessView() {

        //ListView refreshableView = listView.getRefreshableView();
        
       /* TextView tv = new TextView(UiUtils.getContext());
        tv.setText("首页成功布局创建了");*/
        listView = new PullToRefreshListView(UiUtils.getContext());

        //一般来说，如果ＬｉｓｔＶｉｅｗ的ｉｔｅｍ有网络图片进行加载的话，可以对这部分工作进行优化，这样ＬｉｓｔＶｉｅｗ滑动起来就不会卡顿了
        //阻止ＬｉｓｔＶｉｅｗ在触摸或者滑翔的时候加载本地图片
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(),true,true));
        //设置上拉加载和下拉刷新事件的监听
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                System.out.println("onRefresh...");
                /*if(listView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    //加载下一页的数据
                    requestData(true);
                } else {
                    //下拉刷新
                    requestData(false);
                }*/
                requestData(listView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END);
            }
        });
        //设置下拉刷新控件的模式，both指的是既有下拉刷新又有上拉加载
        listView.setMode(PullToRefreshBase.Mode.BOTH);

        myAdapter = new MyAdapter(data);
        listView.setAdapter(myAdapter);


        //给ListView增加头布局
        ListView refreshableView = listView.getRefreshableView();

        /*View headerView = UiUtils.inflateView(R.layout.xxx);
        ViewPager viewPager = (ViewPager) headerView.findViewById(R.id.viewPager);
        viewPager.setAdapter();
        viewPager.setOnPageChangeListener();*/
        HomeHeaderHolder headerHolder = new HomeHeaderHolder();
        headerHolder.setData(picList);
        refreshableView.addHeaderView(headerHolder.convertView);
        myAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //由于设置了头布局，所以数据集合中的position和ListView 的position不一致
                position = position - listView.getRefreshableView().getHeaderViewsCount();
                AppInfo appInfo = data.get(position);
                //安卓中数据传递的几种方式
                //1 Intent:String、其他的一些基本数据类型 、可以传递对象吗？(Parcelable,Serializable)
                //2 构造方法
                //3 对象调用方法
                //4 view.setTag
                //5 Application
                MyApplication.instance.appInfo = appInfo;
               // intent.putExtra("name", appInfo);
                intent.setClass(getActivity(), DetailActivity.class);
                getActivity().startActivity(intent);
                //getActivity().overridePendingTransition(R.anim.from_right_in,R.anim.to_left_out);
            }
        });
        return listView;
    }

    private void requestData(final boolean loadMore) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                HomeProtocol protocol = new HomeProtocol();

                if(loadMore) {
                    protocol.setIndex(myAdapter.getCount());
                }
                ArrayList<AppInfo> newData = protocol.getData();
                if(!loadMore) {
                    data.clear();
                } else {
                    if(newData.size()==0){
                        UiUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UiUtils.toast("目前已加载完所有的数据...");
                            }
                        });
                    }
                }

                data.addAll(newData);
                // dataList.clear();
               // for(int i=0;i<20;i++) {
                   // dataList.add("刷新数据:" + i);
               // }

                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                });



            }
        }).start();
    }

    /**
     * 下一页的数据怎么去加载：
     * more：
     * pageNo、pageSize、totalCount
     * index
     *
     *
     *index控制需要加载的页数：
     * 	第一页的数据：index=0,此时数据集合中的数量为０
     *  第二页的数据：index=20，此时数据集合中的数量为２0
     *  第三页的数据：index=40
     *  第四页的数据：index=60
     *
     *  把当前数据集合中的条目当作ｉｎｄｅｘ的值进行传递就能获得下一页数据
     *
     */
    private void loadMore() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                HomeProtocol protocol = new HomeProtocol();
                protocol.setIndex(myAdapter.getCount());
                ArrayList<AppInfo> moreData = protocol.getData();
                if(moreData.size() == 0) {
                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UiUtils.toast("目前已加载完所有的数据...");
                        }
                    });
                }
                data.addAll(moreData);
               // for(int i=0;i<20;i++) {
                   // dataList.add("更多数据:" + i);
               // }

                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                });



            }
        }).start();
    }

    @Override
    public LoadingPage.ResultState fragmentLoadData() {

        HomeProtocol protocol = new HomeProtocol();
        data = protocol.getData();
        picList = protocol.getPicList();
       /* if(data == null) {
            return LoadingPage.ResultState.ERROR;
        } else {
            if(data.size() == 0) {
                return LoadingPage.ResultState.EMPTY;
            } else {
                return LoadingPage.ResultState.SUCCESS;
            }
        }
*/
        return checkData(data);
       // return LoadingPage.ResultState.SUCCESS;
    }

    class MyAdapter extends MyBaseAdapter<AppInfo> {

        public MyAdapter(ArrayList<AppInfo> dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder getHolder(int position) {
            return new HomeHolder();
        }

        /**
         *分析一下getView做了哪些核心的工作
         * 1、加载布局文件  convertView，布局文件的id
         * 2、初始化控件  TextView、convertView、Holder
         * 3、存储holder  Holder、convertView
         * 4、刷新控件的数据 TextView、数据
         */
       /* @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = UiUtils.inflateView(R.layout.list_item_home);//1、加载布局文件
                holder = new ViewHolder();
                holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);//2、初始化控件
                convertView.setTag(holder);//3、存储holder
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvContent.setText(dataList.get(position));//4、刷新控件的数据

            return convertView;
        }*/
    }

    static class ViewHolder {
        TextView tvContent;
    }
}
