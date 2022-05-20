package ru.hits.android.axolot.math

import kotlin.math.*

class MathInterpreterImpl : MathInterpreter {

    val elemStringParseSymbols = "+-*/%()^&|![]<<>>_"

    val doublingsOperators = "<< >>"

    val functionWithBrakes =
        "sin( cos( tg( ctg( arcsin( arccos( arctg( arcctg( ln( lg( trunc( floor( ceil( abs( sqrt( exp( ( ["
    val functionWithBrakesMass = functionWithBrakes.split(" ")
    val mathWithBrakes =
        "sin( cos( tg( ctg( arcsin( arccos( arctg( arcctg( ln( lg( trunc( floor( ceil( abs( sqrt( exp("
    val mathWithBrakesMass = mathWithBrakes.split(" ")

    val math = "sin cos tg ctg arcsin arccos arctg arcctg ln lg trunc floor ceil abs sqrt exp"

    val mathSequence = "^*/%+-&|!<<>>"

    val hiPriority = "^!"
    val miPriority = "*/%"
    val lowPriority = "+-_&|<<>>"

    val postfixFunction = "!"

    private fun getPriority(str: String): Int {
        if (str in hiPriority) {
            return 3
        }
        if (str in miPriority) {
            return 2
        }
        if (str in lowPriority) {
            return 1
        }
        return 0
    }

