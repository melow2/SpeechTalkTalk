package com.khs.kakaopay.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.recyclerview.scrollEvents
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

    private val mainAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainAdapter(::onClickItem).apply { setHasStableIds(true) }
    }

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
        initRecyclerView()
    }

    private fun initRecyclerView() = mBinding.rcvBooks.run {
        adapter = mainAdapter
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
    }

    /**
     * 1. init ViewModel.
     *
     * - 원래는 onViewCreated()에서 작성을 했었음.
     * - 하지만 현재 MainFragment가 SearchFragment이고, SearchView를 MainActivity가 갖고있음.
     * - MainActivity가 초기화 된 후에 SearchView binding을 사용할 수 있기때문에, onActivityCreated()에 불가피하게 선언.
     * - 결론적으로 mBinding.searchViewChange().. 를 사용하기 위함.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-09 오전 5:41
     **/
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.state.observe(viewLifecycleOwner) { (books, state, error, updatePage) ->
            Timber.tag(TAG).d("[RENDER] - state: $state, books: ${books.size}, error:$error, updatePage:$updatePage")
            mBinding.rootLytSearchOff.isVisible = books.isEmpty()
            mainAdapter.submitList(books)
        }

        mViewModel.processIntents(
            Observable.mergeArray(
                mainActivity.textSearchChanges().map { MainIntent.SearchIntent(it) },
                loadNextPageIntent()
            )
        ).addTo(compositeDisposable)
    }

    private fun loadNextPageIntent(): Observable<MainIntent.LoadNextPage> {
        return mBinding.rcvBooks
            .scrollEvents()
            .filter { (_, _, dy) ->
                val linearLayoutManager = mBinding.rcvBooks.layoutManager as LinearLayoutManager
                dy > 0 && linearLayoutManager.findLastVisibleItemPosition() >= linearLayoutManager.itemCount - 1
            }.map {
                MainIntent.LoadNextPage
            }
    }

    private fun onClickItem(item: MainViewItem.Content) {
        context?.toast(item.book.title ?: "NULL")
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