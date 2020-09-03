package com.argdar.blackout.intent

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.argdar.blackout.activity.AlarmActivity
import com.argdar.blackout.intent.AlarmIntent.BLACKOUT_TIME_KEY
import com.argdar.blackout.koin
import com.argdar.blackout.mock
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class AlarmActivityIntentCreatorTest : KoinTest {

    @Before
    fun setUp() {
        koin {
            factory { mock<Intent>() }
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun returnsIntentForLaunchingAlarmActivity() {
        val context = mock<Context>()

        val result = AlarmActivityIntentCreator(context).create(4L)

        verify { result.setFlags(FLAG_ACTIVITY_NEW_TASK) }
        verify { result.setClass(context, AlarmActivity::class.java) }
        verify { result.putExtra(BLACKOUT_TIME_KEY, 4L) }
    }
}
