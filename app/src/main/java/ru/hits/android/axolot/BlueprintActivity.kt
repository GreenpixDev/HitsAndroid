package ru.hits.android.axolot

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.add_node_item.view.*
import kotlinx.android.synthetic.main.block_item.view.*
import kotlinx.android.synthetic.main.variable_creator_item.view.*
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.interpreter.type.Type
import ru.hits.android.axolot.util.Vec2f
import ru.hits.android.axolot.view.*

/**
 * Активити создания и редактирования кода нашего языка
 */
class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding
    private var offset = Vec2f.ZERO

    private var menuIsVisible = true

    private val program = AxolotProgram.create()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueprintBinding.inflate(layoutInflater)
        setContentView(binding.blueprintLayout)

        addEventListeners()
        createMenu()
    }

    override fun onResume() {
        super.onResume()
        // Это самый тупой костыль, но я уже не вывожу (author: Рома)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.zoomLayout.zoomTo(binding.zoomLayout.getMaxZoom(), false)
        }, 0)
    }

    private fun addEventListeners() {
        //скрывание и показ меню
        binding.showMenu.setOnClickListener() {
            if (menuIsVisible) {
                binding.menu.visibility = View.GONE
                menuIsVisible = false
            } else {
                binding.menu.visibility = View.VISIBLE
                menuIsVisible = true
            }
        }

        //переключение на страницу с настройками
        binding.ToPageSettings.setOnClickListener() {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        //создание новой переменной и доавление ее в список в менюшке
        binding.plusVariable.setOnClickListener {
            val view = VariableCreatorView(this)
            binding.listVariables.addView(view)
            createBlockVariable(view, BlockView(this))
        }

        //создание новой функции
        binding.plusFunction.setOnClickListener {
            val view = VariableCreatorView(this)

            binding.listFunction.addView(view)
        }

        //создание нового макроса
        binding.plusMacros.setOnClickListener {

        }
    }

    private fun createMenu() {
        program.blockTypes.values.forEach {
            val nameBlock = getLocalizationName(it.fullName)
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
    private fun createBlock(
        block: BlockView,
        type: BlockType
    ) {

        // Координаты
        block.x = binding.codeField.width / 2f
        block.y = binding.codeField.height / 2f
        block.translationZ = 30f

        when (type.fullName) {
            "native.main" -> {
                block.level1.setBackgroundColor(Color.RED)
                createNode(block)
            }

            "native.branch" -> {
                block.level1.setBackgroundColor(Color.GRAY)

                val inputRow1 = InputRowView(this)
                val outputRow1 = OutputRowView(this)
                val addNode = AddNodeView(this)

                addNode.addNode = false

                inputRow1.description = false
                outputRow1.descriptionText = "True"

                val inputRow2 = InputRowView(this)
                val outputRow2 = OutputRowView(this)

                inputRow2.descriptionText = "Condition"
                outputRow2.descriptionText = "False"

                addBlockOnField(block, inputRow1, outputRow1, addNode)
                addBlockOnField(block, inputRow2, outputRow2, addNode)
            }

            "native.sequence" -> {
                block.level1.setBackgroundColor(Color.GREEN)

                val inputRow = InputRowView(this)
                val outputRow = OutputRowView(this)
                val addNode = AddNodeView(this)

                inputRow.description = false
                outputRow.outputNode = false
                outputRow.description = false

                addBlockOnField(block, inputRow, outputRow, addNode)

                block.btnAddNode.setOnClickListener {
                    //добавление новых node
                }
            }

            "native.print" -> {
                block.level1.setBackgroundColor(Color.BLUE)

                val inputRow1 = InputRowView(this)
                val outputRow1 = OutputRowView(this)
                val addNode = AddNodeView(this)

                addNode.addNode = false

                inputRow1.description = false
                outputRow1.description = false

                val inputRow2 = InputRowView(this)
                val outputRow2 = OutputRowView(this)

                inputRow2.descriptionText = "In string"
                outputRow2.outputNode = false
                outputRow2.description = false

                addBlockOnField(block, inputRow1, outputRow1, addNode)
                addBlockOnField(block, inputRow2, outputRow2, addNode)
            }
        }

        //присваиваем нужный тип блока
        block.typeBlock = type

        //переименовывем блок
        block.title.text = getLocalizationName(type.fullName)

        // Обработка событий
        block.setOnTouchListener(this::onTouch)

        // добавляем готовый блок на поле
        binding.codeField.addView(block)
    }

    private fun addBlockOnField(
        block: BlockView,
        inputRow: InputRowView,
        outputRow: OutputRowView,
        addNode: AddNodeView
    ) {
        val newRow = TableRow(this)

        //инициализируем row
        inputRow.initComponents()
        outputRow.initComponents()
        addNode.initComponents()

        // добавляем 2 row в основной row
        newRow.addView(inputRow)
        newRow.addView(outputRow)

        block.level2.addView(newRow)

        if (addNode.addNode) {
            val newRow2 = TableRow(this)
            val row = TableRow(this)

            val params = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.START
                weight = 3f
            }

            row.layoutParams = params

            newRow2.addView(row)
            newRow2.addView(addNode)

            block.level3.addView(newRow2)

            block.level3.textAddNode.setOnClickListener {
                createNode(block)
            }
        }
    }

    private fun createNode(block: BlockView) {
        val newRow2 = TableRow(this)
        val row = TableRow(this)
        val outputRow = OutputRowView(this)

        outputRow.description = false
        outputRow.initComponents()

//        val node = ImageButton(this)
//        node.setImageResource(R.drawable.button_shape_circle_stroke)

        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = android.view.Gravity.START
            weight = 3f
        }

        row.layoutParams = params

        newRow2.addView(row)
        newRow2.addView(outputRow)

        block.level2.addView(newRow2)
    }


    /**
     * Метод создания блока-переменной на поле
     */
    private fun createBlockVariable(view: VariableCreatorView, newBlock: BlockView) {
        val outputRow = OutputRowView(this)

        // Координаты
        newBlock.x = binding.codeField.width / 2f
        newBlock.y = binding.codeField.height / 2f
        newBlock.translationZ = 30f

        outputRow.description = false
        outputRow.initComponents()

        when (view.getTypeVariable()) {
            "Boolean" -> {
                newBlock.typeVar = Type.BOOLEAN
            }
            "Integer" -> {
                newBlock.typeVar = Type.INT
            }
            "Double" -> {
                newBlock.typeVar = Type.FLOAT
            }
            "String" -> {
                newBlock.typeVar = Type.STRING
            }
        }

        newBlock.level1.addView(outputRow)
        newBlock.title.text = "variable" //default name variable

        // Обработка событий
        newBlock.setOnTouchListener(this::onTouch)

        view.nameVariable.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(title: Editable) {}
            override fun beforeTextChanged(
                title: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(title: CharSequence, start: Int, before: Int, count: Int) {
                newBlock.title.setText(title)
            }
        })

        binding.codeField.addView(newBlock)
    }


    private fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                offset = Vec2f(view.x - event.rawX, view.y - event.rawY)

                // Нужно вызывать, когда кликаешь по вьюшке,
                // чтобы все стандартные события android срабатывали.
                // Короче, нужно использовать, чтобы не получить warning
                view.performClick()
            }
            MotionEvent.ACTION_MOVE -> {
                view.x = event.rawX + offset.x
                view.y = event.rawY + offset.y
            }
        }
        return true
    }

    private fun getLocalizationName(key: String): String {
        return try {
            resources.getString(resources.getIdentifier(key, "string", packageName))
        } catch (e: Resources.NotFoundException) {
            key
        }
    }

}