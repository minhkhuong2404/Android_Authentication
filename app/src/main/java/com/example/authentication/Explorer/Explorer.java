package com.example.authentication.Explorer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Home.Course;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Home.ImageSliderAdapter;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;
import com.example.authentication.Search.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Explorer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Explorer extends Fragment implements OnItemClickedListener {

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

    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.rgb(236,91,76);
    private TextView slidePagerText;
    private TextView slidePagerCategory;
    private TextView slidePagerView;

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
        mCourses = new CourseHandler(getContext(), null, null,1).loadDataHandler("All");

        mCourseAdapterExplorer = new CourseAdapterExplorer(getContext(), mCourses);
        mCourseAdapterExplorer.setClickedListener(this);
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

        slidePagerText = view.findViewById(R.id.explorer_slide_pager_text);
        slidePagerCategory = view.findViewById(R.id.category_slide_pager_text);
        slidePagerView = view.findViewById(R.id.view_slide_pager_text);

        int[] urls = {
                (R.drawable.orange_background),
                (R.drawable.yellow_background),
                (R.drawable.blue_background)
        };

        ViewPager imageSlider = view.findViewById(R.id.imageSlider_explorer);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(urls);
        imageSlider.setAdapter(imageSliderAdapter);

        // Indicator:
        LinearLayout indicator = view.findViewById(R.id.indicator_explorer);
        for (int i = 0; i < urls.length; i++) {
            // COLOR_ACTIVE ứng với chấm ứng với vị trí hiện tại của ViewPager,
            // COLOR_INACTIVE ứng với các chấm còn lại
            // ViewPager có vị trí mặc định là 0, vì vậy color ở vị trí i == 0 sẽ là COLOR_ACTIVE
            View dot = createDot(indicator.getContext(), i == 0 ? COLOR_ACTIVE : COLOR_INACTIVE);
            indicator.addView(dot);
        }

        // Thay đổi màu các chấm khi ViewPager thay đổi vị trí:
        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < urls.length; i++) {
                    // Duyệt qua từng "chấm" trong indicator
                    // Nếu i == position, tức i đang là vị trí hiện tại của ViewPager,
                    // ta sẽ đổi màu "chấm" thành COLOR_ACTIVE, nếu không
                    // thì sẽ đổi thành màu COLOR_INACTIVE
                    indicator.getChildAt(i).getBackground().mutate().setTint(i == position ? COLOR_ACTIVE : COLOR_INACTIVE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                String title = "";
                String category = "";
                switch (position) {
                    case 0:
                        title = "Business MasterClass 1";
                        category = "Business";
                        break;
                    case 1:
                        title = "Design 101";
                        category = "Design";
                        break;
                    case 2:
                        title = "Security Hack";
                        category = "Hacking";
                        break;

                }
                slidePagerText.setText(title);
                slidePagerCategory.setText(category);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    View createDot(Context context, @ColorInt int color) {
        View dot = new View(context);
        LinearLayout.MarginLayoutParams dotParams = new LinearLayout.MarginLayoutParams(20, 20);
        dotParams.setMargins(20, 10, 20, 10);
        dot.setLayoutParams(dotParams);
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.setTint(color);
        dot.setBackground(drawable);
        return dot;
    }

    private void switchToSearch() {
        Fragment fragment = new Search();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myContainer, fragment, "Tag");
        fragmentTransaction.addToBackStack("Tag");
        fragmentTransaction.commit();
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
