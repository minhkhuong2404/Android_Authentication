package com.example.authentication.Adapter.RecyclerView.Abstract;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.Object.Badges.OnBadgeClickListener;
import com.example.authentication.Object.CollectionItem.OnCollectionClickedListener;
import com.example.authentication.Object.Course.OnItemClickedListener;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractRecyclerViewAdapter<DATA, VIEW_HOLDER extends AbstractViewHolder<DATA>> extends RecyclerView.Adapter<VIEW_HOLDER>{

    private List<DATA> mData;
    private View itemView;
    private int lastPosition = -1;

    abstract public int getLayoutId();
    abstract public VIEW_HOLDER initViewHolder(View view, @NonNull ViewGroup parent, int viewType);

    public AbstractRecyclerViewAdapter() {
        mData = new ArrayList<DATA>();
    }

    @NonNull
    @Override
    public VIEW_HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return initViewHolder(itemView, parent, viewType);
    }

    public void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position) {
        DATA dataItem = getItemPosition(position);
        holder.bind(position, dataItem);
        setAnimation(itemView, position);
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

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.zoom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
