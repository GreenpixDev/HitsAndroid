package ru.hits.android.axolot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.aboutUsLayout)

        addEventListener()
    }

    private fun addEventListener() {
        binding.toMainPage.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.toAboutUsPage.setOnClickListener() {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

        binding.informationIcon.setOnClickListener() {
            startActivity(Intent(this, InformationActivity::class.java))
        }

        binding.settingsIcon.setOnClickListener() {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}