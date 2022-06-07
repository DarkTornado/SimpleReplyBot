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
        if(sbn.getPackageName().equals("com.kakao.talk")) return;
        try {
            Notification.Action[] actions = sbn.getNotification().actions;
            if (actions == null || actions.length == 0) return;

            Bundle bundle = sbn.getNotification().extras;
            String msg = bundle.get("android.text").toString();
            String sender = bundle.getString("android.title");
            String room = bundle.getString("android.subText");
            boolean isGroupChat = room != null;
            if (room == null) room = sender;
            Replier replier = new Replier(this, actions);

            response(room, msg, sender, isGroupChat, replier);
        } catch (Exception e) {
            Log.i(Bot.TAG, e.toString());
        }
    }

    private void response(String room, String msg, String sender, boolean isGroupChat, Replier replier) {
        if (msg.equals("/test")) {
            replier.reply("reply test");
        }
    }

}
