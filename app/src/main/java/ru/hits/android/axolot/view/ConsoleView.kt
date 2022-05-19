package ru.hits.android.axolot.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import ru.hits.android.axolot.BlueprintActivity
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.FrontendConsole
import ru.hits.android.axolot.databinding.ConsoleViewBinding

class ConsoleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var consoleLines: MutableList<TextView> = mutableListOf()
    private val binding = ConsoleViewBinding.inflate(LayoutInflater.from(context), this)
    private val activity: BlueprintActivity
        get() = context as BlueprintActivity

    private lateinit var console: FrontendConsole

    init {
        addEvents()
    }

    fun initConsole(console: FrontendConsole) {
        this.console = console
        this.console.setOnReceive {
            addTextViewToConsole(it)
        }
    }

    /**
     * Добавляем листенер (пока что только на кнопку отправки)
     */
    private fun addEvents() {
        //добавление TextView в консоль
        binding.imageViewSend.setOnClickListener {
            val inputString = binding.consoleInput.text.toString()
            binding.consoleInput.setText("")

            addTextViewToConsole(inputString)
            console.send(inputString)
        }
    }

    /**
     * Метод, который открывает консоль, при этом скрывая боковое меню
     */
    fun openConsole() {
        activity.closeMenu()
        visibility = View.VISIBLE
        activity.consoleIsVisible = true
    }

    /**
     * Скрывает консоль, не трогая боковое меню
     */
    fun closeConsole() {
        visibility = View.GONE
        activity.consoleIsVisible = false
    }

    /**
     * Создание TextView, добавление к нему значение [inputString],
     * добавление этого TextView в консоль, сохранение этого TextView в массив
     * (мало ли нам с ним придется еще работать, так что оставим способ с ним взаимодействовать)
     */
    private fun addTextViewToConsole(inputString: String) {
        val textView = TextView(activity)
        textView.text = inputString

        textView.textSize = 18f
        textView.typeface = ResourcesCompat.getFont(activity, R.font.montserrat_regular)
        textView.setTextColor(Color.BLACK)

        binding.linearLayoutConsole.addView(textView)   //добавили TextView
        consoleLines.add(textView)                      //добавили в массив со всеми TextView
    }
}
