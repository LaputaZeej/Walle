package com.bugu.walle.overlay

data class SettingItem<T>(
    val text: String,
    val data: T,
    val items: List<SettingItem<T>>
)