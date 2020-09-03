package com.argdar.blackout.activity

import android.view.KeyEvent
import android.view.MotionEvent
import org.koin.android.ext.android.inject
import android.os.Bundle
import android.app.Activity
import com.argdar.blackout.activity.AlarmActivityImpl

class AlarmActivity : Activity() {
    val impl by inject<AlarmActivityImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        impl.onCreate(this, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        impl.onPause(this)
    }

    override fun onGenericMotionEvent(event: MotionEvent?) = impl.onGenericMotionEvent(this)

    override fun onBackPressed() = impl.onBackPressed(this)

    override fun onKeyDown(keyCode: Int, event: KeyEvent?) = impl.onKeyDown(this, keyCode, event)

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?) = impl.onKeyLongPress(this, keyCode, event)

    fun superOnBackPressed() = super.onBackPressed()
}