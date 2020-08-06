package com.bugu.walle.log

import android.util.Log
import com.bugu.walle.core.Walle

internal interface ILog {
    fun i(tag: String, msg: String)
    fun e(tag: String, msg: String)
    fun okHttp(msg: String)
}

internal class HDLog : ILog {

    override fun i(tag: String, msg: String) {
        Log.i(tag, msg)
        Walle.append(Message.NormalMessage(System.currentTimeMillis(), tag, msg))
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
        Walle.append(Message.ErrorMessage(System.currentTimeMillis(), tag, msg))
    }

    override fun okHttp(msg: String) {
        Log.e("HTTP", msg)
        Walle.append(Message.ErrorMessage(System.currentTimeMillis(), "HTTP", msg))
    }


}