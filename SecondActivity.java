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
import androidx.appcompat.app.AppCompatActivity;

import android.animation.FloatArrayEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import java.util.*;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;


public class SecondActivity extends AppCompatActivity {

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        Intent intent =getIntent();
        Bundle bundle= intent.getExtras();
        String Brightness=bundle.getString("Brightness");
        String RepeatTimes=bundle.getString("RepeatTimes");
        String BrightTime=bundle.getString("BrightTime");
        String DarkTime=bundle.getString("DarkTime");

        int RepeatTimes_i = Integer.valueOf(RepeatTimes);
        String[] strArray = Brightness.split(",");
        List<Float> Brightness_f = new ArrayList<Float>();
        for (int i = 0; i < strArray.length; i ++) {
            for (int j = 0; j < RepeatTimes_i; j ++) {
                Brightness_f.add(Float.valueOf(strArray[i]) / 100f);
            }
        }
        Collections.shuffle(Brightness_f);
        int BrightTime_i = Integer.valueOf(BrightTime);
        int DarkTime_i = Integer.valueOf(DarkTime);

//        int seconds = SecondActivity.getChronometerSeconds(chronometer);


        Chronometer chronometer =  (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();

        Button btn = (Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.screenBrightness = Float.valueOf(Brightness_f.get(count));
                window.setAttributes(lp);
                count ++;
                if (count >= Brightness_f.size()) {
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    String tem = "";
                    for (int i = 0; i < Brightness_f.size(); i ++) {
                        tem += Brightness_f.get(i).toString();
                        tem += ",";
                    }
                    intent.putExtra("Brightness_f", tem);
                    startActivity(intent);
                }
            }
        });

        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.screenBrightness = Float.valueOf(0) / 100f;
                window.setAttributes(lp);
            }
        });

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