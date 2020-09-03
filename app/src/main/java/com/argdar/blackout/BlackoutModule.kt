package com.argdar.blackout

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Handler
import android.view.WindowManager
import com.argdar.blackout.activity.AlarmActivity
import com.argdar.blackout.activity.AlarmActivityImpl
import com.argdar.blackout.activity.MainActivityImpl
import com.argdar.blackout.intent.*
import org.koin.dsl.module
import kotlin.random.Random

val excellentModule = module {
    single { AlarmActivityIntentCreator(get()) }

    single { PendingBlackoutIntentCreator(get()) }
    single { PendingRegretIntentCreator(get()) }
    single { PhoneCallIntentCreator() }
    single { AlarmAudioAttributesCreator() }

    single { get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    single { get<Context>().getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    single { get<Context>().getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    single { get<Context>().packageManager }

    single { Random.Default as Random }

    single { AlarmReschedulerComponentNameCreator(get()) }
    single { FileCreator(get<Context>().filesDir.absolutePath) }
    single { AlarmScheduler(get(), get()) }
    single { PersistentAlarmTracker(get(), get<Context>().filesDir, get(), get()) }
    single { BlackoutScheduler(get(), get(), get()) }
    single { RussianCallette(get(), get()) }
    single { PhoneNumberPool(get(), get()) }
    single { get<Context>().contentResolver }
    single { BlackoutStartTimePicker(get()) }
    single { SystemClock() }
    single { NextTimeOccurrenceCalculator(get()) }
    single { Alarm(get(), get()) }
    single { NotificationBarBlocker(get()) }
    single { RegrettableConsequencesScheduler(get(), get(), get()) }
    single { Phone(get(), get()) }

    factory { (parent: AlarmActivity) -> NotificationBlockerLayoutParams(parent) }
    factory { (parent: AlarmActivity) -> NotificationBlockerViewGroup(parent) }
    factory { Handler() }
    factory { AlarmActivityImpl(get(), get(), get(), get(), get()) }
    factory { MainActivityImpl(get(), get(), get()) }
    factory { Intent() }
    factory { RegrettableIntent(get()) }

}