package ru.hits.android.axolot.blueprint.project.libs

import ru.hits.android.axolot.blueprint.declaration.NativeBlockType
import ru.hits.android.axolot.blueprint.declaration.pin.*
import ru.hits.android.axolot.blueprint.project.AxolotLibrary
import ru.hits.android.axolot.interpreter.node.executable.NodePrintString
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeSequence
import ru.hits.android.axolot.interpreter.node.function.NodeMath
import ru.hits.android.axolot.interpreter.type.Type

/**
 * Данный класс представляет библиотеку нативных блоков нашего языка.
 * Здесь инициализированы все нативные блоки
 */
class AxolotNativeLibrary : AxolotLibrary() {

    companion object {

        val BLOCK_MAIN = NativeBlockType("main", DeclaredSingleOutputFlowPin({ _, _ -> }))

    }

    init {
        variableTypes["boolean"] = Type.BOOLEAN
        variableTypes["integer"] = Type.INT
        variableTypes["float"] = Type.FLOAT
        variableTypes["string"] = Type.STRING

        // Главный блок программы, с которого всё начинается
        registerNative(BLOCK_MAIN)

        registerNative(
            NativeBlockType(
                "math",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeMath>()
                            .first().init(node)
                    },
                    name = "expression",
                    type = Type.STRING
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeMath() },
                    name = "result",
                    type = Type.FLOAT
                )
            )
        )

        // Ветвление (условие IF)
        registerNative(
            NativeBlockType(
                "branch",
                DeclaredSingleInputFlowPin(
                    nodeFabric = { NodeBranch() },
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeBranch>()
                            .first().init(node)
                    },
                    name = "condition",
                    type = Type.BOOLEAN
                ),
                DeclaredSingleOutputFlowPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeBranch>()
                            .first().trueNode = node
                },
                name = "true"
            ),
            DeclaredSingleOutputFlowPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodeBranch>()
                        .first().falseNode = node
                },
                name = "false"
            )
        ))

        // Последовательность команд
        registerNative(NativeBlockType("sequence",
            DeclaredSingleInputFlowPin(
                nodeFabric = { NodeSequence() }
            ),
            DeclaredVarargOutputFlowPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodeSequence>()
                        .first().then(node)
                },
                namePattern = { "then-$it" },
                minArgs = 1
            )
        ))

        // Вывод в консоль
        registerNative(NativeBlockType("print",
            DeclaredSingleInputFlowPin(
                nodeFabric = { NodePrintString() }
            ),
            DeclaredSingleInputDataPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodePrintString>()
                        .first().init(node)
                },
                name = "text",
                type = Type.STRING
            ),
            DeclaredSingleOutputFlowPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodePrintString>()
                        .first().nextNode = node
                },
            )
        ))
    }

    private fun registerNative(block: NativeBlockType) {
        blockTypes[block.fullName] = block
    }
}