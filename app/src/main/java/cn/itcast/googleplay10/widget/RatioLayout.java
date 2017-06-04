package cn.itcast.googleplay10.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import cn.itcast.googleplay10.R;

import static cn.itcast.googleplay10.R.attr.ratio;

/**
 * Created by zhengping on 2016/12/4,14:27.
 * 按比例动态计算高度的帧布局
 * 如何告诉ＲａｔｉｏＬａｙｏｕｔ按照哪一个比例进行动态计算
 * 自定义属性：
 * １＼给自定义属性起名字
 *      自定义属性集合的名称＋自定义属性的名称　　　数据类型的指定
 * ２＼使用这个自定义属性
 *      命名空间
 *  ３＼获取自定义属性的值
 *      方式１
 *      方式２
 */

public class RatioLayout extends FrameLayout {
    private static final String NS = "http://schemas.android.com/apk/res-auto";
    private float ratio2;

    //private static final float ratio = 2.43f;

    public RatioLayout(Context context) {
        this(context,null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(attrs != null) {
            //方式一，在所有的属性集合中查找自定义属性的值
            //float ratio = attrs.getAttributeFloatValue(NS, "ratio", 0);
            //System.out.println("ratio=" + ratio);
            //方式二：在所有的属性集合中，查找自定义属性集合
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayoutAttrs);
            //自定义属性集合名称_自定义属性的名称：代表的是这个自定义属性位于自定义属性集合中的位置索引
            ratio2 = typedArray.getFloat(R.styleable.RatioLayoutAttrs_ratio, 0);
            System.out.println("ratio2="+ ratio2);
            typedArray.recycle();;//节约内存
        }


    }

    //measure-layout-draw
    //widthMeasureSpec:宽度的信息，宽度的大小＋宽度的模式
    //heightMeasureSpec:高度的信息，高度的大小＋高度的模式
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int innerWidthSize = widthSize - getPaddingLeft() - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //MeasureSpec.AT_MOST; 至多的模式，ｗｒａｐ＿ｃｏｎｔｅｎｔ
        //MeasureSpec.EXACTLY;确定的模式，ｍａｔｃｈ＿ｐａｒｅｎｔ或者写死ｄｐ
        //MeasureSpec.UNSPECIFIED，未确定的模式，ＬｉｓｔＶｉｅｗ

        if(ratio2 != 0 && widthMode == MeasureSpec.EXACTLY && heightMode!=MeasureSpec.EXACTLY) {
            //动态计算高度
            int innerHeightSize = (int) (innerWidthSize/ratio2 +0.5f);
            heightSize = innerHeightSize + getPaddingTop() + getPaddingBottom();
            //真正确定自己控件的大小
            //setMeasuredDimension(widthSize,heightSize);
            //重新生成ｈｅｉｇｈｔＭｅａｓｕｒｅＳｐｅｃ
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }


        //不能将此方法给注释起来，此方法一旦被注释，就不会测量孩子的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
