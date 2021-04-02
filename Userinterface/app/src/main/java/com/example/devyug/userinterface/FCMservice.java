package com.example.devyug.userinterface;

/**
 * Created by dell on 25/9/17.
 */

        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.media.RingtoneManager;
        import android.net.Uri;

        import android.support.v4.app.NotificationCompat;
        import android.util.Log;
        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;

        import java.util.List;
        import java.util.ListIterator;
        import java.util.Random;

public class FCMservice extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    userdatabase db;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationTitle = null, notificationBody = null;
        String dataTitle = null, dataMessage = null;
        db = new userdatabase(getApplicationContext());
        List<String> l = db.getwords();
        ListIterator<String> i = l.listIterator();
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("message"));
            dataTitle = remoteMessage.getData().get("title");
            dataMessage = remoteMessage.getData().get("message");
            if (!dataTitle.equals(UserDetails.username)) {
                int flag = 0;
                if (db.getfilter()) {
                    Log.w("in filter", " k");

                    while (i.hasNext()) {
                    String s = i.next();
                    Log.w("word", s);
                    if (check(dataMessage, s)) {
                        Log.w("filter found", s);
                        flag = 1;
                        break;
                    }


                     }
                    if (flag == 1)
                        sendNotification(dataTitle, dataMessage);
                    // sendNotification("in else", "hi");

                } else {
                    // sendNotification("in else", "hi");
                    sendNotification(dataTitle, dataMessage);
                }
            }


        }
    }








    private void sendNotification(String notificationTitle, String notificationBody) {
        //  UserDetails.chatWith=notificationTitle;
        Random rand = new Random();
        Intent intent = new Intent(this, slider.class);
        //  intent.putExtra("title", dataTitle);
        //  intent.putExtra("message", dataMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);




        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.spla)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(rand.nextInt(1000)/* ID of notification_pojo */, notificationBuilder.build());
    }
    private boolean check(String msg,String filter)
    {
        while(msg.length()!=0) {
            String[] arrstr = msg.split(" ",2);

            if(arrstr[0].equalsIgnoreCase(filter))
                return true;
            if(arrstr.length>1)
                msg=arrstr[1];
            else
                break;


        }

        return false;
    }
}
