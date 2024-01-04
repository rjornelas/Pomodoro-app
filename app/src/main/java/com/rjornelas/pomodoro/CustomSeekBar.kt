package com.rjornelas.pomodoro

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class CustomSeekbar: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.seekbar_dialog, null)
        val alert = AlertDialog.Builder(activity)

        val seekbarTime: SeekBar = view.findViewById(R.id.sbTime)
        val seekbarBreaks: SeekBar = view.findViewById(R.id.sbRepetitions)
        val tvTime: TextView = view.findViewById(R.id.tvTimeSelected)
        val tvBreaks: TextView = view.findViewById(R.id.tvRepetitionsSelected)

        seekbarTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvTime.text = "$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekbarBreaks.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvBreaks.text = "$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        with(alert){
            setView(view)
            setTitle("Timer Setting")
            setPositiveButton("Start"){ _, _ -> onPositiveClick(
                tvTime.text.toString().toInt(),
                tvBreaks.text.toString().toInt()
            )}
        }
        return alert.create()
    }

    private fun onPositiveClick(time: Int, breaks: Int) {
        val i = Intent(requireActivity().baseContext, Timer::class.java)
        if(time == 0){
            i.putExtra("TIME", 1)
        }else{
            i.putExtra("TIME", time)
        }
        i.putExtra("BREAKS", breaks)
        startActivity(i)
    }
}