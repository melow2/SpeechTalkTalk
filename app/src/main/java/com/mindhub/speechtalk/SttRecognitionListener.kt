package com.mindhub.speechtalk

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.mindhub.speechtalk.activity.MainActivity
import timber.log.Timber

class SttRecognitionListener : RecognitionListener {
    override fun onError(error: Int) {
        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
            SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션없음"
            SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
            SpeechRecognizer.ERROR_NO_MATCH -> "찾을수 없음"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "바쁘대"
            SpeechRecognizer.ERROR_SERVER -> "서버이상"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
            else -> "알수없음"
        }
        Timber.tag(MainActivity.TAG_STT).d("error: $message")
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.forEach {
            Timber.tag(MainActivity.TAG_STT).d("matches: $it")
        }
    }
    override fun onBeginningOfSpeech() {}
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
    override fun onPartialResults(partialResults: Bundle?) {}
}