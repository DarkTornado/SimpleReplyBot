package com.darktornado.simplereplybot

import android.app.Notification
import android.app.PendingIntent.CanceledException
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Bundle

class Replier(ctx: Context, actions: Array<Notification.Action>) {

    val ctx: Context
    var read: Notification.Action? = null;
    var reply: Notification.Action? = null;

    init {
        this.ctx = ctx;
        for (action in actions) {
            if (action.remoteInputs != null && action.remoteInputs.isNotEmpty()) reply = action
            else read = action
        }
    }


    fun reply(value: String?): Boolean {
        if (reply == null) return false
        val intent = Intent()
        val msg = Bundle()
        for (input in reply!!.remoteInputs) {
            msg.putCharSequence(input.resultKey, value)
        }
        RemoteInput.addResultsToIntent(reply!!.remoteInputs, intent, msg)
        try {
            reply!!.actionIntent.send(ctx, 0, intent)
        } catch (e: CanceledException) {
        }
        return true
    }

    fun markAsRead(): Boolean {
        return if (read == null) false else try {
            read!!.actionIntent.send(ctx, 1, Intent())
            true
        } catch (e: CanceledException) {
            false
        }
    }


}
