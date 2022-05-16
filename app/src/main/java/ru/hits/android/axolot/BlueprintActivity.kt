package ru.hits.android.axolot

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.creator_item.*
import kotlinx.android.synthetic.main.creator_item.view.*
import kotlinx.android.synthetic.main.pin_item.view.*
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredVarargInputDataPin
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredVarargOutputFlowPin
import ru.hits.android.axolot.blueprint.element.AxolotBlock
import ru.hits.android.axolot.blueprint.element.pin.Pin
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.util.Vec2f
import ru.hits.android.axolot.util.getLocalizedString
import ru.hits.android.axolot.util.getThemeColor
import ru.hits.android.axolot.view.AddNodeView
import ru.hits.android.axolot.view.BlockView
import ru.hits.android.axolot.view.CreatorView
import ru.hits.android.axolot.view.PinView
import java.util.*

/**
 * Активити создания и редактирования кода нашего языка
 */
class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding

    private lateinit var blockTitleToColor: Map<Regex, Int>

    private var offset = Vec2f.ZERO

    private var menuIsVisible = true

    private val program = AxolotProgram.create()

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
    }

    override fun onResume() {
        super.onResume()
        // Это самый тупой костыль, но я уже не вывожу (author: Рома)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.zoomLayout.zoomTo(binding.zoomLayout.getMaxZoom() / 2, false)
        }, 0)
    }

    private fun addEventListeners() {
        // Скрывание и показ меню
        binding.showMenu.setOnClickListener {
            if (menuIsVisible) {
                binding.menu.visibility = View.GONE
                menuIsVisible = false
            } else {
                binding.menu.visibility = View.VISIBLE
                menuIsVisible = true
            }
        }

        // Переключение на страницу с настройками
        binding.ToPageSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Создание новой переменной и доавление ее в список в менюшке
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
                createBlock(BlockView(this), it)
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

        // Объект блока
        val block = type.createBlock()

        // Координаты
        blockView.x = binding.codeField.width / 2f
        blockView.y = binding.codeField.height / 2f
        blockView.translationZ = 30f

        // Задаем цвет заголовку
        for (it in blockTitleToColor) {
            if (it.key matches type.fullName) {
                blockView.header.setBackgroundColor(it.value)
                break
            }
        }

        // Добавляем все пины
        block.contacts.forEach { createPinView(it, blockView) }

        // Добавляем плюсики для всех vararg пинов
        type.declaredPins.forEach { createAddPinView(it, block, blockView) }

        //присваиваем нужный тип блока
        blockView.typeBlock = type

        //переименовывем блок
        blockView.title.text = getLocalizedString(type.fullName)

        // Обработка событий
        blockView.setOnTouchListener(this::onTouch)

        // добавляем готовый блок на поле
        binding.codeField.addView(blockView)
    }

    /**
     * Метод создания новой переменной в меню
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
     * Метод добавления вьюшки пина (или узла/булавочки/круглешочка)
     */
    private fun createPinView(
        pin: Pin,
        blockView: BlockView,
        indexGetter: (Int) -> Int = { it }
    ): PinView {
        val rowView = PinView(pin, this)
        rowView.description.text = pin.name
        rowView.addViewTo(blockView, indexGetter)
        return rowView
    }

    /**
     * Метод добавление вьюшки кнопочки "Add Pin"
     */
    private fun createAddPinView(
        declaredPin: DeclaredPin,
        block: AxolotBlock,
        blockView: BlockView
    ) {
        when (declaredPin) {
            is DeclaredVarargInputDataPin -> {
                val view = AddNodeView(this)
                view.initComponents()
                view.setOnClickListener { _ ->
                    declaredPin.createPin(block).forEach { pin ->
                        block.contacts.add(pin)
                        createPinView(pin, blockView) { it - 1 }
                    }
                }
                blockView.body.linearLayoutLeft.addView(view)
            }
            is DeclaredVarargOutputFlowPin -> {
                val view = AddNodeView(this)
                view.initComponents()
                view.setOnClickListener { _ ->
                    declaredPin.createPin(block).forEach { pin ->
                        block.contacts.add(pin)
                        createPinView(pin, blockView) { it - 1 }
                    }
                }
                blockView.body.linearLayoutRight.addView(view)
            }
        }
    }

    /**
     * Метод создания блока-переменной на поле
     */
    private fun createVariableOnField(view: CreatorView, blockView: BlockView) {
        // инициализация координатов блока
        initCoordinatesBlock(blockView)

        //имя переменной по дефолту
        blockView.title.text = view.name.getText().toString()

        // Обработка передвигания блока
        blockView.setOnTouchListener(this::onTouch)

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
                    ?.let { blockView.typeVar = it }
            }
        }

        binding.codeField.addView(blockView)
    }

    /**
     * Метод передвижения вьюшек с учетом зума
     */
    private fun onTouch(view: View, event: MotionEvent): Boolean {
        val zoom = binding.zoomLayout.realZoom
        val pan = Vec2f(binding.zoomLayout.panX, binding.zoomLayout.panY) * -1

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                offset = (Vec2f(view.x, view.y) - pan) * zoom - Vec2f(event.rawX, event.rawY)
            }
            MotionEvent.ACTION_MOVE -> {
                view.x = (event.rawX + offset.x) / zoom + pan.x
                view.y = (event.rawY + offset.y) / zoom + pan.y
            }
        }
        return true
    }

    /**
     * Метод инициализации координатов блока
     */
    private fun initCoordinatesBlock(block: BlockView) {
        block.x = binding.codeField.width / 2f
        block.y = binding.codeField.height / 2f
        block.translationZ = 30f

    }
}
