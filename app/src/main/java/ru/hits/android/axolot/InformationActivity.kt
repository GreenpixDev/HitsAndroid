package ru.hits.android.axolot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.hits.android.axolot.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.informationLayout)

        binding.backToMain.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.leftArrow.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.start.setOnClickListener() {
            startActivity(Intent(this, BlueprintActivity::class.java))
        }

        binding.rightArrow.setOnClickListener() {
            startActivity(Intent(this, BlueprintActivity::class.java))
        }
    }
}