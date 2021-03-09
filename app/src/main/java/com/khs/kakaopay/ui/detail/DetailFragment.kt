package com.khs.kakaopay.ui.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxrelay3.PublishRelay
import com.khs.kakaopay.R
import com.khs.kakaopay.activity.MainActivity
import com.khs.kakaopay.databinding.FragmentDetailBinding
import com.lovely.deer.base.BaseFragment
import com.lovely.deer.util.data.toast
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import net.cachapa.expandablelayout.util.FastOutSlowInInterpolator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class DetailFragment : BaseFragment<
        DetailViewIntent,
        DetailViewState,
        DetailSingleEvent,
        DetailViewModel,
        FragmentDetailBinding>(R.layout.fragment_detail) {

    private val args by navArgs<DetailFragmentArgs>()
    private val mainActivity get() = requireActivity() as MainActivity
    override val mViewModel: DetailViewModel by viewModel()
    private val intentS = PublishRelay.create<DetailViewIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareTransitions()
         sharedElementEnterTransition = MaterialContainerTransform().apply {
             duration = 300
             scrimColor = Color.TRANSPARENT
             interpolator = FastOutSlowInInterpolator()
             setPathMotion(MaterialArcMotion())
             fadeMode = MaterialContainerTransform.FADE_MODE_IN
         }
         sharedElementReturnTransition = MaterialContainerTransform().apply {
             duration = 50
             scrimColor = Color.TRANSPARENT
             interpolator = FastOutSlowInInterpolator()
             setPathMotion(MaterialArcMotion())
             fadeMode = MaterialContainerTransform.FADE_MODE_OUT
         }
    }

    private fun prepareTransitions() {
        postponeEnterTransition()
    }

    private fun loadThumbnail() = (mBinding as FragmentDetailBinding).run {
        Glide.with(mainActivity)
            .load(args.book.thumbnail)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.placeholderOf(R.color.color_eaeaea))
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(ivBookImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.hideLikeIfNeeded()
    }

    override fun handleEvent(event: DetailSingleEvent) {
        when(event){
            is DetailSingleEvent.MessageEvent ->{
                context?.toast(event.message,true)
            }
        }
    }

    override fun render(viewState: DetailViewState) {
        val (books, state, error) = viewState
        mainActivity.setLikeStatus(books?.like?:false)
    }

    override fun setUpView(view: View, savedInstanceState: Bundle?) {
        startTransitions()
        mainActivity.run {
            setToolbarTitle(args.book.title ?: NO_TITLE)
            invalidateOptionsMenu()
            showLikeBtn()
            likeBtn.clicks()
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .map { DetailViewIntent.Like }
                .subscribe(intentS)
                .addTo(compositeDisposable)
        }
        loadThumbnail()
    }

    private fun startTransitions() {
        mBinding?.ivBookImage?.transitionName = args.transitionName
        startPostponedEnterTransition()
    }

    override fun viewIntents(): Observable<DetailViewIntent> = Observable.mergeArray(
        Observable.just(DetailViewIntent.Init(args.book)),
        intentS
    )

    companion object {
        val TAG = DetailFragment::class.simpleName
        const val NO_TITLE = "제목없음"
    }
}