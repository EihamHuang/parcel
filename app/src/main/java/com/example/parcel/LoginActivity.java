package com.example.parcel;
import android.content.Intent;
import android.os.Bundle;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity  extends AppCompatActivity{
    private Button btnregister ;
    private Button btnlogin;
    EditText et_login_user, et_login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "ced57e17be87e63476489b76d84642b4");
        setContentView(R.layout.login);
        MyUser me = BmobUser.getCurrentUser(MyUser.class);
        if(me == null) {
            et_login_user = (EditText) findViewById(R.id.editText1);
            et_login_password = (EditText) findViewById(R.id.editText2);
            btnregister = (Button) findViewById(R.id.buttonregister);
            btnlogin = (Button) findViewById(R.id.buttonlogin);

            btnregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user_num = et_login_user.getText().toString();
                    String user_password = et_login_password.getText().toString().trim();
                    // 非空验证
                    if (user_num.isEmpty() || user_password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "密码或账号不能为空!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    MyUser me2 = new MyUser();
                    me2.setUsername(user_num);
                    me2.setPassword(user_password);

                    // 使用BmobSDK提供的登录功能
                    me2.login(new SaveListener<MyUser>() {

                        @Override
                        public void done(MyUser bmobUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                                Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent_main);
                                LoginActivity.this.finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败!"+e.getMessage()+e.getErrorCode(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    });
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "已登录!", Toast.LENGTH_SHORT).show();
            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent_main);
            LoginActivity.this.finish();
        }
    }
}