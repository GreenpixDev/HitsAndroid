package ru.hits.android.axolot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivitySaveBinding

class SaveActivity : AppCompatActivity() {
    private lateinit var saveBinding: ActivitySaveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveBinding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(saveBinding.root)

        addEventListeners()
    }

    //функция, которая добавит события к кнопкам
    private fun addEventListeners() {
        saveBinding.goBack.setOnClickListener {
            val intent = Intent(this, BlueprintActivity::class.java)
            startActivity(intent)
        }
    }

}