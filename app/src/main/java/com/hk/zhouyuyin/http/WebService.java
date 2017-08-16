package com.hk.zhouyuyin.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hk.zhouyuyin.MyApplication;

/**
 * 连接服务�?
 *
 * @author Dachun Li
 */
public class WebService {
    // 服务器ip地址
    private final String TAG = "SERVICE";
    private String ip = "115.159.193.122";
    private String logMsg = "";
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private BufferedWriter writer;
    private InetSocketAddress isa = null;
    public static final int port = 7979;

    // 接收服务器信�?
    public String ReceiveMsg(Socket socket) throws IOException {
        if (socket.getInputStream() != null) {
            Log.e(TAG, "获取到输入流");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), "GBK"));
        String line;
        String txt = "";
        char[] temp = new char[800];
        reader.read(temp);
        txt = new String(temp).toString().trim();
        txt += "\n";
        Log.e(TAG, "获取到了：：" + txt);

        /**
         *
         * qq音乐获取点
         *
         */
        Log.e(TAG, "获取到了：：" + txt);
        if (txt.contains("MUSIC_NAME")) {
            String[] data1 = txt.split(" ");
            String data2 = data1[3];
            Log.e(TAG, "获取到了：：" + "music_name "+data2);
            if(!data2 .equals("{}}")){
                try{
                    String music_name_uncom = data2.substring(0,data2.length()-1);
                    String music_name = AnalysisMusicName.Analysis(music_name_uncom);
                    Log.e(TAG, "获取到了：：" + "music_name "+music_name);
                    ComponentName componentName = new ComponentName("com.gg.tiantianshouyin",
                            "com.gg.tiantianshouyin.qqmusic.SearchActivity");
                    final Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("music_name", music_name);
                    intent.putExtras(bundle);
                    intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    //必须要加这句，必须！！！！
                    MyApplication.getContext().startActivity(intent);
                }catch (ActivityNotFoundException e){
                }

            }
        }
        else {
            Log.e(TAG, "获取到了：：" + "还没有说想听什么");
        }
        /**
         *
         * qq音乐获取点，不用捕获listview的更新了，不像喜马拉雅那种获取方法一样，现在直接获取服务器返回的值。
         *
         */
        reader.close();
        return txt;
    }

    // 在向服务器发送消息之前，必须先链接到服务器
    public void connecttoserver() throws UnknownHostException, IOException {
        socket = RequestSocket(ip, port);
        // 判断是否链接成功
        if (socket.isConnected()) {
            Log.e(TAG, "服务器连接成功");
        } else {
            Log.e(TAG, "连接服务器失败，请检查原码");
        }
    }

    /**
     * 链接服务
     *
     * @param host 主机地址
     * @param port 端口
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    private Socket RequestSocket(String host, int port)
            throws UnknownHostException, IOException {
        Socket ConSocket = new Socket(); // 创建套接字地址
        isa = new InetSocketAddress(host, port); // 建立一个远程链接
        ConSocket.connect(isa);
        return ConSocket;
    }

    // 向服务器发送信息
    public void SendMsg(Socket socket, String msg) throws IOException {
        writer = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream(), "GBK"));
        int len = msg.getBytes("GBK").length + 8;
        String bit = stringFill(len + "", 8, '0', true);
        msg = bit + msg;
        Log.i("msg=========", msg);
        writer.write(msg);
        writer.flush();
    }

    /**
     * 字符串补�?
     *
     * @param source     源字符串
     * @param fillLength 补齐长度
     * @param fillChar   补齐的字符串
     * @param isLeftFill true为左补齐，false为右补齐
     * @return
     */
    public static String stringFill(String source, int fillLength,
                                    char fillChar, boolean isLeftFill) {
        if (source == null || source.length() >= fillLength)
            return source;

        StringBuilder result = new StringBuilder(fillLength);
        int len = fillLength - source.length();
        if (isLeftFill) {
            for (; len > 0; len--) {
                result.append(fillChar);
            }
            result.append(source);
        } else {
            result.append(source);
            for (; len > 0; len--) {
                result.append(fillChar);
            }
        }
        return result.toString();
    }

    /**
     * 转化字符串格式为GBK
     *
     * @param msg 目标字符
     */
    public String converMsgToGBK(String msg) {
        String message;
        if (msg == null) {
            return "";
        }
        try {
            byte[] bytesStr = msg.getBytes();
            message = new String(bytesStr, "UTF-8");

            return message;
        } catch (Exception ex) {
            Log.e(TAG, "catch a Exception!!");
            return msg;
        }
    }

}
