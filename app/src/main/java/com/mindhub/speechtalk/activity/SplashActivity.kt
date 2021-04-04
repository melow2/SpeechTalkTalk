package com.mindhub.speechtalk.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mindhub.speechtalk.R
import com.mindhub.speechtalk.disableDarkMode
import com.mindhub.speechtalk.hideSystemUI

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableDarkMode()
        window.hideSystemUI()
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
            finish()
        }, 1500)
    }

    companion object {
        val TAG = SplashActivity::class.simpleName
    }

}