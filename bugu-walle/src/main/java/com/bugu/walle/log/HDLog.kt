package com.bugu.walle.log

import android.util.Log

object HDLog {

    fun v(tag: String, msg: String) {
        logger(LogLevelEnum.VERBOSE, tag, msg)
    }

    fun d(tag: String, msg: String) {
        logger(LogLevelEnum.DEBUG, tag, msg)
    }

    fun i(tag: String, msg: String) {
        logger(LogLevelEnum.INFO, tag, msg)
    }

    fun w(tag: String, msg: String) {
        logger(LogLevelEnum.WARN, tag, msg)
    }

    fun e(tag: String, msg: String) {
        logger(LogLevelEnum.ERROR, tag, msg)
    }

    fun a(tag: String, msg: String) {
        logger(LogLevelEnum.ASSERT, tag, msg)
    }

    private fun logger(level: LogLevelEnum, tag: String, msg: String) {
        if (!DEBUG) return
        when (level) {
            LogLevelEnum.VERBOSE -> {
                Log.v(tag, msg)
            }

            LogLevelEnum.DEBUG -> {
                Log.d(tag, msg)
            }

            LogLevelEnum.INFO -> {
                Log.i(tag, msg)
            }

            LogLevelEnum.WARN -> {
                Log.w(tag, msg)
            }

            LogLevelEnum.ERROR -> {
                Log.e(tag, msg)
            }

            LogLevelEnum.ASSERT -> {
                Log.e(tag, msg)
            }
        }
    }


    var DEBUG = true
}