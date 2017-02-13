package com.kawakp.demingliu.boilerdemo.utils;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.kawakp.demingliu.boilerdemo.bean.DataAnalysisBean;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by deming.liu on 2016/6/20.
 */
public class MyBarCharts {
    private String[] color = {"#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD","#C4FF8E","#FFF88D","#FFD38C","#8CEBFF","#FF8F9D","#6BF3AD"};

    public void showBarChart(BarChart barChart, BarData barData) {
        // 数据描述
        barChart.setDescription("");
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("You need to provide data for the chart.");
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false);
        // 设置是否可以触摸
        barChart.setTouchEnabled(true);
        // 是否可以拖拽
        barChart.setDragEnabled(false);
        // 是否可以缩放
        barChart.setScaleEnabled(false);
        // 集双指缩放
        barChart.setPinchZoom(false);
        // 设置背景
        barChart.setBackgroundColor(Color.parseColor("#01000000"));
        // 如果打开，背景矩形将出现在已经画好的绘图区域的后边。
        barChart.setDrawGridBackground(false);
        // 集拉杆阴影
        barChart.setDrawBarShadow(false);
        // 图例
        barChart.getLegend().setEnabled(false);
        // 设置数据
        barChart.setData(barData);

        // 隐藏右边的坐标轴 (就是右边的0 - 100 - 200 - 300 ... 和图表中横线)
        barChart.getAxisRight().setEnabled(false);
        // 隐藏左边的左边轴 (同上)
//        barChart.getAxisLeft().setEnabled(false);

        // 网格背景颜色
        barChart.setGridBackgroundColor(Color.parseColor("#00000000"));
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false);
        // 设置边框颜色
        barChart.setBorderColor(Color.parseColor("#00000000"));
        // 说明颜色
        barChart.setDescriptionColor(Color.parseColor("#00000000"));
        // 拉杆阴影
        barChart.setDrawBarShadow(false);
        // 打开或关闭绘制的图表边框。（环绕图表的线）
        barChart.setDrawBorders(false);


        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        // 设置窗体样式
        mLegend.setForm(Legend.LegendForm.CIRCLE);
        // 字体
        mLegend.setFormSize(4f);
        // 字体颜色
        mLegend.setTextColor(Color.parseColor("#00000000"));


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        //YAxis yAxis = barChart.getAxisLeft();
       // yAxis.setValueFormatter(new MyYValueFormatter());

        barChart.animateY(1000); // 立即执行的动画,Y轴
    }


    public BarData getBarData(List<DataAnalysisBean> list, int type) {
        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
       // float[] f = new float[]{10,20,15,30,40,45,21};
        //String[] s = new String[]{"14/1","15/1","16/1","17/1","18/1","19/1","20/1"};
        for (int i = 0; i < list.size(); i++) {
            if (type == 0){//实时功率

            }if (type == 1) {//平均负荷
                yValues.add(new BarEntry((float) list.get(i).getPJ_FH(), i));
            }else if (type == 2){//平均效率

            }else if (type == 3){//节能量
                yValues.add(new BarEntry((float) list.get(i).getJ_NL(), i));
                Log.d("MyBarCharts", list.get(i).getJ_NL()+"-------11111111---------------");
            }else if (type == 4){//尘减排量
                yValues.add(new BarEntry((float) list.get(i).getC_JPL(), i));
                Log.d("MyBarCharts",(float) list.get(i).getC_JPL()+"-----------22222222222222222----------");
            }else if (type == 5){//碳减排量
                yValues.add(new BarEntry((float) list.get(i).getT_JPL(), i));
            }else if (type == 6){//氮减排量
                yValues.add(new BarEntry((float) list.get(i).getD_JPL(), i));
            }else if (type == 7){//硫减排量
                yValues.add(new BarEntry((float) list.get(i).getL_JPL(), i));
            }
          /*  SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(list.get(i).getDateStr());//获取当前时间
            String str = formatter.format(curDate);
            String[] s = str.split(" ");
            String time = s[1].substring(0,5);*/
            String time = list.get(i).getDateStr();
            String[] s = time.split(" ");
            String[] s1 = s[0].split("-");
            String date = s1[2]+"/"+s1[1];
            xValues.add(date); // 设置每个壮图的文字描述
        }



        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "ceshi");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i = 0;i < list.size() ;i++){
            colors.add(Color.parseColor(color[i]));
        }
        barDataSet.setColors(colors);
        // 设置栏阴影颜色
        barDataSet.setBarShadowColor(Color.parseColor("#01000000"));
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);
        barDataSet.setValueTextColor(Color.parseColor("#FF8F9D"));
        // 绘制值
        barDataSet.setDrawValues(true);
        BarData barData = new BarData(xValues, barDataSets);
        /*barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                DecimalFormat df = new DecimalFormat("#.00");  //生成一个df对象，确保放大的value也是小数点后一位
                return ""+df.format(v);  //确保返回的数值时0.0

            }
        });*/
        return barData;
    }


}
