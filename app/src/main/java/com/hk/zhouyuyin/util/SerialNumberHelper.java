package com.hk.zhouyuyin.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class SerialNumberHelper {
     final static String SERIAL_NUMBER="/storage/sdcard0/Android/data/serialNumber.txt";
    private Context context;

    public SerialNumberHelper() {
    }

    public SerialNumberHelper(Context context) {
        super();
        this.context = context;
    }

    public  void save2File(String text) {
        try {
            String path=context.getExternalCacheDir().getPath();
            //path=/storage/sdcard0/Android/data/com.tencent.devicedemo/cache
            Log.e("SerialNumber","path="+path);
            FileOutputStream output = new FileOutputStream(path+"/serialNumber.txt");
            output.write(text.getBytes("utf-8"));
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void saveLiFile(String text) {
        try {
            String path=context.getExternalCacheDir().getPath();
            //path=/storage/sdcard0/Android/data/com.tencent.devicedemo/cache
            Log.e("license","path="+path);
            FileOutputStream output = new FileOutputStream(path+"/license.txt");
            output.write(text.getBytes("utf-8"));
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  File makeDir(){
        File file = new File(SERIAL_NUMBER);
        if(!file.exists())
            file.mkdirs();
        return file;
    }

    public  String read4File() {
        StringBuilder sb = new StringBuilder("");
        try {
            FileInputStream input = new FileInputStream(context.getExternalCacheDir().getPath()+"/serialNumber.txt");
            byte[] temp = new byte[1024];
            int len = 0;
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public  String readLiFile() {
        StringBuilder sb = new StringBuilder("");
        try {
            FileInputStream input = new FileInputStream(context.getExternalCacheDir().getPath()+"/license.txt");
            byte[] temp = new byte[1024];
            int len = 0;
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public   boolean fileIsExist(String filePath) {
        if (filePath == null || filePath.length() < 1) {
            Log.e("S","ll " + filePath);
            return false;
        }
        File f = new File(filePath);
        if (!f.exists()) {
            return false;
        }
        return true;
    }

}