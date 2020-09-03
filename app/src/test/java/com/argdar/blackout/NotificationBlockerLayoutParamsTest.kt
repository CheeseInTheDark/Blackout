package com.argdar.blackout

import android.content.res.Resources
import android.graphics.PixelFormat.TRANSPARENT
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager.LayoutParams.*
import com.argdar.blackout.activity.AlarmActivity
import io.mockk.every
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NotificationBlockerLayoutParamsTest {

    lateinit var subject: NotificationBlockerLayoutParams

    val parent = mock<AlarmActivity>()
    val resources = mock<Resources>()
    val statusBarHeightId = 1
    val statusBarHeight = 10
    val wrongHeight = 1000

    @Before
    fun setUp() {
        every { parent.resources } returns resources
        every { resources.getIdentifier("status_bar_height", "dimen", "android") } returns statusBarHeightId
        every { resources.getDimensionPixelSize(0) } returns wrongHeight
        every { resources.getDimensionPixelSize(statusBarHeightId) } returns statusBarHeight

        subject = NotificationBlockerLayoutParams(parent)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun hasTypeOfSystemError() {
        assertEquals(TYPE_SYSTEM_ERROR, subject.type)
    }

    @Test
    fun hasTopGravity() {
        assertEquals(Gravity.TOP, subject.gravity)
    }

    @Test
    fun hasTouchBlockingFlags() {
        assertEquals(FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCH_MODAL or FLAG_LAYOUT_IN_SCREEN, subject.flags)
    }

    @Test
    fun hasWidthOfMatchParent() {
        assertEquals(MATCH_PARENT, subject.width)
    }

    @Test
    fun hasTransparentPixelFormat() {
        assertEquals(TRANSPARENT, subject.format)
    }

    @Test
    fun hasStatusBarHeightWhenAvailable() {
        assertEquals(statusBarHeight, subject.height)
    }

    @Test
    fun heightIsZeroWhenStatusBarHeightIsUnavailable() {
        every { resources.getIdentifier("status_bar_height", "dimen", "android") } returns 0

        assertEquals(0, NotificationBlockerLayoutParams(parent).height)
    }
}