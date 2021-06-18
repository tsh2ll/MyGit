package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;
    public RelativeLayout relativeLayout;
    public ViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
        this.textView = (TextView) itemView.findViewById(R.id.textView);
        this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
    }
}