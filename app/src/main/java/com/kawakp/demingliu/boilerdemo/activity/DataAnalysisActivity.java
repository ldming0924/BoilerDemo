package com.kawakp.demingliu.boilerdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.MyBarCharts;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by deming.liu on 2017/1/11.
 * 数据分析
 */

public class DataAnalysisActivity extends BaseActivity {
    @Bind(R.id.lin_back)
    LinearLayout lin_back;
    @Bind(R.id.barChart_power)
    BarChart powerBarChart;
    @Bind(R.id.average_load_barChart)
    BarChart averageLoad;
    @Bind(R.id.average_efficiency_barChart)
    BarChart averageEfficiency;

    private MyBarCharts mBarCharts;
    private BarData mBarData;
    @Override
    public int setContentViewId() {
        return R.layout.activity_data_analysis;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.stastusbar_bg),0);
        init();
    }

    private void init() {
        mBarCharts = new MyBarCharts();
        mBarData = mBarCharts.getBarData();
        mBarCharts.showBarChart(powerBarChart, mBarData);

        mBarCharts.showBarChart(averageLoad,mBarData);

        mBarCharts.showBarChart(averageEfficiency,mBarData);


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
