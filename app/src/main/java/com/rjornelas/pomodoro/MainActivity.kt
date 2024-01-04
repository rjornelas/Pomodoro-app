package com.rjornelas.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSend: Button = findViewById(R.id.btnSend)
        btnSend.setOnClickListener{

            val seekBarDialog = CustomSeekbar()
            seekBarDialog.show(supportFragmentManager, "")

        }
    }
}