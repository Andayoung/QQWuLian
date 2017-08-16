package com.hk.zhouyuyin;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.hk.zhouyuyin.util.CommonUtil;
import com.hk.zhouyuyin.util.SerialNumberHelper;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by liangliang on 2017/5/8.
 */

public class MyApplication extends Application {
    private static Context context;
    public static int screenWidth;
    public static int screenHeight;
    public static float screenDensity;
    public LBSTraceClient mClient = null;
    public Trace mTrace = null;
    public long serviceId = 145294;
    public String entityName = "UUWatchTrace";
    int gatherInterval = 10;
    int packInterval = 10;
    @Override
    public void onCreate() {
        context = getApplicationContext();
        SpeechUtility.createUtility(this, "appid=" + "55b98087");
        super.onCreate();
        iniData();
        SerialNumberHelper serialNumberHelper = new SerialNumberHelper(getApplicationContext());
        String help = serialNumberHelper.read4File();
        if (help != null && !help.equals("")) {
            String[] s = help.split(" ");
            if(s!=null&&s.length>0){
                entityName=s[0];
            }
        }
//        entityName="biubiubiu";
        if ("com.baidu.track:remote".equals(CommonUtil.getCurProcessName(context))) {
            return;
        }
        SDKInitializer.initialize(context);
        mClient = new LBSTraceClient(context);
        mTrace = new Trace(serviceId, entityName);
        mClient.setInterval(gatherInterval, packInterval);
    }

    public static Context getContext(){
        return context;
    }

    private void iniData() {
        /*
         * 收集收集数据
         */
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenDensity = dm.density;
    }


}
