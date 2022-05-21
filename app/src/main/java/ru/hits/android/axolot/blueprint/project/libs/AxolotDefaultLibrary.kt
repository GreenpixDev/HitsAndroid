package ru.hits.android.axolot.blueprint.project.libs

import ru.hits.android.axolot.blueprint.project.AxolotLibrary

/**
 * Стандартная библиотека нашего языка для любой программы
 * TODO пока содержит только нативные элементы, потом добавим ещё наши
 */
class AxolotDefaultLibrary : AxolotLibrary() {

    init {
        addLibrary(AxolotNativeLibrary())
        /*
        val macrosFor = createMacros("for")
        macrosFor.addInputFlow("")
        macrosFor.addInputData("firstIndex", Type.INT)
        macrosFor.addInputData("lastIndex", Type.INT)
        macrosFor.addOutputFlow("loopBody")
        macrosFor.addOutputFlow("completed")
        macrosFor.addOutputData("index", Type.INT)

        val localInt = macrosFor.createBlock(blockTypes["native.localVariable.int"]!!)

        val firstAssignInt = macrosFor.createBlock(blockTypes["native.assignVariable.int"]!!)
        val branch = macrosFor.createBlock(blockTypes["native.branch"]!!)
        val sequence = macrosFor.createBlock(blockTypes["native.sequence"]!!)
        val assignInt = macrosFor.createBlock(blockTypes["native.assignVariable.int"]!!)
        sequence.contacts.add((sequence.type.declaredPins.find { it is DeclaredVarargOutputFlowPin }!! as DeclaredVarargOutputFlowPin)
            .createOnePin(sequence))

        val lessOrEqual = macrosFor.createBlock(blockTypes["native.math.int.lessOrEquals"]!!)
        val sum = macrosFor.createBlock(blockTypes["native.math.int.sum"]!!)

        connect(
            macrosFor.beginBlock.contacts.find { it is OutputFlowPin && it.name == "" }!!,
            firstAssignInt.contacts.find { it is InputFlowPin && it.name == "" }!!
        )
        connect(
            firstAssignInt.contacts.find { it is OutputFlowPin && it.name == "" }!!,
            branch.contacts.find { it is InputFlowPin && it.name == "" }!!
        )
        connect(
            branch.contacts.find { it is OutputFlowPin && it.name == "true" }!!,
            sequence.contacts.find { it is InputFlowPin && it.name == "" }!!
        )
        connect(
            sequence.contacts.find { it is OutputFlowPin && it.name == "then-1" }!!,
            macrosFor.endBlock.contacts.find { it is InputFlowPin && it.name == "loopBody" }!!
        )
        connect(
            sequence.contacts.find { it is OutputFlowPin && it.name == "then-2" }!!,
            assignInt.contacts.find { it is InputFlowPin && it.name == "" }!!
        )
        connect(
            assignInt.contacts.find { it is OutputFlowPin && it.name == "" }!!,
            branch.contacts.find { it is InputFlowPin && it.name == "" }!!
        )
        connect(
            branch.contacts.find { it is OutputFlowPin && it.name == "true" }!!,
            macrosFor.endBlock.contacts.find { it is InputFlowPin && it.name == "completed" }!!
        )

        connect(
            firstAssignInt.contacts.find { it is InputDataPin && it.name == "ref" }!!,
            localInt.contacts.find { it is OutputDataPin && it.name == "" }!!
        )
        connect(
            firstAssignInt.contacts.find { it is InputDataPin && it.name == "value" }!!,
            macrosFor.beginBlock.contacts.find { it is OutputDataPin && it.name == "firstIndex" }!!
        )
        connect(
            branch.contacts.find { it is InputDataPin && it.name == "condition" }!!,
            lessOrEqual.contacts.find { it is OutputDataPin && it.name == "" }!!
        )
        connect(
            lessOrEqual.contacts.find { it is InputDataPin && it.name == "1" }!!,
            localInt.contacts.find { it is OutputDataPin && it.name == "" }!!
        )
        connect(
            lessOrEqual.contacts.find { it is InputDataPin && it.name == "2" }!!,
            macrosFor.beginBlock.contacts.find { it is OutputDataPin && it.name == "lastIndex" }!!
        )
        connect(
            sum.contacts.find { it is InputDataPin && it.name == "1" }!!,
            localInt.contacts.find { it is OutputDataPin && it.name == "" }!!
        )
        connect(
            firstAssignInt.contacts.find { it is InputDataPin && it.name == "ref" }!!,
            localInt.contacts.find { it is OutputDataPin && it.name == "" }!!
        )
        connect(
            firstAssignInt.contacts.find { it is InputDataPin && it.name == "value" }!!,
            sum.contacts.find { it is OutputDataPin && it.name == "" }!!
        )

        connect(
            macrosFor.endBlock.contacts.find { it is InputDataPin && it.name == "index" }!!,
            localInt.contacts.find { it is OutputDataPin && it.name == "" }!!,
        )

        setValue(sum.contacts.find { it is InputDataPin && it.name == "2" }!! as InputDataPin, Type.INT, 1)*/
    }

}