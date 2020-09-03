package com.argdar.blackout.activity

import android.app.Activity
import android.os.Handler
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import com.argdar.blackout.BlackoutScheduler
import com.argdar.blackout.NextTimeOccurrenceCalculator
import com.argdar.blackout.R
import com.argdar.blackout.mock
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActivityImplTest {

    private lateinit var subject: MainActivityImpl

    private val parent = mock<Activity>()
    private val blackoutScheduler = mock<BlackoutScheduler>()
    private val setAlarmButton = mock<Button>()
    private val blackoutDurationEditText = mock<EditText>()
    private val alarmRangeEndTimePicker = mock<TimePicker>()
    private val alarmRangeStartTimePicker = mock<TimePicker>()
    private val nextTimeOccurrenceCalculator =
        mock<NextTimeOccurrenceCalculator>()
    private val textViewErrorMessage = mock<TextView>()
    private val handler = mock<Handler>()

    private val blackoutDurationEditable = mock<Editable>()
    private val blackoutDurationString = "123"
    private val blackoutDuration = 123L
    private val alarmRangeEndHour = 10
    private val alarmRangeEndMinute = 50
    private val alarmRangeStartHour = 9
    private val alarmRangeStartMinute = 30

    private val alarmRangeStartTimestamp = 1000L
    private val alarmRangeEndTimestamp = 2000L

    private val clickListener = slot<View.OnClickListener>()
    private val runnable = slot<Runnable>()
    private val onTimeChangedListener = slot<TimePicker.OnTimeChangedListener>()

    @Before
    fun setUp() {
        every { parent.findViewById<Button>(R.id.button_set_alarm) } returns setAlarmButton
        every { parent.findViewById<EditText>(R.id.input_blackout_duration) } returns blackoutDurationEditText
        every { parent.findViewById<TimePicker>(R.id.input_alarm_range_end) } returns alarmRangeEndTimePicker
        every { parent.findViewById<TimePicker>(R.id.input_alarm_range_start) } returns alarmRangeStartTimePicker
        every { parent.findViewById<TextView>(R.id.text_feedback_message) } returns textViewErrorMessage

        every { nextTimeOccurrenceCalculator.getNextTimestamp(alarmRangeStartHour, alarmRangeStartMinute) }.returns(alarmRangeStartTimestamp)
        every { nextTimeOccurrenceCalculator.getNextTimestamp(alarmRangeEndHour, alarmRangeEndMinute) }.returns(alarmRangeEndTimestamp)

        every { blackoutDurationEditable.toString() }.returns(blackoutDurationString)

        subject = MainActivityImpl(blackoutScheduler, nextTimeOccurrenceCalculator, handler)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun onCreateSetsLayout() {
        subject.onCreate(parent, null)

        verify { parent.setContentView(R.layout.activity_main) }
    }

    @Test
    fun clickingSetAlarmSetsAlarmWhenFieldsAreFilledOut() {
        every { blackoutDurationEditText.text } returns blackoutDurationEditable
        every { alarmRangeEndTimePicker.currentHour } returns alarmRangeEndHour
        every { alarmRangeEndTimePicker.currentMinute } returns alarmRangeEndMinute
        every { alarmRangeStartTimePicker.currentHour } returns alarmRangeStartHour
        every { alarmRangeStartTimePicker.currentMinute } returns alarmRangeStartMinute
        every { setAlarmButton.setOnClickListener(capture(clickListener)) } just runs

        subject.onCreate(parent, null)

        clickListener.captured.onClick(setAlarmButton)

        verify { blackoutScheduler.scheduleRandomBlackoutPeriod(alarmRangeStartTimestamp, alarmRangeEndTimestamp, blackoutDuration) }
    }

    @Test
    fun alarmSetMessageDisappearsAfterAWhile() {
        every { blackoutDurationEditText.text } returns blackoutDurationEditable
        every { alarmRangeEndTimePicker.currentHour } returns alarmRangeEndHour
        every { alarmRangeEndTimePicker.currentMinute } returns alarmRangeEndMinute
        every { alarmRangeStartTimePicker.currentHour } returns alarmRangeStartHour
        every { alarmRangeStartTimePicker.currentMinute } returns alarmRangeStartMinute
        every { setAlarmButton.setOnClickListener(capture(clickListener)) } just runs
        every { handler.postDelayed(capture(runnable), 5000) } returns true

        subject.onCreate(parent, null)

        clickListener.captured.onClick(setAlarmButton)

        verify(exactly = 0) { textViewErrorMessage.text = "" }

        runnable.captured.run()

        verify { textViewErrorMessage.text = "" }
    }

    @Test
    fun clickingSetAlarmDisplaysAlarmSetMessageWhenFieldsAreFilledOut() {
        every { blackoutDurationEditText.text } returns blackoutDurationEditable
        every { alarmRangeEndTimePicker.currentHour } returns alarmRangeEndHour
        every { alarmRangeEndTimePicker.currentMinute } returns alarmRangeEndMinute
        every { alarmRangeStartTimePicker.currentHour } returns alarmRangeStartHour
        every { alarmRangeStartTimePicker.currentMinute } returns alarmRangeStartMinute
        every { setAlarmButton.setOnClickListener(capture(clickListener)) } just runs

        subject.onCreate(parent, null)

        clickListener.captured.onClick(setAlarmButton)

        verify { textViewErrorMessage.setText(R.string.message_alarm_set) }
    }

    @Test
    fun clickingSetAlarmNotifiesWhenBlackoutPeriodIsEmpty() {
        every { blackoutDurationEditText.text } returns blackoutDurationEditable
        every { alarmRangeEndTimePicker.currentHour } returns alarmRangeEndHour
        every { alarmRangeEndTimePicker.currentMinute } returns alarmRangeEndMinute
        every { alarmRangeStartTimePicker.currentHour } returns alarmRangeStartHour
        every { alarmRangeStartTimePicker.currentMinute } returns alarmRangeStartMinute
        every { setAlarmButton.setOnClickListener(capture(clickListener)) } just runs
        every { blackoutDurationEditable.toString() } returns ""

        subject.onCreate(parent, null)

        clickListener.captured.onClick(setAlarmButton)

        verify { textViewErrorMessage.setText(R.string.message_empty_blackout_period_error) }
    }

    @Test
    fun updatingEndTimeEnsuresStartTimeIsStillValid() {
        every { alarmRangeStartTimePicker.currentHour } returns 12
        every { alarmRangeStartTimePicker.currentMinute } returns 1
        every { alarmRangeEndTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)

        onTimeChangedListener.captured.onTimeChanged(alarmRangeEndTimePicker, 12, 0)

        verify { alarmRangeStartTimePicker.currentHour = 12 }
        verify { alarmRangeStartTimePicker.currentMinute = 0 }
    }

    @Test
    fun updatingEndTimeEnsuresStartTimeIsStillValidForAnotherTime() {
        every { alarmRangeStartTimePicker.currentHour } returns 10
        every { alarmRangeStartTimePicker.currentMinute } returns 30
        every { alarmRangeEndTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)

        onTimeChangedListener.captured.onTimeChanged(alarmRangeEndTimePicker, 10, 29)

        verify { alarmRangeStartTimePicker.currentHour = 10 }
        verify { alarmRangeStartTimePicker.currentMinute = 29 }
    }

    @Test
    fun endTimeChangeDoesNotChangeValidStartTime() {
        every { alarmRangeStartTimePicker.currentHour } returns 12
        every { alarmRangeStartTimePicker.currentMinute } returns 0
        every { alarmRangeEndTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)


        onTimeChangedListener.captured.onTimeChanged(alarmRangeEndTimePicker, 12, 0)

        verify(inverse = true) { alarmRangeStartTimePicker.currentHour = any() }
        verify(inverse = true) { alarmRangeStartTimePicker.currentMinute = any() }
    }

    @Test
    fun endTimeChangeDoesNotChangeValidStartTimeWhenStartMinuteGreaterThanEndMinute() {
        every { alarmRangeStartTimePicker.currentHour } returns 12
        every { alarmRangeStartTimePicker.currentMinute } returns 40
        every { alarmRangeEndTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)

        onTimeChangedListener.captured.onTimeChanged(alarmRangeEndTimePicker, 13, 20)

        verify(inverse = true) { alarmRangeStartTimePicker.currentHour = any() }
        verify(inverse = true) { alarmRangeStartTimePicker.currentMinute = any() }
    }

    @Test
    fun updatingStartTimeEnsuresEndTimeIsStillValid() {
        every { alarmRangeEndTimePicker.currentHour } returns 11
        every { alarmRangeEndTimePicker.currentMinute } returns 59
        every { alarmRangeStartTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)

        onTimeChangedListener.captured.onTimeChanged(alarmRangeStartTimePicker, 12, 0)

        verify { alarmRangeEndTimePicker.currentHour = 12 }
        verify { alarmRangeEndTimePicker.currentMinute = 0 }
    }

    @Test
    fun updatingStartTimeEnsuresEndTimeIsStillValidForAnotherTime() {
        every { alarmRangeEndTimePicker.currentHour } returns 10
        every { alarmRangeEndTimePicker.currentMinute } returns 29
        every { alarmRangeStartTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)

        onTimeChangedListener.captured.onTimeChanged(alarmRangeStartTimePicker, 10, 30)

        verify { alarmRangeEndTimePicker.currentHour = 10 }
        verify { alarmRangeEndTimePicker.currentMinute = 30 }
    }

    @Test
    fun startTimeChangeDoesNotChangeValidEndTime() {
        every { alarmRangeEndTimePicker.currentHour } returns 12
        every { alarmRangeEndTimePicker.currentMinute } returns 0
        every { alarmRangeStartTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)


        onTimeChangedListener.captured.onTimeChanged(alarmRangeStartTimePicker, 12, 0)

        verify(inverse = true) { alarmRangeEndTimePicker.currentHour = any() }
        verify(inverse = true) { alarmRangeEndTimePicker.currentMinute = any() }
    }

    @Test
    fun startTimeChangeDoesNotChangeValidEndTimeWhenEndMinuteGreaterThanStartMinute() {
        every { alarmRangeEndTimePicker.currentHour } returns 12
        every { alarmRangeEndTimePicker.currentMinute } returns 30
        every { alarmRangeStartTimePicker.setOnTimeChangedListener(capture(onTimeChangedListener)) } just runs

        subject.onCreate(parent, null)

        onTimeChangedListener.captured.onTimeChanged(alarmRangeStartTimePicker, 11, 40)

        verify(inverse = true) { alarmRangeEndTimePicker.currentHour = any() }
        verify(inverse = true) { alarmRangeEndTimePicker.currentMinute = any() }
    }
}
