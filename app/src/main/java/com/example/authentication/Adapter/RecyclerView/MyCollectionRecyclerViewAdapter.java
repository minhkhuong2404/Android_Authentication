package com.example.authentication.Adapter.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractRecyclerViewAdapter;
import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractViewHolder;
import com.example.authentication.Object.CollectionItem.CollectionItem;
import com.example.authentication.R;
import com.example.authentication.Object.CollectionItem.OnCollectionClickedListener;

import java.util.List;

// this class is used to view the Options which is shown on My Course fragment as well as on Profile fragment
// such as BADGES, COLLECTION, etc.
public class MyCollectionRecyclerViewAdapter extends AbstractRecyclerViewAdapter<CollectionItem, MyCollectionRecyclerViewAdapter.MyCollectionViewHolder> {
    private List<CollectionItem> collectionList;
    private Context mContext;
    public int positionItem = 0;
    private OnCollectionClickedListener onCollectionClickedListener;
    private MyCollectionRecyclerViewAdapter myCourseCollectionAdapter;

    public MyCollectionRecyclerViewAdapter(Context context, List<CollectionItem> data) {
        mContext = context;
        collectionList = data;
    }

    public void setOnCollectionClickedListener(OnCollectionClickedListener onCollectionClickedListener) {
        this.onCollectionClickedListener = onCollectionClickedListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_course_option;
    }

    @Override
    public MyCollectionViewHolder initViewHolder(View view, @NonNull ViewGroup parent, int viewType) {
        return new MyCollectionViewHolder(view);
    }

    public class MyCollectionViewHolder extends AbstractViewHolder<CollectionItem> {
        private TextView collectionItem;

        public MyCollectionViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        @Override
        public void bind(int position, CollectionItem item) {
            collectionItem = itemView.findViewById(R.id.my_courses_collection);
            CollectionItem collection = collectionList.get(position);

            collectionItem.setText(collection.getItem());
            collectionItem.setSelected(positionItem == position );
            collectionItem.setBackgroundResource(positionItem == position ? R.drawable.underline : Color.TRANSPARENT);
            collectionItem.setOnClickListener(v -> {
                if (onCollectionClickedListener != null) {
                    onCollectionClickedListener.getSelected(collection, position);
                    positionItem = position;
                    notifyDataSetChanged();
                }
            });
        }
    }
}
