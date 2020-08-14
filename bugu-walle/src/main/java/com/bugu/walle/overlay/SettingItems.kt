package com.bugu.walle.overlay

import com.bugu.walle.BuildConfig

val SETTING_ITEMS: List<SettingItem<Item>>
    get() = listOf(
        SettingItem<Item>(
            Item.TagModeItem.text, Item.TagModeItem, createSettingItemList(
                Item.AllTagModelItem,
                Item.HideDateItem,
                Item.HideTagItem
            )
        ),
        SettingItem<Item>(
            Item.FilterItem.text,
            Item.FilterItem, createSettingItemList(
                Item.NoneFilterItem,
                Item.OnlyOkHttpFilterItem, Item.OnlyErrorFilterItem
            )
        ),
//        SettingItem<Item>(Item.AppInfoItem.text, Item.AppInfoItem, listOf()),
//        SettingItem<Item>(Item.DevItem.text, Item.DevItem, listOf()),
//        SettingItem<Item>(Item.LanguageItem.text, Item.LanguageItem, listOf()),
//        SettingItem<Item>(Item.FileItem.text, Item.FileItem, listOf()),
        SettingItem<Item>("v${BuildConfig.VERSION_NAME}", Item.MoreItem, listOf())
    )

private fun createSettingItemList(vararg items: Item): List<SettingItem<Item>> = items.map {
    SettingItem(it.text, it, listOf())
}