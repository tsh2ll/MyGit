package com.example.myapplication;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
    private ItemData[] listdata;
    TextView textView;

    public MyRecyclerAdapter(ItemData[] listdata) {
        this.listdata = listdata;
    }

    //ViewHolder类定义
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View relativeLayout;
        public BreakIterator textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        final ItemData itemData = listdata[position];
        holder.textView.setText(itemData.getInfo());
        holder.imageView.setImageResource(itemData.getPicId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Click: "+ itemData.getInfo(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    ItemData[] itemData = new ItemData[] {
            new ItemData("Email", android.R.drawable.ic_dialog_email),
            new ItemData("Info", android.R.drawable.ic_dialog_info),
            new ItemData("Delete", android.R.drawable.ic_delete),
            new ItemData("Dialer", android.R.drawable.ic_dialog_dialer),
            new ItemData("Alert", android.R.drawable.ic_dialog_alert),
            new ItemData("Map", android.R.drawable.ic_dialog_map),
            new ItemData("Email", android.R.drawable.ic_dialog_email),
            new ItemData("Info", android.R.drawable.ic_dialog_info),
            new ItemData("Delete", android.R.drawable.ic_delete),
            new ItemData("Dialer", android.R.drawable.ic_dialog_dialer),
            new ItemData("Alert", android.R.drawable.ic_dialog_alert),
            new ItemData("Map", android.R.drawable.ic_dialog_map),
    };
    


