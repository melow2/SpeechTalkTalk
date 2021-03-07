package com.khs.kakaopay.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.kakaopay.R
import com.khs.kakaopay.activity.MainActivity
import com.khs.kakaopay.databinding.FragmentMainBinding
import org.koin.androidx.scope.ScopeFragment

class MainFragment : ScopeFragment() {

    lateinit var mBinding: FragmentMainBinding
    private val mainActivity get() = requireActivity() as MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() = mBinding.run {
        mainActivity.setToolbarTitle(TITLE)
    }

    companion object {
        val TAG = MainFragment::class.simpleName
        private const val TITLE = "카카오페이 입사과제"
    }
}