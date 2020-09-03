package com.argdar.blackout

class BlackoutScheduler(
    private val alarmScheduler: AlarmScheduler,
    private val blackoutStartTimePicker: BlackoutStartTimePicker,
    private val persistentAlarmTracker: PersistentAlarmTracker
) {

    fun scheduleRandomBlackoutPeriod(timeRangeStart: Long, timeRangeEnd: Long, blackoutDuration: Long) {
        val blackoutStartTime = blackoutStartTimePicker.pickBetween(timeRangeStart, timeRangeEnd)
        val blackoutId = persistentAlarmTracker.add(blackoutStartTime, blackoutDuration)
        alarmScheduler.schedule(ScheduledBlackout(blackoutId, blackoutStartTime, blackoutDuration))
    }
}