package com.example.authentication.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractRecyclerViewAdapter;
import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractViewHolder;
import com.example.authentication.Object.Course.Course;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Object.Course.OnItemClickedListener;
import com.example.authentication.R;

import java.util.List;

public class CourseExplorerRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Course, CourseExplorerRecyclerViewAdapter.CourseViewHolder> {
    private List<Course> mCourses;
    private Context mContext;
    private OnItemClickedListener clickedListener;
    private int lastPosition = -1;

    public CourseExplorerRecyclerViewAdapter(Context context, List<Course> data) {
        mContext = context;
        mCourses = data;
    }

    public void setClickedListener(OnItemClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.course_explorer;
    }

    @Override
    public CourseViewHolder initViewHolder(View view, @NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(view);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class CourseViewHolder extends AbstractViewHolder<Course> {
        private TextView tvCourseName, tvAfterSalePrice, tvBeforeSalePrice, tvRate;
        private ImageView tvCourseImage;
        private Button btnAdd;

        @SuppressLint("ClickableViewAccessibility")
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        @Override
        public void bind ( int position, Course course){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            String language = sharedPreferences.getString("Locale.Helper.Selected.Language", "en");
            Context context = LocaleHelper.setLocale(itemView.getContext(), language);
            Resources resources = context.getResources();

            tvCourseName = itemView.findViewById(R.id.course_name_explorer);
            tvAfterSalePrice = itemView.findViewById(R.id.after_sale_price_explorer);
            tvBeforeSalePrice = itemView.findViewById(R.id.before_sale_price_explorer);
            tvRate = itemView.findViewById(R.id.rate_explorer);
            tvCourseImage = itemView.findViewById(R.id.course_image_explorer);
            btnAdd = itemView.findViewById(R.id.button_add_explorer);

            btnAdd.setText(resources.getString(R.string.add));
            tvBeforeSalePrice.setPaintFlags(tvBeforeSalePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvBeforeSalePrice.getPaint().setStrikeThruText(true);

            tvCourseName.setText(course.getCourseName());
            float beforePrice = Float.parseFloat(course.getBeforeSalePrice());
            float afterPrice = Float.parseFloat(course.getAfterSalePrice());
            tvBeforeSalePrice.setText("$ " + ((beforePrice >= afterPrice) ? course.getBeforeSalePrice() : course.getAfterSalePrice()));
            tvAfterSalePrice.setText("$ " + ((beforePrice < afterPrice) ? course.getBeforeSalePrice() : course.getAfterSalePrice()));
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