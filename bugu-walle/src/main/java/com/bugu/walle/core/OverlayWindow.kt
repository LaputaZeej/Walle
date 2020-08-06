package com.bugu.walle.core

import androidx.fragment.app.FragmentActivity

interface OverlayWindow {

    fun checkPermission(activity: FragmentActivity, observer: (Boolean?) -> Unit)

    fun show()

    fun dismiss()

    fun append(msg: String)

    fun clear()

    fun update(msg: String)

    fun move(x:Int,y:Int)
}