package com.example.authentication.Fragment.Explorer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.authentication.Adapter.RecyclerView.CourseExplorerRecyclerViewAdapter;
import com.example.authentication.Adapter.SlidePager.ExplorerImageSlideAdapter;
import com.example.authentication.Object.Course.CourseView;
import com.example.authentication.Adapter.SlidePager.FixedSpeedScroller;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Object.Course.Course;
import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Object.Course.OnItemClickedListener;
import com.example.authentication.R;
import com.example.authentication.Fragment.Explorer.Search.Search;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Explorer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Explorer extends AbstractFragment implements OnItemClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;

    private SharedPreferences sharedPreferences ;

    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.rgb(236,91,76);
    private int currentPage;
    private Timer timer;
    private final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    private String firstLogIn;
    private static final float MIN_SCALE = 0.75f;
    private TextView searchButton, explore, editorChoice, seeAll;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_explorer;
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
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        explore.setText(resources.getString(R.string.explorer));
        editorChoice.setText(resources.getString(R.string.editor_choice));
        seeAll.setText(resources.getString(R.string.see_all));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView rvCourses = view.findViewById(R.id.rv_course_explorer);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        firstLogIn = mParam2;

        explore = view.findViewById(R.id.explorer);
        editorChoice = view.findViewById(R.id.editor_choice);
        seeAll = view.findViewById(R.id.see_all_explorer);

        List<Course> mCourses = new CourseHandler(getContext(), null, null,1).loadCourseHandler("All");

        CourseExplorerRecyclerViewAdapter mCourseExplorerAdapter = new CourseExplorerRecyclerViewAdapter(getContext(), mCourses);
        mCourseExplorerAdapter.setData(mCourses);
        mCourseExplorerAdapter.setClickedListener(this);
        rvCourses.setAdapter(mCourseExplorerAdapter);

        rvCourses.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        searchButton = view.findViewById(R.id.search_explorer_btn);
        searchButton.setOnClickListener(v -> switchToSearch());

//        TextView slidePagerText = view.findViewById(R.id.explorer_slide_pager_text);
//        TextView slidePagerCategory = view.findViewById(R.id.category_slide_pager_text);
//        TextView slidePagerView = view.findViewById(R.id.view_slide_pager_text);

        Integer[] urls = {
                (R.drawable.orange_background),
                (R.drawable.yellow_background),
                (R.drawable.blue_background),
                (R.drawable.green_background),
                (R.drawable.violet_background)
        };

        ViewPager imageSlider = view.findViewById(R.id.imageSlider_explorer);
        ExplorerImageSlideAdapter imageSliderAdapter = new ExplorerImageSlideAdapter(requireContext(), urls);
        imageSlider.setAdapter(imageSliderAdapter);

        setScrollSpeed(currentPage, urls, imageSlider);
        autoScrollPageAdapter(urls, imageSlider);

        // Indicator
        LinearLayout indicator = view.findViewById(R.id.indicator_explorer);
        for (int i = 0; i < urls.length; i++) {
            // COLOR_ACTIVE ứng với chấm ứng với vị trí hiện tại của ViewPager,
            // COLOR_INACTIVE ứng với các chấm còn lại
            // ViewPager có vị trí mặc định là 0, vì vậy color ở vị trí i == 0 sẽ là COLOR_ACTIVE
            indicator.addView(createDot(indicator.getContext(), i == 0 ? COLOR_ACTIVE : COLOR_INACTIVE));
        }

        // Change color of the dot the page scroll
        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < urls.length; i++) {
                    indicator.getChildAt(i).getBackground().mutate().setTint(i == position ? COLOR_ACTIVE : COLOR_INACTIVE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                editor.putInt("Page", currentPage - 1).apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageSlider.setPageTransformer(false, this::deepPageTransition);
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

    private void autoScrollPageAdapter(Integer[] totalPages, ViewPager viewPager) {

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == totalPages.length) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage, true);
            currentPage += 1;
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void setScrollSpeed(int currentPage, Integer[] totalPages, ViewPager viewPager) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
            if (currentPage == totalPages.length) {
                scroller.setmDuration(50);
            } else {
                scroller.setmDuration(1000);
            }
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {
        }
    }

    private void deepPageTransition (View view1, float position){
        int pageWidth = view1.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view1.setAlpha(0f);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view1.setAlpha(1f);
            view1.setTranslationX(0f);
            view1.setScaleX(1f);
            view1.setScaleY(1f);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view1.setAlpha(1 - position);

            // Counteract the default slide transition
            view1.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view1.setScaleX(scaleFactor);
            view1.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view1.setAlpha(0f);
        }
    }

    private void switchToSearch() {
        Fragment fragment = new Search();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, fragment, "Tag");
        fragmentTransaction.addToBackStack("Tag").commit();
    }

    @Override
    public void onAddClicked(TextView textView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        textView.clearAnimation();
        textView.startAnimation(a);
    }

    private void switchToCourseView(Course course) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, CourseView.newInstance(course.getCourseImage(), course.getCategory(), course.getBeforeSalePrice(), course.getAfterSalePrice(), course.getCourseName(), course.getRate()), "view");
        fragmentTransaction.addToBackStack("view").commit();
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
        editor.putInt("Page", currentPage - 1).apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Page", currentPage).apply();
    }

}
