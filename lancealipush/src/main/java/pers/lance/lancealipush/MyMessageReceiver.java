package pers.lance.lancealipush;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

/**
 * Created by fermo.fxd on 2017/10/20.
 */

public class MyMessageReceiver extends MessageReceiver {
  // 消息接收部分的LOG_TAG
  public static final String REC_TAG = "receiver";
  public static final int CLICK_NOTIFICATION_CODE = 1;
  public static final int DELETE_NOTIFICATION_CODE = 0;

  public static final String NOTIFICATION_CLICK = "com.zfzn.push.NOTIFICATION_CLICK";
  public static final String NOTIFICATION_DELETE = "com.zfzn.push.NOTIFICATION_DELETE";

  @Override
  public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
    Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);

//        buildNotification(context,cPushMessage);
  }

  @Override
  public void onMessage(Context context, CPushMessage cPushMessage) {
    Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
//        buildNotification(context,cPushMessage);
  }

  /**
   * 接受到对应消息后，消息的弹出处理
   */
  public void buildNotification(Context context, CPushMessage message, String channelId) {
//    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//    Notification notification = new NotificationCompat.Builder(context, channelId)
//      .setContentTitle(message.getTitle())
//      .setContentText(message.getContent())
//      .setSmallIcon(R.mipmap.small_icon)
//      .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//      .setPriority(Notification.PRIORITY_DEFAULT)
//      .setAutoCancel(true)
//      .build();
//    notification.contentIntent = buildClickContent(context, message);
//    notification.deleteIntent = buildDeleteContent(context, message);
//    notificationManager.notify(message.hashCode(), notification);
  }

  public PendingIntent buildClickContent(Context context, CPushMessage message) {
    Intent clickIntent = new Intent();
    clickIntent.setAction(NOTIFICATION_CLICK);
    clickIntent.putExtra("message key", message);//将message放入intent中，方便通知自建通知的点击事件
    return PendingIntent.getService(context, CLICK_NOTIFICATION_CODE, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public PendingIntent buildDeleteContent(Context context, CPushMessage message) {
    Intent deleteIntent = new Intent();
    deleteIntent.setAction(NOTIFICATION_DELETE);
    //添加其他数据
    deleteIntent.putExtra("message key", message);//将message放入intent中，方便通知自建通知的点击事件
    return PendingIntent.getService(context, DELETE_NOTIFICATION_CODE, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  //    @Override
//    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
//        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
//
////        Intent intent=new Intent(NotificationService.ACTION_POMS);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
  @Override
  protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
    Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
  }

  @Override
  protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
    Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
  }

  @Override
  protected void onNotificationRemoved(Context context, String messageId) {
    Log.e("MyMessageReceiver", "onNotificationRemoved");
  }
}
