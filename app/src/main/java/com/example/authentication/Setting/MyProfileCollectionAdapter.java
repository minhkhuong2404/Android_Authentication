package com.example.authentication.Setting;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.MyCourses.CollectionItem;
import com.example.authentication.R;

import java.util.List;

public class MyProfileCollectionAdapter extends RecyclerView.Adapter<MyProfileCollectionAdapter.MyProfileCollectionViewHolder>{
    private static final String TAG = "CourseAdapter";
    private List<CollectionItem> collectionList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public int positionItem = 0;
    private OnCollectionClickedListener onCollectionClickedListener;

    public MyProfileCollectionAdapter(Context context, List<CollectionItem> data) {
        mContext = context;
        collectionList = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnCollectionClickedListener(OnCollectionClickedListener onCollectionClickedListener) {
        this.onCollectionClickedListener = onCollectionClickedListener;
    }

    @NonNull
    @Override
    public MyProfileCollectionAdapter.MyProfileCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.my_course_option, parent, false);
        return new MyProfileCollectionAdapter.MyProfileCollectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileCollectionAdapter.MyProfileCollectionViewHolder holder, int position) {
        CollectionItem collection = collectionList.get(position);

        holder.collectionItem.setText(collection.getItem());
        holder.collectionItem.setSelected(positionItem == position );
        holder.collectionItem.setBackgroundResource(positionItem == position ? R.drawable.underline : Color.TRANSPARENT);
        holder.collectionItem.setOnClickListener(v -> {
            if (onCollectionClickedListener != null) {
                onCollectionClickedListener.getSelected(collection, position);
                positionItem = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class MyProfileCollectionViewHolder extends RecyclerView.ViewHolder{
        private TextView collectionItem;

        public MyProfileCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionItem = itemView.findViewById(R.id.my_courses_collection);

        }
    }
}
