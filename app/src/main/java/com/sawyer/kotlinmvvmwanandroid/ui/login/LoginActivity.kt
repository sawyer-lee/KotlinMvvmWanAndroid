package com.sawyer.kotlinmvvmwanandroid.ui.login

import android.view.inputmethod.EditorInfo.IME_ACTION_GO
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityLoginBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmActivity
import com.sawyer.kotlinmvvmwanandroid.ui.register.RegisterActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseVmActivity<LoginViewModel,ActivityLoginBinding>() {

    override fun viewModelClass() =LoginViewModel::class.java

    override fun initView() {
        binding.ivClose.setOnClickListener {
            ActivityHelper.finish(LoginActivity::class.java)
        }
        binding.tvGoRegister.setOnClickListener {
            ActivityHelper.start(RegisterActivity::class.java)
        }
        //输入密码之后，点击键盘上的确定按钮触发登录按钮的点击
        binding.tietPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId== IME_ACTION_GO){
                binding.btnLogin.performClick()
                true
            }else{
                false
            }
        }
        binding.btnLogin.setOnClickListener {
            binding.tilAccount.error=""
            binding.tilPassword.error=""
            val account = binding.tietAccount.text.toString()
            val pwd = binding.tietPassword.text.toString()
            when{
                account.isEmpty() -> binding.tilAccount.error=getString(R.string.account_can_not_be_empty)
                pwd.isEmpty() -> binding.tilPassword.error=getString(R.string.password_can_not_be_empty)
                else ->mViewModel.login(account, pwd)
            }
        }
    }

    override fun observe() {
        mViewModel.run {
            submitting.observe(this@LoginActivity, Observer {
                if (it) showProgressDialog(R.string.logging_in)
                else   hideProgressDialog()
            })

            loginResult.observe(this@LoginActivity, Observer {
                if (it) ActivityHelper.finish(LoginActivity::class.java)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.lottieAnim.cancelAnimation()
    }
}