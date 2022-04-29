package ru.hits.android.axolot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.R.layout.activity_second
import ru.hits.android.axolot.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater).also { setContentView(activity_second) }

    }
}