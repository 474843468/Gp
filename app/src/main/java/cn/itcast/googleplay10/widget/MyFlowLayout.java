package cn.itcast.googleplay10.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/5,11:14.
 *
 * 1、确定哪些元素属于第一行，哪些元素属于第二行。。将所有的行对象存储在lineList里面去
 * 2、根据lineList里面的内容来计算出MyFlowLayout的高度
 * 3、将行对象里面的view对象，进行位置确定，让他们从左到右一字排开
 * 4、细节的修改
 *      onMeasure会被执行多次
 *      别忘了最后一行
 *      每一行的右边需要对齐,需要进行行里面每一个View对象的重新测量
 *      考虑padding的情况
 *      当一行中元素高度不一致的情况之下，这一行的高度应该以最高的元素为主,其他的元素居中显示
 *
 *
 */

public class MyFlowLayout extends ViewGroup {
    public MyFlowLayout(Context context) {
        super(context);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int top = getPaddingTop();
        int left = getPaddingBottom();
        for(int i=0;i<lineList.size();i++) {
            Line line = lineList.get(i);
            line.reMeasure();
            line.layoutViews(left,top);
            top = top + line.lineHeight + mVerticalSpacing;
        }
    }

    //onMeasure会被执行多次
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        reset();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);///获取控件的宽度
        widthSize = widthSize - getPaddingLeft() - getPaddingRight();

        //取出每一个孩子，将孩子的宽度进行累加，与widthSize进行比较
        int childCount = getChildCount();
        mCurrentLine = new Line();
        for(int i=0;i<childCount;i++) {
            View childView = getChildAt(i);

            //getWidth得到的值很可能是0
           // int childWidth = childView.getWidth();
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
           // int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
            childView.measure(childWidthMeasureSpec, 0);//手动测量childView   把测量的工作交给系统来进行，我们不参与任何的限制条件
            int childWidth =  childView.getMeasuredWidth();
            mUsedWidth = mUsedWidth + childWidth;

            if(mUsedWidth < widthSize) {
                //代表有剩余空间
                mCurrentLine.addLineView(childView);
                mUsedWidth = mUsedWidth + mHorizontalSpacing;
            } else {
                //代表剩余空间不足
                newLine();
                mCurrentLine.addLineView(childView);
                mUsedWidth = mUsedWidth + childWidth + mHorizontalSpacing;
            }

        }

        //别忘了最后一行
        if(!lineList.contains(mCurrentLine)) {
            lineList.add(mCurrentLine);
        }
        int totalHeight = 0;
        System.out.println("lineList.size=" + lineList.size());
        for(int i=0;i<lineList.size();i++) {
            Line line = lineList.get(i);
            totalHeight = totalHeight + line.lineHeight + mVerticalSpacing;
        }

        //去除最后一行的行间距
        totalHeight = totalHeight - mVerticalSpacing;

        totalHeight = totalHeight + getPaddingBottom() + getPaddingTop();

        //重新生成MeasureSpec
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //归位操作
    private void reset() {
        mCurrentLine = new Line();
        lineList.clear();
        mUsedWidth = 0;
    }

    private void newLine() {

        lineList.add(mCurrentLine);
        mCurrentLine = new Line();
        mUsedWidth = 0;

    }

    private int mHorizontalSpacing = UiUtils.dip2px(10);
    private int mVerticalSpacing = UiUtils.dip2px(10);

    private int mUsedWidth = 0;
    private Line mCurrentLine;

    private ArrayList<Line> lineList = new ArrayList<>();

    class Line {
        private ArrayList<View> lineViews = new ArrayList<>();

        public int lineHeight;

        public void addLineView(View childView) {
            if(!lineViews.contains(childView)) {
                lineViews.add(childView);
                if(lineHeight < childView.getMeasuredHeight()) {
                    lineHeight = childView.getMeasuredHeight();
                }

            }
        }

        public void layoutViews(int left,int top) {
            for(int i=0;i<lineViews.size();i++) {
                View childView = lineViews.get(i);
                //确定每一个View对象的位置
                int topOffset = lineHeight/2 - childView.getMeasuredHeight()/2;
                childView.layout(left,top+topOffset,left + childView.getMeasuredWidth(),top+topOffset+childView.getMeasuredHeight());
                left = left + childView.getMeasuredWidth() + mHorizontalSpacing;
            }
        }

        public void reMeasure() {
            int lineWidth = 0;
            for(int i=0;i<lineViews.size();i++) {
                View childView = lineViews.get(i);
                lineWidth = lineWidth + childView.getMeasuredWidth() + mHorizontalSpacing;
            }

            lineWidth = lineWidth - mHorizontalSpacing;//每一行的宽度
            int surplusSpacing = getMeasuredWidth()-getPaddingRight()-getPaddingLeft() - lineWidth;
            if(surplusSpacing > 0) {
                int splitSpacing = surplusSpacing/ lineViews.size();
                for(int i=0;i<lineViews.size();i++) {
                    View childView = lineViews.get(i);
                    int childWidth = childView.getMeasuredWidth();
                    int childHeight = childView.getMeasuredHeight();
                    childWidth = childWidth + splitSpacing;
                    int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                    childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
                }
            }
        }
    }
}
