package com.argdar.blackout

import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import java.io.File

class PersistentAlarmTracker(
    private val fileCreator: FileCreator,
    private val filesDir: File,
    private val packageManager: PackageManager,
    private val alarmReschedulerComponentNameCreator: AlarmReschedulerComponentNameCreator
) {

    fun add(blackoutStartTime: Long, blackoutDuration: Long): Long {
        enableRescheduling()
        return createAlarmFile(blackoutStartTime, blackoutDuration)
    }

    private fun createAlarmFile(blackoutStartTime: Long, blackoutDuration: Long): Long {
        val alarmFileList = filesDir.list().sorted()

        var nextId = alarmFileList.mapIndexed {
            index, fileName -> fileName.toInt() == index
        }.indexOfFirst { !it }.toLong()

        if (alarmFileList.isEmpty()) { nextId = 0 }
        if (nextId == -1L) { nextId = alarmFileList.size.toLong() }

        fileCreator.create(nextId).writeText("$blackoutStartTime\n$blackoutDuration")

        return nextId
    }

    private fun enableRescheduling() {
        val alarmReschedulerComponent = alarmReschedulerComponentNameCreator.create()

        packageManager.setComponentEnabledSetting(alarmReschedulerComponent,
            COMPONENT_ENABLED_STATE_ENABLED,
            DONT_KILL_APP
        )
    }

    fun remove(blackoutId: Long) {
        fileCreator.create(blackoutId).delete()
        disableRescheduling()
    }

    private fun disableRescheduling() {
        val alarmReschedulerComponent = alarmReschedulerComponentNameCreator.create()

        if (filesDir.list().isEmpty()) {
            packageManager.setComponentEnabledSetting(alarmReschedulerComponent,
                COMPONENT_ENABLED_STATE_DISABLED,
                DONT_KILL_APP
            )
        }
    }

    fun getAll(): List<ScheduledBlackout> {
        return filesDir.list().map {
            val id = it.toLong()
            val lines = fileCreator.create(id).readLines()
            val timestamp = lines[0].toLong()
            val duration = lines[1].toLong()
            ScheduledBlackout(id, timestamp, duration)
        }
    }

}
