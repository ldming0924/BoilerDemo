package com.kawakp.demingliu.boilerdemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.bean.DataAnalysisBean;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SpotsCallBack;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.MyBarCharts;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.List;


import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2017/1/11.
 * 数据分析
 */

public class DataAnalysisActivity extends BaseActivity {
    @Bind(R.id.lin_back)
    LinearLayout lin_back;
    @Bind(R.id.barChart_power)
    BarChart powerBarChart;  //实时功率
    @Bind(R.id.average_load_barChart)
    BarChart averageLoad;//平均负荷
    @Bind(R.id.average_efficiency_barChart)
    BarChart averageEfficiency;//平均效率
    @Bind(R.id.energy_saving_barChart)
    BarChart energy_saving;//节能量
    @Bind(R.id.dust_reduction_barChart)
    BarChart dust_reduction;//尘减排量
    @Bind(R.id.carbon_reduction_barChart)
    BarChart carbon_reduction;//碳减排量
    @Bind(R.id.nitrogen_reduction_barChart)
    BarChart nitrogen_reduction;//氮减排量
    @Bind(R.id.sulfur_reduction_barChart)
    BarChart sulfur_reduction;//硫减排量

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
        String deviceId = SharedPerferenceHelper.getDeviceId(DataAnalysisActivity.this);
        long endTime = System.currentTimeMillis();
        long startTime = endTime -6*24*60*60*1000;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(startTime);
        Log.d("DataAnalysisActivity",endTime +"　　"+startTime+"　"+dateString);

        String url = PathUtils.DATA_STATISTICS+deviceId+"/elementTables/945aa97257b74f0481f187fdf453f37d/stats?statFields="
                +"PJ_FH,J_NL,C_JPL,T_JPL,D_JPL,L_JPL&fromDate="+startTime+"&toDate="+endTime+"&statDateInterval=1d";
        Log.d("DataAnalysisActivity",url);
        OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(DataAnalysisActivity.this);
       okHttpHelper.get(url, new SpotsCallBack<String>(DataAnalysisActivity.this) {

           @Override
           public void onSuccess(Response response, String s) {
               Log.d("DataAnalysisActivity","统计数据:  "+s);
               JSONObject object = JSON.parseObject(s);
               JSONArray array = object.getJSONArray("list");
               List<DataAnalysisBean> list = JSON.parseArray(array.toString(),DataAnalysisBean.class);
               mBarData = mBarCharts.getBarData(list, 1);
               mBarCharts.showBarChart(averageLoad, mBarData);
               mBarData = mBarCharts.getBarData(list, 3);
               mBarCharts.showBarChart(energy_saving, mBarData);
               mBarData = mBarCharts.getBarData(list, 4);
               mBarCharts.showBarChart(dust_reduction, mBarData);
               mBarData = mBarCharts.getBarData(list, 5);
               mBarCharts.showBarChart(carbon_reduction, mBarData);
               mBarData = mBarCharts.getBarData(list, 6);
               mBarCharts.showBarChart(nitrogen_reduction, mBarData);
               mBarData = mBarCharts.getBarData(list, 7);
               mBarCharts.showBarChart(sulfur_reduction, mBarData);
           }

           @Override
           public void onError(Response response, int code, Exception e) {
            Log.d("DataAnalysisActivity",code+"  ");
           }
       });




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
