package com.example.taskmaster.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationDetails;
import com.example.taskmaster.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class PushListenerService extends FirebaseMessagingService {
    public static final String TAG = PushListenerService.class.getSimpleName();

    private static final String CHANNEL_ID = "CHANNEL_ID";

    // Intent action used in local broadcast
    public static final String ACTION_PUSH_NOTIFICATION = "push-notification";
    // Intent keys
    public static final String INTENT_SNS_NOTIFICATION_FROM = "from";
    public static final String INTENT_SNS_NOTIFICATION_DATA = "data";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.i(TAG, "Registering push notifications token: " + token);
        MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient().registerDeviceToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "Message: " + remoteMessage.getData().toString());

        // this code works from google notification docs
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Test Notification")
                .setContentText(remoteMessage.getData().toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        final NotificationClient notificationClient = MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient();

        final NotificationDetails notificationDetails = NotificationDetails.builder()
                .from(remoteMessage.getFrom())
                .mapData(remoteMessage.getData())
                .intentAction(NotificationClient.FCM_INTENT_ACTION)
                .build();

        NotificationClient.CampaignPushResult pushResult = notificationClient.handleCampaignPush(notificationDetails);

        if (!NotificationClient.CampaignPushResult.NOT_HANDLED.equals(pushResult)) {
            /**
             The push message was due to a Pinpoint campaign.
             If the app was in the background, a local notification was added
             in the notification center. If the app was in the foreground, an
             event was recorded indicating the app was in the foreground,
             for the demo, we will broadcast the notification to let the main
             activity display it in a dialog.
             */
            if (NotificationClient.CampaignPushResult.APP_IN_FOREGROUND.equals(pushResult)) {
                /* Create a message that will display the raw data of the campaign push in a dialog. */
                final HashMap<String, String> dataMap = new HashMap<>(remoteMessage.getData());
                broadcast(remoteMessage.getFrom(), dataMap);
            }
            return;
        }
    }

    private void broadcast(final String from, final HashMap<String, String> dataMap) {
        Intent intent = new Intent(ACTION_PUSH_NOTIFICATION);
        intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from);
        intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, dataMap);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Helper method to extract push message from bundle.
     *
     * @param data bundle
     * @return message string from push notification
     */
    public static String getMessage(Bundle data) {
        return ((HashMap) data.get("data")).toString();
    }
}