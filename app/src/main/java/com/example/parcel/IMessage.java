package com.example.parcel;

import cn.bmob.v3.BmobObject;

public class IMessage extends BmobObject{

    private MyUser uper;    // 发出信息用户
    private String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public MyUser getUper() { return uper; }

    public void setUper(MyUser uper) { this.uper = uper; }

}
