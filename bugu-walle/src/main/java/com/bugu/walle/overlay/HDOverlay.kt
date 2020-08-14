package com.bugu.walle.overlay

import android.annotation.SuppressLint
import android.app.Application
import android.content.ClipData
import android.graphics.PixelFormat
import android.graphics.Rect
import android.text.Editable
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugu.walle.BuildConfig
import com.bugu.walle.R
import com.bugu.walle.extension.*
import com.bugu.walle.log.HDLog
import com.bugu.walle.log.LogLevelEnum
import com.bugu.walle.log.Message
import com.bugu.walle.log.simpleMsg
import kotlinx.android.synthetic.main.view_walle.view.*
import kotlin.math.abs

/**
 * Walle 悬浮窗口
 */
class HDOverlay(application: Application, resId: Int = -1) :
    AbstractOverlay<Message>(application, resId) {
    private var mAdapter: MessageAdapter? = null
    private var mSettingAdapter: SettingAdapter<Item>? = null
    private var mSettingSecondAdapter: SettingAdapter<Item>? = null
    private var mLogLevelAdapter: SettingAdapter<LogLevelEnum>? = null
    private val mMessageList: MutableList<Message> = mutableListOf()

    // 展开
    private var mSpread: Boolean = true

    // 自动刷新
    private var mAuto: Boolean = true
    private val overlayWidth: Float
    private val overlayHeight: Float
    private val defaultX: Int
    private val defaultY: Int
    private var mLogLevel: LogLevelEnum = LogLevelEnum.VERBOSE
    private var mFilter: FilterMode = FilterMode.ALL
    private var mFilterText: String = ""

    init {
        overlayWidth = mContext.resources.getDimension(R.dimen.overlay_width)
        overlayHeight = mContext.resources.getDimension(R.dimen.overlay_height)
        defaultX = mContext.screenWidth - overlayWidth.toInt()
        defaultY = mContext.screenHeight * 1 / 5
    }


    override fun initView(view: View) {
        with(view) {
            tv_app_info.text = mContext.appInfo
            tv_app_version.text = "v${BuildConfig.VERSION_NAME}"
            // 日志
            recycler.layoutManager = LinearLayoutManager(mContext)
            mAdapter = MessageAdapter(mMessageList).also {
                recycler.adapter = it
            }
            // 设置菜单
            recycler_setting.layoutManager = LinearLayoutManager(mContext)
            mSettingAdapter = SettingAdapter<Item>(mutableListOf()).also {
                recycler_setting.adapter = it
                it.mOnItemClickListener = { _, data ->
                    val items = data.items
                    mSettingSecondAdapter?.setNewData(items)
                }
            }
            // 设置二级菜单
            recycler_setting_second.layoutManager = LinearLayoutManager(mContext)
            mSettingSecondAdapter = SettingAdapter<Item>(mutableListOf()).also {
                it.mOnItemClickListener = { _, data ->
                    when (data.data) {
                        is Item.AllTagModelItem -> {
                            mAdapter?.tagMode = TagMode.ALL
                        }
                        is Item.HideTagItem -> {
                            mAdapter?.tagMode = TagMode.TAG_NONE
                        }
                        is Item.HideDateItem -> {
                            mAdapter?.tagMode = TagMode.DATE_NONE
                        }
                        is Item.NoneFilterItem -> {
                            mFilter = FilterMode.ALL
                            notifyMessageChangedWithFilterMode(mFilter, mFilterText)

                        }
                        is Item.OnlyOkHttpFilterItem -> {
                            mFilter = FilterMode.OK_HTTP
                            notifyMessageChangedWithFilterMode(mFilter, mFilterText)
                        }
                        is Item.OnlyErrorFilterItem -> {
                            mFilter = FilterMode.ERROR
                            notifyMessageChangedWithFilterMode(mFilter, mFilterText)
                        }
                    }
                    changeSetting(false)

                }
                recycler_setting_second.adapter = it
            }
            // 日志显示级别
            rb_v.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    mLogLevel = LogLevelEnum.VERBOSE
                    notifyMessageChanged()
                }
            }
            rb_d.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    mLogLevel = LogLevelEnum.DEBUG
                    notifyMessageChanged()
                }
            }
            rb_i.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    mLogLevel = LogLevelEnum.INFO
                    notifyMessageChanged()
                }
            }
            rb_w.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    mLogLevel = LogLevelEnum.WARN
                    notifyMessageChanged()
                }
            }
            rb_e.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    mLogLevel = LogLevelEnum.ERROR
                    notifyMessageChanged()
                }

            }
            rb_a.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    mLogLevel = LogLevelEnum.ASSERT
                    notifyMessageChanged()
                }
                closeFilterTextInput()
            }
            // 缩小
            iv_reduce.setOnClickListener {
                if (mSpread) {
                    onSizeChanged(false)
                }
                closeFilterTextInput()
            }
            // 复制日志信息
            iv_share.setOnClickListener {
                changeSetting(false)
                copy()
                closeFilterTextInput()
            }
            // 自动刷新到底部
            iv_auto.setOnClickListener {
                changeSetting(false)
                closeFilterTextInput()
                mAuto = !mAuto
                iv_auto.alpha = if (mAuto) 1f else 0.55f
            }
            // 打开过滤编辑
            iv_edit.setOnClickListener {
                //mContext.toast("你有${UPDATE_FLAG_DELAY}ms时间设置过滤~")
                changeSetting(false)
                view_edit.visibility = View.VISIBLE
                et_tag.setText(mFilterText)
                //rg_log_level.visibility = View.VISIBLE
                updateFlag(true) {
                    view_edit.visibility = View.GONE
                    rg_log_level.visibility = View.GONE
                }
            }
            // 过滤文本编辑
            et_tag.setOnFocusChangeListener { _, hasFocus ->
                /* if (hasFocus) {
                     updateFlag(true)
                 } else {
                     updateFlag(false)
                 }*/
            }
            et_tag.addTextChangedListener(object : TextWatcherAdapter() {
                override fun afterTextChanged(s: Editable?) {

                }
            })
            // 设置过滤文字
            tv_tag_send.setOnClickListener {
                val text = et_tag.text.toString()
                mFilterText = text.trim()
                notifyMessageChangedWithFilterMode(FilterMode.ALL, mFilterText)
                closeFilterTextInput()
            }
            // 选择过滤级别
            tv_tag_level.setOnClickListener {
                rg_log_level.visibility = View.VISIBLE

            }

            // 打开设置
            iv_setting.setOnClickListener {
                val vi = view_setting.visibleExt
                changeSetting(!vi)
                closeFilterTextInput()
            }
