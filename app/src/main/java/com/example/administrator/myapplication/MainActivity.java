package com.example.administrator.myapplication;

import com.emotiv.bluetooth.EmotivBluetooth;
import com.google.unity.GoogleUnityActivity;
import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends GoogleUnityActivity {
    /** Called when the activity is first created. */
    // 声明控件
    private LinearLayout u3dLayout;
    public static boolean active=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        u3dLayout = (LinearLayout) findViewById(R.id.u3d_layout);
        u3dLayout.addView(mUnityPlayer);
        mUnityPlayer.requestFocus();
        //new VoiceController(this);
        new MentalController(this);
    }

    @Override
    protected void onResume() {
        active=true;
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
    @Override
    protected void onPause(){
        active=false;
        super.onPause();
        //mUnityPlayer.quit();
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
