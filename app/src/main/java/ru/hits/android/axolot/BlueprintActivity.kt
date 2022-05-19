package ru.hits.android.axolot

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.creator_for_func_item.view.*
import kotlinx.android.synthetic.main.creator_item.view.*
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.VariableGetterBlockType
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.blueprint.project.libs.AxolotNativeLibrary
import ru.hits.android.axolot.compiler.BlueprintCompiler
import ru.hits.android.axolot.console.Console
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.util.*
import ru.hits.android.axolot.view.BlockView
import ru.hits.android.axolot.view.CreatorForFunctionView
import ru.hits.android.axolot.view.CreatorView
import ru.hits.android.axolot.view.VariableView
import java.util.*

/**
 * Активити создания и редактирования кода нашего языка
 */
class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding

    private lateinit var blockTitleToColor: Map<Regex, Int>

    private val blockViews = mutableListOf<BlockView>()
    private var consoleLines: MutableList<TextView> = mutableListOf()

    private var menuIsVisible = true
    var consoleIsVisible = true

    val program = AxolotProgram.create()
    val console = Console()

    /**
     * для понимания куда добавлять variablesView
     */
    enum class VariablePlaces {
        INPUT_PARAMETERS,
        OUTPUT_VARIABLES
    }

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
        Handler(Looper.getMainLooper()).postDelayed({
            binding.zoomLayout.zoomTo(binding.zoomLayout.getMaxZoom() / 2, false)
        }, 0)
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
        binding.plusVariable.setOnClickListener {
            createVariableView()
        }

        // Создание новой функции
        binding.plusFunction.setOnClickListener {
            addCreatorForFuncAndMacros(isFunc = true)
        }

        // Создание нового макроса
        binding.plusMacros.setOnClickListener {
            addCreatorForFuncAndMacros(isFunc = false)
        }
    }

    /**
     * Метод создания атрибутов и выходных переменных для функций и макросов
     */
    private fun addCreatorForFuncAndMacros(isFunc: Boolean) {
        val view = CreatorView(this)

        view.creator.addView(CreatorForFunctionView(this))
        view.typeExpression = false
        view.initComponents()

        if (isFunc) binding.listFunction.addView(view)
        if (!isFunc) binding.listMacros.addView(view)

        view.creator.plusInputParam.setOnClickListener {
            createVariableView(view, VariablePlaces.INPUT_PARAMETERS)
        }

        view.creator.plusOutputVar.setOnClickListener {
            createVariableView(view, VariablePlaces.OUTPUT_VARIABLES)
        }
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

        // Координаты
        val pan = Vec2f(binding.zoomLayout.panX, binding.zoomLayout.panY) * -1
        val offset = binding.zoomLayout.center / binding.zoomLayout.realZoom
        blockView.position = pan + offset

        // Задаем цвет заголовку
        for (it in blockTitleToColor) {
            if (it.key matches name) {
                blockView.header.setBackgroundColor(it.value)
                break
            }
        }

        // Добавляем все пины
        blockView.block.contacts.forEach { blockView.createPinView(it) }

        // Добавляем плюсики для всех vararg пинов
        type.declaredPins.forEach { blockView.createAddPinView(it) }

        // Переименовываем блок
        blockView.displayName = getLocalizedString(name)

        // Если это главный блок - указываем это в программе.
        // Если главный блок уже был - кинет ошибку AxolotException
        if (type == AxolotNativeLibrary.BLOCK_MAIN) {
            program.mainBlock = blockView.block
        }

        // Привязываем блок к программе
        program.addBlock(blockView.block)

        // Добавляем готовый блок на поле
        binding.codeField.addView(blockView)
        blockViews.add(blockView)
    }

    /**
     * Метод создания новой переменной в меню
     * TODO Рома из будущего, сделай переменные с привязкой к компилятору
     */
    private fun createVariableView(
        creatorView: CreatorView? = null,
        place: VariablePlaces? = null
    ) {
        val variableView = VariableView(this)

        //инициализация creatorView
        variableView.edit = false
        variableView.isVar = true

        //дефолтное название
        variableView.variableName = "variable"
        program.createVariable(variableView.variableName)

        //проверка куда добавлять
        if (creatorView != null) {
            variableView.name.width = 200
            variableView.btnAddDel = true
            variableView.initComponents()

            when (place) {
                VariablePlaces.INPUT_PARAMETERS -> {
                    creatorView.listParameters.addView(variableView)
                }

                VariablePlaces.OUTPUT_VARIABLES -> {
                    creatorView.creator.listOutputVar.addView(variableView)
                }

                else -> throw IllegalStateException("Что-то пошло не так")
            }
        } else {
            variableView.name.width = 160
            variableView.initComponents()
            binding.listVariables.addView(variableView)
        }

        // Прослушка изменений имени переменной
        variableView.name.addTextChangedListener { title, _, _, _ ->
            program.renameVariable(variableView.variableName, title.toString())
            variableView.variableName = title.toString()
        }

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

        // Прослушка кнопки SET добавления блока
        variableView.btnSet.setOnClickListener {
            //TODO: сделать добавление блоков SET
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
