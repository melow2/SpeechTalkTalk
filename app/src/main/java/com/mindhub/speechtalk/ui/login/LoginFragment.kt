package com.mindhub.speechtalk.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import com.attractive.deer.ext.hideKeyBoard
import com.mindhub.speechtalk.R
import com.mindhub.speechtalk.activity.LoginActivity
import com.mindhub.speechtalk.activity.MainActivity
import com.mindhub.speechtalk.databinding.FragmentLoginBinding
import com.mindhub.speechtalk.setMargins
import org.koin.androidx.scope.ScopeFragment
import timber.log.Timber

class LoginFragment : ScopeFragment() {

    lateinit var mBinding: FragmentLoginBinding
    private val loginActivity get() = requireActivity() as LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() = mBinding.run {
        val fontSize = loginActivity.getFontSize(3)
        edtId.textSize = fontSize
        edtPassword.textSize = fontSize
        btnLogin.textSize = fontSize
    }

    private fun initListener() = mBinding.run {
        edtId.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    edtPassword.requestFocus()
                }
                else -> {
                    false
                }
            }
        }
        edtPassword.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    loginActivity.hideKeyBoard(v)
                    true
                }
                else -> {
                    false
                }
            }
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(loginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            loginActivity.finish()
        }
    }

    companion object {
        val TAG = LoginFragment::class.simpleName
    }
}