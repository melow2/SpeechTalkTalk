package com.mindhub.speechtalk

import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.attractive.deer.util.data.MediaStoreAudio
import com.attractive.deer.util.data.copyTo
import com.jakewharton.rxbinding4.view.clicks
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.backgroundColor
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.pow

const val ANIMATION_DURATION = 700L // 0.7s

const val CARD_FIRST = 1
const val CARD_SECOND = 2
const val CARD_THIRD = 3
const val CARD_FOURTH = 4

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

fun Context.getNewAudioFile(
    audio: MediaStoreAudio
): File? {
    return File(externalCacheDir, audio.displayName).run {
        contentResolver.openInputStream(Uri.parse(audio.contentUri))?.copyTo(this, true)
    }
}

fun Context.clearCacheData() {
    val cacheDir = externalCacheDir
    if (cacheDir != null) {
        if (cacheDir.isDirectory) {
            val children: Array<String> = cacheDir.list()
            for (i in children.indices) {
                File(cacheDir, children[i]).delete()
            }
        }
    }
}

fun Context.recordButtonAnimation(
    startView: View,
    nextView: View,
    background: View,
    inversionFlag: Boolean
) = RxAnimation.together(
    background.backgroundColor(
        if (inversionFlag) ContextCompat.getColor(
            this,
            R.color.color_d6222b
        ) else ContextCompat.getColor(this, R.color.color_FFFFFFFF),
        if (inversionFlag) ContextCompat.getColor(
            this,
            R.color.color_FFFFFFFF
        ) else ContextCompat.getColor(this, R.color.color_d6222b),
        duration = ANIMATION_DURATION, reverse = false
    ), if (inversionFlag) nextView.fadeIn() else nextView.fadeOut(),
    if (inversionFlag) startView.fadeOut() else startView.fadeIn()
)

fun Context.playButtonAnimation(
    startView: View,
    nextView: View,
    background: View,
    inversionFlag: Boolean
) = RxAnimation.together(
    background.backgroundColor(
        ContextCompat.getColor(this, R.color.color_FFFFFFFF),
        ContextCompat.getColor(this, R.color.color_FFFFFFFF),
        duration = ANIMATION_DURATION, reverse = false
    ), if (inversionFlag) nextView.fadeIn() else nextView.fadeOut(),
    if (inversionFlag) startView.fadeOut() else startView.fadeIn()
)

fun View.throttleClick(duration: Long = ANIMATION_DURATION * 2): Observable<Unit> =
    clicks().throttleFirst(duration, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())