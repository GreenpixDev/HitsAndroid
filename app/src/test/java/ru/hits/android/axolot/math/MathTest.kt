package ru.hits.android.axolot.math

import org.junit.Assert
import org.junit.Test
import kotlin.math.*

class MathTest {
    @Test
    fun test() {
        val mathInterpreter = MathInterpreterImpl()
        val variables: MutableMap<String, Double> = mutableMapOf()

        val a = 1.0
        val c = 2.0
        val b = -1.0
        val d = -2.0
        val ab = 3.0
        val ba = 4.0
        val abc = 5.0

        variables["a"] = a
        variables["c"] = c
        variables["b"] = b
        variables["d"] = d
        variables["ab"] = ab
        variables["ba"] = ba
        variables["abc"] = abc

        val delta = 0.000000001

        Assert.assertEquals(
            Double.NaN,
            mathInterpreter.calcExpression("0/0", variables),
            delta
        )

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