package com.mindhub.speechtalk.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.*
import com.attractive.deer.util.data.toast
import com.mindhub.speechtalk.*
import com.mindhub.speechtalk.R
import com.mindhub.speechtalk.databinding.ActivityMainBinding
import org.koin.androidx.scope.ScopeActivity
import timber.log.Timber

class MainActivity : ScopeActivity() {

    private lateinit var mBinding: ActivityMainBinding


    val commentSize get() = getFontSize(3.6) // 55sp
    val buttonSize get() = getFontSize(3.3) // 45sp
    val headerSize get() = getFontSize(3.0) // 35sp
    val calendarSize get() = getFontSize(2.0) // 25s

    var permissionCheckFlag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableDarkMode()
        window.hideSystemUI()
        mBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }

    private fun getFontSize(rate: Double) =
        (windowManager.getScreenInches().toInt() * rate).toFloat()

    private val requestPermissionLancher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
            var checkFlag = true
            permissionMap.forEach {
                if (!it.value) {
                    toast("모든 권한에 동의해야 서비스를 이용할 수 있습니다.")
                    checkFlag = false
                    return@forEach
                }
            }
            if (!checkFlag) {
                permissionCheckFlag = false
                startActivity(Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS))
            } else permissionCheckFlag = true

        }

    fun permissionCheck() = requestPermissionLancher.launch(
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
    )

    fun getRecognitionIntent() = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANG_KR)
        putExtra(GET_AUDIO_FORMAT, AUDIO_AMR)
        putExtra(GET_AUDIO, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearCacheData()
    }

    companion object {
        const val TAG_STT = "RECOGNITION"
        val TAG = MainActivity::class.simpleName
        const val GET_AUDIO_FORMAT = "android.speech.extra.GET_AUDIO_FORMAT"
        const val GET_AUDIO = "android.speech.extra.GET_AUDIO"
        const val AUDIO_AMR = "audio/AMR"
        const val LANG_KR = "ko-KR"
    }

}
