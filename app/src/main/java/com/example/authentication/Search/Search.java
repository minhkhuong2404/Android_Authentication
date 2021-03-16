package com.example.authentication.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.Course;
import com.example.authentication.DataHandler;
import com.example.authentication.Explorer.CourseAdapterExplorer;
import com.example.authentication.Explorer.Explorer;
import com.example.authentication.Home.Home;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvCourses;
    private SearchAdapter mSearchAdapter;
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
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        mCourses = new DataHandler(getContext(), null, null,1).loadDataHandler("All");

        mSearchAdapter = new SearchAdapter(getContext(), mCourses);

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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.navigation_search, fragment);
//        fragmentTransaction.commit();
    }
    
}
