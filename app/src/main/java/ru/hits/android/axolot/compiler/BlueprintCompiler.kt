package ru.hits.android.axolot.compiler

import ru.hits.android.axolot.blueprint.declaration.*
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
import ru.hits.android.axolot.interpreter.element.InterpretedMacros
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
        // Создаем функции и макросы
        val functions = program.blockTypes.values
            .filterIsInstance<FunctionType>()
            .associateWith { InterpretedFunction() }
        val macros = program.blockTypes.values
            .filterIsInstance<MacrosType>()
            .associateWith { InterpretedMacros() }

        // Компилируем сначала функции и макросы
        program.blockTypes.values
            .filterIsInstance<AxolotSource>()
            .forEach { compileSource(it, functions, macros) }
        program.blockTypes.values
            .filterIsInstance<AxolotSource>()
            .forEach { compileSource(it, functions, macros) }

        // Компилируем саму программу
        val map = compileSource(program, functions, macros)

        // После предыдущего этапа мы получаем полноценный граф узлов, который можно
        // использовать в интерпретаторе. Для этого нужно найти входный узел нашей программы.

        // Получаем входной блок нашей программы
        val main = program.mainBlock
        requireNotNull(main) { "cannot find main block of program" }

        // Получаем из блока входной узел для интерпретатора
        val executable = map[main]?.firstNotNullOfOrNull { it.value }
        require(executable is NodeExecutable?) { "main block of program not executable" }

        return executable
    }

    /**
     * Компилирует исходники [source] в граф узлов [Map<TypedPin, Node>]
     */
    private fun compileSource(
        source: AxolotSource,
        functions: Map<FunctionType, InterpretedFunction>,
        macros: Map<MacrosType, InterpretedMacros>
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

        // Инициализируем узлы функции и макросов
        postCompileFunctions(functions, nodes, adjacent)
        postCompileMacros(macros, nodes, adjacent)

        return adjacent
    }

    /**
     * После компиляции инициализация функций
     */
    private fun postCompileFunctions(
        functions: Map<FunctionType, InterpretedFunction>,
        nodes: Map<AxolotBlock, Map<DeclaredAutonomicPin, Node>>,
        adjacent: Map<AxolotBlock, MutableMap<TypedPin, Node>>
    ) {
        // Инициализация всех узлов Invoke и Returned
        nodes
            .mapKeys { it.key.type }
            .mapValues { it.value.values }
            .filterIsInstance<FunctionType, Collection<Node>>()
            .forEach { nodeList ->
                val node = nodeList.value.filterIsInstance<NodeFunctionInvoke>().first()
                node.function = functions[nodeList.key]!!

                nodeList.value.filterIsInstance<NodeFunctionReturned>().forEach {
                    it.nodeInvoke = node
                }
            }

        // Инициализация всех узлов End
        nodes
            .mapKeys { it.key.type }
            .mapValues { it.value.values }
            .filterIsInstance<FunctionEndType, Collection<Node>>()
            .forEach { nodeList ->
                val node = nodeList.value.filterIsInstance<NodeFunctionEnd>().first()
                node.function = functions[nodeList.key.functionType]!!
            }

        // Инициализация всех узлов Begin
        adjacent
            .filterKeys { it.type is FunctionBeginType }
            .mapValues { it.value.values }
            .forEach { e ->
                val executable = adjacent[e.key]?.firstNotNullOfOrNull { it.value }
                require(executable is NodeExecutable?) {
                    "begin block of ${e.key.type.fullName} not executable"
                }
                functions[(e.key.type as FunctionBeginType).functionType]?.inputExecutable =
                    executable
            }
    }

    /**
     * После компиляции инициализация макросов
     */
    private fun postCompileMacros(
        macros: Map<MacrosType, InterpretedMacros>,
        nodes: Map<AxolotBlock, Map<DeclaredAutonomicPin, Node>>,
        adjacent: Map<AxolotBlock, MutableMap<TypedPin, Node>>
    ) {

    }
}