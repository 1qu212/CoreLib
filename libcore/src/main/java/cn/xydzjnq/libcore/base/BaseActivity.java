package cn.xydzjnq.libcore.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.xydzjnq.libcore.util.ActivityStackManager;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.setInstanceRef(this);
        ActivityStackManager.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //也要在onresume函数里面进行设置，保证弱引用一直引用当前的可见页面
        BaseApplication.setInstanceRef(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getInstance().removeActivity(this);
    }
}
