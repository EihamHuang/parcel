package com.example.parcel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;

import cn.bmob.push.PushConstants;
import android.support.v4.app.NotificationCompat;
import org.json.JSONObject;
import org.json.JSONException;

public class MyPushMessageReceiver extends BroadcastReceiver{

    final private String CHANNEL_ID = "channel_id_1";
    final private String CHANNEL_NAME = "channel_name_1";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String str = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String content = "";
            try{
                JSONObject jsonObject=new JSONObject(str);
                content=jsonObject.getString("alert");
            }catch(JSONException e){
                e.printStackTrace();
            }

            NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //只在Android O之上需要渠道
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder= new NotificationCompat.Builder(context,CHANNEL_ID);
                    builder.setSmallIcon(R.mipmap.ic_launcher)     // 图标
                    .setContentTitle("来自优递的通知")             // 推送标题
                    .setContentText(content)                      // 推送内容
                    .setAutoCancel(true)                          // 自动取消
                    .setDefaults(NotificationCompat.DEFAULT_ALL); // 跟随系统

            // 点击通知返回功能
            Intent resultIntent = new Intent(context, MainActivity.class);//点击通知后进入的活动
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER); // 使之前的活动不出栈
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);         //允许更新
            builder.setContentIntent(resultPendingIntent);

            mNotificationManager.notify(1,builder.build());
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
        }
    }

}
