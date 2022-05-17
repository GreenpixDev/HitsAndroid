package ru.hits.android.axolot

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import ru.hits.android.axolot.databinding.ConsoleViewBinding

class ConsoleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: ConsoleViewBinding

    private var activity: BluePrintActivity = context as BluePrintActivity

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.console_view, this, true)
        binding = ConsoleViewBinding.bind(this)

        addEvents()
    }

    private fun addEvents() {
        //добавление TextView в консоль
        binding.imageViewSend.setOnClickListener() {
            //TODO: inputString - данные, которые нужно положить в консоль
            val inputString = binding.consoleInput.text.toString()
            binding.consoleInput.setText("")

            addTextViewToConsole(inputString)

        }
    }

    //показывает консоль
    fun openConsole() {
        activity.closeMenu()
        binding.consoleView.visibility = View.VISIBLE
        binding.consoleScrollView.visibility = View.VISIBLE
        binding.consoleInput.visibility = View.VISIBLE
        binding.imageViewSend.visibility = View.VISIBLE
        activity.consoleIsVisible = true
    }

    //скрывает консоль
    fun closeConsole() {
        binding.consoleView.visibility = View.GONE
        binding.consoleScrollView.visibility = View.GONE
        binding.consoleInput.visibility = View.GONE
        binding.imageViewSend.visibility = View.GONE
        activity.consoleIsVisible = false
    }

    //создает TextView и добавляяет в консоль
    fun addTextViewToConsole(inputString: String) {
        //TODO: вызвать этот метод, чтобы положить этот текст в консоль
        val textView = TextView(activity)
        textView.text = inputString

        //TODO: подключить стили из styles
        textView.textSize = 18f
        textView.typeface = ResourcesCompat.getFont(activity, R.font.montserrat_regular)
        textView.setTextColor(Color.BLACK)

        binding.linearLayoutConsole.addView(textView)   //добавили TextView
        activity.consoleLines.add(textView)             //добавили в массив со всеми TextView
    }
}
