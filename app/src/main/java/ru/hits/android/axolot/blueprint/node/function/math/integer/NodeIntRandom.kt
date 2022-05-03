package ru.hits.android.axolot.blueprint.node.function.math.integer

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.random.Random

class NodeIntRandom : NodeFunction() {

    companion object {
        const val FROM = 0
        const val UNTIL = 1
    }

    fun init(from: NodeDependency, until: NodeDependency) {
        dependencies[FROM] = from
        dependencies[UNTIL] = until
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val from = dependencies[FROM].invoke(context)[Type.INT]!!
        val until = dependencies[UNTIL].invoke(context)[Type.INT]!!
        return Variable(Type.INT, Random.nextInt(from, until))
    }
}