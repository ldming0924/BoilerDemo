package com.kawakp.demingliu.boilerdemo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.adapter.BaseAdapter;
import com.kawakp.demingliu.boilerdemo.adapter.RealTimeExpandableAdapter;
import com.kawakp.demingliu.boilerdemo.base.BaseFragment;
import com.kawakp.demingliu.boilerdemo.bean.Bean;
import com.kawakp.demingliu.boilerdemo.bean.ChildInfo;
import com.kawakp.demingliu.boilerdemo.bean.MyElementBean;
import com.kawakp.demingliu.boilerdemo.canstant.Canstant;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SimpleCallback;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

/**
 * Created by deming.liu on 2016/9/8.
 */
public class DataFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.rel_e_lv_parameter)
    ExpandableListView eLvParameter;
    @Bind(R.id.lin_back)
    LinearLayout lin_back;
    private RealTimeBroadCase realTimeBroadCase;
    private List<Map<String, String>> ml = new ArrayList<Map<String, String>>();
    private boolean isFirst = true;
    private SpotsDialog mDialog;
    private OkHttpHelper okHttpHelper;
    private String url;
    private String deviceID;
    private List<Bean> totallist = new ArrayList<Bean>();  //分类
    private Map<String, List<ChildInfo>> map;
    private List<String> parent;
    private RealTimeExpandableAdapter adapter;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_data;
    }

    @Override
    protected void init() {
        realTimeBroadCase = new RealTimeBroadCase();
        getActivity().registerReceiver(realTimeBroadCase, new IntentFilter(Canstant.MAINACTIVITY));
        if (getActivity() != null) {
            mDialog = new SpotsDialog(getActivity(),"拼命加载中...");
            mDialog.show();
        }
        okHttpHelper = OkHttpHelper.getInstance(getActivity());
        deviceID = SharedPerferenceHelper.getDeviceId(getActivity());
        url = PathUtils.ELEMENT_LIST +"devices/"+deviceID+ "/elementCategorys?" + "&type=MONITOR";
        Log.d("TAG",url);
        lin_back.setOnClickListener(this);
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(realTimeBroadCase);
    }

    private class RealTimeBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           // Log.d("TAG","shsihshuju"+intent.getStringExtra("MESSAGE"));
            if (intent.getAction().equals(Canstant.MAINACTIVITY)) {
                String message = intent.getStringExtra("MESSAGE");
                //   获取key
                List<MyElementBean> elist = getKeyDatas(message);
                //  通过key，获取值
                getValueDatas(message, elist);
                if (ml.size() > 0) {
                    if (isAdded()) {
                        if (isFirst) {
                            //是否是第一次请求，是就请求分类信息，不是就不请求
                            if (getActivity() != null) {
                                mDialog.dismiss();
                                okHttpHelper.get(url, new SimpleCallback<String>(getActivity()) {
                                    @Override
                                    public void onSuccess(Response response, String s) {
                                        Log.d("TAG","分类信息："+s);
                                        JSONArray jsonArray = JSON.parseArray(s);
                                        List<Bean> list = JSON.parseArray(jsonArray.toString(), Bean.class);
                                        totallist.clear();
                                        totallist.addAll(list);
                                        map = new HashMap<>();
                                        parent = new ArrayList<>();
                                        getParentChildDatas();
                                        eLvParameter.setGroupIndicator(null);
                                        adapter = new RealTimeExpandableAdapter(map, parent, getActivity());
                                        eLvParameter.setAdapter(adapter);
                                        isFirst = false;
                                        //展开每一item
                                        for (int i = 0; i < adapter.getGroupCount(); i++) {
                                            eLvParameter.expandGroup(i);
                                        }
                                    }

                                    @Override
                                    public void onError(Response response, int code, Exception e) {
                                        Log.d("TAG","分类信息code"+code+"----------");
                                    }
                                });
                            }
                        } else {
                            getParentChildDatas();
                            //展开每一item
                            for (int i = 0; i < adapter.getGroupCount(); i++) {
                                eLvParameter.expandGroup(i);
                            }
                            //更新数据
                            adapter.notifyDataSetChanged();

                        }
                    }
                } else {
                    mDialog.dismiss();
                    Log.d("TAG",ml.size()+"----------------");

                }

            }
        }


    }

    private List<MyElementBean> getKeyDatas(String message) {
        //  TODO: 2016/10/14  获取实时数据的element 中的filename

        JSONObject fo = JSON.parseObject(message);
        JSONObject fobj = fo.getJSONObject("elementTable");
        JSONArray fa = fobj.getJSONArray("elements");
        return JSON.parseArray(fa.toString(), MyElementBean.class);
    }

    private void getValueDatas(String message, List<MyElementBean> elist) {
        try {
            // TODO: 2016/10/14 获取实时数据，通过filenam得到key
            org.json.JSONObject object = new org.json.JSONObject(message);
            org.json.JSONArray array = object.getJSONArray("list");
            ml.clear();
            for (int i = 0; i < array.length(); i++) {
                org.json.JSONObject o = array.getJSONObject(i);
                for (int j = 0; j < elist.size(); j++) {
                    Map<String, String> m = new HashMap<String, String>();
                    if (o.opt(elist.get(j).getFieldName()) != null) {
                        m.put(elist.get(j).getFieldName(), o.opt(elist.get(j).getFieldName()) + "");
                        ml.add(m);
                    }


                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG", e.toString());
        }
    }

    private void getParentChildDatas() {
        map.clear();
        parent.clear();
        for (int i = 0; i < totallist.size(); i++) {
            parent.add(totallist.get(i).getName());
            List<ChildInfo> childInfos = new ArrayList<>();
            int size = totallist.get(i).getElements().size();
            for (int j = 0; j < size; j++) {
                String displayName = totallist.get(i).getElements().get(j).getName();
                String address = totallist.get(i).getElements().get(j).getDefaultAddress();

                for (int k = 0; k < ml.size(); k++) {
                    Set set = ml.get(k).entrySet();
                    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        if (totallist.get(i).getElements().get(j).getFieldName().equals(key)) {
                            //Log.d("TAG",displayName);
                            ChildInfo childInfo = new ChildInfo(displayName, ml.get(k).get(key), totallist.get(i).getElements().get(j).getUnit());
                            childInfos.add(childInfo);
                        }
                    }

                }
            }
            map.put(parent.get(i), childInfos);
        }
    }
}
