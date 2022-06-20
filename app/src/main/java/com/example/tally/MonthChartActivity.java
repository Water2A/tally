package com.example.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tally.adapter.ChartVPAdapter;
import com.example.tally.db.DBManager;
import com.example.tally.frag_chart.IncomChartFragment;
import com.example.tally.frag_chart.OutcomChartFragment;
import com.example.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    Button inBtn,outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager chartVp;
    private int year;
    private int month;
    int selectPos=-1,selectMonth=-1;
    List<Fragment> charFragList;
    private IncomChartFragment incomChartFragment;
    private OutcomChartFragment outcomChartFragment;
    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year, month);
        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        charFragList = new ArrayList<>();
        //添加Fragment的对象
        incomChartFragment = new IncomChartFragment();
        outcomChartFragment = new OutcomChartFragment();
        //添加数据到Fragment当中
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        incomChartFragment.setArguments(bundle);
        outcomChartFragment.setArguments(bundle);
        //将Fragment添加到数据源当中
        charFragList.add(outcomChartFragment);
        charFragList.add(incomChartFragment);
        //使用适配器
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), charFragList);
        chartVp.setAdapter(chartVPAdapter);
        //将Fragment加载到Activity当中


    }

    //统计某年某月的收支情况数据
    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);
        dateTv.setText(year+"年"+month+"月账单");
        inTv.setText("共"+incountItemOneMonth+"笔收入，￥"+inMoneyOneMonth);
        outTv.setText("共"+outcountItemOneMonth+"笔支出，￥"+outMoneyOneMonth);

    }

    //初始化时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    private void initView() {
        inBtn = findViewById(R.id.char_btn_in);
        outBtn = findViewById(R.id.char_btn_out);
        dateTv = findViewById(R.id.char_tv_date);
        inTv = findViewById(R.id.char_tv_in);
        outTv = findViewById(R.id.char_tv_out);
        chartVp = findViewById(R.id.char_vp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.char_iv_back:
                break;
            case R.id.char_iv_rili:
                showCalendarDialog();//显示日历
                break;
            case R.id.char_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.char_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }

    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selectPos;
                MonthChartActivity.this.selectMonth=month;
                initStatistics(year,month);
                incomChartFragment.setDate(year,month);
                outcomChartFragment.setDate(year,month);
            }
        });
    }

    //设置按钮样式的改变  支出-0  收入-1
    private void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        } else if (kind == 1) {
            if (kind == 1) {
                inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
                inBtn.setTextColor(Color.WHITE);
                outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
                outBtn.setTextColor(Color.BLACK);
            }
        }
    }

}