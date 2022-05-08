package ru.hits.android.axolot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.hits.android.axolot.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private lateinit var aboutUsBinding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aboutUsBinding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(aboutUsBinding.root)

        addEventListeners()
    }

    private fun addEventListeners() {
        aboutUsBinding.mainTextView.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        aboutUsBinding.informationIcon.setOnClickListener() {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }

        aboutUsBinding.settingsIcon.setOnClickListener() {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}