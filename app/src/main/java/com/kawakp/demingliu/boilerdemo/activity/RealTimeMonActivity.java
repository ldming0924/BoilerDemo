package com.kawakp.demingliu.boilerdemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.bean.Tab;
import com.kawakp.demingliu.boilerdemo.canstant.Canstant;
import com.kawakp.demingliu.boilerdemo.fragment.ContrloFragment;
import com.kawakp.demingliu.boilerdemo.fragment.DataFragment;
import com.kawakp.demingliu.boilerdemo.fragment.ProcessMonitoringFragment;
import com.kawakp.demingliu.boilerdemo.fragment.ParmFragment;
import com.kawakp.demingliu.boilerdemo.service.RealTimeDataService;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RealTimeMonActivity extends BaseActivity {

    @Bind(R.id.framLayout)
    FrameLayout mFrameLayout;
    @Bind(android.R.id.tabcontent)
    FrameLayout mTabcontent;
    @Bind(R.id.fragmentTabHost)
    FragmentTabHost mTabHost;
    private List<Tab> list = new ArrayList<>();
    private RealTimeDataService realTimeDataService;
    @Override
    public int setContentViewId() {
        return R.layout.activity_realtime;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.stastusbar_bg),0);
        initTab();
        initData();
    }

    private void initData() {
        Intent intent = new Intent(RealTimeMonActivity.this, RealTimeDataService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            realTimeDataService = ((RealTimeDataService.MsgBinder) service).getService();

            realTimeDataService.setCallBack(new RealTimeDataService.MyCallBack() {
                @Override
                public void callBackMessage(String message) {
                    //Log.d("TAG","实时数据:"+message);
                    //发送广播
                    if (message != null) {
                        Intent intent = new Intent(Canstant.MAINACTIVITY);
                        intent.putExtra("MESSAGE", message);
                        sendBroadcast(intent);
                    }
                }
            });
        }
    };

    private void initTab() {
        mTabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.framLayout);

        Tab tab_data = new Tab(R.string.data, R.drawable.data_selector, DataFragment.class);
        Tab tab_parm_set = new Tab(R.string.param_set, R.drawable.parm_selector, ParmFragment.class);
        Tab tab_control_set = new Tab(R.string.control_set, R.drawable.control_selector, ContrloFragment.class);
        Tab tab_his_warm = new Tab(R.string.process_monitoring, R.drawable.promom_selector, ProcessMonitoringFragment.class);

        list.add(tab_data);
        list.add(tab_parm_set);
        list.add(tab_control_set);
        list.add(tab_his_warm);

        for (Tab tab : list) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }
        //设置Tab高度和宽度
        final TabWidget tabWidget = mTabHost.getTabWidget();
        for (int i =0; i < tabWidget.getChildCount(); i++) {
            tabWidget.getChildAt(i).getLayoutParams().height = 160;
        }

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE); //去掉分割线
        mTabHost.setCurrentTab(0);
    }

    private View buildIndicator(Tab tab) {
        View view = LayoutInflater.from(RealTimeMonActivity.this).inflate(R.layout.tab_inidicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView textView = (TextView) view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getIcon());
        textView.setText(tab.getTitle());
        return view;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
