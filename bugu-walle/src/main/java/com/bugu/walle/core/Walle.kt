package com.bugu.walle.core

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bugu.walle.log.HDLog
import com.bugu.walle.log.ILog
import com.bugu.walle.log.Message
import com.bugu.walle.log.simpleMsg

object Walle : ILog by HDLog() {
    private lateinit var mOverlayWindow: OverlayWindow
    private lateinit var context: Context
    private val messageList: MutableList<Message> = mutableListOf()

    var debug: Boolean = true

    fun init(context: Context) {
        this.context = context.applicationContext
        this.mOverlayWindow = WalleOverlayWindow(context)
    }

    fun show(activity: FragmentActivity) {
        if (!debug) return
        mOverlayWindow.checkPermission(activity) {
            if (it == true) {
                Toast.makeText(activity, "请求成功", Toast.LENGTH_SHORT).show()
                mOverlayWindow.show()
            } else {
                Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun dismiss() {
        if (!debug) return
        mOverlayWindow.dismiss()
    }

    internal fun append(msg: Message) {
        if (!debug) return
        messageList.add(msg)
        mOverlayWindow.append(msg.simpleMsg)
    }

    fun clear() {
        if (!debug) return
        mOverlayWindow.clear()
    }

    internal fun update(msg: Message.NormalMessage) {
        messageList.add(msg)
        if (!debug) return
        mOverlayWindow.update(msg.simpleMsg)
    }
}