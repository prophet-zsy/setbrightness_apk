package com.example.glaring;

//import android.app.Activity;
//
//public class SecondActivity extends Activity {
//
//
//
//
//}

//import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ThirdActivity extends AppCompatActivity {

    private String savePath = Environment.getExternalStorageDirectory() + "/history.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        String Brightness = bundle.getString("Brightness_f");
        String History = bundle.getString("History");

        TextView btn = findViewById(R.id.textView6);
        btn.setText(Brightness);

        saveFile(History);
        TextView btn2 = findViewById(R.id.textView8);
        btn2.setText(savePath);

    }

    public boolean saveFile(String info){
        String path = savePath;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(info.getBytes());
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    /**
//     *
//     * @param cmt  Chronometer控件
//     * @return 小时+分钟+秒数  的所有秒数
//     */
//    public  static int getChronometerSeconds(Chronometer cmt) {
//        int totalss = 0;
//        String string = cmt.getText().toString();
//        if(string.length()==7){
//
//            String[] split = string.split(":");
//            String string2 = split[0];
//            int hour = Integer.parseInt(string2);
//            int Hours =hour*3600;
//            String string3 = split[1];
//            int min = Integer.parseInt(string3);
//            int Mins =min*60;
//            int  SS =Integer.parseInt(split[2]);
//            totalss = Hours+Mins+SS;
//            return totalss;
//        }
//
//        else if(string.length()==5){
//
//            String[] split = string.split(":");
//            String string3 = split[0];
//            int min = Integer.parseInt(string3);
//            int Mins =min*60;
//            int  SS =Integer.parseInt(split[1]);
//
//            totalss =Mins+SS;
//            return totalss;
//        }
//        return totalss;
//
//
//    }


}

























//package com.example.glaring;
//
////import android.app.Activity;
////
////public class SecondActivity extends Activity {
////
////
////
////
////}
//
////import android.support.v7.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.animation.FloatArrayEvaluator;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Chronometer;
//import java.util.*;
//
//
//public class SecondActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_experiment);
//
//        Intent intent =getIntent();
//        Bundle bundle= intent.getExtras();
//        String Brightness=bundle.getString("Brightness");
//        String RepeatTimes=bundle.getString("RepeatTimes");
//        String BrightTime=bundle.getString("BrightTime");
//        String DarkTime=bundle.getString("DarkTime");
//
//        int RepeatTimes_i = Integer.valueOf(RepeatTimes);
//        String[] strArray = Brightness.split(",");
//        List<Float> Brightness_f = new ArrayList<Float>();
//        for (int i = 0; i < strArray.length; i ++) {
//            for (int j = 0; j < RepeatTimes_i; j ++) {
//                Brightness_f.add(Float.valueOf(strArray[i]) / 100f);
//            }
//        }
//        Collections.shuffle(Brightness_f);
//        int BrightTime_i = Integer.valueOf(BrightTime);
//        int DarkTime_i = Integer.valueOf(DarkTime);
//
////        int seconds = SecondActivity.getChronometerSeconds(chronometer);
//
//        Handler handler = new Handler(new Handler.Callback() {
//
//            public int times = 0;
//            @Override
//            public boolean handleMessage(Message msg) {
//                Window window = getWindow();
//                WindowManager.LayoutParams lp = window.getAttributes();
//                lp.screenBrightness = Float.valueOf(avalue) / 100f;
//                window.setAttributes(lp);
//                times ++;
//                return false;
//            }
//        });
//
//        class TimeTask extends TimerTask{
//            public TimeTask(){
//            }
//            @Override
//            public void run() {
//
//                Message msg = new Message();
//                msg.what = 1;
//                Bundle bundle = new Bundle();
//                bundle.putFloatArray("Brightness_f", (FloatArray)Brightness_f);
//                msg.setData(bundle);//mes利用Bundle传递数据
//                handler.sendMessage(msg);//用activity中的handler发送消息
//
//                Message msg = handler.obtainMessage(0);
//                msg.sendToTarget();
//            }
//        }
//
//        Timer timer = new Timer(true);
//        timer.schedule(new TimeTask(), 0, (DarkTime_i + BrightTime_i) * 1000);
//
//        Chronometer chronometer =  (Chronometer) findViewById(R.id.chronometer);
//        chronometer.start();
//    }
//
////    /**
////     *
////     * @param cmt  Chronometer控件
////     * @return 小时+分钟+秒数  的所有秒数
////     */
////    public  static int getChronometerSeconds(Chronometer cmt) {
////        int totalss = 0;
////        String string = cmt.getText().toString();
////        if(string.length()==7){
////
////            String[] split = string.split(":");
////            String string2 = split[0];
////            int hour = Integer.parseInt(string2);
////            int Hours =hour*3600;
////            String string3 = split[1];
////            int min = Integer.parseInt(string3);
////            int Mins =min*60;
////            int  SS =Integer.parseInt(split[2]);
////            totalss = Hours+Mins+SS;
////            return totalss;
////        }
////
////        else if(string.length()==5){
////
////            String[] split = string.split(":");
////            String string3 = split[0];
////            int min = Integer.parseInt(string3);
////            int Mins =min*60;
////            int  SS =Integer.parseInt(split[1]);
////
////            totalss =Mins+SS;
////            return totalss;
////        }
////        return totalss;
////
////
////    }
//
//
//}