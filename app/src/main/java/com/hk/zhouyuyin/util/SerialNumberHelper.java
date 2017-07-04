package com.hk.zhouyuyin.util;

import android.content.Context;
import android.os.Environment;
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
    private Context context;

    public SerialNumberHelper() {
    }

    public SerialNumberHelper(Context context) {
        super();
        this.context = context;
    }

    public void save2File(String text) {
        try {

            File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if(!path.mkdirs()) {
                Log.e("licence", "Directory not created");
            }
            File file=new File(path,"serialNumber.txt");
            FileOutputStream output = new FileOutputStream(file);
//            String path = context.getExternalCacheDir().getPath();
//            String path ="/storage/sdcard0/Android/data/com.tencent.devicedemo/cache";
//            Log.e("SerialNumber", "path=" + path);
//            FileOutputStream output = new FileOutputStream(path + "/serialNumber.txt");
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

    public void saveLiFile(String text) {
        try {
//            String path = context.getExternalCacheDir().getPath();
            ///storage/sdcard0/Android/data/com.gg.classlist/files
//            String path ="/storage/sdcard0/Android/data/com.tencent.devicedemo/cache";
//            String path="/storage/sdcard0/Android/data/com.tencent.devicedemo/files";
//            Log.e("license", "path=" + path);
            File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if(!path.mkdirs()) {
                Log.e("licence", "Directory not created");
            }
            File file=new File(path,"license.txt");
            FileOutputStream output = new FileOutputStream(file);
//            FileOutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath() + "/license.txt");
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

    public String read4File() {
        StringBuilder sb = new StringBuilder("");
        try {
            File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if(!path.mkdirs()) {
                Log.e("licence", "Directory not created");
            }
            File file=new File(path, "serialNumber.txt");
            FileInputStream input = new FileInputStream(file);
//            FileInputStream input = new FileInputStream(context.getExternalCacheDir().getPath() + "/serialNumber.txt");
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

    public String readLiFile() {
        StringBuilder sb = new StringBuilder("");
        try {
            File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if(!path.mkdirs()) {
                Log.e("licence", "Directory not created");
            }
            File file=new File(path, "license.txt");
            FileInputStream input = new FileInputStream(file);
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

}