package com.example.authentication.Walkthrough;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.authentication.MainActivity;
import com.example.authentication.NewAccount;
import com.example.authentication.R;

public class SlideViewPagerAdapter extends PagerAdapter {

    Context ctx;

    public SlideViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater= (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_screen,container,false);

        ImageView ind1=view.findViewById(R.id.ind1);
        ImageView ind2=view.findViewById(R.id.ind2);
        ImageView ind3=view.findViewById(R.id.ind3);

        TextView title=view.findViewById(R.id.title);
        TextView desc=view.findViewById(R.id.desc);

        TextView next=view.findViewById(R.id.next);
        TextView back=view.findViewById(R.id.back);

        TextView btnGetStarted=view.findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, NewAccount.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK); //Intent.FLAG_ACTIVITY_CLEAR_TASK  vs Intent.FLAG_ACTIVITY_TASK_ON_HOME
                ctx.startActivity(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideActivity.viewPager.setCurrentItem(position+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideActivity.viewPager.setCurrentItem(position-1);
            }
        });

        switch (position)
        {
            case 0:
                ind1.setImageResource(R.drawable.walkthrough_selected);
                ind2.setImageResource(R.drawable.walkthrough_unselected);
                ind3.setImageResource(R.drawable.walkthrough_unselected);

                title.setText("Elearning Platform");
                desc.setText("Contrary to popular belief. lorem ipsum tesing puspose");
//                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                break;
            case 1:
                ind1.setImageResource(R.drawable.walkthrough_unselected);
                ind2.setImageResource(R.drawable.walkthrough_selected);
                ind3.setImageResource(R.drawable.walkthrough_unselected);

                title.setText("Be Focus");
                desc.setText("To achieve success in your life. lorem ipsum tesing puspose");
//                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 2:
                ind1.setImageResource(R.drawable.walkthrough_unselected);
                ind2.setImageResource(R.drawable.walkthrough_unselected);
                ind3.setImageResource(R.drawable.walkthrough_selected);

                title.setText("Professional");
                desc.setText("Most courses are from top lectures. lorem ipsum tesing puspose");
//                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                break;
        }

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

