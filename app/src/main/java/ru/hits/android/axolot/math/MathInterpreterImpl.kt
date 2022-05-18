package ru.hits.android.axolot.math

class MathInterpreterImpl : MathInterpreter {

    val elemStringParseSymbols = "+-*/%()^&|![]<<>>_"

    val doublingsOperators = "<< >>"

    val function = "sin( cos( tg( ctg( arcsin( arccos( arctg( arcctg( ln( log( ( ["

    val postfixFunction = "!"

    private fun parseToElementary(str: String): MutableList<String> {
        var fstr = str.filter { (it != ' ') }
        val elementary = mutableListOf("")
        for (i in fstr.indices) {
            if (fstr[i] in elemStringParseSymbols) {
                elementary.add(fstr[i].toString())
                elementary.add("")
            } else {
                elementary[elementary.lastIndex] = elementary[elementary.lastIndex] + fstr[i]
            }
        }
        return elementary.filter { (it != "") }.toMutableList()
    }

    private fun merge(elementary: MutableList<String>): MutableList<String> {

        val merged = mutableListOf<String>()
        var i = 0
        while (i < elementary.size) {
            if (i == elementary.size - 1) {
                merged.add(elementary[i])
            } else if ((elementary[i] + elementary[i + 1]) in doublingsOperators) {
                merged.add(elementary[i] + elementary[i + 1])
                i += 1
            } else if ((elementary[i] + elementary[i + 1]) in function) {
                merged.add(elementary[i] + elementary[i + 1])
                i += 1
            } else {
                merged.add(elementary[i])
            }
            i += 1
        }

        return merged
    }

    private fun findUnar(elementary: MutableList<String>): MutableList<String> {
        if (elementary[0] == "-") {
            elementary[0] = "_"
        }
        for (i in 1 until elementary.size) {
            if (elementary[i] == "-" && (elementary[i - 1] in function || elementary[i - 1] in elemStringParseSymbols)) {
                elementary[i] = "_"
            }
        }
        return elementary
    }

    private fun numerizathion(
        merged: MutableList<String>,
        variables: Map<String, Double>
    ): MutableList<String> {
        return merged
    }

    private fun toPolish(
        mergedAndUnary: MutableList<String>,
        variables: Map<String, Double>
    ): MutableList<String> {

        val polish = mutableListOf<String>()

        val stack = mutableListOf<String>()

        for (i in 0 until mergedAndUnary.size) {
            if (mergedAndUnary[i] in postfixFunction) {
                polish.add(mergedAndUnary[i])
            } else if (mergedAndUnary[i].toDoubleOrNull() != null) {
                polish.add(mergedAndUnary[i])
            } else if (mergedAndUnary[i] in variables.keys) {
                polish.add(variables[mergedAndUnary[i]].toString())
            } else if (mergedAndUnary[i] in function) {
                stack.add(mergedAndUnary[i])
            } else if (mergedAndUnary[i] in function) {
                stack.add(mergedAndUnary[i])
            }
        }


        return mergedAndUnary

    }

    override fun calcExpression(expression: String, variables: Map<String, Double>): Double {

        if (expression == "") {
            throw IllegalArgumentException("Send null string.")
        }

        println(findUnar(merge(parseToElementary("-123.23+213.2"))))
        println(findUnar(merge(parseToElementary("a + a - c +0.30*2*(b +2)"))))
        println(findUnar(merge(parseToElementary("[ab + ba] - abc* 23 + 3 + a << c"))))
        println(findUnar(merge(parseToElementary("(a + b) - c * ((a + b) * ((c + d)))"))))
        println(findUnar(merge(parseToElementary("sin( sin(cos (ln(log(4)))) + 23.0 * 32.12)"))))
        println(findUnar(merge(parseToElementary("----23"))))
        println(findUnar(merge(parseToElementary("a + - 1 * -a + (-ds * - a + 3.2)"))))
//        println(parseToElementary("123.23+213.2"))
//        println(parseToElementary("123.23+213.2"))
        return 0.0
    }

}