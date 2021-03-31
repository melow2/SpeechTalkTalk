package com.mindhub.speechtalk.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.*
import com.mindhub.speechtalk.R
import com.mindhub.speechtalk.databinding.ActivityLoginBinding
import com.mindhub.speechtalk.disableDarkMode
import com.mindhub.speechtalk.getScreenInches
import com.mindhub.speechtalk.hideSystemUI
import org.koin.androidx.scope.ScopeActivity

class LoginActivity : ScopeActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableDarkMode()
        window.hideSystemUI()
        mBinding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
    }

    fun getFontSize(rate:Int) = (windowManager.getScreenInches().toInt() * rate).toFloat()
    companion object {
        val TAG = LoginActivity::class.simpleName
    }
}
