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
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionParameter
import ru.hits.android.axolot.interpreter.node.function.custom.NodeFunctionReturned
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosDependency
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosInput
import ru.hits.android.axolot.interpreter.node.macros.NodeMacrosOutput
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
        // Создаем функции
        val functions = program.blockTypes.values
            .filterIsInstance<FunctionType>()
            .associateWith { InterpretedFunction() }

        // Компилируем сначала функции
        program.blockTypes.values
            .filterIsInstance<FunctionType>()
            .forEach { compileFunction(it, functions) }

        // Компилируем саму программу
        val adjacent = compileSource(program, functions).second

        // После предыдущего этапа мы получаем полноценный граф узлов, который можно
        // использовать в интерпретаторе. Для этого нужно найти входный узел нашей программы.

        // Получаем входной блок нашей программы
        val main = program.mainBlock
        requireNotNull(main) { "cannot find main block of program" }

        // Получаем из блока входной узел для интерпретатора
        val executable = adjacent[main]?.firstNotNullOfOrNull { it.value }
        require(executable is NodeExecutable?) { "main block of program not executable" }

        return executable
    }

    /**
     * Компилирует исходники [source] в граф узлов [Map<TypedPin, Node>]
     */
    private fun compileSource(
        source: AxolotSource,
        functions: Map<FunctionType, InterpretedFunction>
    ): Pair<Map<AxolotBlock, Map<DeclaredAutonomicPin, Node>>,
            Map<AxolotBlock, Map<TypedPin, Node>>
            > {
        // Ищем все вызовы макросов и создаем для них InterpretedMacros
        val macros = source.blocks
            .filter { it.type is MacrosType }
            .associateWith { InterpretedMacros() }

        /*
         * ОСНОВНАЯ ЧАСТЬ
         */

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

        /*
         * ФУНКЦИИ
         */

        // Инициализация всех узлов Invoke и Returned для функций
        nodes
            .filterKeys { it.type is FunctionType }
            .forEach { block ->
                val node = block.value.values.filterIsInstance<NodeFunctionInvoke>().first()
                node.function = functions[block.key.type as FunctionType]!!

                block.value.values.filterIsInstance<NodeFunctionReturned>().forEach {
                    it.nodeInvoke = node
                }
            }

        /*
         * МАКРОСЫ
         */

        // Инициализация первой половины узлов InterpretedMacros
        nodes.filterKeys { it.type is MacrosType }.forEach { e ->
            e.value.filterIsInstance<DeclaredAutonomicPin, NodeMacrosInput>().forEach {
                macros[e.key]?.inputExecutable?.set(it.key.name, it.value)
            }
            e.value.filterIsInstance<DeclaredAutonomicPin, NodeMacrosDependency>().forEach {
                macros[e.key]?.output?.set(it.key.name, it.value)
            }
        }

        // Так как макросы мы встраваем в наш код, то мы находим все блоки вызова макросов
        // и заменяем их на то, что внутри макросов.
        // Поэтому каждый вызов макроса компилируем отдельно (функции мы компилировали 1 раз для каждой)
        macros.forEach { compileMacros(it.value, it.key.type as MacrosType, functions) }

        // Так как инициализация обычных узлов не работает для макросов,
        // мы инициализируем их тут.
        adjacent
            .filter { it.key.type is MacrosType }
            .forEach { block ->
                block.value.forEach { pin ->
                    val macro = macros[block.key]!!
                    val collection = mutableListOf<Node>()
                    collection.addAll(macro.input.values)
                    collection.addAll(macro.outputExecutable.values)
                    pin.key.type.handle(collection, pin.value)
                }
            }

        return nodes to adjacent
    }

    /**
     * Компиляция функций
     */
    private fun compileFunction(
        source: FunctionType,
        functions: Map<FunctionType, InterpretedFunction>
    ) {
        val result = compileSource(source, functions)
        val interpretedFunction = functions[source]!!
        val nodes = result.first
        val adjacent = result.second

        // Инициализация всех узлов Begin
        adjacent
            .filterKeys { it.type is FunctionBeginType }
            .mapValues { it.value.values }
            .forEach { e ->
                val executable = adjacent[e.key]?.firstNotNullOfOrNull { it.value }
                require(executable is NodeExecutable?) {
                    "begin block of ${e.key.type.fullName} not executable"
                }
                interpretedFunction.inputExecutable = executable
            }
        nodes.forEach { block ->
            block.value
                .filterIsInstance<DeclaredAutonomicPin, NodeFunctionParameter>()
                .forEach {
                    interpretedFunction.input[it.key.name] = it.value
                }
        }

        // Инициализация всех узлов End
        nodes.forEach { block ->
            val node = block.value.values
                .filterIsInstance<NodeFunctionEnd>()
                .firstOrNull()
            node?.function = interpretedFunction
        }
    }

    /**
     * Компиляция макросов
     */
    private fun compileMacros(
        macros: InterpretedMacros,
        source: MacrosType,
        functions: Map<FunctionType, InterpretedFunction>
    ) {
        val result = compileSource(source, functions)
        val nodes = result.first
        val adjacent = result.second

        // Инициализация второй половины узлов InterpretedMacros
        nodes
            .filter { it.key.type is MacrosBeginType || it.key.type is MacrosEndType }
            .forEach { block ->
                block.value.filterIsInstance<DeclaredAutonomicPin, NodeMacrosDependency>().forEach {
                    macros.input[it.key.name] = it.value
                }
                block.value.filterIsInstance<DeclaredAutonomicPin, NodeMacrosOutput>().forEach {
                    macros.outputExecutable[it.key.name] = it.value
                }
            }

        // Так как инициализация обычных узлов не работает для макросов,
        // мы инициализируем их тут.
        adjacent
            .filter { it.key.type is MacrosBeginType }
            .forEach { block ->
                block.value.forEach { pin ->
                    val collection = macros.inputExecutable.values
                    pin.key.type.handle(collection, pin.value)
                }
            }
        adjacent
            .filter { it.key.type is MacrosEndType }
            .forEach { block ->
                block.value.forEach { pin ->
                    val collection = macros.output.values
                    pin.key.type.handle(collection, pin.value)
                }
            }
    }
}