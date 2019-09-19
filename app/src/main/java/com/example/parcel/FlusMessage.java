package com.example.parcel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import android.widget.Toast;

public class FlusMessage extends Thread {

    List<IMessage> data;           // 获取信息信息
    int oldsize=0;
    String time = "";
    Context context;
    int oldLen=0;
    String msg="",msg2="";
    public FlusMessage(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (true) {
            data = new ArrayList<IMessage>();
            BmobQuery<IMessage> bmobQuery = new BmobQuery<IMessage>();
            bmobQuery.include("uper");
            bmobQuery.findObjects(new FindListener<IMessage>() {
                @Override
                public void done(List<IMessage> list, BmobException e) {
                    if(e==null){
                        if(oldsize!=list.size()){
                            oldsize=list.size();
                            for(int i=0;i < list.size();i++) {
                                data.add(list.get(i));
                                msg=msg+data.get(i).getCreatedAt()+"\n";
                                msg=msg+data.get(i).getUper().getName()+":";
                                msg=msg+data.get(i).getMsg()+"\n";
                            }
                            ChatFragment.showTxt.setText(msg);//设置主UI界面TextView
                            msg2=msg;//备份消息
                            msg="";
//                            Toast.makeText(context,"查询成功！", Toast.LENGTH_SHORT).show();
                        }else{
                            ChatFragment.showTxt.setText(msg2);//设置主UI界面TextView
                        }
                    }else{
                        Toast.makeText(context, "查询失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            try {
                Thread.sleep(5000);//每隔5s刷新一次数据
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        }

    }

}
