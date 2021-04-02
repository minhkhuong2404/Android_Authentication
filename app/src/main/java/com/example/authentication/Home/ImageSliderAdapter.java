package com.example.authentication.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.authentication.R;

public class ImageSliderAdapter extends PagerAdapter {

    private int[] background;
    private String[] title;
    private LayoutInflater inflater;

    public ImageSliderAdapter(Context context, int[] background, String[] title) {
        this.title = title;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.background = background;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Tạo ImageView, container chính là ViewPager của chúng ta
//        final Context context = container.getContext();
//        final AppCompatImageView imageView = new AppCompatImageView(context);
//        container.addView(imageView);
//        // Load ảnh vào ImageView bằng Glide
//        Glide.with(context).load(title[position]).apply(new RequestOptions().override(800,300)).into(imageView);
//        // Return
//        return imageView;

        View itemView;
        itemView = inflater.inflate(R.layout.slide_pager, container, false);

        LinearLayout layout = itemView.findViewById(R.id.background_slide_pager_home);
        TextView titleText = (TextView) itemView.findViewById(R.id.home_slide_pager_text);

        layout.setBackgroundResource(background[position]);

        titleText.setText(title[position]);


        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // container chính là ViewPager, còn Object chính là return của instantiateItem ứng với position
        container.removeView((View) object);
    }

}
