package ru.hits.android.axolot

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_blueprint.*
import kotlinx.android.synthetic.main.creator_for_func_item.view.*
import kotlinx.android.synthetic.main.creator_item.view.*
import ru.hits.android.axolot.blueprint.declaration.*
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.AxolotSource
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.blueprint.element.pin.impl.InputDataPin
import ru.hits.android.axolot.blueprint.element.pin.impl.OutputDataPin
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.compiler.BlueprintCompiler
import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.interpreter.Interpreter
import ru.hits.android.axolot.interpreter.node.NodeExecutable
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.util.*
import ru.hits.android.axolot.view.*
import java.util.*

/**
 * Активити создания и редактирования кода нашего языка
 */
class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding

    private lateinit var blockTitleToColor: Map<Regex, Int>

    private val blockViews = mutableListOf<BlockView>()

    private var menuIsVisible = true
    var consoleIsVisible = true

    private val program = AxolotProgram.create()
    val console = Console {
        Handler(Looper.getMainLooper()).post {
            it.invoke()
        }
    }

    /**
     * для понимания куда добавлять variablesView
     */
    enum class VariablePlaces {
        INPUT_PARAMETERS,
        OUTPUT_VARIABLES
    }

    var currentSource: AxolotSource = program

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueprintBinding.inflate(layoutInflater)
        setContentView(binding.blueprintLayout)

        // Палитра цветов
        blockTitleToColor = mapOf(
            Regex("^native\\.main$") to getThemeColor(R.attr.colorBlockHeaderMain),
            Regex("^native\\..+\\.boolean.*") to getThemeColor(R.attr.colorVariableBoolean),
            Regex("^native\\..+\\.int.*") to getThemeColor(R.attr.colorVariableInt),
            Regex("^native\\..+\\.float.*") to getThemeColor(R.attr.colorVariableFloat),
            Regex("^native\\..+\\.string.*") to getThemeColor(R.attr.colorVariableString),
            Regex("^function\\..*") to getThemeColor(R.attr.colorBlockHeaderFunction),
            Regex("^macros\\..*") to getThemeColor(R.attr.colorBlockHeaderMacros),
        )

        addEventListeners()
        createBlockTypeViews()

        openMenu()
        binding.consoleView.initConsole(console)
    }

    override fun onResume() {
        super.onResume()

        // Задаем зум по-умолчанию.
        Handler(Looper.getMainLooper()).post {
            binding.zoomLayout.zoomTo(binding.zoomLayout.getMaxZoom() / 2, false)
            restoreSource(program)
        }
    }

    /**
     * Добавляем все прослушки событий
     */
    private fun addEventListeners() {
        // Скрывание и показ меню
        binding.showMenu.setOnClickListener {
            if (menuIsVisible) {
                closeMenu()
            } else {
                openMenu()
            }
        }

        //открыть/закрыть консоль
        binding.showConsole.setOnClickListener {
            if (consoleIsVisible) {
                binding.consoleView.closeConsole()
            } else {
                binding.consoleView.openConsole()
            }
        }

        // Запуск программы
        binding.ToStartCode.setOnClickListener { startProgram() }

        // Переключение на страницу с настройками
        binding.ToPageSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Создание новой переменной и добавление ее в список в менюшке
        binding.plusVariable.setOnClickListener { createVariableView() }

        // Создание новой функции
        binding.plusFunction.setOnClickListener { createFunctionView() }

        // Создание нового макроса
        binding.plusMacros.setOnClickListener { createMacrosView() }

        // Возвращение в main
        binding.goToMain.setOnClickListener { restoreSource(program) }

        // Добавление return блока для функций
        binding.returnBlock.setOnClickListener {
            if (currentSource is FunctionType) {
                val type = (currentSource as FunctionType).endType
                addBlock(currentSource.createBlock(type))
            }
        }
    }

    /**
     * Восстановить блоки на экран телефона из объекта [source]
     */
    private fun restoreSource(source: AxolotSource) {
        binding.codeField.removeViews(0, binding.codeField.childCount)

        val blockViews = source.blocks
            .map { addBlock(it) }
            .onEach { it.update() }

        val pinViews = blockViews
            .flatMap { it.pinViews }
            .associateBy { it.pin }

        pinViews.keys
            .filterIsInstance<PinToOne>()
            .onEach { pinViews[it]?.restoreConstant() }
            .filter { it.adjacent != null && it.adjacent !is ConstantPin }
            .forEach {
                Handler(Looper.getMainLooper()).post {
                    pinViews[it]?.restoreEdge(pinViews[it.adjacent!!]!!)
                }
            }

        currentSource = source
    }

    /**
     * Метод создания всех вьюшек нативных типов блоков
     */
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    private fun createBlockTypeViews() {
        program.blockTypes.values.forEach {
            val nameBlock = getLocalizedString(it.fullName)
            val typeBlock = Button(this)

            typeBlock.text = nameBlock
            typeBlock.setTextColor(Color.WHITE)
            typeBlock.setBackgroundColor(R.color.bg_btn_menu)
            typeBlock.textAlignment = View.TEXT_ALIGNMENT_CENTER
            typeBlock.textSize = 15f
            typeBlock.background = resources.getDrawable(R.drawable.border_style)
            typeBlock.typeface = ResourcesCompat.getFont(this, R.font.montserrat_light)

            binding.listBlocks.addView(typeBlock)

            typeBlock.setOnClickListener { _ ->
                try {
                    createBlock(it)
                } catch (e: AxolotException) {
                    // nothing
                }
            }
        }
    }

    /**
     * Метод создания блока на поле
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun createBlock(
        type: BlockType
    ): BlockView {
        return addBlock(currentSource.createBlock(type))
    }

    /**
     * Добавить блок на поле
     */
    private fun addBlock(block: AxolotBlock): BlockView {
        val blockView = BlockView(this)
        blockView.block = block

        if (block.position == null) {
            // Координаты
            val pan = Vec2f(binding.zoomLayout.panX, binding.zoomLayout.panY) * -1
            val offset = binding.zoomLayout.center / binding.zoomLayout.realZoom
            blockView.position = pan + offset
        } else {
            blockView.position = block.position!!
        }

        // Добавляем все пины
        block.contacts.forEach { blockView.createPinView(it) }

        // Добавляем плюсики для всех vararg пинов
        block.type.declaredPins.forEach { blockView.createAddPinView(it) }

        blockView.update()

        // Добавляем готовый блок на поле
        binding.codeField.addView(blockView)
        blockViews.add(blockView)
        return blockView
    }

    /**
     * Метод создания атрибутов и выходных переменных для функций и макросов
     */
    private fun addCreator(view: CreatorView) {
        view.creator.addView(CreatorForFunctionView(this))
        view.typeExpression = false
        view.initComponents()
        view.addViewMenu()
    }

    /**
     * Метод создания новой переменной в меню
     */
    private fun createVariableView() {
        val variableView = VariableView(this)

        //инициализация creatorView
        variableView.edit = false
        variableView.isVar = true

        //дефолтное название
        variableView.variableName = generateName("var") { !program.hasVariable(it) }
        variableView.name.setText(variableView.variableName)

        program.createVariable(variableView.variableName)

        // Добавляем в меню
        variableView.name.width = 100
        variableView.initComponents()
        variableView.addViewMenu()

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!program.hasVariable(title.toString())) {
                    program.renameVariable(variableView.variableName, title.toString())
                    variableView.variableName = title.toString()
                    updateVariable(variableView.variableName)
                    return
                }
                variableView.name.removeTextChangedListener(this)
                variableView.name.setText(variableView.variableName)
                variableView.name.addTextChangedListener(this)
                Toast.makeText(
                    this@BlueprintActivity,
                    "Такая переменная уже есть!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        variableView.name.addTextChangedListener(listener)

        // Прослушка изменений типа переменной
        variableView.typeVariable.addItemSelectedListener { parent, _, _, _ ->
            program.variableTypes[parent.selectedItem.toString()
                .lowercase(Locale.getDefault())]
                ?.let { type ->
                    program.retypeVariable(variableView.variableName, type)
                    updateVariable(variableView.variableName)
                }
        }

        // Прослушка кнопки GET добавления блока
        variableView.btnGet.setOnClickListener {
            val variableGetter = program.getVariableGetter(variableView.variableName)
            createBlock(variableGetter)
        }

        // Прослушка кнопки SET добавления блока
        variableView.btnSet.setOnClickListener {
            val variableSetter = program.getVariableSetter(variableView.variableName)
            createBlock(variableSetter)
        }
    }

    private fun createFunctionView(): FunctionView {
        val functionName = generateName("func") { !program.hasFunction(it) }
        val functionType = program.createFunction(functionName)
        val functionView = FunctionView(this)

        functionView.functionName = functionName
        functionView.name.setText(functionName)
        addCreator(functionView)

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!program.hasFunction(title.toString())) {
                    program.renameFunction(functionView.functionName, title.toString())
                    functionView.functionName = title.toString()
                    updateFunction(functionType)
                    return
                }
                functionView.name.removeTextChangedListener(this)
                functionView.name.setText(functionView.functionName)
                functionView.name.addTextChangedListener(this)
                Toast.makeText(
                    this@BlueprintActivity,
                    "Такая функция уже есть!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        functionView.name.addTextChangedListener(listener)

        // Прослушка добавления параметров функции
        functionView.creator.plusInputParam.setOnClickListener {
            val paramView = createFunctionInputView(functionType, functionView)
            functionType.addInput(paramView.variableName, Type.BOOLEAN)
            updateFunction(functionType)
        }

        // Прослушка добавления результатов функции
        functionView.creator.plusOutputVar.setOnClickListener {
            val paramView = createFunctionOutputView(functionType, functionView)
            functionType.addOutput(paramView.variableName, Type.BOOLEAN)
            updateFunction(functionType)
        }

        // Прослушка добавлении вызова функции на поле
        functionView.btnGet.setOnClickListener { createBlock(functionType) }

        // Прослушка изменений функции
        functionView.btnEdit.setOnClickListener {
            restoreSource(functionType)
        }

        return functionView
    }

    private fun createMacrosView(): MacrosView {
        val macrosName = generateName("macros") { !program.hasMacros(it) }
        val macrosType = program.createMacros(macrosName)
        val macrosView = MacrosView(this)

        macrosView.macrosName = macrosName
        macrosView.name.setText(macrosName)
        addCreator(macrosView)

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!program.hasFunction(title.toString())) {
                    program.renameMacros(macrosView.macrosName, title.toString())
                    macrosView.macrosName = title.toString()

                    findBlockView<MacrosType>()
                        .filter { it.block.type == macrosType }
                        .forEach { it.update() }
                    return
                }
                macrosView.name.removeTextChangedListener(this)
                macrosView.name.setText(macrosView.macrosName)
                macrosView.name.addTextChangedListener(this)
                Toast.makeText(
                    this@BlueprintActivity,
                    "Такой макрос уже есть!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        macrosView.name.addTextChangedListener(listener)

        // Прослушка добавления входных данных макросов
        macrosView.creator.plusInputParam.setOnClickListener {
            val paramView = createMacrosInputView(macrosType, macrosView)
            macrosType.addInputData(paramView.variableName, Type.BOOLEAN)
            updateMacros(macrosType)
        }

        // Прослушка добавления выходных данных макросов
        macrosView.creator.plusOutputVar.setOnClickListener {
            val paramView = createMacrosOutputView(macrosType, macrosView)
            macrosType.addOutputData(paramView.variableName, Type.BOOLEAN)
            updateMacros(macrosType)
        }

        // Прослушка добавлении вызова макроса на поле
        macrosView.btnGet.setOnClickListener { createBlock(macrosType) }

        // Прослушка изменений макроса
        macrosView.btnEdit.setOnClickListener { restoreSource(macrosType) }

        return macrosView
    }

    /**
     * Метод создания новой переменной в функции/макросе
     */
    private fun createParameterView(
        name: String,
        creatorView: CreatorView,
        place: VariablePlaces
    ): VariableView {
        val parameterView = VariableView(this)

        //инициализация creatorView
        parameterView.edit = false
        parameterView.isVar = true

        //дефолтное название
        parameterView.variableName = name
        parameterView.name.setText(parameterView.variableName)

        parameterView.name.width = 200
        parameterView.btnAddDel = true
        parameterView.initComponents()

        //проверка куда добавлять
        when (place) {
            VariablePlaces.INPUT_PARAMETERS -> {
                creatorView.listParameters.addView(parameterView)
            }

            VariablePlaces.OUTPUT_VARIABLES -> {
                creatorView.creator.listOutputVar.addView(parameterView)
            }
        }
        return parameterView
    }

    /**
     * Метод создания входного параметра в функции
     */
    private fun createFunctionInputView(
        function: FunctionType,
        creatorView: CreatorView
    ): VariableView {
        val parameterView = createParameterView(generateName("param") {
            !function.hasInput(it)
        }, creatorView, VariablePlaces.INPUT_PARAMETERS)

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!function.hasInput(title.toString())) {
                    function.renameInput(parameterView.variableName, title.toString())

                    program.findAllBlockByType(function)
                        .flatMap { it.contacts }
                        .filterIsInstance<InputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }
                    program.findAllBlockByType(function.beginType)
                        .flatMap { it.contacts }
                        .filterIsInstance<OutputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }

                    parameterView.variableName = title.toString()
                    updateFunction(function)
                    return
                }
                parameterView.name.removeTextChangedListener(this)
                parameterView.name.setText(parameterView.variableName)
                parameterView.name.addTextChangedListener(this)
                Toast.makeText(
                    this@BlueprintActivity,
                    "Такой параметр уже есть!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        parameterView.name.addTextChangedListener(listener)

        // Прослушка изменений типа переменной
        parameterView.typeVariable.addItemSelectedListener { parent, _, _, _ ->
            program.variableTypes[parent.selectedItem.toString()
                .lowercase(Locale.getDefault())]
                ?.let { type ->
                    function.retypeInput(parameterView.variableName, type)
                    updateFunction(function)
                }
        }
        return parameterView
    }

    /**
     * Метод создания выходного параметра в функции
     */
    private fun createFunctionOutputView(
        function: FunctionType,
        creatorView: CreatorView
    ): VariableView {
        val parameterView = createParameterView(generateName("param") {
            !function.hasOutput(it)
        }, creatorView, VariablePlaces.INPUT_PARAMETERS)

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!function.hasOutput(title.toString())) {
                    function.renameOutput(parameterView.variableName, title.toString())

                    program.findAllBlockByType(function)
                        .flatMap { it.contacts }
                        .filterIsInstance<OutputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }
                    program.findAllBlockByType(function.endType)
                        .flatMap { it.contacts }
                        .filterIsInstance<InputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }

                    parameterView.variableName = title.toString()
                    updateFunction(function)
                    return
                }
                parameterView.name.removeTextChangedListener(this)
                parameterView.name.setText(parameterView.variableName)
                parameterView.name.addTextChangedListener(this)
                Toast.makeText(
                    this@BlueprintActivity,
                    "Такой параметр уже есть!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        parameterView.name.addTextChangedListener(listener)

        // Прослушка изменений типа переменной
        parameterView.typeVariable.addItemSelectedListener { parent, _, _, _ ->
            program.variableTypes[parent.selectedItem.toString()
                .lowercase(Locale.getDefault())]
                ?.let { type ->
                    function.retypeOutput(parameterView.variableName, type)
                    updateFunction(function)
                }
        }
        return parameterView
    }

    /**
     * Метод создания входного параметра в макроса
     */
    private fun createMacrosInputView(
        macros: MacrosType,
        creatorView: CreatorView
    ): VariableView {
        val parameterView = createParameterView(generateName("param") {
            !macros.hasInput(it)
        }, creatorView, VariablePlaces.INPUT_PARAMETERS)

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!macros.hasInput(title.toString())) {
                    macros.renameInput(parameterView.variableName, title.toString())

                    program.findAllBlockByType(macros)
                        .flatMap { it.contacts }
                        .filterIsInstance<InputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }
                    program.findAllBlockByType(macros.beginType)
                        .flatMap { it.contacts }
                        .filterIsInstance<OutputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }

                    parameterView.variableName = title.toString()
                    updateMacros(macros)
                    return
                }
                parameterView.name.removeTextChangedListener(this)
                parameterView.name.setText(parameterView.variableName)
                parameterView.name.addTextChangedListener(this)
                Toast.makeText(
                    this@BlueprintActivity,
                    "Такой параметр уже есть!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        parameterView.name.addTextChangedListener(listener)

        // Прослушка изменений типа переменной
        parameterView.typeVariable.addItemSelectedListener { parent, _, _, _ ->
            program.variableTypes[parent.selectedItem.toString()
                .lowercase(Locale.getDefault())]
                ?.let { type ->
                    macros.retypeInput(parameterView.variableName, type)
                    updateMacros(macros)
                }
        }
        return parameterView
    }

    /**
     * Метод создания выходного параметра в макроса
     */
    private fun createMacrosOutputView(
        macros: MacrosType,
        creatorView: CreatorView
    ): VariableView {
        val parameterView = createParameterView(generateName("param") {
            !macros.hasOutput(it)
        }, creatorView, VariablePlaces.INPUT_PARAMETERS)

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!macros.hasOutput(title.toString())) {
                    macros.renameOutput(parameterView.variableName, title.toString())

                    program.findAllBlockByType(macros)
                        .flatMap { it.contacts }
                        .filterIsInstance<OutputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }
                    program.findAllBlockByType(macros.endType)
                        .flatMap { it.contacts }
                        .filterIsInstance<InputDataPin>()
                        .filter { it.name == parameterView.variableName }
                        .forEach { it.name = title.toString() }

                    parameterView.variableName = title.toString()
                    updateMacros(macros)
                    return
                }
                parameterView.name.removeTextChangedListener(this)
                parameterView.name.setText(parameterView.variableName)
                parameterView.name.addTextChangedListener(this)
                Toast.makeText(
                    this@BlueprintActivity,
                    "Такой параметр уже есть!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        parameterView.name.addTextChangedListener(listener)

        // Прослушка изменений типа переменной
        parameterView.typeVariable.addItemSelectedListener { parent, _, _, _ ->
            program.variableTypes[parent.selectedItem.toString()
                .lowercase(Locale.getDefault())]
                ?.let { type ->
                    macros.retypeOutput(parameterView.variableName, type)
                    updateMacros(macros)
                }
        }
        return parameterView
    }

    /**
     * Обновить переменную с названием [variableName]
     */
    private fun updateVariable(variableName: String) {
        findBlockView<VariableGetterBlockType>()
            .filter { (it.block.type as VariableGetterBlockType).variableName == variableName }
            .forEach { it.update() }

        findBlockView<VariableSetterBlockType>()
            .filter { (it.block.type as VariableSetterBlockType).variableName == variableName }
            .forEach { it.update() }
    }

    /**
     * Обновить все блоки функции [function]
     */
    private fun updateFunction(function: FunctionType) {
        findBlockView<FunctionType>()
            .filter { it.block.type == function }
            .forEach { it.update() }
        findBlockView<FunctionBeginType>()
            .filter { it.block.type == function.beginType }
            .forEach { it.update() }
        findBlockView<FunctionEndType>()
            .filter { it.block.type == function.endType }
            .forEach { it.update() }
    }

    /**
     * Обновить все блоки макроса [macros]
     */
    private fun updateMacros(macros: MacrosType) {
        findBlockView<MacrosType>()
            .filter { it.block.type == macros }
            .forEach { it.update() }
        findBlockView<MacrosBeginType>()
            .filter { it.block.type == macros.beginType }
            .forEach { it.update() }
        findBlockView<MacrosEndType>()
            .filter { it.block.type == macros.endType }
            .forEach { it.update() }
    }

    /**
     * Запуск нашей БОМБЕЗНОЙ программы
     */
    private fun startProgram() {
        // Запускаем это всё в отдельном потоке, потому что
        // в нашей программе присутствуют прерывания
        Thread {
            val compiler = BlueprintCompiler()
            val interpreter: Interpreter
            val node: NodeExecutable?

            try {
                interpreter = compiler.prepareInterpreter(program, console)
                node = compiler.compile(program)
            } catch (e: Throwable) {
                e.printStackTrace()
                console.sendStringToUser("Ошибка компиляции")
                return@Thread
            }

            try {
                interpreter.execute(node)
            } catch (e: Throwable) {
                e.printStackTrace()
                console.sendStringToUser("Ошибка исполнения")
            }
        }.start()
    }

    private inline fun <reified T> findBlockView(): List<BlockView> {
        return (0 until codeField.childCount)
            .map { codeField.getChildAt(it) }
            .filterIsInstance<BlockView>()
            .filter { it.block.type is T }
    }

    /**
     * Показывает боковое меню, при этом скрывает консоль.
     */
    private fun openMenu() {
        binding.consoleView.closeConsole()
        binding.menu.visibility = View.VISIBLE
        menuIsVisible = true
    }

    /**
     * Сокрытие бокового меню
     */
    fun closeMenu() {
        binding.menu.visibility = View.GONE
        menuIsVisible = false
    }

}