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
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.creator_item.*
import kotlinx.android.synthetic.main.creator_item.view.*
import ru.hits.android.axolot.blueprint.FrontendConsoleMock
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.blueprint.project.libs.AxolotNativeLibrary
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.exception.AxolotException
import ru.hits.android.axolot.util.*
import ru.hits.android.axolot.view.BlockView
import ru.hits.android.axolot.view.CreatorView
import java.util.*

/**
 * Активити создания и редактирования кода нашего языка
 */
class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding

    private lateinit var blockTitleToColor: Map<Regex, Int>

    private var menuIsVisible = true
    var consoleIsVisible = true

    val program = AxolotProgram.create()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueprintBinding.inflate(layoutInflater)
        setContentView(binding.blueprintLayout)

        // Палитра цветов
        blockTitleToColor = mapOf(
            Regex("^native\\.main$") to getThemeColor(R.attr.colorBlockHeaderMain),
            Regex("^native\\.getVariable$") to getThemeColor(R.attr.colorBlockHeaderVariable),
            Regex("^function\\..*") to getThemeColor(R.attr.colorBlockHeaderFunction),
            Regex("^macros\\..*") to getThemeColor(R.attr.colorBlockHeaderMacros)
        )

        addEventListeners()
        createBlockTypeViews()

        openMenu()
        binding.consoleView.initConsole(FrontendConsoleMock())
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
            val view = CreatorView(this)

            view.typeExpression = false
            view.initComponents()

            binding.listFunction.addView(view)
        }

        // Создание нового макроса
        binding.plusMacros.setOnClickListener {
            val view = CreatorView(this)

            view.typeExpression = false
            view.initComponents()

            binding.listMacros.addView(view)
        }
    }

    /**
     * Метод создания всех вьюшек  нативных типов блоков
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
                    createBlock(BlockView(this), it)
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
        type: BlockType
    ) {

        // Инициализация
        blockView.block = type.createBlock()

        // Координаты
        val pan = Vec2f(binding.zoomLayout.panX, binding.zoomLayout.panY) * -1
        val offset = binding.zoomLayout.center / binding.zoomLayout.realZoom
        blockView.position = pan + offset

        // Задаем цвет заголовку
        for (it in blockTitleToColor) {
            if (it.key matches type.fullName) {
                blockView.header.setBackgroundColor(it.value)
                break
            }
        }

        // Добавляем все пины
        blockView.block.contacts.forEach { blockView.createPinView(it) }

        // Добавляем плюсики для всех vararg пинов
        type.declaredPins.forEach { blockView.createAddPinView(it) }

        // Переименовывем блок
        blockView.title.text = getLocalizedString(type.fullName)

        // Если это главный блок - указываем это в программе.
        // Если главный блок уже был - кинет ошибку AxolotException
        if (type == AxolotNativeLibrary.BLOCK_MAIN) {
            program.mainBlock = blockView.block
        }

        // Привязываем блок к программе
        program.addBlock(blockView.block)

        // Добавляем готовый блок на поле
        binding.codeField.addView(blockView)
    }

    /**
     * Метод создания новой переменной в меню
     * TODO Рома из будущего, сделай переменные с привязкой к компилятору
     */
    private fun createVariableView() {
        val view = CreatorView(this)

        view.edit = false
        view.initComponents()

        binding.listVariables.addView(view)
        view.btnAdd.setOnClickListener {
            createVariableOnField(view, BlockView(this))
        }
    }

    /**
     * Метод создания блока-переменной на поле
     * TODO Рома из будущего, сделай переменные с привязкой к компилятору
     */
    private fun createVariableOnField(view: CreatorView, blockView: BlockView) {
        // инициализация координатов блока
        initCoordinatesBlock(blockView)

        //имя переменной по дефолту
        blockView.title.text = view.name.text.toString()

        //прослушка изменений имени переменной
        view.name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(title: Editable) {}

            override fun beforeTextChanged(
                title: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(title: CharSequence, start: Int, before: Int, count: Int) {
                blockView.title.setText(title)
            }
        })

        //прослушка изменений типа переменной
        view.type?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                program.variableTypes[type.selectedItem.toString()
                    .lowercase(Locale.getDefault())]
                //?.let { blockView.typeVar = it } TODO ДОДЕЛАТЬ
            }
        }

        binding.codeField.addView(blockView)
    }

    /**
     * Метод инициализации координатов блока
     * TODO Рома из будущего, сделай переменные с привязкой к компилятору
     */
    private fun initCoordinatesBlock(block: BlockView) {
        block.x = binding.codeField.width / 2f
        block.y = binding.codeField.height / 2f
        block.translationZ = 30f
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
