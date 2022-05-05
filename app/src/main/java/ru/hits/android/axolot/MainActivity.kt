package ru.hits.android.axolot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.hits.android.axolot.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
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