package com.rjornelas.pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class Timer : AppCompatActivity(), View.OnClickListener {

    private var tvTime: TextView? = null
    private var tvBreakCount: TextView? = null
    private var imgPlay:ImageView? = null
    private var imgReplay: ImageView? = null
    private var progressbar: ProgressBar? = null
    private var timerLength: Int? = null
    private var breakCount: Int = 0
    private var totalBreak: Int? = null
    private var formatText: FormatText? = null

    private var timerStatus = TimerStatus.STARTED

    var timeLeftInMillis:Long = 0
    private lateinit var countDownTimer: CountDownTimer

    companion object {
        const val ACTIVITY_BREAK = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        init()
        getData()
        initTimer()
    }

    private fun init(){
        tvTime = findViewById(R.id.tvTimePlay)
        tvBreakCount = findViewById(R.id.tvTotalBreaks)
        progressbar = findViewById(R.id.progressBar)
        imgPlay = findViewById(R.id.img_play)
        imgReplay = findViewById(R.id.img_replay)
    }

    private fun getData(){
        timerLength = intent.getIntExtra("TIME", 1)
        totalBreak = intent.getIntExtra("BREAKS", 0)
    }

    private fun initTimer() {
        imgPlay!!.setOnClickListener (this)
        imgReplay!!.setOnClickListener(this)

        if (totalBreak  == 0){
            tvBreakCount!!.visibility = View.GONE
        }else{
            tvBreakCount!!.text = String.format("%d / %d", breakCount, totalBreak)
        }

        timeLeftInMillis = (timerLength!! * 60000).toLong()

        setProgressBarValues()
        formatText = FormatText(this, R.id.tvTimePlay)
        formatText!!.updateCountDownText(timeLeftInMillis)

        startTimer()
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.img_play -> {
                if (timerStatus == TimerStatus.STARTED) {
                    pauseTimer()
                } else {
                    startTimer()
                }
            }

            R.id.img_replay -> resetTimer()
        }
    }

    private fun setProgressBarValues(){
        progressbar!!.max = timeLeftInMillis.toInt() / 1000
        progressbar!!.progress = timeLeftInMillis.toInt() / 1000
    }

    fun statusBreak(){
        if(breakCount == totalBreak){
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.title_finish_pomodoro))
            builder.setMessage(getString(R.string.message_finish_pomodoro))
            builder.setPositiveButton(getString(R.string.action_ok)){ _, _ -> finish()}

            val dialog = builder.create()
            dialog.show()
        }else{
            val intent = Intent(this@Timer, Break::class.java)
            startActivityIfNeeded(intent, ACTIVITY_BREAK)
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000){

            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                imgPlay!!.setImageResource(android.R.drawable.ic_media_pause)
                formatText!!.updateCountDownText(timeLeftInMillis)
                progressbar!!.progress = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                timerStatus = TimerStatus.STOPPED
                resetTimer()
                statusBreak()
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
        timeLeftInMillis = (timerLength!! * 60000).toLong()
        setProgressBarValues()
        formatText!!.updateCountDownText(timeLeftInMillis)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pauseTimer()

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_attention))
        builder.setMessage(getString(R.string.message_finish_timer))
        builder.setPositiveButton(getString(R.string.action_yes)){ _, _ -> finish()}
        builder.setNegativeButton(getString(R.string.action_no)){ _, _ -> startTimer()}

        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Companion.ACTIVITY_BREAK){
            breakCount++
            tvBreakCount!!.text = String.format("%d / %d", breakCount, totalBreak)
            startTimer()
        }
    }
}