package com.parcel.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.widget.ListView;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.parcel.R;
import com.parcel.activity.AddActivity;
import com.parcel.activity.DeleteActivity;
import com.parcel.data.GetParcel;
import com.parcel.adapter.MyAdapter;
import com.parcel.data.MyUser;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainFragment extends Fragment {

    private Context context;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private SwipeRefreshLayout swiperereshlayout;
    private ListView listview;
    private MyAdapter adapter;
    private List<GetParcel> data;           // 获取信息信息
    private GetParcel gp;
    private BmobQuery<GetParcel> query;     // 后端信息队列
    private String time = "";
    private MyUser me = BmobUser.getCurrentUser(MyUser.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        this.context = getActivity();   //获取当前活动

        // 发布按钮
        fab = (FloatingActionButton)view.findViewById(R.id.fbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add = new Intent(context, AddActivity.class);
                startActivity(intent_add);
            }
        });

        fab2 = (FloatingActionButton)view.findViewById(R.id.fbutton2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_delete = new Intent(context, DeleteActivity.class);
                startActivity(intent_delete);
            }
        });


        // 设置数据适配器
        data = new ArrayList<GetParcel>();
        initData();
        adapter = new MyAdapter(data, context);
        listview = (ListView)view.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        // 初步加载
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        },500);

        // 设置滑动颜色
        swiperereshlayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperereshlayout);
        swiperereshlayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);

        // 刷新监听
        swiperereshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 设置1.5秒的时间来执行以下事件
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getInformation();
                        adapter.notifyDataSetChanged();
                        swiperereshlayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        // 按钮监听
        adapter.setOnBtnClickListener(new MyAdapter.onBtnClickListener() {
            @Override
            public void onBtnClick(final int position) {
                gp = data.get(position);
                if(gp.getDowner() != null && gp.getStatus()!=0){
                    Toast.makeText(context,"哦您晚了，该订单已被用户\""+gp.getDowner().getName()+"\"承包了" +
                            "",Toast.LENGTH_SHORT).show();
                    data.remove(position);
                    adapter.notifyDataSetChanged();
                }else {
                    AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
                    alterDialog.setTitle("提示");
                    alterDialog.setMessage("确定接单吗？");
                    alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gp.setDowner(me);
                            gp.setStatus(1);
                            gp.update(gp.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        pushMessage(gp);
                                        Toast.makeText(context, "接单成功！" + position, Toast.LENGTH_SHORT).show();
                                        data.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(context, "该订单已被删除！", Toast.LENGTH_SHORT).show();
                                        data.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    });
                    alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(context,"点击取消",Toast.LENGTH_SHORT).show();
                        }
                    });
                    alterDialog.show();
                }
            }
        });

        return view;
    }

//    private void updateItem(int position) {
//        //屏幕中第一个可见的条目位置
//        int firstVisiblePosition = listview.getFirstVisiblePosition();
//        //屏幕中最后一个可见的条目位置
//        int lastVisiblePosition = listview.getLastVisiblePosition();
//        //在看见范围内才更新，不可见的滑动后自动会调用getView方法更新
//        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
//            //获取指定位置view对象
//            View view = listview.getChildAt(position - firstVisiblePosition);
//            adapter.getView(position, view, listview);
//        }
//    }

    // 推送消息
    public void pushMessage(GetParcel gp){
        BmobQuery<MyUser> bmobQueryUser = new BmobQuery<>();
        bmobQueryUser.addWhereEqualTo("objectId",gp.getUper().getObjectId());
        bmobQueryUser.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        BmobPushManager bmobPushManager = new BmobPushManager();
                        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
                        query.addWhereEqualTo("user", list.get(0));
                        bmobPushManager.setQuery(query);
                        bmobPushManager.pushMessage("恭喜，你的订单被" + me.getName() + "接下了！",
                                new PushListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(context, "推送成功！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "推送失败！" + e.getMessage() +
                                                    e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(context, "找不到上传者！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "推送失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(context, "当前还没有可接的订单哦" , Toast.LENGTH_SHORT).show();
                }else {
                    if (e == null) {
                        for(GetParcel gp:list) {
                            if (!gp.getUper().getObjectId().equals(me.getObjectId())) {
                                data.add(0, gp);
                            }
                        }
                        time = list.get(list.size()-1).getCreatedAt();
                        Log.d("截止时间是：", time);
                    } else {
                        Toast.makeText(context, "获取信息失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
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
                                !gp.getUper().getObjectId().equals(me.getObjectId())) {
                            time = gp.getCreatedAt();
                            data.add(0, gp);
                            Log.d("截止时间是：", time);
                        }
                    }
                }else {
                    Toast.makeText(context, "获取信息失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
