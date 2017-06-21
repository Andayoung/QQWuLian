package com.hk.zhouyuyin.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

public class PlayMusic {

    private Context context;
    private MediaPlayer mp;

    public PlayMusic(Context context) {
        super();
        this.context = context;
    }

    public void playMusic(String filePath) {
        AssetManager assetManager = context.getAssets();
        try {
            mp = new MediaPlayer();
            AssetFileDescriptor fileDescriptor = assetManager.openFd(filePath.replace(" ", ""));
            mp.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mp.setVolume(1.0f, 1.0f);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
