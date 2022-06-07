package com.darktornado.simplereplybot

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this)
        layout.orientation = 1
        val on = Switch(this)
        on.text = "봇 활성화"
        on.isChecked = Bot.readBoolean(this, "botOn")
        on.setOnCheckedChangeListener { swit, onoff ->
            Bot.saveBoolean(this, "botOn", onoff)
        }
        layout.addView(on)

        setContentView(layout)
    }
}