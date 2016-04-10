package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2016/2/12.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.emotiv.insight.IEdk;
import com.emotiv.insight.IEmoStateDLL;
import com.emotiv.insight.MentalCommandDetection;
import com.unity3d.player.UnityPlayer;
import com.emotiv.insight.MentalCommandDetection;
import java.util.Timer;

import java.util.TimerTask;
import java.util.logging.LogRecord;

import getdata.*;

public class MentalController implements EngineInterface {
    EngineConnector engineConnector;
    Context context;
    int stage=0;
    boolean isTrainning = false;

    public MentalController(Context context){
        this.context=context;
        EngineConnector.setContext(context);
        //here is the problem
        engineConnector = EngineConnector.shareInstance();
        engineConnector.delegate = MentalController.this;

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextStage();
                //power();
            }
        }, 5000);

    }

    private void power(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("data af3",""+IEdk.IEE_GetAverageBandPowers(IEdk.IEE_DataChannel_t.IED_AF3));
                Log.d("data af4",""+IEdk.IEE_GetAverageBandPowers(IEdk.IEE_DataChannel_t.IED_AF4)[0]);
                Log.d("data t7",IEdk.IEE_GetAverageBandPowers(IEdk.IEE_DataChannel_t.IED_T7).toString());
                Log.d("data t8",IEdk.IEE_GetAverageBandPowers(IEdk.IEE_DataChannel_t.IED_T8).toString());
                Log.d("data Pz",IEdk.IEE_GetAverageBandPowers(IEdk.IEE_DataChannel_t.IED_Pz).toString());
            }
        }, 100, 100);
    }

    private void startTrain(IEmoStateDLL.IEE_MentalCommandAction_t command){
        if(!engineConnector.isConnected) {
            Toast.makeText(context, "You need to connect to your headset.", Toast.LENGTH_LONG).show();
            Log.d("Result00","not connect");
        }
        else{
            engineConnector.enableMentalcommandActions(command);
            isTrainning=engineConnector.startTrainingMetalcommand(isTrainning, command);
            //UnityPlayer.UnitySendMessage("Manager", "setStage",String.valueOf(stage));
        }
    }

    private void nextStage(){
        stage++;
        Log.d("Result00",""+stage);
        switch(stage){
            case 1:
                startTrain(IEmoStateDLL.IEE_MentalCommandAction_t.MC_NEUTRAL);
                break;
            case 3:
                startTrain(IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH);
                break;
        }
        UnityPlayer.UnitySendMessage("Manager", "setStage",String.valueOf(stage));
    }

    @Override
    public void trainStarted() {
        Log.d("Result00","train start");
    }

    @Override
    public void trainSucceed() {
        //UnityPlayer.UnitySendMessage("Manager", "trainStop","");
        nextStage();
        Log.d("Result00", "train success");
        engineConnector.setTrainControl(MentalCommandDetection.IEE_MentalCommandTrainingControl_t.MC_ACCEPT.getType());

    }

    @Override
    public void trainCompleted() {
        isTrainning=false;
        Log.d("Result00", "train complete");
        nextStage();
    }

    @Override
    public void trainRejected() {
        isTrainning=false;
    }

    @Override
    public void trainReset() {

    }

    @Override
    public void trainFailed(){
        Log.d("Result00", "train failed");
    }

    @Override
    public void trainErased() {

    }

    @Override
    public void userAdd(int userId) {

    }

    @Override
    public void userRemoved() {

    }

    @Override
    public void currentAction(int typeAction, float power) {
        switch(typeAction){
            case 1:
                UnityPlayer.UnitySendMessage("Manager", "commandMove","NEUTRAL");
                break;
            case 2:
                UnityPlayer.UnitySendMessage("Manager", "commandMove","PUSH");
                break;
        }

        Log.d("Result01","action"+typeAction+"  power"+power+" single1:"+ IEdk.IEE_GetInsightSignalStrength(0)+
                " single2:"+ IEdk.IEE_GetInsightSignalStrength(1)+
                " single3:"+ IEdk.IEE_GetInsightSignalStrength(2)+
                " single4:"+ IEdk.IEE_GetInsightSignalStrength(3)+
                " single5:"+ IEdk.IEE_GetInsightSignalStrength(4));
    }
}
