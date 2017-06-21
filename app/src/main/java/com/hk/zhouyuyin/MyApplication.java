package com.hk.zhouyuyin;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import com.iflytek.cloud.SpeechUtility;

/**
 * Created by liangliang on 2017/5/8.
 */

public class MyApplication extends Application {
    /**
     * 屏幕的宽
     */
    public static int screenWidth;

    /**
     * 屏幕的高
     */
    public static int screenHeight;

    /**
     * 屏幕的密度
     */
    public static float screenDensity;

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(this, "appid=" + "55b98087");
        super.onCreate();
        iniData();


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
        //
    }


}
