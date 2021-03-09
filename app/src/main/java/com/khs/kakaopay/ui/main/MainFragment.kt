package com.khs.kakaopay.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.recyclerview.scrollEvents
import com.khs.kakaopay.R
import com.khs.kakaopay.activity.MainActivity
import com.khs.kakaopay.databinding.FragmentMainBinding
import com.lovely.deer.util.data.observe
import com.lovely.deer.util.data.observeEvent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        mainActivity.setToolbarTitle(TITLE)

        mViewModel.state.observe(viewLifecycleOwner) { (books, state, error, isEnd, updatePage) ->
            Timber.tag(TAG).d("[RENDER] - state: $state, books: ${books.size}, isEnd:$isEnd, error:$error, updatePage:$updatePage")
            mBinding.rootLytSearchOff.isVisible = books.isEmpty() && updatePage == 1 && state != MainViewState.State.LOADING
            mainAdapter.submitList(books)
        }

        mViewModel.processIntents(
            Observable.mergeArray(
                mainActivity.textSearchChanges().map { MainViewIntent.SearchIntent(it) },
                loadNextPageIntent()
            )
        ).addTo(compositeDisposable)

        mViewModel.singleEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is MainSingleEvent.MessageEvent -> context?.toast(message = event.message, true)
            }
        }
    }

    private fun loadNextPageIntent(): Observable<MainViewIntent.LoadNextPage> {
        return mBinding.rcvBooks
            .scrollEvents()
            .filter { (_, _, dy) ->
                val linearLayoutManager = mBinding.rcvBooks.layoutManager as LinearLayoutManager
                dy > 0 && linearLayoutManager.findLastVisibleItemPosition() >= linearLayoutManager.itemCount - 1
            }.map {
                MainViewIntent.LoadNextPage
            }
    }

    private fun onClickItem(item: MainViewItem.Content) {
        mainActivity.hideSearchIfNeeded()
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(book = item.book))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
    }

    /**
     * 메뉴버튼 클릭 이벤트.
     *
     * 1) 현재 검색화면일 경우 검색창을 보여줌.
     * 2) 아닐 경우 정의된 navigation을 따름.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-07 오전 11:43
     **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search)
            return mainActivity.showSearch().let { true }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val TAG = MainFragment::class.simpleName
        private const val TITLE = "카카오 도서 검색"
    }
}