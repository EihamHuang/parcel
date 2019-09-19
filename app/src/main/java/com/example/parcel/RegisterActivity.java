package com.example.parcel;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class RegisterActivity extends AppCompatActivity {
    EditText et_register_username;
    EditText et_register_password;
    EditText et_register_name;
    EditText et_register_idnum;
    EditText et_register_learnnum;
    EditText et_register_phone;
    Button btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Bmob.initialize(this, "ced57e17be87e63476489b76d84642b4");
        et_register_username = (EditText) findViewById(R.id.editText1);
        et_register_password = (EditText) findViewById(R.id.editText2);
        et_register_name = (EditText) findViewById(R.id.editText3);
        et_register_idnum = (EditText) findViewById(R.id.editText4);
        et_register_learnnum = (EditText) findViewById(R.id.editText5);
        et_register_phone = (EditText) findViewById(R.id.editText6);
        btnregister = (Button) findViewById(R.id.buttonregister2);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonregister2:
                        String user_num = et_register_username.getText().toString();
                        String user_password = et_register_password.getText().toString().trim();
                        String user_name = et_register_name.getText().toString().trim();
                        String user_idnum = et_register_idnum.getText().toString().trim();
                        String user_learnnum = et_register_learnnum.getText().toString().trim();
                        String user_phone = et_register_phone.getText().toString().trim();
                        // 非空验证
                        if (user_num.isEmpty() || user_password.isEmpty()) {
                            Toast.makeText(RegisterActivity.this, "密码或账号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (user_name.isEmpty() ) {
                            Toast.makeText(RegisterActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        if (user_idnum.isEmpty() ) {
//                            Toast.makeText(RegisterActivity.this, "身份证号不能为空", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if (user_learnnum.isEmpty() ) {
//                            Toast.makeText(RegisterActivity.this, "学号不能为空", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (user_phone.isEmpty() ) {
                            Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 使用BmobSDK提供的注册功能
                        MyUser me = new MyUser();
                        me.setUsername(user_num);
                        me.setPassword(user_password);
                        me.setPhone(user_phone);
                        me.setName(user_name);
                        me.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    Intent intent_main = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent_main);
                                    RegisterActivity.this.finish();
                                } else {
                                    //loge(e);
                                    Toast.makeText(RegisterActivity.this, "注册失败，用户名已存在", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        //注意：不能用save方法进行注册

                }
            }
        });
    }
}
