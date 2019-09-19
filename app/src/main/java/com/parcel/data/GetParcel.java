package com.parcel.data;

import cn.bmob.v3.BmobObject;

public class GetParcel extends BmobObject {
    private String begin;   // 起点
    private String ending;  // 终点
    private String describe; // 描述
    private Integer money;  // 金额
    private Integer status; // 订单状态 0未接单 1已接单 2已送达
    private MyUser uper;    // 发出请求用户
    private MyUser downer;  // 接收请求用户

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnding() {
        return ending;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }

    public String getDescribe() { return describe; }

    public void setDescribe(String describe) { this.describe = describe; }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public MyUser getUper() {
        return uper;
    }

    public void setUper(MyUser uper) {
        this.uper = uper;
    }

    public MyUser getDowner() { return downer; }

    public void setDowner(MyUser downer) { this.downer = downer; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }
}
