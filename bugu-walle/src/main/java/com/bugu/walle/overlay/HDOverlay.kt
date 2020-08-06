package com.bugu.walle.overlay

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * 悬浮窗口
 */
class HDOverlay(private val application: Application) {
    private val mWindowManager: WindowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var mDecor: View? = null

    var mOnCancelListener: OnCancelListener? = null

    private var mCreated = false
    private var mShowing = false
    private var mCanceled = false

    private val mHandler = Handler()
    private val DISMISS = 0x43
    private val CANCEL = 0x44
    private val SHOW = 0x45

    companion object {
        private const val TAG = "HDOverlay"
    }

    interface OnCancelListener {
        fun onCancel(hdOverlay: HDOverlay)
    }

}