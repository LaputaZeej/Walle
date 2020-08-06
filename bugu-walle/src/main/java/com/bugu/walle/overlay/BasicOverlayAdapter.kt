package com.bugu.walle.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import com.bugu.walle.R
import com.bugu.walle.extension.appInfo
import com.bugu.walle.extension.screenHeight
import com.bugu.walle.extension.screenWidth
import kotlinx.android.synthetic.main.view_walle.view.*

class BasicOverlayAdapter(context: Context) :
    BaseOverlayAdapter<OverlayAdapter.ViewHolder>(context) {

    private val defaultX: Int
    private val defaultY: Int

    init {
        val overlayWidth = context.resources.getDimension(R.dimen.overlay_width)
        defaultX = context.screenWidth - overlayWidth.toInt()
        defaultY = context.screenHeight * 1 / 5
    }

    private var onClickListener: ((View) -> Unit)? = null
    var onSizeChanged: ((Int, Int) -> Unit)? = null
    var mSpread: Boolean = true

    override fun onCreateViewHolder(): OverlayAdapter.ViewHolder =
        OverlayAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_walle, null, false)
        )

    override fun onBindViewHolder(holder: OverlayAdapter.ViewHolder) {
        holder.view.apply {
            iv_reduce.setOnClickListener {
                if (mSpread) {
                    onSizeChanged(false)
                    onClickListener?.invoke(it)
                }
            }

            iv_walle.setOnClickListener {
                if (!mSpread) {
                    onSizeChanged(true)
                    onClickListener?.invoke(it)
                }
            }

            tv_app_info.text = context.appInfo
        }
    }

    private fun View.onSizeChanged(spread: Boolean) {
        if (spread) {
            view_scroll.visibility = View.VISIBLE
            view_top.visibility = View.VISIBLE
            val x = defaultX
            val y = defaultY
            view_bottom.layoutParams = view_bottom.layoutParams.also { param ->
                param.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            onSizeChanged?.invoke(x, y)
            mSpread = true
        } else {
            view_scroll.visibility = View.GONE
            view_top.visibility = View.GONE
            val x = context.screenWidth - iv_walle.measuredWidth
            val y = defaultY
            view_bottom.layoutParams = view_bottom.layoutParams.also { param ->
                param.width = iv_walle.measuredWidth
            }
            onSizeChanged?.invoke(x, y)
            mSpread = false
        }
    }

    override fun onWindowSetting(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = defaultX
            y = defaultY
            format = PixelFormat.RGBA_8888
            gravity = Gravity.LEFT or Gravity.TOP
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            windowAnimations = 0
        }
    }


}