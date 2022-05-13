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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.block_item.view.*
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.blueprint.project.AxolotProgram
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.util.Vec2f
import ru.hits.android.axolot.view.BlockRowView
import ru.hits.android.axolot.view.BlockView

/**
 * Активити редактора исходного кода нашего языка
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
                addBlockOnField(BlockView(this), BlockRowView(this), it)
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

    /**
     * Метод создания блока на поле
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun addBlockOnField(
        block: BlockView,
        rowBlock: BlockRowView,
        typeBlock: BlockType
    ) {

        // Координаты
        block.x = binding.codeField.width / 2f
        block.y = binding.codeField.height / 2f
        block.translationZ = 30f

        rowBlock.inputNode = false
        rowBlock.description = false
        rowBlock.expression = false

//        when (typeView) {
//            "Boolean" -> {
//                rowBlock.typeBlock = Type.BOOLEAN
//            }
//            "Integer" -> {
//                rowBlock.typeBlock = Type.INT
//            }
//            "Double" -> {
//                rowBlock.typeBlock = Type.FLOAT
//            }
//            "String" -> {
//                rowBlock.typeBlock = Type.STRING
//            }
//        }
        //инициализируем и добавляем row в основной блок
        rowBlock.initComponents()
        rowBlock.setInputType()
        block.level1.addView(rowBlock)

        // Обработка событий
        block.setOnTouchListener(this::onTouch)

        // Добавляем блок на поле и List
        block.title.text = getLocalizationName(typeBlock.fullName)
        binding.codeField.addView(block)
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
