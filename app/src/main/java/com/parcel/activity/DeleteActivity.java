package com.parcel.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.widget.ListView;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.parcel.R;
import com.parcel.adapter.MyAdapterOrderDelete;
import com.parcel.data.GetParcel;
import com.parcel.data.MyUser;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class DeleteActivity extends AppCompatActivity {

    private SwipeRefreshLayout swiperereshlayout;
    private ListView listview;
    private MyAdapterOrderDelete adapter;
    private List<GetParcel> data;           // 获取信息信息
    private BmobQuery<GetParcel> query;     // 后端信息队列
    private String time = "";
    private GetParcel gp;
    private MyUser me = BmobUser.getCurrentUser(MyUser.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_now);

        // 设置数据适配器
        data = new ArrayList<GetParcel>();
        initData();
        adapter = new MyAdapterOrderDelete(data, this);
        listview = (ListView)findViewById(R.id.listview);
        listview.setAdapter(adapter);

        // 初步加载
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        },500);

        // 设置滑动颜色
        swiperereshlayout = (SwipeRefreshLayout)findViewById(R.id.swiperereshlayout);
        swiperereshlayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);

        // 刷新监听
        swiperereshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 设置2秒的时间来执行以下事件
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getInformation();
                        adapter.notifyDataSetChanged();
                        swiperereshlayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        // 按钮监听
        adapter.setOnBtnClickListener(new MyAdapterOrderDelete.onBtnClickListener() {
            @Override
            public void onBtnClick(final int position) {
                gp = data.get(position);
                BmobQuery<GetParcel> gp_list = new BmobQuery<GetParcel>();
                gp_list.getObject(gp.getObjectId(), new QueryListener<GetParcel>() {
                    @Override
                    public void done(GetParcel getParcel, BmobException e) {
                        if(e==null){
                            if(getParcel.getStatus()==0){
                                AlertDialog.Builder alterDialog = new AlertDialog.Builder(DeleteActivity.this);
                                alterDialog.setTitle("提示");
                                alterDialog.setMessage("确定删除吗？");
                                alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        gp.delete(gp.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    data.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                    Toast.makeText(DeleteActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(DeleteActivity.this,"删除失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                });
                                alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(DeleteActivity.this,"点击取消",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alterDialog.show();
                            }else{
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(DeleteActivity.this,"晚了，该订单已被人接下！",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(DeleteActivity.this,"查询出错！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }

    // 初始化数据
    public void initData(){
        query = new BmobQuery<GetParcel>();
        query.addWhereEqualTo("status",0);
        query.order("createdAt");
        query.include("uper,downer");
        query.findObjects(new FindListener<GetParcel>() {
            @Override
            public void done(List<GetParcel> list, BmobException e) {
                if(list.size()<=0){
                    Toast.makeText(DeleteActivity.this, "当前还没有已发布的订单哦" , Toast.LENGTH_SHORT).show();
                }else {
                    if (e == null) {
                        for(GetParcel gp:list) {
                            if(gp.getUper().getObjectId().equals(me.getObjectId())) {
                                data.add(0, gp);
                            }
                        }
                        time = list.get(0).getCreatedAt();
                        Log.d("截止时间是：", time);
                    } else {
                        Toast.makeText(DeleteActivity.this, "获取信息失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // 上垃获取数据
    public void getInformation(){
        query = new BmobQuery<GetParcel>();
        query.addWhereEqualTo("status",0);
        query.order("createdAt");
        query.include("uper,downer");
        query.findObjects(new FindListener<GetParcel>() {
            @Override
            public void done(List<GetParcel> list, BmobException e) {
                if(e==null){
                    for(GetParcel gp:list) {
                        if(gp.getCreatedAt().compareTo(time)>0&&
                                gp.getUper().getObjectId().equals(me.getObjectId())) {
                            time = gp.getCreatedAt();
                            data.add(0, gp);
                            Log.d("截止时间是：", time);
                        }
                    }
                }else {
                    Toast.makeText(DeleteActivity.this, "获取信息失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
