package ru.hits.android.axolot

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_blue_print.*

class BluePrintActivity : AppCompatActivity() {
    var menuIsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blue_print)
    }

    fun onClickShowMenu(view: View) {
        if (menuIsVisible) {
            menu.visibility = View.GONE
            menuIsVisible = false
        } else {
            menu.visibility = View.VISIBLE
            menuIsVisible = true
        }
    }

    fun onSettings(view: View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}