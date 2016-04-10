package com.example.administrator.myapplication;

import android.util.Log;

import com.unity3d.player.UnityPlayer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to recognize the meaning of the voice
 */
public class MeaningRecognizer {
    private Action root;
    private Action current;
    private HashMap<String,Boolean> word_map=new HashMap<String,Boolean>();

    public MeaningRecognizer(){
        root =new RootAction();
    }

    private void InitializeMap(String... words){
        for(String word:words){
            word_map.put(word,false);
        }
    }

    public void startRecognize(ArrayList<String> input){
        InitializeMap("大幅", "亮", "暗", "灯光","调亮","调暗");
        for(String s:input){
            word_map.put(s,true);
        }
        current=root;
        WhileLoop:
        while(true){
            ArrayList<Action> children=current.getChildren();
            Log.d("Result001",current.getClass().toString());
            if(children.size()==0){
                current.execute();
                return;
            }
            for(Action a:children){
                if(judgeWords(a)){
                    Log.d("Result00",a.getClass().toString());
                    current=a;
                    continue WhileLoop;
                }
            }
            current.execute();
            return;
        }
    }

    private boolean judgeWords(Action a){
        if(a.getWords().size()==0)
            return true;
        for(ArrayList<String> combine:a.getWords()){
            for(String word:combine){
                if(!word_map.get(word))
                    return false;
            }
            return true;
        }
        return true;
    }
}

class Action{
    /**
     * each list of string represent a possibility combine of words that action can be activated
     */
    private ArrayList<ArrayList<String>> key_words=new ArrayList<ArrayList<String>>();
    private ArrayList<Action> children=new ArrayList<Action>();

    public void execute(){

    };

    protected void addKey_words(String... words){
        ArrayList<String> word_list=new ArrayList<String>();
        for(String word:words)
            word_list.add(word);
        key_words.add(word_list);
    }

    public ArrayList<Action> getChildren(){
        return children;
    }

    public void addChild(Action... actions){
        for(Action a:actions)
            children.add(a);
    }

    public ArrayList<ArrayList<String>> getWords(){
        return key_words;
    }
}

class RootAction extends Action{
    public RootAction(){
        addChild(new LightUp());
        addChild(new LightDown());
    }
}

class LightUp extends Action{
    public LightUp(){
        addKey_words("亮","灯光");
        addKey_words("调亮","灯光");
        addChild(new LightUpDrastically());
    }

    public void execute(){
        Log.d("Result00:", "LightUp");
        UnityPlayer.UnitySendMessage("Manager", "lightChange", "0.2");
        //new MentalController(VoiceController.context);
    }
}

class LightUpDrastically extends Action{
    public LightUpDrastically(){
        addKey_words("大幅");
    }

    public void execute(){
        Log.d("Result00:", "LightUpDrastically");
        UnityPlayer.UnitySendMessage("Manager", "lightChange", "0.5");
    }
}

class LightDown extends Action{
    public LightDown(){
        addKey_words("暗","灯光");
        addKey_words("调暗","灯光");
        addChild(new LightDownDrastically());
    }

    public void execute(){
        UnityPlayer.UnitySendMessage("Manager", "lightChange", "-0.2");
    }
}

class LightDownDrastically extends Action{
    public LightDownDrastically(){
        addKey_words("大幅");
    }

    public void execute(){
        UnityPlayer.UnitySendMessage("Manager", "lightChange", "-0.5");
    }
}

