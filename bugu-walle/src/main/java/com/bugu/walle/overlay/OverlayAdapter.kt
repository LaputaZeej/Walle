package com.bugu.walle.overlay

import android.content.Context
import android.view.View
import android.view.WindowManager
interface OverlayAdapter<VH : OverlayAdapter.ViewHolder> {


    fun onCreateViewHolder(): VH

    fun onBindViewHolder(holder: VH)

    fun onWindowSetting(): WindowManager.LayoutParams

    open class ViewHolder(val view: View)

}

@Suppress("LeakingThis")
abstract class BaseOverlayAdapter<VH : OverlayAdapter.ViewHolder>(val context: Context) :
    OverlayAdapter<VH> {
    val viewHolder: OverlayAdapter.ViewHolder

    init {
        viewHolder = onCreateViewHolder()
        onBindViewHolder(viewHolder)
    }
}

