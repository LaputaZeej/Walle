package com.bugu.walle.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.bugu.walle.R
import com.bugu.walle.extension.addViewExt
import com.bugu.walle.extension.screenWidth
import com.bugu.walle.extension.viewModels
import com.bugu.walle.extension.windowManagerService
import com.bugu.walle.overlay.*
import kotlinx.android.synthetic.main.view_walle.view.*

class WalleOverlayWindow(val context: Context) : OverlayWindow {
    private val mWindowManager: WindowManager = context.windowManagerService
    private lateinit var mActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks
    private var mAdapter: BaseOverlayAdapter<OverlayAdapter.ViewHolder> =
        BasicOverlayAdapter(context).apply {
            onSizeChanged = { x, y ->
                move(x, y)
            }
        }

    init {
        val overlayWidth = context.resources.getDimension(R.dimen.overlay_width)

    }

    fun init(context: Context) {
        mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }

        }
    }

    fun setAdapter(adapter: BaseOverlayAdapter<OverlayAdapter.ViewHolder>) {
        mAdapter = adapter
    }

    private fun getOrCreateContainerView() = mAdapter.viewHolder.view

    private fun checkOverlay(activity: FragmentActivity, observer: (Boolean?) -> Unit) {
        activity.viewModels<RequestOverlayViewModel>().apply {
            success.observe(activity, Observer(observer))
        }
        activity.supportFragmentManager.beginTransaction()
            .add(RequestOverlayFragment.newInstance(), "checkOverlay")
            .commitAllowingStateLoss()
    }

    override fun checkPermission(activity: FragmentActivity, observer: (Boolean?) -> Unit) {
        checkOverlay(activity) {
            observer(it)
        }
    }

    override fun show() {
        mWindowManager.addViewExt(
            getOrCreateContainerView(),
            mAdapter.onWindowSetting().apply {

            }
        )

    }

    override fun update(msg: String) {
        getOrCreateContainerView()
            .findViewById<TextView>(R.id.tv_msg).text = msg + "\n"
    }

    override fun move(x: Int, y: Int) {
        val layoutParams = getOrCreateContainerView().layoutParams as WindowManager.LayoutParams
        layoutParams.x = x
        layoutParams.y = y
        getOrCreateContainerView().layoutParams = layoutParams
        mWindowManager.updateViewLayout(getOrCreateContainerView(), layoutParams)
    }


    override fun dismiss() {
        getOrCreateContainerView().run {
            mWindowManager.removeViewImmediate(this)
        }
    }

    override fun append(msg: String) {
        getOrCreateContainerView()
            .findViewById<TextView>(R.id.tv_msg).append(msg + "\n")
    }

    override fun clear() {
        getOrCreateContainerView()
            .tv_msg.text = ""
    }


}