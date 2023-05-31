package com.sawyer.kotlinmvvmwanandroid.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentProgressDialogBinding

class ProgressDialogFragment : DialogFragment() {

    private var messageResId: Int? = null
    private var _binding : FragmentProgressDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProgressDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMessage.text = getString(messageResId ?: R.string.loading)
    }

    fun show(
        fragmentManager: FragmentManager,
        @StringRes messageResId: Int,
        isCancelable: Boolean = false
    ) {
        this.messageResId = messageResId
        this.isCancelable = isCancelable
        show(fragmentManager, "progressDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}