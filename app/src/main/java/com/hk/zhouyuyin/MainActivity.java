package com.hk.zhouyuyin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.devicedemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class MainActivity extends Activity {
    public static final String ZHUCE_URL = "http://192.168.124.27:8080/znsb/QQwlZnsbUser/register.do";
    public static final String DENGLU_URL = "http://192.168.124.27:8080/znsb/QQwlZnsbUser/login.do";
    @BindView(R.id.img_log)
    ImageView imgLog;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.user_pwd)
    EditText userPwd;
    @BindView(R.id.user_bir)
    EditText userBir;
    @BindView(R.id.user_sex)
    EditText userSex;
    @BindView(R.id.txt_go_reg)
    TextView txtGoReg;
    @BindView(R.id.zhuce)
    Button zhuce;
    @BindView(R.id.denglu)
    Button denglu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_log);
        ButterKnife.bind(this);
        if ( checkIsLogin()) {
            Intent intent=new Intent(MainActivity.this, OldMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
    private boolean checkIsLogin() {
        SharedPreferences pre = getSharedPreferences("dataz", MODE_PRIVATE);
        if(pre.getString("serialNumber","").equals("")){
            return false;
        }else {
            return true;
        }

    }
    @OnClick({R.id.zhuce, R.id.denglu, R.id.txt_go_reg})
    public void submit(View btn) {
        if (btn.getId() == R.id.zhuce) {
            Log.e("OldMainActivity", "zhuce!");
            rigister();
        } else if (btn.getId() == R.id.denglu) {
            Log.e("OldMainActivity", "denglu!");
            login();
        } else if (btn.getId() == R.id.txt_go_reg) {
            dlToZc();
        }
    }

    private void dlToZc() {
        imgLog.setVisibility(View.GONE);
        userSex.setVisibility(View.VISIBLE);
        userBir.setVisibility(View.VISIBLE);
        denglu.setVisibility(View.GONE);
        txtGoReg.setVisibility(View.GONE);
        zhuce.setVisibility(View.VISIBLE);
    }

    private void zcToDl() {
        imgLog.setVisibility(View.VISIBLE);
        userSex.setVisibility(View.GONE);
        userBir.setVisibility(View.GONE);
        denglu.setVisibility(View.VISIBLE);
        txtGoReg.setVisibility(View.VISIBLE);
        zhuce.setVisibility(View.GONE);
    }

    private void login() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DENGLU_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("OldMainActivity", "login-s=" + s);
                try {
                    JSONObject jsonData = new JSONObject(s);
                    JSONObject jsonResult = jsonData.getJSONObject("data");
                    Log.e("OldMainActivity", "jsonResult" + jsonResult + ",serialNumber=" + jsonResult.getString("jsonResult"));
                    SharedPreferences.Editor editor = getSharedPreferences("dataz", MODE_PRIVATE).edit();
                    editor.putString("serialNumber", jsonResult.getString("jsonResult"));//"serialNumber":"20160222uu000003"
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("OldMainActivity", "login-error=" + volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userName", userName.getText().toString());
                map.put("passWord", userPwd.getText().toString());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    private void rigister() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ZHUCE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("OldMainActivity", "rigister-s=" + s);
                zcToDl();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("OldMainActivity", "rigister-error=" + volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                try {
                    String md5Secret = getMd5Secret(userPwd.getText().toString());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userName", userName.getText().toString());
                    map.put("passWord", md5Secret);
                    map.put("sex", userSex.getText().toString());
                    map.put("birthDate", userBir.getText().toString());
                    return map;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        mQueue.add(stringRequest);
    }

    public String getMd5Secret(String pwd) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] cipherData = md5.digest(pwd.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte cipher : cipherData) {
            String toHexStr = Integer.toHexString(cipher & 0xff);
            builder.append(toHexStr.length() == 1 ? "0" + toHexStr : toHexStr);
        }
        Log.e("MainActivity", "加密=" + builder.toString());
        return builder.toString();
        //c0bb4f54f1d8b14caf6fe1069e5f93ad
    }
}