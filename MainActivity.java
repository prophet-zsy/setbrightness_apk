package com.example.glaring;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText brightness = (EditText)findViewById(R.id.editTextTextPersonName);
        EditText repeatTimes = (EditText)findViewById(R.id.editTextTextPersonName2);
        EditText brightTime = (EditText)findViewById(R.id.editTextTextPersonName3);
        EditText darkTime = (EditText)findViewById(R.id.editTextTextPersonName4);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                String Brightness = brightness.getText().toString();
                String RepeatTimes = repeatTimes.getText().toString();
                String BrightTime = brightTime.getText().toString();
                String DarkTime = darkTime.getText().toString();
                intent.putExtra("Brightness", Brightness);
                intent.putExtra("RepeatTimes", RepeatTimes);
                intent.putExtra("BrightTime", BrightTime);
                intent.putExtra("DarkTime", DarkTime);
                startActivity(intent);
            }
        });
    }
}