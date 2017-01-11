package com.kawakp.demingliu.boilerdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by deming.liu on 2017/1/11.
 */

public class LoginActivity extends BaseActivity {

    @Override
    public int setContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
    }

    @OnClick(R.id.button_login)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_login:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
                break;
        }
    }



    //点击两次退出
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else { //两次按键小于2秒时，退出应用
                    ActivityManager.getInstance().exit();

                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
