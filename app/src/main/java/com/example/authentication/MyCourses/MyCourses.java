package com.example.authentication.MyCourses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.Home.Course;
import com.example.authentication.Explorer.CourseAdapterExplorer;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Explorer.Explorer;
import com.example.authentication.R;
import com.example.authentication.Setting.OnCollectionClickedListener;

import java.util.ArrayList;
import java.util.List;

public class MyCourses extends Fragment implements OnCollectionClickedListener, OnItemClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvCourses;
    private RecyclerView rvCollection;
    private CourseAdapterExplorer mCourseAdapterExplorer;
    private MyCourseCollectionAdapter myCourseCollectionAdapter;
    private List<Course> mCourses;
    private List<CollectionItem> item;

    public MyCourses() {
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
        return inflater.inflate(R.layout.my_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvCourses = view.findViewById(R.id.rv_my_courses);
        rvCollection  = view.findViewById(R.id.rv_collection);



        item = new ArrayList<>();
        item.add(new CollectionItem("STUDYING"));
        item.add(new CollectionItem("COLLECTION"));

        myCourseCollectionAdapter = new MyCourseCollectionAdapter(getContext(), item);
        rvCollection.setAdapter(myCourseCollectionAdapter);
        myCourseCollectionAdapter.setOnCollectionClickedListener(this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCollection.setLayoutManager(linearLayoutManager2);

        mCourses = new ArrayList<>();
        mCourses = new CourseHandler(getContext(), null, null,1).loadDataHandler("All");

        mCourseAdapterExplorer = new CourseAdapterExplorer(getContext(), mCourses);
        mCourseAdapterExplorer.setClickedListener(this);
        rvCourses.setAdapter(mCourseAdapterExplorer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        TextView searchButton = view.findViewById(R.id.back_btn_my_courses);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToSettings();
            }
        });

    }

    private void backToSettings() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void getSelected(CollectionItem collectionItem, int position) {
        mCourses = new ArrayList<>();
        if (position == 1){
            mCourses = new CourseHandler(getContext(), null, null,1).loadDataHandler("Hacking");
            mCourseAdapterExplorer = new CourseAdapterExplorer(getContext(), mCourses);
            mCourseAdapterExplorer.setClickedListener(this);
            rvCourses.setAdapter(mCourseAdapterExplorer);
        } else {
            mCourses = new CourseHandler(getContext(), null, null,1).loadDataHandler("All");
            mCourseAdapterExplorer = new CourseAdapterExplorer(getContext(), mCourses);
            mCourseAdapterExplorer.setClickedListener(this);
            rvCourses.setAdapter(mCourseAdapterExplorer);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onAddClicked(Course course) {
        Toast.makeText(getContext(), "Added "+course.getCourseName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewClicked(Course course) {
        Toast.makeText(getContext(), course.getCourseName(), Toast.LENGTH_SHORT).show();
    }
}
