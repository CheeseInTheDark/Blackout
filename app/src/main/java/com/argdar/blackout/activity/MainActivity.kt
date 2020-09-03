package com.argdar.blackout.activity

import android.app.Activity
import android.os.Bundle
import org.koin.android.ext.android.inject

class MainActivity : Activity() {
    val impl by inject<MainActivityImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        impl.onCreate(this, savedInstanceState)
    }
}