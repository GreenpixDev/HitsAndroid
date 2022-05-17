package ru.hits.android.axolot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        addEventListeners()
    }

    private fun addEventListeners() {
        mainBinding.aboutUsTextView.setOnClickListener() {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }

        mainBinding.button.setOnClickListener() {
            val intent = Intent(this, BlueprintActivity::class.java)
            startActivity(intent)
        }

        mainBinding.informationIcon.setOnClickListener() {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }

        mainBinding.settingsIcon.setOnClickListener() {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}