package com.khs.kakaopay.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.khs.kakaopay.R
import com.lovely.deer.util.SecureSharedPreferences
import org.koin.androidx.scope.ScopeActivity

class SplashActivity : ScopeActivity() {

    private val mPrefer: SecureSharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
            finish()
        }, 3000)
    }

    companion object {
        val TAG = SplashActivity::class.simpleName
    }

}