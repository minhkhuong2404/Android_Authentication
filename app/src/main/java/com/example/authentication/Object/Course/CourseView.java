package com.example.authentication.Object.Course;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.R;

public class CourseView extends AbstractFragment {

    private static final String COURSE_IMAGE = "param1";
    private static final String COURSE_CATEGORY = "param2";
    private static final String COURSE_BEFORE_SALE = "param3";
    private static final String COURSE_AFTER_SALE = "param4";
    private static final String COURSE_NAME = "param5";
    private static final String COURSE_RATE = "param6";

    // TODO: Rename and change types of parameters
    private String getCourseImage;
    private String getCourseCategory;
    private String getCourseBeforeSale;
    private String getCourseAfterSale;
    private String getCourseName;
    private String getCourseRate;
    private TextView courseRate, courseName, courseBeforeSalePrice, courseAfterSalePrice, courseCategory;
    private TextView courseRateText, courseBeforeSalePriceText, courseAfterSalePriceText, courseCategoryText;

    @Override
    public int getLayoutId() {
        return R.layout.course_view;
    }

    public CourseView() {
        // Required empty public constructor
    }

    @Override
    protected CourseView newInstance(String mParam1, String mParam2) {
        return null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public CourseView newInstance(String courseImage, String courseCategory, String courseBeforeSale, String courseAfterSale, String courseName, String courseRate) {
        CourseView fragment = new CourseView();
        Bundle args = new Bundle();
        args.putString(COURSE_IMAGE, courseImage);
        args.putString(COURSE_CATEGORY, courseCategory);
        args.putString(COURSE_BEFORE_SALE, courseBeforeSale);
        args.putString(COURSE_AFTER_SALE, courseAfterSale);
        args.putString(COURSE_NAME, courseName);
        args.putString(COURSE_RATE, courseRate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getCourseImage = getArguments().getString(COURSE_IMAGE);
            getCourseCategory = getArguments().getString(COURSE_CATEGORY);
            getCourseBeforeSale = getArguments().getString(COURSE_BEFORE_SALE);
            getCourseAfterSale = getArguments().getString(COURSE_AFTER_SALE);
            getCourseName = getArguments().getString(COURSE_NAME);
            getCourseRate = getArguments().getString(COURSE_RATE);
        }

    }

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        courseCategoryText.setText(resources.getString(R.string.course_category));
        courseBeforeSalePriceText.setText(resources.getString(R.string.course_before_sale_price));
        courseAfterSalePriceText.setText(resources.getString(R.string.course_after_sale_price));
        courseRateText.setText(resources.getString(R.string.course_rate));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button backToCourse = findViewById(R.id.back_btn_course_view);
        ImageView courseImage = findViewById(R.id.course_view_image);
        courseCategory = findViewById(R.id.course_view_category);
        courseBeforeSalePrice = findViewById(R.id.course_view_before_sale_price);
        courseAfterSalePrice = findViewById(R.id.course_view_after_sale_price);
        courseName = findViewById(R.id.course_view_name);
        courseRate = findViewById(R.id.course_view_rate);

        courseCategoryText = findViewById(R.id.course_category);
        courseBeforeSalePriceText = findViewById(R.id.course_before_sale_price);
        courseAfterSalePriceText = findViewById(R.id.course_after_sale_price);
        courseRateText = findViewById(R.id.course_rate);

        backToCourse.setOnClickListener(v -> goBack());

        Glide.with(getContext()).load(getCourseImage).into(courseImage);
        courseCategory.setText(getCourseCategory);
        courseBeforeSalePrice.setText(getCourseBeforeSale);
        courseAfterSalePrice.setText(getCourseAfterSale);
        courseName.setText(getCourseName);
        courseRate.setText(getCourseRate);

        courseBeforeSalePrice.setPaintFlags(courseBeforeSalePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        courseBeforeSalePrice.getPaint().setStrikeThruText(true);
    }
}
