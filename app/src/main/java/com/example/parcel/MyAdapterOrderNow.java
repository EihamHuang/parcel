package com.example.parcel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapterOrderNow extends BaseAdapter {
    private List<GetParcel> data;
    private LayoutInflater inflater;
    private GetParcel gp;
    private ViewHolder viewHolder;
    private onBtnClickListener mOnBtnClickListener;

    public class ViewHolder{
        public ImageView image_photo;
        public TextView tv_name;
        public TextView tv_money;
        public TextView tv_begin;
        public TextView tv_ending;
        public TextView tv_describe;
        public TextView tv_time;
        public Button accept;
    }

    public MyAdapterOrderNow(List<GetParcel> data, Context context) {
        this.data = data;
        this.inflater=LayoutInflater.from(context);
    }

    // 按钮监听接口
    public interface onBtnClickListener {
        void onBtnClick(int position);
    }

    public void setOnBtnClickListener(onBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public GetParcel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.my_order_item_now, null);
            //在视图中查找控件
            viewHolder.image_photo = (ImageView) convertView.findViewById(R.id.image_photo);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tv_money = (TextView) convertView.findViewById(R.id.money);
            viewHolder.tv_begin = (TextView) convertView.findViewById(R.id.begin);
            viewHolder.tv_ending = (TextView) convertView.findViewById(R.id.ending);
            viewHolder.tv_describe = (TextView) convertView.findViewById(R.id.describe);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.accept = (Button) convertView.findViewById(R.id.accept);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // 设置内容
        gp = data.get(position);
        viewHolder.tv_name.setText(gp.getDowner().getName());
        viewHolder.tv_money.setText("金额："+String.valueOf(gp.getMoney()));
        viewHolder.tv_begin.setText("起点："+gp.getBegin());
        viewHolder.tv_ending.setText("终点："+gp.getEnding());
        viewHolder.tv_describe.setText("电话："+gp.getDowner().getPhone());
        viewHolder.tv_time.setText("时间："+gp.getUpdatedAt());

        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBtnClickListener.onBtnClick(position);
            }
        });
        return convertView;
    }
}
