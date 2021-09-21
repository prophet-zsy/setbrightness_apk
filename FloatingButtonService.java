package com.example.glaring;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


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


public class FloatingButtonService extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private Button button, button2, button3;
    private TextView brightShow;

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
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 300;
        layoutParams.height = 550;
        layoutParams.x = 300;
        layoutParams.y = 300;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatingWindow(Intent intent) {
        //        接受intent传过来的参数
        Bundle bundle= intent.getExtras();
        String Brightness=bundle.getString("Brightness");
        String RepeatTimes=bundle.getString("RepeatTimes");
        String BrightTime=bundle.getString("BrightTime");
        String DarkTime=bundle.getString("DarkTime");
//      处理intent传过来的参数
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


        if (Settings.canDrawOverlays(this)) {

//            绘制悬浮窗内容
            LinearLayout layout = new LinearLayout(this); // 线性布局方式
            layout.setOrientation(LinearLayout.VERTICAL); //
            layout.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams LP_MM = new LinearLayout.LayoutParams(layoutParams.MATCH_PARENT, layoutParams.MATCH_PARENT);
            LP_MM.setMargins(0, 10, 0, 0);
            layout.setLayoutParams(LP_MM);

            timerView = new TextView(this);
            timerView.setText("00:00:00:000");
            timerView.setGravity(Gravity.CENTER);
            timerView.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams PARA1 = new LinearLayout.LayoutParams(300,80);
            timerView.setLayoutParams(PARA1);
            layout.addView(timerView);

            //      计时器
            baseTimer = SystemClock.elapsedRealtime();
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
                    int time = (int)(SystemClock.elapsedRealtime() - baseTimer);
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


            LinearLayout.LayoutParams PARA = new LinearLayout.LayoutParams(300,120);

            button = new Button(getApplicationContext());
            button.setText("设置灭屏");
            button.setGravity(Gravity.CENTER);
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setLayoutParams(PARA);
            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrightness(Float.valueOf(0));
                    record(Ops.SET_DARK);
                }
            });

            button2 = new Button(getApplicationContext());
            button2.setText("下一亮度");
            button2.setGravity(Gravity.CENTER);
            button2.setBackgroundColor(Color.TRANSPARENT);
            button2.setLayoutParams(PARA);
            layout.addView(button2);

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (count >= Brightness_f.size()) {
////                    如果实验组数耗尽，则将亮度执行顺序序列化打包并发送至下一个页面
//                        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
//                        String tem = "";
//                        for (int i = 0; i < Brightness_f.size(); i ++) {
//                            tem += Brightness_f.get(i).toString();
//                            tem += ",";
//                        }
//                        intent.putExtra("Brightness_f", tem);
//                        intent.putExtra("History", history);
//                        startActivity(intent);
//                        return;
//                    }
                    setBrightness(Float.valueOf(Brightness_f.get(count)));
                    record(Ops.NEXT_BRIGHT);
                    count ++;
                }
            });

            button3 = new Button(getApplicationContext());
            button3.setText("确认舒适");
            button3.setGravity(Gravity.CENTER);
            button3.setBackgroundColor(Color.TRANSPARENT);
            button3.setLayoutParams(PARA);
            layout.addView(button3);

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    record(Ops.CONFIRM_FIT);
                }
            });

            LinearLayout subLayout = new LinearLayout(this); // 线性布局方式
            subLayout.setOrientation(LinearLayout.HORIZONTAL); //
            subLayout.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(layoutParams.MATCH_PARENT, layoutParams.MATCH_PARENT);
            LP.height = 120;
            LP.width = 300;
            subLayout.setLayoutParams(LP);

            ImageButton btnMin = new ImageButton(this);
            LinearLayout.LayoutParams LPMin = new LinearLayout.LayoutParams(layoutParams.MATCH_PARENT, layoutParams.MATCH_PARENT);
            LPMin.height = 80;
            LPMin.width = 80;
            btnMin.setImageResource(R.mipmap.minus);
            btnMin.setLayoutParams(LPMin);
            subLayout.addView(btnMin);

            btnMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrightness(getSystemBrightness() - stepBrightness);
                    record(Ops.MINUS_BRIGHT);
                }
            });

            brightShow = new TextView(this);
            LinearLayout.LayoutParams LPbrightShow = new LinearLayout.LayoutParams(layoutParams.MATCH_PARENT, layoutParams.MATCH_PARENT);
            LPbrightShow.height = 120;
            LPbrightShow.width = 140;
            brightShow.setLayoutParams(LPbrightShow);
            brightShow.setTextSize((float) 13.5);
            brightShow.setGravity(Gravity.CENTER);
            brightShow.setText(Float.toString(keep2Num(getSystemBrightness())));
            subLayout.addView(brightShow);

            ImageButton btnAdd = new ImageButton(this);
            LinearLayout.LayoutParams LPAdd = new LinearLayout.LayoutParams(layoutParams.MATCH_PARENT, layoutParams.MATCH_PARENT);
            LPAdd.height = 80;
            LPAdd.width = 80;
            btnAdd.setImageResource(R.mipmap.add);
            btnAdd.setLayoutParams(LPAdd);
            subLayout.addView(btnAdd);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrightness(getSystemBrightness() + stepBrightness);
                    record(Ops.ADD_BRIGHT);
                }
            });

            layout.addView(subLayout);

            windowManager.addView(layout, layoutParams);

            layout.setOnTouchListener(new FloatingOnTouchListener());
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }


