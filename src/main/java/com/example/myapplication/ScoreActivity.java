package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }


    public void btn1(View v) {
        show(1);
    }

    public void btn2(View v) {
        show(2);
    }

    public void btn3(View v) {
        show(3);
    }

    public void btnReset(View v) {
        TextView out = (TextView)findViewById(R.id.score);
        out.setText("0");
    }

    private void show(int i){
        TextView out = (TextView)findViewById(R.id.score);
        String oldScore = (String) out.getText();
        String newScore = String.valueOf(Integer.parseInt(oldScore) + i);
        out.setText(newScore);
    }
}
