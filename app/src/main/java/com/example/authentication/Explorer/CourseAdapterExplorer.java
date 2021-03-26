package com.example.authentication.Explorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.Home.Course;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;

import java.util.List;

public class CourseAdapterExplorer extends RecyclerView.Adapter<CourseAdapterExplorer.CourseViewHolder> {
    private static final String TAG = "CourseAdapter";
    private List<Course> mCourses;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickedListener clickedListener;

    public CourseAdapterExplorer(Context context, List<Course> data) {
        mContext = context;
        mCourses = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setClickedListener(OnItemClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.course_explorer, parent, false);
        return new CourseAdapterExplorer.CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = mCourses.get(position);

        holder.tvCourseName.setText(course.getCourseName());
        holder.tvBeforeSalePrice.setText(course.getBeforeSalePrice());
        holder.tvAfterSalePrice.setText(course.getAfterSalePrice());
        holder.tvRate.setText(course.getRate());
        holder.tvCourseImage.setBackgroundResource(course.getCourseImage());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedListener!= null)
                    clickedListener.onAddClicked(course);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickedListener!= null){
                    clickedListener.onViewClicked(course);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCourseName;
        private TextView tvAfterSalePrice;
        private TextView tvBeforeSalePrice;
        private TextView tvRate;
        private ImageView tvCourseImage;
        private Button btnAdd;

        @SuppressLint("ClickableViewAccessibility")
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.course_name_explorer);
            tvAfterSalePrice = itemView.findViewById(R.id.after_sale_price_explorer);
            tvBeforeSalePrice = itemView.findViewById(R.id.before_sale_price_explorer);
            tvRate = itemView.findViewById(R.id.rate_explorer);
            tvCourseImage = itemView.findViewById(R.id.course_image_explorer);
            btnAdd =itemView.findViewById(R.id.button_add_explorer);

            tvBeforeSalePrice.setPaintFlags(tvBeforeSalePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvBeforeSalePrice.getPaint().setStrikeThruText(true);

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
