package ru.hits.android.axolot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.hits.android.axolot.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {
    private lateinit var informationBinding: ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        informationBinding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(informationBinding.root)

        addEventListeners()
    }

    private fun addEventListeners() {
        informationBinding.backToMain.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        informationBinding.start.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}