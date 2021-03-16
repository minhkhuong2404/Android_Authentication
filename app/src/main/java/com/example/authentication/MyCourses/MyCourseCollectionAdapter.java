package com.example.authentication.MyCourses;

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

import com.example.authentication.R;

import java.util.List;

public class MyCourseCollectionAdapter extends RecyclerView.Adapter<MyCourseCollectionAdapter.MyCoursesCollectionViewHolder>{
    private static final String TAG = "CourseAdapter";
    private List<CollectionItem> collectionList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public int positionItem = 0;

    public MyCourseCollectionAdapter(Context context, List<CollectionItem> data) {
        mContext = context;
        collectionList = data;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MyCourseCollectionAdapter.MyCoursesCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.my_course_option, parent, false);
        return new MyCourseCollectionAdapter.MyCoursesCollectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseCollectionAdapter.MyCoursesCollectionViewHolder holder, int position) {
        CollectionItem collection = collectionList.get(position);

        holder.collectionItem.setText(collection.getItem());
        holder.collectionItem.setSelected(positionItem == position );
        holder.collectionItem.setBackgroundResource(positionItem == position ? R.drawable.underline : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class MyCoursesCollectionViewHolder extends RecyclerView.ViewHolder{
        private TextView collectionItem;

        public MyCoursesCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionItem = itemView.findViewById(R.id.my_courses_collection);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    notifyItemChanged(positionItem);
                    positionItem = getLayoutPosition();
                    notifyItemChanged(positionItem);
                }
            });
        }
    }
}
