package com.kawakp.demingliu.boilerdemo.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseFragment;

import butterknife.Bind;
import okhttp3.Response;

/**
 * Created by deming.liu on 2016/9/26.
 */
public class ProcessMonitoringFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.lin_back)
    LinearLayout lin_back;


    @Override
    protected int setContentViewId() {
        return R.layout.fragment_processmon;
    }

    @Override
    protected void init() {
    lin_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_back:
                getActivity().finish();
                break;
        }
    }
}
