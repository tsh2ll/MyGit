package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String TAG="GridViewActivity";
    List<String>data=new ArrayList<String>();
    ArrayAdapter adapter;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
    ListView listView=(ListView)findViewById(R.id.mylist);

        //GridView listView = (GridView) findViewById(R.id.mylist);
        //init data
        for(int i=0;i<100;i++){
            data.add("item" + i);
        }
         adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));
        listView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
        Log.i(TAG, "onItemClick: position=" + position);
        Log.i(TAG, "onItemClick: parent" + listview);
        adapter.remove(listview.getItemAtPosition(position));
        //adapter.notifyDataSetChanged();
    }
}
