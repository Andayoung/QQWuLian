package com.hk.zhouyuyin.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.hk.zhouyuyin.OldMainActivity;
import com.hk.zhouyuyin.PublicWebActivity;
import com.hk.zhouyuyin.util.XunfeiYuyinHecheng;

import java.io.IOException;

public class YuyinHttp {
    private Context context;

    public static final int SHOW_SEND = 1;
    public static final int SHOW_RECEIVED = 2;
    public static final int SHOW_FAIL = 3;

    private int count = -1;
    // 发送的信息
    String sendMsg;
    // 接收的信息
    String recieveMsg;

    WebService mService = new WebService();
    private String content;

    private OldMainActivity oldMainActivity;

    public YuyinHttp(Context context, String content, OldMainActivity oldMainActivity) {
        super();
        this.context = context;
        this.content = content;
        this.oldMainActivity = oldMainActivity;
    }

    /**
     * 发送按钮监听
     */
    public void sendMessage() {
        if (content.equals("") || content == null) {
            Toast.makeText(context, "请输入文字", Toast.LENGTH_SHORT).show();
        } else {
            String imei = getIMEINum();
            count = 1;
            sendMsg = "Pis_Get_App_Info {COMM_INFO {BUSI_CODE 10011} {REGION_ID A} {COUNTY_ID A00} {OFFICE_ID robot} {OPERATOR_ID 77777} {CHANNEL A2} {OP_MODE SUBMIT}} { {ROLE {1}} {WORDS {"
                    + content
                    + "}} {ROBOT_TYPE {0}} {XW_OPENID "
                    + imei
                    + "} {SELFID " + imei + "} {CUSTOMER_ID" + " 3836" + "}}";
            Send send = new Send();
            send.start();
        }
    }

    public void sendGPS(String LOC_X, String LOC_Y, String wz) {
        String imei = getIMEINum();
        if (LOC_X == null || LOC_X.equals("") || LOC_Y == null || LOC_Y.equals("")) {
            Toast.makeText(context, "GPS信号弱，请检查定位权限是否开启", Toast.LENGTH_SHORT).show();
        } else {
            String city = wz.substring(wz.indexOf("省") + 1, wz.indexOf("市"));
            count = 2;
            sendMsg = "Ssbon_Location_WX  {COMM_INFO {BUSI_CODE 10011} {REGION_ID A} {COUNTY_ID A00} {OFFICE_ID 22342} {OPERATOR_ID 43643} {CHANNEL A2} {OP_MODE SUBMIT}} { "
                    + "{XW_OPENID " + imei + "} "
                    + "{CITY " + city + "} "
                    + "{LOC_X " + LOC_X + "} "
                    + "{LOC_Y " + LOC_Y + "} "
                    + "{USER_ADDR " + wz + "} }";
            Send send = new Send();
            send.start();
        }
    }

