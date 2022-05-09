package ru.hits.android.axolot.blueprint.project.libs

import ru.hits.android.axolot.blueprint.declaration.DeclaredNativeBlock
import ru.hits.android.axolot.blueprint.declaration.DeclaredProgramMainBlock
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleInputFlowPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredSingleOutputFlowPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredVarargOutputFlowPin
import ru.hits.android.axolot.blueprint.project.AxolotLibrary
import ru.hits.android.axolot.interpreter.node.executable.NodePrintString
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeSequence

/**
 * Данный класс представляет библиотеку нативных блоков нашего языка.
 * Здесь инициализированы все нативные блоки
 */
class AxolotNativeLibrary : AxolotLibrary() {

    init {
        // Главный блок программы, с которого всё начинается
        declarations["main"] = DeclaredProgramMainBlock(
            DeclaredSingleOutputFlowPin(
                handler = { _, _ -> }
            )
        )

        // Ветвление (условие IF)
        declarations["branch"] = DeclaredNativeBlock(
            DeclaredSingleInputFlowPin(
                nodeFabric = { NodeBranch() }
            ),
            DeclaredSingleInputDataPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodeBranch>()
                        .first().init(node)
                },
                name = "condition"
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
        )

        // Последовательность команд
        declarations["sequence"] = DeclaredNativeBlock(
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
        )

        // Вывод в консоль
        declarations["print"] = DeclaredNativeBlock(
            DeclaredSingleInputFlowPin(
                nodeFabric = { NodePrintString() }
            ),
            DeclaredSingleInputDataPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodePrintString>()
                        .first().init(node)
                },
                name = "text"
            ),
            DeclaredSingleOutputFlowPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodePrintString>()
                        .first().nextNode = node
                },
            )
        )
    }
}