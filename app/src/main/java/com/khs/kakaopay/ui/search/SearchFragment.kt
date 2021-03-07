package com.khs.kakaopay.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.kakaopay.R
import com.khs.kakaopay.activity.MainActivity
import com.khs.kakaopay.databinding.FragmentSearchBinding
import org.koin.androidx.scope.ScopeFragment

class SearchFragment : ScopeFragment() {
    lateinit var mBinding: FragmentSearchBinding
    private val mainActivity get() = requireActivity() as MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = mBinding.run {
        view?.post { mainActivity.showSearch() }
        mainActivity.setToolbarTitle(TITLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.hideSearchIfNeeded()
    }

    companion object {
        val TAG = SearchFragment::class.simpleName
        private const val TITLE = "브랜드 검색"
    }

}