    /**
     * 获取手机唯一识别imei码
     */
    private String getIMEINum() {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei != null) {
            if ("".equals(imei)) {
                imei = "358733050263717";
            }
        } else {
            imei = "358733050263717";
        }
        return imei;
    }

    /**
     * 向服务器发送信息
     */
    class Send extends Thread {

        public void run() {
            try {
                mService.connecttoserver();
                if (mService.getSocket().isConnected()) {
                    // 向服务器发送信息
                    mService.SendMsg(mService.getSocket(), sendMsg);
                    Message message = new Message();
                    message.what = SHOW_SEND;
                    mHandler.sendMessage(message);
                    // 接收服务器信息
                    recieveMsg = mService.ReceiveMsg(mService.getSocket());
                    Log.i("logct", recieveMsg);
                    if (recieveMsg.equals("") && null != recieveMsg) {
                        recieveMsg = "服务器无返回消息";
                    } else {
                        if (count == 1) {
                            Message message1 = new Message();
                            message1.what = SHOW_RECEIVED;
                            mHandler.sendMessage(message1);
                        }
                    }
//					Message message1 = new Message();
//					message1.what = SHOW_RECEIVED;
//					mHandler.sendMessage(message1);
                } else {
                    Message message2 = new Message();
                    message2.what = SHOW_FAIL;
                    mHandler.sendMessage(message2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_SEND:
                    // Toast.makeText(getActivity(), "已发送",
                    // Toast.LENGTH_LONG).show();
                    // String text = "发送的信息为：\n" + sendMsg;
                    break;
                case SHOW_RECEIVED:
                    //setData(getMyString(recieveMsg));
                    if (recieveMsg.equals("00000017处理失败!") || recieveMsg.contains("OSS异常")) {
                        Toast.makeText(oldMainActivity, "后台数据处理失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        jiexiwenzi(getMyString(recieveMsg));
                    }
                    Log.i("tag", recieveMsg);
//				Toast.makeText(oldMainActivity,recieveMsg, Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_FAIL:
                    Toast.makeText(context, "服务器繁忙", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    // private void setData(String str) {
    // tvResult.setText(str);
    //
    // }

    /*
     * 解析返回值
     */
    private void jiexiwenzi(String str) {
        if (isHaveWav(recieveMsg)) {

            toPublicWebActivty(getWavPath(recieveMsg));
            yuyinhecheng(str);
            com.hk.zhouyuyin.modle.Message message = new com.hk.zhouyuyin.modle.Message(
                    str, null, getWavPath(recieveMsg), true);
            oldMainActivity.updateList(message);
            com.hk.zhouyuyin.modle.Message messages = new com.hk.zhouyuyin.modle.Message(
                    str, null, null, true);
            oldMainActivity.updateList(messages);
        } else {
            String[] s = str.split("\\^-\\^");
            if (s.length == 3) {
                yuyinhecheng(s[0]);
                if (s[2].contains("图片")) {
                    toPublicWebActivty(s[1]);
                    Toast.makeText(context, "图片-->" + s[1], Toast.LENGTH_SHORT).show();
                } else if (s[2].contains("视频")) {
                    toPublicWebActivty(s[1]);
                    Toast.makeText(context, "视频-->" + s[1], Toast.LENGTH_SHORT).show();
                } else if (s[2].contains("音乐")) {
                    Intent intent = new Intent();
                    Uri uri = Uri.parse(s[1]);
                    Toast.makeText(context, "音乐-->" + s[1], Toast.LENGTH_SHORT).show();
                    intent.setDataAndType(uri, "audio/*");
                    intent.setAction(Intent.ACTION_VIEW);
                    context.startActivity(intent);
                }
            } else {
                yuyinhecheng(str);
                com.hk.zhouyuyin.modle.Message message = new com.hk.zhouyuyin.modle.Message(
                        str, null, null, true);
                oldMainActivity.updateList(message);
                Log.i("tag", message.getStrMessage());
            }
        }
    }

    private void toPublicWebActivty(String url) {
        Intent intentSimple = new Intent();
        intentSimple.setClass(context, PublicWebActivity.class);
        Bundle bundleSimple = new Bundle();
        bundleSimple.putString("webUrl", url);
        bundleSimple.putString("title", "视频");
        intentSimple.putExtras(bundleSimple);
        context.startActivity(intentSimple);
    }

    private void yuyinhecheng(String str) {
        XunfeiYuyinHecheng xunfeiYuyinHecheng = new XunfeiYuyinHecheng(context);
        xunfeiYuyinHecheng.start(str);
    }

    private String getMyString(String str) {
        String str1 = "";
        String[] s = spiltFuckText(str);
        if (s != null) {
            str1 = s[1];
        } else {
            str1 = "没有找到内容";
        }
        return str1;
    }

    public String[] spiltFuckText(String msg) {
        String[] Fuckresult = {"", ""};
        String lastt = "";
        String[] a = msg.split("ROBOT_OUTPUT");
        String last = "";
        if (a.length > 1) {
            String s = a[1];
            int index = s.indexOf("}");
            last = s.substring(0, index);
        }

        System.out.println(">>>>>>" + last);

        if (last.contains("{")) {
            int inde = last.indexOf("{");
            lastt = last.substring(inde + 1, last.length());
            System.out.println(lastt);
            Fuckresult[1] = lastt;
        } else {
            Fuckresult[1] = last;
        }
        Fuckresult[0] = "textpic";
        return Fuckresult;

    }

    private String getWavPath(String str) {
        String[] Fuckresult = {"", ""};
        if (str != null) {
            if (str.contains("WAV_PATH")) {
                String lastt = "";
                String[] a = str.split("WAV_PATH");
                String last = "";
                if (a.length > 1) {
                    String s = a[1];
                    int index = s.indexOf("}");
                    last = s.substring(0, index);
                }

                if (last.contains("{")) {
                    int inde = last.indexOf("{");
                    lastt = last.substring(inde + 1, last.length());
                    System.out.println(lastt);
                    Fuckresult[1] = lastt;
                } else {
                    Fuckresult[1] = last;
                }
                Fuckresult[0] = "textpic";
            }
        }
        return Fuckresult[1];
    }

    // 判断有没有音乐视频等。
    private boolean isHaveWav(String str) {
        if (!str.contains("WAV_PATH")) {
            return false;
        } else {
            return true;
        }
    }

}
