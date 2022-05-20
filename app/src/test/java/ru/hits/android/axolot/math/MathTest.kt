package ru.hits.android.axolot.math

import org.junit.Assert
import org.junit.Test
import kotlin.math.*

class MathTest {
    @Test
    fun test() {
        val mathInterpreter = MathInterpreterImpl()
        val variables: MutableMap<String, Double> = mutableMapOf()
        variables["a"] = 1.0
        variables["c"] = 2.0
        variables["b"] = -1.0
        variables["d"] = -1.0
        variables["ab"] = 1.0
        variables["ba"] = 1.0
        variables["abc"] = 1.0

        val delta = 0.000000001

        val a = 1.0
        val c = 2.0
        val b = -1.0
        val d = -1.0
        val ab = 1.0
        val ba = 1.0
        val abc = 1.0
        Assert.assertEquals(
            -123.23 + 213.2,
            mathInterpreter.calcExpression("-123.23+213.2", variables),
            delta
        )
        Assert.assertEquals(
            a + a - c + 0.30 * 2 * (b + 2),
            mathInterpreter.calcExpression("a + a - c +0.30*2*(b +2)", variables),
            delta
        )
        Assert.assertEquals(
            truncate(ab + ba) - abc * 23 + 3 + c,
            mathInterpreter.calcExpression("[ab + ba] - abc* 23 + 3 + c", variables),
            delta
        )
        Assert.assertEquals(
            (a + b) - c * ((a + b) * ((c + d))),
            mathInterpreter.calcExpression("(a + b) - c * ((a + b) * ((c + d)))", variables),
            delta
        )
        Assert.assertEquals(
            -sin(sin(cos(ln(log10(4.0)))) + 23.0 * 32.12),
            mathInterpreter.calcExpression("-sin( sin(cos (ln(lg(4)))) + 23.0 * 32.12)", variables),
            delta
        )
        Assert.assertEquals(
            sin(sin(cos(ln(log10(4.0)))) + 23.0 * 32.12),
            mathInterpreter.calcExpression("sin( sin(cos (ln(lg(4)))) + 23.0 * 32.12)", variables),
            delta
        )
        Assert.assertEquals(
            a + b / 2 - 3 * 4 + (sin(23 % 23.1)),
            mathInterpreter.calcExpression("a + b / 2 - 3 * 4 + (sin(23%23.1))", variables),
            delta
        )
    }
}