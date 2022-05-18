package ru.hits.android.axolot.math

import org.junit.Test

class MathTest {
    @Test
    fun test() {
        val mathInterpreter = MathInterpreterImpl()
        mathInterpreter.calcExpression("a", mapOf())
    }
}