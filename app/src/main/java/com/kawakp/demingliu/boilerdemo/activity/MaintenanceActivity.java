package com.kawakp.demingliu.boilerdemo.activity;

import android.os.Bundle;
import android.view.View;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import butterknife.OnClick;

/**
 * Created by deming.liu on 2017/1/11.
 */

public class MaintenanceActivity extends BaseActivity {
    @Override
    public int setContentViewId() {
        return R.layout.activity_maintenance;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.stastusbar_bg),0);
    }

    @OnClick({R.id.lin_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
        }
    }
}
