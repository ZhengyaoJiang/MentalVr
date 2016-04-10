package com.example.administrator.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.UserWords;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/2/12.
 */
public class VoiceController {
    private SpeechRecognizer mIat;
    static Context context;
    public VoiceController(Context context){
        VoiceController.context=context;
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=56a98895");
        mIat= SpeechRecognizer.createRecognizer(context, null);
        upLoadWords();
        listen();
    }
    private void upLoadWords(){
        UserWords uw=new UserWords();
        uw.putWord("大幅");
        uw.putWord("小幅");
        uw.putWord("灯光");
        uw.putWord("亮");
        uw.putWord("调亮");
        uw.putWord("暗");
        uw.putWord("调暗");
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf_8");
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        LexiconListener lexiconListener =new LexiconListener() {
            @Override
            public void onLexiconUpdated(String s, SpeechError speechError) {
                if(speechError==null)
                    Log.d("Result00", "上传成功");
                else
                    Log.d("Result00",speechError.toString());
            }
        };

        int ret = mIat.updateLexicon("userword",uw.toString(),lexiconListener);
        if(ret!= ErrorCode.SUCCESS){
            Log.d("ERRORUP","上传失败"+ret);
        }
    }

    private void listen() {
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        //mIat.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        //mIat.setParameter(SpeechConstant.SUBJECT, "asr");
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        RecognizerListener mySpeechListener = new RecognizerListener() {
            public void onVolumeChanged(int volume, byte[] bytes) {

            }
            @Override
            public void onBeginOfSpeech() {
                Log.d("Result:", "beginofspeech");
            }
            @Override
            public void onEndOfSpeech() {
                Log.d("Result:", "endofspeech");
            }

            @Override
            public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                String json = recognizerResult.getResultString();
                Matcher m = Pattern.compile("\"w\":\"(.|..|...|....)\"").matcher(json);
                ArrayList<String> words=new ArrayList<String>();
                while (m.find()) {
                    Log.d("Result00:", m.group(1) + " ");
                    words.add(m.group(1));
                }
                MeaningRecognizer mr=new MeaningRecognizer();
                mr.startRecognize(words);
                if(MainActivity.active)
                    mIat.startListening(this);
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.d("ERROR:", speechError.toString());
                if(speechError.getErrorCode()==10118&&MainActivity.active)
                    mIat.startListening(this);
            }

            public void onEvent(int eventType, int arg1, int srg2, Bundle obj) {
                Log.d("Result:", "ONEVENT");
            }
        };
        int i = mIat.startListening(mySpeechListener);
        Log.d("state", "startListening" + i);
    }
}
