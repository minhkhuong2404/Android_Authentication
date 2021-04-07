package com.example.authentication.Adapter.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
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

public class CourseRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Course, CourseRecyclerViewAdapter.CourseViewHolder> {
    private Context mContext;
    private int lastPosition = -1;

    private OnItemClickedListener clickedListener;

    public CourseRecyclerViewAdapter(Context context) {
        super();
        mContext = context;
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
            viewToAnimate.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_in));
            lastPosition = position;
        }
    }


    class CourseViewHolder extends AbstractViewHolder<Course> {
        public CourseViewHolder(View itemView){
            super(itemView);
        }

        private TextView tvCourseName, tvAfterSalePrice, tvBeforeSalePrice, tvRate;
        private ImageView tvCourseImage;
        private Button btnAdd;

        @Override
        public void bind(int position, Course course) {
            if (course != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
                String language = sharedPreferences.getString("Locale.Helper.Selected.Language", "en");
                Context context = LocaleHelper.setLocale(itemView.getContext(), language);
                Resources resources = context.getResources();

                tvCourseName = itemView.findViewById(R.id.course_name);
                tvAfterSalePrice = itemView.findViewById(R.id.after_sale_price);
                tvBeforeSalePrice = itemView.findViewById(R.id.before_sale_price);
                tvRate = itemView.findViewById(R.id.rate_homepage);
                tvCourseImage = itemView.findViewById(R.id.course_image);
                btnAdd = itemView.findViewById(R.id.btnAdd);

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
}
