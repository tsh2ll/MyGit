package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity<msg> extends Activity implements Runnable {
    EditText rmb;
    TextView result;
    private float dollarRate = 0.15f;
    private float euroRate = 0.13f;
    private float wonRate = 175.27f;
    private final String TAG = "RateActivity";

    Handler handler;
    Document doc = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text4);
        rmb = findViewById(R.id.input_rmb);
        result = findViewById(R.id.result);

        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.0f);
        wonRate = sharedPreferences.getFloat("won_rate", 0.0f);

        Log.i(TAG, "onCreate: sp dollarRate=" + dollarRate);
        Log.i(TAG, "onCreate: sp euroRate=" + euroRate);
        Log.i(TAG, "onCreate: sp wonRate=" + wonRate);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);

        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate",dollarRate);
        editor.putFloat("euro_rate",euroRate);
        editor.putFloat("won_rate",wonRate);
        editor.putString("update_date",todayStr);
        editor.apply();

        String updateDate = sharedPreferences.getString("update_date","");

        //开启子线程
        Log.i(TAG, "onCreate: sp updateDate=" + updateDate);
        Log.i(TAG, "onCreate: todayStr=" + todayStr);

//判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG, "onCreate: 数据需要更新");
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else{
            Log.i(TAG, "onCreate: 数据不需要更新");
        }

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    /*String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);
                    result.setText(str);*/

                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate = bdl.getFloat("dollar_rate");
                    euroRate = bdl.getFloat("euro_rate");
                    wonRate = bdl.getFloat("won_rate");

                    Log.i(TAG, "handleMessage: dollarRate:" + dollarRate);
                    Log.i(TAG, "handleMessage: euroRate:" + euroRate);
                    Log.i(TAG, "handleMessage: wonRate:" + wonRate);
                    Toast.makeText(RateActivity.this, "汇率已更新", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }

        };
    }

    public void onClick(View btn) {
        Log.i(TAG, "onClick: ");
        String str = rmb.getText().toString();
        Log.i(TAG, "onClick: get str=" + str);

        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            //用户没有输入内容
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "onClick: r=" + r);
        Log.i(TAG,"onClick:");

        //int r = Integer.valueOf(String.valueOf(rmb)).intValue();
        if(btn.getId()==R.id.btn_dollar){
            result.setText(String.valueOf(r*dollarRate));
        }else if(btn.getId()==R.id.btn_euro){
            result.setText(String.valueOf(r*euroRate));
        }else{
            result.setText(String.valueOf(r*wonRate));
        }
    }

    public void openConfig(View btn) {
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_key_rate", dollarRate);
        config.putExtra("euro_key_rate", euroRate);
        config.putExtra("won_key_rate", wonRate);

        Log.i(TAG, "openOne: dollarRate=" + dollarRate);
        Log.i(TAG, "openOne: euroRate=" + euroRate);
        Log.i(TAG, "openOne: wonRate=" + wonRate);

        //startActivity(config);
        startActivityForResult(config, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {

            //将新设置的汇率写到SP里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate", dollarRate);
            editor.putFloat("euro_rate", euroRate);
            editor.putFloat("won_rate", wonRate);
            editor.commit();
            Log.i(TAG, "onActivityResult: 数据已保存到sharedPreferences");

            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar", 0.1f);
            euroRate = bundle.getFloat("key_euro", 0.1f);
            wonRate = bundle.getFloat("key_won", 0.1f);
            Log.i(TAG, "onActivityResult: dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult: euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult: wonRate=" + wonRate);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public void run() {
        Log.i(TAG, "run:......");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run:after 1d");

        Bundle bundle = new Bundle();
        try {
            String url = "http://www.usd-cny.com/bankofchina.htm";
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG, "run:" + doc.title());

            Element table =doc.getElementsByTag("table").first();
            Elements trs = table.getElementsByTag("tr");
            for(Element tr:trs){
                Elements tds=tr.getElementsByTag("td");
                if(tds.size()>0){
                    String str=tds.get(0).text();
                    String val=tds.get(5).text();
                   Log.i(TAG,str+"=>"+val);

                    float v = 100f / Float.parseFloat(val);
                    if("美元".equals(str)){
                        bundle.putFloat("dollar_rate", v);
                    }else if("欧元".equals(str)){
                        bundle.putFloat("euro_rate", v);
                    }else if("韩元".equals(str)){
                        bundle.putFloat("won_rate", v);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //获取网络数据
      /* URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
//msg.what = 5;
        //msg.obj = "Hello from run()";
        msg.obj = bundle;
        handler.sendMessage(msg);
    }
}




