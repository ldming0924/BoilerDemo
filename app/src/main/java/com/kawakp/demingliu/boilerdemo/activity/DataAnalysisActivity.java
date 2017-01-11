package com.kawakp.demingliu.boilerdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by deming.liu on 2017/1/11.
 * 数据分析
 */

public class DataAnalysisActivity extends BaseActivity {
    @Bind(R.id.lin_back)
    LinearLayout lin_back;
    @Override
    public int setContentViewId() {
        return R.layout.activity_data_analysis;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
    }

    @OnClick({R.id.lin_back})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.lin_back:
                finish();
                break;

        }
    }
}
