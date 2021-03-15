package com.example.authentication.Explorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.authentication.Course;
import com.example.authentication.R;
import com.example.authentication.Search.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Explorer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Explorer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvCourses;
    private CourseAdapterExplorer mCourseAdapterExplorer;
    private List<Course> mCourses;

    public Explorer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Explorer newInstance(String param1, String param2) {
        Explorer fragment = new Explorer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explorer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvCourses = view.findViewById(R.id.rv_course_explorer);

        mCourses = new ArrayList<>();
        mCourses.add(new Course("Sketch App Masterclass","$ 340", "$ 199", "3.0", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Figma App Materclass","$ 350", "$ 199", "5.0", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Business Master Class","$ 440", "$ 299", "4.8", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Adobe XD Masterclass","$ 140", "$ 99", "4.8", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Photoshop CC Masterclass","$ 540", "$ 399", "4.5", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Illustrator CC Masterclass","$ 640", "$ 499", "4.8", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Premiere Pro CC Masterclass","$ 849", "$ 599", "4.7", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Business Masterclass", "$ 450", "$ 299", "5.0", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Business Introduction", "$ 350", "$ 199", "4.6", getContext().getResources().getDrawable(R.drawable.orange_background)));
        mCourses.add(new Course("Business Management", "$ 250", "$ 99", "3.8", getContext().getResources().getDrawable(R.drawable.orange_background)));

        mCourseAdapterExplorer = new CourseAdapterExplorer(getContext(), mCourses);

        rvCourses.setAdapter(mCourseAdapterExplorer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        TextView searchButton = view.findViewById(R.id.search_explorer_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switchToSearch();
            }
        });
    }

    private void switchToSearch() {
        Fragment fragment = new Search();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myContainer, fragment, "Tag");
        fragmentTransaction.addToBackStack("Tag");
        fragmentTransaction.commit();
    }
}
