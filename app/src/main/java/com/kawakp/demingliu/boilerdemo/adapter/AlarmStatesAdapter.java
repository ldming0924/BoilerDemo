package com.kawakp.demingliu.boilerdemo.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.bean.AlarmStatesValueBean;
import com.kawakp.demingliu.boilerdemo.bean.WarmBean;

import java.util.List;
import java.util.Map;

/**
 * Created by deming.liu on 2017/1/16.
 */

public class AlarmStatesAdapter extends SimpleAdapter<AlarmStatesValueBean> {
    private List<AlarmStatesValueBean> datas;
    private Context context;


    public AlarmStatesAdapter(Context context, List<AlarmStatesValueBean> datas) {
        super(context, R.layout.adapter_alarmstates_item, datas);
        this.context = context;
        this.datas = datas;
    }


    @Override
    protected void convert(BaseViewHolder viewHoder, AlarmStatesValueBean item) {
        viewHoder.getTextView(R.id.textView_name).setText(item.getName());
        ImageView imageView = (ImageView) viewHoder.getView(R.id.imageView_state);
        if (item.getValue().equals("false")){
            imageView.setBackgroundResource(R.drawable.state_running);
        }else {
            imageView.setBackgroundResource(R.drawable.state_stop);
        }
    }
}
