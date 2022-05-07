package ru.hits.android.axolot

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_blue_print.*
import kotlinx.android.synthetic.main.activity_blue_print.view.*
import ru.hits.android.axolot.databinding.ActivityBluePrintBinding

class BluePrintActivity : AppCompatActivity() {
    private lateinit var bluePrintBinding: ActivityBluePrintBinding
    private var menuIsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluePrintBinding = ActivityBluePrintBinding.inflate(layoutInflater)
        setContentView(bluePrintBinding.root)

        createTextView()

        bluePrintBinding.scrollViewMenu.linearLayoutFunctionsContainer.newFunction.setOnClickListener(){
            Toast.makeText(applicationContext, "Пока что это затычка", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickShowMenu(view: View) {
        if (menuIsVisible) {
            bluePrintBinding.menu.visibility = View.GONE
            menuIsVisible = false
        } else {
            bluePrintBinding.menu.visibility = View.VISIBLE
            menuIsVisible = true
        }
    }

    fun onSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun createTextView() {
        var textSize = 20f
        var textColor = Color.BLACK
        var textAlign = View.TEXT_ALIGNMENT_CENTER

        var variables = resources.getStringArray(R.array.btnBlocksVariables)
        var cycles = resources.getStringArray(R.array.btnBlocksCycles)
        var conditions = resources.getStringArray(R.array.btnBlocksConditions)

        for (textViewName in variables){
            var textView = TextView(this)
            textView.text = textViewName.toString()
            textView.setTextColor(textColor)
            textView.textAlignment = textAlign
            textView.textSize = textSize
            bluePrintBinding.scrollViewMenu.linearLayoutMenu.linearLayoutVariablesContainer.addView(textView)
        }

        for (textViewName in cycles){
            var textView = TextView(this)
            textView.text = textViewName.toString()
            textView.setTextColor(textColor)
            textView.textAlignment = textAlign
            textView.textSize = textSize
            bluePrintBinding.scrollViewMenu.linearLayoutMenu.linearLayoutCyclesContainer.addView(textView)
        }

        for (textViewName in conditions){
            var textView = TextView(this)
            textView.text = textViewName.toString()
            textView.setTextColor(textColor)
            textView.textAlignment = textAlign
            textView.textSize = textSize
            bluePrintBinding.scrollViewMenu.linearLayoutMenu.linearLayoutConditionsContainer.addView(textView)
        }
    }


}