package com.hk.zhouyuyin;

import java.util.ArrayList;
import java.util.List;

import com.hk.zhouyuyin.adapter.DrawAdapter;
import com.hk.zhouyuyin.http.YuyinHttp;
import com.hk.zhouyuyin.modle.Message;
import com.hk.zhouyuyin.util.PhoneInfo;
import com.hk.zhouyuyin.util.XunfeiYuyinShuruUtil;
import com.tencent.av.VideoController;
import com.tencent.device.TXBinderInfo;
import com.tencent.device.TXDeviceService;
import com.tencent.devicedemo.R;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class OldMainActivity extends Activity implements TencentLocationListener {

    private EditText edtContent;

    private ImageView imgVoiceOrKeyboard, imgSend;
    private LinearLayout llVoice;
    private ImageView imgVOice;

    private ListView lvMessage;

    private DrawAdapter drawAdapter;
    private List<Message> messages;

    private NotifyReceiver mNotifyReceiver;

    private List<TXBinderInfo> binderList = null;

    private boolean isVoice = false;
    private PhoneInfo info;

    private Handler mHandler;
    private HandlerThread mThread;

    private TencentLocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mThread = new HandlerThread("Thread_demo_" + (int) (Math.random() * 10));
        mThread.start();
        mHandler = new Handler(mThread.getLooper());
        initQQ();

        mLocationManager = TencentLocationManager.getInstance(this);
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        info = new PhoneInfo(this);
        initView();
        startGPS();
        initClick();
        initData();

    }


    @Override
    protected void onResume() {
        super.onResume();

        TXBinderInfo[] arrayBinder = TXDeviceService.getBinderList();
        if (arrayBinder != null) {
            binderList = new ArrayList<TXBinderInfo>();
            for (int i = 0; i < arrayBinder.length; ++i) {
                binderList.add(arrayBinder[i]);
            }
        }
    }

    private void initQQ() {
        Intent startIntent = new Intent(this, TXDeviceService.class);
        startService(startIntent);

        IntentFilter filter = new IntentFilter();
        filter.addAction(TXDeviceService.BinderListChange);
        filter.addAction(TXDeviceService.OnEraseAllBinders);
        mNotifyReceiver = new NotifyReceiver();
        registerReceiver(mNotifyReceiver, filter);
    }

    public void initGPS(String LOC_X, String LOC_Y, String wz) {
        YuyinHttp yuyinHttp = new YuyinHttp(this, "", this);
        yuyinHttp.sendGPS(LOC_X, LOC_Y, wz);
    }

    private void initView() {
        edtContent = (EditText) findViewById(R.id.edt_content);
        imgVoiceOrKeyboard = (ImageView) findViewById(R.id.img_voice_keyboard);
        imgSend = (ImageView) findViewById(R.id.img_mian_send);
        llVoice = (LinearLayout) findViewById(R.id.ll_is_voice);
        imgVOice = (ImageView) findViewById(R.id.img_voice);
        lvMessage = (ListView) findViewById(R.id.lv_content);

    }

    private void initData() {
        // PisWelcomeFirstHttp pisWelcomeFirstHttp = new
        // PisWelcomeFirstHttp(this,
        // tvResult);
        // pisWelcomeFirstHttp.sendMessage();
        messages = new ArrayList<Message>();
        drawAdapter = new DrawAdapter(this, messages);
        lvMessage.setAdapter(drawAdapter);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void updateList(Message message) {
        messages.add(message);
        drawAdapter.notifyDataSetChanged();
        lvMessage.setSelection(drawAdapter.getCount() - 1);
    }

    private void initClick() {
        imgSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getResult();
            }
        });
        imgVoiceOrKeyboard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isVoice = !isVoice;
                if (!isVoice) {
                    show(OldMainActivity.this);
                    imgVoiceOrKeyboard.setImageResource(R.drawable.uu_25);
                    llVoice.setVisibility(View.VISIBLE);
                } else {
                    hideSoftInputView(arg0);
                    llVoice.setVisibility(View.GONE);
                    imgVoiceOrKeyboard.setImageResource(R.drawable.uu_03);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        imgVOice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startVoice();
            }
        });
    }

    private void startVoice() {
        if (binderList != null && binderList.size() > 0) {
            XunfeiYuyinShuruUtil xunfeiYuyinShuruUtil = new XunfeiYuyinShuruUtil(this, binderList.get(0).tinyid, binderList.get(0).binder_type);
            xunfeiYuyinShuruUtil.start();
        } else {
            XunfeiYuyinShuruUtil xunfeiYuyinShuruUtil = new XunfeiYuyinShuruUtil(this, -1, -1);
            xunfeiYuyinShuruUtil.start();
//
        }
    }

    private void getResult() {
        String msg = edtContent.getText().toString();
        if (msg == null || msg.equals("")) {
            Toast.makeText(OldMainActivity.this, "输入不能为空!", Toast.LENGTH_SHORT).show();
        } else {
            if (msg.equals("我要找妈妈")) {
                if (OldMainActivity.isNetworkAvailable(this)) {
                    if (binderList != null && binderList.size() > 0) {
                        if (false == VideoController.getInstance().hasPendingChannel()) {
                            edtContent.setText("");
                            TXDeviceService.getInstance().startAudioChatActivity(binderList.get(0).tinyid, binderList.get(0).binder_type);
                        } else {
                            Toast.makeText(OldMainActivity.this.getApplicationContext(), "语音中", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "测试阶段请绑定指定设备!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OldMainActivity.this.getApplicationContext(), "当前网络不可用，请连接网络", Toast.LENGTH_SHORT).show();
                }
            } else {
                Message m = new Message(msg, null, null, false);
                updateList(m);
                YuyinHttp yuyinHttp = new YuyinHttp(this, edtContent.getText().toString(), this);
                yuyinHttp.sendMessage();
                edtContent.setText("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mNotifyReceiver);
        // 退出 activity 前一定要停止定位!
        stopGPS();
        // 清空
        mHandler.removeCallbacksAndMessages(null);
        // 停止线程
        mThread.getLooper().quit();
    }

    private void stopGPS() {
        mLocationManager.removeUpdates(this);
    }

    private void startGPS() {
        // 创建定位请求
        final TencentLocationRequest request = TencentLocationRequest.create();
        // 修改定位请求参数, 定位周期 3000 ms
        request.setInterval(3000);

        // 在 mThread 线程发起定位
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mLocationManager.requestLocationUpdates(request, OldMainActivity.this);
            }
        });
    }

    @Override
    public void onLocationChanged(final TencentLocation location, int error, String arg2) {
        // TODO Auto-generated method stub
        String msg = null;

        if (error == TencentLocation.ERROR_OK) {
            // 当前线程名字
            String threadName = Thread.currentThread().getName();
            initGPS(location.getLongitude() + "", location.getLatitude() + "", location.getAddress());
            OldMainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    // Log.i("tag", "GPS信号弱，请到开阔地带获取位置信息");
                    Toast.makeText(OldMainActivity.this, "定位成功，当前位置:" + location.getAddress(), Toast.LENGTH_SHORT).show();
                }
            });
            stopGPS();
        } else {
            // Log.i("tag", "GPS信号弱，请到开阔地带获取位置信息");
            // edtContent.post(new Runnable() {
            //
            // @Override
            // public void run() {
            // // TODO Auto-generated method stub
            // edtContent.setText("GPS信号弱，请到开阔地带获取位置信息");
            // }
            // });
            OldMainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    // Log.i("tag", "GPS信号弱，请到开阔地带获取位置信息");
                    Toast.makeText(OldMainActivity.this, "GPS信号弱，请检查定位权限是否开启", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onStatusUpdate(String arg0, int arg1, String arg2) {
        // TODO Auto-generated method stub

    }

    //打开软键盘
    public void show(Context context) {
        // 打开软键盘(如果输入法在窗口上已经显示，则隐藏，反之则显示)
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //隐藏软件盘
    public void hideSoftInputView(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    public class NotifyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == TXDeviceService.BinderListChange) {
                try {
                    Parcelable[] listTemp = intent.getExtras().getParcelableArray("binderlist");
                    binderList = new ArrayList<TXBinderInfo>();
                    for (int i = 0; i < listTemp.length; ++i) {
                        TXBinderInfo binder = (TXBinderInfo) (listTemp[i]);
                        binderList.add(binder);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (cm.getActiveNetworkInfo() != null) {
                return cm.getActiveNetworkInfo().isAvailable();
            }
        }
        return false;
    }
}