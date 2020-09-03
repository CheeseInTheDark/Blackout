package com.argdar.blackout

import android.content.Context
import android.view.ViewGroup
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificationBlockerViewGroupTest {

    lateinit var subject: NotificationBlockerViewGroup

    val context = mock<Context>()

    @Before
    fun setUp() {
        subject = spyk(NotificationBlockerViewGroup(context))

        every { subject.systemUiVisibility = any() } returns Unit
        every { subject["superOnWindowSystemUiVisibilityChanged"](any<Int>()) } returns Unit
    }

    @Test
    fun systemUIVisibilityChangeRehidesNavigation() {
        subject.onWindowSystemUiVisibilityChanged(0)

        verify { subject.systemUiVisibility = ViewGroup.SYSTEM_UI_FLAG_HIDE_NAVIGATION }
    }

    @Test
    fun interceptsAllTouchEvents() {
        assertTrue(subject.onInterceptTouchEvent(mock()))
    }
}