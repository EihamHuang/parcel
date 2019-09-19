package com.example.parcel;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.functions.Action1;

public class MeFragment extends Fragment {
    private Context context;
    private ImageView im;
    private TextView tv1;
    private TextView tv2;
    private Button btn2;
    private Button btn5;
    private Button ord;
    private Button accept;
    private Button btn3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment, container, false);
        this.context = getActivity();

        tv1 = (TextView)view.findViewById(R.id.textView1);
        tv2 = (TextView)view.findViewById(R.id.textView2);
        btn2 = (Button)view.findViewById(R.id.button2);
        btn5 = (Button)view.findViewById(R.id.button5);
        ord = (Button)view.findViewById(R.id.btnorder);
        accept = (Button)view.findViewById(R.id.btnaccept);
        btn3 = (Button)view.findViewById(R.id.button3);

        MyUser me = BmobUser.getCurrentUser(MyUser.class);
        tv1.setText(me.getName());
        tv2.setText(me.getPhone());


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(context,SearchActivity.class);
                startActivity(intent_login);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3= new Intent(context,AboutusActivity.class);
                startActivity(intent3);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyInstallationUser();
            }
        });

        ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_order = new Intent(context,MyOrderActivity.class);
                startActivity(intent_order);
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_accept = new Intent(context,MyOrderAcceptActivity.class);
                startActivity(intent_accept);
            }
        });

        return view;
    }

    private void modifyInstallationUser() {
        BmobQuery<Installation> bmobQuery = new BmobQuery<>();
        final String id = BmobInstallationManager.getInstallationId();
        bmobQuery.addWhereEqualTo("installationId", id);
        bmobQuery.findObjectsObservable(Installation.class)
                .subscribe(new Action1<List<Installation>>() {
                    @Override
                    public void call(List<Installation> installations) {

                        if (installations.size() > 0) {
                            Installation installation = installations.get(0);
                            MyUser user = new MyUser();
                            installation.setUser(user);
                            user.setObjectId("");
                            installation.updateObservable()
                                    .subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
//                                            Toast.makeText(context, "解除推送绑定成功！", Toast.LENGTH_SHORT).show();
                                            /**
                                             * TODO 更新成功之后再退出
                                             */
                                            BmobUser.logOut();
                                            startActivity(new Intent(context, LoginActivity.class));
                                            getActivity().finish();
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            Toast.makeText(context, "更新设备用户信息失败：" +
                                                    throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(context, "后台不存在此设备Id的数据，请确认此设备Id是否正确！\n"
                                    + id, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(context, "查询设备数据失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
