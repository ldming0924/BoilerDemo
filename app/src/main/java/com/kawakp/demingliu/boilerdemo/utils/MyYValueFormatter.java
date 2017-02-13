package com.kawakp.demingliu.boilerdemo.utils;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by deming.liu on 2017/1/18.
 */

public class MyYValueFormatter implements YAxisValueFormatter {
    private DecimalFormat mFormat;

    public MyYValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return mFormat.format(value);
    }
}
