package com.kawakp.demingliu.boilerdemo.fragment;


import android.view.View;
import android.widget.LinearLayout;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by deming.liu on 2016/9/8.
 */
public class ContrloFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.lin_back)
    LinearLayout lin_back;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_control;
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
