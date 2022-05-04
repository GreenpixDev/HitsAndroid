package ru.hits.android.axolot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.settingsLayout)

        binding.goToMain.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.backToGame.setOnClickListener() {
            startActivity(Intent(this, BlueprintActivity::class.java))
        }

        binding.exit.setOnClickListener() {
            this.finishAffinity()
        }
    }
}