package com.bugu.walle.overlay

import android.annotation.SuppressLint
import android.app.Application
import android.content.ClipData
import android.graphics.PixelFormat
import android.graphics.Rect
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugu.walle.R
import com.bugu.walle.extension.appInfo
import com.bugu.walle.extension.screenHeight
import com.bugu.walle.extension.screenWidth
import com.bugu.walle.log.Message
import com.bugu.walle.log.simpleMsg
import kotlinx.android.synthetic.main.view_walle.view.*
import kotlin.random.Random

/**
 * Walle 悬浮窗口
 */
class HDOverlay(application: Application, resId: Int = -1) :
    AbstractOverlay<Message>(application, resId) {
    private var mAdapter: MessageAdapter? = null
    private val mMessageList: MutableList<Message> = mutableListOf()
    private var mSpread: Boolean = true
    private val overlayWidth: Float
    private val overlayHeight: Float
    private val defaultX: Int
    private val defaultY: Int

    init {
        overlayWidth = mContext.resources.getDimension(R.dimen.overlay_width)
        overlayHeight = mContext.resources.getDimension(R.dimen.overlay_height)
        defaultX = mContext.screenWidth - overlayWidth.toInt()
        defaultY = mContext.screenHeight * 1 / 5
    }

    override fun initView(view: View) {
        with(view) {

            tv_app_info.text = mContext.appInfo
            recycler.layoutManager = LinearLayoutManager(mContext)
            mAdapter = MessageAdapter(mutableListOf()).also {
                recycler.adapter = it
            }
            iv_reduce.setOnClickListener {
                if (mSpread) {
                    onSizeChanged(false)
                }
            }

            /* iv_walle.setOnClickListener {
                 if (!mSpread) {
                     onSizeChanged(true)
                 }
             }
             iv_walle.setOnLongClickListener {
                 false
             }*/

            iv_share.setOnClickListener {
                copy()
            }
            iv_setting.setOnClickListener {
                mAdapter?.level = MessageShowLevel.values()[Random.nextInt(3)]
            }

        }
    }

    private var mDrag = false
    private var downX = 0f
    private var downY = 0f
    private val mDragViewLocation = IntArray(2)
    private val mDragViewRect = Rect()
    private val mViewRect = Rect()
    private var lastClickTime: Long = 0
    private var onDragDoubleClickListener: ((View) -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun initTouchEvent(view: View) {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    downY = event.rawY
                    Log.i(TAG, "down (x,y) = ($downX,$downY)")
                    val positionInView = view.iv_walle.containsPoint(event.rawX, event.rawY)
                    if (positionInView) {
                        if (!mSpread) {//当是缩小时
                            val now = System.currentTimeMillis()
                            if (now - lastClickTime < 500) {
                                onDragDoubleClickListener?.invoke(view)
                                view.onSizeChanged(true)
                                return@setOnTouchListener true
                            } else {
                                lastClickTime = now
                            }
                        }
                        mDrag = true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (mDrag) {
                        //val scaledTouchSlop = ViewConfiguration.get(mContext).scaledTouchSlop
                        val scaledTouchSlop = 0
                        val moveX = event.rawX
                        val moveY = event.rawY
                        val offsetX = moveX - downX
                        val offsetY = moveY - downY
                        if (!(offsetX < scaledTouchSlop && offsetY < scaledTouchSlop)) {
                            moveBy(offsetX.toInt(), offsetY.toInt())
                            downX = moveX
                            downY = moveY
                            return@setOnTouchListener true
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Log.i(TAG, "up")
                    if (mDrag) {
                        mDrag = false
                        val offsetX = event.rawX - downX
                        val offsetY = event.rawY - downY
                        val scaledTouchSlop = ViewConfiguration.get(mContext).scaledTouchSlop
                        // 处理边界
                        val newLayoutParam = view.layoutParams.run {
                            this as WindowManager.LayoutParams
                        }.also {
                            val lastX = it.x + offsetX
                            val lastY = it.y + offsetY
                            // 当时展开时用主View的大小，缩小时用
                            /*val viewWidth =if (mSpread){
                                mViewRect.right - mViewRect.left
                            }else{
                                mDragViewRect.right-mViewRect.left
                            }*/

                            val viewWidth = view.measuredWidth
                            val finalX: Float = when {
                                lastX + viewWidth / 2 < mContext.screenWidth / 2 -> {
                                    0f
                                }
                                else -> {
                                    mContext.screenWidth - viewWidth.toFloat()
                                }

                            }

                            val finalY: Float = when {

                                lastY < defaultY -> {
                                    defaultY.toFloat()
                                }
                                lastY >= mContext.screenHeight * 4 / 5f -> {
                                    mContext.screenHeight * 4 / 5f
                                }
                                else -> {
                                    lastY
                                }
                            }
                            it.x = finalX.toInt()
                            it.y = finalY.toInt()
                        }
                        mWindowManager.updateViewLayout(view, newLayoutParam)
                        return@setOnTouchListener true
                    }
                }
            }

            false
        }
    }

    private fun View.containsPoint(x: Float, y: Float): Boolean {
        this.getLocationOnScreen(mDragViewLocation)//屏幕的位置
        this.getLocalVisibleRect(mDragViewRect)//在父控件的大小
        return x > mDragViewLocation[0] && x < mDragViewLocation[0] + mDragViewRect.right - mDragViewRect.left && y > mDragViewLocation[1] && y < mDragViewLocation[1] + mDragViewRect.bottom - mDragViewRect.top
    }


    override fun copy() {
        clipboardManager.primaryClip = ClipData.newPlainText(
            com.bugu.walle.TAG,
            "${mContext.appInfo} \n \n${mMessageList.joinToString(separator = "") {
                it.simpleMsg + "\n"
            }}"
        )
        Toast.makeText(mContext, "已经复制到粘贴板", Toast.LENGTH_SHORT).show()
    }

    override fun defaultLayoutParam(): WindowManager.LayoutParams =
        WindowManager.LayoutParams().apply {
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

    private fun View.onSizeChanged(spread: Boolean) {
        if (spread) {
            view_scroll.visibility = View.VISIBLE
            view_top.visibility = View.VISIBLE
            val x = defaultX
            val y = defaultY
            view_bottom.layoutParams = view_bottom.layoutParams.also { param ->
                param.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            this.requestLayout()
            move(x, y)
            mSpread = true
        } else {
            view_scroll.visibility = View.GONE
            view_top.visibility = View.GONE
            val x = context.screenWidth - iv_walle.measuredWidth
            val y = defaultY
            view_bottom.layoutParams = view_bottom.layoutParams.also { param ->
                param.width = iv_walle.measuredWidth
            }
            this.requestLayout()
            move(x, y)
            mSpread = false
        }

        getLocalVisibleRect(mViewRect)//在父控件的大小
        Log.i(
            TAG,
            "mViewRect ${mViewRect.right - mViewRect.left} ${mViewRect.bottom - mViewRect.top}"
        )
    }

    override fun append(msg: Message) {
        mHandler.post {
            if (mMessageList.isEmpty()) {
                mMessageList.add(defaultAppMessage())
            }
            mMessageList.add(msg)
            mAdapter?.setNewData(mMessageList)
        }
    }

    private fun defaultAppMessage() = Message.AppInfoMessage(
        System.currentTimeMillis(),
        TAG, "欢迎使用Walle工具！"
    )

    override fun clear() {
        mHandler.removeCallbacksAndMessages(null)
        mMessageList.clear()
        mMessageList.add(defaultAppMessage())
        mAdapter?.setNewData(mMessageList)
    }

    override fun update(msg: Message) {

    }

    override fun onSizeChange(spread: Boolean) {
        mView?.onSizeChanged(spread)
    }

}