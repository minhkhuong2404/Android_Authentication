package com.example.authentication.Search;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.CourseView;
import com.example.authentication.Home.Course;
import com.example.authentication.Explorer.CourseAdapterExplorer;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Home.Home;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment implements OnItemClickedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvCourses;
    private CourseAdapterExplorer mSearchAdapter;
    private List<Course> mCourses;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    
        rvCourses = view.findViewById(R.id.rv_search);
        
        mCourses = new ArrayList<Course>();
        mCourses = new CourseHandler(getContext(), null, null,1).loadCourseHandler("All");

        mSearchAdapter = new CourseAdapterExplorer(getContext(), mCourses);
        mSearchAdapter.setClickedListener(this);
        rvCourses.setAdapter(mSearchAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        Button backToExplorer = view.findViewById(R.id.back_btn_search);
        TextView clearButton = view.findViewById(R.id.search_clear_btn);
        EditText searchButton = view.findViewById(R.id.search_btn);

        backToExplorer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToExplorer();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchButton.getText().clear();
            }
        });
    }

    private void backToExplorer() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.navigation_search, fragment);
//        fragmentTransaction.commit();
    }

    @Override
    public void onAddClicked(TextView textView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        textView.clearAnimation();
        textView.startAnimation(a);
    }

    private void switchToCourseView(Course course) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, CourseView.newInstance(course.getCourseImage(), course.getCategory(), course.getBeforeSalePrice(), course.getAfterSalePrice(), course.getCourseName(), course.getRate()), "view");
        fragmentTransaction.addToBackStack("view");
        fragmentTransaction.commit();
    }

    @Override
    public void onViewClicked(Course course) {
        switchToCourseView(course);
    }

    @Override
    public void onCourseClicked(ImageView imageView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        imageView.clearAnimation();
        imageView.startAnimation(a);
    }

}
