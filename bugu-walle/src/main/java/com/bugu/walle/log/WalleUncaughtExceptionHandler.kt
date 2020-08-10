package com.bugu.walle.log

import com.bugu.walle.core.Walle
import com.bugu.walle.overlay.AbstractOverlay.Companion.TAG
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

class WalleUncaughtExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        HDLog.e(TAG, "异常来了 $e")
        val info = getStackTraceInfo(e)
        HDLog.e(TAG, "异常信息 $info")
        Walle.error(TAG, info)
    }

    private fun getStackTraceInfo(throwable: Throwable): String {
        var pw: PrintWriter? = null
        val writer: Writer = StringWriter()
        try {
            pw = PrintWriter(writer)
            throwable.printStackTrace(pw)
        } catch (e: Exception) {
            return ""
        } finally {
            pw?.close()
        }
        return writer.toString()
    }
}