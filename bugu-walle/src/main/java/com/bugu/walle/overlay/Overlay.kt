package com.bugu.walle.overlay


interface Overlay<T> {
    fun show()

    fun dismiss()

    fun append(msg: T)

    fun clear()

    fun update(msg: T)

    fun move(x:Int,y:Int)

    fun changedSpread(spread:Boolean)
}