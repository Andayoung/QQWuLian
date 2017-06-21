package com.hk.zhouyuyin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class XunfeiYuyinHecheng {

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "nannan";
    // 语音合成对象
    private SpeechSynthesizer mTts;

    private SharedPreferences mSharedPreferences;

    // // 缓冲进度
    // private int mPercentForBuffering = 0;
    // // 播放进度
    // private int mPercentForPlaying = 0;

    public XunfeiYuyinHecheng(Context context) {
        super();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        mSharedPreferences = context.getSharedPreferences(
                "com.iflytek.setting", Context.MODE_PRIVATE);
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                // showTip("初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    public void start(String str) {
        // 设置参数
        setParam();

        int code = mTts.startSpeaking(str, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // 未安装则跳转到提示安装页面
            } else {
                // showTip("语音合成失败,错误码: " + code);
            }
        }
    }

    public void stop() {
        mTts.stopSpeaking();
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            // showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            // showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            // showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            // mPercentForBuffering = percent;
            // showTip(String.format(context.getString(R.string.tts_toast_format),
            // mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            // mPercentForPlaying = percent;
            // showTip(String.format(context.getString(R.string.tts_toast_format),
            // mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                // showTip("播放完成");
                // if (xunfeiYuyinShuruUtil != null) {
                // xunfeiYuyinShuruUtil.start();
                // }
                // if (xfmCU != null) {
                // xfmCU.SetAdapterizationBeam((byte)
                // uubqApplication.getMaikefengNo());
                // }
            } else if (error != null) {
                // showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            // if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            // String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            // Log.d(TAG, "session id =" + sid);
            // }
        }
    };

    /**
     * 参数设置
     *
     * @param param
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE,
                    SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            // 设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "70");
            // 设置合成音调
            mTts.setParameter(SpeechConstant.PITCH,
                    "40");
            // 设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME,
                    mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE,
                    SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE,
                mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
//				Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    private XunfeiYuyinShuruUtil xunfeiYuyinShuruUtil;

    public void setXunfeiYuyinShuruUtil(
            XunfeiYuyinShuruUtil xunfeiYuyinShuruUtil) {
        this.xunfeiYuyinShuruUtil = xunfeiYuyinShuruUtil;
    }

    public void destroy() {
        mTts.stopSpeaking();
        mTts.destroy();
    }

}
