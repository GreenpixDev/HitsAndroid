package ru.hits.android.axolot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {

    private lateinit var informationBinding: ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        informationBinding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(informationBinding.informationLayout)

        addEventListeners()
    }

    private fun addEventListeners() {
        informationBinding.backToMain.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        informationBinding.leftArrow.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        informationBinding.rightArrow.setOnClickListener() {
            startActivity(Intent(this, BlueprintActivity::class.java))
        }

        informationBinding.start.setOnClickListener() {
            startActivity(Intent(this, BlueprintActivity::class.java))
        }
    }
}