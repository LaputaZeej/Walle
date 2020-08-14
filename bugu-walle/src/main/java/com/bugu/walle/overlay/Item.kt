package com.bugu.walle.overlay

sealed class Item(val text: String) {
    object TagModeItem : Item("TAG")
    object AllTagModelItem : Item("全部显示")
    object HideDateItem : Item("隐藏时间")
    object HideTagItem : Item("隐藏TAG")

    object FilterItem : Item("日志过滤")
    object NoneFilterItem : Item("全部显示")
    object OnlyOkHttpFilterItem : Item("只显示OkHttp")
    object OnlyErrorFilterItem : Item("只显示Error")

    object AppInfoItem : Item("App信息")
    object DevItem : Item("开发者选项")
    object LanguageItem : Item("本地语言")
    object FileItem : Item("沙盒浏览")

    object MoreItem : Item("更多")
}