package com.example.authentication.Adapter.SlidePager.Abstract;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.authentication.R;

abstract public class AbstractSlidePagerAdapter<DATA> extends PagerAdapter {

    DATA[] data;
    public AbstractSlidePagerAdapter(DATA[] data) {
        this.data = data;
    }

    abstract public int getLayoutId();
    abstract public void bind(View view, @NonNull ViewGroup container, int position, String language);

    @Override
    public int getCount() {
        return data.length;
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
}
