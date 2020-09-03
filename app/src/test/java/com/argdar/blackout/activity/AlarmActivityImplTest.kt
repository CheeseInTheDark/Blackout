package com.argdar.blackout.activity

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color.WHITE
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import android.widget.Button
import com.argdar.blackout.*
import com.argdar.blackout.R.color.darkGray
import com.argdar.blackout.R.color.lightBlueGray
import com.argdar.blackout.intent.AlarmIntent.BLACKOUT_TIME_KEY
import io.mockk.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AlarmActivityImplTest {

    private lateinit var subject: AlarmActivityImpl

    private val notificationBarBlocker = mock<NotificationBarBlocker>()
    private val handler = mock<Handler>()
    private val alarm = mock<Alarm>()
    private val regretsScheduler = mock<RegrettableConsequencesScheduler>()
    private val systemClock = mock<SystemClock>()

    private val parent = mock<AlarmActivity>()
    private val window = mock<Window>()
    private val resources = mock<Resources>()
    private val activityLaunchIntent = mock<Intent>()
    private val blackoutTime = 10L
    private val blackoutTimeInMs = 600000L

    private val silenceButton = mock<Button>()

    private val clickListener = slot<View.OnClickListener>()
    private val runnable = slot<Runnable>()

    private val darkGrayColor = 1
    private val lightBlueGrayColor = 2

    @Before
    fun setUp() {
        every { parent.resources } returns resources
        every { parent.findViewById<Button>(R.id.button_silence) } returns silenceButton
        every { parent.intent } returns activityLaunchIntent
        every { parent.window } returns window
        every { resources.getColor(darkGray) } returns darkGrayColor
        every { resources.getColor(lightBlueGray) } returns lightBlueGrayColor
        every { activityLaunchIntent.getLongExtra(BLACKOUT_TIME_KEY, 1L)} returns blackoutTime

        subject = AlarmActivityImpl(alarm, handler, notificationBarBlocker, systemClock, regretsScheduler)
    }

    @Test
    fun onCreateBlocksNotificationBar() {
        subject.onCreate(parent, null)

        verify { notificationBarBlocker.block(parent) }
    }

    @Test
    fun onCreateSetsLayout() {
        subject.onCreate(parent, null)

        verify { parent.setContentView(R.layout.activity_alarm) }
    }

    @Test
    fun onCreateStartsAlarm() {
        subject.onCreate(parent, null)

        verify { alarm.start() }
    }

    @Test
    fun onCreateHidesStatusBar() {
        subject.onCreate(parent, null)

        verifyAll {
            window.clearFlags(FLAG_TRANSLUCENT_STATUS);
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(WHITE)
        }
    }

    @Test
    fun onPauseSchedulesRussianCalletteForRemainderOfBlackoutDuration() {
        val elapsedTime = 3000L
        every { systemClock.currentTimeMillis() } returns 1000L
        subject.onCreate(parent, null)
        every { systemClock.currentTimeMillis() } returns 1000L + elapsedTime

        subject.onPause(parent)

        verify { regretsScheduler.scheduleRegrets(blackoutTimeInMs - elapsedTime) }
    }

    @Test
    fun onPauseDoesNotScheduleRegretsWhenTimeIsUp() {
        val elapsedTime = blackoutTimeInMs
        every { systemClock.currentTimeMillis() } returns 1000L
        subject.onCreate(parent, null)
        every { systemClock.currentTimeMillis() } returns 1000L + elapsedTime

        subject.onPause(parent)

        verify(exactly = 0) { regretsScheduler.scheduleRegrets(any()) }
    }

    @Test
    fun onPauseDoesNotScheduleRegretsWhenTimeIsPastUp() {
        val elapsedTime = blackoutTimeInMs + 1
        every { systemClock.currentTimeMillis() } returns 1000L
        subject.onCreate(parent, null)
        every { systemClock.currentTimeMillis() } returns 1000L + elapsedTime

        subject.onPause(parent)

        verify(exactly = 0) { regretsScheduler.scheduleRegrets(any()) }
    }

    @Test
    fun clickingSilenceButtonStopsAlarm() {
        every { silenceButton.setOnClickListener(capture(clickListener)) } just runs
        subject.onCreate(parent, null)

        clickListener.captured.onClick(silenceButton)

        verify { alarm.stop() }
    }

    @Test
    fun clickingSilenceButtonChangesButtonTextToAlarmSilenced() {
        every { silenceButton.setOnClickListener(capture(clickListener)) } just runs
        subject.onCreate(parent, null)

        clickListener.captured.onClick(silenceButton)

        verify { silenceButton.setText(R.string.alarm_silenced) }
    }

    @Test
    fun clickingSilenceButtonChangesButtonStyleToSilenced() {
        every { silenceButton.setOnClickListener(capture(clickListener)) } just runs
        subject.onCreate(parent, null)

        clickListener.captured.onClick(silenceButton)

        verify { silenceButton.setBackgroundColor(darkGrayColor) }
        verify { silenceButton.setTextColor(lightBlueGrayColor) }
    }

    @Test
    fun preventsMotionEventsFromDoingAnythingWhileBlackoutIsInProgress() {
        assertTrue(subject.onGenericMotionEvent(parent))
    }

    @Test
    fun preventsBackButtonFromWorkingWhileBlackoutIsInProgress() {
        subject.onCreate(parent, null)

        subject.onBackPressed(parent)

        verify(exactly = 0) { parent.superOnBackPressed() }
    }

    @Test
    fun preventsAnyKeyPressWhileBlackoutIsInProgress() {
        subject.onCreate(parent, null)

        assertTrue(subject.onKeyDown(parent, 0, null))
    }

    @Test
    fun preventsLongKeyPressesWhileBlackoutIsInProgress() {
        assertTrue(subject.onKeyLongPress(parent, 0, null))
    }

    @Test
    fun finishesActivityWhenBlackoutIsOver() {
        every { handler.postDelayed(capture(runnable), eq(blackoutTimeInMs)) } returns true

        subject.onCreate(parent, null)

        verify(exactly = 0) { parent.finish() }

        runnable.captured.run()

        verify { parent.finish() }
    }
}