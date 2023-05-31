package com.sawyer.kotlinmvvmwanandroid.ui.main.discovery

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.ext.showToast
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentDiscoveryBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Banner
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.sawyer.kotlinmvvmwanandroid.ui.main.MainActivity
import com.sawyer.kotlinmvvmwanandroid.ui.search.SearchActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoveryFragment : BaseVmFragment<DiscoveryViewModel,FragmentDiscoveryBinding>(), ScrollToTop {

    companion object { fun newInstance() = DiscoveryFragment() }

    override fun viewModelClass()=DiscoveryViewModel::class.java

    private lateinit var hotWordsAdapter: HotWordsAdapter

    override fun scrollToTop() { binding.nestedScrollView.smoothScrollTo(0,0) }

    override fun initView() {
        binding.ivAdd.setOnClickListener {
            "增加分享文章的功能".showToast()
        }
        binding.ivSearch.setOnClickListener {
            ActivityHelper.start(SearchActivity::class.java)
        }
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.getData() }
        }
        binding.reloadView.btnReload.setOnClickListener {
            mViewModel.getData()
        }
        hotWordsAdapter = HotWordsAdapter().apply {
            binding.rvHotWord.adapter = this
            setOnItemClickListener { _, _, position ->
                ActivityHelper.start(
                    SearchActivity::class.java,
                    mapOf(SearchActivity.SEARCH_WORDS to data[position].name)
                )
            }
        }
        binding.nestedScrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (activity is MainActivity && scrollY !=oldScrollY){
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
        }
    }

    override fun initData() { mViewModel.getData() }

    override fun observe() {
        super.observe()
        mViewModel.run {
            banners.observe(viewLifecycleOwner, Observer {
                setBanner(it)
            })
            hotWords.observe(viewLifecycleOwner, Observer {
                hotWordsAdapter.setList(it)
                binding.tvHotWordTitle.isVisible = it.isNotEmpty()
            })
            frequentlyList.observe(viewLifecycleOwner, Observer {
                binding.tagFlowLayout.adapter = TagAdapter(it)
                binding.tagFlowLayout.setOnTagClickListener { _, position, _ ->
                    val frequently = it[position]
                    ActivityHelper.start(
                        DetailActivity::class.java,
                        mapOf(
                            PARAM_ARTICLE to Article(
                                title = frequently.name,
                                link = frequently.link
                            )
                        )
                    )
                    false
                }
                binding.tvFrquently.isGone = it.isEmpty()
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadView.llReload.isVisible = it
            })
        }
    }

    private fun setBanner(banners: List<Banner>) {
        binding.bannerView.run {
            setBannerStyle(BannerConfig.NOT_INDICATOR)
            setImageLoader(BannerImageLoader())
            setImages(banners)
            setBannerAnimation(Transformer.CubeOut)
            start()

            setOnBannerListener{
                val banner = banners[it]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(PARAM_ARTICLE to Article(title = banner.title , link = banner.url))
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bannerView.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        binding.bannerView.stopAutoPlay()
    }

}