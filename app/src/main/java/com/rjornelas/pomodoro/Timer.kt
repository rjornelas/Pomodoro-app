package com.rjornelas.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.Locale

    class Timer : AppCompatActivity() {

    private var tvTime: TextView? = null
    private var tvBreaks: TextView? = null
    private var imgPlay:ImageView? = null
    private var imgReplay: ImageView? = null
    private var progressbar: ProgressBar? = null
    private var timerLength: Int? = null
    private var breaksCount: Int = 0
    private var totalBreaks: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        init()
        getData()

        imgPlay!!.setOnClickListener {
            if (timerStatus == TimerStatus.STARTED){
                pauseTimer()
            }else{
                startTimer()
            }
        }

        imgReplay!!.setOnClickListener {
            resetTimer()
        }
        updateCountDownText()
        setProgressBarValues()
    }

    private fun init(){
        tvTime = findViewById(R.id.tvTimePlay)
        tvBreaks = findViewById(R.id.tvTotalBreaks)
        progressbar = findViewById(R.id.progressBar)
        imgPlay = findViewById(R.id.img_play)
        imgReplay = findViewById(R.id.img_replay)
    }

        private enum class TimerStatus{
            STARTED, STOPPED
        }

        private var timerStatus = TimerStatus.STARTED

        var timeLeftInMillis:Long = 1 * 60000
        private lateinit var countDownTimer: CountDownTimer

        private fun getData(){

            timerLength = intent.getIntExtra("TIME", 1)
            totalBreaks = intent.getIntExtra("BREAKS", 0)

            if (totalBreaks  == 0){
                tvBreaks!!.visibility = View.GONE
            }else{
                tvBreaks!!.text = breaksCount.toString() + "/" + totalBreaks.toString()
            }

            timeLeftInMillis = (timerLength!! * 60 * 1000).toLong()
            startTimer()

        }

        private fun setProgressBarValues(){
            progressbar!!.max = timeLeftInMillis.toInt() / 1000
            progressbar!!.progress = timeLeftInMillis.toInt() / 1000
        }

        private fun startTimer() {
            countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000){

                override fun onTick(millisUntilFinished: Long) {
                    timeLeftInMillis = millisUntilFinished
                    imgPlay!!.setImageResource(android.R.drawable.ic_media_pause)
                    updateCountDownText()
                    progressbar!!.progress = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    timerStatus = TimerStatus.STOPPED
                    resetTimer()
                }

            }.start()
            timerStatus = TimerStatus.STARTED
        }

        private fun pauseTimer(){
            countDownTimer.cancel()
            timerStatus = TimerStatus.STOPPED
            imgPlay!!.setImageResource(android.R.drawable.ic_media_play)
        }

        private fun resetTimer(){
            pauseTimer()
            timeLeftInMillis = (timerLength!! * 60 * 1000).toLong()
            setProgressBarValues()
            updateCountDownText()
        }

        private fun updateCountDownText() {
            val minutes: Int = (timeLeftInMillis.toInt() / 1000) / 60
            val seconds: Int = (timeLeftInMillis.toInt() / 1000) % 60
            val timeLeftFormatted: String =
                java.lang.String.format(
                    Locale.getDefault(),
                    "%02d:%02d", minutes, seconds
                )
            tvTime!!.text = timeLeftFormatted
        }
}