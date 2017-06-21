package com.tencent.devicedemo;

import com.tencent.device.TXFriendInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NewFriendReqActivity extends Activity {
    private TXFriendInfo mFriendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_newfriendreq);

        Intent intent = getIntent();
        mFriendInfo = intent.getParcelableExtra("FriendInfo");


    }

    public void showdetail(View v) {

    }
}
