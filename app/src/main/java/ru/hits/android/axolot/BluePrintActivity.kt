package ru.hits.android.axolot

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_blue_print.view.*
import ru.hits.android.axolot.databinding.ActivityBluePrintBinding

class BluePrintActivity : AppCompatActivity() {
    private lateinit var bluePrintBinding: ActivityBluePrintBinding
    private var menuIsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluePrintBinding = ActivityBluePrintBinding.inflate(layoutInflater)
        setContentView(bluePrintBinding.root)

        addEventListeners()
        createTextView()
    }

    fun addEventListeners() {
        //закрыть/открыть меню
        bluePrintBinding.showMenu.setOnClickListener() {
            if (menuIsVisible) {
                bluePrintBinding.menu.visibility = View.GONE
                menuIsVisible = false
            } else {
                bluePrintBinding.menu.visibility = View.VISIBLE
                menuIsVisible = true
            }
        }

        //перейти в настройки
        bluePrintBinding.imageViewSettings.setOnClickListener() {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        //перейти в сохранение кода
        bluePrintBinding.imageViewSave.setOnClickListener() {
            val intent = Intent(this, SaveActivity::class.java)
            startActivity(intent)
        }

        //добавление новой функции
        bluePrintBinding.scrollViewMenu.linearLayoutFunctionsContainer.newFunction.setOnClickListener() {
            Toast.makeText(applicationContext, "Пока что это затычка", Toast.LENGTH_SHORT).show()
        }
    }


    fun createTextView() {
        var variables = resources.getStringArray(R.array.btnBlocksVariables)
        var cycles = resources.getStringArray(R.array.btnBlocksCycles)
        var conditions = resources.getStringArray(R.array.btnBlocksConditions)

        someFunctionName(variables, 1)
        someFunctionName(cycles, 2)
        someFunctionName(conditions, 3)
    }

    fun someFunctionName(array: Array<String>, textViewType: Int) {
        //textViewType == 1 - variables
        //             == 2 - cycles
        //             == 3 - conditions
        //это нужно как-то красивее сделать, но я не придумал как (как-то убрать textViewType и передавать что-то другое)

        var id = textViewType.toString()
        var textSize = 20f
        var textColor = Color.BLACK
        var textAlign = View.TEXT_ALIGNMENT_CENTER
        var fontFamily = ResourcesCompat.getFont(this, R.font.montserrat_regular)
        var counter = 0

        for (textViewName in array) {
            var currentId: String

            if (counter < 10) {
                currentId = "${id}0${counter}"
            } else {
                currentId = "$id$counter"
            }

            var textView = TextView(this)
            textView.text = textViewName
            textView.setTextColor(textColor)
            textView.textAlignment = textAlign
            textView.textSize = textSize
            textView.setTypeface(fontFamily)
            textView.id = currentId.toInt()

            //это бы как-нибудь красиво переписать
            if (textViewType == 1) {
                bluePrintBinding.linearLayoutVariablesContainer.addView(textView)
            } else if (textViewType == 2) {
                bluePrintBinding.linearLayoutCyclesContainer.addView(textView)
            } else if (textViewType == 3) {
                bluePrintBinding.linearLayoutConditionsContainer.addView(textView)
            } else {
                //что-то не так...
            }

            counter++
        }
    }

}