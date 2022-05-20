package ru.hits.android.axolot.interpreter.node.executable.string

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.interpreter.node.NodeDependency
import ru.hits.android.axolot.interpreter.node.NodeFunction
import ru.hits.android.axolot.interpreter.variable.Variable

class NodeStringConcatenation : NodeFunction() {

    fun init(vararg input: NodeDependency) {
        for (i in input.indices) {
            dependencies[i] = input[i]
        }
    }

    fun add(input: NodeDependency) {
        dependencies[dependencies.size] = input
    }

    override fun invoke(context: InterpreterContext): Variable {
        throw UnsupportedOperationException("Не поддерживается")
    }
}