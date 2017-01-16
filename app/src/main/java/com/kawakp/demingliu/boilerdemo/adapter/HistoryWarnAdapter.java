package com.kawakp.demingliu.boilerdemo.adapter;

import android.content.Context;


import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.bean.HistoryWarnBean;
import com.kawakp.demingliu.boilerdemo.bean.WarmBean;

import java.util.List;

/**
 * Created by deming.liu on 2016/8/26.
 */
public class HistoryWarnAdapter extends SimpleAdapter<WarmBean> {
    private List<HistoryWarnBean> list;
    private Context context;

    public HistoryWarnAdapter(Context context, List<WarmBean> datas) {
        super(context, R.layout.adapter_historywarn_item, datas);
        this.context = context;
    }



    @Override
    protected void convert(BaseViewHolder viewHoder, WarmBean item) {
        //viewHoder.getTextView(R.id.textView_num).setText(item.));
        viewHoder.getTextView(R.id.textView_name).setText(item.getName());
        viewHoder.getTextView(R.id.textView_time).setText(item.getCreateDate());
        viewHoder.getTextView(R.id.textView_states).setText(item.getName());
        /*int type = viewHoder.getItemViewType();
        switch (type){
            case 0:
                viewHoder.getView(R.id.lin_detail).setBackgroundColor(context.getResources().getColor(R.color.gray));
                break;
            case 1:
                viewHoder.getView(R.id.lin_detail).setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
        }*/
    }
}
