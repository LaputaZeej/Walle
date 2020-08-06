package com.bugu.walle.core

import android.app.Application
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.bugu.walle.extension.viewModels
import com.bugu.walle.log.Message
import com.bugu.walle.overlay.HDOverlay

object Walle: LifecycleObserver {
    lateinit var hdOverlay: HDOverlay
    fun init(application: Application) {
        hdOverlay = HDOverlay(application)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        //hdOverlay.onSizeChange(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        hdOverlay.onSizeChange(false)
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
    fun appendNormal(tag: String, msg: String) {
        append(Message.NormalMessage(System.currentTimeMillis(), tag, msg))
    }

    @JvmStatic
    fun appendError(tag: String, msg: String) {
        append(Message.ErrorMessage(System.currentTimeMillis(), tag, msg))
    }

    @JvmStatic
    fun appendOkHttp(tag: String, msg: String) {
        append(Message.OkHttpMessage(System.currentTimeMillis(), tag, msg))
    }

    @JvmStatic
    private fun show() {
        hdOverlay.show()
    }

    @JvmStatic
    fun dismiss() {
        hdOverlay.dismiss()
    }

    private fun append(msg: Message) {
        hdOverlay.append(msg)
    }

    @JvmStatic
    fun clear() {
        hdOverlay.clear()
    }

    private fun update(msg: Message) {
    }

    @JvmStatic
    fun move(x: Int, y: Int) {
        hdOverlay.move(x, y)
    }


}