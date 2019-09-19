package com.example.parcel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddActivity extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private Button btn1;
    private Button btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et1 = (EditText)findViewById(R.id.editText1);
        et2 = (EditText)findViewById(R.id.editText2);
        et3 = (EditText)findViewById(R.id.editText3);
        et4 = (EditText)findViewById(R.id.editText4);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String begin= et1.getText().toString();
                String ending= et2.getText().toString();
                String money= et3.getText().toString();
                String describe= et4.getText().toString();
                if(begin.isEmpty()||ending.isEmpty()||money.isEmpty()||describe.isEmpty()){
                    Toast.makeText(AddActivity.this,"有信息还未填入",Toast.LENGTH_SHORT).show();
                }else {
                    GetParcel gp = new GetParcel();
                    gp.setBegin(begin);
                    gp.setEnding(ending);
                    gp.setMoney(Integer.valueOf(money));
                    gp.setDescribe(describe);
                    gp.setStatus(0);
                    gp.setUper(BmobUser.getCurrentUser(MyUser.class));
                    gp.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null)
                                Toast.makeText(AddActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AddActivity.this, "发布失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
