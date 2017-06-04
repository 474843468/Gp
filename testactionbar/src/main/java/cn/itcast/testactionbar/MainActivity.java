package cn.itcast.testactionbar;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 继承AppCompatActivity的目的，为了兼容ActionBar效果
 * TitleBar-->ActionBar-->ToolBar
 *
 * ActionBar比titleBar增加了哪些功能？
 * ToolBar：它可以融入到我们自己的布局文件，会更加好配置
 *
 * V7包的目的：为了达到ActionBar效果的兼容
 *
 *
 * 兼容性：
 * V4：Fragment
 * V7：ActionBar效果的兼容
 * 属性动画的兼容：nineoldandroid.jar
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("传智播客");
        actionBar.setDisplayHomeAsUpEnabled(true);//是否显示返回箭头
        //actionBar.setDisplayShowHomeEnabled(true);//是否显示图标

        //开关
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        toggle.syncState();;//同步状态
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if(menuId == R.id.menu1) {
            Toast.makeText(this, "设置1被点击了", Toast.LENGTH_SHORT).show();
        } else if(menuId == android.R.id.home) {
           // finish();
            toggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
