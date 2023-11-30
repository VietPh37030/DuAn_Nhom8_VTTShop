package anhpvph37030.fpoly.duan_nhom8;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Xử lý thông báo ở đây
        if (remoteMessage.getData().size() > 0) {
            // Gọi hàm hiển thị thông báo
            hienThiThongBao(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        }

        // ...
    }

    private void hienThiThongBao(String title, String body) {
        // Tạo NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo đối tượng Notification
        Notification.Builder builder;

        // Kiểm tra phiên bản Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id"; // Đặt mã channel theo ý muốn
            String channelName = "Channel Name"; // Đặt tên channel theo ý muốn
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(this, channelId);
        } else {
            builder = new Notification.Builder(this);
        }

        // Thiết lập thông tin cho Notification
        builder
                .setSmallIcon(R.drawable.avt) // Thay thế bằng icon thông báo của bạn
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        // Tạo âm thanh cho thông báo
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(soundUri)  ;

        // Hiển thị thông báo
        notificationManager.notify(0, builder.build());
    }
}
