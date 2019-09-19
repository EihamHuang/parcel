package com.example.parcel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private Fragment f1;
    private Fragment f2;
    private Fragment f3;
    private BottomNavigationView navigation;
    private MyUser user = BmobUser.getCurrentUser(MyUser.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 启动推送服务
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if(e==null){
                    modifyInstallationUser(user);
//                    Toast.makeText(MainActivity.this,"开启推送成功！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"开启推送失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        BmobPush.startWork(this);

        setFragment(1);

    }

    // 绑定设备标识符installationId与当前用户
    private void modifyInstallationUser(final MyUser user) {
        BmobQuery<Installation> bmobQuery = new BmobQuery<>();
        final String id = BmobInstallationManager.getInstallationId();
        bmobQuery.addWhereEqualTo("installationId", id);
        bmobQuery.findObjectsObservable(Installation.class)
                .subscribe(new Action1<List<Installation>>() {
                    @Override
                    public void call(List<Installation> installations) {

                        if (installations.size() > 0) {
                            Installation installation = installations.get(0);
                            installation.setUser(user);
                            installation.updateObservable()
                                    .subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
//                                            Toast.makeText(MainActivity.this,"绑定当前用户成功！",Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            Toast.makeText(MainActivity.this,"绑定当前用户失败！"+
                                                    throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity.this,"后台不存在此设备Id的数据，请确认此设备Id是否正确！\n"
                                    + id,Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MainActivity.this,"查询设备数据失败："
                                + throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    setFragment(1);
                    return true;
                case R.id.navigation_chat:
                    setFragment(2);
                    return true;
                case R.id.navigation_home:
                    setFragment(3);
                    return true;
            }
            return false;
        }
    };

    // 设置fragment
    private void setFragment(int i){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (i){
            case 1:
                if(f1==null){
                    f1 = new MainFragment();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;
            case 2:
                if(f2==null){
                    f2 = new ChatFragment();
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;
            case 3:
                if(f3==null){
                    f3 = new MeFragment();
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            default:
                break;
        }
        transaction.commit();
    }

    // 隐藏所有fragment
    private void hideFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }

    }

}
