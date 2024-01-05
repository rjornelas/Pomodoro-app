package com.rjornelas.pomodoro

import android.app.Activity
import android.content.Context
import android.widget.TextView
import java.util.Locale

class FormatText(
    context: Context,
    textTime: Int
) {

    var textTime: TextView = (context as Activity).findViewById(textTime)

    fun updateCountDownText(timeLeftInMillis: Long) {
        val minutes: Int = (timeLeftInMillis.toInt() / 1000) / 60
        val seconds: Int = (timeLeftInMillis.toInt() / 1000) % 60
        val timeLeftFormatted: String =
            java.lang.String.format(
                Locale.getDefault(),
                "%02d:%02d", minutes, seconds
            )
        textTime.text = timeLeftFormatted
    }
}