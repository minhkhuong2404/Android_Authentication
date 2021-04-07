package com.example.authentication.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractRecyclerViewAdapter;
import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractViewHolder;
import com.example.authentication.Object.Badges.Badge;
import com.example.authentication.Object.Badges.OnBadgeClickListener;
import com.example.authentication.R;

import java.util.List;

public class BadgeRecyclerViewAdapter extends AbstractRecyclerViewAdapter<Badge, BadgeRecyclerViewAdapter.BadgeViewHolder> {
    private List<Badge> mBadges;
    private Context mContext;
    private OnBadgeClickListener clickedListener;
    private int lastPosition = -1;

    public BadgeRecyclerViewAdapter(Context context, List<Badge> data) {
        mContext = context;
        mBadges = data;
    }

    public void setClickedListener(OnBadgeClickListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.badge_item;
    }

    @Override
    public BadgeViewHolder initViewHolder(View view, @NonNull ViewGroup parent, int viewType) {
        return new BadgeViewHolder(view);
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

    class BadgeViewHolder extends AbstractViewHolder<Badge> {
        private ImageView tvBadgeIcon;


        @SuppressLint("ClickableViewAccessibility")
        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position, Badge o) {
            TextView tvBadgeName = itemView.findViewById(R.id.badge_name);
            TextView tvBadgeRequirement = itemView.findViewById(R.id.badge_requirement);
            tvBadgeIcon = itemView.findViewById(R.id.badge_icon);

            Badge badge = mBadges.get(position);

            tvBadgeName.setText(badge.getBadgeName());
            tvBadgeRequirement.setText(badge.getBadgeRequirement());
            Glide.with(mContext).load(badge.getBadgeIcon()).into(tvBadgeIcon);

            itemView.setOnClickListener(v -> {

                if (clickedListener!= null){
                    clickedListener.onViewClicked(badge);
                }

            });

            tvBadgeIcon.setOnClickListener(v -> {

                if (clickedListener!= null){
                    clickedListener.onBadgeClicked(tvBadgeIcon);
                }
            });

            tvBadgeName.setOnClickListener(v -> {

                if (clickedListener!= null){
                    clickedListener.onViewClicked(badge);
                }
                v.setOnClickListener(null);
            });
            setAnimation(itemView, position);
        }
    }
}