//            tv_show_mode.setOnClickListener {
//                mAdapter?.level = MessageShowLevel.values()[Random.nextInt(3)]
//                view_setting.visibility = View.GONE
//            }

        }
    }

    private fun closeFilterTextInput() {
        mView?.run {
            updateFlag(false) {
                view_edit.visibility = View.GONE
                rg_log_level.visibility = View.GONE
            }
        }
    }

    private fun notifyMessageChangedWithFilterMode(mode: FilterMode, filterText: String) {

        if (filterText.isNotEmpty()) {
            mAdapter?.messageFilter = { it.tag == filterText || it.msg.contains(filterText) }
        } else {
            mAdapter?.messageFilter = when (mode) {
                FilterMode.ALL -> {
                    { true }
                }
                FilterMode.OK_HTTP -> {
                    {
                        it is Message.OkHttpMessage
                    }
                }
                FilterMode.ERROR -> {
                    {
                        it is Message.ErrorMessage || it.level >= LogLevelEnum.ERROR
                    }
                }
            }
        }
        mAdapter?.setNewData(mMessageList)
    }

    private fun View.notifyMessageChanged() {
        tv_tag_level.text = mLogLevel.text
        rg_log_level.visibility = View.GONE
        mAdapter?.level = mLogLevel
        mAdapter?.setNewData(mMessageList)
    }

    private fun changeSetting(open: Boolean) {
        mView?.run {
            if (open) {
                view_setting.visibility = View.VISIBLE
                mSettingAdapter?.setNewData(SETTING_ITEMS)
            } else {
                view_setting.visibility = View.GONE
                mSettingAdapter?.setNewData(mutableListOf())
                mSettingSecondAdapter?.setNewData(mutableListOf())
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
                    HDLog.i(TAG, "down (x,y) = ($downX,$downY)")
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
                        val scaledTouchSlop = ViewConfiguration.get(mContext).scaledTouchSlop
                        val moveX = event.rawX
                        val moveY = event.rawY
                        val offsetX = moveX - downX
                        val offsetY = moveY - downY
                        if (!(abs(offsetX) < scaledTouchSlop && abs(offsetY) < scaledTouchSlop)) {
                            moveBy(offsetX.toInt(), offsetY.toInt())
                            downX = moveX
                            downY = moveY
                            return@setOnTouchListener true
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    HDLog.i(TAG, "up")
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
                            val viewHeight = view.measuredHeight
                            val topLimit = mContext.screenHeight / 10
                            val bottomLimit = mContext.screenHeight * 9 / 10
                            HDLog.i(
                                TAG,
                                "lastY = $lastY bottomLimit=$bottomLimit topLimit=$topLimit"
                            )
                            val finalY: Float = when {

                                lastY < topLimit -> {
                                    topLimit.toFloat()
                                }
                                lastY >= bottomLimit.toFloat() - viewHeight -> {
                                    bottomLimit.toFloat() - viewHeight
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
            TAG,
            "${mContext.appInfoForCopy} \n\n\n${mMessageList.joinToString(separator = "") {
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
        HDLog.i(
            TAG,
            "mViewRect ${mViewRect.right - mViewRect.left} ${mViewRect.bottom - mViewRect.top}"
        )
    }


    override fun append(msg: Message) {
        mHandler.post {
            if (mMessageList.isEmpty()) {
                val defaultAppMessage = defaultAppMessage()
                mMessageList.add(defaultAppMessage)
                mAdapter?.addData(defaultAppMessage)
            }
            mMessageList.add(msg)
            mAdapter?.addData(msg)
            if (mAuto) {
                mAdapter?.run {
                    mView?.recycler?.scrollToPosition(this.itemCount - 1)
                }
            }
        }
    }

    private fun defaultAppMessage() = Message.NormalMessage(
        System.currentTimeMillis(),
        TAG, "欢迎使用Walle工具！\n${mContext.appInfoForCopy}", LogLevelEnum.INFO
    )

    override fun clear() {
        mCreated =false
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