package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RateActivity extends Activity implements Runnable {

    private static final String TAG = "RateActivity";
    EditText rmb;
    TextView result;
    private float dollarRate;
    private float euroRate;
    private float wonRate;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text4);
        rmb = findViewById(R.id.rmb);
        result = findViewById(R.id.result);

        SharedPreferences sharedPreferences =
                getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);

        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.15f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.13f);
        wonRate = sharedPreferences.getFloat("won_rate", 170.27f);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 6) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage:getMessage msg=" + str);
                    result.setText(str);
                }
                super.handleMessage(msg);
            }
        };
    }

    public void onClick(View btn) {
        String str = rmb.getText().toString();
        float r = 0;
        if (str != null && str.length() > 0) {
            r = Float.parseFloat(str);
        }
        Log.i(TAG, "onClick:r=" + r);
        if (btn.getId() == R.id.btn_dollar) {
            float val = r * dollarRate;
            result.setText(String.valueOf(val));
        } else if (btn.getId() == R.id.btn_euro) {

            float val = r * euroRate;
            result.setText(String.valueOf(val));
        } else {
            float val = r * wonRate;
            result.setText(String.valueOf(val));
        }
    }

    public void openConfig(View btn) {

        Intent config = new Intent(this, ConfigActivity.class);

        config.putExtra("dollar_key_rate", dollarRate);
        config.putExtra("euro_key_rate", euroRate);
        config.putExtra("won_key_rate", wonRate);

        Log.i(TAG, "openConfig:dollarRate" + dollarRate);
        Log.i(TAG, "openConfig:euroRate" + euroRate);
        Log.i(TAG, "openConfig:wonRate" + wonRate);

        startActivityForResult(config, 1);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == 2) {

            SharedPreferences sp =
                    getSharedPreferences("myrate", Activity.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("dollar_rate", dollarRate);
            editor.putFloat("euro_rate", euroRate);
            editor.putFloat("won_rate", wonRate);
            editor.apply();

            Bundle bdl = data.getExtras();
            dollarRate = bdl.getFloat("key_dollar", 0.15f);
            euroRate = bdl.getFloat("key_euro", 0.13f);
            wonRate = bdl.getFloat("key_won", 170.27f);

            Log.i(TAG, "onActivityResult:dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult:euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult:wonRate=" + wonRate);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void run() {
        Log.i(TAG, "run:.......");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run:after 3s");

        URL url=null;
        try{
            url=new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http=(HttpURLConnection) url.openConnection();
            InputStream in =http.getInputStream();
            String html=inputStream2String(in);
            Log.i(TAG,"run:html="+html);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(6, "23333");
        // msg.what=6;
        // msg.obj="Hello from run()";
        handler.sendMessage(msg);
    }

    private String inputStream2String(InputStream inputStream)
            throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}



