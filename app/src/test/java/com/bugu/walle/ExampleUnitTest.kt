package com.bugu.walle

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {


        try {
            val resp1 = Resp(TODO()).test3()
            val resp2 = Resp(TODO()).test2()
            val resp3 = Resp(TODO()).test1()
        } catch (e: Throwable) {
            //e.printStackTrace()
        }
//        val resp = Resp(null)
//        val data = resp.data
//        println("null is Nothing?  ${null is Nothing?}")
//        println("null is Nothing  ${null is Nothing}")
//        println("data is Nothing  ${data is Nothing}")
//        println("data is Nothing?  ${data is Nothing?}")

        val a: Nothing? = null
        println("print Nothing $a")
    }

    @Test
    fun test01() {
        var a = 0b0000
        var b = 0b1001
        var c = 0b0110
        var d = 0b1111
        println(a and b)
        println(a or b or c)
        println(a xor b xor c xor d)
        println(a shl b)
        println(a shr b)
        println(a ushr b)

        println(b shr 3 and 1)
    }

    // 非局部返回
    @Test
    fun testFooxx() {
//        testFoo()
//        val testFoo01 = testFoo01()
//        println("testFoo01 = $testFoo01")
//        debug01()
        debug02()
    }
}

sealed class A<T, R>(val t: T, val r: R) {
    class AB<R>(r: R) : A<Nothing?, R>(null, r)
    class AC<T>(t: T) : A<T, Nothing?>(t, null)
}


data class Resp<T>(val data: T)
data class Resp3<T : Any>(val data: T)
data class Resp2<T, D>(val data: T, val result: D)

fun debug() {
    val resp = Resp(null)
    val resp4: Resp<Int?> = Resp(null)
    val resp1 = Resp2(null, 1)
    val resp2 = Resp(Unit)
//    val resp3 = Resp3(null)
}

fun test(data: Nothing) {
    println("$data")
}

fun Resp<Nothing>.test1() = Resp(1)
fun Resp<Nothing>.test2() = Resp("222")
fun Resp<Nothing>.test3() = Resp(null)

fun <D> Resp2<Nothing?, D>.test4(result: D) = Resp2(null, result)


open class ViewBinding

abstract class BaseActivity<VB : ViewBinding> {
    open lateinit var vb: VB

    fun onCreate() {
        vb = createVb()
    }

    abstract fun createVb(): VB
}

class AActivity : BaseActivity<ViewBinding>() {
    override fun createVb(): ViewBinding {
        return ViewBinding()
    }

}


fun foo(block: () -> Unit) {
    println("before")
    block()
    println("after")
}

fun testFoo() {
    foo { return@foo }
}


fun debug01() {
    val result = testFoo01()
    println(result)
}

fun testFoo01(): Int {
    val result = foo01 { return 1 }
    return result + 5
}

inline fun foo01(block: () -> Int): Int {
    val s = block()
    return s + 3
}


//
fun debug02() {
    val result = testFoo02()
    println(result)
}

fun testFoo02(): Int {
    val result = foo02 { return@foo02 1 }
    return result + 5
}

fun foo02(block: () -> Int): Int {
    val s = block()
    return s + 3
}

//fun debug03() {
//    val result = testFoo03()
//    println(result)
//}
//
//fun testFoo03(): Int {
//    val result = foo03 { return 1 }
//    return result + 5
//}
//
//inline fun foo03(crossinline block: () -> Int): Int {
//    val s = block()
//    return s + 3
//}

