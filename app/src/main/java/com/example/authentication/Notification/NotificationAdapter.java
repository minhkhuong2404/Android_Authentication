package com.example.authentication.Notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authentication.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private static final String TAG = "NotificationAdapter";
    private List<NotificationItem> mNotifications;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public NotificationAdapter(Context context, List<NotificationItem> data) {
        mContext = context;
        mNotifications = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.NotificationViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem course = mNotifications.get(position);

        holder.tvNotificationIcon.setBackground(course.getNotificationIcon());
        holder.tvNotificationInfo.setText(course.getNotificationInformation());
        holder.tvNotificationTime.setText(course.getNotificationTime());

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNotificationInfo;
        private TextView tvNotificationTime;
        private TextView tvNotificationIcon;

        @SuppressLint("ClickableViewAccessibility")
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationInfo = itemView.findViewById(R.id.notification_info);
            tvNotificationTime = itemView.findViewById(R.id.notification_time_before);
            tvNotificationIcon = itemView.findViewById(R.id.notification_icon);

            tvNotificationInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    NotificationItem course = mNotifications.get(getAdapterPosition());
                    Toast.makeText(mContext, course.getNotificationInformation(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
