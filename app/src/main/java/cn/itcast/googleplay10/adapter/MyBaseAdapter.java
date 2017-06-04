package cn.itcast.googleplay10.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cn.itcast.googleplay10.holder.BaseHolder;
import cn.itcast.googleplay10.holder.HomeHolder;
import cn.itcast.googleplay10.utils.ItemAnimationUtils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by zhengping on 2016/12/2,9:05.
 *
 * 封装的思想：
 *  抽取基类：
 *      在基类中做的一般都是通用的事情
 *      不通用的事情
 *          1、父类不知道如何完成的事情，通过抽象方法来交给子类实现
 *          2、父类不知道的数据类型，通过泛型来解决，泛型是自定义的一种不存在的类型，这种类型究竟是啥，在定义子类的时候确定
 *              尖括号的位置
 *                  如果在自己的类名后面，代表定义了一个泛型
 *                  如果跟在父类的名称后面，代表确定父类所定义的泛型类型
 *
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    private ArrayList<T> dataList;

    public MyBaseAdapter(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        if(convertView == null) {
            holder = getHolder(position);
        } else {
            holder = (BaseHolder) convertView.getTag();
        }

        holder.setData(dataList.get(position));

        ItemAnimationUtils.startAnim(holder.convertView);

        return holder.convertView;
    }

    public abstract BaseHolder getHolder(int position);
}
