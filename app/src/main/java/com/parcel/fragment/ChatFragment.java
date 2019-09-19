package com.parcel.fragment;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parcel.R;
import com.parcel.chat.FlusMessage;
import com.parcel.data.GetParcel;
import com.parcel.chat.IMessage;
import com.parcel.data.MyUser;

import java.util.List;

public class ChatFragment extends Fragment {
    private List<GetParcel> data;           // 获取信息信息
    private GetParcel gp;
    public static TextView showTxt;
    private EditText editText;
    private Button button;
    private Context context;
    private MyUser me = BmobUser.getCurrentUser(MyUser.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        this.context = getActivity();

        //初始化控件
        showTxt=(TextView)view.findViewById(R.id.showtxt);
        editText=(EditText)view.findViewById(R.id.edit);
        button=(Button)view.findViewById(R.id.buutton);

        //打开一个线程实时刷新消息
        final FlusMessage flusMessage=new FlusMessage(context);
        flusMessage.start();
//        Toast.makeText(context,"聊天室 启动！", Toast.LENGTH_SHORT).show();

        button.setOnClickListener(new OnClickListener() {//点击发送消息

            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(context,"有信息还未填入",Toast.LENGTH_SHORT).show();
                }
                else{
                    IMessage iMessage=new IMessage();//Bmob数据库表格类
                    iMessage.setMsg(message);
                    iMessage.setUper(me);
                    iMessage.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                showTxt.setText(flusMessage.msg2+me.getName()+":"+editText.getText().toString());
                                editText.setText("");
                                Toast.makeText(context,"发布成功！", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "发布失败！" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });

        return view;
    }
}



