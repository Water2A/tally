package com.example.tally.adapter;

import static com.example.tally.utils.FloatUtils.ratioToPercent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tally.R;
import com.example.tally.db.ChartItemBean;

import java.util.List;

//账单详情页面，ListView的适配器
public class ChartItemAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    List<ChartItemBean> mDatas;

    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chartfrag_lv, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //获取显示内容
        ChartItemBean bean = mDatas.get(position);
        viewHolder.iv.setImageResource(bean.getsImageId());
        viewHolder.typeTv.setText(bean.getType());
        viewHolder.totalTv.setText("￥"+bean.getTotalMoney());
        float ratio = bean.getRatio();
        String pert = ratioToPercent(ratio);
        viewHolder.ratioTv.setText(pert);
        return convertView;
    }

    class ViewHolder{
        TextView typeTv,ratioTv,totalTv;
        ImageView iv;

        public ViewHolder(View view) {
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type);
            ratioTv = view.findViewById(R.id.item_chartfrag_tv_pert);
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }
}
