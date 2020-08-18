package com.bugu.walle.overlay

import com.bugu.walle.BuildConfig

val SETTING_ITEMS: List<SettingItem<Item>>
    get() = listOf(
        SettingItem<Item>(
            TagModeItem.text, TagModeItem, createSettingItemList(
                AllTagModelItem,
                HideDateItem,
                HideTagItem
            )
        ),
        SettingItem<Item>(
            FilterItem.text,
            FilterItem, createSettingItemList(
                NoneFilterItem,
                OnlyOkHttpFilterItem, OnlyErrorFilterItem
            )
        ),
        SettingItem<Item>(AppInfoItem.text, AppInfoItem, listOf()),
        SettingItem<Item>(DevItem.text, DevItem, listOf()),
        SettingItem<Item>(LanguageItem.text, LanguageItem, listOf()),
        //SettingItem<Item>(FileItem.text, FileItem, listOf()),
        SettingItem<Item>("v${BuildConfig.VERSION_NAME}", MoreItem, listOf())
    )

private fun createSettingItemList(vararg items: Item): List<SettingItem<Item>> = items.map {
    SettingItem(it.text, it, listOf())
}