package pers.lance.lancealipush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

  private final String CHANNEL_ID_EVENT = "com.zfzn.tuanbo.event";

  private WeakReference<Context> mContext;

  private android.app.NotificationManager mManager;

  private int counter = 0;

  private static NotificationManager instance;

  private NotificationManager(Context context) {

    this.mContext = new WeakReference<>(context.getApplicationContext());

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

      List<NotificationChannel> channels = new ArrayList<>();

      NotificationChannel patrolChannel = new NotificationChannel(CHANNEL_ID_EVENT, mContext.get().getString(R.string.channel_name_patrol), android.app.NotificationManager.IMPORTANCE_DEFAULT);
      patrolChannel.setDescription(mContext.get().getString(R.string.channel_description_patrol));
      patrolChannel.enableLights(true);
      patrolChannel.setLightColor(Color.WHITE);
      channels.add(patrolChannel);

      mManager = (android.app.NotificationManager) mContext.get().getSystemService(Context.NOTIFICATION_SERVICE);
      mManager.createNotificationChannels(channels);
    }

  }

  public static NotificationManager getInstance(Context context) {

    if (instance == null)
      synchronized (NotificationManager.class) {
        if (instance == null) {
          instance = new NotificationManager(context);
        }
      }
    return instance;
  }

  public void showPatrolNotification(String content) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext.get(), CHANNEL_ID_EVENT);
    builder.setAutoCancel(true).setContentTitle(mContext.get().getString(R.string.channel_name_patrol)).setContentText(content).setSmallIcon(R.mipmap.small_icon).setLargeIcon(BitmapFactory.decodeResource(mContext.get().getResources(), R.mipmap.ic_launcher)).setDefaults(Notification.DEFAULT_ALL);
    NotificationManagerCompat.from(mContext.get()).notify(counter++, builder.build());
  }
}
