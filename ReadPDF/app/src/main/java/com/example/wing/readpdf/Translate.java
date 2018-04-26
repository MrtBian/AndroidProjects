package com.example.wing.readpdf;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Wing on 2018/4/7.
 */

public class Translate extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //Service被创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder localBuilder = new Notification.Builder(this);
        localBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
        localBuilder.setAutoCancel(false);
        localBuilder.setSmallIcon(R.mipmap.ic_launcher);
        localBuilder.setTicker("Foreground Service Start");
        localBuilder.setContentTitle("Translate");
        localBuilder.setContentText("正在运行...");
        startForeground(1, localBuilder.getNotification());
    }

    //Service被启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //Service被关闭之前回调
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
