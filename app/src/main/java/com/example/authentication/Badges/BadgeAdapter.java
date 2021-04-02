package com.example.authentication.Badges;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;

import java.util.List;
import java.util.logging.Handler;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>{

    private static final String TAG = "CourseAdapter";
    private List<Badge> mBadges;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnBadgeClickListener clickedListener;
    private int lastPosition = -1;

    public BadgeAdapter(Context context, List<Badge> data) {
        mContext = context;
        mBadges = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setClickedListener(OnBadgeClickListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    @NonNull
    @Override
    public BadgeAdapter.BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.badge_item, parent, false);
        return new BadgeAdapter.BadgeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeAdapter.BadgeViewHolder holder, int position) {
        Badge badge = mBadges.get(position);

        holder.tvBadgeName.setText(badge.getBadgeName());
        holder.tvBadgeRequirement.setText(badge.getBadgeRequirement());
        Glide.with(mContext).load(badge.getBadgeIcon()).into(holder.tvBadgeIcon);

        holder.itemView.setOnClickListener(v -> {

            if (clickedListener!= null){
                clickedListener.onViewClicked(badge);
            }

        });

        holder.tvBadgeIcon.setOnClickListener(v -> {

            if (clickedListener!= null){
                clickedListener.onBadgeClicked(holder.tvBadgeIcon);
            }
        });

        holder.tvBadgeName.setOnClickListener(v -> {

            if (clickedListener!= null){
                clickedListener.onViewClicked(badge);
            }
            v.setOnClickListener(null);
        });
        setAnimation(holder.itemView, position);

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return mBadges.size();
    }

    class BadgeViewHolder extends RecyclerView.ViewHolder{
        private TextView tvBadgeName;
        private TextView tvBadgeRequirement;
        private ImageView tvBadgeIcon;


        @SuppressLint("ClickableViewAccessibility")
        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBadgeName = itemView.findViewById(R.id.badge_name);
            tvBadgeRequirement = itemView.findViewById(R.id.badge_requirement);
            tvBadgeIcon = itemView.findViewById(R.id.badge_icon);

        }
    }
}
