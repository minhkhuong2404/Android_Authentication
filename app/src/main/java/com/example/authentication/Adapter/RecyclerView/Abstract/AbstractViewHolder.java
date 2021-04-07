package com.example.authentication.Adapter.RecyclerView.Abstract;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

abstract public class AbstractViewHolder<DATA> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract public void bind(int position, DATA data);
}
