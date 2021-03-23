package com.example.authentication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.authentication.Notification.NotificationItem;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SharedPreferences sharedPreferences ;
    private int numberOfMessages = 1;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            Intent intent = new Intent("Notification");
            intent.putExtra("notify", remoteMessage.getNotification().getBody());
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            try {
                sendNotification(remoteMessage.getNotification().getBody());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        Log.d("Job", "Hello");
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) throws ParseException {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationHandler notifHandler = new NotificationHandler(this, null,null,1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // get username
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("username", "No name");

        // choose icon depends on the notification title
        int which_icon = 0;
        if (messageBody.toLowerCase().contains("message")) {
            which_icon = R.drawable.message;
        } else if (messageBody.toLowerCase().contains("video")) {
            which_icon = R.drawable.videocam;
        } else if (messageBody.toLowerCase().contains("badge")) {
            which_icon = R.drawable.badge;
        } else if (messageBody.toLowerCase().contains("information")) {
            which_icon = R.drawable.info;
        } else {
            which_icon = R.drawable.noti;
        }

        // add raw datetime to the notification
        Date date = new Date();
        String dateStr = inputFormat.format(date);
        notifHandler.addDataHandler(new NotificationItem(dateStr, messageBody, which_icon, dateStr));

        // load all notification items
        List<NotificationItem> notificationItems = notifHandler.loadDataHandler();

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // for loop to get all notification items in descending order
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        for (int i = notificationItems.size() - 1; i >= 0; i--) {
            Date date1 = inputFormat.parse(notificationItems.get(i).getNotificationCreationTime());
            Notification notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                            .setContentTitle("E-learning")
                            .setContentText(notificationItems.get(i).getNotificationInformation())
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setWhen(date1.getTime())
                            .setGroup("MESSAGE")
                            .build();
            notificationManager.notify(i+1, notificationBuilder);
        }

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // create a summary notification to keep all the notifications
        Notification summaryNotification =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("All notifications")
                        .setContentText(notificationItems.size() + " new messages")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine(username + "! Check this out")
                                .setBigContentTitle(notificationItems.size() + " new messages")
                                .setSummaryText("You have " + notificationItems.size() + " new messages"))
                        //specify which group this notification belongs to
                        .setGroup("MESSAGE")
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        notificationManager.notify(0, summaryNotification);
    }

}