//    public void setBrightness(Float a) {
////        设置当前窗口屏幕亮度
////        传入float值，范围0.0-1.0
//        Window window = getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.screenBrightness = a;
//        window.setAttributes(lp);
//
//        brightness = a;
//        Log.i("seting", Float.toString(a));
//        Log.i("geting", Float.toString(getSystemBrightness()));
//    }

        public void setBrightness(Float a) {
//        设置系统屏幕亮度
//        传入float值，范围0.0-1.0

            int systemBrightness = (int) (a * 255);
            Log.i("seting", Integer.toString(systemBrightness));
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,systemBrightness);

            brightness = a;
//        Log.i("seting", Float.toString(a));
//        Log.i("geting", Float.toString(getSystemBrightness()));
    }

//    private void setBrightness(Float a){
//////        悬浮窗设置当前屏幕亮度
//////        传入float值，范围0.0-1.0
//        Log.i("############3", "#$###############");
////        int screenWidth = windowManager.getDefaultDisplay().getWidth();
////        int screenHeight = windowManager.getDefaultDisplay().getHeight();
//        int screenWidth = 50;
//        int screenHeight = 50;
//        LinearLayout mScreenLayout = new LinearLayout(this);
//        WindowManager.LayoutParams myLayoutParams = new WindowManager.LayoutParams();
//        myLayoutParams.x = 0;
//        myLayoutParams.y = 0;
//        myLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        myLayoutParams.format = PixelFormat.RGBA_8888;
//        myLayoutParams.width = screenWidth;
//        myLayoutParams.height = screenHeight;
//        myLayoutParams.screenBrightness = a;
//
//        windowManager.addView(mScreenLayout, myLayoutParams);
//        windowManager.removeView(mScreenLayout);
//        mScreenLayout = null;
//        myLayoutParams = null;
//    }

    private float getSystemBrightness() {
//        获取系统亮度并不及时
//        int systemBrightness = 0;
//        try {
//            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//        return systemBrightness / 255f;

        //            获取当前系统屏幕亮度
        int birght = -1;
        try {
            birght = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        float birghtNess = birght / 255f;
//        return brightness;  // 返回私有变量记录的值
        return birghtNess;
    }

    public void record(Ops op) {
        String timer = timerView.getText().toString();
        String brightness = String.valueOf(keep2Num(getSystemBrightness()));
        history += op.name();
        history += ",";
        history += timer;
        history += ",";
        history += brightness;
        history += "\n";
//        记录一次，显示一次亮度
        brightShow.setText(brightness);
    }

    public float keep2Num(float a) {
        Log.i("##########", Float.toString(a));
        float num = Math.round(a*100)/100f;
        Log.i("##########", Float.toString(num));
        return num;
    }
}