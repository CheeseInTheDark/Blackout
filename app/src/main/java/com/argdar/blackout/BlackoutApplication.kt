package com.argdar.blackout

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BlackoutApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BlackoutApplication)
            modules(excellentModule)
        }
    }
}