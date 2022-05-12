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
    private var consoleIsVisible = false
    private var consoleLines: MutableList<TextView> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluePrintBinding = ActivityBluePrintBinding.inflate(layoutInflater)
        setContentView(bluePrintBinding.root)

        closeConsole()
        openMenu()

        addEventListeners()
        createMenuList()
    }

    fun addEventListeners() {
        //скрыть/показать меню, по нажатию на круглую кнопку
        bluePrintBinding.showMenu.setOnClickListener() {
            if (menuIsVisible) {
                closeMenu()
            } else {
                closeConsole()
                openMenu()
            }
        }

        //скрыть/показать консоль, по нажатию на иконки консоли в правом нижнем углу
        bluePrintBinding.imageViewConsole.setOnClickListener() {
            if (consoleIsVisible) {
                closeConsole()
            } else {
                closeMenu()
                openConsole()
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
        bluePrintBinding.menuScrollView.linearLayoutFunctionsContainer.newFunction.setOnClickListener() {
            Toast.makeText(applicationContext, "Пока что это затычка", Toast.LENGTH_SHORT).show()
        }

        //вывод текста в консоль
        bluePrintBinding.imageViewSend.setOnClickListener() {
            var textView = TextView(this)

            textView.text = bluePrintBinding.consoleInput.text

            //костыльное задание стилей, пока не разобрался как их подключить из styles.xml
            textView.textSize = 18f
            textView.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_regular))
            textView.setTextColor(Color.BLACK)
            bluePrintBinding.linearLayoutConsole.addView(textView)

            //насколько я понимаю, нам будет полезно хранить эти TextView в списке
            consoleLines.add(textView)
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
            } else {
                //что-то не так...
            }

            counter++
        }
    }

    //показывает боковое меню
    private fun openMenu() {
        bluePrintBinding.menuView.visibility = View.VISIBLE
        bluePrintBinding.menuScrollView.visibility = View.VISIBLE
        menuIsVisible = true
    }

    //скрывает боковое меню
    private fun closeMenu() {
        bluePrintBinding.menuView.visibility = View.GONE
        bluePrintBinding.menuScrollView.visibility = View.GONE
        menuIsVisible = false
    }

    //показывает консоль
    private fun openConsole() {
        bluePrintBinding.consoleView.visibility = View.VISIBLE
        bluePrintBinding.consoleScrollView.visibility = View.VISIBLE
        bluePrintBinding.consoleInput.visibility = View.VISIBLE
        bluePrintBinding.imageViewSend.visibility = View.VISIBLE
        consoleIsVisible = true
    }

    //скрывает консоль
    private fun closeConsole() {
        bluePrintBinding.consoleView.visibility = View.GONE
        bluePrintBinding.consoleScrollView.visibility = View.GONE
        bluePrintBinding.consoleInput.visibility = View.GONE
        bluePrintBinding.imageViewSend.visibility = View.GONE
        consoleIsVisible = false
    }
}