package com.example.authentication.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.authentication.AbstractAdapter;
import com.example.authentication.AbstractViewHolder;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CourseAdapter extends AbstractAdapter<Course, CourseAdapter.CourseViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int lastPosition = -1;

    private OnItemClickedListener clickedListener;

    public CourseAdapter(Context context) {
        super();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CourseViewHolder initViewHolder(View view, @NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.course_homepage;
    }

    public void setClickedListener(OnItemClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }
    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    class CourseViewHolder extends AbstractViewHolder<Course> {
        public CourseViewHolder(View itemView){
            super(itemView);
        }

        private TextView tvCourseName;
        private TextView tvAfterSalePrice;
        private TextView tvBeforeSalePrice;
        private TextView tvRate;
        private ImageView tvCourseImage;
        private Button btnAdd;

        @Override
        public void bind(int position, Course course) {
            if (course != null) {
                tvCourseName = itemView.findViewById(R.id.course_name);
                tvAfterSalePrice = itemView.findViewById(R.id.after_sale_price);
                tvBeforeSalePrice = itemView.findViewById(R.id.before_sale_price);
                tvRate = itemView.findViewById(R.id.rate_homepage);
                tvCourseImage = itemView.findViewById(R.id.course_image);
                btnAdd = itemView.findViewById(R.id.btnAdd);
                tvBeforeSalePrice.setPaintFlags(tvBeforeSalePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvBeforeSalePrice.getPaint().setStrikeThruText(true);
                tvCourseName.setText(course.getCourseName());
                float beforePrice = Float.parseFloat(course.getBeforeSalePrice());
                float afterPrice = Float.parseFloat(course.getAfterSalePrice());
                tvBeforeSalePrice.setText("$ " + ((beforePrice >= afterPrice) ? course.getBeforeSalePrice() : course.getAfterSalePrice()));
                tvAfterSalePrice.setText("$ " + ((beforePrice < afterPrice) ? course.getAfterSalePrice() : course.getBeforeSalePrice()));
                tvRate.setText(course.getRate());

                Glide.with(mContext).load(course.getCourseImage()).placeholder(R.drawable.orange_background).into(tvCourseImage);

                btnAdd.setOnClickListener(v -> {
                    if (clickedListener != null)
                        clickedListener.onAddClicked(btnAdd);
                });

                itemView.setOnClickListener(v -> {

                    if (clickedListener != null) {
                        clickedListener.onViewClicked(course);
                    }
                });

                tvCourseImage.setOnClickListener(v -> {

                    if (clickedListener != null) {
                        clickedListener.onCourseClicked(tvCourseImage);
                    }
                });

                tvCourseName.setOnClickListener(v -> {

                    if (clickedListener != null) {
                        clickedListener.onViewClicked(course);
                    }
                });
                setAnimation(itemView, position);
            }
        }
    }
}
