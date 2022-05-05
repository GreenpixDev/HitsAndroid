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
    }

    fun goMainActivity(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun goBluePrintActivity(view: View){
        val intent = Intent(this, BluePrintActivity::class.java)
        startActivity(intent)
    }
}