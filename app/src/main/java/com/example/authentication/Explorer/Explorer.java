package com.example.authentication.Explorer;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.CourseView;
import com.example.authentication.FixedSpeedScroller;
import com.example.authentication.Home.Course;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Home.ImageSliderAdapter;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;
import com.example.authentication.Search.Search;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private SharedPreferences sharedPreferences ;

    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.rgb(236,91,76);
    private TextView slidePagerText;
    private TextView slidePagerCategory;
    private TextView slidePagerView;
    private int currentPage;
    private Timer timer;
    private final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    private String firstLogIn;
    private static final float MIN_SCALE = 0.75f;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        firstLogIn = mParam2;

        mCourses = new ArrayList<>();
        mCourses = new CourseHandler(getContext(), null, null,1).loadCourseHandler("All");

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
                (R.drawable.blue_background),
                (R.drawable.green_background),
                (R.drawable.violet_background)
        };
        String[] title = {
                "Design MasterClass 1",
                "Business MasterClass 2",
                "Hacking 101",
                "Security Systems",
                "UI/UX 101"
        };

        String[] category = {
                "Design",
                "Business",
                "Hacking",
                "Hacking",
                "Design"
        };

        Integer[] colors_temp = {
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.violet)
        };

        ViewPager imageSlider = view.findViewById(R.id.imageSlider_explorer);
        ExplorerImageSlideAdapter imageSliderAdapter = new ExplorerImageSlideAdapter(getContext(), urls, title, category);
        imageSlider.setAdapter(imageSliderAdapter);
//        imageSlider.setBackgroundResource(
//                R.drawable.background_slide_pager_explorer
//        );
        Interpolator sInterpolator = new AccelerateInterpolator();
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(imageSlider.getContext(), sInterpolator);
            if (currentPage == urls.length) {
                scroller.setmDuration(50);
            } else {
                scroller.setmDuration(1000);
            }
            mScroller.set(imageSlider, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {

        }
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == urls.length) {
                    currentPage = 0;
                }
                imageSlider.setCurrentItem(currentPage, true);
                currentPage += 1;
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

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

//                GradientDrawable draw = (GradientDrawable) imageSlider.getBackground();
//                if (position < (imageSliderAdapter.getCount() -1) && position < (colors_temp.length - 1)) {
//
//                    draw.setColor(
//                            (Integer) argbEvaluator.evaluate(
//                                    positionOffset,
//                                    colors_temp[position],
//                                    colors_temp[position + 1]
//                            ));
//                }
//
//                else {
//                    draw.setColor(
//                            colors_temp[colors_temp.length - 1]);
//                }
            }

            @Override
            public void onPageSelected(int position) {
                editor.putInt("Page", currentPage - 1);
                editor.apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageSlider.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0f);

                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    view.setAlpha(1f);
                    view.setTranslationX(0f);
                    view.setScaleX(1f);
                    view.setScaleY(1f);

                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    view.setAlpha(1 - position);

                    // Counteract the default slide transition
                    view.setTranslationX(pageWidth * -position);

                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = MIN_SCALE
                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0f);
                }
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
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, fragment, "Tag");
        fragmentTransaction.addToBackStack("Tag");
        fragmentTransaction.commit();
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

    @Override
    public void onResume() {
        super.onResume();
        if (firstLogIn.equals("0")) {
            currentPage = 0;
            firstLogIn = "1";
        } else {
            currentPage = sharedPreferences.getInt("Page", 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Page", currentPage - 1);
        editor.apply();

    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Page", currentPage);
        editor.apply();

    }

}
