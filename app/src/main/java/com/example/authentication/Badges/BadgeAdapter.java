package com.example.authentication.Badges;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.R;

import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>{

    private static final String TAG = "CourseAdapter";
    private List<Badge> mBadges;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public BadgeAdapter(Context context, List<Badge> data) {
        mContext = context;
        mBadges = data;
        mLayoutInflater = LayoutInflater.from(context);
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
        holder.tvBadgeIcon.setBackgroundResource(badge.getBadgeIcon());


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


            tvBadgeName.setOnClickListener(v -> {
                Badge badge = mBadges.get(getAdapterPosition());
                Toast.makeText(mContext, badge.getBadgeName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
