package com.hk.zhouyuyin.receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.hk.zhouyuyin.MyApplication;

/**
 * Created by Administrator on 2017/8/3 0003.
 */

public class SafeService extends Service {
    private MyApplication trackApp = null;
    private OnTraceListener traceListener = null;

    private PowerManager.WakeLock wakeLock = null;

    @Override
    public void onCreate() {
        Log.e("SafeService","onCreate");
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, SafeService.class.getName());
        wakeLock.acquire();
        trackApp = (MyApplication) getApplicationContext();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SafeService","onStartCommand");
        initListener();
        trackApp.mClient.startTrace(trackApp.mTrace, traceListener);
        trackApp.mClient.startGather(traceListener);
        return START_STICKY;
    }

    private void initListener() {
        traceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int errorNo, String message) {
            }
            @Override
            public void onStartTraceCallback(int errorNo, String message) {

            }
            @Override
            public void onStopTraceCallback(int errorNo, String message) {
            }
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
            }
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
            }
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {
            }
        };
    }

    @Override
    public void onDestroy() {
        Log.e("SafeService","onDestory");
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        stopForeground(true);
        trackApp.mClient.stopTrace(trackApp.mTrace, traceListener);
        trackApp.mClient.stopGather(traceListener);
        Intent intent = new Intent("com.gg.safetymanagement.destroy");
        sendBroadcast(intent);
        super.onDestroy();
    }


}
