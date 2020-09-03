package com.argdar.blackout

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class NotificationBlockerViewGroup(context: Context) : ViewGroup(context) {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

    override fun onWindowSystemUiVisibilityChanged(visible: Int) {
        superOnWindowSystemUiVisibilityChanged(visible)
        systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    private fun superOnWindowSystemUiVisibilityChanged(visible: Int) {
        super.onWindowSystemUiVisibilityChanged(visible)
    }
}
