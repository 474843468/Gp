package cn.itcast.googleplay10.holder;

import android.view.View;
import android.widget.TextView;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.bean.CategoryInfo;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/5,10:04.
 */

public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {

    private TextView tv;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_category_title);
        tv = (TextView) view.findViewById(R.id.tv);
        return view;
    }

    @Override
    public void refreshView() {
        tv.setText(data.getTitle());
    }
}
