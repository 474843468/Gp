package cn.itcast.googleplay10.holder;

import android.view.View;
import android.widget.TextView;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.utils.UiUtils;


/**
 * Created by zhengping on 2016/12/2,9:25.
 *  1、加载布局文件  convertView，布局文件的id
 *  2、初始化控件  TextView、convertView、Holder
 * 3、存储holder  Holder、convertView
 * 4、刷新控件的数据 TextView、数据
 */

public abstract class BaseHolder<T> {

    public View convertView;

    public T data;

    public BaseHolder() {
        convertView = initView();
        convertView.setTag(this);
    }



    public void setData(T data) {
        if(data != null) {
            this.data = data;
        }
        refreshView();

    }
    public abstract View initView() ;
    public abstract void refreshView();


}
