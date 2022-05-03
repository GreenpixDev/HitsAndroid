package ru.hits.android.axolot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hits.android.axolot.databinding.ActivitySecondBinding
import ru.hits.android.axolot.view.BlockView

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.secondLayout)

        binding.addBlock.setOnClickListener() {
            binding.secondLayout.addView(BlockView(this, null))
        }
    }
}