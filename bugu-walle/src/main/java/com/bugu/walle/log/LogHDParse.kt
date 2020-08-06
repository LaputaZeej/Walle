package com.bugu.walle.log

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.bugu.walle.TAG
import kotlin.jvm.internal.Reflection

fun findAll(packageName:String) {

}

@RequiresApi(Build.VERSION_CODES.O)
fun hook(){
    val clz = Log::class.java
    val methodI = clz.getMethod("i", String::class.java, String::class.java)
}