package com.example.authentication.Search;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Home.Course;
import com.example.authentication.R;

import java.util.List;

// same functionality as CourseAdapterExplorer
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private static final String TAG = "CourseAdapter";
    private List<Course> mCourses;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public SearchAdapter(Context context, List<Course> data) {
        mContext = context;
        mCourses = data;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.course_explorer, parent, false);
        return new SearchAdapter.SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Course course = mCourses.get(position);

        holder.tvCourseName.setText(course.getCourseName());
        holder.tvBeforeSalePrice.setText(course.getBeforeSalePrice());
        holder.tvAfterSalePrice.setText(course.getAfterSalePrice());
        holder.tvRate.setText(course.getRate());
        Glide.with(mContext).load(course.getCourseImage()).into(holder.tvCourseImage);
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCourseName;
        private TextView tvAfterSalePrice;
        private TextView tvBeforeSalePrice;
        private TextView tvRate;
        private ImageView tvCourseImage;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.course_name_explorer);
            tvAfterSalePrice = itemView.findViewById(R.id.after_sale_price_explorer);
            tvBeforeSalePrice = itemView.findViewById(R.id.before_sale_price_explorer);
            tvRate = itemView.findViewById(R.id.rate_explorer);
            tvCourseImage = itemView.findViewById(R.id.course_image_explorer);

            tvBeforeSalePrice.setPaintFlags(tvBeforeSalePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvBeforeSalePrice.getPaint().setStrikeThruText(true);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Course course = mCourses.get(getAdapterPosition());
                    Toast.makeText(mContext, course.getCourseName(), Toast.LENGTH_SHORT).show();
                }
            });

            tvCourseName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvCourseName.setPaintFlags(tvCourseName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    tvCourseName.getPaint().setUnderlineText(true);
                }
            });
        }
    }
}
