package com.example.authentication.Adapter.SlidePager.Abstract;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.authentication.Adapter.SlidePager.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

abstract public class AbstractSlidePagerAdapter<DATA> extends PagerAdapter {

    private DATA[] background;
    private int currentPage = 0;
    private final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    private ViewPager viewPager;

    public AbstractSlidePagerAdapter(int currentPage, DATA[] data, ViewPager viewPager) {
        this.background = data;
        this.currentPage = currentPage;
        this.viewPager = viewPager;
    }

    abstract public int getLayoutId();
    abstract public void bind(View view, @NonNull ViewGroup container, int position, String language);

    @Override
    public int getCount() {
        return background.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = ((LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getLayoutId(), container, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(container.getContext());
        String language = sharedPreferences.getString("Locale.Helper.Selected.Language", "en");
        bind(view, container, position, language);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void init() {
        autoScrollPageAdapter(background, viewPager);
        setScrollSpeed(currentPage, background, viewPager);
    }

    private void autoScrollPageAdapter(DATA[] totalPages, ViewPager viewPager) {

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == totalPages.length) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage, true);
            currentPage += 1;
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    public View createDot(Context context, @ColorInt int color) {
        View dot = new View(context);
        LinearLayout.MarginLayoutParams dotParams = new LinearLayout.MarginLayoutParams(20, 20);
        dotParams.setMargins(20, 10, 20, 10);
        dot.setLayoutParams(dotParams);
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.setTint(color);
        dot.setBackground(drawable);
        return dot;
    }

    private void setScrollSpeed(int currentPage, DATA[] totalPages, ViewPager viewPager) {
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
}
