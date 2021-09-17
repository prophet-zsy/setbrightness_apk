package com.example.glaring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    // private ImageView welcomeImg = null;
    private static final int PERMISSION_REQUEST = 1;
    // 检查权限

    private boolean sysControl = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText brightness = (EditText)findViewById(R.id.editTextTextPersonName);
        EditText repeatTimes = (EditText)findViewById(R.id.editTextTextPersonName2);
        RadioGroup radiogroup = (RadioGroup)findViewById(R.id.radiogroup1);
        RadioButton radio2 = (RadioButton) findViewById(R.id.radiobutton2);  // 系统控制亮度
        RadioButton radio1 = (RadioButton) findViewById(R.id.radiobutton1);  // 手动控制亮度
        EditText brightTime = (EditText)findViewById(R.id.editTextTextPersonName3);
        EditText darkTime = (EditText)findViewById(R.id.editTextTextPersonName4);
        TextView clueDarkTime  = (TextView)findViewById(R.id.textView4);  // 提示文本
        TextView clueBrightnessTime  = (TextView)findViewById(R.id.textView3);   // 提示文本

        radiogroup.check(radio2.getId());  // 默认系统控制
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radio2.getId()) {  // 系统控制亮度
                    sysControl = true;
                    clueDarkTime.setVisibility(View.VISIBLE);
                    clueBrightnessTime.setVisibility(View.VISIBLE);
                    darkTime.setVisibility(View.VISIBLE);
                    brightTime.setVisibility(View.VISIBLE);
                } else if(checkedId == radio1.getId()){  // 手动控制亮度
                    sysControl = false;
                    clueDarkTime.setVisibility(View.INVISIBLE);
                    clueBrightnessTime.setVisibility(View.INVISIBLE);
                    darkTime.setVisibility(View.INVISIBLE);
                    brightTime.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                String Brightness = brightness.getText().toString();
                String RepeatTimes = repeatTimes.getText().toString();
                String BrightTime;
                String DarkTime;
                if (sysControl) {  // 系统控制亮度
                    BrightTime = brightTime.getText().toString();
                    DarkTime = darkTime.getText().toString();
                } else {  // 手动控制亮度
                    BrightTime = "-1";
                    DarkTime = "-1";
                }
                intent.putExtra("Brightness", Brightness);
                intent.putExtra("RepeatTimes", RepeatTimes);
                intent.putExtra("BrightTime", BrightTime);
                intent.putExtra("DarkTime", DarkTime);
                startActivity(intent);
            }
        });
    }


    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }
    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

}