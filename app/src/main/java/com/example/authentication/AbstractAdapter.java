package com.example.authentication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractAdapter<DATA, VIEW_HOLDER extends AbstractViewHolder<DATA>> extends RecyclerView.Adapter<VIEW_HOLDER> {

    private List<DATA> mData;

    abstract public int getLayoutId();
    abstract public VIEW_HOLDER initViewHolder(View view, @NonNull ViewGroup parent, int viewType);

    public AbstractAdapter() {
        mData = new ArrayList<DATA>();
    }

    @NonNull
    @Override
    public VIEW_HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return initViewHolder(itemView, parent, viewType);
    }

    public void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position) {
        DATA dataItem = getItemPosition(position);
        holder.bind(position, dataItem);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public DATA getItemPosition(int position) {

        return  mData.get(position) ;
    }
     public void setData(List<DATA> data){
        this.mData =data;
        notifyDataSetChanged();
     }
}
