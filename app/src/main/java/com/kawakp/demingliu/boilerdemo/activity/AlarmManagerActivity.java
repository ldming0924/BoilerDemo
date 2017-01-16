package com.kawakp.demingliu.boilerdemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.bean.Tab;
import com.kawakp.demingliu.boilerdemo.canstant.Canstant;
import com.kawakp.demingliu.boilerdemo.fragment.AlarmHisFragment;
import com.kawakp.demingliu.boilerdemo.fragment.AlarmStateFragment;
import com.kawakp.demingliu.boilerdemo.fragment.ContrloFragment;
import com.kawakp.demingliu.boilerdemo.fragment.DataFragment;
import com.kawakp.demingliu.boilerdemo.fragment.ParmFragment;
import com.kawakp.demingliu.boilerdemo.fragment.ProcessMonitoringFragment;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by deming.liu on 2017/1/11.
 */

public class AlarmManagerActivity extends BaseActivity {
    @Bind(R.id.framLayout)
    FrameLayout mFrameLayout;
    @Bind(R.id.lin_state)
    LinearLayout lin_state;
    @Bind(R.id.lin_his)
    LinearLayout lin_his;
    @Bind(R.id.img_state)
    ImageView img_state;
    @Bind(R.id.img_his)
    ImageView img_his;
    @Bind(R.id.tv_state)
    TextView tv_state;
    @Bind(R.id.tv_his)
    TextView tv_his;
    private List<Tab> list = new ArrayList<>();
    private int total = 0;
    private int update = 0;
    private BadgeView badgeView;
    private WarmReceive warmReceive;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private AlarmStateFragment alarmStateFragment ;

    @Override
    public int setContentViewId() {
        return R.layout.activity_alarm_manager;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.stastusbar_bg),0);
        alarmStateFragment = new AlarmStateFragment();
        setFragment(alarmStateFragment);
        warmReceive = new WarmReceive();
        registerReceiver(warmReceive, new IntentFilter(Canstant.INTENTFILTER));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(warmReceive);
    }

    @OnClick({R.id.lin_state,R.id.lin_his})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.lin_state:
                setFragment(alarmStateFragment);
                img_state.setBackgroundResource(R.drawable.icon_police_higlight);
                img_his.setBackgroundResource(R.drawable.icon_history_default);
                tv_state.setTextColor(Color.BLACK);
                tv_his.setTextColor(Color.GRAY);
                break;
            case R.id.lin_his:
                setFragment(new AlarmHisFragment());
                img_state.setBackgroundResource(R.drawable.icon_police_default);
                img_his.setBackgroundResource(R.drawable.icon_history_higlight);
                tv_state.setTextColor(Color.GRAY);
                tv_his.setTextColor(Color.BLACK);
                break;
        }
    }
    public void setFragment(Fragment fragment) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.framLayout, fragment);
        transaction.commit();
    }


    public class WarmReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Canstant.INTENTFILTER)) {
                //从本地读取报警条数
                total = SharedPerferenceHelper.getUpdateNum(AlarmManagerActivity.this);
                update = intent.getIntExtra("TOTAL", 0);
                Log.d("AlarmManagerActivity",total+"   " +update);
                int i = update - total;//获取更新的条数
                if (badgeView == null) {
                    badgeView = new BadgeView(AlarmManagerActivity.this);
                }
                badgeView.setTargetView(lin_his);
                badgeView.setBadgeCount(i);

            }
        }
    }


}
