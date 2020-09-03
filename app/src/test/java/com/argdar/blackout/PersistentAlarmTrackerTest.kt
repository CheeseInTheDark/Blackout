package com.argdar.blackout

import com.argdar.blackout.AlarmReschedulerComponentNameCreator
import android.content.ComponentName
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class PersistentAlarmTrackerTest {

    private lateinit var subject: PersistentAlarmTracker

    private val fileCreator = mock<FileCreator>()
    private val packageManager = mock<PackageManager>()
    private val componentNameCreator = mock<AlarmReschedulerComponentNameCreator>()
    private val alarmReschedulerComponentName = mock<ComponentName>()

    private val filesDir = mock<File>()

    private val file0 = mock<File>()
    private val file1 = mock<File>()
    private val file2 = mock<File>()
    private val wrongFile = mock<File>()

    @Before
    fun setUp() {
        subject = PersistentAlarmTracker(fileCreator, filesDir, packageManager, componentNameCreator)

        mockkStatic("kotlin.io.FilesKt__FileReadWriteKt")

        every { any<File>().writeText(any()) } just Runs

        every { fileCreator.create(any()) } answers {
            when(firstArg<Long>()) {
                0L -> file0
                1L -> file1
                2L -> file2
                else -> wrongFile
            }
        }

        every { componentNameCreator.create() } returns alarmReschedulerComponentName
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun createsNewAlarmFile() {
        subject.add(0L, 1L)

        verify { file0.writeText("0\n1") }
    }

    @Test
    fun createsNextAlarmFileInLine() {
        every { filesDir.list() } returns arrayOf("0")

        subject.add(0L, 1L)

        verify { file1.writeText("0\n1")}
    }

    @Test
    fun createsAlarmFileWhenThereIsAGapInSequentialAlarmIds() {
        every { filesDir.list() } returns arrayOf("0", "3")

        subject.add(2L, 10L)

        verify { file1.writeText("2\n10") }
    }

    @Test
    fun returnsIdOfNewAlarm() {
        val result = subject.add(0L, 10L)

        assertEquals(0, result)
    }

    @Test
    fun returnsIdOfNewAlarmWhenAlarmsExist() {
        every { filesDir.list() } returns arrayOf("0")

        val result = subject.add(0L, 0L)

        assertEquals(1, result)
    }

    @Test
    fun removesFileForAlarmId() {
        subject.remove(1L)

        verify { file1.delete() }
    }

    @Test
    fun returnsListOfScheduledBlackouts() {
        every { filesDir.list() } returns arrayOf("0", "1", "2")
        every { file0.readLines() } returns listOf("0", "10")
        every { file1.readLines() } returns listOf("10", "50")
        every { file2.readLines() } returns listOf("20", "25")

        val result = subject.getAll()

        assertEquals(listOf(
            ScheduledBlackout(0, 0, 10),
            ScheduledBlackout(1, 10, 50),
            ScheduledBlackout(2, 20, 25)
        ), result)
    }

    @Test
    fun addingAlarmEnablesRescheduler() {
        subject.add(0, 0)

        verify { packageManager.setComponentEnabledSetting(
            alarmReschedulerComponentName,
            COMPONENT_ENABLED_STATE_ENABLED,
            DONT_KILL_APP) }
    }

    @Test
    fun removingLastAlarmDisabledRescheduler() {
        every { filesDir.list() } returns arrayOf<String>()

        subject.remove(0)

        verify {
            packageManager.setComponentEnabledSetting(
                alarmReschedulerComponentName,
                COMPONENT_ENABLED_STATE_DISABLED,
                DONT_KILL_APP
            )
        }
    }

    @Test
    fun removingAnAlarmDoesNotDisableReschedulingWhenAlarmsRemain() {
        every { filesDir.list() } returns arrayOf("an alarm file")

        subject.remove(0)

        verify(exactly = 0) {
            packageManager.setComponentEnabledSetting(any(), any(), any())
        }
    }
}