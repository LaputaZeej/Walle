package com.bugu.walle.log

import com.bugu.walle.extension.formatDate


sealed class Message(
    open val time: Long,
    open val tag: String,
    open val msg: String,
    open val level: LogLevelEnum
) {
    data class NormalMessage(
        override val time: Long,
        override val tag: String,
        override val msg: String,
        override val level: LogLevelEnum
    ) : Message(time, tag, msg, level)

    data class ErrorMessage(
        override val time: Long,
        override val tag: String,
        override val msg: String
    ) : Message(time, tag, msg, LogLevelEnum.ERROR)

    data class OkHttpMessage(
        override val time: Long,
        override val msg: String
    ) : Message(time, "OkHttp", msg, LogLevelEnum.VERBOSE)
}

val Message.simpleMsg
    get() = "[${formatDate(time)}][${level.text}][${tag}]-${msg}"