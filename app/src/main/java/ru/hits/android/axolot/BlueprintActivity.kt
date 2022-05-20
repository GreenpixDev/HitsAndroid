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
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.creator_for_func_item.view.*
import kotlinx.android.synthetic.main.creator_item.view.*
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.VariableGetterBlockType
import ru.hits.android.axolot.blueprint.declaration.VariableSetterBlockType
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.AxolotSource
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
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
    }

    /**
     * Восстановить блоки на экран телефона из объекта [source]
     */
    private fun restoreSource(source: AxolotSource) {
        binding.codeField.removeViews(0, binding.codeField.childCount)

        val blockViews = source.blocks
            .map { addBlock(it, it.type.fullName) }

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
                    createBlock(it, it.fullName)
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
        type: BlockType,
        name: String
    ): BlockView {
        return addBlock(currentSource.createBlock(type), name)
    }

    /**
     * Добавить блок на поле
     */
    private fun addBlock(block: AxolotBlock, name: String): BlockView {
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

        // Задаем цвет заголовку
        for (it in blockTitleToColor) {
            if (it.key matches name) {
                blockView.header.setBackgroundColor(it.value)
                break
            }
        }

        // Переименовываем блок
        blockView.displayName = getLocalizedString(name)

        // Добавляем все пины
        blockView.block.contacts.forEach { blockView.createPinView(it) }

        // Добавляем плюсики для всех vararg пинов
        blockView.block.type.declaredPins.forEach { blockView.createAddPinView(it) }

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
        variableView.name.width = 160
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
                    val newColorName = "colorVariable${type}"

                    blockViews.filter { it.block.type is VariableGetterBlockType }
                        .filter {
                            (it.block.type as VariableGetterBlockType).variableName ==
                                    variableView.variableName
                        }
                        .forEach { blockView ->
                            blockView.header.setBackgroundColor(getThemeColor(newColorName))
                            blockView.pinViews.forEach { it.update() }
                        }
                }
        }

        // Прослушка кнопки GET добавления блока
        variableView.btnGet.setOnClickListener {
            val variableGetter = program.getVariableGetter(variableView.variableName)
            val blockView = createBlock(variableGetter, VariableGetterBlockType.PREFIX_NAME)

            // Цвет заголовка
            val colorName = "colorVariable${variableGetter.variableType}"
            blockView.header.setBackgroundColor(getThemeColor(colorName))

            // Прослушка изменений имени переменной
            variableView.name.addTextChangedListener { title, _, _, _ ->
                blockView.pinViews.forEach { it.displayName = title.toString() }
            }
        }

        // Прослушка кнопки SET добавления блока
        variableView.btnSet.setOnClickListener {
            val variableSetter = program.getVariableSetter(variableView.variableName)
            val blockView = createBlock(variableSetter, VariableSetterBlockType.PREFIX_NAME)

            // Цвет заголовка
            val colorName = "colorVariable${variableSetter.variableType}"
            blockView.header.setBackgroundColor(getThemeColor(colorName))

            // Прослушка изменений имени переменной
            variableView.name.addTextChangedListener { title, _, _, _ ->
                blockView.pinViews.last().let { it.displayName = title.toString() }
            }
        }
    }

    private fun createFunctionView(): FunctionView {
        val functionName = generateName("func") { !program.hasFunction(it) }
        val functionType = program.createFunction(functionName)
        val functionView = FunctionView(this)

        functionView.functionName = functionName
        functionView.name.setText(functionName)
        addCreator(functionView)

        // Прослушка добавлении вызова функции на поле
        functionView.btnGet.setOnClickListener {
            createBlock(functionType, functionName)
        }

        // Прослушка изменений функции
        functionView.btnEdit.setOnClickListener {
            restoreSource(functionType)
        }

        // Прослушка изменений имени переменной
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(title: Editable) {
                if (!program.hasFunction(title.toString())) {
                    program.renameFunction(functionView.functionName, title.toString())
                    functionView.functionName = title.toString()
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
            val paramView = createParameterView(functionView, VariablePlaces.INPUT_PARAMETERS)
            functionType.addInput(paramView.variableName, Type.BOOLEAN)
        }

        // Прослушка добавления результатов функции
        functionView.creator.plusOutputVar.setOnClickListener {
            val paramView = createParameterView(functionView, VariablePlaces.OUTPUT_VARIABLES)
            functionType.addOutput(paramView.variableName, Type.BOOLEAN)
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

        // Прослушка добавлении вызова макроса на поле
        macrosView.btnGet.setOnClickListener {
            createBlock(macrosType, macrosName)
        }

        // Прослушка изменений макроса
        macrosView.btnEdit.setOnClickListener {
            restoreSource(macrosType)
        }

        // Прослушка добавления входных данных макросов
        macrosView.creator.plusInputParam.setOnClickListener {
            val paramView = createParameterView(macrosView, VariablePlaces.INPUT_PARAMETERS)
            macrosType.addInputData(paramView.variableName, Type.BOOLEAN)
        }

        // Прослушка добавления выходных данных макросов
        macrosView.creator.plusOutputVar.setOnClickListener {
            val paramView = createParameterView(macrosView, VariablePlaces.OUTPUT_VARIABLES)
            macrosType.addOutputData(paramView.variableName, Type.BOOLEAN)
        }

        return macrosView
    }

    /**
     * Метод создания новой переменной в функции/макросе
     */
    private fun createParameterView(
        creatorView: CreatorView,
        place: VariablePlaces
    ): VariableView {
        val variableView = VariableView(this)

        //инициализация creatorView
        variableView.edit = false
        variableView.isVar = true

        //дефолтное название
        variableView.variableName = "param"
        variableView.name.setText(variableView.variableName)

        variableView.name.width = 200
        variableView.btnAddDel = true
        variableView.initComponents()

        //проверка куда добавлять
        when (place) {
            VariablePlaces.INPUT_PARAMETERS -> {
                creatorView.listParameters.addView(variableView)
            }

            VariablePlaces.OUTPUT_VARIABLES -> {
                creatorView.creator.listOutputVar.addView(variableView)
            }
        }
        return variableView
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
