package ru.hits.android.axolot

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_blue_print.*
import ru.hits.android.axolot.databinding.ActivityBluePrintBinding

class BluePrintActivity : AppCompatActivity() {
    private lateinit var bluePrintBinding: ActivityBluePrintBinding
    private var menuIsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluePrintBinding = ActivityBluePrintBinding.inflate(layoutInflater)
        setContentView(bluePrintBinding.root)
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

    fun onSettings(view: View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}