package com.mindhub.speechtalk.ui.listening

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.attractive.deer.base.BaseFragment
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxrelay3.PublishRelay
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.alpha
import com.mikhaellopez.rxanimation.backgroundColor
import com.mikhaellopez.rxanimation.fadeOut
import com.mindhub.speechtalk.*
import com.mindhub.speechtalk.activity.MainActivity
import com.mindhub.speechtalk.databinding.FragmentListeningBinding
import com.mindhub.speechtalk.ui.listening.Listening1SingleEvent.AnswerSound
import com.mindhub.speechtalk.ui.listening.Listening1SingleEvent.WrongSound
import com.mindhub.speechtalk.ui.listening.Listening1ViewState.State.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ListeningFragment : BaseFragment<
        Listening1Intent,
        Listening1ViewState,
        Listening1SingleEvent,
        ListeningViewModel,
        FragmentListeningBinding>(R.layout.fragment_listening) {

    override val mViewModel: ListeningViewModel by viewModel()
    private val mainActivity get() = requireActivity() as MainActivity
    private val intentS = PublishRelay.create<Listening1Intent>()

    override fun handleEvent(event: Listening1SingleEvent) {
        when (event) {
            WrongSound -> {

            }
            AnswerSound -> {

            }
        }
    }

    override fun render(viewState: Listening1ViewState) {
        val (answer, choice, error, state) = viewState
        Timber.tag(TAG).d("state: $state, answer: $answer, choice: $choice, error: $error")
        val answerView = getChoiceView(answer)
        val choiceView = getChoiceView(choice)
        when (state) {
            CORRECT -> {
                answerView?.correctAnimation(false)?.andThen{
                  findNavController().navigate(ListeningFragmentDirections.actionListening1FragmentToSpeaking1Fragment())
                }?.subscribe()?.addTo(compositeDisposable)
            }
            WRONG -> choiceView?.wrongAnimation(choice)?.subscribe()?.addTo(compositeDisposable)
            HINT -> answerView?.correctAnimation(true)?.subscribe()?.addTo(compositeDisposable)
            else -> { }
        }
    }

    private fun getChoiceView(choice: Int?): View? {
        return when (choice) {
            CARD_FIRST -> (mBinding as FragmentListeningBinding).btnFirstAnimArea
            CARD_SECOND -> (mBinding as FragmentListeningBinding).btnSecondAnimArea
            CARD_THIRD -> (mBinding as FragmentListeningBinding).btnThirdAnimArea
            CARD_FOURTH -> (mBinding as FragmentListeningBinding).btnFourthAnimArea
            else -> null
        }
    }

    override fun setUpView(view: View, savedInstanceState: Bundle?) {
        initView()
        initListener()
    }

    private fun initListener() = (mBinding as FragmentListeningBinding).run {
        rootLytBtnFirst.throttleClick()
            .switchMapCompletable { // at least one
                btnFirstAnimArea.beforeAnimation()
                    .andThen {
                        intentS.accept(Listening1Intent.ImageClick(1))
                    }
            }.subscribe()
            .addTo(compositeDisposable)

        rootLytBtnSecond.throttleClick()
            .switchMapCompletable { // at least one
                btnSecondAnimArea.beforeAnimation()
                    .andThen {
                        intentS.accept(Listening1Intent.ImageClick(2))
                    }
            }.subscribe()
            .addTo(compositeDisposable)

        rootLytBtnThird.throttleClick()
            .switchMapCompletable { // at least one
                btnThirdAnimArea.beforeAnimation()
                    .andThen {
                        intentS.accept(Listening1Intent.ImageClick(3))
                    }
            }.subscribe()
            .addTo(compositeDisposable)

        rootLytBtnFourth.throttleClick()
            .switchMapCompletable { // at least one
                btnFourthAnimArea.beforeAnimation()
                    .andThen {
                        intentS.accept(Listening1Intent.ImageClick(4))
                    }
            }.subscribe()
            .addTo(compositeDisposable)

        rootLytBtnHint.throttleClick()
            .switchMapCompletable { // at least one
                btnFourthAnimArea.hintAnimation()
                    .andThen {
                        intentS.accept(Listening1Intent.HintClick)
                    }
            }.subscribe()
            .addTo(compositeDisposable)
    }

    private fun initView() = (mBinding as FragmentListeningBinding).run {
        tvProblemGuide.textSize = mainActivity.commentSize
        tvCurrentEpisode.textSize = mainActivity.buttonSize
        tvHint.textSize = mainActivity.buttonSize
    }

    private fun View.beforeAnimation() = RxAnimation.together(
        alpha(0.8f, duration = ANIMATION_DURATION, reverse = false),
        backgroundColor(
            ContextCompat.getColor(mainActivity, R.color.color_FFFFFFFF),
            ContextCompat.getColor(mainActivity, R.color.color_cbcbcb),
            duration = ANIMATION_DURATION, reverse = false
        )
    )

    private fun View.wrongAnimation(choice: Int?) = RxAnimation.together(
        backgroundColor(
            ContextCompat.getColor(mainActivity, R.color.color_cbcbcb),
            ContextCompat.getColor(mainActivity, R.color.color_ff848b),
            duration = ANIMATION_DURATION, reverse = false
        )
    ).andThen(viewFadeOut(choice))

    private fun View.correctAnimation(hint: Boolean) = RxAnimation.together(
        backgroundColor(
            ContextCompat.getColor(mainActivity, if (hint) R.color.color_FFFFFFFF else R.color.color_cbcbcb),
            ContextCompat.getColor(mainActivity, R.color.color_a1b9ff),
            duration = ANIMATION_DURATION, reverse = hint
        )
    )

    private fun View.hintAnimation() = alpha(1f)

    private fun viewFadeOut(choice: Int?): CompletableSource? = when (choice) {
        CARD_FIRST -> mBinding?.rootLytBtnFirst?.fadeOut()
        CARD_SECOND -> mBinding?.rootLytBtnSecond?.fadeOut()
        CARD_THIRD -> mBinding?.rootLytBtnThird?.fadeOut()
        CARD_FOURTH -> mBinding?.rootLytBtnFourth?.fadeOut()
        else -> null
    }

    override fun viewIntents(): Observable<Listening1Intent> = Observable.mergeArray(
        Observable.just(Listening1Intent.Init), intentS
    )

    companion object {

        val TAG = ListeningFragment::class.simpleName
    }
}