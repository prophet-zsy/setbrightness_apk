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
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Chronometer;
import java.util.*;
import java.text.DecimalFormat;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;


public class SecondActivity extends AppCompatActivity {

    private int count = 0;
    private float brightness = (float) 0.0;
    private float stepBrightness = (float) 0.05;

    private TextView timerView;
    private long baseTimer;

    private enum Ops
    {
        SET_DARK,NEXT_BRIGHT,CONFIRM_FIT,ADD_BRIGHT,MINUS_BRIGHT   //注意这里可以没有分号
    }
    private String history = "";

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

//      计时器
        SecondActivity.this.baseTimer = SystemClock.elapsedRealtime();
        timerView = (TextView) this.findViewById(R.id.timerView);
        final Handler startTimehandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        if (null != timerView) {
                            timerView.setText((String) msg.obj);
                         }
                        return false;
                    }
                }
        );

        new Timer("开机计时器").scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int time = (int)(SystemClock.elapsedRealtime() - SecondActivity.this.baseTimer);
                String hh = new DecimalFormat("00").format(time / 3600000);
                String mm = new DecimalFormat("00").format(time % 3600000 / 60000);
                String ss = new DecimalFormat("00").format(time % 60000 / 1000);
                String ms = new DecimalFormat("000").format(time % 1000);
                String timeFormat = new String(hh + ":" + mm + ":" + ss + ":" + ms);
                Message msg = new Message();
                msg.obj = timeFormat;
                startTimehandler.sendMessage(msg);
            }

        }, 0, 20L);

        Button btn2 = (Button)findViewById(R.id.button2);  // 下一亮度
        Button btn3 = (Button)findViewById(R.id.button3);  // 设置灭屏

        Button btn = (Button)findViewById(R.id.button);  //  确认舒适
        ImageButton btn_min = (ImageButton)findViewById(R.id.btn_min);  //  减亮度
        ImageButton btn_add = (ImageButton)findViewById(R.id.btn_add);  //  加亮度

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record(Ops.CONFIRM_FIT);
            }
        });

        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBrightness(getSystemBrightness() - stepBrightness);
                record(Ops.MINUS_BRIGHT);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBrightness(getSystemBrightness() + stepBrightness);
                record(Ops.ADD_BRIGHT);
            }
        });

        if (BrightTime_i != -1) {
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);

            //        定时打开屏幕亮度
            final Handler brightnessHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int count = (int) msg.obj;
                    setBrightness(Brightness_f.get(count));
                    record(Ops.NEXT_BRIGHT);
                    return false;
                }
            }
            );

            new Timer("亮度设置计时器").scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int time = (int)(SystemClock.elapsedRealtime() - SecondActivity.this.baseTimer - DarkTime_i * 1000);
                    int count = time / (DarkTime_i + BrightTime_i) / 1000;
                    Message msg = new Message();
                    msg.obj = count;
                    brightnessHandler.sendMessage(msg);
                }

            }, DarkTime_i * 1000, (DarkTime_i + BrightTime_i) * 1000);

//        定时关闭屏幕亮度
            final Handler brightnessCloseHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    setBrightness(Float.valueOf(0));
                    record(Ops.SET_DARK);
                    int count = (int) msg.obj;
                    if (count >= Brightness_f.size()) {
//                    如果实验组数耗尽，则将亮度执行顺序序列化打包并发送至下一个页面
                        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                        String tem = "";
                        for (int i = 0; i < Brightness_f.size(); i ++) {
                            tem += Brightness_f.get(i).toString();
                            tem += ",";
                        }
                        intent.putExtra("Brightness_f", tem);
                        intent.putExtra("History", history);
                        startActivity(intent);
                    }
                    return false;
                }
            }
            );

            new Timer("亮度关闭计时器").scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int time = (int)(SystemClock.elapsedRealtime() - SecondActivity.this.baseTimer);
                    int count = time / (DarkTime_i + BrightTime_i) / 1000;
                    Message msg = new Message();
                    msg.obj = count;
                    brightnessCloseHandler.sendMessage(msg);
                }
            }, 0, (DarkTime_i + BrightTime_i) * 1000);
        } else {
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (count >= Brightness_f.size()) {
//                    如果实验组数耗尽，则将亮度执行顺序序列化打包并发送至下一个页面
                        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                        String tem = "";
                        for (int i = 0; i < Brightness_f.size(); i ++) {
                            tem += Brightness_f.get(i).toString();
                            tem += ",";
                        }
                        intent.putExtra("Brightness_f", tem);
                        intent.putExtra("History", history);
                        startActivity(intent);
                        return;
                    }

                    setBrightness(Float.valueOf(Brightness_f.get(count)));
                    record(Ops.NEXT_BRIGHT);
                    count ++;
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrightness(Float.valueOf(0));
                    record(Ops.SET_DARK);
                }
            });
        }
    }

    public void setBrightness(Float a) {
//        设置当前屏幕亮度
//        传入float值，范围0.0-1.0
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = a;
        window.setAttributes(lp);

        brightness = a;
        Log.i("seting", Float.toString(a));
        Log.i("geting", Float.toString(getSystemBrightness()));
    }

    private float getSystemBrightness() {
//        获取系统亮度并不及时
//        int systemBrightness = 0;
//        try {
//            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//        return systemBrightness / 255f;
        return brightness;
    }

    public void record(Ops op) {
        String timer = timerView.getText().toString();
        String brightness = String.valueOf(getSystemBrightness());
        history += op.name();
        history += ",";
        history += timer;
        history += ",";
        history += brightness;
        history += "\n";
//        记录一次，显示一次亮度
        EditText show_bright = (EditText)findViewById(R.id.show_bright);  //  显示亮度
        show_bright.setText(brightness);
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