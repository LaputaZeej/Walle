package com.bugu.walle.overlay

import android.view.View
import com.bugu.walle.log.Message

typealias OnItemClickListener<T> = (View, T) -> Unit

typealias MessageFilter = (Message) -> Boolean

typealias Block = () -> Unit