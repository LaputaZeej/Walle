package com.bugu.walle.overlay

sealed class Item(val text: String) {
    object TagModeItem : Item("标签模式")
    object AllTagModelItem : Item("全部")
    object HideDateItem : Item("隐藏时间")
    object HideTagItem : Item("隐藏TAG")

    object FilterItem : Item("日志过滤")
    object NoneFilterItem : Item("全部")
    object OnlyOkHttpFilterItem : Item("只显示OkHttp")
    object OnlyErrorFilterItem : Item("只显示Error")
    object TextFilterItem : Item("过滤文字")
    
    object MoreItem : Item("更多")
}