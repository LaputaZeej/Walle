package com.bugu.walle.core

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bugu.walle.extension.requestOverlayPermission
import com.bugu.walle.extension.viewModels
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RequestOverlayFragment : Fragment() {

    private var mRequestCode: Int =
        DEFAULT_REQUEST
    lateinit var viewModel: RequestOverlayViewModel
    private var explain = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestCode = arguments?.getInt(REQUEST_CODE) ?: DEFAULT_REQUEST
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = requireActivity().viewModels()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                alertExplainBeforeRequestOverlayPermission(explain)
            } else {
                updateResult(true)
            }
        } else {
            updateResult(true)
        }
    }

    private fun alertExplainBeforeRequestOverlayPermission(show: Boolean) {
        if (show) {
            explainRequestOverlayPermission()
        } else {
            requestOverlayPermission(DEFAULT_REQUEST)
        }
    }

    private fun explainRequestOverlayPermission() {
        AlertDialog.Builder(requireActivity()).setTitle("申请悬浮框权限")
            .setMessage("Walle需要获得你的授权才能使用，请进入设置页面授权。").setPositiveButton("进入") { _, _ ->
                requestOverlayPermission(DEFAULT_REQUEST)
            }.setNegativeButton("取消") { _, _ -> updateResult(false) }
            .create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (mRequestCode) {
            mRequestCode -> {
                MainScope().launch {
                    delay(1000) // 不加延时会失败
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(requireContext())) {
                            updateResult(false)
                        } else {
                            updateResult(true)
                        }
                    } else {
                        updateResult(true)
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun updateResult(success: Boolean) {
        viewModel.result(success)
    }

    companion object {
        private const val DEFAULT_REQUEST = 0xFC
        private const val REQUEST_CODE = "request_code"
        fun newInstance(requestCode: Int = DEFAULT_REQUEST) = RequestOverlayFragment()
            .apply {
            this.arguments = bundleOf(REQUEST_CODE to requestCode)
        }
    }
}