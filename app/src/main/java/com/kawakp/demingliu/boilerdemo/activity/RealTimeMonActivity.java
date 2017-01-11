package com.kawakp.demingliu.boilerdemo.activity;

import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.bean.Tab;
import com.kawakp.demingliu.boilerdemo.fragment.ContrloFragment;
import com.kawakp.demingliu.boilerdemo.fragment.DataFragment;
import com.kawakp.demingliu.boilerdemo.fragment.HistoryFragment;
import com.kawakp.demingliu.boilerdemo.fragment.ParmFragment;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;

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
    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        initTab();
    }

    private void initTab() {
        mTabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.framLayout);

        Tab tab_data = new Tab(R.string.data, R.drawable.data_selector, DataFragment.class);
        Tab tab_control_set = new Tab(R.string.control_set, R.drawable.control_selector, ContrloFragment.class);
        Tab tab_parm_set = new Tab(R.string.param_set, R.drawable.parm_selector, ParmFragment.class);
        Tab tab_his_warm = new Tab(R.string.his_warm, R.drawable.his_selector, HistoryFragment.class);

        list.add(tab_data);
        list.add(tab_control_set);
        list.add(tab_parm_set);
        list.add(tab_his_warm);

        for (Tab tab : list) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
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

    @OnClick({R.id.lin_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
        }
    }
}
