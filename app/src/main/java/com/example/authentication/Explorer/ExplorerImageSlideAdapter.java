package com.example.authentication.Explorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.authentication.R;

public class ExplorerImageSlideAdapter extends PagerAdapter {

    private int[] background ;
    private String[] title;
    private String[] category;
    private LayoutInflater inflater;


    public ExplorerImageSlideAdapter(Context context, int[] background, String[] title, String[] category) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.background = background;
        this.title = title;
        this.category = category;
    }

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
//        final Context context = container.getContext();
//        final AppCompatImageView imageView = new AppCompatImageView(context);
//
//        container.addView(imageView);

        View itemView;
        itemView = inflater.inflate(R.layout.slide_pager_explorer, container, false);

        LinearLayout layout = itemView.findViewById(R.id.background_slide_pager);
        TextView titleText = (TextView) itemView.findViewById(R.id.explorer_slide_pager_text);
        TextView categoryText = (TextView) itemView.findViewById(R.id.category_slide_pager_text);
        TextView viewText = (TextView) itemView.findViewById(R.id.view_slide_pager_text);

        layout.setBackgroundResource(background[position]);

        titleText.setText(title[position]);
        categoryText.setText(category[position]);

        viewText.setText("VIEW");
        viewText.setBackgroundResource(R.drawable.text_field);

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // container chính là ViewPager, còn Object chính là return của instantiateItem ứng với position
        container.removeView((View) object);
    }

}
