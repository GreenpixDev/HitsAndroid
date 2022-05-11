package ru.hits.android.axolot

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.block_item.view.*
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.interpreter.type.Type
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
    private var blocksList = mutableListOf<View>()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueprintBinding.inflate(layoutInflater)
        setContentView(binding.blueprintLayout)

        addEventListeners()
        createTextView()
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

    private fun createTextView() {
        val variables = resources.getStringArray(R.array.btnBlocksVariables)
        val cycles = resources.getStringArray(R.array.btnBlocksCycles)
        val conditions = resources.getStringArray(R.array.btnBlocksConditions)

        someFunctionName(variables, 1)
        someFunctionName(cycles, 2)
        someFunctionName(conditions, 3)
    }

    private fun someFunctionName(array: Array<String>, textViewType: Int) {
        //textViewType == 1 - variables
        //             == 2 - cycles
        //             == 3 - conditions
        //это нужно как-то красивее сделать, но я не придумал как (как-то убрать textViewType и передавать что-то другое)

        val textSize = 20f
        val textColor = Color.BLACK
        val textAlign = View.TEXT_ALIGNMENT_CENTER
        val fontFamily = ResourcesCompat.getFont(this, R.font.montserrat_regular)

        for (typeView in array) {

            val textView = TextView(this)
            textView.text = typeView.toString()
            textView.setTextColor(textColor)
            textView.textAlignment = textAlign
            textView.textSize = textSize
            textView.typeface = fontFamily


            when (textViewType) {
                1 -> {
                    binding.linearLayoutVariablesContainer.addView(textView)
                    textView.setOnClickListener() {
                        addBlockOnField(BlockView(this), BlockRowView(this), typeView)
                    }
                }
                2 -> {
                    binding.linearLayoutCyclesContainer.addView(textView)
                    textView.setOnClickListener() {
                        addBlockOnField(BlockView(this), BlockRowView(this), typeView)
                    }
                }
                3 -> {
                    binding.linearLayoutConditionsContainer.addView(textView)
                    textView.setOnClickListener() {
                        addBlockOnField(BlockView(this), BlockRowView(this), typeView)
                    }
                }

            }

        }
    }

    /**
     * Метод создания блока на поле
     */
    private fun addBlockOnField(view: View, rowView: BlockRowView, typeView: String) {
        //TODO: подправить генерирацию блоков нужного размера

        // Координаты
        view.x = binding.codeField.width / 2f
        view.y = binding.codeField.height / 2f
        view.translationZ = 30f

        when(typeView) {
            "Integer" -> {
                //TODO: сгенерировать ноды и все остальное для блока
                rowView.typeBlock = Type.INT
                rowView.inputNode = false
                rowView.descriptionField = false
                rowView.expressionField = false

                view.level1.addView(rowView)
            }
            "Double" -> {
                //TODO: сгенерировать ноды и все остальное для блока
                view.level1.addView(rowView)
            }
            "Boolean" -> {
                //TODO: сгенерировать ноды и все остальное для блока
                view.level1.addView(rowView)
            }
            "String" -> {
                //TODO: сгенерировать ноды и все остальное для блока
                view.level1.addView(rowView)
            }
        }

        // Обработка событий
        view.setOnTouchListener(this::onTouch)

        // Добавляем блок на поле и List
        binding.codeField.addView(view)
        blocksList.add(view)
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

}
