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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText tva = (EditText)findViewById(R.id.editTextTextPersonName);
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avalue = tva.getText().toString();
                Log.d("setting brightness: ", avalue);

                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.screenBrightness = Float.valueOf(avalue) / 100f;
                window.setAttributes(lp);

            }
        });
    }
}