    private fun parseToElementary(str: String): MutableList<String> {
        val fstr = str.filter { (it != ' ') }
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
            } else if ((elementary[i] + elementary[i + 1]) in functionWithBrakes) {
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
            if (elementary[i] == "-" && (elementary[i - 1] in functionWithBrakesMass || elementary[i - 1] in elemStringParseSymbols && elementary[i - 1] !in ")]")) {
                elementary[i] = "_"
            }
        }
        return elementary
    }



    private fun toPolishAndNum(
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
            } else if (mergedAndUnary[i] == "_") {
                stack.add(mergedAndUnary[i])
            } else if (mergedAndUnary[i] in "([") {
                stack.add(mergedAndUnary[i])
            } else if (mergedAndUnary[i] in functionWithBrakes) {
                stack.add(mergedAndUnary[i])
            } else if (mergedAndUnary[i] == ")") {
                if (stack.isEmpty()) {
                    throw IllegalArgumentException("(error with brackets")
                }
                while (stack.last() !in mathWithBrakes && stack.last() != "(") {
                    polish.add(stack.last())
                    stack.removeLast()
                    if (stack.isEmpty()) {
                        throw IllegalArgumentException("(error with brackets")
                    }
                }
                if (stack.last() == "(") {

                } else {
                    polish.add(stack.last().substring(0, stack.last().length - 1))
                }
                stack.removeLast()
            } else if (mergedAndUnary[i] == "]") {
                if (stack.isEmpty()) {
                    throw IllegalArgumentException("[error with brackets")
                }
                while (stack.last() != "[") {
                    polish.add(stack.last())
                    stack.removeLast()
                    if (stack.isEmpty()) {
                        throw IllegalArgumentException("[error with brackets")
                    }
                }

                polish.add("trunc")
                stack.removeLast()
            } else if (mergedAndUnary[i] in mathSequence) {
                if (stack.isEmpty()) {
                    stack.add(mergedAndUnary[i])
                    continue
                }
                val oPriority = getPriority(mergedAndUnary[i])
                var sPriority = getPriority(stack.last())
                while (stack.last() in math || sPriority >= oPriority || stack.last() == "_") {
                    polish.add(stack.last())
                    stack.removeLast()
                    if (stack.isEmpty()) {
                        break
                    }
                    sPriority = getPriority(stack.last())
                }
                stack.add(mergedAndUnary[i])
            } else {
                throw IllegalArgumentException("I dont know what is this")
            }
        }
        while (stack.isNotEmpty()) {
            polish.add(stack.last())
            stack.removeLast()
        }

        return polish
    }

    fun calc(polish: MutableList<String>): Double {
        val stack: MutableList<Double> = mutableListOf()

        for (i in 0 until polish.size) {
            if (polish[i].toDoubleOrNull() != null) {
                stack.add(polish[i].toDouble())
            } else {
                if (polish[i] == "+") {
                    val a = stack.removeLast()
                    val b = stack.removeLast()
                    stack.add(a + b)
                } else if (polish[i] == "-") {
                    val a = stack.removeLast()
                    val b = stack.removeLast()
                    stack.add(b - a)
                } else if (polish[i] == "*") {
                    val a = stack.removeLast()
                    val b = stack.removeLast()
                    stack.add(a * b)
                } else if (polish[i] == "/") {
                    val a = stack.removeLast()
                    val b = stack.removeLast()
                    stack.add(b / a)
                } else if (polish[i] == "%") {
                    val a = stack.removeLast()
                    val b = stack.removeLast()
                    stack.add(b % a)
                } else if (polish[i] == "^") {
                    val a = stack.removeLast()
                    val b = stack.removeLast()
                    stack.add(b.pow(a))
                } else if (polish[i] == "&") {
                    throw IllegalArgumentException("Не поддерживается" + polish[i])
                } else if (polish[i] == "|") {
                    throw IllegalArgumentException("Не поддерживается" + polish[i])
                } else if (polish[i] == ">>") {
                    throw IllegalArgumentException("Не поддерживается" + polish[i])
                } else if (polish[i] == "<<") {
                    throw IllegalArgumentException("Не поддерживается" + polish[i])
                } else if (polish[i] == "_") {
                    val a = stack.removeLast()
                    stack.add(-a)
                } else if (polish[i] == "!") {
                    val a = stack.removeLast()
                    throw IllegalArgumentException("Не поддерживается" + polish[i])
                } else if (polish[i] == "sin") {
                    val a = stack.removeLast()
                    stack.add(sin(a))
                } else if (polish[i] == "cos") {
                    val a = stack.removeLast()
                    stack.add(cos(a))
                } else if (polish[i] == "tg") {
                    val a = stack.removeLast()
                    stack.add(tan(a))
                } else if (polish[i] == "ctg") {
                    val a = stack.removeLast()
                    stack.add(1 / tan(a))
                } else if (polish[i] == "arcsin") {
                    val a = stack.removeLast()
                    stack.add(asin(a))
                } else if (polish[i] == "arccos") {
                    val a = stack.removeLast()
                    stack.add(acos(a))
                } else if (polish[i] == "arctg") {
                    val a = stack.removeLast()
                    stack.add(atan(a))
                } else if (polish[i] == "arcctg") {
                    val a = stack.removeLast() //TODO(не работает)
                    stack.add(1 / atan(a))
                    throw IllegalArgumentException("Не поддерживается" + polish[i])
                } else if (polish[i] == "ln") {
                    val a = stack.removeLast()
                    stack.add(ln(a))
                } else if (polish[i] == "lg") {
                    val a = stack.removeLast()
                    stack.add(log10(a))
                } else if (polish[i] == "trunc") {
                    val a = stack.removeLast()
                    stack.add(truncate(a))
                } else if (polish[i] == "floor") {
                    val a = stack.removeLast()
                    stack.add(floor(a))
                } else if (polish[i] == "ceil") {
                    val a = stack.removeLast()
                    stack.add(ceil(a))
                } else if (polish[i] == "abs") {
                    val a = stack.removeLast()
                    stack.add(abs(a))
                } else if (polish[i] == "sqrt") {
                    val a = stack.removeLast()
                    stack.add(sqrt(a))
                } else if (polish[i] == "exp") {
                    val a = stack.removeLast()
                    stack.add(exp(a))
                }
            }
        }

        return stack.last()
    }

    override fun calcExpression(expression: String, variables: Map<String, Double>): Double {
        if (expression == "") {
            throw IllegalArgumentException("Send null string.")
        }
        return calc(toPolishAndNum(findUnar(merge(parseToElementary(expression))), variables))
    }

}