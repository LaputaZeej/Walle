package com.bugu.walle.overlay

import android.app.Activity
import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.bugu.walle.R
import com.bugu.walle.extension.toActivity
import com.bugu.walle.ui.SettingActivity
import java.lang.ref.WeakReference

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
    private val mActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks
    protected var mCurrentActivity: WeakReference<Activity?> = WeakReference(null)

    init {
        mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                mCurrentActivity = WeakReference(null)
            }


            override fun onActivityResumed(activity: Activity) {
                mCurrentActivity = WeakReference(activity)
            }

            override fun onActivityStarted(activity: Activity) {


            }

            override fun onActivityDestroyed(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

        }.also {
            application.registerActivityLifecycleCallbacks(it)
        }
    }

    protected val mHandler = Handler() {
        when (it.what) {
            MSG_UPDATE_FLAG -> {
                @Suppress("UNCHECKED_CAST") val block: Block? = it.obj as Block?
                block?.invoke()
                updateFlag(false)
            }
        }
        false
    }

    interface OnCancelListener {
        fun onCancel(hdOverlay: AbstractOverlay<*>)
    }

    protected fun showDialogFragment(fragment: Fragment, tag: String) {
        try {
            mCurrentActivity.get()?.run {
                if (this is FragmentActivity) {
                    val beginTransaction = this.supportFragmentManager.beginTransaction()
                    beginTransaction.add(android.R.id.content, fragment, tag).show(fragment)
                    beginTransaction.commit()
                }
            }
        }catch (e:Throwable){
            e.printStackTrace()
        }


    }

    protected fun toAppInfo(){
        mCurrentActivity.get()?.run {
            toActivity<SettingActivity> ()
        }
    }


    protected fun dismissFragment(fragment: Fragment) {
        try {
            if (this is FragmentActivity) {
                val beginTransaction = this.supportFragmentManager.beginTransaction()
                beginTransaction.remove(fragment)
                    .commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkResourceId() {
        if (resId <= 0) resId = R.layout.view_walle
    }

    override fun show() {
        checkResourceId()
        if (mCreated) {
            return
        }
        mCreated = true
        mView = LayoutInflater.from(mContext).inflate(resId, null, false).also { view ->
            initView(view)
            initTouchEvent(view)
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
                    flags = FLAG_NOT_FOCUSABLE
                    type =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        else WindowManager.LayoutParams.TYPE_PHONE
                }
            )

        }
    }

    /**
     * 解决悬浮窗口键盘不能编辑的问题
     * 改变flag
     */
    protected fun updateFlag(enable: Boolean, block: Block? = null) {
        mView?.run {

            if(!this.isAttachedToWindow){
                return
            }
            val layout = this.layoutParams.run {
                this@run as WindowManager.LayoutParams
            }.also {
                mHandler.removeMessages(MSG_UPDATE_FLAG)
                if (enable) {
                    mHandler.sendMessageDelayed(Message.obtain().apply {
                        what = MSG_UPDATE_FLAG
                        obj = block
                    }, UPDATE_FLAG_DELAY)
                    it.flags = FLAG_NOT_TOUCH_MODAL
                } else {
                    block?.invoke()
                    it.flags = FLAG_NOT_FOCUSABLE
                }
            }
            post {
                mWindowManager.updateViewLayout(this, layout)
            }
        }

    }

    private fun checkViewAdded(): Boolean {
        return (mView?.isActivated == true || mView?.isAttachedToWindow == true || mView?.windowToken != null)
    }

    abstract fun initTouchEvent(view: View)

    abstract fun initView(view: View)

    abstract fun copy()

    abstract fun defaultLayoutParam(): WindowManager.LayoutParams

    override fun dismiss() {
        mCreated = false
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
        internal const val TAG = "Walle"
        const val MSG_UPDATE_FLAG = 0X02
        const val UPDATE_FLAG_DELAY = 12 * 1000L
    }

}