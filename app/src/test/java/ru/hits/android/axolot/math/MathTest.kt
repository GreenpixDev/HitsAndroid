package ru.hits.android.axolot.math

import org.junit.Test

class MathTest {
    @Test
    fun test() {
        val mathInterpreter = MathInterpreterImpl()
        var variables: MutableMap<String, Double> = mutableMapOf()
        variables["a"] = 1.0
        variables["c"] = 2.0
        variables["b"] = -1.0
        variables["d"] = -1.0
        variables["ab"] = 1.0
        variables["ba"] = 1.0
        variables["abc"] = 1.0
        println(mathInterpreter.calcExpression("-123.23+213.2", variables))//89.97
        println(mathInterpreter.calcExpression("a + a - c +0.30*2*(b +2)", variables))
        println(mathInterpreter.calcExpression("[ab + ba] - abc* 23 + 3 + c", variables))
        println(mathInterpreter.calcExpression("(a + b) - c * ((a + b) * ((c + d)))", variables))
        println(
            mathInterpreter.calcExpression(
                "-sin( sin(cos (ln(lg(4)))) + 23.0 * 32.12)",
                variables
            )
        )
        println(
            mathInterpreter.calcExpression(
                "sin( sin(cos (ln(lg(4)))) + 23.0 * 32.12)",
                variables
            )
        )
    }
}