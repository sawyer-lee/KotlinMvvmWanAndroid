package com.sawyer.kotlinmvvmwanandroid.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sawyer.kotlinmvvmwanandroid.ext.copyTextIntoClipboard
import com.sawyer.kotlinmvvmwanandroid.ext.showToast
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentDetailAcitonsBinding
import com.sawyer.kotlinmvvmwanandroid.ext.openInExplorer
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.sawyer.kotlinmvvmwanandroid.util.share

class ActionFragment : BottomSheetDialogFragment() {

    companion object{
        fun newInstance(article: Article) : ActionFragment{
            return ActionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_ARTICLE,article)
                }
            }
        }
    }
    private var _binding : FragmentDetailAcitonsBinding? = null
    private val binding get() = _binding!!

    private var behavior : BottomSheetBehavior<View>? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAcitonsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            val article = this.getParcelable<Article>(PARAM_ARTICLE) ?: return@run
            binding.llCollect.visibility = if (article.id != 0L) View.VISIBLE else View.GONE
            binding.ivCollect.isSelected = article.collect
            binding.tvCollect.text =
                getString(if (article.collect) R.string.cancel_collect else R.string.add_collect)
            //改变收藏状态
            binding.llCollect.setOnClickListener {
                val detailActivity = (activity as? DetailActivity) ?: return@setOnClickListener
                if (detailActivity.checkLogin()){
                    binding.ivCollect.isSelected = !article.collect
                    detailActivity.changeCollect()
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }else{
                    it.postDelayed({dismiss()},400)
                }
            }
            //分享
            binding.llShare.setOnClickListener {
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                share(activity!!,content = article.title + article.link)
            }
            //用浏览器打开
            binding.llExplorer.setOnClickListener {
                openInExplorer(article.link)
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            //复制至剪切板
            binding.llCopy.setOnClickListener {
                context?.copyTextIntoClipboard(article.link,article.title)
                "复制成功".showToast()
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            //刷新页面
            binding.llRefresh.setOnClickListener {
                (activity as? DetailActivity)?.refreshPage()
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet : View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return

        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun show(fm : FragmentManager){
        if (!this.isAdded){
            super.show(fm,"ActionFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}