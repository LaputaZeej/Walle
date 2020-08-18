package com.bugu.walle.overlay

sealed class Item(open val text: String)
sealed class TitleItem(override val text: String):Item(text)
sealed class MenuItem(override val text: String):Item(text)
object TagModeItem : TitleItem("TAG")
object FilterItem : TitleItem("日志过滤")
object MoreItem : TitleItem("更多")
object AppInfoItem : Item("App信息")
object DevItem : Item("开发者选项")
object LanguageItem : Item("本地语言")
object FileItem : Item("沙盒浏览")

object AllTagModelItem : MenuItem("全部显示")
object HideDateItem : MenuItem("隐藏时间")
object HideTagItem : MenuItem("隐藏TAG")
object NoneFilterItem : MenuItem("全部显示")
object OnlyOkHttpFilterItem : MenuItem("只显示OkHttp")
object OnlyErrorFilterItem : MenuItem("只显示Error")



