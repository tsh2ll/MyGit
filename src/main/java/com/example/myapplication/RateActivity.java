package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RateActivity extends Activity {
    EditText rmb;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text4);
        rmb = findViewById(R.id.input_rmb);
        result = findViewById(R.id.result);

    }

    public void click(View btn) {
        String str = rmb.getText().toString();
        int i = Integer.valueOf(String.valueOf(rmb)).intValue();
        if (str != null && str.length() > 0) {

            float r = 0.1f;
            if (btn.getId() == R.id.btnr1) {
                r = 0.15f;
            } else if (btn.getId() == R.id.btnr2) {
                r = 0.13f;
            } else {
                r = 170.97f;
            }//确定汇率

            //计算过程
            r=r*Float.parseFloat(str);

            result.setText("结果为" + String.format("%.2f",r));
        }
    }
}