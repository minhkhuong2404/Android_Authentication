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
import com.example.authentication.Setting.OnCollectionClickedListener;

import java.util.List;

// this class is used to view the Options which is shown on My Course fragment as well as on Profile fragment
// such as BADGES, COLLECTION, etc.
public class MyCourseCollectionAdapter extends RecyclerView.Adapter<MyCourseCollectionAdapter.MyCoursesCollectionViewHolder>{
    private static final String TAG = "CourseAdapter";
    private List<CollectionItem> collectionList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public int positionItem = 0;
    private OnCollectionClickedListener onCollectionClickedListener;

    public MyCourseCollectionAdapter(Context context, List<CollectionItem> data) {
        mContext = context;
        collectionList = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnCollectionClickedListener(OnCollectionClickedListener onCollectionClickedListener) {
        this.onCollectionClickedListener = onCollectionClickedListener;
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

    public class MyCoursesCollectionViewHolder extends RecyclerView.ViewHolder{
        private TextView collectionItem;

        public MyCoursesCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionItem = itemView.findViewById(R.id.my_courses_collection);

        }
    }
}
