package com.hk.zhouyuyin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hk.zhouyuyin.util.SerialNumberHelper;
import com.tencent.device.TXBinderInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class DBManager {
    private DBsqliteHelper helper;
    private Context context;
    private final static String SEND_TO_QQ = "http://192.168.124.27:8080/znsb/QQwlBinders/bindersAddOrDel.do";

    public DBManager(Context context) {
        helper = new DBsqliteHelper(context);
        this.context = context;
    }

    public void update(String tinyid, String nickname) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nickname", nickname);
        String whereClause = "tinyid=?";
        String[] whereArgs = {tinyid};
        db.update("binderz", values, whereClause, whereArgs);
    }

    public void addForOne(TXBinderInfo txBinderInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO binderz VALUES(null,?,?,?,?)", new Object[]{txBinderInfo.tinyid, new String(txBinderInfo.nick_name), txBinderInfo.binder_type, txBinderInfo.head_url});
        Log.e("DBManager", "添加成功");
        db.close();
    }

    public void addAll(List<TXBinderInfo> txBinderInfos) {
        SQLiteDatabase db = helper.getWritableDatabase();
        for (TXBinderInfo t : txBinderInfos) {
            db.execSQL("INSERT INTO binderz VALUES(null,?,?,?,?)", new Object[]{t.tinyid, new String(t.nick_name), t.binder_type, t.head_url});
            Log.e("DBManager", "添加成功");
        }
        db.close();
    }

    public void deleteForNickName(String tinyid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from binderz where tinyid = " + tinyid);
        Log.e("DBManager", "删除成功");
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from binderz");
        Log.e("DBManager", "删除成功");
        db.close();
    }


    public TXBinderInfo queryForId(String nick_name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query("binderz", null, "nickname=?", new String[]{nick_name}, null, null, null, null);
        while (c.moveToNext()) {
            TXBinderInfo txBinderInfo = new TXBinderInfo(c.getLong(c.getColumnIndex("tinyid"))
                    , c.getString(c.getColumnIndex("nickname"))
                    , c.getInt(c.getColumnIndex("bindertype"))
                    , c.getString(c.getColumnIndex("headurl")));
            return txBinderInfo;
        }
        c.close();
        db.close();
        return null;
    }

    public List<TXBinderInfo> query() {
        ArrayList<TXBinderInfo> txBinderInfos = new ArrayList<TXBinderInfo>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM binderz", null);
        while (c.moveToNext()) {
            Log.e("DDD", "q=" + c.getLong(c.getColumnIndex("tinyid")) + c.getString(c.getColumnIndex("nickname"))
                    + c.getInt(c.getColumnIndex("bindertype"))
                    + c.getString(c.getColumnIndex("headurl")));
            TXBinderInfo txBinderInfo = new TXBinderInfo(c.getLong(c.getColumnIndex("tinyid"))
                    , c.getString(c.getColumnIndex("nickname"))
                    , c.getInt(c.getColumnIndex("bindertype"))
                    , c.getString(c.getColumnIndex("headurl")));
            txBinderInfos.add(txBinderInfo);
        }
        c.close();
        db.close();
        return txBinderInfos;
    }

    public void deleteOtherNickName(List<TXBinderInfo> list) {
        if (list == null) {
            Log.e("DDDD", "deleteOtherNickName list is null");
            return;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM binderz", null);
        while (c.moveToNext()) {
            int i = 0;
            for (TXBinderInfo t : list) {
                i = 0;
                Log.e("TXDeviceService", "c=" + c.getString(c.getColumnIndex("nickname")));
                if (c.getString(c.getColumnIndex("tinyid")).equals(String.valueOf(t.tinyid))) {
                    i = 1;
                    break;
                }
            }
            if (i == 0) {
                deleteForNickName(c.getString(c.getColumnIndex("nickname")));
                sendToQQ(context, "del", String.valueOf(c.getString(c.getColumnIndex("tinyid"))), c.getString(c.getColumnIndex("nickname")), String.valueOf(c.getInt(c.getColumnIndex("bindertype"))), c.getString(c.getColumnIndex("headurl")));
                Log.e("DDD", "deleteOtherNickName " + c.getString(c.getColumnIndex("nickname")));
            }
        }
        c.close();
        db.close();
    }

    public void addOtherNickName(List<TXBinderInfo> list) {
        if (list == null) {
            Log.e("DDDD", "addOtherNickName list is null");
            return;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM binderz", null);
        for (TXBinderInfo t : list) {
            int i = 0;

            while (c.moveToNext()) {
                i = 0;
                if (c.getString(c.getColumnIndex("tinyid")).equals(String.valueOf(t.tinyid))) {
                    i = 1;
                    break;
                }
            }
            if (i == 0) {
                addForOne(t);
                sendToQQ(context, "add", String.valueOf(t.tinyid), new String(t.nick_name), String.valueOf(t.binder_type), t.head_url);
                Log.e("DDD", "addOtherNickName " + t.toString());

            }
        }
        c.close();
        db.close();

    }

    private void sendToQQ(final Context context, final String addOrdel, final String tinyid, final String nickname, final String bindertype, final String headurl) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_TO_QQ, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("MainActivity", "login-s=" + s);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("MainActivity", "login-error=" + volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SerialNumberHelper serialNumberHelper = new SerialNumberHelper(context);
                String serialNumber = serialNumberHelper.read4File();
                if (serialNumber == null || serialNumber.equals("")) {
                    return  null;
                }else {
                    String[] s = serialNumber.split(" ");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("addOrdel", addOrdel);
                    map.put("nickname", nickname);
                    map.put("tinyid", tinyid);
                    map.put("bindertype", bindertype);
                    map.put("headurl", headurl);
                    map.put("serialNumber", s[0]);
                    return map;
                }
            }
        };
        mQueue.add(stringRequest);
    }
}
