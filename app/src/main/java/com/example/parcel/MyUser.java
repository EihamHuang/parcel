package com.example.parcel;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser {
    private String name;    // 昵称
    private String idnum;   // 身份证号
    private String learnnum;// 学号
    private String phone;   // 电话
    private BmobFile pic;   // 用户头像
    private GetParcel getparcel;    // 订单

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getIdnum() {
        return this.idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum=idnum;
    }

    public String getLearnnum(){
        return this.learnnum;
    }

    public void setLearnnum(String learnnum) {
        this.learnnum=learnnum;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public BmobFile getPic() { return pic; }

    public void setPic(BmobFile pic) { this.pic = pic; }

    public GetParcel getGetparcel() { return getparcel; }

    public void setGetparcel(GetParcel getparcel) { this.getparcel = getparcel; }

}
