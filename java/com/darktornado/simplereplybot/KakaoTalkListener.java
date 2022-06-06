package com.darktornado.simplereplybot;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class KakaoTalkListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (!Bot.readBoolean(this, "botOn")) return;
        final String packageName = sbn.getPackageName();
        try {
            Notification.Action[] actions = sbn.getNotification().actions;
            if (actions.length == 0) return;

            Bundle bundle = sbn.getNotification().extras;
            String msg = bundle.get("android.text").toString();
            String sender = bundle.getString("android.title");
            String room = bundle.getString("android.subText");
            boolean isGroupChat = room != null;
            if (room == null) room = sender;

        } catch (Exception e) {
            Log.i(Bot.TAG, e.toString());
        }
        //toast(a);
    }

}
