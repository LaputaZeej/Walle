package com.bugu.walle.overlay

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.os.Message
import android.view.*
import com.bugu.walle.R

/**
 * 悬浮窗口
 */
abstract class AbstractOverlay<T>(private val application: Application, var resId: Int = -1) :
    Overlay<T> {
    protected val mWindowManager: WindowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    protected val clipboardManager: ClipboardManager =
        application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    protected var mLayoutParams: WindowManager.LayoutParams? = null
    var mOnCancelListener: OnCancelListener? = null
    protected var mContext: Context = application.applicationContext
    protected var mView: View? = null
    protected var mCreated = false
    protected var mShowing = false
    protected var mCanceled = false

    protected val mHandler = Handler() {
        when (it.what) {
            1 -> {
                val point = it.obj as Point
                mView?.run {
                    if (isAttachedToWindow) {
                        val newLayoutParam = this.layoutParams.run {
                            this as WindowManager.LayoutParams
                        }.also { param ->
                            param.x += point.x
                            param.y += point.y
                        }
                        mWindowManager.updateViewLayout(this, newLayoutParam)
                    }
                }
            }
        }
        false
    }

    interface OnCancelListener {
        fun onCancel(hdOverlay: AbstractOverlay<*>)
    }


    private fun checkResourceId() {
        if (resId <= 0) resId = R.layout.view_walle
    }

    override fun show() {
        checkResourceId()
        if (mView?.isActivated == true) {
            return
        }
        mView = LayoutInflater.from(mContext).inflate(resId, null, false).also { view ->
            initView(view)
            mWindowManager.addView(
                view,
                mLayoutParams ?: defaultLayoutParam().apply {
                    // 在Android 8.0之前，悬浮窗口设置可以为TYPE_PHONE，这种类型是用于提供用户交互操作的非应用窗口。
                    // 而Android 8.0对系统和API行为做了修改，包括使用SYSTEM_ALERT_WINDOW权限的应用无法再使用一下窗口类型来在其他应用和窗口上方显示提醒窗口
                    // - TYPE_PHONE
                    // - TYPE_PRIORITY_PHONE
                    // - TYPE_SYSTEM_ALERT
                    // - TYPE_SYSTEM_OVERLAY
                    // - TYPE_SYSTEM_ERROR
                    // 如果需要实现在其他应用和窗口上方显示提醒窗口，那么必须该为TYPE_APPLICATION_OVERLAY的新类型
                    type =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        else WindowManager.LayoutParams.TYPE_PHONE
                }
            )
            initTouchEvent(view)
        }
    }

    abstract fun initTouchEvent(view: View)

    abstract fun initView(view: View)

    abstract fun copy()

    abstract fun defaultLayoutParam(): WindowManager.LayoutParams

    override fun dismiss() {
        mHandler.removeCallbacksAndMessages(null)
        mView?.run {
            if (this.isAttachedToWindow) {
                mWindowManager.removeViewImmediate(this)
            }
        }
    }

    override fun move(x: Int, y: Int) {
        mView?.run {
            if (isAttachedToWindow) {
                val newLayoutParam = this.layoutParams.run {
                    this as WindowManager.LayoutParams
                }.also {
                    it.x = x
                    it.y = y
                }
                mWindowManager.updateViewLayout(this, newLayoutParam)
            }
        }
    }

    fun moveBy(x: Int, y: Int) {
        mView?.run {
            if (isAttachedToWindow) {
                val newLayoutParam = this.layoutParams.run {
                    this as WindowManager.LayoutParams
                }.also { param ->
                    param.x += x
                    param.y += y
                }
                mWindowManager.updateViewLayout(this, newLayoutParam)
            }
        }


    }

    companion object {
        internal const val TAG = "HDOverlay"
    }

}