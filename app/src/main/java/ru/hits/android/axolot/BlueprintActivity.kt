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
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.blueprint.project.libs.AxolotNativeLibrary
import ru.hits.android.axolot.compiler.BlueprintCompiler
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.util.*
import ru.hits.android.axolot.view.BlockView
import ru.hits.android.axolot.view.CreatorView
import ru.hits.android.axolot.view.VariableView
import java.util.*

/**
 * Активити создания и редактирования кода нашего языка
 */
class BlueprintActivity : AppCompatActivity() {

    private lateinit var blueprintBinding: ActivityBlueprintBinding

    private lateinit var blockTitleToColor: Map<Regex, Int>

    private val blockViews = mutableListOf<BlockView>()
    private var consoleLines: MutableList<TextView> = mutableListOf()

    private var menuIsVisible = true
    private var consoleIsVisible = false

    val program = AxolotProgram.create()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blueprintBinding = ActivityBlueprintBinding.inflate(layoutInflater)
        setContentView(blueprintBinding.blueprintLayout)

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

        addEventListenersButtons()
        addEventListenerMenu()
        addEventListenerConsole()
        createBlockTypeViews()
    }

    override fun onResume() {
        super.onResume()

        // Задаем зум по-умолчанию.
        Handler(Looper.getMainLooper()).postDelayed({
            blueprintBinding.zoomLayout.zoomTo(blueprintBinding.zoomLayout.getMaxZoom() / 2, false)
        }, 0)
    }

    /**
     * Метод прослушок событий перехода на другое активити или открытие какого-то окна
     */
    private fun addEventListenersButtons() {
        // Запуск программы
        blueprintBinding.ToStartCode.setOnClickListener { startProgram() }

        // Переключение на страницу с настройками
        blueprintBinding.ToPageSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        //перейти в сохранение кода
        blueprintBinding.toSave.setOnClickListener() {
            val intent = Intent(this, SaveActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Метод прослушки менюшки
     */
    private fun addEventListenerMenu() {
        // Скрыть и показать меню
        blueprintBinding.showMenu.setOnClickListener {
            if (menuIsVisible) {
                closeMenu()
            } else {
                closeConsole()
                openMenu()
            }
        }

        // Создание новой переменной и добавление ее в список в менюшке
        blueprintBinding.plusVariable.setOnClickListener {
            createVariableView()
        }

        // Создание новой функции
        blueprintBinding.plusFunction.setOnClickListener {
            val view = CreatorView(this)

            view.typeExpression = false
            view.initComponents()

            blueprintBinding.listFunction.addView(view)
        }

        // Создание нового макроса
        blueprintBinding.plusMacros.setOnClickListener {
            val view = CreatorView(this)

            view.typeExpression = false
            view.initComponents()

            blueprintBinding.listMacros.addView(view)
        }
    }

    /**
     * Метод прослушки консоли
     */
    private fun addEventListenerConsole() {
        //скрыть/показать консоль, по нажатию на иконки консоли в правом нижнем углу
        blueprintBinding.toShowConsole.setOnClickListener() {
            if (consoleIsVisible) {
                closeConsole()
            } else {
                closeMenu()
                openConsole()
            }
        }

        //вывод текста в консоль
        blueprintBinding.toSend.setOnClickListener() {
            val textView = TextView(this)

            textView.text = blueprintBinding.consoleInput.text

            //костыльное задание стилей, пока не разобрался как их подключить из styles.xml
            textView.textSize = 18f
            textView.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_regular))
            textView.setTextColor(Color.BLACK)
            blueprintBinding.linearLayoutConsole.addView(textView)

            //насколько я понимаю, нам будет полезно хранить эти TextView в списке
            consoleLines.add(textView)
        }
    }

    //показывает боковое меню
    private fun openMenu() {
        blueprintBinding.menu.visibility = View.VISIBLE
        blueprintBinding.scrollViewMenu.visibility = View.VISIBLE
        menuIsVisible = true
    }

    //скрывает боковое меню
    private fun closeMenu() {
        blueprintBinding.menu.visibility = View.GONE
        blueprintBinding.scrollViewMenu.visibility = View.GONE
        menuIsVisible = false
    }

    //показывает консоль
    private fun openConsole() {
        blueprintBinding.consoleView.visibility = View.VISIBLE
        blueprintBinding.consoleScrollView.visibility = View.VISIBLE
        blueprintBinding.consoleInput.visibility = View.VISIBLE
        blueprintBinding.toSend.visibility = View.VISIBLE
        consoleIsVisible = true
    }

    //скрывает консоль
    private fun closeConsole() {
        blueprintBinding.consoleView.visibility = View.GONE
        blueprintBinding.consoleScrollView.visibility = View.GONE
        blueprintBinding.consoleInput.visibility = View.GONE
        blueprintBinding.toSend.visibility = View.GONE
        consoleIsVisible = false
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

            blueprintBinding.listBlocks.addView(textView)

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

        // Координаты
        val pan = Vec2f(blueprintBinding.zoomLayout.panX, blueprintBinding.zoomLayout.panY) * -1
        val offset = blueprintBinding.zoomLayout.center / blueprintBinding.zoomLayout.realZoom
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
        blueprintBinding.codeField.addView(blockView)
        blockViews.add(blockView)
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
        blueprintBinding.listVariables.addView(variableView)

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

        // Прослушка кнопки добавления блока
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

    private fun startProgram() {
        val compiler = BlueprintCompiler()
        val interpreter = compiler.prepareInterpreter(program)
        val node = compiler.compile(program)

        try {
            interpreter.execute(node)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
