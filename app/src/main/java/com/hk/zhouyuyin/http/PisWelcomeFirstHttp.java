package com.hk.zhouyuyin.http;

import java.io.IOException;

import com.hk.zhouyuyin.util.PlayMusic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class PisWelcomeFirstHttp {

    private Context context;

    public static final int SHOW_SEND = 1;
    public static final int SHOW_RECEIVED = 2;
    public static final int SHOW_FAIL = 3;
    // 发送的信息
    String sendMsg;
    // 接收的信息
    String recieveMsg;

    private TextView tvResult;

    WebService mService = new WebService();

    public PisWelcomeFirstHttp(Context context, TextView tvResult) {
        super();
        this.context = context;
        this.tvResult = tvResult;
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
     * 发送按钮监听
     */
    public void sendMessage() {
        sendMsg = "Pis_Welcome_First  {COMM_INFO {BUSI_CODE 10011} {REGION_ID A} {COUNTY_ID A00} {OFFICE_ID 22342} {OPERATOR_ID 43643} {CHANNEL A2} {OP_MODE SUBMIT}} {   {XW_OPENID "
                + getIMEINum() + "}  {CUSTOMER_ID 3836} }";
        Send send = new Send();
        send.start();
    }

    /**
     * 向服务器发送信息
     *
     * @param content
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
                    if (recieveMsg.equals("") && null != recieveMsg) {
                        recieveMsg = "服务器无返回消息";
                    }
                    Message message1 = new Message();
                    message1.what = SHOW_RECEIVED;
                    mHandler.sendMessage(message1);
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
                    getWav(recieveMsg);
                    break;
                case SHOW_FAIL:
                    Toast.makeText(context, "服务器繁忙", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void setMyTextView(String str) {
        tvResult.setText(str);
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

    // 播放本地录音
    private void getWav(String str) {
        String str1 = "";
        String[] s = spiltFuckText2(str);
        if (s != null) {
            str1 = s[1];
            PlayMusic playMusic = new PlayMusic(context);
            playMusic.playMusic("" + str1);
        } else {
            setMyTextView(getMyString(recieveMsg));
        }

    }

    // 获取返回内容
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

    // 如返回语音用于获取返回语音文件名称与本地语音文件对应。
    public String[] spiltFuckText2(String msg) {
        String[] Fuckresult = {"", ""};
        String lastt = "";
        String[] a = msg.split("WAV");
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
}
