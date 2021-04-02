package com.example.authentication;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Explorer.CourseAdapterExplorer;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Home.Course;
import com.example.authentication.Home.Home;
import com.example.authentication.MyCourses.OnItemClickedListener;

import java.util.ArrayList;
import java.util.List;

public class CourseView extends Fragment {

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

    public CourseView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseView newInstance(String courseImage, String courseCategory, String courseBeforeSale, String courseAfterSale, String courseName, String courseRate) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.course_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button backToCourse = view.findViewById(R.id.back_btn_course_view);
        ImageView courseImage = view.findViewById(R.id.course_view_image);
        TextView courseCategory = view.findViewById(R.id.course_view_category);
        TextView courseBeforeSalePrice = view.findViewById(R.id.course_view_before_sale_price);
        TextView courseAfterSalePrice = view.findViewById(R.id.course_view_after_sale_price);
        TextView courseName = view.findViewById(R.id.course_view_name);
        TextView courseRate = view.findViewById(R.id.course_view_rate);

        backToCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToCourse();
            }
        });

        Glide.with(getContext()).load(getCourseImage).into(courseImage);
        courseCategory.setText(getCourseCategory);
        courseBeforeSalePrice.setText(getCourseBeforeSale);
        courseAfterSalePrice.setText(getCourseAfterSale);
        courseName.setText(getCourseName);
        courseRate.setText(getCourseRate);

        courseBeforeSalePrice.setPaintFlags(courseBeforeSalePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        courseBeforeSalePrice.getPaint().setStrikeThruText(true);
    }

    private void backToCourse() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.navigation_search, fragment);
//        fragmentTransaction.commit();
    }

}
