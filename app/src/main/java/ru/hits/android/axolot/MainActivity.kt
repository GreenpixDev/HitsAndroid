package ru.hits.android.axolot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBegin(view: View){
        val intent = Intent(this, BluePrintActivity::class.java)
        startActivity(intent)
    }

    fun onAboutUs(view: View){
        val intent = Intent(this, AboutUsActivity::class.java)
        startActivity(intent)
    }

    fun onInformation(view: View){
        val intent = Intent(this, InformationActivity::class.java)
        startActivity(intent)
    }

    fun onSettings(view: View){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}