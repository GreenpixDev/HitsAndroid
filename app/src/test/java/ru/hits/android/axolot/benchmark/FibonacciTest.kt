package ru.hits.android.axolot.benchmark

import org.junit.Test

class FibonacciTest {

    @Test
    fun recursiveTest() {
        val timestamp = System.currentTimeMillis()
        fibonacci(40)
        //println(result)
        println("Calc for ${System.currentTimeMillis() - timestamp} ms")
    }

    fun fibonacci(number: Int): Int {
        if (number == 1 || number == 2) {
            return 1
        }
        var a = Math.random()
        return fibonacci(number - 1) + fibonacci(number - 2)
    }

}