package com.example.authentication.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractRecyclerViewAdapter;
import com.example.authentication.Adapter.RecyclerView.Abstract.AbstractViewHolder;
import com.example.authentication.Object.NotificationItem;
import com.example.authentication.R;

import java.util.List;

public class NotificationRecyclerViewAdapter extends AbstractRecyclerViewAdapter<NotificationItem, NotificationRecyclerViewAdapter.NotificationViewHolder> {
    private List<NotificationItem> mNotifications;
    private Context mContext;
    private int lastPosition = -1;

    public NotificationRecyclerViewAdapter(Context context, List<NotificationItem> data) {
        mContext = context;
        mNotifications = data;
    }

    @Override
    public int getLayoutId() {
        return R.layout.notification_item;
    }

    @Override
    public NotificationViewHolder initViewHolder(View view, @NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(view);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            viewToAnimate.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.zoom_in));
            lastPosition = position;
        }
    }

    class NotificationViewHolder extends AbstractViewHolder<NotificationItem> {

        private TextView tvNotificationInfo, tvNotificationTime;
        private ImageView tvNotificationIcon;

        @SuppressLint("ClickableViewAccessibility")
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position, NotificationItem notificationItem) {
            tvNotificationInfo = itemView.findViewById(R.id.notification_info);
            tvNotificationTime = itemView.findViewById(R.id.notification_time_before);
            tvNotificationIcon = itemView.findViewById(R.id.notification_icon);

            tvNotificationInfo.setText(notificationItem.getNotificationInformation());
            tvNotificationTime.setText(notificationItem.getNotificationTime());
            Glide.with(mContext).load(notificationItem.getNotificationIcon()).placeholder(R.drawable.orange_background).into(tvNotificationIcon);

            tvNotificationInfo.setOnClickListener(v -> {
                Toast.makeText(mContext, mNotifications.get(getAdapterPosition()).getNotificationInformation(), Toast.LENGTH_SHORT).show();
            });

            setAnimation(itemView, position);

        }
    }
}
