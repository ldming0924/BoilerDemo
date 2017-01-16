package com.kawakp.demingliu.boilerdemo.activity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseActivity;
import com.kawakp.demingliu.boilerdemo.bean.DeviceListBean;
import com.kawakp.demingliu.boilerdemo.bean.OrgBean;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SpotsCallBack;
import com.kawakp.demingliu.boilerdemo.tree.bean.Bean;
import com.kawakp.demingliu.boilerdemo.tree.bean.Node;
import com.kawakp.demingliu.boilerdemo.tree.bean.TreeListViewAdapter;
import com.kawakp.demingliu.boilerdemo.tree.view.SimpleTreeAdapter;
import com.kawakp.demingliu.boilerdemo.utils.ActivityManager;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.boilerdemo.widget.systembar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2017/1/11.
 */

public class DeviceManagerActivity extends BaseActivity {
    @Bind(R.id.id_tree)
    ListView listView;
    private List<OrgBean> totallist = new ArrayList<OrgBean>();
    private String orgId;
    int page = 1;
    int pageSize = 1000;
    private TreeListViewAdapter mAdapter;
    private List<Bean> mDatas = new ArrayList<Bean>();
    private String device_url;
    private OkHttpHelper okHttpHelper;

    @Override
    public int setContentViewId() {
        return R.layout.activity_device_manager;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.stastusbar_bg),0);
        init();
    }

    private void init() {
        orgId = SharedPerferenceHelper.getOrgId(DeviceManagerActivity.this);
        device_url = PathUtils.DEVICELIST_PATH + "&pageNum=" + page + "&pageSize=" + pageSize;
        //获取组织结构url
        String url = PathUtils.DEVICE_ORG;
        Log.d("TAG","------"+url+"　＝＝＝"+device_url);
        okHttpHelper = OkHttpHelper.getInstance(DeviceManagerActivity.this);
        //获取组织结构列表
        getOrg(url);
    }
    /**
     * 获取组织结构
     *
     * @param url
     */
    private void getOrg(String url) {
        okHttpHelper.get(url, new SpotsCallBack<String>(DeviceManagerActivity.this) {

            @Override
            public void onSuccess(Response response, String s) {
                Log.d("TAG","zuzhijiegou ==="+s);
                JSONArray array = JSON.parseArray(s);
                List<OrgBean> list = JSON.parseArray(array.toString(), OrgBean.class);
                totallist.addAll(list);
                for (int i = 0; i < totallist.size(); i++) {
                    mDatas.add(new Bean(list.get(i).getId(), list.get(i).getParentId(), list.get(i).getName(), null, null, null));
                }
                //获取设备列表
                getDeviceList();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.e("DeviceList","code = "+code + " -------------------");
            }
        });
    }

    /**
     * 获取设备类别加到组织结构上
     */
    private void getDeviceList() {
        okHttpHelper.get(device_url, new SpotsCallBack<String>(DeviceManagerActivity.this) {

            @Override
            public void onSuccess(Response response, String s) {
                // Log.d("TAG","设备列表=="+s);

                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray array = jsonObject.getJSONArray("list");
                List<DeviceListBean> list = JSON.parseArray(array.toString(), DeviceListBean.class);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getOrgId() != null) {
                        for (int j = 0; j < totallist.size(); j++) {
                            if (totallist.get(j).getId().equals(list.get(i).getOrgId())) {
                                //将设备数据绑定到组织结构上
                                mDatas.add(new Bean(list.get(i).getId() + "", list.get(i).getOrgId(), list.get(i).getName(), list.get(i).getPlcDataModelId(), list.get(i).getId(), list.get(i).getOnlineStatus()));
                            }
                        }
                    }

                }

                try {
                    mAdapter = new SimpleTreeAdapter<Bean>(listView, DeviceManagerActivity.this, mDatas, 0);
                    mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                        @Override
                        public void onClick(Node node, int position) {
                            if (node.isLeaf()) {
                                if (node.getDeviceId() != null ) {
                                    //if (node.getDeviceId() != null && node.getPlcDataModelId() != null) {
                                    //保存deviceModelId
                                    boolean b = SharedPerferenceHelper.saveDeviceModelId(DeviceManagerActivity.this, node.getPlcDataModelId(), node.getDeviceId());
                                    if (b) {
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "暂无设备",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ERR", e.toString());
                }
                listView.setAdapter(mAdapter);

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.e("DeviceList","code = "+code + " "+e.toString());
            }
        });
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
