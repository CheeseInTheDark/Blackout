package com.argdar.blackout

import android.graphics.PixelFormat.TRANSPARENT
import android.view.Gravity
import android.view.WindowManager
import com.argdar.blackout.activity.AlarmActivity

class NotificationBlockerLayoutParams(parent: AlarmActivity) : WindowManager.LayoutParams() {

    val touchBlockingFlags = FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCH_MODAL or FLAG_LAYOUT_IN_SCREEN

    init {
        type = TYPE_SYSTEM_ERROR
        gravity = Gravity.TOP
        flags = touchBlockingFlags
        width = MATCH_PARENT
        format = TRANSPARENT

        val statusBarResourceId = parent.resources.getIdentifier("status_bar_height", "dimen", "android")
        height = if (statusBarResourceId > 0) parent.resources.getDimensionPixelSize(statusBarResourceId) else 0
    }
}
