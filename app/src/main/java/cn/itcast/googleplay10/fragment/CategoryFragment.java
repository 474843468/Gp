package cn.itcast.googleplay10.fragment;

import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import cn.itcast.googleplay10.adapter.MyBaseAdapter;
import cn.itcast.googleplay10.bean.CategoryInfo;
import cn.itcast.googleplay10.holder.BaseHolder;
import cn.itcast.googleplay10.holder.CategoryNormalHolder;
import cn.itcast.googleplay10.holder.CategoryTitleHolder;
import cn.itcast.googleplay10.protocol.CategoryProtocol;
import cn.itcast.googleplay10.utils.UiUtils;
import cn.itcast.googleplay10.widget.LoadingPage;

/**
 * Created by zhengping on 2016/12/1,11:27.
 * 分类的Fragment
 */

public class CategoryFragment extends BaseFragment {

    private ArrayList<CategoryInfo> data;

    @Override
    public View fragmentCreateSuccessView() {
        ListView listView = new ListView(UiUtils.getContext());
        listView.setAdapter(new MyAdapter(data));

        return listView;
    }

    @Override
    public LoadingPage.ResultState fragmentLoadData() {

        CategoryProtocol categoryProtocol = new CategoryProtocol();
        data = categoryProtocol.getData();
        return checkData(data);
    }

    /**
     * ListView展示多种布局类型
     * 1、告诉系统你有几种布局类型
     * 2、定义规则：告诉系统，在哪个位置上显示哪种布局类型
     * 3、根据规则，加载不同的布局文件
     */
    class MyAdapter extends MyBaseAdapter<CategoryInfo> {


        //布局类型必须从0开始，依次向上增加
        public static final int TYPE_NORMAL = 0;
        public static final int TYPE_TITLE = 1;


        public MyAdapter(ArrayList<CategoryInfo> dataList) {
            super(dataList);
        }

        @Override
        public int getViewTypeCount() {

            return super.getViewTypeCount()+1;
        }

        @Override
        public int getItemViewType(int position) {
            System.out.println("-----------------------------");
            CategoryInfo categoryInfo = data.get(position);
            if(categoryInfo.isTitle()) {
                return TYPE_TITLE;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public BaseHolder getHolder(int position) {
            //根据规则，加载不同的布局文件
            int type = getItemViewType(position);
            if(type == TYPE_NORMAL) {
                return new CategoryNormalHolder();
            } else {
                return new CategoryTitleHolder();
            }

        }
    }
}
