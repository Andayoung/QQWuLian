package com.hk.zhouyuyin;

import android.content.Context;
import android.util.Log;

import com.hk.zhouyuyin.db.DBManager;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.tencent.device.TXBinderInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class QQBinderReceiver extends XGPushBaseReceiver {
    private DBManager mgr;

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.e("ClassListReceiver", "title=" + xgPushTextMessage.getTitle() + ",content=" + xgPushTextMessage.getContent() + ",CustomContent=" + xgPushTextMessage.getCustomContent());
        //刷新界面！
        String content = xgPushTextMessage.getCustomContent();
        Log.e("onReceive", "content=" + content);
        JSONObject jClass = null;
        try {
            jClass = new JSONObject(content);
            JSONObject model = jClass.getJSONObject("model");
            if (mgr == null) {
                mgr = new DBManager(context);
            }
            mgr.update(model.getString("tinyid"), model.getString("nickname"));
            mgr.query();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        Log.e("ClassListReceiverx", "title=" + xgPushShowedResult.getTitle() + ",content=" + xgPushShowedResult.getContent());
    }


}