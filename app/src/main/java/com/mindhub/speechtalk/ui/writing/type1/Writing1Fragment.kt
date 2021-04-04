package com.mindhub.speechtalk.ui.writing.type1

import android.os.Bundle
import android.view.View
import com.attractive.deer.base.BaseFragment
import com.attractive.deer.util.data.toast
import com.jakewharton.rxrelay3.PublishRelay
import com.mindhub.speechtalk.*
import com.mindhub.speechtalk.activity.MainActivity
import com.mindhub.speechtalk.databinding.FragmentWriting1Binding
import com.mindhub.speechtalk.ui.writing.type1.Writing1ViewState.State.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Writing1Fragment : BaseFragment<
        Writing1Intent,
        Writing1ViewState,
        Writing1SingleEvent,
        Writing1ViewModel,
        FragmentWriting1Binding>(R.layout.fragment_writing_1) {

    override val mViewModel: Writing1ViewModel by viewModel()
    private val mainActivity get() = requireActivity() as MainActivity
    private val intentS = PublishRelay.create<Writing1Intent>()

    override fun handleEvent(event: Writing1SingleEvent) {
        TODO("Not yet implemented")
    }

    override fun render(viewState: Writing1ViewState) {
        val (answer, selected, searchedAnswer) = viewState
        Timber.tag(TAG)
            .d("state:${viewState.state}, answer: $answer, selected: $selected, searchedAnswer: $searchedAnswer,")
        mBinding?.run {
            when (viewState.state) {

                INIT -> {
                    // todo 텍스트뷰 정답텍스트로 셋팅해야함.
                }

                SEARCH_CORRECT -> {
                    selected?.let { card ->
                        mainActivity.wordCardCorrectAfterAnimation(card.rootView, card.subView)
                            .andThen { wordTranslation(selected, searchedAnswer) }
                            .subscribe()
                            .addTo(compositeDisposable)
                    }
                }

                SEARCH_WRONG -> {
                    selected?.let {
                        mainActivity.wordCardWrongAfterAnimation(
                            it.subView,
                            it.rootView,
                            it.cardTextView
                        ).subscribe().addTo(compositeDisposable)
                    }
                }

                SEARCH_CORRECT_CLEAR -> {
                    selected?.let { card ->
                        mainActivity.wordCardCorrectAfterAnimation(card.rootView, card.subView)
                            .andThen {
                                wordTranslation(selected, searchedAnswer)
                                it.onComplete()
                            }.delay(1000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete{ context?.toast("정답입니다.") }
                            .subscribe()
                            .addTo(compositeDisposable)

                    }
                }

                HINT -> {
                    mBinding?.run {
                        val answerList = answer.toList()
                        val tvMatrix1 = tvMatrix1.text?.toString()?.trim()
                        val tvMatrix2 = tvMatrix2.text?.toString()?.trim()
                        val tvMatrix3 = tvMatrix3.text?.toString()?.trim()
                        val tvMatrix4 = tvMatrix4.text?.toString()?.trim()
                        val hintObservable = Observable.create<Int> { emitter ->
                            answerList.forEach {
                                when (it.first[it.first.keys.toIntArray()[0]]) {
                                    tvMatrix1 -> emitter.onNext(RIGHT_CARD_TOP_1)
                                    tvMatrix2 -> emitter.onNext(RIGHT_CARD_TOP_2)
                                    tvMatrix3 -> emitter.onNext(RIGHT_CARD_TOP_3)
                                    tvMatrix4 -> emitter.onNext(RIGHT_CARD_TOP_4)
                                }
                            }
                            emitter.onComplete()
                        }
                        mainActivity.run {
                            hintObservable.delayEach(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .concatMapCompletable { // onCompleted(), onError(throwable)
                                    when (it) {
                                        RIGHT_CARD_TOP_1 -> wordCardHintAnimation(bgRightCard1)
                                        RIGHT_CARD_TOP_2 -> wordCardHintAnimation(bgRightCard2)
                                        RIGHT_CARD_TOP_3 -> wordCardHintAnimation(bgRightCard3)
                                        RIGHT_CARD_TOP_4 -> wordCardHintAnimation(bgRightCard4)
                                        else -> Completable.complete()
                                    }
                                }.subscribeOn(Schedulers.io())
                                .subscribe()
                                .addTo(compositeDisposable)
                        }
                    }
                }
            }
            Unit
        }
    }

    private fun wordTranslation(
        selected: SelectedCard?,
        searchedAnswer: Map<Int, String>?
    ) {
        mBinding?.run {
            when (searchedAnswer?.keys?.toIntArray()?.get(0)) {
                LEFT_ANSWER_BOTTOM_1 -> {
                    selected?.cardTextView?.animate()
                        ?.x(tvBottom1.x)
                        ?.y(tvBottom1.y)
                        ?.start()
                }
                LEFT_ANSWER_BOTTOM_2 -> {
                    selected?.cardTextView?.animate()
                        ?.x(tvBottom2.x)
                        ?.y(tvBottom2.y)
                        ?.start()
                }
                else -> null
            }
        }

    }


    override fun setUpView(view: View, savedInstanceState: Bundle?) {
        initView()
        initListener()
    }

    private fun initListener() = mBinding?.run {
        bgRightCard1.throttleClick()
            .take(1)
            .switchMapCompletable {
                val cardContent = tvMatrix1.text.toString().trim()
                mainActivity.wordCardBeforeAnimation(bgRightCard1).andThen {
                    intentS.accept(
                        Writing1Intent.CardClick(
                            RIGHT_CARD_TOP_1,
                            cardContent,
                            rootLytRightCard1,
                            bgRightCard1,
                            tvMatrix1
                        )
                    )
                }
            }.subscribe().addTo(compositeDisposable)

        bgRightCard2.throttleClick()
            .take(1)
            .switchMapCompletable {
                val cardContent = tvMatrix2.text.toString().trim()
                mainActivity.wordCardBeforeAnimation(bgRightCard2).andThen {
                    intentS.accept(
                        Writing1Intent.CardClick(
                            RIGHT_CARD_TOP_2,
                            cardContent,
                            rootLytRightCard2,
                            bgRightCard2,
                            tvMatrix2
                        )
                    )
                }
            }.subscribe().addTo(compositeDisposable)

        bgRightCard3.throttleClick()
            .take(1)
            .switchMapCompletable {
                val cardContent = tvMatrix3.text.toString().trim()
                mainActivity.wordCardBeforeAnimation(bgRightCard3).andThen {
                    intentS.accept(
                        Writing1Intent.CardClick(
                            RIGHT_CARD_TOP_3,
                            cardContent,
                            rootLytRightCard3,
                            bgRightCard3,
                            tvMatrix3
                        )
                    )
                }
            }.subscribe().addTo(compositeDisposable)

        bgRightCard4.throttleClick()
            .take(1)
            .switchMapCompletable {
                val cardContent = tvMatrix4.text.toString().trim()
                mainActivity.wordCardBeforeAnimation(bgRightCard4).andThen {
                    intentS.accept(
                        Writing1Intent.CardClick(
                            RIGHT_CARD_TOP_4,
                            cardContent,
                            rootLytRightCard4,
                            bgRightCard4,
                            tvMatrix4
                        )
                    )
                }
            }.subscribe().addTo(compositeDisposable)

        btnHint.throttleClick(3000)
            .map { Writing1Intent.HintClick }
            .subscribe(intentS).addTo(compositeDisposable)

        rootLytBtnSound.setOnClickListener { }
    }

    override fun viewIntents(): Observable<Writing1Intent> = Observable.mergeArray(
        Observable.just(Writing1Intent.Init), intentS
    )

    private fun initView() = mBinding?.run {
        mainActivity.run {
            tvBottom1.textSize = rightMatrixSize
            tvBottom2.textSize = rightMatrixSize
            tvProblemGuide.textSize = commentSize
            tvCurrentEpisode.textSize = buttonSize
            tvHint.textSize = buttonSize
            tvMatrix1.textSize = rightMatrixSize
            tvMatrix1.bringToFront()
            tvMatrix2.textSize = rightMatrixSize
            tvMatrix3.textSize = rightMatrixSize
            tvMatrix4.textSize = rightMatrixSize
        }
    }

    companion object {
        val TAG = Writing1Fragment::class.simpleName
    }

}