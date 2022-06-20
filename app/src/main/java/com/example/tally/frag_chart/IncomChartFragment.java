package com.example.tally.frag_chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tally.R;
import com.example.tally.adapter.ChartItemAdapter;
import com.example.tally.db.BarChartItemBean;
import com.example.tally.db.ChartItemBean;
import com.example.tally.db.DBManager;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;


public class IncomChartFragment extends BaseChartFragment {
    int kind = 1;
    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        //获取这个月每天的总支出金额
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size() == 0) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else{
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);
            //设置有多少根柱子
            List<BarEntry> barEntries = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                //初始每一根柱子，添加到柱状图当中
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries.add(entry);
            }

            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay();//获取日期
                //根据天数，获取x轴的位置
                int xIndex = day - 1;
                BarEntry barEntry = barEntries.get(xIndex);
                barEntry.setY(itemBean.getSummoney());
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "");
            barDataSet.setValueTextColor(Color.BLACK);//值的颜色
            barDataSet.setValueTextSize(8f);//值的大小
            barDataSet.setColor(Color.parseColor("#006400"));//柱子的颜色

            //设置柱子上数据显示的格式
            barDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    //此处的value默认保存一位小数
                    if (value == 0) {
                        return "";
                    }
                    return value + "";
                }
            });

            sets.add(barDataSet);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f);//设置柱子的宽度
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year, int month) {
        //获取本月收入最高的一天为多少，将他设置为y轴的最大值
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        float max=(float) Math.ceil(maxMoney);
        //设置y轴
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setAxisMaximum(max);
        axisRight.setAxisMinimum(0f);
        axisRight.setEnabled(false);

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMaximum(max);
        axisLeft.setAxisMinimum(0f);
        axisLeft.setEnabled(false);

        //设置不显示图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
        //清空柱状图当中的数据
        barChart.clear();
        barChart.invalidate();
        setAxis(year,month);
        setAxisData(year,month);
    }
}