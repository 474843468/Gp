package cn.itcast.googleplay10.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import cn.itcast.googleplay10.R;
import cn.itcast.googleplay10.manager.MyThreadPoolManager;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/1,14:28.
 *
 *
 * LoadingPage作为每一个Fragment所需要展示的View对象存在
 * 这个View对象包含了几种状态
 * 1、加载中的状态，STATE_LOADING，mLoadingView
 * 2、加载失败的状态，STATE_ERROR，mErrorView
 * 3、加载成功，但是数据集合的条目数量为0，STATE_EMPTY，mEmptyView
 * 4、加载成功，STATE_SUCCESS，mSuccessView
 *
 */

public abstract class LoadingPage extends FrameLayout {

    public static final int STATE_NONE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;

    private  int mCurrentState = STATE_NONE;

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mLoadingView == null) {
            mLoadingView = createLoadingView();
            addView(mLoadingView);
        }

        if(mErrorView == null) {
            mErrorView = createErrorView();
            addView(mErrorView);
        }

        if (mEmptyView == null) {
            mEmptyView = createEmptyView();
            addView(mEmptyView);
        }
        showRightPage();
    }

    //根据当前的状态动态的控制哪个VIew显示，哪个VIew不显示
    private void showRightPage() {
        /*if(mCurrentState == STATE_LOADING) {
            mLoadingView.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
        } else if(mCurrentState == STATE_ERROR) {
            mLoadingView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else if(mCurrentState == STATE_EMPTY) {
            mLoadingView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }*/
        //使用三目表达式来替代上诉代码
        mLoadingView.setVisibility((mCurrentState == STATE_LOADING)?View.VISIBLE:View.GONE);
        mErrorView.setVisibility((mCurrentState == STATE_ERROR)?View.VISIBLE:View.GONE);
        mEmptyView.setVisibility((mCurrentState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        if(mCurrentState == STATE_SUCCESS) {
            if (mSuccessView == null) {
                mSuccessView = createSuccessView();
                if (mSuccessView != null) {
                    addView(mSuccessView);
                }
            }
        }
        if(mSuccessView != null) {
            mSuccessView.setVisibility((mCurrentState == STATE_SUCCESS)?View.VISIBLE:View.GONE);
        }

    }

    //加载网络数据
    public void loadData() {


        if(mCurrentState == STATE_LOADING) {
            return;
        }
        mCurrentState = STATE_LOADING;
        //当mCurrentState的值发生改变的时候，应该更新UI
        showRightPage();

        MyThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                //加载网络数据

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ResultState resultState = onLoad();
                if(resultState != null) {
                    mCurrentState = resultState.state;
                }

                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showRightPage();
                    }
                });

            }
        });

        //new Thread().start();
    }



    //LoadingPage无法确定每一个Fragment去加载网络数据的url，所以LoadingPage无法完成这项工作，需要把它转交出去
    //需要把此方法抽象
    public   abstract ResultState onLoad();

    //此时LoadingPage作为每一个Fragment显示的View对象，它无法确定每一个Fragmnet成功之后长什么样子
    //LoadingPage完成不了这项工作，所以应该把这项工作转交出去，通过抽象方法的方式
    public   abstract  View createSuccessView();

    private View createEmptyView() {
        View view = UiUtils.inflateView(R.layout.layout_empty);
        return view;
    }

    private View createErrorView() {
        View view = UiUtils.inflateView(R.layout.layout_error);
        return view;
    }

    private View createLoadingView() {
        View view = UiUtils.inflateView(R.layout.layout_loading);
        return view;
    }


    /**
     * 1、关键字不同
     * 2、创建对象的写法简化了
     * 3、构造方法必须私有化，通过构造方法的私有化，达到对象数量控制的效果
     */
    public enum ResultState {
        LOADING(STATE_LOADING),ERROR(STATE_ERROR),EMPTY(STATE_EMPTY),SUCCESS(STATE_SUCCESS);

        public int state;

        private ResultState(int state) {
            this.state = state;
        }

    }
}
