package com.sawyer.kotlinmvvmwanandroid.ui.register

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityRegisterBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmActivity
import com.sawyer.kotlinmvvmwanandroid.ui.login.LoginActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseVmActivity<RegisterViewModel,ActivityRegisterBinding>() {

    override fun viewModelClass()=RegisterViewModel::class.java

    override fun initView() {
        binding.ivBack.setOnClickListener { ActivityHelper.finish(RegisterActivity::class.java) }
        binding.tietConfirmPssword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                binding.btnRegister.performClick()
                true
            } else {
                false
            }
        }
        binding.btnRegister.setOnClickListener {
            binding.tilAccount.error = ""
            binding.tilPassword.error = ""
            binding.tilConfirmPssword.error = ""

            val account = binding.tietAccount.text.toString()
            val password = binding.tietPassword.text.toString()
            val confirmPassword = binding.tietConfirmPssword.text.toString()

            when {
                account.isEmpty() -> binding.tilAccount.error = getString(R.string.account_can_not_be_empty)
                account.length < 3 -> binding.tilAccount.error =
                    getString(R.string.account_length_over_three)
                password.isEmpty() -> binding.tilPassword.error =
                    getString(R.string.password_can_not_be_empty)
                password.length < 6 -> binding.tilPassword.error =
                    getString(R.string.password_length_over_six)
                confirmPassword.isEmpty() -> binding.tilConfirmPssword.error =
                    getString(R.string.confirm_password_can_not_be_empty)
                password != confirmPassword -> binding.tilConfirmPssword.error =
                    getString(R.string.two_password_are_inconsistent)
                else -> mViewModel.register(account, password, confirmPassword)
            }
        }
    }

    override fun observe() {
        mViewModel.run {
            submitting.observe(this@RegisterActivity, Observer {
                if (it) showProgressDialog(R.string.registerring) else hideProgressDialog()
            })
            registerResult.observe(this@RegisterActivity, Observer {
                if (it) {
                    ActivityHelper.finish(LoginActivity::class.java, RegisterActivity::class.java)
                }
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.lottieAnim.cancelAnimation()
    }
}