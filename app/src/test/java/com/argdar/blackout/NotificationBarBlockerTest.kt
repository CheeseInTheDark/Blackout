package com.argdar.blackout

import android.view.WindowManager
import com.argdar.blackout.activity.AlarmActivity
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class NotificationBarBlockerTest : KoinTest {

    lateinit var subject: NotificationBarBlocker

    val notificationBlockerLayoutParams = mock<NotificationBlockerLayoutParams>()
    val notificationBlockerViewGroup = mock<NotificationBlockerViewGroup>()
    val parentActivity = mock<AlarmActivity>()
    val windowManager = mock<WindowManager>()

    @Before
    fun setUp() {
        koin {
            factory { (parent: AlarmActivity) -> if (parent == parentActivity) notificationBlockerLayoutParams else null }
            factory { (parent: AlarmActivity) -> if (parent == parentActivity) notificationBlockerViewGroup else null }
        }

        subject = NotificationBarBlocker(windowManager)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun blockBlocksTheNotificationBarFromBeingPulledDown() {
        subject.block(parentActivity)

        verify { windowManager.addView(notificationBlockerViewGroup, notificationBlockerLayoutParams) }
    }
}