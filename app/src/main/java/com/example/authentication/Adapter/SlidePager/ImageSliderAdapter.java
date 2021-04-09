package com.example.authentication.Adapter.SlidePager;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.authentication.Adapter.SlidePager.Abstract.AbstractSlidePagerAdapter;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.R;

public class ImageSliderAdapter extends AbstractSlidePagerAdapter<Integer> {

    private Integer[] background;
    private String[] title;
    private LayoutInflater inflater;
    private Context context;
    public ImageSliderAdapter(int currentPage, Context context, Integer[] background, ViewPager viewPager) {
        super(currentPage, background, viewPager);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.background = background;
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide_pager;
    }

    @Override
    public void bind(View itemView, @NonNull ViewGroup container, int position, String language) {
        LinearLayout layout = itemView.findViewById(R.id.background_slide_pager_home);
        TextView titleText = (TextView) itemView.findViewById(R.id.home_slide_pager_text);
        Context context = LocaleHelper.setLocale(this.context, language);
        Resources resources = context.getResources();

        String[] title = new String[]{
                resources.getString(R.string.title_home1),
                resources.getString(R.string.title_home2),
                resources.getString(R.string.title_home3),
                resources.getString(R.string.title_home4),
                resources.getString(R.string.title_home5),
                resources.getString(R.string.title_home6)
        };
        layout.setBackgroundResource(background[position]);
        titleText.setText(title[position]);
    }

}
