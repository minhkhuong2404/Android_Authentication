package com.example.authentication.Fragment.Explorer.Search;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.Adapter.RecyclerView.CourseExplorerRecyclerViewAdapter;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Object.Course.Course;
import com.example.authentication.Object.Course.CourseView;
import com.example.authentication.Object.Course.OnItemClickedListener;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

public class Search extends AbstractFragment implements OnItemClickedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvCourses;
    private CourseExplorerRecyclerViewAdapter mSearchAdapter;
    private List<Course> mCourses = new ArrayList<Course>();;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.search;
    }

    @Override
    public void UpdateLanguage(String language) {

    }

    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public Search newInstance(String param1, String param2) {
        Search fragment = new Search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    
        rvCourses = findViewById(R.id.rv_search);
        
        mCourses = new CourseHandler(getContext(), null, null,1).loadCourseHandler("All");

        mSearchAdapter = new CourseExplorerRecyclerViewAdapter(getContext(), mCourses);
        mSearchAdapter.setData(mCourses);
        mSearchAdapter.setClickedListener(this);

        rvCourses.setAdapter(mSearchAdapter);
        rvCourses.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        EditText searchButton = findViewById(R.id.search_btn);

        findViewById(R.id.back_btn_search).setOnClickListener(v -> goBack());
        findViewById(R.id.search_clear_btn).setOnClickListener(v -> searchButton.getText().clear());
    }

    @Override
    public void onAddClicked(TextView textView) {
        startAnimation(textView);
    }

    private void switchToCourseView(Course course) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, new CourseView().newInstance(course.getCourseImage(), course.getCategory(), course.getBeforeSalePrice(), course.getAfterSalePrice(), course.getCourseName(), course.getRate()), "view");
        fragmentTransaction.addToBackStack("view").commit();
    }

    @Override
    public void onViewClicked(Course course) {
        switchToCourseView(course);
    }

    @Override
    public void onCourseClicked(ImageView imageView) {
        startAnimation(imageView);
    }

}
