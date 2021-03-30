package com.bugu.walle

import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

fun main() {
    // public方法的Lookup
    val publicLookup = MethodHandles.publicLookup()
    // 所有方法的Lookup
    //val lookup = MethodHandles.lookup()
    //val methodType = MethodType.methodType(List::class.java, Array<Any?>::class.java)
    val methodType02 = MethodType.methodType(String::class.java, String::class.java)
    val replaceMH = publicLookup.findVirtual(MethodHandleDemo::class.java, "test", methodType02)
    val result = replaceMH.invoke("hello")
    println(result)
}

class MethodHandleDemo {
    fun test(hello: String): String {
        return "test $hello"
    }

    val s: (x: Int, y: Int) -> String = { x, y -> "$x+$y" }

    fun test(){

    }
}


