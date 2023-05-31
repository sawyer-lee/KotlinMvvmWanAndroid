package com.sawyer.kotlinmvvmwanandroid.ui.main.mine

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sawyer.kotlinmvvmwanandroid.ext.showToast
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.R.layout.view_change_text_zoom
import com.sawyer.kotlinmvvmwanandroid.common.SeekBarChangeListenerAdapter
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentMineBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.store.SettingsStore
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.model.store.isLogin
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.collect.CollectActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.sawyer.kotlinmvvmwanandroid.ui.history.HistoryActivity
import com.sawyer.kotlinmvvmwanandroid.ui.login.LoginActivity
import com.sawyer.kotlinmvvmwanandroid.ui.opensource.OpenSourceActivity
import com.sawyer.kotlinmvvmwanandroid.ui.points.mine.MinePointsActivity
import com.sawyer.kotlinmvvmwanandroid.util.*
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED

@SuppressLint("SetTextI18n")
class MineFragment : BaseVmFragment<MineViewModel,FragmentMineBinding>(){

    companion object { fun newInstance() = MineFragment() }

    override fun viewModelClass()=MineViewModel::class.java

    override fun initView() {
        binding.scDayNight.isChecked = isNightMode(this.requireContext())
        binding.tvFontSize.text = "${SettingsStore.getWebTextZoom()}%"
        binding.tvClearCache.text = getCacheSize(this.requireContext())

        binding.clHeader.setOnClickListener {
            checkLogin { /*上传头像，暂不支持*/ }
        }
        binding.imgRank.setOnClickListener {
            checkLogin { ActivityHelper.start(MinePointsActivity::class.java) }
        }
        binding.llMyCollect.setOnClickListener {
            checkLogin { ActivityHelper.start(CollectActivity::class.java) }
        }
        binding.llHistory.setOnClickListener {
            ActivityHelper.start(HistoryActivity::class.java)
        }
        binding.llOpenSource.setOnClickListener {
            ActivityHelper.start(OpenSourceActivity::class.java)
        }
        binding.llAboutAuthor.setOnClickListener {
            ActivityHelper.start(
                DetailActivity::class.java,
                mapOf(
                    PARAM_ARTICLE to Article(
                        title = getString(R.string.my_about_author),
                        link = "https://github.com/SawyerLYH"
                    )
                )
            )
        }
        binding.scDayNight.setOnCheckedChangeListener { _, isChecked ->
            setNightMode(isChecked)
            SettingsStore.setNightMode(isChecked)
        }
        binding.llFontSize.setOnClickListener {
            val textZoom = SettingsStore.getWebTextZoom()
            var tempTextZoom = textZoom
            AlertDialog.Builder(this.requireContext())
                .setTitle(R.string.font_size)
                .setView(LayoutInflater.from(this.activity).inflate(view_change_text_zoom, null).apply {
                    val seekBar = findViewById<AppCompatSeekBar>(R.id.seekBar)
                    seekBar.progress = textZoom - 50
                    seekBar.setOnSeekBarChangeListener(SeekBarChangeListenerAdapter(
                        onProgressChanged = { _, progress, _ ->
                            tempTextZoom = 50 + progress
                            binding.tvFontSize.text = "$tempTextZoom%"
                        }
                    ))
                })
                .setNegativeButton(R.string.cancel) { _, _ ->
                    binding.tvFontSize.text = "$textZoom%"
                }
                .setPositiveButton(R.string.confirm) { _, _ ->
                    SettingsStore.setWebTextZoom(tempTextZoom)
                }
                .show()
        }
        binding.llClearCache.setOnClickListener {
            AlertDialog.Builder(this.requireContext())
                .setMessage(R.string.confirm_clear_cache)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    clearCache(this.requireContext())
                    binding.tvClearCache.text = getCacheSize(this.requireContext())
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
        binding.llCheckVersion.setOnClickListener {
            "船新版本会有的！！".showToast()
        }
        binding.imgLogout.setOnClickListener {
            if (isLogin()){
                AlertDialog.Builder(this.requireContext())
                    .setMessage(R.string.confirm_logout)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        mViewModel.logout()
                        ActivityHelper.start(LoginActivity::class.java)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
            }
        }
        updateUi()
    }

    override fun observe() {
        super.observe()
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            updateUi()
        }
    }

    private fun updateUi() {
        val isLogin = isLogin()
        val userInfo = UserInfoStore.getUserInfo()
        binding.tvLoginRegister.isGone = isLogin
        binding.tvNickName.isVisible = isLogin
        binding.tvId.isVisible = isLogin
        if (isLogin && userInfo != null) {
            binding.tvNickName.text = userInfo.nickname
            binding.tvId.text = "ID: ${userInfo.id}"
            val requestOptions = RequestOptions().apply{
                circleCrop()
            }
            Glide.with(this.requireActivity())
                .load(R.drawable.icon_avatar)
                .apply(requestOptions)
                .into(binding.civAvatar)
        }
    }

}