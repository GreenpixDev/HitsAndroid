package ru.hits.android.axolot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEventListeners()
    }

    private fun addEventListeners() {
        binding.toMainPage.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.startBtn.setOnClickListener {
            startActivity(Intent(this, BlueprintActivity::class.java))
        }

        binding.btnInfo.setOnClickListener() {
            startActivity(Intent(this, InformationActivity::class.java))
        }

        binding.btnSettings.setOnClickListener() {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.toAboutUsPage.setOnClickListener() {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }
    }

}