package com.example.glaring;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean sysControl = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySysSetting();
        setBrightnessMode();

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

    public void applySysSetting() {
        //申请设置系统亮度的权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
            } else {
                //有了权限，具体的动作
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();;
            }
        }
    }

    public void setBrightnessMode() {
        //            获取亮度调节模式
        int systemMode = -1;
        try {
            systemMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (systemMode == 1) { // 如果为自动1，改为手动0
            systemMode = 0;
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, systemMode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FloatingButtonService.class));
            }
        }
    }
}