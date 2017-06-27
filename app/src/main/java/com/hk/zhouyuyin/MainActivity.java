package com.hk.zhouyuyin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hk.zhouyuyin.util.DateChoose;
import com.hk.zhouyuyin.util.SerialNumberHelper;
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
    TextView userBir;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.txt_go_reg)
    TextView txtGoReg;
    @BindView(R.id.zhuce)
    Button zhuce;
    @BindView(R.id.denglu)
    Button denglu;
    SerialNumberHelper serialNumberHelper;
    private String whichApp="";//1:myclass;2:habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_log);
        ButterKnife.bind(this);
        if (checkIsLogin()) {
            Intent intent = new Intent(MainActivity.this, OldMainActivity.class);
            startActivity(intent);
            finish();
        }

        Intent intent = getIntent();
        if(intent != null) {
            whichApp=intent.getStringExtra("appName");
        }
//        serialNumberHelper.makeDir();
//        serialNumberHelper.save2File("1234567890");
//        Log.e("MainActivity","num="+serialNumberHelper.read4File());

    }


    private boolean checkIsLogin() {
        if(serialNumberHelper==null){
            serialNumberHelper = new SerialNumberHelper(getApplicationContext());
        }
        String serialNumber=serialNumberHelper.read4File();
        Log.e("MainActivity", "serialNumber=" + serialNumber);
        if (serialNumber==null||serialNumber.equals("")) {
            return false;
        } else {
            return true;
        }

    }

    @OnClick({R.id.zhuce, R.id.denglu, R.id.txt_go_reg, R.id.user_sex, R.id.user_bir})
    public void submit(View btn) {
        if (btn.getId() == R.id.zhuce) {
            if (!userName.getText().toString().equals("") && !userPwd.getText().toString().equals("")
                    && !userBir.getText().toString().equals("") && !userSex.getText().toString().equals("")) {
                Log.e("OldMainActivity", "zhuce!");
                rigister();
            } else {
                Toast.makeText(MainActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
            }

        } else if (btn.getId() == R.id.denglu) {
            Log.e("OldMainActivity", "denglu!");
            if (!userName.getText().toString().equals("") && !userPwd.getText().toString().equals("")) {
                login();
            } else {
                Toast.makeText(MainActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
            }

        } else if (btn.getId() == R.id.txt_go_reg) {
            dlToZc();
        } else if (btn.getId() == R.id.user_bir) {
            createDateDialog();
        } else if (btn.getId() == R.id.user_sex) {
            createSexDialog();
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
                    String resultSuccess = new JSONObject(s).getString("success");
                    if (resultSuccess.equals("true")) {
                        JSONObject jsonResult = new JSONObject(s).getJSONObject("data");
                        if(serialNumberHelper==null){
                            serialNumberHelper = new SerialNumberHelper(getApplicationContext());
                        }
                        serialNumberHelper.makeDir();
                        serialNumberHelper.save2File(jsonResult.getString("serialNumber"));//"serialNumber":"20160222uu000003"
                        if(whichApp.equals("myclass")){
                            finish();
                        }else {
                            Intent intent = new Intent(MainActivity.this, OldMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                       
                    } else {
                        String faultMsg = new JSONObject(s).getString("msg");
                        Toast.makeText(MainActivity.this, faultMsg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("OldMainActivity", "login-error=" + volleyError);
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String md5Secret = null;
                try {
                    md5Secret = getMd5Secret(userPwd.getText().toString());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userName", userName.getText().toString());
                    map.put("passWord", md5Secret);
                    return map;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return null;
                }

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
                try {
                    String resultSuccess = new JSONObject(s).getString("success");
                    if (resultSuccess.equals("true")) {
                        zcToDl();
                    } else {
                        String faultMsg = new JSONObject(s).getString("msg");
                        Toast.makeText(MainActivity.this, faultMsg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("OldMainActivity", "rigister-error=" + volleyError);
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
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

    private void createSexDialog() {
        final String[] single_list = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择性别");
        int j = (userSex.getText() != null && userSex.getText().toString().equals("女")) ? 1 : 0;
        builder.setSingleChoiceItems(single_list, j, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = single_list[which];
                userSex.setText(str);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createDateDialog() {
        DateChoose dateDialog = new DateChoose(MainActivity.this, "2013年9月3日 14:44");
        dateDialog.dateTimePicKDialog(userBir);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}