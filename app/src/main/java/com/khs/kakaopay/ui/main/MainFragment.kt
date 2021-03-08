package com.khs.kakaopay.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.kakaopay.R
import com.khs.kakaopay.activity.MainActivity
import com.khs.kakaopay.databinding.FragmentMainBinding
import com.lovely.deer.util.data.observe
import com.lovely.deer.util.data.toast
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainFragment : ScopeFragment() {

    lateinit var mBinding: FragmentMainBinding
    private val mainActivity get() = requireActivity() as MainActivity
    private val mViewModel by viewModel<MainViewModel>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.state.observe(viewLifecycleOwner){ (books,state,error,updatePage) ->
            Timber.tag(TAG).d("[RENDER] - state: $state, books: ${books.size}, error:$error, updatePage:$updatePage")
            context?.toast("$books")

        }

        mViewModel.processIntents(
            Observable.mergeArray(
                mainActivity.textSearchChanges()
                .map { MainIntent.SearchIntent(it) },
            )
        ).addTo(compositeDisposable)
    }


    override fun onResume() {
        super.onResume()
        mainActivity.setToolbarTitle(TITLE)
    }

    companion object {
        val TAG = MainFragment::class.simpleName
        private const val TITLE = "카카오페이 입사과제"
    }
}