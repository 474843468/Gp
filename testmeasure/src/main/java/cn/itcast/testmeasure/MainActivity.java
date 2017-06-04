package cn.itcast.testmeasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        int height = tv.getHeight();
        System.out.println("height=" + height);

        //1、监听视图树  measure-layout-draw
        /*tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height1 = tv.getHeight();
                System.out.println("height1=" + height1);

                tv.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });*/
        //2、手动测量  AT_MOST  EXACTLY  UNSPECIFIED
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.AT_MOST);
        tv.measure(widthMeasureSpec, 0);
        int measuredHeight = tv.getMeasuredHeight();
        System.out.println("measuredHeight=" + measuredHeight);

    }
}
