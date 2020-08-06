package com.bugu.walle.log

import com.bugu.walle.extension.formatDate


sealed class Message(open val time: Long, open val tag: String, open val msg: String) {
    data class AppInfoMessage(
        override val time: Long,
        override val tag: String,
        override val msg: String
    ) : Message(time, tag, msg)

    data class NormalMessage(
        override val time: Long,
        override val tag: String,
        override val msg: String
    ) : Message(time, tag, msg)

    data class ErrorMessage(
        override val time: Long,
        override val tag: String,
        override val msg: String
    ) : Message(time, tag, msg)

    data class OkHttpMessage(
        override val time: Long,
        override val tag: String,
        override val msg: String
    ) : Message(time, tag, msg)
}

val Message.simpleMsg
    get() = "[${formatDate(time)}][${tag}]-${msg}"