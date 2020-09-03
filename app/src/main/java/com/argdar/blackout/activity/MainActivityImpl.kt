package com.argdar.blackout.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import com.argdar.blackout.BlackoutScheduler
import com.argdar.blackout.NextTimeOccurrenceCalculator
import com.argdar.blackout.R

class MainActivityImpl(
    private val blackoutScheduler: BlackoutScheduler,
    private val nextTimeOccurrenceCalculator: NextTimeOccurrenceCalculator,
    private val handler: Handler
) {

    lateinit var startTimePicker: TimePicker
    lateinit var endTimePicker: TimePicker
    lateinit var blackoutDurationEditText: EditText
    lateinit var feedbackText: TextView

    fun onCreate(parent: Activity, savedInstanceState: Bundle?) {
        parent.setContentView(R.layout.activity_main)
        startTimePicker = parent.findViewById(R.id.input_alarm_range_start)
        endTimePicker = parent.findViewById(R.id.input_alarm_range_end)
        blackoutDurationEditText = parent.findViewById(R.id.input_blackout_duration)
        feedbackText = parent.findViewById(R.id.text_feedback_message)

        parent.findViewById<Button>(R.id.button_set_alarm).setOnClickListener(::onClickSetAlarm)
        startTimePicker.setOnTimeChangedListener(::onStartTimeChanged)
        endTimePicker.setOnTimeChangedListener(::onEndTimeChanged)
    }

    private fun onClickSetAlarm(view : View) {
        val blackoutDurationText = blackoutDurationEditText.text.toString()
        if (blackoutDurationText.isBlank()) {
            feedbackText.setText(R.string.message_empty_blackout_period_error)
            return
        }

        val startTimestamp = timestampFrom(startTimePicker)
        val endTimestamp = timestampFrom(endTimePicker)

        blackoutScheduler.scheduleRandomBlackoutPeriod(
            startTimestamp,
            endTimestamp,
            blackoutDurationText.toLong())

        feedbackText.setText(R.string.message_alarm_set)

        handler.postDelayed({ feedbackText.setText("") }, 5000)
    }

    private fun timestampFrom(timePicker: TimePicker) =
        nextTimeOccurrenceCalculator.getNextTimestamp(timePicker.currentHour, timePicker.currentMinute)

    private fun onStartTimeChanged(timePicker: TimePicker, hour: Int, minute: Int) {
        if (endTimePicker.currentHour < hour || (endTimePicker.currentMinute < minute && endTimePicker.currentHour == hour)) {
            endTimePicker.currentHour = hour
            endTimePicker.currentMinute = minute
        }
    }

    private fun onEndTimeChanged(timePicker: TimePicker, hour: Int, minute: Int) {
        if (startTimePicker.currentHour > hour || (startTimePicker.currentMinute > minute && startTimePicker.currentHour == hour)) {
            startTimePicker.currentHour = hour
            startTimePicker.currentMinute = minute
        }
    }
}
