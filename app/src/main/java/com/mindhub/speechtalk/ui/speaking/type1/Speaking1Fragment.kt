package com.mindhub.speechtalk.ui.speaking.type1

import android.media.MediaPlayer
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.attractive.deer.base.BaseFragment
import com.attractive.deer.util.data.MediaStoreAudio
import com.attractive.deer.util.data.getDataFromContentUri
import com.attractive.deer.util.data.toast
import com.jakewharton.rxrelay3.PublishRelay
import com.mikhaellopez.rxanimation.alpha
import com.mikhaellopez.rxanimation.backgroundColor
import com.mikhaellopez.rxanimation.fadeIn
import com.mindhub.speechtalk.*
import com.mindhub.speechtalk.activity.MainActivity
import com.mindhub.speechtalk.activity.MainActivity.Companion.TAG_STT
import com.mindhub.speechtalk.databinding.FragmentSpeaking1Binding
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1ViewState.State.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Speaking1Fragment : BaseFragment<
        Speaking1Intent,
        Speaking1ViewState,
        Speaking1SingleEvent,
        Speaking1ViewModel,
        FragmentSpeaking1Binding>(R.layout.fragment_speaking_1), MediaPlayer.OnCompletionListener {

    override val mViewModel: Speaking1ViewModel by viewModel()
    private val mainActivity get() = requireActivity() as MainActivity
    private val intentS = PublishRelay.create<Speaking1Intent>()
    private val progressObservable = PublishSubject.create<Long>()
    private val mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener(this@Speaking1Fragment)
    }

    private val googleVoiceRecognitionLancher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val speechList =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val voiceRecord = speechList?.get(0)
                val audioUrl = result.data?.data
                audioUrl?.let {
                    val contentData = context?.getDataFromContentUri(audioUrl)
                    val mediaFile = context?.getNewAudioFile(contentData as MediaStoreAudio)
                    Timber.tag(TAG_STT).d("mediaFile: ${mediaFile?.absolutePath}")
                    intentS.accept(
                        Speaking1Intent.StopWithRecognition(
                            recordFile = mediaFile,
                            voiceRecord = voiceRecord
                        )
                    )
                }
            } else intentS.accept(Speaking1Intent.RecordCancle)
        }

    override fun handleEvent(event: Speaking1SingleEvent) {
        when (event) {
            Speaking1SingleEvent.WrongSound -> {

            }
            Speaking1SingleEvent.AnswerSound -> {

            }
        }
    }

    override fun render(viewState: Speaking1ViewState) {
        mBinding?.run {
            when (viewState.state) {
                RECORD -> {
                    mainActivity.run {
                        googleVoiceRecognitionLancher.launch(getRecognitionIntent())
                    }
                }
                HINT -> {
                    context?.toast(viewState.hint ?: "")
                }
                RECORD_STOP_WITH_RECOGNITION -> {
                    Timber.tag(TAG_STT)
                        .d("mediaFile:${viewState.recordFile}, voiceRecord:${viewState.voiceRecord}")
                    mainActivity.recordButtonAnimation(
                        ivRecord,
                        ivRecordStop,
                        btnRecordOrStop,
                        false
                    )
                        .subscribe().addTo(compositeDisposable)
                    // 정답 일 경우
                    if (viewState.voiceRecord == viewState.answer) context?.toast("정답입니다.")
                    if (rootLytBtnPlay.alpha != 1F) {
                        ivPlay.fadeIn().subscribe().addTo(compositeDisposable)
                        rootLytBtnPlay.fadeIn().subscribe().addTo(compositeDisposable)
                    } else {
                        Unit
                    }
                }

                PLAYING -> {
                    // button animation.
                    btnRecordOrStop.backgroundColor(
                        ContextCompat.getColor(mainActivity, R.color.color_d6222b),
                        ContextCompat.getColor(mainActivity, R.color.color_cbcbcb),
                        duration = ANIMATION_DURATION, reverse = false
                    ).andThen {
                        btnRecordOrStop.isClickable = false
                    }.subscribe().addTo(compositeDisposable)

                    // mediaplayer setting.
                    mediaPlayer.run {
                        reset()
                        setDataSource(viewState.recordFile?.absolutePath)
                        prepare()
                        start()
                    }

                    // progressbar setting.
                    progressBar.max = mediaPlayer.duration
                    progressObservable.mergeWith(Observable.interval(1, TimeUnit.MILLISECONDS))
                        .takeUntil { (it >= mediaPlayer.duration) }
                        .doOnComplete {
                            progressBar.progress = 0
                        }.share()
                        .subscribe {
                            progressBar.progress = it.toInt()
                        }.addTo(compositeDisposable)
                }

                PLAYING_STOP -> {
                    if (mediaPlayer.isPlaying) {
                        kotlin.runCatching {
                            mediaPlayer.stop()
                            progressObservable.onNext(mediaPlayer.duration.toLong())
                        }.onSuccess {
                            onPlayCompleteAnimation()
                        }
                    } else {
                        Unit
                    }
                }

                RECORD_CANCEL -> {
                    mainActivity.recordButtonAnimation(
                        ivRecord,
                        ivRecordStop,
                        btnRecordOrStop,
                        false
                    )
                        .subscribe().addTo(compositeDisposable)
                }

                else -> {

                }
            }
        }
    }

    private fun onPlayCompleteAnimation() {
        mBinding?.run {
            mainActivity.playButtonAnimation(ivPlay, ivPlayStop, btnPlay, false)
                .andThen(
                    btnRecordOrStop.backgroundColor(
                        ContextCompat.getColor(mainActivity, R.color.color_cbcbcb),
                        ContextCompat.getColor(mainActivity, R.color.color_d6222b),
                        duration = ANIMATION_DURATION, reverse = false
                    )
                ).andThen {
                    btnRecordOrStop.isClickable = true
                }.subscribe()
                .addTo(compositeDisposable)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) = onPlayCompleteAnimation()
    override fun onDetach() {
        super.onDetach()
        mediaPlayer.release()
    }

    override fun setUpView(view: View, savedInstanceState: Bundle?) {
        mainActivity.permissionCheck()
        initView()
        initListener()
    }

    private fun initListener() = (mBinding as FragmentSpeaking1Binding).run {
        btnRecordOrStop.throttleClick()
            .filter { mainActivity.permissionCheckFlag }
            .switchMapCompletable {
                val recordFlag = ivRecord.alpha == 1f
                mainActivity.recordButtonAnimation(
                    ivRecord,
                    ivRecordStop,
                    btnRecordOrStop,
                    recordFlag
                ).andThen {
                    if (recordFlag) intentS.accept(Speaking1Intent.Record)
                }
            }.subscribe()
            .addTo(compositeDisposable)

        btnPlay.throttleClick()
            .filter { mainActivity.permissionCheckFlag }
            .switchMapCompletable {
                val playFlag = ivPlay.alpha == 1f
                mainActivity.playButtonAnimation(ivPlay, ivPlayStop, btnPlay, playFlag)
                    .andThen {
                        if (playFlag) intentS.accept(Speaking1Intent.Play)
                        else intentS.accept(Speaking1Intent.Stop)
                    }
            }.subscribe()
            .addTo(compositeDisposable)


        btnHint1.throttleClick()
            .filter { mainActivity.permissionCheckFlag }
            .switchMapCompletable {
                btnHint1.alpha(1f).andThen { intentS.accept(Speaking1Intent.Hint(1)) }
            }.subscribe()
            .addTo(compositeDisposable)

        btnHint2.throttleClick()
            .filter { mainActivity.permissionCheckFlag }
            .switchMapCompletable {
                btnHint1.alpha(1f).andThen { intentS.accept(Speaking1Intent.Hint(2)) }
            }.subscribe()
            .addTo(compositeDisposable)

        btnHint3.throttleClick()
            .filter { mainActivity.permissionCheckFlag }
            .switchMapCompletable {
                btnHint1.alpha(1f).andThen { intentS.accept(Speaking1Intent.Hint(3)) }
            }.subscribe()
            .addTo(compositeDisposable)

    }

    private fun initView() = (mBinding as FragmentSpeaking1Binding).run {
        tvProblemGuide.textSize = mainActivity.commentSize
        tvCurrentEpisode.textSize = mainActivity.buttonSize
        tvHint1.textSize = mainActivity.buttonSize
        tvHint2.textSize = mainActivity.buttonSize
        tvHint3.textSize = mainActivity.buttonSize
    }

    override fun viewIntents(): Observable<Speaking1Intent> = Observable.mergeArray(
        Observable.just(Speaking1Intent.Init), intentS
    )

    companion object {
        val TAG = Speaking1Fragment::class.simpleName
    }

}
