package com.example.parcel;

import cn.bmob.v3.BmobInstallation;
public class Installation extends BmobInstallation {

    private MyUser user;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
