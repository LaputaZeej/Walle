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
//        println(Math.round(556 / 5f) * 5)
//        println(Math.round(558 / 5f) * 5)
//        println(Math.round((999 - 410) / 5f) * 5)
//        println(Math.round((999 - 410) / 5f) * 5 + 410)
        println((check5(999 - 410) )/5)
        println(check5((999 - 410) / 5))
        println(check51((999 - 410) / 5))
        println(check52((999 - 410) / 5))


    }
}

internal fun check52(value: Int, divisor: Int = 5) =value / (divisor * 1.0f)
internal fun check5(value: Int, divisor: Int = 5) = Math.round(value / (divisor * 1.0f)) * divisor
internal fun check51(value: Int, divisor: Int = 5) = Math.round(value / (divisor * 1.0f))