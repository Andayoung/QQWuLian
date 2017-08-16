package com.hk.zhouyuyin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class SafeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.gg.safetymanagement.destroy")) {
            Log.e("SafeReceiver","startService");
           context.startService(new Intent(context,SafeService.class));
        }
    }
}
