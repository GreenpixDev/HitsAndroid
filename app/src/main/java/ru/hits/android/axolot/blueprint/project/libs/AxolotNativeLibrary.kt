package ru.hits.android.axolot.blueprint.project.libs

import ru.hits.android.axolot.blueprint.declaration.NativeBlockType
import ru.hits.android.axolot.blueprint.declaration.pin.*
import ru.hits.android.axolot.blueprint.project.AxolotLibrary
import ru.hits.android.axolot.interpreter.node.executable.NodeAsync
import ru.hits.android.axolot.interpreter.node.executable.NodePrintString
import ru.hits.android.axolot.interpreter.node.executable.regex.NodeRegexFind
import ru.hits.android.axolot.interpreter.node.executable.regex.NodeRegexMatch
import ru.hits.android.axolot.interpreter.node.executable.string.NodeStringConcatenation
import ru.hits.android.axolot.interpreter.node.executable.thread.NodeSleep
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeBranch
import ru.hits.android.axolot.interpreter.node.flowcontrol.NodeSequence
import ru.hits.android.axolot.interpreter.node.function.NodeInput
import ru.hits.android.axolot.interpreter.node.function.NodeCast
import ru.hits.android.axolot.interpreter.node.function.NodeInput
import ru.hits.android.axolot.interpreter.node.function.NodeMath
import ru.hits.android.axolot.interpreter.node.function.math.bool.NodeBooleanAnd
import ru.hits.android.axolot.interpreter.node.function.math.bool.NodeBooleanNot
import ru.hits.android.axolot.interpreter.node.function.math.bool.NodeBooleanOr
import ru.hits.android.axolot.interpreter.node.function.math.integer.*
import ru.hits.android.axolot.interpreter.node.function.math.real.*
import ru.hits.android.axolot.interpreter.node.function.math.trig.*
import ru.hits.android.axolot.interpreter.node.macros.NodeAssignVariable
import ru.hits.android.axolot.interpreter.node.macros.NodeLocalVariable
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
        variableTypes["int"] = Type.INT
        variableTypes["float"] = Type.FLOAT
        variableTypes["string"] = Type.STRING

        // Преобразования типов (сделано потупому, потому что у Ромы косяк в архитектуре)
        for (from in variableTypes) {
            for (to in variableTypes) {
                if (from != to) {
                    registerBlock(
                        NativeBlockType(
                            "cast.${from.key}_${to.key}",
                            DeclaredSingleInputDataPin(
                                handler = { target, node ->
                                    target
                                        .filterIsInstance<NodeCast>()
                                        .first().init(node)
                                },
                                type = from.value
                            ),
                            DeclaredSingleOutputDataPin(
                                nodeFabric = { NodeCast(to.value) },
                                type = to.value
                            )
                        )
                    )
                }
            }
        }

        // Главный блок программы, с которого всё начинается
        registerBlock(BLOCK_MAIN)

        // input
        registerBlock(
            NativeBlockType(
                "input",

                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeInput() },
                    type = Type.STRING
                )
            )
        )

        // string + string
        registerBlock(
            NativeBlockType(
                "sumStrings",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeStringConcatenation>()
                            .first().add(node)
                    },
                    type = Type.STRING,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeStringConcatenation() },
                    type = Type.STRING
                )
            )
        )

        // Регулярные выражения match
        registerBlock(
            NativeBlockType(
                "regexMatch",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeRegexMatch>()
                            .first().dependencies[NodeRegexMatch.TEXT] = node
                    },
                    name = "text",
                    type = Type.STRING
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeRegexMatch>()
                            .first().dependencies[NodeRegexMatch.REGEX_TEXT] = node
                    },
                    name = "regex",
                    type = Type.STRING
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeRegexMatch() },
                    type = Type.BOOLEAN
                )
            )
        )

        // Регулярные выражения find
        registerBlock(
            NativeBlockType(
                "regexFind",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeRegexMatch>()
                            .first().dependencies[NodeRegexFind.TEXT] = node
                    },
                    name = "text",
                    type = Type.STRING
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeRegexMatch>()
                            .first().dependencies[NodeRegexFind.REGEX_TEXT] = node
                    },
                    name = "regex",
                    type = Type.STRING
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeRegexMatch>()
                            .first().dependencies[NodeRegexFind.START_INDEX] = node
                    },
                    name = "start index",
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeRegexMatch() },
                    type = Type.STRING
                )
            )
        )

        // Узел ассинхронности
        registerBlock(
            NativeBlockType(
                "async",
                DeclaredSingleInputFlowPin(
                    nodeFabric = { NodeAsync() },
                ),
                DeclaredSingleOutputFlowPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeAsync>()
                            .first().nextNode = node
                    },
                    name = "async"
                )
            )
        )

        registerBlock(
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
        registerBlock(
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
        registerBlock(NativeBlockType("sequence",
            DeclaredSingleInputFlowPin(
                nodeFabric = { NodeSequence() }
            ),
            DeclaredVarargOutputFlowPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodeSequence>()
                        .first().then(node)
                },
                lazyName = { "then-$it" },
                minArgs = 1
            )
        )
        )

        // Вывод в консоль
        registerBlock(
            NativeBlockType(
                "print",
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
        )
        )

        // Остановить поток на n миллисекунд
        registerBlock(NativeBlockType("sleep",
            DeclaredSingleInputFlowPin(
                nodeFabric = { NodeSleep() }
            ),
            DeclaredSingleInputDataPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodeSleep>()
                        .first().init(node)
                },
                name = "delay",
                type = Type.INT
            ),
            DeclaredSingleOutputFlowPin(
                handler = { target, node ->
                    target
                        .filterIsInstance<NodeSleep>()
                        .first().nextNode = node
                },
            )
        )
        )

        /*
         *
         *
         *
         */

        // not bool
        registerBlock(
            NativeBlockType(
                "math.boolean.not",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeBooleanNot>()
                            .first().dependencies[NodeBooleanNot.INPUT] = node
                    },
                    type = Type.BOOLEAN
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeBooleanNot() },
                    type = Type.BOOLEAN
                )
            )
        )

        // bool && bool
        registerBlock(
            NativeBlockType(
                "math.boolean.and",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeBooleanAnd>()
                            .first().dependencies[NodeBooleanAnd.FIRST] = node
                    },
                    type = Type.BOOLEAN
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeBooleanAnd>()
                            .first().dependencies[NodeBooleanAnd.SECOND] = node
                    },
                    type = Type.BOOLEAN
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeBooleanAnd() },
                    type = Type.BOOLEAN
                )
            )
        )

        // bool || bool
        registerBlock(
            NativeBlockType(
                "math.boolean.or",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeBooleanOr>()
                            .first().dependencies[NodeBooleanOr.FIRST] = node
                    },
                    type = Type.BOOLEAN
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeBooleanOr>()
                            .first().dependencies[NodeBooleanOr.SECOND] = node
                    },
                    type = Type.BOOLEAN
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeBooleanOr() },
                    type = Type.BOOLEAN
                )
            )
        )

        /*
         *
         *
         *
         */

        // int + int
        registerBlock(
            NativeBlockType(
                "math.int.sum",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntSum>()
                            .first().add(node)
                    },
                    type = Type.INT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntSum() },
                    type = Type.INT
                )
            )
        )

        // int - int
        registerBlock(
            NativeBlockType(
                "math.int.sub",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntSub>()
                            .first().dependencies[NodeIntSub.FIRST] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntSub>()
                            .first().dependencies[NodeIntSub.SECOND] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntSub() },
                    type = Type.INT
                )
            )
        )

        // int * int
        registerBlock(
            NativeBlockType(
                "math.int.mul",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntMul>()
                            .first().add(node)
                    },
                    type = Type.INT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntMul() },
                    type = Type.INT
                )
            )
        )

        // abs int
        registerBlock(
            NativeBlockType(
                "math.int.abs",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntAbs>()
                            .first().dependencies[NodeIntAbs.INPUT] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntAbs() },
                    type = Type.INT
                )
            )
        )

        // min int
        registerBlock(
            NativeBlockType(
                "math.int.min",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntMin>()
                            .first().add(node)
                    },
                    type = Type.INT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntMin() },
                    type = Type.INT
                )
            )
        )

        // max int
        registerBlock(
            NativeBlockType(
                "math.int.max",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntMax>()
                            .first().add(node)
                    },
                    type = Type.INT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntMax() },
                    type = Type.INT
                )
            )
        )

        // equals int
        registerBlock(
            NativeBlockType(
                "math.int.equals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntEqual>()
                            .first().dependencies[NodeIntEqual.FIRST] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntEqual>()
                            .first().dependencies[NodeIntEqual.SECOND] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        // not equals int
        registerBlock(
            NativeBlockType(
                "math.int.notEquals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntNotEqual>()
                            .first().dependencies[NodeIntNotEqual.FIRST] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntNotEqual>()
                            .first().dependencies[NodeIntNotEqual.SECOND] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntNotEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less int
        registerBlock(
            NativeBlockType(
                "math.int.less",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntLess>()
                            .first().dependencies[NodeIntLess.FIRST] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntLess>()
                            .first().dependencies[NodeIntLess.SECOND] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntLess() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less or equals int
        registerBlock(
            NativeBlockType(
                "math.int.lessOrEquals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntLessOrEqual>()
                            .first().dependencies[NodeIntLessOrEqual.FIRST] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntLessOrEqual>()
                            .first().dependencies[NodeIntLessOrEqual.SECOND] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntLessOrEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less int
        registerBlock(
            NativeBlockType(
                "math.int.more",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntMore>()
                            .first().dependencies[NodeIntMore.FIRST] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntMore>()
                            .first().dependencies[NodeIntMore.SECOND] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntMore() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less or equals int
        registerBlock(
            NativeBlockType(
                "math.int.moreOrEquals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntMoreOrEqual>()
                            .first().dependencies[NodeIntMoreOrEqual.FIRST] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeIntMoreOrEqual>()
                            .first().dependencies[NodeIntMoreOrEqual.SECOND] = node
                    },
                    type = Type.INT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeIntMoreOrEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        /*
         *
         *
         *
         */

        // float + float
        registerBlock(
            NativeBlockType(
                "math.float.sum",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatSum>()
                            .first().add(node)
                    },
                    type = Type.FLOAT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatSum() },
                    name = "",
                    type = Type.FLOAT
                )
            )
        )

        // float - float
        registerBlock(
            NativeBlockType(
                "math.float.sub",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatSub>()
                            .first().dependencies[NodeFloatSub.FIRST] = node
                    },
                    name = "1",
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatSub>()
                            .first().dependencies[NodeFloatSub.SECOND] = node
                    },
                    name = "2",
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatSub() },
                    name = "",
                    type = Type.FLOAT
                )
            )
        )

        // float * float
        registerBlock(
            NativeBlockType(
                "math.float.mul",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatMul>()
                            .first().add(node)
                    },
                    type = Type.FLOAT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatMul() },
                    name = "",
                    type = Type.FLOAT
                )
            )
        )

        // abs int
        registerBlock(
            NativeBlockType(
                "math.float.abs",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatAbs>()
                            .first().dependencies[NodeFloatAbs.INPUT] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatAbs() },
                    type = Type.FLOAT
                )
            )
        )

        // min int
        registerBlock(
            NativeBlockType(
                "math.float.min",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatMin>()
                            .first().add(node)
                    },
                    type = Type.FLOAT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatMin() },
                    type = Type.FLOAT
                )
            )
        )

        // max int
        registerBlock(
            NativeBlockType(
                "math.float.max",
                DeclaredVarargInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatMax>()
                            .first().add(node)
                    },
                    type = Type.FLOAT,
                    minArgs = 2
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatMax() },
                    type = Type.FLOAT
                )
            )
        )

        // equals int
        registerBlock(
            NativeBlockType(
                "math.float.equals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatEqual>()
                            .first().dependencies[NodeFloatEqual.FIRST] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatEqual>()
                            .first().dependencies[NodeFloatEqual.SECOND] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        // not equals int
        registerBlock(
            NativeBlockType(
                "math.float.notEquals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatNotEqual>()
                            .first().dependencies[NodeFloatNotEqual.FIRST] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatNotEqual>()
                            .first().dependencies[NodeFloatNotEqual.SECOND] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatNotEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less int
        registerBlock(
            NativeBlockType(
                "math.float.less",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatLess>()
                            .first().dependencies[NodeFloatLess.FIRST] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatLess>()
                            .first().dependencies[NodeFloatLess.SECOND] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatLess() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less or equals int
        registerBlock(
            NativeBlockType(
                "math.float.lessOrEquals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatLessOrEqual>()
                            .first().dependencies[NodeFloatLessOrEqual.FIRST] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatLessOrEqual>()
                            .first().dependencies[NodeFloatLessOrEqual.SECOND] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatLessOrEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less int
        registerBlock(
            NativeBlockType(
                "math.float.more",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatMore>()
                            .first().dependencies[NodeFloatMore.FIRST] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatMore>()
                            .first().dependencies[NodeFloatMore.SECOND] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatMore() },
                    type = Type.BOOLEAN
                )
            )
        )

        // less or equals int
        registerBlock(
            NativeBlockType(
                "math.float.moreOrEquals",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatMoreOrEqual>()
                            .first().dependencies[NodeFloatMoreOrEqual.FIRST] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeFloatMoreOrEqual>()
                            .first().dependencies[NodeFloatMoreOrEqual.SECOND] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeFloatMoreOrEqual() },
                    type = Type.BOOLEAN
                )
            )
        )

        /*
         *
         *
         *
         */

        // log
        registerBlock(
            NativeBlockType(
                "math.log",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeLog>()
                            .first().dependencies[NodeLog.NUMBER] = node
                    },
                    name = "number",
                    type = Type.FLOAT
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeLog>()
                            .first().dependencies[NodeLog.BASE] = node
                    },
                    name = "base",
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeLog() },
                    type = Type.FLOAT
                )
            )
        )

        registerBlock(
            NativeBlockType(
                "math.sin",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeSin>()
                            .first().dependencies[NodeSin.INPUT] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeSin() },
                    type = Type.FLOAT
                )
            )
        )

        registerBlock(
            NativeBlockType(
                "math.cos",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeCos>()
                            .first().dependencies[NodeCos.INPUT] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeCos() },
                    type = Type.FLOAT
                )
            )
        )

        registerBlock(
            NativeBlockType(
                "math.tan",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeTan>()
                            .first().dependencies[NodeTan.INPUT] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeTan() },
                    type = Type.FLOAT
                )
            )
        )

        registerBlock(
            NativeBlockType(
                "math.asin",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeArcSin>()
                            .first().dependencies[NodeArcSin.INPUT] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeArcSin() },
                    type = Type.FLOAT
                )
            )
        )

        registerBlock(
            NativeBlockType(
                "math.acos",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeArcCos>()
                            .first().dependencies[NodeArcCos.INPUT] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeArcCos() },
                    type = Type.FLOAT
                )
            )
        )

        registerBlock(
            NativeBlockType(
                "math.atan",
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeArcTan>()
                            .first().dependencies[NodeArcTan.INPUT] = node
                    },
                    type = Type.FLOAT
                ),
                DeclaredSingleOutputDataPin(
                    nodeFabric = { NodeArcTan() },
                    type = Type.FLOAT
                )
            )
        )

        /*
         *
         *
         *
         */

        // Локальная переменная
        for (typeVariable in variableTypes) {
            registerBlock(
                NativeBlockType(
                    "localVariable.${typeVariable.key}",
                    DeclaredSingleOutputDataPin(
                        nodeFabric = { NodeLocalVariable(typeVariable.value) },
                        name = "",
                        type = typeVariable.value
                    )
                )
            )
        }

        // Присваивание
        for (typeVariable in variableTypes) {
            registerBlock(NativeBlockType("assignVariable.${typeVariable.key}",
                DeclaredSingleInputFlowPin(
                    nodeFabric = { NodeAssignVariable() }
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeAssignVariable>()
                            .first().dependencies[NodeAssignVariable.REFERENCE] = node
                    },
                    name = "ref",
                    type = typeVariable.value
                ),
                DeclaredSingleInputDataPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeAssignVariable>()
                            .first().dependencies[NodeAssignVariable.VALUE] = node
                    },
                    name = "value",
                    type = typeVariable.value
                ),
                DeclaredSingleOutputFlowPin(
                    handler = { target, node ->
                        target
                            .filterIsInstance<NodeAssignVariable>()
                            .first().nextNode = node
                    },
                )
            )
            )
        }
    }
}