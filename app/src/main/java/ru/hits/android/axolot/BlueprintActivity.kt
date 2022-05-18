package ru.hits.android.axolot

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.creator_item.view.*
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.VariableGetterBlockType
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.AxolotSource
import ru.hits.android.axolot.blueprint.element.pin.PinToOne
import ru.hits.android.axolot.blueprint.element.pin.impl.ConstantPin
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.blueprint.project.libs.AxolotNativeLibrary
import ru.hits.android.axolot.compiler.BlueprintCompiler
import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.util.*
import ru.hits.android.axolot.view.BlockView
import ru.hits.android.axolot.view.CreatorView
import ru.hits.android.axolot.view.FunctionView
import ru.hits.android.axolot.view.VariableView
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
    val console = Console()

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
        binding.plusMacros.setOnClickListener {
            val view = CreatorView(this)

            view.typeExpression = false
            view.initComponents()

            binding.listMacros.addView(view)
        }

        // Обратно в Main программы
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
    private fun createBlockTypeViews() {
        program.blockTypes.values.forEach {
            val nameBlock = getLocalizedString(it.fullName)
            val textView = TextView(this)

            textView.text = nameBlock
            textView.setTextColor(Color.BLACK)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView.textSize = 20f
            textView.typeface = ResourcesCompat.getFont(this, R.font.montserrat_regular)

            binding.listBlocks.addView(textView)

            textView.setOnClickListener { _ ->
                try {
                    createBlock(BlockView(this), it, it.fullName)
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
        blockView: BlockView,
        type: BlockType,
        name: String
    ) {
        // Инициализация
        blockView.block = type.createBlock()

        // Если это главный блок - указываем это в программе.
        // Если главный блок уже был - кинет ошибку AxolotException
        if (blockView.block.type == AxolotNativeLibrary.BLOCK_MAIN) {
            program.mainBlock = blockView.block
        }

        // Привязываем блок к программе
        currentSource.addBlock(blockView.block)

        // Добавляем на поле
        addBlock(blockView.block, name)
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
     * Метод создания новой переменной в меню
     * TODO Рома из будущего, сделай переменные с привязкой к компилятору
     */
    private fun createVariableView() {
        val variableView = VariableView(this)

        variableView.edit = false
        variableView.initComponents()
        variableView.variableName = "variable"

        program.createVariable(variableView.variableName)
        binding.listVariables.addView(variableView)

        // Прослушка изменений имени переменной
        variableView.name.addTextChangedListener { title, _, _, _ ->
            program.renameVariable(variableView.variableName, title.toString())
            variableView.variableName = title.toString()
        }

        // Прослушка изменений типа переменной
        variableView.type.addItemSelectedListener { parent, _, _, _ ->
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

        // Прослушка кнопка добавления блока
        variableView.btnAdd.setOnClickListener {
            val variableGetter = program.getVariableGetter(variableView.variableName)
            val blockView = BlockView(this)
            createBlock(blockView, variableGetter, VariableGetterBlockType.PREFIX_NAME)

            // Цвет заголовка
            val colorName = "colorVariable${variableGetter.variableType}"
            blockView.header.setBackgroundColor(getThemeColor(colorName))

            // Прослушка изменений имени переменной
            variableView.name.addTextChangedListener { title, _, _, _ ->
                blockView.pinViews.forEach { it.displayName = title.toString() }
            }
        }
    }

    private fun createFunctionView() {
        val functionName = "function"
        val functionType = program.createFunction(functionName)
        val functionView = FunctionView(this)

        functionView.functionName = functionName
        binding.listFunction.addView(functionView)

        // Прослушка изменений типа переменной
        functionView.btnEdit.setOnClickListener {
            restoreSource(functionType)
        }
    }

    private fun startProgram() {
        val compiler = BlueprintCompiler()
        val interpreter = compiler.prepareInterpreter(program, console)
        val node = compiler.compile(program)

        try {
            interpreter.execute(node)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
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
