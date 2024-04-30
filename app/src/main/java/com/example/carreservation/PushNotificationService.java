//package com.example.carreservation;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationManagerCompat;
//
//
//public class PushNotificationService extends FirebaseMessagingService {
//
//
////    public void onTokenRefresh() {
////        // Get updated InstanceID token.
////        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
////        Log.d("TAG", "Refreshed token: " + refreshedToken);
////
////        // If you want to send messages to this application instance or
////        // manage this apps subscriptions on the server side, send the
////        // Instance ID token to your app server.
//////        sendRegistrationToServer(refreshedToken);
////    }
//
//    public PushNotificationService() {
//        FirebaseInstallations.getInstance().getId().addOnCompleteListener(
//                task -> {
//                    if (task.isSuccessful()) {
//                        String token = task.getResult();
//                        Log.i("token ---->>", token);
//
//                        // store the token in shared preferences
//
////                        PrefUtils.getInstance(getApplicationContext()).setValue(PrefKeys.FCM_TOKEN, token);
//
//                    }
//                }
//        );
//    }
//
//    @Override
//    public void onNewToken(@NonNull String token) {
//        Log.e("newToken", token);
//        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();
//        super.onNewToken(token);
//    }
//
//    public static String getToken(Context context) {
//        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
//    }
//
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage message) {
//       String title =  message.getNotification().getTitle();
//       String body =  message.getNotification().getBody();
//       final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Heads Up Notifications",
//                    NotificationManager.IMPORTANCE_HIGH
//            ) ;
//            getSystemService(NotificationManager.class).createNotificationChannel(channel);
//          Notification.Builder notification = new Notification.Builder(
//                    this,
//                    CHANNEL_ID
//            )
//                  .setContentTitle(title)
//                  .setContentText(body)
//                  .setSmallIcon(R.drawable.ic_launcher_background)
//                  .setAutoCancel(true);
//            NotificationManagerCompat.from(this).notify(1,notification.build());
//        }
//
//       super.onMessageReceived(message);
//    }
//}
