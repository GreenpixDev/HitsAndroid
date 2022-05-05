package ru.hits.android.axolot.interpreter.service

import org.junit.Test
import ru.hits.android.axolot.blueprint.node.function.NodeGetVariable
import ru.hits.android.axolot.blueprint.node.function.math.bool.*
import ru.hits.android.axolot.blueprint.scope.GlobalScope
import ru.hits.android.axolot.blueprint.service.impl.function.math.bool.NodeBooleanService
import ru.hits.android.axolot.blueprint.type.Type

class Boolean {


    @Test
    fun not() {

        val scope = GlobalScope()
        val service = NodeBooleanService()

        scope.declareVariable("a", Type.BOOLEAN, true)
        scope.declareVariable("b", Type.BOOLEAN, false)

        val node1 = NodeBooleanNot()
        val getVatA = NodeGetVariable("a")
        node1.init(getVatA)

        val getVatB = NodeGetVariable("b")
        val node2 = NodeBooleanNot()
        node2.init(getVatB)

    }

    @Test
    fun and() {
        val node1 = NodeBooleanAnd()
        val node2 = NodeBooleanAnd()
        val node3 = NodeBooleanAnd()
        val node4 = NodeBooleanAnd()
    }

    @Test
    fun or() {
        val node = NodeBooleanOr()
    }

    @Test
    fun nand() {
        val node = NodeBooleanNand()
    }

    @Test
    fun xor() {
        val node = NodeBooleanXor()
    }

    @Test
    fun xnor() {
        val node = NodeBooleanXnor()
    }

    @Test
    fun nor() {
        val node = NodeBooleanNor()
    }
}