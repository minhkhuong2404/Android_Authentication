package com.example.authentication.Fragment.Setting.ActivityHistory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.Adapter.RecyclerView.CourseExplorerRecyclerViewAdapter;
import com.example.authentication.Adapter.RecyclerView.MyCollectionRecyclerViewAdapter;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Object.CollectionItem.CollectionItem;
import com.example.authentication.Object.CollectionItem.OnCollectionClickedListener;
import com.example.authentication.Object.Course.Course;
import com.example.authentication.Object.Course.CourseView;
import com.example.authentication.Object.Course.OnItemClickedListener;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistory extends AbstractFragment implements OnCollectionClickedListener, OnItemClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;

    private RecyclerView rvCourses, rvCollection;
    private TextView myCourse;
    private CourseExplorerRecyclerViewAdapter mCourseExplorerAdapter;
    private MyCollectionRecyclerViewAdapter myCourseCollectionAdapter;
    private List<Course> mCourses = new ArrayList<>();
    private List<CollectionItem> item = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.my_course;
    }

    public ActivityHistory() {
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
    public ActivityHistory newInstance(String param1, String param2) {
        ActivityHistory fragment = new ActivityHistory();
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
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        myCourse.setText(resources.getString(R.string.my_courses));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvCourses = findViewById(R.id.rv_my_courses);
        rvCollection  = findViewById(R.id.rv_collection);
        myCourse = findViewById(R.id.my_courses);

        String language = getStringPref("Locale.Helper.Selected.Language", "en");
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        item.add(new CollectionItem(resources.getString(R.string.collectionItemStudy)));
        item.add(new CollectionItem(resources.getString(R.string.collectionItemCollection)));

        myCourseCollectionAdapter = new MyCollectionRecyclerViewAdapter(getContext(), item);
        myCourseCollectionAdapter.setData(item);
        myCourseCollectionAdapter.setOnCollectionClickedListener(this);

        rvCollection.setAdapter(myCourseCollectionAdapter);
        rvCollection.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        setUpRvCourses(mParam1);

        TextView searchButton = findViewById(R.id.back_btn_my_courses);
        searchButton.setOnClickListener(v -> goBack());

    }

    private void setUpRvCourses( String category) {
        mCourses = new CourseHandler(getContext(), null, null,1).loadCourseHandler(category);

        mCourseExplorerAdapter = new CourseExplorerRecyclerViewAdapter(getContext(), mCourses);
        mCourseExplorerAdapter.setData(mCourses);
        mCourseExplorerAdapter.setClickedListener(this);

        rvCourses.setAdapter(mCourseExplorerAdapter);
        rvCourses.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void getSelected(CollectionItem collectionItem, int position) {
        mCourses = new ArrayList<>();
        if (position == 1){
            setUpRvCourses(mParam2);
        } else {
            setUpRvCourses(mParam1);
        }
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
    public void onAddClicked(TextView textView) {
        startAnimation(textView);
    }

    @Override
    public void onCourseClicked(ImageView imageView) {
        startAnimation(imageView);
    }
}
