package cn.itcast.googleplay10.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.itcast.googleplay10.R;

public class BaseActivity extends AppCompatActivity {


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.from_right_in,R.anim.to_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in,R.anim.to_right_out);
    }
}
