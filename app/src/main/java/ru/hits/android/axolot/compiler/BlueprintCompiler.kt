package ru.hits.android.axolot.compiler

import ru.hits.android.axolot.blueprint.declaration.FunctionBeginType
import ru.hits.android.axolot.blueprint.declaration.FunctionEndType
import ru.hits.android.axolot.blueprint.declaration.FunctionType
import ru.hits.android.axolot.blueprint.declaration.VariableGetterBlockType
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredAutonomicPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.AxolotSource
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.TypedPin
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.interpreter.BlueprintInterpreter
import ru.hits.android.axolot.interpreter.Interpreter
import ru.hits.android.axolot.interpreter.element.InterpretedFunction
import ru.hits.android.axolot.interpreter.node.Node
import ru.hits.android.axolot.interpreter.node.NodeConstant
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionEnd
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionInvoke
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.interpreter.scope.GlobalScope
import ru.hits.android.axolot.util.filterIsInstance
import ru.hits.android.axolot.util.filterValuesNotNull
import ru.hits.android.axolot.util.putMap

class BlueprintCompiler : Compiler {

    override fun prepareInterpreter(program: AxolotProgram, console: Console): Interpreter {
        val scope = GlobalScope()
        val interpreter = BlueprintInterpreter(scope, console)

        program.blockTypes.values
            .filterIsInstance<VariableGetterBlockType>()
            .forEach {
                scope.declareVariable(it.variableName, it.variableType)
            }

        return interpreter
    }

    override fun compile(program: AxolotProgram): NodeExecutable? {
        val functions = program.blockTypes.values
            .filterIsInstance<FunctionType>()
            .associateWith { InterpretedFunction() }

        // Компилируем сначала макросы и функции
        program.blockTypes.values
            .filterIsInstance<AxolotSource>()
            .forEach { compileSource(it, functions) }

        // Компилируем саму программу
        val map = compileSource(program, functions)

        // После предыдущего этапа мы получаем полноценный граф узлов, который можно
        // использовать в интерпретаторе. Для этого нужно найти входный узел нашей программы.

        // Получаем входной блок нашей программы
        val main = program.mainBlock
        requireNotNull(main) { "cannot find main block of program" }

        // Получаем из блока входной узел для интерпретатора
        val executable = map[main]?.firstNotNullOf { it.value }
        require(executable is NodeExecutable?) { "fatal error" }

        return executable
    }

    /**
     * Компилирует исходники [source] в граф узлов [Map<TypedPin, Node>]
     */
    private fun compileSource(
        source: AxolotSource,
        functions: Map<FunctionType, InterpretedFunction>
    ): Map<AxolotBlock, Map<TypedPin, Node>> {
        // У каждого блока есть пины, которые требуют зависимости (автономный пин).
        // Пробегаемся по всем блокам, находим такие пины и создаём для них узлы.
        // Этот словарь содержит следующие данные:
        // Map<"Блок", Map<"Автономный пин", "Узел интерпретатора">>
        val nodes: Map<AxolotBlock, Map<DeclaredAutonomicPin, Node>> = source.blocks
            .associateWith { block ->
                block.type.declaredPins
                    .filterIsInstance<DeclaredAutonomicPin>()
                    .associateWith { it.createNode(block) }
            }
            .filter { it.value.isNotEmpty() }

        // Этот словарь содержит следующие данные:
        // Map<"Блок", Map<"Смежный пин", "Узел интерпретатора">>
        val adjacent: MutableMap<AxolotBlock, MutableMap<TypedPin, Node>> = hashMapOf()

        // Помимо этого нам нужно получить сами зависимости для узлов.
        // Пробегаемся по всем блокам, находим смежные пины и узлы для них,
        // найденные на первом этапе.
        source.blocks
            .associateWith { block ->
                block.contacts
                    .filterIsInstance<PinToOne>()
                    .associateWith { it.adjacent }
                    .filterValuesNotNull()
                    .filterIsInstance<TypedPin, TypedPin>()
                    .filterValues { it.type is DeclaredAutonomicPin }
                    .mapValues { nodes[it.value.owner]?.get(it.value.type as DeclaredAutonomicPin) }
                    .filterValuesNotNull()
            }
            .onEach { adjacent.putMap(it) }

        // Также пин может не ссылаться на другой пин, но при этом иметь значение (константное).
        // Этот вариант мы обрабатываем иначе.
        // Пробегаемся по всем блокам, находим смежные пины-константы и создаем узел константы.
        source.blocks
            .associateWith { block ->
                block.contacts
                    .filterIsInstance<PinToOne>()
                    .associateWith { it.adjacent }
                    .filterValuesNotNull()
                    .filterIsInstance<TypedPin, ConstantPin>()
                    .mapValues { NodeConstant.of(it.value.constant) }
            }
            .onEach { adjacent.putMap(it) }

        // А теперь, когда мы создали все узлы для интерпретатора, мы можем
        // проинициализировать их (связать их, добавить зависимости и следующие узлы).
        adjacent.forEach { block ->
            block.value.forEach { pin ->
                val map = nodes[block.key]
                var collection = emptyList<Node>()
                if (map != null) {
                    collection = map.map { it.value }
                }
                pin.key.type.handle(collection, pin.value)
            }
        }

        // Инициализируем узлы функции
        nodes
            .mapKeys { it.key.type }
            .mapValues { it.value.values }
            .filterIsInstance<FunctionType, Collection<Node>>()
            .forEach { nodes ->
                val node = nodes.value.filterIsInstance<NodeFunctionInvoke>().first()
                node.function = functions[nodes.key]!!

                nodes.value.filterIsInstance<NodeFunctionReturned>().forEach {
                    it.nodeInvoke = node
                }
            }

        nodes
            .mapKeys { it.key.type }
            .mapValues { it.value.values }
            .filterIsInstance<FunctionEndType, Collection<Node>>()
            .forEach { nodes ->
                val node = nodes.value.filterIsInstance<NodeFunctionEnd>().first()
                node.function = functions[nodes.key.functionType]!!
            }

        adjacent
            .filterKeys { it.type is FunctionBeginType }
            .mapValues { it.value.values }
            .forEach { e ->
                val executable = adjacent[e.key]?.firstNotNullOf { it.value }
                require(executable is NodeExecutable?) { "fatal error" }
                functions[(e.key.type as FunctionBeginType).functionType]?.inputExecutable =
                    executable
            }

        return adjacent
    }
}