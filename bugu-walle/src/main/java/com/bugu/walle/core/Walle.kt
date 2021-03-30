package com.bugu.walle.core

import android.app.Application
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.bugu.walle.extension.viewModels
import com.bugu.walle.log.LogLevelEnum
import com.bugu.walle.log.Message
import com.bugu.walle.log.WalleUncaughtExceptionHandler
import com.bugu.walle.overlay.HDOverlay

object Walle : LifecycleObserver {

    private var hdOverlay: HDOverlay? = null

    private var opened = false

    fun init(application: Application, configuration: Configuration? = null) {
        hdOverlay = HDOverlay(application)
        Thread.setDefaultUncaughtExceptionHandler(WalleUncaughtExceptionHandler())
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        //hdOverlay.onSizeChange(true)
        if (opened)
            show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        //hdOverlay?.onSizeChange(false)
        if (opened)
            dismiss()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        clear()
    }


    private fun checkPermission(activity: FragmentActivity, observer: (Boolean?) -> Unit) {
        checkOverlay(activity) {
            observer(it)
        }
    }

    private fun checkOverlay(activity: FragmentActivity, observer: (Boolean?) -> Unit) {
        activity.viewModelStore.clear() // 会创建多个viewModel
        activity.viewModels<RequestOverlayViewModel>().apply {
            success.observe(activity, Observer(observer))
        }
        activity.supportFragmentManager.beginTransaction()
            .add(RequestOverlayFragment.newInstance(), "checkOverlay")
            .commitAllowingStateLoss()
    }

    @JvmStatic
    fun show(activity: FragmentActivity) {
        checkPermission(activity) {
            if (it == true) {
                Toast.makeText(activity, "请求成功", Toast.LENGTH_SHORT).show()
                show()
            } else {
                Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @JvmStatic
    fun error(tag: String, msg: String) {
        if (!opened) return
        append(Message.ErrorMessage(System.currentTimeMillis(), tag, msg))
    }

    @JvmStatic
    fun okHttp(msg: String) {
        if (!opened) return
        append(Message.OkHttpMessage(System.currentTimeMillis(), msg))
    }

    @JvmStatic
    private fun append(tag: String, msg: String, level: LogLevelEnum) {
        if (!opened) return
        append(Message.NormalMessage(System.currentTimeMillis(), tag, msg, level))
    }

    @JvmStatic
    fun v(tag: String, msg: String) {
        if (!opened) return
        append(tag, msg, LogLevelEnum.VERBOSE)
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (!opened) return
        append(tag, msg, LogLevelEnum.DEBUG)
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (!opened) return
        append(tag, msg, LogLevelEnum.INFO)
    }

    @JvmStatic
    fun w(tag: String, msg: String) {
        if (!opened) return
        append(tag, msg, LogLevelEnum.WARN)
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (!opened) return
        append(tag, msg, LogLevelEnum.ERROR)
    }

    @JvmStatic
    fun a(tag: String, msg: String) {
        if (!opened) return
        append(tag, msg, LogLevelEnum.ASSERT)
    }

    @JvmStatic
    private fun show() {
        opened = true
        hdOverlay?.show()
    }

    @JvmStatic
    fun dismiss() {
        hdOverlay?.dismiss()
    }

    private fun append(msg: Message) {
        hdOverlay?.append(msg)
    }

    @JvmStatic
    private fun clear() {
        hdOverlay?.clear()
    }

    private fun update(msg: Message) {

    }

    @JvmStatic
    fun move(x: Int, y: Int) {
        hdOverlay?.move(x, y)
    }


}