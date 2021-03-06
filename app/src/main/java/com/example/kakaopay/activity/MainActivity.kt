package com.example.kakaopay.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kakaopay.R
import com.example.kakaopay.ui.main.MainFragment
import com.lovely.deer.util.SecureSharedPreferences
import org.koin.androidx.scope.ScopeActivity
import timber.log.Timber

class MainActivity : ScopeActivity() {

    private val mPrefer: SecureSharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    companion object{
        val TAG = MainActivity::class.simpleName
    }
}