package com.mindhub.speechtalk

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import kotlin.math.pow


@Suppress("DEPRECATION")
fun Window.hideSystemUI() {
    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
}

fun disableDarkMode() {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}

@Suppress("DEPRECATION")
fun WindowManager.getScreenInches(): Double {
    val dm = DisplayMetrics()
    defaultDisplay.getMetrics(dm)
    val width = dm.widthPixels
    val height = dm.heightPixels
    val wi = width.toDouble() / dm.xdpi.toDouble()
    val hi = height.toDouble() / dm.ydpi.toDouble()
    val x = wi.pow(2.0)
    val y = hi.pow(2.0)
    return kotlin.math.sqrt(x + y)
}

fun View.setMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    if (layoutParams is MarginLayoutParams) {
        val p = layoutParams as MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        requestLayout()
    }
}