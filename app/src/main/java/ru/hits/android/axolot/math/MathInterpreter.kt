package ru.hits.android.axolot.math

interface MathInterpreter {

    fun calcExpression(expression: String, variables: Map<String, Double>): Double

}