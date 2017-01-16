package com.kawakp.demingliu.boilerdemo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.adapter.AlarmStatesAdapter;
import com.kawakp.demingliu.boilerdemo.base.BaseFragment;
import com.kawakp.demingliu.boilerdemo.bean.AlarmStatesKeyBean;
import com.kawakp.demingliu.boilerdemo.bean.AlarmStatesValueBean;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SpotsCallBack;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.boilerdemo.widget.decoration.DividerItemDecoration;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2017/1/13.
 */

public class AlarmStateFragment extends BaseFragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private String deviceID;
    private List<AlarmStatesValueBean> totallist = new ArrayList<AlarmStatesValueBean>();
    @Override
    protected int setContentViewId() {
        return R.layout.fragment_alarmstate;
    }

    @Override
    protected void init() {
        deviceID = SharedPerferenceHelper.getDeviceId(getActivity());
        String url = PathUtils.REALTIME_DATA+deviceID+ "/elementTables/ceb6ab16297943af8af1921bf1155ace/datas?pageNum=1&pageSize=1";
       // Log.d("AlarmStateFragment","报警与状态url:"+url);
        //获取数据
        OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(getActivity());
        okHttpHelper.get(url, new SpotsCallBack<String>(getActivity()) {

            @Override
            public void onSuccess(Response response, String s) {
                Log.d("AlarmStateFragment","报警与状态:"+s);
                //获取key
                List<AlarmStatesKeyBean> list = getKey(s);
                //通过key获取对应的值
                getValue(s, list);
                //设置适配器
                setAdapter();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void getValue(String s, List<AlarmStatesKeyBean> list) {
        try {
            org.json.JSONObject object1 = new org.json.JSONObject(s);
            org.json.JSONArray array1 = object1.getJSONArray("list");
            for (int i = 0;i<array1.length();i++){
                org.json.JSONObject o1 = array1.getJSONObject(i);
                for (int j = 0; j < list.size(); j++) {
                    AlarmStatesValueBean bean = new AlarmStatesValueBean();
                    if (o1.opt(list.get(j).getFieldName()) != null) {
                        bean.setName(list.get(j).getName());
                        bean.setValue(o1.opt(list.get(j).getFieldName()).toString()); //通过key获取值
                        totallist.add(bean);
                        Log.d("AlarmStateFragment",list.get(j).getName()+"  "+o1.opt(list.get(j).getFieldName()).toString()+"  "+totallist.size());
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<AlarmStatesKeyBean> getKey(String s) {
        JSONObject object = JSON.parseObject(s);
        JSONObject o = object.getJSONObject("elementTable");
        JSONArray array = o.getJSONArray("elements");
        return JSON.parseArray(array.toString(),AlarmStatesKeyBean.class);
    }

    private void setAdapter() {
        //构建适配器
        AlarmStatesAdapter adapter = new AlarmStatesAdapter(getActivity(),totallist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
    }

    @OnClick(R.id.lin_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                getActivity().finish();
                break;
        }
    }
}
