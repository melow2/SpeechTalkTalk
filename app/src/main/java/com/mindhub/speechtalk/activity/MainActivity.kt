package com.mindhub.speechtalk.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.*
import com.mindhub.speechtalk.R
import com.mindhub.speechtalk.databinding.ActivityMainBinding
import com.mindhub.speechtalk.disableDarkMode
import com.mindhub.speechtalk.getScreenInches
import com.mindhub.speechtalk.hideSystemUI
import org.koin.androidx.scope.ScopeActivity

class MainActivity : ScopeActivity() {

    private lateinit var mBinding: ActivityMainBinding

    val commentSize get() = getFontSize(3.6) // 55sp
    val buttonSize get() = getFontSize(3.3) // 45sp
    val headerSize get() = getFontSize(3.0) // 35sp
    val calendarSize get() = getFontSize(2.0) // 25sp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableDarkMode()
        window.hideSystemUI()
        mBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }

    private fun getFontSize(rate: Double) =
        (windowManager.getScreenInches().toInt() * rate).toFloat()

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}
