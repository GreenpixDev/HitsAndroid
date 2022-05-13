package ru.hits.android.axolot

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.add_node_item.view.*
import kotlinx.android.synthetic.main.block_item.view.*
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

        binding.plusVariable.setOnClickListener {
            val view = VariableCreatorView(this)
            binding.listVariables.addView(view)
            createBlockVariable(view, BlockView(this))
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

    private fun createUserFunctionAndMacros() {
        binding.plusFunction.setOnClickListener {

        }

        binding.plusMacros.setOnClickListener {

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
                val inputRow = InputRowView(this)
                val outputRow = OutputRowView(this)
                val addNode = AddNodeView(this)

                inputRow.inputNode = false
                inputRow.description = false
                outputRow.description = false
                addNode.addNode = false

                addBlockOnField(block, inputRow, outputRow, addNode)
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
//        val paramsInput = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT
//        ).apply {
//            weight = 2F
//            gravity = Gravity.LEFT
//        }
//
//        val paramsOutput = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT
//        ). apply {
//            gravity = Gravity.RIGHT
//        }

//        inputRow.layoutParams = paramsInput
//        outputRow.layoutParams = paramsOutput

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

            newRow2.addView(TableRow(this))
            newRow2.addView(addNode)

            block.level2.addView(newRow2)
        }
    }

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

        newBlock.level2.addView(outputRow)
        newBlock.title.text = view.getNameVariable()

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


//    fun addTextView(stringArray : Array<String>) {
//        for (textViewName in stringArray){
//            val textView = TextView(this)
//            textView.setTextAppearance(R.style.TextViewStyleForMenu)
//            textView.text = textViewName.toString()
//
//            if (stringArray.toString() == "variables") {
//                binding.scrollViewMenu.linearLayoutMenu.linearLayoutVariablesContainer.addView(textView)
//            } else if (stringArray.toString() == "cycles") {
//                binding.scrollViewMenu.linearLayoutMenu.linearLayoutCyclesContainer.addView(textView)
//            } else if (stringArray.toString() == "conditions") {
//                binding.scrollViewMenu.linearLayoutMenu.linearLayoutConditionsContainer.addView(textView)
//            }
//        }
//    }

    private fun getLocalizationName(key: String): String {
        return try {
            resources.getString(resources.getIdentifier(key, "string", packageName))
        } catch (e: Resources.NotFoundException) {
            key
        }
    }

}
//    private fun someFunctionName(array: Array<String>, typeBlock: String) {
//        val textSize = 20f
//        val textColor = Color.BLACK
//        val textAlign = View.TEXT_ALIGNMENT_CENTER
//        val fontFamily = ResourcesCompat.getFont(this, R.font.montserrat_regular)
//
//        for (typeView in array) {
//            val textView = TextView(this)
//            textView.text = typeView.toString()
//            textView.setTextColor(textColor)
//            textView.textAlignment = textAlign
//            textView.textSize = textSize
//            textView.typeface = fontFamily
//
//            when (typeBlock) {
//                "Variables" -> {
//                    binding.linearLayoutVariablesContainer.addView(textView)
//                    textView.setOnClickListener() {
//                        addBlockOnField(BlockView(this), BlockRowView(this), typeView, typeBlock)
//                    }
//                }
//                "Cycles" -> {
//                    binding.linearLayoutCyclesContainer.addView(textView)
//                    textView.setOnClickListener() {
//                        addBlockOnField(BlockView(this), BlockRowView(this), typeView, typeBlock)
//                    }
//                }
//                "Conditions" -> {
//                    binding.linearLayoutConditionsContainer.addView(textView)
//                    textView.setOnClickListener() {
//                        addBlockOnField(BlockView(this), BlockRowView(this), typeView, typeBlock)
//                    }
//                }
//            }
//        }
//    }
