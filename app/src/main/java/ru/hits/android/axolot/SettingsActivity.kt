package ru.hits.android.axolot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)
        addEventListeners()
    }

    private fun addEventListeners() {
        settingsBinding.backToMain.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settingsBinding.start.setOnClickListener(){
            val intent = Intent(this, BlueprintActivity::class.java)
            startActivity(intent)
        }
    }
}