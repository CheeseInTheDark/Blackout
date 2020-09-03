package com.argdar.blackout.activity

import android.graphics.Color.WHITE
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import android.widget.Button
import com.argdar.blackout.*
import com.argdar.blackout.intent.AlarmIntent.BLACKOUT_TIME_KEY

class AlarmActivityImpl(
    private val alarm: Alarm,
    private val handler: Handler,
    private val notificationBarBlocker: NotificationBarBlocker,
    private val systemClock: SystemClock,
    private val regretsScheduler: RegrettableConsequencesScheduler
) {

    var startTime: Long = 0
    var blackoutDuration: Long = 0
    lateinit var silenceButton: Button

    val msPerMinute = 60000L

    fun onCreate(parent: AlarmActivity, savedInstanceState: Bundle?) {
        parent.setContentView(R.layout.activity_alarm)

        startTime = systemClock.currentTimeMillis()
        blackoutDuration = parent.intent.getLongExtra(BLACKOUT_TIME_KEY, 1L) * msPerMinute

        silenceButton = parent.findViewById(R.id.button_silence)
        silenceButton.setOnClickListener(onSilenceClickedListener(parent))

        alarm.start()

        handler.postDelayed(parent::finish, blackoutDuration)

        notificationBarBlocker.block(parent)

        parent.window.clearFlags(FLAG_TRANSLUCENT_STATUS)
        parent.window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        parent.window.setStatusBarColor(WHITE)
    }

    private fun onSilenceClickedListener(parent: AlarmActivity): (View) -> Unit {
        return { view: View ->
            alarm.stop()
            silenceButton.setText(R.string.alarm_silenced)
            silenceButton.setBackgroundColor(parent.resources.getColor(R.color.darkGray))
            silenceButton.setTextColor(parent.resources.getColor(R.color.lightBlueGray))
        }
    }

    fun onBackPressed(parent: AlarmActivity) = Unit
    fun onGenericMotionEvent(parent: AlarmActivity) = true
    fun onKeyDown(parent: AlarmActivity, keyCode: Int, event: KeyEvent?): Boolean {
        return true
    }

    fun onKeyLongPress(parent: AlarmActivity, keyCode: Int, event: KeyEvent?): Boolean {
        return true
    }

    fun onPause(parent: AlarmActivity) {
        val elapsedTime = systemClock.currentTimeMillis() - startTime

        if (elapsedTime < blackoutDuration) {
            regretsScheduler.scheduleRegrets(blackoutDuration - elapsedTime)
        }
    }
}