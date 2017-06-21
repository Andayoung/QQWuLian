package com.hk.zhouyuyin;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hk.zhouyuyin.fragment.PublicWebFragment;
import com.tencent.devicedemo.R;

public class PublicWebActivity extends FragmentActivity {

    private ImageButton backPublicWebActivity;
    private TextView tvTitle;

    private FragmentManager fm;

    private PublicWebFragment publicWebFragment;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_web_activitiy_layout);
        findId();
        click();
        init();
    }

    private void findId() {
        backPublicWebActivity = (ImageButton) findViewById(R.id.btn_back_public_web_activity);
        tvTitle = (TextView) findViewById(R.id.public_web_activity_title);
    }

    private void click() {
        backPublicWebActivity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void init() {
        Bundle bundle = this.getIntent().getExtras();
        webUrl = bundle.getString("webUrl");
        tvTitle.setText(bundle.getString("title"));
        fm = getSupportFragmentManager();
        publicWebFragment = new PublicWebFragment();
        publicWebFragment.setWebUrl(webUrl);
        initFagment(publicWebFragment);
    }

    @SuppressLint("Recycle")
    private void initFagment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.public_web_activity_content, fragment);
        transaction.commit();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        super.onPause();
        publicWebFragment.getWebview().reload();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            publicWebFragment.getWebview().onPause();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
