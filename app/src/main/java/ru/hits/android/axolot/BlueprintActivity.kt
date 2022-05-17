package ru.hits.android.axolot

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import ru.hits.android.axolot.databinding.ActivityBluePrintBinding

class BlueprintActivity : AppCompatActivity() {
    public lateinit var bluePrintBinding: ActivityBluePrintBinding
    public var menuIsVisible = true
    public var consoleIsVisible = false
    public var consoleLines: MutableList<TextView> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluePrintBinding = ActivityBluePrintBinding.inflate(layoutInflater)
        setContentView(bluePrintBinding.root)

        addEventListeners()
        createMenuList()

        openMenu()
        bluePrintBinding.consoleView.closeConsole()
    }

    fun addEventListeners() {
        //скрыть/показать меню, по нажатию на круглую кнопку
        bluePrintBinding.showMenu.setOnClickListener() {
            if (menuIsVisible) {
                closeMenu()
            } else {
                openMenu()
            }
        }

        //скрыть/показать консоль, по нажатию на иконки консоли в правом нижнем углу
        bluePrintBinding.imageViewConsole.setOnClickListener() {
            if (consoleIsVisible) {
                bluePrintBinding.consoleView.closeConsole()
            } else {
                bluePrintBinding.consoleView.openConsole()
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
    }

    //генерация списка в меню из файла с ресурсами
    private fun createMenuList() {
        var variables = resources.getStringArray(R.array.btnBlocksVariables)
        var cycles = resources.getStringArray(R.array.btnBlocksCycles)
        var conditions = resources.getStringArray(R.array.btnBlocksConditions)

        generateMenuListFromArray(variables, 1)
        generateMenuListFromArray(cycles, 2)
        generateMenuListFromArray(conditions, 3)
    }

    private fun generateMenuListFromArray(array: Array<String>, textViewType: Int) {
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
            }
            counter++
        }
    }

    //показывает боковое меню
    public fun openMenu() {
        bluePrintBinding.consoleView.closeConsole()
        bluePrintBinding.menuView.visibility = View.VISIBLE
        bluePrintBinding.menuScrollView.visibility = View.VISIBLE
        menuIsVisible = true
    }

    //скрывает боковое меню
    public fun closeMenu() {
        bluePrintBinding.menuView.visibility = View.GONE
        bluePrintBinding.menuScrollView.visibility = View.GONE
        menuIsVisible = false
    }

}