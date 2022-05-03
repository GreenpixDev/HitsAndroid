package ru.hits.android.axolot.blueprint.node.function.math.real

import ru.hits.android.axolot.interpreter.InterpreterContext
import ru.hits.android.axolot.blueprint.node.NodeDependency
import ru.hits.android.axolot.blueprint.node.NodeFunction
import ru.hits.android.axolot.blueprint.type.Type
import ru.hits.android.axolot.blueprint.variable.Variable
import kotlin.random.Random

class NodeFloatRandom : NodeFunction() {

    companion object {
        const val FROM = 0
        const val UNTIL = 1
    }

    fun init(from: NodeDependency, until: NodeDependency) {
        dependencies[FROM] = from
        dependencies[UNTIL] = until
    }

    override operator fun invoke(context: InterpreterContext): Variable {
        val from = dependencies[FROM].invoke(context)[Type.FLOAT]!!
        val until = dependencies[UNTIL].invoke(context)[Type.FLOAT]!!
        return Variable(Type.FLOAT, Random.nextDouble(from, until))
    }
}